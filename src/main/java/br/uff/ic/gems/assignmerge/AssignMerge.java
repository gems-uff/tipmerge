/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author catarinacosta
 */
public class AssignMerge {

   
    public static void main(String[] args) {
        
        //printMergesAndNames();
        printStatistics();

    }
    
    public static void printStatistics(){
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File f = new File("D:\\repositorio\\");
        file.setSelectedFile(f);
        file.showSaveDialog(null);
        File project = file.getSelectedFile();
        String projectName = project.getName();
        String repositoryPath = project.toString();
        String output = "C:\\Users\\Catarina\\Dropbox\\Doutorado\\Kraken\\OutputAuthorsInBranch";
        //teste
        String authorsList;
        
        List<String> mergeRevisions = getMergeRevisions(repositoryPath);
        List<Committer> allCommiters = new ArrayList<Committer>();
        
        int count = 1;
        int position = 1;
        int size = mergeRevisions.size();
        int i = 1;      
        for (String merge : mergeRevisions) {
            authorsList = "Merge " + i++ +": " + merge;
           // List<CommitAuthor> authors1 = new ArrayList<CommitAuthor>();
            //List<CommitAuthor> authors2 = new ArrayList<CommitAuthor>();
            if (position % 100 == 0) {
                System.out.println("Analising " + position + "/" + size);
                System.out.println(new Date());
            }
            ++position;
            List<String> parents = getParents(repositoryPath, merge);
            String commit1 = null, commit2 = null;
            for (String parent : parents) {
                if (commit1 == null) {
                    commit1 = parent;
                } else if (commit2 == null) {
                    commit2 = parent;
                }
            }
            String mergeBase = getMergeBase(repositoryPath, commit1, commit2);
            List<String> logOneline1 = logOneline(repositoryPath, mergeBase, commit1);
            List<String> logOneline2 = logOneline(repositoryPath, mergeBase, commit2);
            
            List<Committer> committersOnBranch1 = getAuthorsInBranch(allCommiters, logOneline1), 
                            committersOnBranch2 = getAuthorsInBranch(allCommiters, logOneline2);
            
            //mostra os dados dos autores incluindo media e desvio padrao de commits por ramos em cada merge
            //showCommittersStatistics(committersOnBranch1);

            //verifica a quantidade de autores efetuaram commit em ambos os branchs
            Integer commom = getCommomAuthors(committersOnBranch1,committersOnBranch2);
            
            // busca a quantidade de commits no branch e a quantidade de autores
            int commitsInBrach = getCommitsInBranch(logOneline1), 
                authorsInBranch = committersOnBranch1.size();
            Double average = (double)commitsInBrach/authorsInBranch;
            authorsList += "\t" + commit1 + "\t " +commitsInBrach + "\t" + authorsInBranch + "\t" +  average;
            
            //mostra a quantidade de commits por autor em cada branch
            String commitArray = "[";
            for (Committer eachOne: committersOnBranch1){
                //commitArray += "\t" + eachOne.getCommits() + ":" + eachOne.getName();
                commitArray += eachOne.getName() + ": " + eachOne.getCommits() + "\t";
            }commitArray += "]\t";
            // busca a quantidade de commits no branch e a quantidade de autores
            commitsInBrach = getCommitsInBranch(logOneline2);
            authorsInBranch = committersOnBranch2.size();
            average = (double)commitsInBrach/authorsInBranch;
            authorsList += "\t" + commit2 + "\t" + commitsInBrach + "\t" + authorsInBranch + "\t" +  average + "\t" + commom; //imprime os autores em comum, nos dois ramos
            //acrescenta uma lista com a quantidade de commits por autor
            authorsList += "\t\t" + commitArray; 
            commitArray = "[";
            for (Committer eachOne: committersOnBranch2){
                commitArray += eachOne.getName() + ": " + eachOne.getCommits();
            }commitArray += "]";
            authorsList += commitArray; 
            
            //<editor-fold defaultstate="collapsed" desc="Código comentado para impressao de nomes">
            /**
             * getAuthorOnline(logOneline1, authors1);
             * getAuthorOnline(logOneline2, authors2);
             * //System.out.println("Trunk 1 Authors: " + authors1.size());
             * authorsList += "Branch 1 \tAuthors: " + authors1.size();
             * 
             * authorsList += "Branch 1 \tAuthors: " + authors1.size();
             * int countCommits = 0;
             * String str = "";
             * for (Committer cmt : authors1) {
             * str += " - " + cmt.toString() + "\n";
             * countCommits += cmt.getCommits();
             * }
             * authorsList += "\tCommits: " + countCommits + "\n";
             * authorsList += str;
             * authorsList += "Branch 2 \tAuthors: " + authors2.size();
             * countCommits = 0;
             * str = "";
             * for (Committer cmt : authors2) {
             * str += " - " + cmt.toString() + "\n";
             * countCommits += cmt.getCommits();
             * }
             * authorsList += "\tCommits: " + countCommits + "\n";
             * authorsList += str;
             * //          String line = mergeBase + ", " + commit1 + ", " + logOneline1.size() + ", " + commit2 + ", " + logOneline2.size();
             */
//</editor-fold>
            
            System.out.println(authorsList);
            //System.out.println("Lista de Nomes: " + committersOnBranch1.toString());
            FileWriter.writeToFile(output, projectName + String.format("%1$tY-%1$tm-%1$td", new Date()) + ".txt", authorsList);

        }
        System.out.println("Count = " + count);
        System.out.println(count + "/" + mergeRevisions.size());
        System.out.println("Committers:    " + allCommiters.size());
        System.out.println("Mean:          " + StatisticsUtil.getMean(allCommiters));
        System.out.println("Std Deviation: " + StatisticsUtil.getStandardDeviation(allCommiters));
        System.out.println("Merges:        " + size);
    }
    
