/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge;

import arch.Session;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.experiment.Experiment;
import br.uff.ic.gems.tipmerge.experiment.Parameter;
import br.uff.ic.gems.tipmerge.model.Repository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjcfigueiredo
 */
public class MergeVerification {

    public static void main(String[] args) {

       // Session.startSession(0); //When using gpu
        //String dir = "c:/testes/imagem2";
        File inputFolder = new File("projects");

        File fList[] = inputFolder.listFiles();

        System.out.println("Parameters Defined");
        System.out.println("THRESHOLD\t" + Parameter.THRESHOLD);
        System.out.println("EXTENSION\t" + Parameter.EXTENSION);
        System.out.println("DATABASE\t" + Parameter.DATABASE);

        System.out.println("Available projetcs: " + Arrays.toString(fList));

        for (File file : fList) {
            if (!file.getName().startsWith(".")) {

                try {

                    System.out.println("Analyzing: " + file.getName() + " " + new Date(file.lastModified()));

                    File outputLocation = new File("results/" + file.getName() + ".txt");
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputLocation, true));
                    bufferedWriter.write("Project: " + file.getName() + "\t" + new Date(file.lastModified()) + "\n");
                    bufferedWriter.close();

                    Experiment experimentMerges = new Experiment(getRepositoryInfo(file));
                    
                    Map<String, Integer> result = experimentMerges.getDatasFromMerges(outputLocation);
                    
                } catch (IOException ex) {
                    Logger.getLogger(MergeVerification.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
      //  Session.closeSection(); //When using gpu

    }


    private static Repository getRepositoryInfo(File file) {
        System.out.println("\n+----- Analyzing the project -----+" + file.getName());
        try {

            RepositoryDao rDao = new RepositoryDao(file);
            Repository repository = rDao.getRepository();
            rDao.setDetails(repository);
            //System.out.println(repository.getCommits().toString());
            System.out.println(repository.getLastCommit().format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US)));

            System.out.println(String.valueOf(repository.getListOfMerges().size()));
            System.out.println(String.valueOf(repository.getCommitters().size()));
            return repository;
        } catch (Exception e) {
            System.out.println(
                    "Please, select a git project folder."
                    + "Inane error");
        }

        return null;

    }
}
