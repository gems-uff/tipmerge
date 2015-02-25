/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class RepoCommits{
	
	private final Repository repository;
	
	private List<MergeCommits> merges = new ArrayList<>();

	public RepoCommits(Repository repository) {
		this.repository = repository;
		repository.getListOfMerges().stream().forEach((mergeHash) -> {
			this.merges.add(new MergeCommits(mergeHash, repository.getProject()));
		});
	}

	/**
	 * @param merges the merges to set
	 */
	public void setMerges(List<MergeCommits> merges) {
		this.merges = merges;
	}
	
	/**
	 * @return the merges
	 */
	public List<MergeCommits> getMerges() {
		return merges;
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}
	
}
