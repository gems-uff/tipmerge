/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge;

import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.experiment.Experiment;
import br.uff.ic.gems.tipmerge.model.Repository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author jjcfigueiredo
 */
public class MergesVerification {

    public static void main(String[] args) throws IOException {

        //String dir = "c:/testes/imagem2";
        File inputFolder = new File("projects");

        File fList[] = inputFolder.listFiles();

        System.out.println("Number of available projetcs: " + fList.length);

        for (File file : fList) {
            if (!file.getName().startsWith(".")) {
                //System.out.println(file.getName() + " " + new Date(file.lastModified()));

                File outputLocation = new File("Dropbox/results/" + file.getName() + ".txt");

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputLocation, true));
                
                bufferedWriter.write("Project\t" + file.getName() + "\n");
                
                bufferedWriter.close();
                
                Experiment experimentMerges = new Experiment(getRepositoryInfo(file));
                //Map<String, Integer> result = experimentMerges.getDatasFromMerges(outputLocation);
                Map<String, Integer> result = experimentMerges.getDatasFromMerges();
                int rankingTotal = result.get("1stPosition") + result.get("2ndPosition") + result.get("3thPosition") + result.get("isInRank") + result.get("outOfRank");

                //  System.out.println(result.toString());
                //result.put("Merges", 0);
                System.out.println("Number of merges with Enough developers " + result.get("Merges"));
                System.out.println("Number of merges with changed files in both branches " + result.get("Files"));
                System.out.println("Number of merges with dependencies across branches " + result.get("Dependencies"));
                System.out.println("Number of merges with conflicts " + result.get("Conflicts"));
                System.out.println("Number of merges with raking " + rankingTotal);
                System.out.println("Matched with 1st Position " + result.get("1stPosition"));
                System.out.println("Matched with 2nd Position " + result.get("2ndPosition"));
                System.out.println("Matched with 3th Position " + result.get("3thPosition"));
                System.out.println("Matched with other Position " + result.get("isInRank"));
                System.out.println("Out of the Ranking " + result.get("outOfRank"));

                bufferedWriter = new BufferedWriter(new FileWriter(outputLocation, true));

                bufferedWriter.write("Number of merges with Enough developers " + result.get("Merges") + "\n");
                bufferedWriter.write("Number of merges with changed files in both branches " + result.get("Files") + "\n");
                bufferedWriter.write("Number of merges with dependencies across branches " + result.get("Dependencies") + "\n");
                bufferedWriter.write("Number of merges with conflicts " + result.get("Conflicts") + "\n");
                bufferedWriter.write("Number of merges with raking " + rankingTotal + "\n");
                bufferedWriter.write("Matched with 1st Position " + result.get("1stPosition") + "\n");
                bufferedWriter.write("Matched with 2nd Position " + result.get("2ndPosition") + "\n");
                bufferedWriter.write("Matched with 3th Position " + result.get("3thPosition") + "\n");
                bufferedWriter.write("Matched with other Position " + result.get("isInRank") + "\n");
                bufferedWriter.write("Out of the Ranking " + result.get("outOfRank"));

                bufferedWriter.close();

            }
        }

    }

    private static Repository getRepositoryInfo(File file) {
        System.out.println("\n+----- Analyzing the project -----+" + file.getName());
        try {

            Repository repository = new RepositoryDao(file).getRepository();
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
