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
	
	private static void countSilverMedals(EditedFile file, List<Medalist> medalists) {
		for(Committer cmter : file.getWhoEditTheFile()){
			Medalist medalist = new Medalist(cmter);
			int pos = medalists.indexOf(medalist);
			if(pos == -1){
				medalist.setSilverMedals(1);
				medalists.add(medalist);
			}
			else
				medalists.get(pos).setSilverMedals(medalists.get(pos).getSilverMedals() + 1);
		}
	}

	public void updateGoldMedals(MergeFiles mergeFiles) {
		System.out.println("Medalhas de Ouro: editou no RAMO os arquivos de both");
		
		Set<EditedFile> filesTarget = mergeFiles.getFilesOnBothBranch();

		System.out.println("RAMO 1");
		for(EditedFile file : mergeFiles.getFilesOnBranchOne()){
			if(filesTarget.contains(file)){
				System.out.println("File: " + file + "\n\t" + file.getWhoEditTheFile());
				countGoldMedals(file, this.getRanking());
			}
		}
		
		System.out.println("RAMO 2");
		for(EditedFile file : mergeFiles.getFilesOnBranchTwo()){
			if(filesTarget.contains(file)){
				System.out.println("File: " + file + "\n\t" + file.getWhoEditTheFile());
				countGoldMedals(file, this.getRanking());
			}
		}

		System.out.println(this.toString());

	}

	public void updateBronzeMedals(MergeFiles mergeFiles) {
		System.out.println("Medalhas de Bronze: editou no HISTORICO os arquivos de both");

		Set<EditedFile> filesTarget = mergeFiles.getFilesOnBothBranch();

		for(EditedFile file : mergeFiles.getFilesOnPreviousHistory()){
			if(filesTarget.contains(file)){

				System.out.println("File: " + file + "\n\t" + file.getWhoEditTheFile());
				 countBronzeMedals(file, this.getRanking());
				
			}
		}
		System.out.println(this.toString());

	}
	
	public void setMedalFromDependencies(Map<EditedFile,Set<EditedFile>> dependenciesList, Collection<EditedFile> excepiontFiles, Set<EditedFile> historyFiles){
		
		System.out.println("Medalhas das: DEPENDENCIAS");
		
		historyFiles.removeAll(excepiontFiles);
		Set<EditedFile> consequents = new HashSet<>();
		dependenciesList.entrySet().stream().forEach((dependence) -> {
			EditedFile ascendent = dependence.getKey();
			if(!excepiontFiles.contains(ascendent)){
				System.out.println("OURO (ascendente): " + ascendent + "\n\t" + ascendent.getWhoEditTheFile());
				countGoldMedals(ascendent, ranking);
				System.out.println("PRATA (ascendente): " + ascendent + "\n\t" + ascendent.getWhoEditTheFile());
				countSilverMedals(ascendent, ranking);
				
				for(EditedFile ascOnHistory : historyFiles){
					if(ascOnHistory.equals(ascendent)){
						System.out.println("BRONZE (ascedente no historico): " + ascOnHistory + "\n\t" + ascOnHistory.getWhoEditTheFile());
						countBronzeMedals(ascOnHistory, ranking);
					}
				}
				
				Set<EditedFile> consequentList = dependence.getValue();
				consequentList.stream().forEach((consequent) -> {
					System.out.println("OURO (consequente): " + consequent + "\n\t" + consequent.getWhoEditTheFile());
					consequents.add(consequent);
					countGoldMedals(consequent, ranking);					
				});
				
			}
		});
		consequents.stream().forEach((consequent) -> {
			for(EditedFile consOnHistory : historyFiles){
				if(consOnHistory.equals(consequent)){
					System.out.println("BRONZE (consequente no historico): " + consOnHistory + "\n\t" + consOnHistory.getWhoEditTheFile());
					countBronzeMedals(consOnHistory, ranking);
				}
			}
		});
		
		System.out.println(this.toString());

	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(Medalist medalist : ranking){
			result.append(medalist).append("\n");
		}
		return result.toString();
	}

	
}
