/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

/**
 *
 * @author j2cf, Catarina
 */
public class Conciliator {
	
	private final Committer committer;
	private int commitsBranch1 = 0;
	private int commitsBranch2 = 0;
	private int commitsBoothBranchs = 0;
	private int commitsHistory = 0;

	public Conciliator(Committer commiter) {
		this.committer = commiter;
	}

	/**
	 * @return the committer
	 */
	public Committer getCommitter() {
		return committer;
	}

	/**
	 * @return the commitsBranch1
	 */
	public int getCommitsBranch1() {
		return commitsBranch1;
	}

	/**
	 * @param commitsBranch1 the commitsBranch1 to set
	 */
	public void setCommitsBranch1(int commitsBranch1) {
		this.commitsBranch1 = commitsBranch1;
	}

	/**
	 * @return the commitsBranch2
	 */
	public int getCommitsBranch2() {
		return commitsBranch2;
	}

	/**
	 * @param commitsBranch2 the commitsBranch2 to set
	 */
	public void setCommitsBranch2(int commitsBranch2) {
		this.commitsBranch2 = commitsBranch2;
	}

	/**
	 * @return the commitsBoothBranchs
	 */
	public int getCommitsBoothBranchs() {
		return commitsBoothBranchs;
	}

	/**
	 * @param commitsBoothBranchs the commitsBoothBranchs to set
	 */
	public void setCommitsBoothBranchs(int commitsBoothBranchs) {
		this.commitsBoothBranchs = commitsBoothBranchs;
	}

	/**
	 * @return the commitsHistory
	 */
	public int getCommitsHistory() {
		return commitsHistory;
	}

	/**
	 * @param commitsHistory the commitsHistory to set
	 */
	public void setCommitsHistory(int commitsHistory) {
		this.commitsHistory = commitsHistory;
	}
	
}
