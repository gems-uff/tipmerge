/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author j2cf
 */
public class RankingGenerator {

    private List<Medalist> ranking = new ArrayList<>();
    private List<Medalist> developers;
    private Set<BitSet> solutions;
    private int developersQuantity = 1;

    public int getDevelopersQuantity() {
        return developersQuantity;
    }

    public void setDevelopersQuantity(int developersQuantity) {
        this.developersQuantity = developersQuantity;
    }

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

    public void createMedals(MergeFiles mergeFiles, Map<EditedFile, Set<EditedFile>> dependenciesBranchOne, Map<EditedFile, Set<EditedFile>> dependenciesBranchTwo) {
        Set<EditedFile> excepiontFiles = this.setMedalsFilesEditedBothBranches(mergeFiles);
        excepiontFiles = this.setMedalFromDependenciesBranch(1, dependenciesBranchOne, mergeFiles, excepiontFiles);
        excepiontFiles = this.setMedalFromDependenciesBranch(2, dependenciesBranchTwo, mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);

        developers = new ArrayList<>(this.getRanking());
        int size = developers.size();
        solutions = new HashSet<>();
        for (int i = 0; i < size; i++) {
            BitSet solution = new BitSet(size);
            solution.set(i);
            solutions.add(solution);
        }
    }

    public void combineDevelopers(int quantity) {
        quantity = Math.min(quantity, this.developers.size());
        this.setDevelopersQuantity(quantity);

        BitSet configuration = new BitSet(this.developers.size());
        for (int i = 0; i < quantity; i++) {
            configuration.set(i);
        }

        this.createSolution(configuration);
    }

    public Medalist createSolution(BitSet configuration) {
        if (solutions.contains(configuration)) {
            return null;
        }

        List<Medalist> selected = new ArrayList<>();
        for (int i = configuration.nextSetBit(0); i != -1; i = configuration.nextSetBit(i + 1)) {
            selected.add(this.developers.get(i));
        }

        List<Committer> committers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        Set<String> goldListBranch1 = new TreeSet<>();
        Set<String> goldListBranch2 = new TreeSet<>();
        Set<String> silverList = new TreeSet<>();
        Map<String, MedalBronze> bronzeList = new HashMap<>();

        for (Medalist medalist : selected) {
            Committer committer = medalist.getCommitter();
            committers.add(committer);
            goldListBranch1.addAll(medalist.getGoldListBranch1());
            goldListBranch2.addAll(medalist.getGoldListBranch2());
            silverList.addAll(medalist.getSilverList());

            for (Map.Entry<String, MedalBronze> entry : medalist.getBronzeList().entrySet()) {
                MedalBronze oldBronze = bronzeList.get(entry.getKey());
                MedalBronze newBronze = entry.getValue();
                if (oldBronze == null) {
                    bronzeList.put(entry.getKey(), newBronze);
                } else {
                    if (!oldBronze.getDirection().equals(newBronze.getDirection())) {
                        oldBronze.setDirection(9);
                    }
                    Map<String, Integer> oldFileDepend = oldBronze.getFileDepend();
                    for (Map.Entry<String, Integer> fileDependEntry : newBronze.getFileDepend().entrySet()) {
                        Integer oldBranch = oldFileDepend.get(fileDependEntry.getKey());
                        Integer newBranch = fileDependEntry.getValue();
                        if (oldBranch == null) {
                            oldFileDepend.put(fileDependEntry.getKey(), newBranch);
                        } else {
                            if (!oldBranch.equals(newBranch)) {
                                oldFileDepend.put(fileDependEntry.getKey(), 2);
                            }
                        }
                    }
                }
            }
            names.add(committer.getName().trim());
            emails.add(committer.getEmail().trim());
        }

        String combinedName = names.stream().collect(Collectors.joining(", "));
        String combinedEmail = emails.stream().collect(Collectors.joining(", "));

        Committer combinedCommitter = new CombinedCommitter(committers, combinedName, combinedEmail);
        Medalist combinedMedalist = new Medalist(combinedCommitter);
        combinedMedalist.setGoldListBranch1(goldListBranch1);
        combinedMedalist.setGoldListBranch2(goldListBranch2);
        combinedMedalist.setSilverList(silverList);
        combinedMedalist.setBronzeList(bronzeList);

        this.ranking.add(combinedMedalist);
        solutions.add(configuration);

        return combinedMedalist;
    }

