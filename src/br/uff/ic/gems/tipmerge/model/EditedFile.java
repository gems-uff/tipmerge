/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class has information about the files and who edited these files. This
 * is show in file analysis
 *
 * @author j2cf, Catarina
 */
public class EditedFile {

    private String fileName;
    private List<Committer> whoEditFile = new ArrayList<>();

    public EditedFile(String fileName) {
        this.fileName = fileName;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Committer> getWhoEditTheFile() {
        return whoEditFile;
    }

    public void setWhoEditTheFile(List<Committer> whoEditTheFile) {
        this.whoEditFile = whoEditTheFile;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    //this method compare file names
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditedFile other = (EditedFile) obj;
        return Objects.equals(this.fileName, other.fileName);
    }

    @Override
    public String toString() {
        return this.getFileName();
    }

}
