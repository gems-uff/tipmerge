/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class CommandLine {
	
	public static List<String> getMultiplesResults(String command, File repositoryPath) {
		List<String> result = new ArrayList<String>();
		String error;
		try {
			Process exec = Runtime.getRuntime().exec(command, null, repositoryPath);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
			String partialResult = "";
			while ((partialResult = stdInput.readLine()) != null) {
				result.add(partialResult);
			}
			while ((error = stdError.readLine()) != null) {
				System.out.println(error);
			}

		} catch (IOException e) {

		}
		return result;
	}
	
	@SuppressWarnings("empty-statement")
	public static String getSingleResult(String command, File repositoryPath) {
		String result = null;
		String error, partialResult;
		try {
			Process exec = Runtime.getRuntime().exec(command, null, repositoryPath);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

			if ((result = stdInput.readLine()) != null);
			while ((error = stdError.readLine()) != null) {
				System.out.println(error);
			}

		} catch (IOException e) {
		}
		return result;
	}
	
}
