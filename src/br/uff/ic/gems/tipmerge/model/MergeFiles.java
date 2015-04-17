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
 * This class has information about each merge, as: files on b1, b2 and previous history and who changed them (and all information in Merge)
 * @author j2cf, Catarina
 */
public class MergeFiles extends Merge {
	
	private List<EditedFile> filesOnBranchOne;
	private List<EditedFile> filesOnBranchTwo;
	private Set<EditedFile> filesOnBothBranch;
	private Set<EditedFile> filesOnPreviousHistory;

	public MergeFiles(String hashOfMerge, File pathToRepository) {
		super(hashOfMerge, pathToRepository);
	}
	
	//This method returns the name of all authors modifying any files on Branch1 and Branch2
	public Set<Committer> getCommittersOnMege(){
		List<Committer> authors = new ArrayList<>();
		Set<Committer> committersBranch = this.getCommittersOnBranchOne();
		
		for (Committer committer : committersBranch){
			Auxiliary.addOnlyNew(authors, committer);
		}
		
		committersBranch = this.getCommittersOnBranchTwo();
		
		for (Committer cmterBranch : committersBranch){
			Auxiliary.addOnlyNew(authors, cmterBranch);
		}
		
	/*	Set<Committer> authors = this.getCommittersOnBranchOne();
		authors.addAll(this.getCommittersOnBranchTwo());
	*/			
		return new HashSet<>(authors);
	}
	
	//This method returns the name of all authors modifying any files on Previous History
	public Set<Committer> getCommittersOnPreviousHistory() {
		List<Committer> authors = new ArrayList<>();
		Set<EditedFile> files = this.getFilesOnPreviousHistory();
				
		for (EditedFile editedFile : files){
			for (Committer cmterFile : editedFile.getWhoEditTheFile()){
				Auxiliary.addOnlyNew(authors, cmterFile);
			}
		}
		return new HashSet<>(authors);
	}
	

	public List<EditedFile> getFilesOnBranchOne() {
		return filesOnBranchOne;
	}
	public void setFilesOnBranchOne(List<EditedFile> filesOnBranchOne) {
		this.filesOnBranchOne = filesOnBranchOne;
		if (this.filesOnBranchTwo != null)
			this.setFilesOnPreviousHistory();
	}

	public List<EditedFile> getFilesOnBranchTwo() {
		return filesOnBranchTwo;
	}
	public void setFilesOnBranchTwo(List<EditedFile> filesOnBranchTwo) {
		this.filesOnBranchTwo = filesOnBranchTwo;
		if (this.filesOnBranchOne != null)
			this.setFilesOnPreviousHistory();
	}

	public Set<Committer> getCommittersOnBranchOne() {
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
	 * @return the filesOnPreviousHistory
	 */
	public Set<EditedFile> getFilesOnPreviousHistory() {
		return filesOnPreviousHistory;
	}

	/**
	public void setFilesOnPreviousHistory(List<EditedFile> filesOnPreviousHistory) {
		this.filesOnPreviousHistory = filesOnPreviousHistory;
	}
	*/
	private void setFilesOnPreviousHistory() {
		if ((this.filesOnBranchOne != null) && (this.filesOnBranchTwo != null)){
			Set<EditedFile> filesOnMerge = new HashSet<>();
			this.getFilesOnBranchOne().stream().forEach((file)->{
				filesOnMerge.add(new EditedFile(file.getFileName()));
			});
			this.getFilesOnBranchTwo().stream().forEach((file)->{
				filesOnMerge.add(new EditedFile(file.getFileName()));
			});
			this.filesOnPreviousHistory = filesOnMerge;
		}
	}	

	public void setFilesOnPreviousHistory(Set<EditedFile> files) {
		this.filesOnPreviousHistory = files;	
	}

	/**
	 * @return the filesOnBothBranch
	 */
	public Set<EditedFile> getFilesOnBothBranch() {
		Set<EditedFile> files = new HashSet<>();
		if(filesOnBothBranch == null){
			for(EditedFile efile1 : this.getFilesOnBranchOne()){
				for(EditedFile efile2 : this.getFilesOnBranchTwo()){
					if(efile1.equals(efile2)) files.add(efile1);
				}
			}
			filesOnBothBranch = files;
		}
		return filesOnBothBranch;
	}

	/**
	 * @param filesOnBothBranch the filesOnBothBranch to set
	 */
	public void setFilesOnBothBranch(Set<EditedFile> filesOnBothBranch) {
		this.filesOnBothBranch = filesOnBothBranch;
	}
	
	

}
