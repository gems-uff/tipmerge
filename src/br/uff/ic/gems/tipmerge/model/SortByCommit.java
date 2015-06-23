/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Medalist;
import java.util.Comparator;

/**
 *
 * @author Jhunior
 */
public class SortByCommit  implements Comparator<Committer>{

    public int compare(Committer o1, Committer o2) {
        if(o1.getCommits()>o2.getCommits()) 
            return -1;
         else 
            if(o1.getCommits()<o2.getCommits())  
                return+1;
                                
     
        return 0;
    }
    
}
