	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.Auxiliary;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of getting the Committers information from a repository using Gits commands
 * @author j2cf, Catarina
 */	
public class CommitterDao {

	//get the committers name and committers e-mail information	from commits
	public static Committer getCommitter1(String hash, Repository repository){
            return getCommitter1(hash, repository.getProject());
		//String datas = RunGit.getResult("git show --format=%an#%ae " + hash, repository.getProject());
		//Committer cmter = new Committer(datas.split("#")[0], datas.split("#")[1]);
		//return cmter;
	}

        //get the committers name and committers e-mail information	from commits
	public static Committer getCommitter1(String hash, File file){
		String datas = RunGit.getResult("git show --format=%an#%ae " + hash, file);
		Committer cmter = new Committer(datas.split("#")[0], datas.split("#")[1]);
		return cmter;
	}
        
    //get the committers that modified a file between two commitswithoud merged files   
	public List<Committer> getWhoEditedFile (String base, String parent, String fileName, File path){
		String command = "git shortlog -sne --no-merges " + base + ".." + parent + " -- " + fileName;
		List<String> committerList = RunGit.getListOfResult(command, path);
		return getCommittersFromString(committerList);
   }
	
	//get the committers that committed between two commits 
	public List<Committer> getCommittersList(String mergeBase, String mergeTarget, File path){
		List<String> committerList = 
				RunGit.getListOfResult("git shortlog -sne --no-merges " + mergeBase + ".." + mergeTarget, path);
		List<Committer> result = getCommittersFromString(committerList);
		return result;
	}
	
	//this metodh return one list of committers 
	private List<Committer> getCommittersFromString(List<String> committerList) throws NumberFormatException {
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
					break;
				}
			}
			if(!hasIt) cmterList.add(committer);
		}
				
		return cmterList;
	}

}
