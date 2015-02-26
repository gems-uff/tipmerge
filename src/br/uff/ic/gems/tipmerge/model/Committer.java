/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + Objects.hashCode(this.name);
		hash = 67 * hash + Objects.hashCode(this.email);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Committer other = (Committer) obj;
		if (!Objects.equals(this.name, other.name) && (!Objects.equals(this.email, other.email))) {
			return false;
		}
		//if (!Objects.equals(this.email, other.email)) {
		//	return false;
		//}
		return true;
	}
	
}
