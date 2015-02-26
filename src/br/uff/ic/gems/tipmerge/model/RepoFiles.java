/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.List;

/**
 *
 * @author j2cf
 */
public class RepoFiles {
	
	private final Repository repository;
	private List<MergeFiles> mergeFiles;

	public RepoFiles(Repository repository) {
		this.repository = repository;
	}

	public Repository getRepository() {
		return repository;
	}

	public List<MergeFiles> getFiles() {
		return mergeFiles;
	}
	public void setFiles(List<MergeFiles> files) {
		this.mergeFiles = files;
	}
	
	
}
