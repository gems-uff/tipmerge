/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

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
public class RunGit {
	
	public static String getResult(String command, File repository) {
		BufferedReader stdInput = RunGit.executeGitCommand(command, repository);
		try {
			return stdInput.readLine();
		} catch (IOException ex) {
			System.out.println(ex);
			Logger.getLogger(RunGit.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	public static List<String> getListOfResult(String command, File repository) {
		List<String> result = new ArrayList<>();
		String line;
		BufferedReader stdInput = RunGit.executeGitCommand(command, repository);
		try {
			while ((line = stdInput.readLine()) != null) {
				result.add(line);
			}
		} catch (IOException ex) {
			System.out.println(ex);
			Logger.getLogger(RunGit.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;
	}
	
	
	private static BufferedReader executeGitCommand(String command, File file){
		BufferedReader stdInput = null;
		String error;
		try {
			Process exec = Runtime.getRuntime().exec(command, null, file);

			stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

			while ((error = stdError.readLine()) != null) {
				System.out.println(error);
			}

		} catch (IOException e) {
			
		}
		
		return stdInput;
	}
	

	
}
