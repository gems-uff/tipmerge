/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template fullFileName, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author j2cf
 */
public class Coverage {
	
	private String developer;
	private List<String[]> values = new ArrayList<>();

	/**
	 * @return the developer
	 */
	public String getDeveloper() {
		return developer;
	}

	/**
	 * @param developer the developer to set
	 */
	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	/**
	 * @return the values
	 */
	public List<String[]> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<String[]> values) {
		this.values = values;
	}
	
	/**
	 * @param value the values to set
	 */
	public void addValue(String[] value) {
		this.values.add(value);
	}

	@Override
	public String toString() {
		String result = "\n" + this.developer;
		
		for(String[] value : this.getValues())
			result += "\n\t" + Arrays.toString(value);
			
		return result;
	}
	
	public Map<String, Integer[]> getCoverage(Map<String, Integer[]> fileNames) {

		Integer totalMethodsFile = 0;
		
		totalMethodsFile = 0;
		for(String file : fileNames.keySet()){
			for(String[] fullFileName : this.getValues() ){
				if(fullFileName[0].equals(file)){
					totalMethodsFile++;
				}
			}
			fileNames.put(file, new Integer[]{fileNames.get(file)[0] , totalMethodsFile});
			totalMethodsFile = 0;
		}
				
		return fileNames;
	}
	
}
