/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.Comparator;

/**
 *
 * @author Jhunior
 */
public class Compara implements Comparator<Medalist> {

    @Override
    public int compare(Medalist o1, Medalist o2) {
       if(o1.getGoldMedals()>o2.getGoldMedals()) return -1;
        else if(o1.getGoldMedals()<o2.getGoldMedals())  return+1;
        else if(o1.getGoldMedals()== o2.getGoldMedals())
                if(o1.getSilverMedals()>o2.getSilverMedals()) return -1;
                 else if(o1.getSilverMedals()<o2.getSilverMedals())  return+1;
                  else if(o1.getSilverMedals()== o2.getSilverMedals()){
                              if(o1.getBronzeMedals()>o2.getBronzeMedals()) return -1;
                                else if(o1.getBronzeMedals()<o2.getBronzeMedals())  return+1;
                                }
     
        return 0;
    }
    
}
