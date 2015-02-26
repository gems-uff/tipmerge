/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author j2cf
 */
public class EditedFile {
	
	private String fileName;
	private List<Committer> whoEditTheFile;
	
	public EditedFile(String fileName){
		this.fileName = fileName;
		
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Committer> getWhoEditTheFile() {
		return whoEditTheFile;
	}
	public void setWhoEditTheFile(List<Committer> whoEditTheFile) {
		this.whoEditTheFile = whoEditTheFile;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.fileName);
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
		final EditedFile other = (EditedFile) obj;
		if (!Objects.equals(this.fileName, other.fileName)) {
			return false;
		}
		return true;
	}
	
}
