/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer.git;

import br.uff.ic.gems.tipmerge.model.EditedFile;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jjaircf
 */
public class Dependency {
    
    Map<EditedFile, Set<EditedFile>> dependencyMap;

    public Dependency(Map<EditedFile, Set<EditedFile>> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }

    public Map<EditedFile, Set<EditedFile>> getDependencyMap() {
        return dependencyMap;
    }

    public void setDependencyMap(Map<EditedFile, Set<EditedFile>> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }    
    
}
