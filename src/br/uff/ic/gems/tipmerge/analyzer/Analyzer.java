/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.experiment.Git;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjcfigueiredo
 */
public class Analyzer {

    private String projURL;
    private File repoProject;

    public Analyzer(String url) {

        projURL = url;
    }

    public boolean saveToFile(String folderName) {

        try {
			String fileName = projURL.split("github.com/")[1].replace("/", "_");
            File outputFile = new File(folderName + "out_" + fileName + ".txt");
            BufferedWriter bwOutput = new BufferedWriter(new FileWriter(outputFile, true));

            String destination = folderName + fileName + "/";
            repoProject = new File(destination);

            Git.cloneFromURL(projURL, destination);

            bwOutput.write(destination + "\n");

            List<String> merges = RunGit.getListOfResult("git log --merges --all --pretty=%H ", repoProject);
            int totalofMerges = merges.size();
            bwOutput.write("Project URL:\t" + projURL + "\n");
            bwOutput.write("All merges:\t" + totalofMerges + "\n");

            Map<Committer, Integer> mjClass = new HashMap<>();

            for (String hashMerge : merges) {

                Committer committer = CommitterDao.getCommitter1(hashMerge, repoProject);
                if (mjClass.get(committer) == null) {
                    mjClass.put(committer, 1);
                } else {
                    mjClass.put(committer, mjClass.get(committer) + 1);
                }

            }

            List<String> mjlist = new ArrayList<>();
            mjClass.keySet().stream().forEach((cmter) -> {
                double number = (double)mjClass.get(cmter);
                Double percent =  (double)mjClass.get(cmter) / totalofMerges;
                //System.out.println(percent + " = " + mjClass.get(cmter) + " / " + totalofMerges);
                mjlist.add(mjClass.get(cmter) + "\t" + percent + "\t\t" + cmter.getName());
            });

            Collections.sort(mjlist);

            for (String author : mjlist) {
                bwOutput.write(author + "\n");
            }

            bwOutput.close();

            System.out.println("Delete project " + destination);
			Process exec = Runtime.getRuntime().exec("rm -rf " + destination);

            return true;

        } catch (IOException ex) {
            System.out.println("Fail to create output file");
            Logger.getLogger(MajorityClassVerification.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
