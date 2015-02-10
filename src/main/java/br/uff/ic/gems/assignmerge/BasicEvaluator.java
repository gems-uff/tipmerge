/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.io.File;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class BasicEvaluator {

	File repositoryPath;

	public BasicEvaluator(File repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public void getResults() {
		System.out.println(this.getCommits());
	}

	@SuppressWarnings("empty-statement")
	public String getCommits() {
		return CommandLine.getMultiplesResults("git rev-list HEAD --count",repositoryPath).get(0);
	}

	public String getDate() {
		String command = "git show -s --format=%ci";
		return CommandLine.getMultiplesResults(command,repositoryPath).get(0);
	}

	public List<String> getTags() {
		return CommandLine.getMultiplesResults("git tag",repositoryPath);
	}

	public String getTotalOfMerges() {
		return String.valueOf(CommandLine.getMultiplesResults("git log --merges --pretty=%H",repositoryPath).size());
	}
	
	public List<String> getMerges() {
		return CommandLine.getMultiplesResults("git log --merges --pretty=%H",repositoryPath);
	}
	
	public List<String> getBranches(){
		List<String> branches = CommandLine.getMultiplesResults("git branch -a",repositoryPath);
		branches.remove(1);
		branches.remove(0);
		branches.add(0, " origin/master");
		return branches;
	}
	
	public String getLastHashOfBranch(String branchName){
		String command = "git log -n 1 --pretty=format:%H " + branchName;
		return CommandLine.getSingleResult(command,repositoryPath);
	}

}
