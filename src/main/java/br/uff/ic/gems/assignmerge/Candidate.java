/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

/**
 *
 * @author j2cf
 */
public class Candidate {
	
	private final CommitAuthor author;
	private int cbOne = 0;
	private int cbTwo = 0;
	private int cBooth = 0;
	private int cHistory = 0;

	Candidate(CommitAuthor ca) {
		this.author = ca;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author.getName();
	}

	/**
	 * @return the cbOne
	 */
	public int getCbOne() {
		return cbOne;
	}

	/**
	 * @param cbOne the cbOne to set
	 */
	public void setCbOne(int cbOne) {
		this.cbOne = cbOne;
	}

	/**
	 * @return the cbTwo
	 */
	public int getCbTwo() {
		return cbTwo;
	}

	/**
	 * @param cbTwo the cbTwo to set
	 */
	public void setCbTwo(int cbTwo) {
		this.cbTwo = cbTwo;
	}

	/**
	 * @return the cBooth
	 */
	public int getcBooth() {
		return cBooth;
	}

	/**
	 * @param cBooth the cBooth to set
	 */
	public void setcBooth(int cBooth) {
		this.cBooth = cBooth;
	}

	/**
	 * @return the cHistory
	 */
	public int getcHistory() {
		return cHistory;
	}

	/**
	 * @param cHistory the cHistory to set
	 */
	public void setcHistory(int cHistory) {
		this.cHistory = cHistory;
	}
	
	@Override
	public String toString(){
		return this.author.getName() + " " + this.cbOne + " " + this.cbTwo + " " + this.cBooth + " " + this.cHistory;
	}
	
}
