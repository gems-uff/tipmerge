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
 *
 * @author j2cf
 */
public class MergeFilesDao {
    
    public MergeFiles getMerge(String hash, File path){
        MergeFiles merge = new MergeFiles(hash, path);
        String hashParents = RunGit.getResult("git log --pretty=%P -n 1 " + merge.getHash(), path);
        merge.setHashBase(RunGit.getResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], path));
        merge.setParents(hashParents.split(" ")[0],hashParents.split(" ")[1]);
        return merge;
    }
	
}