    public Set<EditedFile> setMedalsFilesEditedBothBranches(MergeFiles mergeFiles, int mode) {
        // Modes:
        // 1 -> setSilverMedals [default, A, C]
        // 2 -> setBronzeMedals [B]
        Set<EditedFile> files = new HashSet<>(mergeFiles.getFilesOnBothBranch());
        List<EditedFile> filesHistory = new ArrayList<>(mergeFiles.getFilesOnPreviousHistory());

        for (EditedFile file : files) {
            int indexb1 = mergeFiles.getFilesOnBranchOne().indexOf(file);
            if (indexb1 > -1) {
                this.setGoldMedals(mergeFiles.getFilesOnBranchOne().get(indexb1), 1);
            }

            int indexb2 = mergeFiles.getFilesOnBranchTwo().indexOf(file);
            if (indexb2 > -1) {
                this.setGoldMedals(mergeFiles.getFilesOnBranchTwo().get(indexb2), 2);
            }

            int indexh = filesHistory.indexOf(file);
            if (indexh > -1) {
                if (mode == 1) {
                    this.setSilverMedals(filesHistory.get(indexh));
                } else if (mode == 2) {
                    this.setBronzeMedals(filesHistory.get(indexh), file.getFileName(), 9);
                }
            }
        }
        return files;
    }

    public Set<EditedFile> setMedalsFilesEditedBothBranches(MergeFiles mergeFiles) {
        return this.setMedalsFilesEditedBothBranches(mergeFiles, 1);
    }
    
    public Set<EditedFile> setMedalsFilesEditedBothBranchesA(MergeFiles mergeFiles) {
        return this.setMedalsFilesEditedBothBranches(mergeFiles, 1);
    }
    
    public Set<EditedFile> setMedalsFilesEditedBothBranchesB(MergeFiles mergeFiles) {
        return this.setMedalsFilesEditedBothBranches(mergeFiles, 2);
    }
    
    public Set<EditedFile> setMedalsFilesEditedBothBranchesC(MergeFiles mergeFiles) {
        return this.setMedalsFilesEditedBothBranches(mergeFiles, 1);
    }
    
