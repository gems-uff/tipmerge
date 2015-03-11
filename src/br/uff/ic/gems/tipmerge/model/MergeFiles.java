/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import br.uff.ic.gems.tipmerge.util.Auxiliary;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author j2cf, Catarina
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
		List<Committer> authors = new ArrayList<>();
		Set<Committer> committersBranch = this.getCommittersOnBranchOne();
		
		for (Committer committer : committersBranch){
			Auxiliary.addOnlyNew(authors, committer);
		}
		
		committersBranch = this.getCommittersOnBranchTwo();
		
		for (Committer cmtrBranch : committersBranch){
			Auxiliary.addOnlyNew(authors, cmtrBranch);
		}
		
	/*	Set<Committer> authors = this.getCommittersOnBranchOne();
		authors.addAll(this.getCommittersOnBranchTwo());
	*/	imprime("No merge", authors);
		
		return new HashSet<>(authors);
	}
	
	
	public Set<Committer> getCommittersOnHistory() {
		List<Committer> authors = new ArrayList<>(this.getCommittersOnMege());
		Set<EditedFile> files = this.getFilesOnHistory();
				
		for (EditedFile editedFile : files){
			for (Committer cmtrFile : editedFile.getWhoEditTheFile()){
				Auxiliary.addOnlyNew(authors, cmtrFile);
			}
		}
		
		
			//authors.add(committer);
		
	/*	Set<Committer> authors = this.getCommittersOnMege();
		this.getFilesOnHistory().stream().forEach((file) -> {
			authors.addAll(file.getWhoEditTheFile());
		});
	*/	
		return new HashSet<>(authors);
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

	//TODO DELETE
	private void imprime(String text, Collection<Committer> authors){
		System.out.println(text);
		for (Committer cmtr : authors)
			System.out.println(cmtr.toString());

	}
	
}
