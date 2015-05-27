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

	public void setMedalFromDependencies(Map<EditedFile, Set<EditedFile>> dependencies, MergeFiles mergeFiles) {
		System.out.println("Medalhas das: DEPENDENCIAS");
		
		Set<EditedFile> aaaab1 = new HashSet<>();
		Set<EditedFile> ccccb2 = new HashSet<>();
		Set<EditedFile> ccccb1 = new HashSet<>();
		Set<EditedFile> aaaab2 = new HashSet<>();
		
		List<EditedFile> filesOnBoth = new ArrayList<>(mergeFiles.getFilesOnBothBranch());
		List<EditedFile> filesOnBranch1 = mergeFiles.getFilesOnBranchOne();
		List<EditedFile> filesOnBranch2 = mergeFiles.getFilesOnBranchTwo();
		List<EditedFile> filesOnHistory = new ArrayList<>(mergeFiles.getFilesOnPreviousHistory());
		
		List<Committer> goldList = new ArrayList<>();
		List<Committer> silverList = new ArrayList<>();
		List<Committer> bronzeList = new ArrayList<>();


		dependencies.entrySet().stream().forEach((dependence) -> {
			
			EditedFile ascendentCand = dependence.getKey();
			int index1 = filesOnBranch1.indexOf(ascendentCand);		
			//verifica se encontrou um ascendent
			if(index1 > -1){
				boolean hasConsequent = false;
				//se tem um ascendent entao vamos procurar o conseguent
				Set<EditedFile> consequentList = dependence.getValue();

				for(EditedFile consequent : consequentList){
				//consequentList.stream().forEach((consequent) -> {
					int index2 = filesOnBranch2.indexOf(consequent);
					if((index2 > -1) && (!ccccb2.contains(consequent))){
						hasConsequent = true;
						ccccb2.add(consequent);
						List<Committer> cmterList2 = findThisFileOnHistory(consequent,filesOnHistory);
						bronzeList.addAll(cmterList2);
						goldList.addAll(filesOnBranch2.get(index2).getWhoEditTheFile());
//						for(Committer cmter : cmterList2)
//							if(!bronzeList.contains(cmter))
//								bronzeList.add(cmter);
//						for(Committer cmter : filesOnBranch2.get(index2).getWhoEditTheFile())
//							if(!goldList.contains(cmter)){
//								goldList.add(cmter);
//							}
					}
				}		
				if((hasConsequent) && (aaaab1.contains(ascendentCand))){
					aaaab1.add(ascendentCand);
					if(!filesOnBoth.contains(ascendentCand)){
						bronzeList.addAll(findThisFileOnHistory(ascendentCand,filesOnHistory));
						silverList.addAll(filesOnBranch1.get(index1).getWhoEditTheFile());
						goldList.addAll(filesOnBranch1.get(index1).getWhoEditTheFile());
					}
//					List<Committer> cmterList = findThisFileOnHistory(ascendentCand,filesOnHistory);
//					for(Committer cmter : cmterList)
//						if(!bronzeList.contains(cmter))
//							bronzeList.add(cmter);
//					if(!filesOnBoth.contains(ascendentCand)){
//						for(Committer cmter : filesOnBranch1.get(index1).getWhoEditTheFile()){
//							if(!goldList.contains(cmter))
//								goldList.add(cmter);
//							if(!silverList.contains(cmter))
//								silverList.add(cmter);
//							}
//					}					
				}		
			
			}
			//Agora vamos fazer para o outro branch
			int indexb2 = filesOnBranch2.indexOf(ascendentCand);		
			//verifica se encontrou um ascendent
			if(indexb2 > -1){
				boolean hasConsequent = false;
				//se tem um ascendent entao vamos procurar o conseguent
				Set<EditedFile> consequentList = dependence.getValue();

				for(EditedFile consequent : consequentList){
				//consequentList.stream().forEach((consequent) -> {
					int indexb1 = filesOnBranch1.indexOf(consequent);
					if((indexb1 > -1) && !(ccccb1.contains(consequent))){
						ccccb1.add(consequent);
						hasConsequent = true;
						goldList.addAll(filesOnBranch1.get(indexb1).getWhoEditTheFile());
						bronzeList.addAll(findThisFileOnHistory(consequent,filesOnHistory));
//						List<Committer> cmterList2 = findThisFileOnHistory(consequent,filesOnHistory);
//						for(Committer cmter : cmterList2)
//							if(!bronzeList.contains(cmter))
//								bronzeList.add(cmter);
//						for(Committer cmter : filesOnBranch1.get(indexb1).getWhoEditTheFile())
//							if(!goldList.contains(cmter)){
//								goldList.add(cmter);
//							}
					}
				}		
				if((hasConsequent) && (!aaaab2.contains(ascendentCand))){
					aaaab2.add(ascendentCand);
					if(!filesOnBoth.contains(ascendentCand)){
						bronzeList.addAll(findThisFileOnHistory(ascendentCand,filesOnHistory));
						silverList.addAll(filesOnBranch2.get(indexb2).getWhoEditTheFile());
						goldList.addAll(filesOnBranch2.get(indexb2).getWhoEditTheFile());
					}
//					List<Committer> cmterList = findThisFileOnHistory(ascendentCand,filesOnHistory);
//					for(Committer cmter : cmterList)
//						if(!bronzeList.contains(cmter))
//							bronzeList.add(cmter);
//					if(!filesOnBoth.contains(ascendentCand)){
//						for(Committer cmter : filesOnBranch2.get(indexb2).getWhoEditTheFile()){
//							if(!goldList.contains(cmter))
//								goldList.add(cmter);
//							if(!silverList.contains(cmter))
//								silverList.add(cmter);
//							}
//					}					
				}		
			
			}
			
		});

		setMedals(goldList, silverList, bronzeList);
		
		System.out.println(this.toString());
		
	}
	
	private void setMedals(Collection<Committer> cmterGold, Collection<Committer> cmterSilver, Collection<Committer> cmterBronze){
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
	

	private List<Committer> findThisFileOnHistory(EditedFile ascendentCand, List<EditedFile> filesOnHistory) {
		int index = filesOnHistory.indexOf(ascendentCand);
		if(index > -1)
			return  filesOnHistory.get(index).getWhoEditTheFile();
		return null;
	}

	
}