    public Set<EditedFile> setMedalFromDependenciesBranch(int branch, Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

        int otherBranch = (branch == 1)? 2 : 1;
        
        dependenciesMap.entrySet().stream().forEach((dependency) -> {

            EditedFile ascendentCand = dependency.getKey();
            EditedFile bronzeRights = ascendentCand;

            if (!excepiontFiles.contains(ascendentCand)) {

                this.setGoldMedals(ascendentCand, branch);

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

                this.setBronzeMedals(bronzeRights, consequent.getFileName(), branch - 1);
                //	Changed the value - silver medals to changed files with dependencies in branches

                if (!excepiontFiles.contains(consequent)) {

                    this.setGoldMedals(consequent, otherBranch);

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

    public Set<EditedFile> setMedalFromDependenciesBranchA(int branch, Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

        dependenciesMap.entrySet().stream().forEach((dependency) -> {

            EditedFile ascendentCand = dependency.getKey();
            EditedFile bronzeRights = ascendentCand;

            if (!excepiontFiles.contains(ascendentCand)) {

                excepiontFiles.add(ascendentCand);
            }

            Set<EditedFile> consequentList = dependency.getValue();
            for (EditedFile consequent : consequentList) {

                //testA - Bronze to indirect key files in the branches 
                this.setBronzeMedals(bronzeRights, consequent.getFileName(), branch - 1);
                if (!excepiontFiles.contains(consequent)) {
                    excepiontFiles.add(consequent);
                }
            }

        });
        return excepiontFiles;
    }
    public Set<EditedFile> setMedalFromDependenciesBranchB(int branch, Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

        dependenciesMap.entrySet().stream().forEach((dependency) -> {

            EditedFile ascendentCand = dependency.getKey();
            if (!excepiontFiles.contains(ascendentCand)) {

                for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
                    if (fileHistory.equals(ascendentCand)) {
                        this.setBronzeMedals(fileHistory,ascendentCand.getFileName(),9);
                        //Changed the value - silver medals to changed files in history
                        break;
                    }
                }
                excepiontFiles.add(ascendentCand);
            }

            Set<EditedFile> consequentList = dependency.getValue();
            for (EditedFile consequent : consequentList) {

                this.setSilverMedals(ascendentCand);

                if (!excepiontFiles.contains(consequent)) {
                    for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
                        if (fileHistory.equals(consequent)) {
                            this.setBronzeMedals(fileHistory,ascendentCand.getFileName(),9);
                            break;
                        }
                    }

                    this.setSilverMedals(consequent);

                }excepiontFiles.add(consequent);
                
            }

        });
        return excepiontFiles;
    }    
    public Set<EditedFile> setMedalFromDependenciesBranchC(int branch, Map<EditedFile, Set<EditedFile>> dependenciesMap, MergeFiles mergeFiles, Set<EditedFile> excepiontFiles) {

        dependenciesMap.entrySet().stream().forEach((dependency) -> {

            EditedFile ascendentCand = dependency.getKey();

            if (!excepiontFiles.contains(ascendentCand)) {

                for (EditedFile fileHistory : mergeFiles.getFilesOnPreviousHistory()) {
                    if (fileHistory.equals(ascendentCand)) {
                        this.setBronzeMedals(fileHistory, ascendentCand.getFileName(), 9);
                        //Changed the value - silver medals to changed files in history
                        break;
                    }
                }
                excepiontFiles.add(ascendentCand);
            }

            Set<EditedFile> consequentList = dependency.getValue();
            for (EditedFile consequent : consequentList) {
                this.setSilverMedals(ascendentCand);
            }

        });
        return excepiontFiles;
    }


    private void setBronzeMedals(EditedFile ascend, String conseq, Integer direction) {
        //System.out.println("ascend: " + ascend + "\tconseq: " + conseq + "\tDir: " + direction);
        //System.out.println("Quem?" + ascend.getWhoEditTheFile());
        for (Committer cmter : ascend.getWhoEditTheFile()) {
            Medalist medalist = new Medalist(cmter);
            medalist.addBronzeMedal(conseq, ascend.getFileName(), direction);
            int index = ranking.indexOf(medalist);
            if (index == -1) 
                ranking.add(medalist);
            else {
                Integer direcDepA = ranking.get(index).directionFromFileDepend(conseq);
                if (direcDepA != -1 && !Objects.equals(direcDepA, direction))
                    ranking.get(index).addBronzeMedal(conseq, ascend.getFileName(), 2);
                 else 
                    ranking.get(index).addBronzeMedal(conseq, ascend.getFileName(), direction);
               
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

    private void setGoldMedals(EditedFile file, int branch) {
        for (Committer cmter : file.getWhoEditTheFile()) {
            Medalist medalist = new Medalist(cmter);
            medalist.addGoldMedal(file.getFileName(), branch);
            int index = ranking.indexOf(medalist);
            if (index == -1) {
                ranking.add(medalist);
            } else {
                ranking.get(index).addGoldMedal(file.getFileName(), branch);
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
    
    public Medalist getMedalist(String name){
        for(Medalist m : this.ranking){
            if(m.getCommitter().getName().equals(name))
                return m;
        }
        return null;
    }
    
}
