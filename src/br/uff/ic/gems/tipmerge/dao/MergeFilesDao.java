
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;

/**
 * This class gets the information about the merge - commits on Branch1, on Branch2 e all history before the branch
 * @author j2cf, Catarina
 */
public class MergeFilesDao {
    
	//get the commits on Branch1 (base to parent 1) and on Branch2 (base to parent 2)
    public MergeFiles getMerge(String hash, File path){
        MergeFiles merge = new MergeFiles(hash, path);
        String hashParents = RunGit.getResult("git log --pretty=%P -n 1 " + merge.getHash(), path);
		merge.setHashBase(this.getMergeBase(hashParents.split(" ")[0], hashParents.split(" ")[1], path));
        //merge.setHashBase(RunGit.getResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], path));
        merge.setParents(hashParents.split(" ")[0],hashParents.split(" ")[1]);
        return merge;
    }
	
	//get the commit base from two commits
	public String getMergeBase(String parent1, String parent2, File path){
		 return RunGit.getResult("git merge-base " + parent1 + " " + parent2, path);
	}
	
}
