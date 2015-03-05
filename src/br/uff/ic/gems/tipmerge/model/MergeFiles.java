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
	private Set<EditedFile> filesOnHistory;

	public MergeFiles(String hashOfMerge, File pathToRepository) {
		super(hashOfMerge, pathToRepository);
	}
	
	//retorna o nome de todos os autores, que alteraram algum arquivo
	public Set<Committer> getCommittersOnMege(){
		Set<Committer> authors = this.getCommittersOnBranchOne();
		authors.addAll(this.getCommittersOnBranchTwo());
		return authors;
	}
	
	
	public Set<Committer> getCommittersOnHistory() {
		Set<Committer> authors = this.getCommittersOnMege();
		this.getFilesOnHistory().stream().forEach((file) -> {
			authors.addAll(file.getWhoEditTheFile());
		});
		return authors;
	}
	

	public List<EditedFile> getFilesOnBranchOne() {
		return filesOnBranchOne;
	}
	public void setFilesOnBranchOne(List<EditedFile> filesOnBranchOne) {
		this.filesOnBranchOne = filesOnBranchOne;
		if (this.filesOnBranchTwo != null)
			this.setFilesOnHistory();
	}

	public List<EditedFile> getFilesOnBranchTwo() {
		return filesOnBranchTwo;
	}
	public void setFilesOnBranchTwo(List<EditedFile> filesOnBranchTwo) {
		this.filesOnBranchTwo = filesOnBranchTwo;
		if (this.filesOnBranchOne != null)
			this.setFilesOnHistory();
	}

	public Set<Committer> getCommittersOnBranchOne() {
		System.out.println("Pediu os committers");
		Set<Committer> authors = new HashSet<>();
		this.getFilesOnBranchOne().stream().forEach((file) -> {
			
			//EditedFile newFile = file.clone();
			
			authors.addAll(file.getWhoEditTheFile());
		});
		return authors;	
	}

	public Set<Committer> getCommittersOnBranchTwo() {
		Set<Committer> authors = new HashSet<>();
		this.getFilesOnBranchTwo().stream().forEach((file) -> {
			authors.addAll(file.getWhoEditTheFile());
		});
		return authors;	
	}

	/**
	 * @return the filesOnHistory
	 */
	public Set<EditedFile> getFilesOnHistory() {
		return filesOnHistory;
	}

	/**
	public void setFilesOnHistory(List<EditedFile> filesOnHistory) {
		this.filesOnHistory = filesOnHistory;
	}
	*/
	private void setFilesOnHistory() {
		if ((this.filesOnBranchOne != null) && (this.filesOnBranchTwo != null)){
			Set<EditedFile> filesOnMerge = new HashSet<>();
			this.getFilesOnBranchOne().stream().forEach((file)->{
				filesOnMerge.add(new EditedFile(file.getFileName()));
			});
			this.getFilesOnBranchTwo().stream().forEach((file)->{
				filesOnMerge.add(new EditedFile(file.getFileName()));
			});
			this.filesOnHistory = filesOnMerge;
		}
	}	

}
