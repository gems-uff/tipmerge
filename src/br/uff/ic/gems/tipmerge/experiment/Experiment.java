/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.experiment;

import arch.Session;
import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.dominoes.DominoesFiles;
import br.uff.ic.gems.tipmerge.gui.JFrameDependencies;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Dependencies;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import br.uff.ic.gems.tipmerge.model.Repository;
import domain.Dominoes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j2cf
 */
public class Experiment {

    private final Repository repository;

    public Experiment(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepo() {
        return this.repository;
    }

    public Map<String, Integer> getDatasFromMerges(File file) throws IOException {
        Map<String, Integer> mapValues = new HashMap<>();

        mapValues.put("Merges", 0);
        mapValues.put("Files", 0);
        mapValues.put("Dependencies", 0);
        mapValues.put("Conflicts", 0);

        Map<String, Integer> rankMapA = new HashMap<>();
        rankMapA.put("1stPosition", 0);
        rankMapA.put("2ndPosition", 0);
        rankMapA.put("3thPosition", 0);
        rankMapA.put("isInRank", 0);
        rankMapA.put("outOfRank", 0);

        Map<String, Integer> rankMapB = new HashMap<>();
        rankMapB.put("1stPosition", 0);
        rankMapB.put("2ndPosition", 0);
        rankMapB.put("3thPosition", 0);
        rankMapB.put("isInRank", 0);
        rankMapB.put("outOfRank", 0);

        Map<String, Integer> rankMapC = new HashMap<>();
        rankMapC.put("1stPosition", 0);
        rankMapC.put("2ndPosition", 0);
        rankMapC.put("3thPosition", 0);
        rankMapC.put("isInRank", 0);
        rankMapC.put("outOfRank", 0);

        //quant. merges | arq 2 lados | arq dep | conflitos | ranking (1,2,3,9,0) -> 1a 2a 3a posição, no rank, fora do rank
        List<String> mergesList = this.getRepo().getListOfMerges();
        for (int i = mergesList.size() - 1; i >= 0; i--) {
            String hashMerge = mergesList.get(i);
            try (BufferedWriter bwOutput = new BufferedWriter(new FileWriter(file, true))) {
                //Integer[] values = new Integer[4];
                DateFormat timeFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss.SSS");
                String formattedDate = timeFormat.format(new Date());

                System.out.println("Merge under review: " + hashMerge + "\tTime: " + formattedDate);
                bwOutput.write("Merge under review: " + hashMerge + "\tTime: " + formattedDate + "\n");

                MergeFiles mergeFiles = getFilesAnalisys(hashMerge.split(" ")[0]);

                if (hasEnoughtDevelopers(mergeFiles)) {

                    System.out.println("\t1. Enough Committer:\tYES");
                    bwOutput.write("\t1. Enough Committer:\tYES" + "\n");
                    mapValues.put("Merges", mapValues.get("Merges") + 1);

                    int filesInCommon = mergeFiles.getFilesOnBothBranch().size();

                    if (filesInCommon == 0) {

                        System.out.println("\t2. Files in common:\tNO");
                        bwOutput.write("\t2. Files in common:\tNO" + "\n");

                    } else {

                        System.out.println("\t2. Files in common:\tYES\t" + filesInCommon);
                        bwOutput.write("\t2. Files in common:\tYES\t" + filesInCommon + "\n");

                        mapValues.put("Files", mapValues.get("Files") + 1);

                        //testa conflitos
                        if (RevisionAnalyzer.hasConflict(this.getRepo().getProject().toString(), mergeFiles.getParents()[0], mergeFiles.getParents()[1])) {
                            System.out.println("\t4. Conflicting files:\tYES");
                            bwOutput.write("\t4. Conflicting files:\tYES" + "\n");

                            mapValues.put("Conflicts", mapValues.get("Conflicts") + 1);
                        } else {
                            System.out.println("\t4. Conflicting files:\tNO");
                            bwOutput.write("\t4. Conflicting files:\tNO" + "\n");
                        }

                    }

                    List<Map<EditedFile, Set<EditedFile>>> dependencies = getFilesDependencies(mergeFiles);
                    boolean hasFilesDependencies = !(dependencies.get(0).isEmpty() && dependencies.get(1).isEmpty());

                    if (!hasFilesDependencies) {
                        System.out.println("\t3. Dependencies:\tNO");
                        bwOutput.write("\t3. Dependencies:\tNO" + "\n");

                    } else {
                        System.out.println("\t3. Dependencies:\tYES");
                        bwOutput.write("\t3. Dependencies:\tYES" + "\n");

                        mapValues.put("Dependencies", mapValues.get("Dependencies") + 1);

                    }
                    if ((filesInCommon > 0) || (hasFilesDependencies)) {

                        //RankingGenerator rGenerator = getRanking(dependencies, mergeFiles);
                        //showRanking(rGenerator, hashMerge, bwOutput, mapValues);
                        Committer committer = CommitterDao.getCommitter1(hashMerge.split(" ")[0], this.getRepo());
                        System.out.println("\tMerge Integrator:\t" + " " + "\t" + committer.getName());
                        bwOutput.write("\tMerge Integrator:\t" + " " + "\t" + committer.getName() + "\n");

						System.out.println("DEPENDENCIES MAP");
						System.out.println(Arrays.toString(dependencies.toArray()));
                        bwOutput.write("DEPENDENCIES MAP\n" + Arrays.toString(dependencies.toArray()) + "\n");

                        //System.out.println("TEST A");
                        //bwOutput.write("TEST A" + "\n");
                        //RankingGenerator rGeneratorA = getRankingA(dependencies, mergeFiles);
                        //showRanking(rGeneratorA, hashMerge, bwOutput, rankMapA);

                        System.out.println("TEST B");
                        bwOutput.write("TEST B" + "\n");
                        RankingGenerator rGeneratorB = getRankingB(dependencies, mergeFiles);
                        showRanking(rGeneratorB, hashMerge, bwOutput, rankMapB);

                        //System.out.println("TEST C");
                        //bwOutput.write("TEST C" + "\n");
                        //RankingGenerator rGeneratorC = getRankingC(dependencies, mergeFiles);
                        //showRanking(rGeneratorC, hashMerge, bwOutput, rankMapC);

                    }

                } else {
                    System.out.println("\t1. Enough Commiter:\tNO");
                    bwOutput.write("\t1. Enough Commiter:\tNO" + "\n");
                }
            }

            Session.debugInfo();

            System.gc();

        }
        showFinalResults(mapValues, rankMapB, file);
        //showFinalResults(mapValues, rankMapA, file);
        //showFinalResults(mapValues, rankMapC, file);

        //Git.checkoutMaster(this.getRepo().getProject());
        return mapValues;
    }

    private void showRanking(RankingGenerator rGenerator, String hashMerge, final BufferedWriter bwOutput, Map<String, Integer> mapValues) throws IOException {
        List<Medalist> ranking = rGenerator.getRanking();

        Committer committer = CommitterDao.getCommitter1(hashMerge.split(" ")[0], this.getRepo());
        int position = ranking.indexOf(new Medalist(committer)) + 1;
        System.out.println("\t4. Rank position:\t" + position);
        bwOutput.write("\t4. Rank position:\t" + position + "\n");

        if (position == 1) {
            mapValues.put("1stPosition", mapValues.get("1stPosition") + 1);
        } else if (position == 2) {
            mapValues.put("2ndPosition", mapValues.get("2ndPosition") + 1);
        } else if (position == 3) {
            mapValues.put("3thPosition", mapValues.get("3thPosition") + 1);
        } else if (position != 0) {
            mapValues.put("isInRank", mapValues.get("isInRank") + 1);
        } else {
            mapValues.put("outOfRank", mapValues.get("outOfRank") + 1);
        }

        System.out.println(rGenerator.toString());
        bwOutput.write(Arrays.toString(rGenerator.getList().toArray()) + "\n");
    }

    /**
     * public Map<String, Integer> getDatasFromMerges() { //
     * Map<String, Integer[]> datas = new HashMap<>(); Map<String, Integer>
     * mapValues = new HashMap<>();
     *
     * mapValues.put("Merges", 0); mapValues.put("Files", 0);
     * mapValues.put("Dependencies", 0); mapValues.put("Conflicts", 0);
     * mapValues.put("1stPosition", 0); mapValues.put("2ndPosition", 0);
     * mapValues.put("3thPosition", 0); mapValues.put("isInRank", 0);
     * mapValues.put("outOfRank", 0); //quant. merges | arq 2 lados | arq dep |
     * conflitos | ranking (1,2,3,9,0) -> 1a 2a 3a posição, no rank, fora do
     * rank
     *
     * List<String> mergesList = this.getRepo().getListOfMerges(); for(int i =
     * mergesList.size() - 1 ; i >= 0 ; i-- ){ String hashMerge =
     * mergesList.get(i); //for (String hashMerge :
     * this.getRepo().getListOfMerges()) {
     *
     * //Integer[] values = new Integer[4]; DateFormat timeFormat = new
     * SimpleDateFormat("dd/MM/YYYY - HH:mm:ss.SSS"); String formattedDate =
     * timeFormat.format(new Date());
     *
     * System.out.println("Merge under review: " + hashMerge + "\tTime: " +
     * formattedDate);
     *
     * MergeFiles mergeFiles = getFilesAnalisys(hashMerge.split(" ")[0]);
     * boolean hasDevelopers = hasEnoughtDevelopers(mergeFiles);
     *
     * if (hasDevelopers) {
     *
     * System.out.println("\t1. Enough Committer:\tYES");
     * mapValues.put("Merges", mapValues.get("Merges") + 1);
     *
     * int filesInCommon = mergeFiles.getFilesOnBothBranch().size(); //
     * values[1] = filesInCommon;
     *
     * if (filesInCommon == 0) {
     *
     * System.out.println("\t2. Files in common:\tNO");
     *
     * } else {
     *
     * System.out.println("\t2. Files in common:\tYES\t" + filesInCommon);
     * mapValues.put("Files", mapValues.get("Files") + 1);
     *
     * //testa conflitos if
     * (RevisionAnalyzer.hasConflict(this.getRepo().getProject().toString(),
     * mergeFiles.getParents()[0], mergeFiles.getParents()[1])) {
     * System.out.println("\t4. Conflicting files:\tYES\tYES\tYES\tYES\t");
     * mapValues.put("Conflicts", mapValues.get("Conflicts") + 1); } else {
     * System.out.println("\t4. Conflicting files:\tNO"); }
     *
     * }
     *
     * List<Map<EditedFile, Set<EditedFile>>> dependencies =
     * getFilesDependencies(mergeFiles); boolean hasNoDependencies =
     * (dependencies.get(0).isEmpty() && dependencies.get(1).isEmpty());
     *
     * if (hasNoDependencies) { //	values[2] = 0; System.out.println("\t3.
     * Dependencies:\tNO");
     *
     * } else { //	values[2] = 1; System.out.println("\t3. Dependencies:\tYES");
     * mapValues.put("Dependencies", mapValues.get("Dependencies") + 1);
     *
     * }
     * if ((filesInCommon > 0) || (!hasNoDependencies)) {
     *
     * RankingGenerator rGenerator = getRanking(dependencies, mergeFiles);
     * List<Medalist> ranking = rGenerator.getRanking();
     *
     * Committer committer = CommitterDao.getCommitter1(hashMerge.split(" ")[0],
     * this.getRepo());
     *
     * int position = ranking.indexOf(new Medalist(committer)) + 1; //	values[3]
     * = position; System.out.println("\t4. Rank position:\t" + position + "\t"
     * + committer.getName());
     *
     * if (position == 1) { mapValues.put("1stPosition",
     * mapValues.get("1stPosition") + 1); } else if (position == 2) {
     * mapValues.put("2ndPosition", mapValues.get("2ndPosition") + 1); } else if
     * (position == 3) { mapValues.put("3thPosition",
     * mapValues.get("3thPosition") + 1); } else if (position != 0) {
     * mapValues.put("isInRank", mapValues.get("isInRank") + 1); } else {
     * mapValues.put("outOfRank", mapValues.get("outOfRank") + 1); }
     *
     * //System.out.println(rGenerator.toString()); }
     *
     * } else { System.out.println("\t1. Enough Commiter:\tNO"); }
     *
     * //datas.put(hashMerge, values); System.gc(); } //
     * Git.checkoutMaster(this.getRepo().getProject()); return mapValues; }
     */
    private RankingGenerator getRanking(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
        RankingGenerator rGenerator = new RankingGenerator();

        Set<EditedFile> excepiontFiles = rGenerator.setMedalsFilesEditedBothBranches(mergeFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch1(dependencies.get(0), mergeFiles, excepiontFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch2(dependencies.get(1), mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);

        return rGenerator;
    }

    private RankingGenerator getRankingA(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
        RankingGenerator rGenerator = new RankingGenerator();

        Set<EditedFile> excepiontFiles = rGenerator.setMedalsFilesEditedBothBranchesA(mergeFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch1A(dependencies.get(0), mergeFiles, excepiontFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch2A(dependencies.get(1), mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);

        return rGenerator;
    }

    private RankingGenerator getRankingB(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
        RankingGenerator rGenerator = new RankingGenerator();

        Set<EditedFile> excepiontFiles = rGenerator.setMedalsFilesEditedBothBranchesB(mergeFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch1B(dependencies.get(0), mergeFiles, excepiontFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch2B(dependencies.get(1), mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);

        return rGenerator;
    }

    private RankingGenerator getRankingC(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
        RankingGenerator rGenerator = new RankingGenerator();

        Set<EditedFile> excepiontFiles = rGenerator.setMedalsFilesEditedBothBranchesC(mergeFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch1C(dependencies.get(0), mergeFiles, excepiontFiles);
        excepiontFiles = rGenerator.setMedalFromDependenciesBranch2C(dependencies.get(1), mergeFiles, excepiontFiles);
        excepiontFiles.removeAll(excepiontFiles);

        return rGenerator;
    }

    private MergeFiles getFilesAnalisys(String merge) {

        MergeFilesDao mergeFilesDao = new MergeFilesDao();

        MergeFiles mergeSelected = mergeFilesDao.getMerge(merge, this.getRepo().getProject());

        EditedFilesDao filesDao = new EditedFilesDao();
        mergeSelected.setFilesOnBranchOne(filesDao.getFiles(mergeSelected.getHashBase(),
                mergeSelected.getParents()[0],
                mergeSelected.getPath(),
                Parameter.EXTENSION));
        mergeSelected.setFilesOnBranchTwo(filesDao.getFiles(mergeSelected.getHashBase(),
                mergeSelected.getParents()[1],
                mergeSelected.getPath(),
                Parameter.EXTENSION));

        CommitterDao cmterDao = new CommitterDao();
        List<EditedFile> files = new LinkedList<>();

        for (EditedFile editedFile : mergeSelected.getFilesOnBranchOne()) {
            List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(),
                    mergeSelected.getParents()[0],
                    editedFile.getFileName(),
                    mergeSelected.getPath());
            if (whoEdited.size() > 0) {
                editedFile.setWhoEditTheFile(whoEdited);
                files.add(editedFile);
            }
        }
        mergeSelected.setFilesOnBranchOne(files);

        files = new LinkedList<>();
        for (EditedFile editedFile : mergeSelected.getFilesOnBranchTwo()) {
            List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(),
                    mergeSelected.getParents()[1],
                    editedFile.getFileName(),
                    mergeSelected.getPath());
            if (whoEdited.size() > 0) {
                editedFile.setWhoEditTheFile(whoEdited);
                files.add(editedFile);
            }
        }
        mergeSelected.setFilesOnBranchTwo(files);

        files = new LinkedList<>();
        for (EditedFile editedFile : mergeSelected.getFilesOnPreviousHistory()) {
            List<Committer> whoEdited = cmterDao.getWhoEditedFile(this.getRepo().getFirstCommit(),
                    mergeSelected.getHashBase(),
                    editedFile.getFileName(),
                    mergeSelected.getPath());
            if (whoEdited.size() > 0) {
                editedFile.setWhoEditTheFile(whoEdited);
                files.add(editedFile);
            }
        }
        mergeSelected.setFilesOnPreviousHistory(new HashSet<>(files));

        return mergeSelected;

    }

    private List<Map<EditedFile, Set<EditedFile>>> getFilesDependencies(MergeFiles mergeFiles) {

        List<Map<EditedFile, Set<EditedFile>>> depList = new ArrayList<>();

        MergeCommitsDao mCommitsDao = new MergeCommitsDao(this.getRepo().getProject());
        List<String> hashsOnPreviousHistory = mCommitsDao.getHashs(this.getRepo().getFirstCommit(), mergeFiles.getHashBase());

        try {

            Set<String> editedFiles = new HashSet<>();
            mergeFiles.getFilesOnBranchOne().stream().forEach((editedFile) -> {
                editedFiles.add("'" + editedFile.getFileName() + "'");
            });
            mergeFiles.getFilesOnBranchTwo().stream().forEach((editedFile) -> {
                editedFiles.add("'" + editedFile.getFileName() + "'");
            });

            List<Integer> matrices = new ArrayList<>(Arrays.asList(7));
            //System.out.println("\nCreating the dominoes of History");
            List<Dominoes> dominoesHistory
                    = //DominoesSQLDao2.loadAllMatrices(Parameter.DATABASE, this.getRepo().getName(), "GPU", hashsOnPreviousHistory, matrices);
                    DominoesFiles.loadMatrices(Parameter.DATABASE, this.getRepo().getName(), "CPU", hashsOnPreviousHistory, editedFiles, matrices);
            //dominoesHistory.addAll(DominoesSQLDao2.loadAllMatrices(Parameter.DATABASE, this.getRepo().getName(), "GPU", hashsOnPreviousHistory, matrices));

            //System.out.println("->->->Hist. Dep:\t" + dominoesHistory.get(0).getHistoric());
            Dominoes domCF = dominoesHistory.get(0);
            //Dominoes domCFt = dominoesHistory.get(1);
            Dominoes domCFt = domCF.cloneNoMatrix();
            domCFt.transpose();

            System.out.println("Matrix 1 and 2 non-zeros\t" + domCF.getMat().getNonZeroData().size() + "\t" + domCFt.getMat().getNonZeroData().size());

            Dominoes domFF = domCFt.multiply(domCF);
            domFF.confidence();

            Dependencies dependencies = new Dependencies(domFF);
            double threshold = Parameter.THRESHOLD;

            //System.out.println("Dependencies Branch One");
            depList.add(0, dependencies.getDependenciesAcrossBranches(
                    mergeFiles.getFilesOnBranchOne(),
                    mergeFiles.getFilesOnBranchTwo(),
                    threshold));
            depList.add(1, dependencies.getDependenciesAcrossBranches(
                    mergeFiles.getFilesOnBranchTwo(),
                    mergeFiles.getFilesOnBranchOne(),
                    threshold));
        } catch (SQLException ex) {
            Logger.getLogger(JFrameDependencies.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JFrameDependencies.class.getName()).log(Level.SEVERE, null, ex);
        }

        return depList;

    }

    private boolean hasEnoughtDevelopers(MergeFiles mergeFiles) {
        if (mergeFiles.getCommittersOnBranchOne().isEmpty() || mergeFiles.getCommittersOnBranchTwo().isEmpty()) {
            return false;
        }
        if (mergeFiles.getCommittersOnBranchOne().size() == 1 && mergeFiles.getCommittersOnBranchTwo().size() == 1) {
            return !mergeFiles.getCommittersOnBranchOne().get(0).equals(mergeFiles.getCommittersOnBranchTwo().get(0));
        }
        return true;

    }

    public List<String> getAuthorsFromMerges() {

        RepositoryDao rdao = new RepositoryDao(repository.getProject());
        rdao.setDetails(repository);
        //Map<String, String> names = new HashMap<>();
        Map<Committer, Integer> mjClass = new HashMap<>();
        for (String hashMerge : this.getRepo().getListOfMerges()) {

            //DateFormat timeFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss.SSS");  
            //String formattedDate = timeFormat.format(new Date());  
            //System.out.println("Merge under review: " + hashMerge + "\tTime: " + formattedDate);
            //MergeCommits merge = new MergeCommits(hashMerge,this.getRepo().getProject());
            //MergeCommitsDao mergeDao = new MergeCommitsDao(this.getRepo().getProject());
            //mergeDao.update(merge);
            Committer committer = CommitterDao.getCommitter1(hashMerge.split(" ")[0], this.getRepo());

            //names.put(hashMerge, committer.getName());
            //Committer committer = CommitterDao.getCommitter1(hashMerge, this.repos);
            if (mjClass.get(committer) == null) {
                mjClass.put(committer, 1);
            } else {
                mjClass.put(committer, mjClass.get(committer) + 1);
            }

        }

        List<String> mjlist = new ArrayList<>();
        mjClass.keySet().stream().forEach((cmter) -> {
            mjlist.add(mjClass.get(cmter) + " - " + cmter.getName());
        });
        Collections.sort(mjlist);

        return mjlist;
    }

    private static void showFinalResults(Map<String, Integer> result, Map<String, Integer> rankMap, File outputLocation) throws IOException {
        BufferedWriter bufferedWriter;
        //Map<String, Integer> result = experimentMerges.getDatasFromMerges();
        int rankingTotal = rankMap.get("1stPosition") + rankMap.get("2ndPosition") + rankMap.get("3thPosition") + rankMap.get("isInRank") + rankMap.get("outOfRank");
        //System.out.println(result.toString());
        //result.put("Merges", 0);
        System.out.println("Number of merges with Enough developers " + result.get("Merges"));
        System.out.println("Number of merges with changed files in both branches " + result.get("Files"));
        System.out.println("Number of merges with dependencies across branches " + result.get("Dependencies"));
        System.out.println("Number of merges with conflicts " + result.get("Conflicts"));
        System.out.println("Number of merges with raking " + rankingTotal);
        System.out.println("Matched with 1st Position " + rankMap.get("1stPosition"));
        System.out.println("Matched with 2nd Position " + rankMap.get("2ndPosition"));
        System.out.println("Matched with 3th Position " + rankMap.get("3thPosition"));
        System.out.println("Matched with other Position " + rankMap.get("isInRank"));
        System.out.println("Out of the Ranking " + rankMap.get("outOfRank"));

        bufferedWriter = new BufferedWriter(new FileWriter(outputLocation, true));
        bufferedWriter.write("\nNumber of merges with Enough developers " + result.get("Merges") + "\n");
        bufferedWriter.write("Number of merges with changed files in both branches " + result.get("Files") + "\n");
        bufferedWriter.write("Number of merges with dependencies across branches " + result.get("Dependencies") + "\n");
        bufferedWriter.write("Number of merges with conflicts " + result.get("Conflicts") + "\n");
        bufferedWriter.write("Number of merges with raking " + rankingTotal + "\n");
        bufferedWriter.write("Matched with 1st Position " + rankMap.get("1stPosition") + "\n");
        bufferedWriter.write("Matched with 2nd Position " + rankMap.get("2ndPosition") + "\n");
        bufferedWriter.write("Matched with 3th Position " + rankMap.get("3thPosition") + "\n");
        bufferedWriter.write("Matched with other Position " + rankMap.get("isInRank") + "\n");
        bufferedWriter.write("Out of the Ranking " + rankMap.get("outOfRank"));

        bufferedWriter.close();
    }

}
