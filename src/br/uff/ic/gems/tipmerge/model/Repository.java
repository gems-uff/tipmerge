/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import br.uff.ic.gems.tipmerge.util.Auxiliary;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has a lot of information about one project (repository), as: number of commits, last commit, merges, branches, authors...
 * @author j2cf, Catarina
 */
public class Repository {
	
	private final File project;
	private Long commits;
	private LocalDateTime lastCommit;
	private String firstCommit;
	private List<String> listOfMerges;
	private List<String> branches;	
	private List<Committer> committers;
	
	public Repository(File projectPath){
		this.project = projectPath;
	}
	/**
	 * @return the project
	 */
	public File getProject() {
		return project;
	}

	/**
	 * @return the commits
	 */
	public Long getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(Long commits) {
		this.commits = commits;
	}

	/**
	 * @return the firstCommit
	 */
	public String getFirstCommit() {
		return firstCommit;
	}

	/**
	 * @param firstCommit the firstCommit to set
	 */
	public void setFirstCommit(String firstCommit) {
		this.firstCommit = firstCommit;
	}


	/**
	 * @return the branches
	 */
	public List<String> getBranches() {
		return branches;
	}

	/**
	 * @param branches the branches to set
	 */
	public void setBranches(List<String> branches) {
		this.branches = branches;
	}

	/**
	 * @return the lastCommit
	 */
	public LocalDateTime getLastCommit() {
		return lastCommit;
	}

	/**
	 * @return the lastCommit
	 */
	public String getName() {
		return project.getName();
	}
	
	/**
	 * @param lastCommit the lastCommit to set
	 */
	public void setLastCommit(LocalDateTime lastCommit) {
		this.lastCommit = lastCommit;
	}

	/**
	 * @return the listOfMerges
	 */
	public List<String> getListOfMerges() {
            int i=1;
            List<String> listOfMerges2 = new ArrayList<>();
            for(String s:listOfMerges){
                listOfMerges2.add(s+" "+"["+i+"]");
                i++;
            }
		return listOfMerges2;
	}

	/**
	 * @param listOfMerges the listOfMerges to set
	 */
	public void setListOfMerges(List<String> listOfMerges) {
		this.listOfMerges = listOfMerges;
	}

	/**
	 * @return the committers
	 */
	public List<Committer> getCommitters() {
		return committers;
	}

	/**
	 * @param committers the committers to set
	 */
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}
	
	//this method add committer without repetition in the main screen
	public void setCommittersFromString(List<String> authors){
		List<Committer> allcmter = Auxiliary.getCommittersFromString(authors);
		this.committers = new ArrayList<>();
		
		for (Committer cmterInMainScreen : allcmter){
			Auxiliary.addOnlyNew(this.committers, cmterInMainScreen);
		}
	}
        
	
	
}
