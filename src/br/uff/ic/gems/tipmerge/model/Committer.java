/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

/**
 *
 * @author j2cf
 */
public class Committer implements Comparable<Committer>{
	
	private final String name;
	private final String email;
	private Integer commits;
	
	public Committer(String name,String email){
		this.name = name;
		this.email = email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the commits
	 */
	public Integer getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(Integer commits) {
		this.commits = commits;
	}
	
	@Override
	public String toString(){
		return this.getName() + " : " + this.getEmail() + " : " + this.getCommits();
	}

	@Override
	public int compareTo(Committer cmter) {
        //if ( this.getName().equals(cmter.getName()) || this.getEmail().equals(cmter.getEmail()) )
		if (this.getEmail().equals(cmter.getEmail()) )
            return 1;
        return 0;	
	}
	
}