    public static void printMergesAndNames(){

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File f = new File("D:\\repositorio");
        file.setSelectedFile(f);
        file.showSaveDialog(null);
        File project = file.getSelectedFile();
        String projectName = project.getName();
        String repositoryPath = project.toString();
        String output = "D:\\Dropbox\\Doutorado\\GitDataExtractor\\Outputs";
        String authorsList;

        List<String> mergeRevisions = getMergeRevisions(repositoryPath);
     //   System.out.println("mergerevisions: " + mergeRevisions.toString());
        int count = 1;
        int position = 1;
        int size = mergeRevisions.size();
        int i = 0;      
        for (String merge : mergeRevisions) {
            //System.out.println("\nMerge " + i++ +": " + merge);
            authorsList = "\nMerge " + i++ +": " + merge + "\n";
            List<Committer> authors1 = new ArrayList<Committer>();
            List<Committer> authors2 = new ArrayList<Committer>();
            if (position % 100 == 0) {
                System.out.println("Analising " + position + "/" + size);
                System.out.println(new Date());
            }
            ++position;
            List<String> parents = getParents(repositoryPath, merge);
            String commit1 = null, commit2 = null;
            for (String parent : parents) {
                if (commit1 == null) {
                    commit1 = parent;
                } else if (commit2 == null) {
                    commit2 = parent;
                }

            }
            String mergeBase = getMergeBase(repositoryPath, commit1, commit2);
            List<String> logOneline1 = logOneline(repositoryPath, mergeBase, commit1);
            List<String> logOneline2 = logOneline(repositoryPath, mergeBase, commit2);
            getAuthorOnline(logOneline1, authors1);
            getAuthorOnline(logOneline2, authors2);
            //System.out.println("Trunk 1 Authors: " + authors1.size());       
            authorsList += "Branch 1 \tAuthors: " + authors1.size();
            int countCommits = 0;
            String str = "";
            for (Committer author : authors1) {
                 str += " - " + author.toString() + "\n";
                 countCommits += author.getCommits();
            }
            authorsList += "\tCommits: " + countCommits + "\n";
            authorsList += str;
            authorsList += "Branch 2 \tAuthors: " + authors2.size();
            countCommits = 0;
            str = "";
            for (Committer author : authors2) {
               str += " - " + author.toString() + "\n";
               countCommits += author.getCommits();
            }           
            authorsList += "\tCommits: " + countCommits + "\n";
            authorsList += str;
  //          String line = mergeBase + ", " + commit1 + ", " + logOneline1.size() + ", " + commit2 + ", " + logOneline2.size();
              System.out.println(authorsList);

            FileWriter.writeToFile(output, projectName + String.format("%1$tY-%1$tm-%1$td", new Date()) + ".txt", authorsList);

        }
        System.out.println("Count = " + count);
        System.out.println(count + "/" + mergeRevisions.size());
    }        


    public static List<String> getMergeRevisions(String repositoryPath) {
        String command = "git log --merges --pretty=%H";
       
        List<String> output = new ArrayList<String>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));
//            exec.waitFor();

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output.add(s);

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
            System.out.println(command + ' ' + repositoryPath);
            //Logger.getLogger(Explorer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static List<String> getParents(String repositoryPath, String revision) {
        String command = "git log --pretty=%P -n 1 " + revision;


        List<String> output = new ArrayList<String>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));
