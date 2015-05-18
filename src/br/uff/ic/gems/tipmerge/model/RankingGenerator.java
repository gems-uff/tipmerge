/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author j2cf
 */
public class RankingGenerator {
	

	public static List<Medalist> getMedalists(MergeFiles merge){
		List<Medalist> medalists = new ArrayList<>();
		Set<EditedFile> filesTarget = merge.getFilesOnBothBranch();
		
		for(EditedFile file : merge.getFilesOnBranchOne()){
			if(filesTarget.contains(file)){

				countGoldMedals(file, medalists);
				
			}
		}

		for(EditedFile file : merge.getFilesOnBranchTwo()){
			if(filesTarget.contains(file)){

				countGoldMedals(file, medalists);
				
			}
		}
		
		for(EditedFile file : merge.getFilesOnPreviousHistory()){
			if(filesTarget.contains(file)){

				 countBronzeMedals(file, medalists);
				
			}
		}
		
		//TODO incluir a medalhas de prata
		
		
		//TODO ordenar o ranking
                 Collections.sort(medalists, new Compara());
		return medalists;
	}

	private static void countGoldMedals(EditedFile file, List<Medalist> medalists) {
		for(Committer cmter : file.getWhoEditTheFile()){
			Medalist medalist = new Medalist(cmter);
			int pos = medalists.indexOf(medalist);
			if(pos == -1){
				medalist.setGoldMedals(1);
				medalists.add(medalist);
			}
			else
				medalists.get(pos).setGoldMedals(medalists.get(pos).getGoldMedals() + 1);
		}
	}
	
	private static void countBronzeMedals(EditedFile file, List<Medalist> medalists) {
		for(Committer cmter : file.getWhoEditTheFile()){
			Medalist medalist = new Medalist(cmter);
			int pos = medalists.indexOf(medalist);
			if(pos == -1){
				medalist.setBronzeMedals(1);
				medalists.add(medalist);
			}
			else
				medalists.get(pos).setBronzeMedals(medalists.get(pos).getBronzeMedals() + 1);
		}
	}
	/*
	public static void main(String args[]){
		Committer c = new Committer("Jair", "jair@ufac");	
		Committer c2 = new Committer("Catarina", "Catarina@ufac");
		Committer c3 = new Committer("Catarina", "Catarina@ufac");
		Committer c4 = new Committer("Catarina1", "caty@ufac");
		List<Committer> cList = new ArrayList<>();
		cList.add(c); cList.add(c2); cList.add(c3); cList.add(c4);
		List<Medalist> medalists = new ArrayList<>();
		//medalists.add(new Medalist(c));
		//medalists.add(new Medalist(c2));

				for(Committer cmter : cList){
					Medalist medalist = new Medalist(cmter);
					int pos = medalists.indexOf(medalist);
					if(pos == -1){
						medalist.setGoldMedals(1);
						medalists.add(medalist);
					}
					else
						medalists.get(pos).setGoldMedals(medalists.get(pos).getGoldMedals() + 1);				
					System.out.println(pos);

				}

		System.out.println(medalists.toString());
		
	}
	*/

	
}
