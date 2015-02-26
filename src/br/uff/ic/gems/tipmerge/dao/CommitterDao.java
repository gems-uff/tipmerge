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
 *
 * @author j2cf
 */
public class CommitterDao {


	public Committer getCommitter(String hash, Repository repository){
		String datas = RunGit.getResult("git show --format=\"%an#%ae \" " + hash, repository.getProject());
		Committer cmt = new Committer(datas.split("#")[0], datas.split("#")[1]);
		return cmt;
	}
        
         public List<Committer> getWhoEditedFile (String base, String parent, String file, File path){
                String command = "git shortlog -sne " + base + ".." + parent + " " + file;
                List<String> committerList = RunGit.getListOfResult(command, path);
                return getCommittersFromString(committerList);
        }
	
	public List<Committer> getCommittersList(String mergeBase, String mergeTarget, File path){
		List<String> committerList = 
				RunGit.getListOfResult("git shortlog -sne " + mergeBase + ".." + mergeTarget, path);

		return getCommittersFromString(committerList);
	}

        private List<Committer> getCommittersFromString(List<String> committerList) throws NumberFormatException {
            List<Committer> cmtList = new ArrayList<>();
            for(String line : committerList){
                String[] datas = Auxiliary.getSplittedLine(line);
                Committer committer = new Committer(datas[0], datas[1]);
                committer.setCommits(Integer.valueOf(datas[2]));
                cmtList.add(committer);
            }
            return cmtList;
        }

}
