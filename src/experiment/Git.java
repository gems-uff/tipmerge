/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j2cf
 */
public class Git {
	public static List<String> checkout(String repositoryPath, String revision) {
//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git checkout " + revision;
		
		System.out.println("\tCommand checkout: " + command);

        List<String> output = new ArrayList<String>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                String[] split = s.split(" ");
                for (String rev : split) {
                    output.add(rev);
                }

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static List<String> merge(String repositoryPath, String revision, boolean commit, boolean fastForward) {
//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git merge ";

        if (!commit) {
            command = command + " --no-commit ";
        }

        if (!fastForward) {
            command = command + "--no-ff ";
        }

        command = command + revision;
		System.out.println("\tCommand merge: " + command);

        List<String> output = new ArrayList<String>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
//                String[] split = s.split(" ");
//                for (String rev : split) {
//                    output.add(rev);
//                }
                output.add(s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }
	
	public static String getMergeBase(String repositoryPath, String commit1, String commit2) {

//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git merge-base " + commit1 + " " + commit2;

        String output = null;


        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output = s;

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }
	
	
	
}
