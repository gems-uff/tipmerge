/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.analyzer.git.GitRepository;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * 1- com o tempo a acurácia é maior, menor (alocamos quem de fato fez o merge e
 * quais as posições do ranking)? Como seria esse gráfico de cada projeto.
 * Podemos verificar também sobre o último a commitar, qual a incidência ao
 * longo do tempo. - tempo absoluto – (1 mês de projeto – no caso de pegar todos
 * os projetos juntos) - Se for usar vários projetos, usar alguma visualização
 * no R: http://revolution-
 * computing.typepad.com/.a/6a010534b1db25970b01bb07f03990970d-800wi - olhar a
 * tese do luiz laerte, ele tem algo assim.
 */
public class AccuracyToTime {

    public static void main(String[] args){

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm");

        JFileChooser projectChooser = selectGitProject("Select Git Project Folder");
        System.out.println("You chose to open this directory: " + projectChooser.getSelectedFile().getAbsolutePath());

        File targetFile = new File(System.getProperty("user.home") 
                + "/dev/java-projects/gems/results/AccuracyToTime-" 
                + projectChooser.getSelectedFile().getName() + "-" + dateFormat.format(new Date()) + ".txt");
        //String outputFile = "/AccuracyToTime-" + dateFormat.format(new Date()) + ".txt";
        //JFileChooser saveToChooser = selectGitProject("Select Destination Folder");
        //File targetFile = new File(saveTo.getAbsolutePath() + outputFile);
        System.out.println("You chose save to: " + targetFile.getAbsolutePath());

        //sendToFile(targetFile, "Analyzing the project\t" + projectChooser.getSelectedFile().getName());

        File repo = projectChooser.getSelectedFile();
        List<String> mergesList = GitRepository.getMerges(repo);
        mergesList.forEach((merge) -> {
            String hash = merge.split("\t")[0];
            String date = merge.split("\t")[1];
            System.out.println("\nMerge:\t" + merge);
            String firstH = GitRepository.getFirstHash(repo, hash);
            RankingGenerator rankingGen = EasyRankingGenerator.getRankingGen(repo, hash);
            //MergeFiles mergeFiles = getMerge(repo, firstH, hash);
            //List<Dependency> dependencies = getFilesDependencies(repo, mergeFiles, firstH);
            //System.out.println("\tGerando Ranking....");
            //RankingGenerator rankingGen = getRankingGen(mergeFiles, dependencies);
            if (rankingGen != null) {
                Committer commiter = GitRepository.getWhoMerge(repo, hash);
                Medalist cmtMedalist = new Medalist(commiter);
                List<Medalist> ranking = rankingGen.getRanking();
                System.out.println("\t" + ranking);
                int i = 1; int position = 0;
                for(Medalist medalist : ranking){
                    if(medalist.equals(cmtMedalist)){
                        position = i;
                        break;
                    }
                    i++;
                }

                sendToFile(targetFile, merge + "\t" + commiter + "\t" + position);                

            }
        });

    }

    private static void sendToFile(File targetFile, String content) {
        System.out.println(content);
        try (PrintWriter dataDestination = new PrintWriter(new BufferedWriter(new FileWriter(targetFile, true)))) {
            dataDestination.println(content);
        } catch (IOException ex) {
            Logger.getLogger(AccuracyToTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static JFileChooser selectGitProject(String message) {

        String userDir = System.getProperty("user.home");
        JFileChooser projectChooser = new JFileChooser(new File(userDir + "/dev/java-projects/gems/clones"));
        projectChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        projectChooser.setDialogTitle(message);
        int selectedOption = projectChooser.showOpenDialog(null);
        if (selectedOption == JFileChooser.APPROVE_OPTION) {
            return projectChooser;
        }
        System.exit(0);
        return null;

    }

}
