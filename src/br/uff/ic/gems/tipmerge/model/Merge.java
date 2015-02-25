/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.io.File;

/**
 *
 * @author j2cf
 */
public abstract class Merge {
	
	private final File pathToRepository;
	private String hash;
	private String parent1;
	private String parent2;
	private String hashBase;
	
	public Merge(String hashOfMerge, File pathToRepository) {
		this.hash = hashOfMerge;
		this.pathToRepository = pathToRepository;
	}
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}


	/**
	 * @param parent1 the parent1 to set
	 * @param parent2 the parent2 to set
	 */
	public void setParents(String parent1, String parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
	}

	/**
	 * @return the parent2
	 */
	public String[] getParents() {
		String[] parents = {parent1,parent2};
		return parents;
	}

	
	/**
	 * @return the hashBase
	 */
	public String getHashBase() {
		return hashBase;
	}

	/**
	 * @param hashBase the hashBase to set
	 */
	public void setHashBase(String hashBase) {
		this.hashBase = hashBase;
	}
	
	/**
	 * @return the path to project
	 */
	public File getPath() {
		return pathToRepository;
	}
	
}
