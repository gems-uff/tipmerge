/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.coverage;

import br.uff.ic.gems.tipmerge.model.MedalBronze;
import br.uff.ic.gems.tipmerge.model.Medalist;
import java.util.List;

/**
 *
 * @author jjcfigueiredo
 */
public class ToolTipMessage {
    
    public String getMessage(Medalist medalist, String file){
        String tip = "";
        for(MedalBronze medal : medalist.getBronzeList())
            if(medal.getFile().equals(file)){
                //System.out.println(medal);
                return medal.getFileDepend().toString();
            }
        
        return null;
    }
    
}
