/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class MergeBranches{
	
	private GitProject project;
	
	private String commitHash;
	private String parent1;
	private String parent2;
	private String mergeBase;
	private List<CommitAuthor> authorsBranchOne;
	private List<CommitAuthor> authorsBranchTwo;
	
	List<String> commitsOnBranch1;
	List<String> commitsOnBranch2;
	
	public MergeBranches(String commitHash, GitProject project){
		this.commitHash = commitHash;
		this.project = project;
		//aqui entra o código para fazer tudo o que tem que ser feito para preencher os outros valores
	}
	


	/**
	 * @return the commitHash
	 */
	public String getCommitHash() {
		return commitHash;
	}

	/**
	 * @param commitHash the commitHash to set
	 */
	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}

	/**
	 * @return the parent1
	 */
	public String getParent1() {
		return parent1;
	}

	/**
	 * @param parent1 the parent1 to set
	 */
	public void setParent1(String parent1) {
		this.authorsBranchOne = getAuthors(parent1);
		this.parent1 = parent1;
	}

	/**
	 * @return the parent2
	 */
	public String getParent2() {
		return parent2;
	}

	/**
	 * @param parent2 the parent2 to set
	 */
	public void setParent2(String parent2) {
		this.authorsBranchTwo = getAuthors(parent2);
		this.parent2 = parent2;
	}

	/**
	 * @return the authorsBranchOne
	 */
	public List<CommitAuthor> getAuthorsBranchOne() {
		if (this.parent1 == null){
			System.out.println("parent não informado");
			return null;
		}else
			return authorsBranchOne;
	}

	/**
	 * @param authorsBranchOne the authorsBranchOne to set
	 */
	public void setAuthorsBranchOne(List<CommitAuthor> authorsBranchOne) {
		this.authorsBranchOne = authorsBranchOne;
	}

	/**
	 * @return the authorsBranchTwo
	 */
	public List<CommitAuthor> getAuthorsBranchTwo() {
		return authorsBranchTwo;
	}

	/**
	 * @param authorsBranchTwo the authorsBranchTwo to set
	 */
	public void setAuthorsBranchTwo(List<CommitAuthor> authorsBranchTwo) {
		this.authorsBranchTwo = authorsBranchTwo;
	}

	/**
	 * @return the mergeBase
	 */
	public String getMergeBase() {
		return mergeBase;
	}

	/**
	 * @param mergeBase the mergeBase to set
	 */
	public void setMergeBase(String mergeBase) {
		this.mergeBase = mergeBase;
	}

	private List<CommitAuthor> getAuthors(String hashParent) {
		
		List<String> authorsOnBranch = CommandLine.getMultiplesResults("git shortlog -s " + this.getMergeBase() + ".." + hashParent, this.project.getProjectDirectory());
		List<CommitAuthor> authorList = new ArrayList<CommitAuthor>();
		for(String line : authorsOnBranch){
			line = line.trim();
			CommitAuthor authorTemp = new CommitAuthor(line.split("\t")[1], "");
			authorTemp.setCommits(Integer.valueOf(line.split("\t")[0]));
			authorList.add(authorTemp);
		}
		return authorList;
	}
	
}
