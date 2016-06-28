/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import br.uff.ic.gems.tipmerge.util.Statistics;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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

    private LinkedList<Medalist> ranking;
    
    private List<Medalist> developers;
    private Medalist fullCoverage;

    private int developersQuantity;
    
    private Set<BitSet> solutions;
    private Map<Integer, BigInteger> totalSolutionsPerQuantity;
    private Map<Integer, BigInteger> numberOfSolutionsPerQuantity;
    
    private int maxDevelopersQuantity = 1;
    private double minCoverage = 0.0;
    private int maxIterations = 100;
    private int maxDuration = 300;
    private int fitness = 0;
    private long firstTime;
    
    private Comparator<Medalist> medalistComparator = new MedalistComparator();
    private Comparator<Medalist> coverageComparator;
    private Medalist bestMedalist = null;

    public RankingGenerator() {
        this.ranking = new LinkedList<>();
    }
   
    /**
     * Create Medalists with medals
     * Initialize solutions (tabu set)
     * @param mergeFiles
     * @param dependenciesBranchOne
     * @param dependenciesBranchTwo
     */
    public void createMedals(MergeFiles mergeFiles, Map<EditedFile, Set<EditedFile>> dependenciesBranchOne, Map<EditedFile, Set<EditedFile>> dependenciesBranchTwo) {
        Set<EditedFile> excepiontFiles = this.setMedalsFilesEditedBothBranches(mergeFiles);
        excepiontFiles = this.setMedalFromDependenciesBranch(1, dependenciesBranchOne, mergeFiles, excepiontFiles);
        excepiontFiles = this.setMedalFromDependenciesBranch(2, dependenciesBranchTwo, mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);
        
        this.initializeData();
    }
    
    public void reset() {
        this.ranking = new LinkedList<>(this.getDevelopers());
        this.initializeData();
    }
    
    /**
     * Initialize solutions (tabu set)
     * Create maps with maximum/current number of combinations
     */
    private void initializeData() {
        this.sortRanking();
        List<Medalist> developersRanking = this.getRanking();
        this.setDevelopers(developersRanking);
        
        this.totalSolutionsPerQuantity = new HashMap<>();
        this.numberOfSolutionsPerQuantity = new HashMap<>();
        this.solutions = new HashSet<>();
        
        int size = this.getDevelopersQuantity();
        
        BitSet full = new BitSet(size);
        for (int i = 0; i < size; i++) {
            full.set(i);
            BitSet solution = new BitSet(size);
            solution.set(i);
            developersRanking.get(i).setConfiguration(solution);
            this.addSolution(solution);
            
            this.numberOfSolutionsPerQuantity.put(i + 1, BigInteger.ZERO);
        
            if (i <= size / 2) {
                BigInteger combinations = Statistics.combination(size, i + 1);
                this.totalSolutionsPerQuantity.put(i + 1, combinations);
                this.totalSolutionsPerQuantity.put(size - i - 1, combinations);
            }
        }
        this.totalSolutionsPerQuantity.put(0, BigInteger.ONE);
        this.totalSolutionsPerQuantity.put(size, BigInteger.ONE);
        this.numberOfSolutionsPerQuantity.put(0, BigInteger.ZERO);
        this.numberOfSolutionsPerQuantity.put(1, BigInteger.valueOf(size));
        this.numberOfSolutionsPerQuantity.put(size, BigInteger.ZERO);
        
        this.setMinCoverage(0.0);
        this.setFullCoverage(this.createSolution(full, false));
    }
    
    /**
     * @param maxDevelopers: maximum number of developers
     * @param minCoverage: minimum coverage
     * @return boolean indicating if initial solution were found
     */
    private boolean initialSolution(int maxDevelopers, double minCoverage) {
        boolean created = false;
        this.setMaxDevelopersQuantity(maxDevelopers);
        this.setMinCoverage(minCoverage);
        
        // It should use i < this.getMaxDevelopersQuantity()
        // because createSolution changes maxDevelopersQuantity if minCoverage > 0
        BitSet configuration = new BitSet(this.developers.size());
        for (int i = 0; i < this.getMaxDevelopersQuantity(); i++) {
            configuration.set(i);
            if (this.createSolution(configuration) != null) {
                created = true;
            }
        }
        
        return created;
    }
    
    /**
     * Combine developers to generate new solutions
     * @param maxDevelopers
     * @param minCoverage
     * @param maxIterations
     * @param maxDuration
     * @param fitness 
     */
    public void combineDevelopers(int maxDevelopers, double minCoverage, int maxIterations, int maxDuration, int fitness) {
        this.setFirstTime(System.currentTimeMillis());
        this.setMaxDuration(maxDuration);
        this.setMaxIterations(maxIterations);
        this.setFitness(fitness);
        this.sortRanking();
        if (this.initialSolution(maxDevelopers, minCoverage)) {
            this.metaheuristic();
        } else {
            System.out.println("No initial solution found");
        }
    }
    
    /**
     * Run metaheuristic
     */
    private void metaheuristic() {
        int changed = 0;
        int iter = 0;
        long start = this.getFirstTime();
        long delta = start - System.currentTimeMillis();
        long maxTime = this.getMaxDuration() * 1000;
        maxTime = (maxTime == 0) ? delta + 1 : maxTime;

        while (delta < maxTime && (iter - changed < this.getMaxIterations())) {
            iter++;
            while (this.increaseDevelopers(this.getBestConfiguration())) {
                changed = iter;
            }
            while (this.decreaseDevelopers(this.getBestConfiguration())) {
                changed = iter;
            }
            if (this.mutateN(this.getBestConfiguration())) {
                changed = iter;
            }
            delta = start - System.currentTimeMillis();
            maxTime = (maxTime == 0) ? delta + 1 : maxTime;
        }
    }
    
    /**
     * Create solution and include in the solutions
     * @param configuration
     * @return Medalist solution
     */
    private Medalist createSolution(BitSet configuration) {
        return this.createSolution(configuration, true);
    }
    
    /**
     * Create solution
     * @param configuration
     * @param include
     * @return Medalist solution
     */
    private Medalist createSolution(BitSet configuration, boolean include) {
        configuration = (BitSet) configuration.clone();
        if (this.inSolutions(configuration)) {
            return null;
        }

        int quantity = 0;
        List<Medalist> devs = this.getDevelopers();
        List<Medalist> selected = new ArrayList<>();
        for (int i = configuration.nextSetBit(0); i != -1; i = configuration.nextSetBit(i + 1)) {
            selected.add(devs.get(i));
            quantity += 1;
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

        combinedMedalist.setConfiguration(configuration);
        if (include) {
            this.addSolution(configuration);
            this.incrementQuantity(quantity);
            
            double minCoverage = this.getMinCoverage();
            if (minCoverage > 0.0) {
                double coverage = this.getCoverageComparator().coverage(combinedMedalist);
                if (coverage < minCoverage) {
                    return null;
                } else {
                    this.setMaxDevelopersQuantity(quantity);
                }
            }
            
            if (this.getComparator().compare(combinedMedalist, this.getBestMedalist()) < 0) {
                this.setBestMedalist(combinedMedalist);
            }
            this.addToRanking(combinedMedalist);
        }
        return combinedMedalist;
    }
    
    /**
     * Decrease number of developers
     * @param configuration
     * @return true if it was possible
     */
    private boolean decreaseDevelopers(BitSet configuration) {
        int size = this.getDevelopersQuantity();
        ArrayList<Integer> bits1 = new ArrayList<>();
        BitSet newConfiguration = new BitSet(size);
        int bits1Quantity = 0;
        boolean result = false;
        
        for (int i = 0; i < size; i++) {
            if (configuration.get(i)) {
                bits1.add(i);
                bits1Quantity += 1;
                newConfiguration.set(i);
            }
        }
        
        if (bits1Quantity == 1) {
            return result;
        }
        
        for (Integer bit1 : bits1) {
            if (this.hasTriedAll(bits1Quantity - 1)) {
                break;
            }
            newConfiguration.set(bit1, false);
            Medalist newSolution = this.createSolution(newConfiguration);
            result |= (newSolution != null);
            newConfiguration.set(bit1, true);
        }
        
        return result;
    }
    
    /**
     * Increase number of developers
     * @param configuration
     * @return true if it was possible
     */
    private boolean increaseDevelopers(BitSet configuration) {
        int size = this.getDevelopersQuantity();
        ArrayList<Integer> bits0 = new ArrayList<>();
        BitSet newConfiguration = new BitSet(size);
        int bits1Quantity = 0;
        boolean result = false;
        
        for (int i = 0; i < size; i++) {
            if (configuration.get(i)) {
                bits1Quantity++;
                newConfiguration.set(i);
            } else {
                bits0.add(i);
            }
        }
        
        if (bits1Quantity == this.getMaxDevelopersQuantity() || bits1Quantity == size) {
            return result;
        }
        
        for (Integer bit0 : bits0) {
            if (this.hasTriedAll(bits1Quantity + 1)) {
                break;
            }
            newConfiguration.set(bit0, true);
            Medalist newSolution = this.createSolution(newConfiguration);
            result |= (newSolution != null);                    
            newConfiguration.set(bit0, false);
        }
        return result;
    }
    
    /**
     * Mutate solution
     * @param configuration
     * @param quantity
     * @return all mutations
     */
    private ArrayList<Medalist> mutate(BitSet configuration, int quantity) {
        int size = this.getDevelopersQuantity();
        ArrayList<Medalist> result = new ArrayList<>();
        ArrayList<Integer> bits0 = new ArrayList<>();
        ArrayList<Integer> bits1 = new ArrayList<>();
        BitSet newConfiguration = new BitSet(size);
        int bits1Quantity = 0;
        int bits0Quantity = 0;
        
        for (int i = 0; i < size; i++) {
            if (configuration.get(i)) {
                bits1.add(i);
                bits1Quantity++;
                newConfiguration.set(i);
            } else {
                bits0Quantity++;
                bits0.add(i);
            }
        }
        
        Collections.shuffle(bits0);
        Collections.shuffle(bits1);
        
        if (quantity == 0) {
            quantity = bits0Quantity;
        }
        quantity = Math.min(quantity, bits1Quantity*bits0Quantity);
        
        // Swap persons on the solution
        for (Integer bit1 : bits1) {
            newConfiguration.set(bit1, false);
            for (Integer bit0 : bits0) {
                if (this.hasTriedAll(bits1Quantity)) {
                    return result;
                }
                newConfiguration.set(bit0, true);
                Medalist newSolution = this.createSolution(newConfiguration);
                if (newSolution != null) {
                    result.add(newSolution);
                    quantity--;
                    if (quantity == 0) {
                        return result;
                    }
                }
                newConfiguration.set(bit0, false);
            }
            newConfiguration.set(bit1, true);
        }
        
        return result;
    }
    
    /**
     * Mutate solution once
     * @param configuration
     * @return 
     */
    private boolean mutate1(BitSet configuration) {
        List<Medalist> result = this.mutate(configuration, 1);
        return (result.size() > 0);
    }
    
    /**
     * Mutate solution for each bit 0
     * @param configuration
     * @return 
     */
    private boolean mutateN(BitSet configuration) {
        List<Medalist> result = this.mutate(configuration, 0);
        return (result.size() > 0);
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
                this.addToRanking(medalist);
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
                this.addToRanking(medalist);
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
                this.addToRanking(medalist);
            } else {
                ranking.get(index).addGoldMedal(file.getFileName(), branch);
            }
        }
    }

    public List<String> getList() {
        Collections.sort(ranking, this.getComparator());
        List<String> rankList = new ArrayList<>();
        for (Medalist medalist : ranking) {
            rankList.add(medalist.getCommitter().getName());
        }
        return rankList;
    }

    @Override
    public String toString() {
        Collections.sort(ranking, this.getComparator());
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

    /**
     * Insert solution to ranking
     * @param medalist
     * @return 
     */
    public boolean addToRanking(Medalist medalist) {
        ListIterator<Medalist> itr = this.getRanking().listIterator();
        while(true) {
            if (itr.hasNext() == false) {
                itr.add(medalist);
                return true;
            }
            Medalist elementInList = itr.next();
            if (this.getComparator().compare(elementInList, medalist) > 0) {
                itr.previous();
                itr.add(medalist);
                return true;
            } else if (this.getMedalistComparator().compare(elementInList, medalist) == 0) {
                System.out.println("repeated " + medalist);
                return false;
            }
        }
    }

    /**
     * @return metaheuristic start time
     */
    public long getFirstTime() {
        return firstTime;
    }

    /**
     * Set metaheuristic start time
     * @param firstTime 
     */
    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }
    
    /**
     * Set comparator:
     * 0: medalistComparator
     * 1: coverageComparator
     * @param fitness 
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    
    /**
     * @return maximum duration (seconds)
     */
    public int getMaxDuration() {
        return maxDuration;
    }
    
    /**
     * @param maxDuration (seconds)
     */
    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }
    
    /**
     * @return number of developers limit for a solution
     */
    public int getMaxDevelopersQuantity() {
        return maxDevelopersQuantity;
    }

    /**
     * @param maxDevelopersQuantity: number of developers limit for a solution
     */
    public void setMaxDevelopersQuantity(int maxDevelopersQuantity) {
        int devs = this.getDevelopersQuantity();
        
        this.maxDevelopersQuantity = 
                (maxDevelopersQuantity <= 0) ? 
                devs : Math.min(devs, maxDevelopersQuantity);
    }
    
    /**
     * @return total number of developers
     */
    public int getDevelopersQuantity() {
        return developersQuantity;
    }
    
    /**
     * @return developers list
     */
    public List<Medalist> getDevelopers() {
        return developers;
    }

    /**
     * @param developers
     */
    public void setDevelopers(List<Medalist> developers) {
        this.developers = new ArrayList<>(developers);
        this.developersQuantity = developers.size();
    }

    /**
     * @return the ranking
     */
    public List<Medalist> getRanking() {
        return ranking;
    }

    /**
     * @param configuration
     * @return true if solutions (tabu list) contains configuration 
     */
    public boolean inSolutions(BitSet configuration) {
        return solutions.contains(configuration);
    }
    
    /**
     * @param configuration
     * Add configuration to solutions (tabu list)
     */
    public void addSolution(BitSet configuration) {
        this.solutions.add(configuration);
    }
    
    /**
     * Increment number of solutions for quantity
     * @param quantity 
     */
    public void incrementQuantity(int quantity) {
        this.numberOfSolutionsPerQuantity.merge(quantity, BigInteger.ONE, BigInteger::add);
    }

    /**
     * @return best medalist
     */
    public Medalist getBestMedalist() {
        return bestMedalist;
    }

    /**
     * @param bestMedalist 
     */
    public void setBestMedalist(Medalist bestMedalist) {
        this.bestMedalist = bestMedalist;
    }
    
    /**
     * Sort ranking according to comparator
     */
    public void sortRanking() {
        Collections.sort(this.ranking, this.getComparator());
        this.setBestMedalist(this.ranking.get(0));
    }

    /**
     * @param fullCoverage medalist 
     */
    public void setFullCoverage(Medalist fullCoverage) {
        this.coverageComparator = new CoverageComparator(fullCoverage);
        this.fullCoverage = fullCoverage;
    }

    /**
     * @return minimum coverage
     */
    public double getMinCoverage() {
        return minCoverage;
    }

    /**
     * @param minCoverage
     */
    public void setMinCoverage(double minCoverage) {
        this.minCoverage = minCoverage;
    }

    /**
     * @return maximum number of iterations without changes
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    /**
     * @param maxIterations 
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
    
    /**
     * @return defined comparator
     */
    public Comparator<Medalist> getComparator() {
        if (this.fitness == 0) {
            return this.medalistComparator;
        } else {
            return this.coverageComparator;
        }
    }

    /**
     * @return comparator that compares number of medals
     */
    public Comparator<Medalist> getMedalistComparator() {
        return this.medalistComparator;
    }
    
    /**
     * @return comparator that computes coverage
     */
    public CoverageComparator getCoverageComparator() {
        return (CoverageComparator) this.coverageComparator;
    }
    
    /**
     * @return best configuration
     */
    public BitSet getBestConfiguration() {
        return this.bestMedalist.getConfiguration();
    }
    
    /**
     * @param quantity
     * @return check if it has exhausted quantity
     */
    public boolean hasTriedAll(int quantity) {
        return (numberOfSolutionsPerQuantity.get(quantity)
            .equals(totalSolutionsPerQuantity.get(quantity)));
    }
    
}
