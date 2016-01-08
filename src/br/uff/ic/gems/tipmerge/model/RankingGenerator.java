/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

	public Set<EditedFile> setMedalsFilesEditedBothBranches(MergeFiles mergeFiles) {
		//List<Committer> silver = new ArrayList<>();

		Set<EditedFile> files = new HashSet<>(mergeFiles.getFilesOnBothBranch());
		List<EditedFile> filesHistory = new ArrayList<>(mergeFiles.getFilesOnPreviousHistory());

		for (EditedFile file : files) {
			int indexb1 = mergeFiles.getFilesOnBranchOne().indexOf(file);
			if (indexb1 > -1) {
				//gold.addAll(mergeFiles.getFilesOnBranchOne().get(indexb1).getWhoEditTheFile());
				this.setGoldMedalsB1(mergeFiles.getFilesOnBranchOne().get(indexb1));
			}

			int indexb2 = mergeFiles.getFilesOnBranchTwo().indexOf(file);
			if (indexb2 > -1) {
				//gold.addAll(mergeFiles.getFilesOnBranchTwo().get(indexb2).getWhoEditTheFile());
				this.setGoldMedalsB2(mergeFiles.getFilesOnBranchTwo().get(indexb2));

			}

			int indexh = filesHistory.indexOf(file);
			if (indexh > -1) {
				//silver.addAll(filesHistory.get(indexh).getWhoEditTheFile());
				this.setSilverMedals(filesHistory.get(indexh));
				System.out.println("prata por His (bb)\t" + filesHistory.get(indexh)
					+ "\t para \t" + filesHistory.get(indexh).getWhoEditTheFile());
				//break;
			}
		}
        //Changed the value - silver medals to changed files in history
		//this.setGoldMedals(gold);
		//this.setSilverMedals(silver);
		//System.out.println(this.toString());
		return files;
	}

	public Set<EditedFile> setMedalFromDependenciesBranch1(Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

		dependenciesMap.entrySet().stream().forEach((dependency) -> {

			EditedFile ascendentCand = dependency.getKey();

			EditedFile bronzeRights = ascendentCand;

			if (!excepiontFiles.contains(ascendentCand)) {

				this.setGoldMedalsB1(ascendentCand);

				for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
					if (fileHistory.equals(ascendentCand)) {
						this.setSilverMedals(fileHistory);
						//Changed the value - silver medals to changed files in history
						break;
					}
				}

				excepiontFiles.add(ascendentCand);

			}

			Set<EditedFile> consequentList = dependency.getValue();
			for (EditedFile consequent : consequentList) {

				this.setBronzeMedals(bronzeRights, consequent.getFileName(), 0);
				//	Changed the value - silver medals to changed files with dependencies in branches

				if (!excepiontFiles.contains(consequent)) {

					this.setGoldMedalsB2(consequent);

					for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
						if (fileHistory.equals(consequent)) {
							this.setSilverMedals(fileHistory);
							//Changed the value - silver medals to changed files in history
							break;
						}
					}
					excepiontFiles.add(consequent);
				}
			}

		});

		return excepiontFiles;

	}

	public Set<EditedFile> setMedalFromDependenciesBranch2(Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

		dependenciesMap.entrySet().stream().forEach((dependency) -> {

			EditedFile ascendentCand = dependency.getKey();

			EditedFile bronzeRights = ascendentCand;

			if (!excepiontFiles.contains(ascendentCand)) {

				this.setGoldMedalsB2(ascendentCand);

				for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
					if (fileHistory.equals(ascendentCand)) {
						this.setSilverMedals(fileHistory);
						//Changed the value - silver medals to changed files in history
						break;
					}
				}

				excepiontFiles.add(ascendentCand);

			}

			Set<EditedFile> consequentList = dependency.getValue();
			for (EditedFile consequent : consequentList) {

				this.setBronzeMedals(bronzeRights, consequent.getFileName(), 1);
				//Changed the value - silver medals to changed files with dependencies in branches

				if (!excepiontFiles.contains(consequent)) {

					this.setGoldMedalsB1(consequent);

					for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
						if (fileHistory.equals(consequent)) {
							this.setSilverMedals(fileHistory);
							//Changed the value - silver medals to changed files in history
							break;
						}
					}
					excepiontFiles.add(consequent);
				}
			}

		});

		return excepiontFiles;

	}

	private void setBronzeMedals(EditedFile ascend, String conseq, Integer direction) {
		for (Committer cmter : ascend.getWhoEditTheFile()) {
			Medalist medalist = new Medalist(cmter);
			medalist.addBronzeMedal(conseq, direction);
			int index = ranking.indexOf(medalist);
			if (index == -1) {
				ranking.add(medalist);
				//System.out.println("add\t" + medalist.getCommitter().getName() + "\t" + conseq + "\t" + direction);
			} else {
				//System.out.println("add+\t" + medalist.getCommitter().getName() + "\t" + conseq + "\t" + direction);
				Integer depA = ranking.get(index).getBronzeList().get(conseq);
				//System.out.println(depA + "\t" + direction);
				if (depA != null && !Objects.equals(depA, direction)) {
					ranking.get(index).addBronzeMedal(conseq, 2);
				} else {
					ranking.get(index).addBronzeMedal(conseq, direction);
				}
			}
		}
	}

	private void setSilverMedals(EditedFile file) {
		for (Committer cmter : file.getWhoEditTheFile()) {
			Medalist medalist = new Medalist(cmter);
			medalist.addSilverMedal(file.getFileName());
			int index = ranking.indexOf(medalist);
			if (index == -1) {
				ranking.add(medalist);
			} else {
				ranking.get(index).addSilverMedal(file.getFileName());
			}
		}
	}

	private void setGoldMedalsB1(EditedFile file) {
		for (Committer cmter : file.getWhoEditTheFile()) {
			Medalist medalist = new Medalist(cmter);
			medalist.addGoldMedalBranch1(file.getFileName());
			int index = ranking.indexOf(medalist);
			if (index == -1) {
				ranking.add(medalist);
			} else {
				ranking.get(index).addGoldMedalBranch1(file.getFileName());
			}
		}
	}

	private void setGoldMedalsB2(EditedFile file) {
		for (Committer cmter : file.getWhoEditTheFile()) {
			Medalist medalist = new Medalist(cmter);
			medalist.addGoldMedalBranch2(file.getFileName());
			int index = ranking.indexOf(medalist);
			if (index == -1) {
				ranking.add(medalist);
			} else {
				ranking.get(index).addGoldMedalBranch2(file.getFileName());
			}
		}
	}

	public List<String> getList() {
		Collections.sort(ranking, new Compara());
		List<String> rankList = new ArrayList<>();
		for (Medalist medalist : ranking) {
			rankList.add(medalist.getCommitter().getName());
		}
		return rankList;
	}

	@Override
	public String toString() {
		Collections.sort(ranking, new Compara());
		StringBuilder result = new StringBuilder();
		for (Medalist medalist : ranking) {
			result.append(medalist).append("\n");
		}
		return result.toString();
	}

}
