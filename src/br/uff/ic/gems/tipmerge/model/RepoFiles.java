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
public class RepoFiles {
	
	private final Repository repository;
        
	private List<MergeFiles> mergeFiles = new ArrayList<>();

	public RepoFiles(Repository repository) {
		this.repository = repository;
	}

	public Repository getRepository() {
		return repository;
	}

    /**
     * @return the mergeFiles
     */
    public List<MergeFiles> getMergeFiles() {
        return mergeFiles;
    }

    /**
     * @param mergeFiles the mergeFiles to set
     */
    public void setMergeFiles(List<MergeFiles> mergeFiles) {
        this.mergeFiles = mergeFiles;
    }

	
	
}
