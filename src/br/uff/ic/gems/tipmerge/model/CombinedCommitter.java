/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author joao
 */
public class CombinedCommitter extends Committer {

    private List<Committer> committers;

    public CombinedCommitter(List<Committer> committers, String name, String email) {
        super(name, email);
        this.committers = committers;
    }

    public String getInitial() {
        return this.committers.stream().map(Committer::getInitial).map(String::trim).collect(Collectors.joining(", "));
    }

    public String getNameEmail() {
        return this.committers.stream().map(Committer::getNameEmail).map(String::trim).collect(Collectors.joining(", \n"));
    }

    public Integer getQuantity() {
        return this.committers.size();
    }
    
    public List<Committer> getCommitters(){
        return this.committers;
    }


}
