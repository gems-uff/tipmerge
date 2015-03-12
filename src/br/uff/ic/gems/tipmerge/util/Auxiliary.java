/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

import br.uff.ic.gems.tipmerge.model.Committer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf, Catarina
 */
public class Auxiliary {
	//This method separates the received string git in an array containing the name, quantity of commmits and email
	public static String[] getSplittedLine(String line){
		line = line.trim();
		String[] datas = line.split("\t");
		String[] result = {	datas[1].substring(0, datas[1].indexOf("<")), 
							datas[1].substring(datas[1].indexOf("<") + 1, datas[1].length() - 1),
							datas[0]
		};
		return result;
	}
	
		//this metodh return one list of committers 
	public static List<Committer> getCommittersFromString(List<String> committerList) throws NumberFormatException {
		List<Committer> cmterList = new ArrayList<>();
		for(String line : committerList){
			String[] datas = Auxiliary.getSplittedLine(line);
			Committer committer = new Committer(datas[0], datas[1]);
			committer.setCommits(Integer.valueOf(datas[2]));
			boolean hasIt = false;
			//check the insertion of the committers in the list 
			//identifying those that version control is not yet identified
			//TODO
			for(Committer cmter : cmterList){
				if(cmter.equals(committer)){
					cmter.setCommits(cmter.getCommits() + committer.getCommits());
					hasIt = true;
				}
			}
			if(!hasIt) cmterList.add(committer);
		}
		return cmterList;
	}
	
	//this method scroll througth list of authors and adds only new committers, avoiding repetitions. It uses the equals method that compares name and e-mail.
	public static void addOnlyNew(List<Committer> authors, Committer committer) {
		boolean exist = false;
		for (Committer cmterList : authors){
			if (committer.equals(cmterList)){
				exist = true;
				break;
			} else exist = false;
		}
		if (!exist)
			authors.add(committer);
	}
	
	
}
