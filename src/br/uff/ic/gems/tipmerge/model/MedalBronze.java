/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jjcfigueiredo
 */
public class MedalBronze {

    private Integer direction;
    private String file;
    private Set<String> fileDepend = new HashSet<>();

    public MedalBronze(String file, String fileDependent, Integer direction) {
        this.direction = direction;
        this.file = file;
        this.fileDepend.add(fileDependent);
    }
    

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Set<String> getFileDepend() {
        return fileDepend;
    }

    public void setFileDepend(Set<String> fileDepend) {
        this.fileDepend = fileDepend;
    }
    
    public void addFileDepend(String file){
        this.fileDepend.add(file);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MedalBronze other = (MedalBronze) obj;
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.file + "\t" +
                this.fileDepend.toString() + "\t" +
                this.direction;
    }
    
    
    
    
    
}
