/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JProgressBar;

/**
 *
 * @author j2cf
 */
public class GitProject{
	
	final private File projectDirectory;
	final private String totalOfCommits;
	final private String dateOfLastCommit;
	final private String totalOfMerges;
	final private String totalOfReleases;
	final private List<String> listOfBranches;
	
	public GitProject(File projectDirectory){
		
		this.projectDirectory = projectDirectory;
		BasicEvaluator basicDatas = new BasicEvaluator(projectDirectory);
		this.totalOfCommits = basicDatas.getCommits();
		this.totalOfMerges = basicDatas.getTotalOfMerges();
		this.dateOfLastCommit = basicDatas.getDate();
		this.totalOfReleases = String.valueOf(basicDatas.getTags().size());
		this.listOfBranches = basicDatas.getBranches();
	}

	/**
	 * @return the totalOfCommits
	 */
	public String getTotalOfCommits() {
		return totalOfCommits;
	}

	/**
	 * @return the dateOfLastCommit
	 */
	public String getDateOfLastCommit() {
		return dateOfLastCommit;
	}

	/**
	 * @return the totalOfMerges
	 */
	public String getTotalOfMerges() {
		return totalOfMerges;
	}

	/**
	 * @return the lastRelease
	 */
	public String getTotalOfReleases() {
		return totalOfReleases;
	}

	/**
	 * @return the listOfBranches
	 */
	public List<String> getListOfBranches() {
		return listOfBranches;
	}

	/**
	 * @return the projectDirectory
	 */
	public File getProjectDirectory() {
		return projectDirectory;
	}
	
	public List<MergeBranches> getMerges(){
		List<String> hashMerges = CommandLine.getMultiplesResults("git log --merges --pretty=%H", this.projectDirectory);
		List<MergeBranches> merges = new ArrayList<MergeBranches>();
		
		for (String hashMerge : hashMerges){
			MergeBranches mergeTemp = new MergeBranches(hashMerge, this);
/*			String hashParents = CommandLine.getSingleResult("git log --pretty=%P -n 1 " + hashMerge, this.projectDirectory);
			mergeTemp.setMergeBase(CommandLine.getSingleResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], this.projectDirectory));
			mergeTemp.setParent1(hashParents.split(" ")[0]);
			mergeTemp.setParent2(hashParents.split(" ")[1]); */
			merges.add(mergeTemp);
		}
		
		return merges;
	}
	
	public void setMergeDetails(MergeBranches merge){
			String hashParents = CommandLine.getSingleResult("git log --pretty=%P -n 1 " + merge.getCommitHash(), this.projectDirectory);
			merge.setMergeBase(CommandLine.getSingleResult("git merge-base " + hashParents.split(" ")[0] + " " + hashParents.split(" ")[1], this.projectDirectory));
			merge.setParent1(hashParents.split(" ")[0]);
			merge.setParent2(hashParents.split(" ")[1]);
	}
	
	
	
	
	
}