//            exec.waitFor();

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                String[] split = s.split(" ");
                output.addAll(Arrays.asList(split));

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
//            Logger.getLogger(Explorer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static String getMergeBase(String repositoryPath, String commit1, String commit2) {
        String command = "git merge-base " + commit1 + " " + commit2;
//                System.out.println(command);

        String output = null;

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));
//            exec.waitFor();

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output = s;
//                System.out.println("\t"+s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
//            Logger.getLogger(Explorer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static List<String> logOneline(String repositoryPath, String since, String until) {
        // committers names and emails
      //  String command = "git log --first-parent --pretty=format:\"%an-%ae\" " + since + ".." + until;//pega todos os ramos envolvidos
        //String command = "git rev-list --ancestry-path " + since + ".." + until;
        String command = "git rev-list --ancestry-path --pretty=format:\"%an#%ae\"  " + since + ".." + until;
        
        //System.out.println(command);

        List<String> output = new ArrayList<String>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
        }
        //System.out.println("Nro de Posicoes Vetor de Saida: " + output.size());
        //System.out.println("Conteudo do Vetor: " + output.toString());

        return output;
    }

    private static void getAuthorOnline(List<String> logOneline, List<Committer> authors) {
       for (String authorDatas : logOneline){
           //separa a string com autor e e-mail em dois dados
           String datas[] = authorDatas.split("-");
           boolean isNew = true;
           for (Committer author : authors){
               if (     (author.getName().equals(datas[0].toUpperCase())) || 
                        (author.getEmail().equals(datas[1].toLowerCase())) 
                   ){
                   author.addCommit();
                   isNew = false;
                   break;
               }
           }
           if (isNew)
               authors.add(new Committer(datas[0].toUpperCase(), datas[1].toLowerCase()));
       }
   }
    
    private static List<Committer> getAuthorsInBranch(List<Committer> allCommitters, List<String> logOneline) {
        
        List <Committer> committers = new ArrayList<Committer> ();
        for (int i = 1; i<logOneline.size(); i=i+2){
           //separa a string com autor e e-mail em dois dados
           String commit = logOneline.get(i);
           String datas[] = commit.split("#");
           boolean isNew = true;
           for (Committer cmt : committers){
               if (     (cmt.getName().equals(datas[0].toLowerCase())) || 
                        (cmt.getEmail().equals(datas[1].toLowerCase())) 
                   ){
                   cmt.addCommit();
                   isNew = false;
                   break;
               }
           }
           if (isNew){
               Committer newCmt = new Committer(datas[0].toUpperCase(), datas[1].toLowerCase());
               committers.add(newCmt);
                isNew = true;
                for (Committer cmt : allCommitters){
                    if (cmt.compareTo(newCmt) == 1){
                        cmt.addCommit(newCmt.getCommits());
                        isNew = false;
                        break;
                    }
                }
                if (isNew){
                    allCommitters.add(newCmt.clone());
                }
           }
       }
       return committers;
    }

    private static int getCommitsInBranch(List<String> logOneline1) {
        return logOneline1.size()/2;
      /*  Integer commitsNumber = 0; //botar 1 para incrementar o commit que não ta vindo
        for (String commit : logOneline1) {
            if (commit.contains("commit"))
                commitsNumber++;
        }
        return commitsNumber;
        */ 
    }

    /* retorno
     * 0 - ninguem em comum
     * 1 - há em comum
     * 2 - todos em comum
     */
    private static Integer getCommomAuthors(List<Committer> authors1, List<Committer> authors2) {
        Integer number = 0;
        for (Committer author1 : authors1){
            for (Committer author2 : authors2){
                if (author1.compareTo(author2) == 1){
                    number++;
                }
            }
        }
        if (number == 0){
            //System.out.println("0 - ninguem em comum");
            return 0;
        }else if ((number == authors1.size()) && (number == authors2.size())){
            //System.out.println("2 - todos em comum");
            return 2;
        }else{
            //System.out.println("1 - há em comum");
            return 1;
        }
        //return number;
    }

    private static void showCommittersStatistics(List<Committer> committersOnBranch1) {
        System.out.println("Estatística de Autores");
        System.out.println("Total de Autores:\t" + committersOnBranch1.size());
        System.out.println("Média de Commits:\t" + StatisticsUtil.getMean(committersOnBranch1) );
        System.out.println("Desvio Padrão   :\t" + StatisticsUtil.getStandardDeviation(committersOnBranch1));
        System.out.println("Todos os valores:\t" + committersOnBranch1.toString());
    }
}
