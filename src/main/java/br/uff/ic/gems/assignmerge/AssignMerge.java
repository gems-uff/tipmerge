/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * This program only show the statistics about project commits
 */
package br.uff.ic.gems.assignmerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
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
        String output = "D:\\Dropbox\\Doutorado\\assignmerge\\Outputs";
        //test
        StringBuffer authorsList = new StringBuffer();
        
        List<String> mergeRevisions = getMergeRevisions(repositoryPath);
        List<Committer> allCommiters = new ArrayList<Committer>();
        
        int branchesMerge = 0;
        int count = 1;
        int position = 1;
        int size = mergeRevisions.size();
        int i = 1;      
        for (String merge : mergeRevisions) {
            authorsList.append("Merge " + i++ +": " + merge);
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
            
            //showCommittersStatistics(committersOnBranch1, committersOnBranch2, Standard Deviation, mean);

            //checks the amount of authors that commits in both branchs
            Integer commom = getCommomAuthors(committersOnBranch1,committersOnBranch2);
            
            // search the amount of commits and the authors in the branch
            int commitsInBrach = getCommitsInBranch(logOneline1), 
                authorsInBranch = committersOnBranch1.size();
            Double average = (double)commitsInBrach/authorsInBranch;
           
            String averageTxt = new DecimalFormat("#.##").format(average);
            System.out.println (averageTxt);
            //Printing on the File
            authorsList.append("\t" + commit1 + "\t " +commitsInBrach + "\t" + authorsInBranch + "\t" +  averageTxt);
            
            //shows the number of commits per author in each branch
            String commitArray = "[";
            for (Committer eachOne: committersOnBranch1){
               
                commitArray += eachOne.getName() + ": " + eachOne.getCommits() + "\t";
            }commitArray += "]\t";
            
            // search the amount of commits and the authors in the branch
            commitsInBrach = getCommitsInBranch(logOneline2);
            authorsInBranch = committersOnBranch2.size();
            average = (double)commitsInBrach/authorsInBranch;
            
            averageTxt = new DecimalFormat("#.##").format(average);
            
            //Count number of Software Branches Merges (with more than two developers in each branch)
            if (committersOnBranch1.size() >= 2 && committersOnBranch2.size() >=2){
                branchesMerge++;
            }
            
            
            //Printing on the File
            authorsList.append("\t" + commit2 + "\t" + commitsInBrach + "\t" + authorsInBranch + "\t" +  averageTxt + "\t" + commom); //prints the authors in common, in the two branches
            
            //Printing on the File
            //Adds a list with the number of commits by author
            authorsList.append("\t\t" + commitArray);
            
            commitArray = "[";
            for (Committer eachOne: committersOnBranch2){
                commitArray += eachOne.getName() + ": " + eachOne.getCommits();
            }commitArray += "]";
            
            //Printing on the File
            authorsList.append(commitArray); 
            
            //<editor-fold defaultstate="collapsed" desc="Commented code for printing the authors names">
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
            
            //Print File
           //System.out.println(authorsList.toString());
            //System.out.println("Lista de Nomes: " + committersOnBranch1.toString());
            //File name saved
            
            authorsList.append("\n");

        }
        FileWriter.writeToFile(output, projectName + String.format("%1$tY-%1$tm-%1$td", new Date()) + ".txt", authorsList.toString());
        
        System.out.println("Count = " + count);
        System.out.println(count + "/" + mergeRevisions.size());
        System.out.println("Committers:    " + allCommiters.size());
        System.out.println("Mean:          " + StatisticsUtil.getMean(allCommiters));
        System.out.println("Std Deviation: " + StatisticsUtil.getStandardDeviation(allCommiters));
        System.out.println("Merges:        " + size);
        System.out.println("Branches Merges: " + branchesMerge);
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
        String output = "D:\\Dropbox\\Doutorado\\assignmerge\\Outputs";
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
            //System.out.println("Branch 1 Authors: " + authors1.size());       
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
        // String command = "git log --first-parent --pretty=format:\"%an-%ae\" " + since + ".." + until; //shows all project branches
        //String command = "git rev-list --ancestry-path " + since + ".." + until; //gleiph code
        String command = "git rev-list --ancestry-path --pretty=format:\"%an#%ae\"  " + since + ".." + until; //shows only merge branches
        
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
        

        return output;
    }

    private static void getAuthorOnline(List<String> logOneline, List<Committer> authors) {
       for (String authorDatas : logOneline){
           // separate the string with author and email in two data
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
           //separate the string with author and email in two data
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
      /*  Integer commitsNumber = 0; //put 1 to increase the commit is not coming
        for (String commit : logOneline1) {
            if (commit.contains("commit"))
                commitsNumber++;
        }
        return commitsNumber;
        */ 
    }

    /* return
     * 0 - nobody in common
     * 1 - there is in common
     * 2 - all in common
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
            //System.out.println("0 - nobody in common");
            return 0;
        }else if ((number == authors1.size()) && (number == authors2.size())){
            //System.out.println("2 - all in common");
            return 2;
        }else{
            //System.out.println("1 - There is common");
            return 1;
        }
       
    }

    private static void showCommittersStatistics(List<Committer> committersOnBranch1) {
        System.out.println("Statistics of Authors");
        System.out.println("Total Authors:\t" + committersOnBranch1.size());
        System.out.println("Commits Mean:\t" + StatisticsUtil.getMean(committersOnBranch1) );
        System.out.println("Standard Deviation   :\t" + StatisticsUtil.getStandardDeviation(committersOnBranch1));
        System.out.println("All values:\t" + committersOnBranch1.toString());
    }
}
