/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author j2cf
 */
public class RankingGenerator {
	
	private List<Medalist> ranking = new ArrayList<>();
	
	/**
	 * @return the ranking
	 */
	public List<Medalist> getRanking() {
        Collections.sort(ranking, new Compara());
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(List<Medalist> ranking) {
		this.ranking = ranking;
	}

	public Set<EditedFile> setMedalFromDependencies(Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

		//System.out.println("Excep: " + excepiontFiles.toString());

		dependenciesMap.entrySet().stream().forEach((dependency) -> {
			
			EditedFile ascendentCand = dependency.getKey();
			
			List<Committer> silverRights = new ArrayList<>();
			
			if(!excepiontFiles.contains(ascendentCand)){
				
				this.setGoldMedals(ascendentCand.getWhoEditTheFile());
				silverRights = ascendentCand.getWhoEditTheFile();
				
				for(EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()){
					if(fileHistory.equals(ascendentCand)){
						this.setBronzeMedals(fileHistory.getWhoEditTheFile());
						break;
					}
				}
				
				excepiontFiles.add(ascendentCand);

			}else
				silverRights = ascendentCand.getWhoEditTheFile();
			
			Set<EditedFile> consequentList = dependency.getValue();
			for(EditedFile consequent : consequentList){

				this.setSilverMedals(silverRights);
				
				if(!excepiontFiles.contains(consequent)){
					
					this.setGoldMedals(consequent.getWhoEditTheFile());
					
					for(EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()){
						if(fileHistory.equals(consequent)){
							this.setBronzeMedals(fileHistory.getWhoEditTheFile());
							break;
						}
					}
					
					excepiontFiles.add(consequent);

				}
			}
			
		});

		return excepiontFiles;
		
	}

	private void setBronzeMedals(Collection<Committer> cmterBronze) {
		for(Committer cmter : cmterBronze){
			Medalist medalist = new Medalist(cmter);
			medalist.setBronzeMedals(1);
			int index = ranking.indexOf(medalist);
			if(index == -1)
				ranking.add(medalist);
			else{
				ranking.get(index).addBronzeMedal();
			}
		}
	}

	private void setSilverMedals(Collection<Committer> cmterSilver) {
		for(Committer cmter : cmterSilver){
			Medalist medalist = new Medalist(cmter);
			medalist.setSilverMedals(1);
			int index = ranking.indexOf(medalist);
			if(index == -1)
				ranking.add(medalist);
			else{
				ranking.get(index).addSilverMedal();
			}
		}
	}

	private void setGoldMedals(Collection<Committer> cmterGold) {
		//		for(Medalist m : this.ranking){
		for(Committer cmter : cmterGold){
			Medalist medalist = new Medalist(cmter);
			medalist.setGoldMedals(1);
			int index = ranking.indexOf(medalist);
			if(index == -1)
				ranking.add(medalist);
			else{
				ranking.get(index).addGoldMedal();
			}
		}
	}

	public Set<EditedFile> setMedalsFilesEditedBothBranches(MergeFiles mergeFiles) {
		List<Committer> gold = new ArrayList<>();
		List<Committer> bronze = new ArrayList<>();
		
		Set<EditedFile> files = new HashSet<>( mergeFiles.getFilesOnBothBranch() );
		for(EditedFile file : files){
			int indexb1 = mergeFiles.getFilesOnBranchOne().indexOf(file);
			if(indexb1 > -1){
				gold.addAll(mergeFiles.getFilesOnBranchOne().get(indexb1).getWhoEditTheFile());
				
			}
			
			int indexb2 = mergeFiles.getFilesOnBranchTwo().indexOf(file);
			if(indexb2 > -1){

				gold.addAll(mergeFiles.getFilesOnBranchTwo().get(indexb2).getWhoEditTheFile());
				
			}
		}
		List<EditedFile> filesHistory = new ArrayList<>(mergeFiles.getFilesOnPreviousHistory());
		for(EditedFile file : files){
			int index = filesHistory.indexOf(file);
			if(index > -1){
				bronze.addAll(filesHistory.get(index).getWhoEditTheFile());
				break;
			}
		}

		this.setGoldMedals(gold);
		this.setBronzeMedals(bronze);
		//System.out.println(this.toString());
		return  files;
	}
	
	@Override
	public String toString() {
		Collections.sort(ranking, new Compara());
		StringBuilder result = new StringBuilder();
		for(Medalist medalist : ranking){
			result.append(medalist).append("\n");
		}
		return result.toString();
	}
	
	
}
