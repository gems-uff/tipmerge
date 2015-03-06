/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.MergeCommits;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class gets the information about the merge - commits on Branch1, on Branch2 e all history before the branch - commit level
 * @author j2cf, Catarina
 */
public class MergeCommitsDao {
	
	private final File path;

	public MergeCommitsDao(File path) {
		this.path = path;
	}
	
	public void teste(){
//		RuntimeFactory execute = RuntimeFactory.getInstance();
	}
	// get the merges information - only hash information
	public List<MergeCommits> getMerges() {
		
		List<String> mergesHashes = RunGit.getListOfResult("git log --merges --pretty=%H", path);
		List<MergeCommits> merges = new ArrayList<>();
		
		mergesHashes.stream().forEach((hashOfMerge) -> {
			merges.add(new MergeCommits(hashOfMerge,path));
		});
		
		return merges;
	}
	//get and set the commits on Branch1 (base to parent 1) and on Branch2 (base to parent 2)
	public void update(MergeCommits merge) {
		String hashParents = RunGit.getResult("git log --pretty=%P -n 1 " + merge.getHash(), this.path);
		merge.setHashBase(RunGit.getResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], this.path));
		merge.setParents(hashParents.split(" ")[0],hashParents.split(" ")[1]);

		updateCommitters(merge);
	}

	public void updateCommitters(MergeCommits merge) {
		CommitterDao committerDao = new CommitterDao();

		merge.setCommittersBranchOne(committerDao.getCommittersList(merge.getHashBase(), merge.getParents()[0], merge.getPath()));
		merge.setCommittersBranchTwo(committerDao.getCommittersList(merge.getHashBase(), merge.getParents()[1], merge.getPath()));
	}
	
	//get and set the committers in the history before the Branch (first commit to base)
	public void setCommittersBeforeBranches(MergeCommits merge){
		//insere a informação do primeiro commit
		CommitterDao committerDao = new CommitterDao();
		String firstHash = RunGit.getResult("git rev-list --max-parents=0 HEAD", merge.getPath());	
		merge.setCommittersBeforeBranches(
			committerDao.getCommittersList(firstHash, merge.getHashBase(), merge.getPath())
		);
	}
	
	
	
	// get the commit base from two commits
	// @param parent1 (hash of some commit on branch), parent2 (hash of some commit of another branch)
	public String getMergeBase(String parent1, String parent2, File path){
		 return RunGit.getResult("git merge-base " + parent1 + " " + parent2, path);
	}

	
}
