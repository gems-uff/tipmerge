/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author j2cf
 */
public class MergeFiles extends Merge {
	
	private List<EditedFile> filesOnBranchOne;
	private List<EditedFile> filesOnBranchTwo;

	public MergeFiles(String hashOfMerge, File pathToRepository) {
		super(hashOfMerge, pathToRepository);
	}
	
	//retorna o nome de todos os autores, que alteraram algum arquivo
	public List<Committer> getCommittersOnMege(){
		Set<Committer> authors = new HashSet<>();
		this.getFilesOnBranchOne().stream().forEach((file) -> {
			authors.addAll(file.getWhoEditTheFile());
		});
		this.getFilesOnBranchTwo().stream().forEach((file) -> {
			authors.addAll(file.getWhoEditTheFile());
		});
		return (List<Committer>) authors;
	}

	public List<EditedFile> getFilesOnBranchOne() {
		return filesOnBranchOne;
	}
	public void setFilesOnBranchOne(List<EditedFile> filesOnBranchOne) {
		this.filesOnBranchOne = filesOnBranchOne;
	}

	public List<EditedFile> getFilesOnBranchTwo() {
		return filesOnBranchTwo;
	}
	public void setFilesOnBranchTwo(List<EditedFile> filesOnBranchTwo) {
		this.filesOnBranchTwo = filesOnBranchTwo;
	}

	
	
}
