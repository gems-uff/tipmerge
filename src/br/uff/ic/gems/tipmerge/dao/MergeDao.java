/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Merge;
import br.uff.ic.gems.tipmerge.util.Auxiliary;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class MergeDao {
	
	private final File path;

	public MergeDao(File path) {
		this.path = path;
	}
	
	public void teste(){
//		RuntimeFactory execute = RuntimeFactory.getInstance();
	}

	public List<Merge> getMerges() {
		
		List<String> mergesHashes = RunGit.getListOfResult("git log --merges --pretty=%H", path);
		List<Merge> merges = new ArrayList<>();
		
		mergesHashes.stream().forEach((hashOfMerge) -> {
			merges.add(new Merge(hashOfMerge,path));
		});
		
		return merges;
	}

	public void update(Merge merge) {
		String hashParents = RunGit.getResult("git log --pretty=%P -n 1 " + merge.getHash(), this.path);
		merge.setHashBase(RunGit.getResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], this.path));
		merge.setParents(hashParents.split(" ")[0],hashParents.split(" ")[1]);
		setCommiters(merge);
		
	}

	public void setCommiters(Merge merge) {
		merge.setCommittersBranchOne(
			this.getCommitters(
				RunGit.getListOfResult(
					"git shortlog -sne " + merge.getHashBase() + ".." + merge.getParents()[0], merge.getPath())));
		merge.setCommittersBranchTwo(
			this.getCommitters(
				RunGit.getListOfResult(
					"git shortlog -sne " + merge.getHashBase() + ".." + merge.getParents()[1], merge.getPath())));
		setCommittersBeforeMerge(merge);
	}
	
	private List<Committer> getCommitters(List<String> committerList) {
		List<Committer> cmtList = new ArrayList<>();
		for(String line : committerList){
			String[] datas = Auxiliary.getSplittedLine(line);
			Committer committer = new Committer(datas[0], datas[1]);
			committer.setCommits(Integer.valueOf(datas[2]));
			cmtList.add(committer);
		}
		return cmtList;
	}
	
	private void setCommittersBeforeMerge(Merge merge){
		//insere a informação do primeiro commit
		String firstHash = RunGit.getResult("git rev-list --max-parents=0 HEAD", merge.getPath());	
		merge.setCmtBeforeMerge(this.getCommitters(RunGit.getListOfResult("git shortlog -sne "  + firstHash + ".." +  merge.getHashBase(), merge.getPath())));
	}
	
}
