/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author jjcfigueiredo
 */
public class MedalBronze {

    private Integer directionIcon;
    private String file;
    private Map<String, Integer> fileDepend = new HashMap<>();

    public MedalBronze(String file, String fileDependent, Integer direction) {
        this.directionIcon = direction;
        this.file = file;
        this.fileDepend.put(fileDependent,direction);
    }
    

    public Integer getDirection() {
        return directionIcon;
    }

    public void setDirection(Integer direction) {
        this.directionIcon = direction;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Map<String, Integer> getFileDepend() {
        return fileDepend;
    }

    public void setFileDepend(Map<String, Integer> fileDepend) {
        this.fileDepend = fileDepend;
    }
    
    public void addFileDepend(String file, Integer direction){
        this.fileDepend.put(file,direction);
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
        return this.file + "\t" + this.directionIcon + 
                this.fileDepend.toString()
                ;
    }
    
}
