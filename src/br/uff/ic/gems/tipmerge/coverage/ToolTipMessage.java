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
				tip = "<html>";
				for (String fileDep : medal.getFileDepend()){
					tip += "File " + fileDep + " changed in " + getBranch(medal.getDirection()) + " uses File " + file + "<br>";
				}
				tip += "</html>";
				return tip;
            }
        
        return null;
    }
	
	private String getBranch(Integer branch){
		switch (branch) {
			case 0 : return "B1";
			case 1 : return "B2";
			case 2 : return "B1 and B2";
		}
		return null;
	}    
}
