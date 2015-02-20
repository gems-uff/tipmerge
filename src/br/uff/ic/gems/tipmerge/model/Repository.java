/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class Repository {
	
	private final File project;
	private Long commits;
	private LocalDateTime lastCommit;
	private String firstCommit;
	private List<Merge> merges;
	private List<String> branches;	
	private List<String> authors;
	
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
	 * @return the merges
	 */
	public List<Merge> getMerges() {
		return merges;
	}

	/**
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
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
	 * @param merges the merges to set
	 */
	public void setMerges(List<Merge> merges) {
		this.merges = merges;
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
	
	
}
