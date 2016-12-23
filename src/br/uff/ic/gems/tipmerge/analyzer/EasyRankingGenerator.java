/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.analyzer.git.Dependency;
import br.uff.ic.gems.tipmerge.analyzer.git.GitRepository;
import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dominoes.DominoesFiles;
import br.uff.ic.gems.tipmerge.experiment.Parameter;
import br.uff.ic.gems.tipmerge.gui.JFrameDependencies;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Dependencies;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import domain.Dominoes;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjaircf
 */
public class EasyRankingGenerator {

    public static RankingGenerator getRankingGen(File gitRepo, String hash) {
        String firstH = GitRepository.getFirstHash(gitRepo, hash);
        MergeFiles mergeFiles = getMerge(gitRepo, firstH, hash);
        List<Dependency> dependencies = getFilesDependencies(gitRepo, mergeFiles, firstH);
        return getRankingGen(mergeFiles, dependencies);
    }

    private static MergeFiles getMerge(File gitRepo, String firstH, String mergeH) {

        MergeFiles mergeSelected = grabEditedFiles(mergeH, gitRepo);

        mergeSelected.setFilesOnBranchOne(
                getWhoEditedFiles(mergeSelected.getFilesOnBranchOne(), mergeSelected.getHashBase(), mergeSelected.getParents()[0], gitRepo));
        mergeSelected.setFilesOnBranchTwo(
                getWhoEditedFiles(mergeSelected.getFilesOnBranchTwo(), mergeSelected.getHashBase(), mergeSelected.getParents()[1], gitRepo));
        mergeSelected.setFilesOnPreviousHistory(new HashSet<>(
                getWhoEditedFiles(mergeSelected.getFilesOnPreviousHistory(), firstH, mergeSelected.getHashBase(), gitRepo)));

        return mergeSelected;
    }

    private static List<Dependency> getFilesDependencies(File gitRepo, MergeFiles mergeFiles, String firstH) {
        List<Dependency> depList = new ArrayList<>();

        List<String> historyHash = GitRepository.getHashList(gitRepo, firstH, mergeFiles.getHashBase());

        try {

            Set<String> editedFiles = createEditedFilesSet(mergeFiles);

            Dominoes domFF = loadDominoesMatrices(gitRepo, historyHash, editedFiles);

            Dependencies dependencies = new Dependencies(domFF);
            double threshold = Parameter.THRESHOLD;

            depList.add(0, new Dependency(dependencies.getDependenciesAcrossBranches(
                    mergeFiles.getFilesOnBranchOne(),
                    mergeFiles.getFilesOnBranchTwo(),
                    threshold)));

            depList.add(1, new Dependency(dependencies.getDependenciesAcrossBranches(
                    mergeFiles.getFilesOnBranchTwo(),
                    mergeFiles.getFilesOnBranchOne(),
                    threshold)));

        } catch (Exception ex) {
            Logger.getLogger(JFrameDependencies.class.getName()).log(Level.SEVERE, null, ex);
        }

        return depList;
    }

    private static List<EditedFile> getWhoEditedFiles(Collection<EditedFile> editedFiles, String baseHash, String parentHash, File gitRepo) {
        List<EditedFile> files = new LinkedList<>();
        editedFiles.forEach((editedFile) -> {
            List<Committer> whoEdited = (new CommitterDao()).getWhoEditedFile(baseHash, 
                    parentHash,
                    editedFile.getFileName(),
                    gitRepo);
            if (whoEdited.size() > 0) {
                editedFile.setWhoEditTheFile(whoEdited);
                files.add(editedFile);
            }
        });
        return files;
    }

    private static MergeFiles grabEditedFiles(String mergeH, File gitRepo) {
        MergeFiles mergeSelected = (new MergeFilesDao()).getMerge(mergeH, gitRepo);
        //System.out.println(Arrays.toString(mergeSelected.getParents()) + "\t" + mergeSelected.getHashBase());
        mergeSelected.setFilesOnBranchOne((new EditedFilesDao()).getFiles(mergeSelected.getHashBase(),
                mergeSelected.getParents()[0],
                mergeSelected.getPath(),
                Parameter.EXTENSION));
        mergeSelected.setFilesOnBranchTwo((new EditedFilesDao()).getFiles(mergeSelected.getHashBase(),
                mergeSelected.getParents()[1],
                mergeSelected.getPath(),
                Parameter.EXTENSION));
        return mergeSelected;
    }

    private static Dominoes loadDominoesMatrices(File gitRepo, List<String> historyHash, Set<String> editedFiles) throws Exception {
        List<Integer> matrices = new ArrayList<>(Arrays.asList(7));
        System.out.println("\tCreating the dominoes of dependencies");
        List<Dominoes> dominoesHistory
                = DominoesFiles.loadMatrices(Parameter.DATABASE, gitRepo.getName(), "CPU", historyHash, editedFiles, matrices);
        Dominoes domCF = dominoesHistory.get(0);
        Dominoes domCFt = domCF.cloneNoMatrix();
        domCFt.transpose();
        //System.out.println("Matrix 1 and 2 non-zeros\t" + domCF.getMat().getNonZeroData().size() + "\t" + domCFt.getMat().getNonZeroData().size());
        Dominoes domFF = domCFt.multiply(domCF);
        domFF.confidence();
        return domFF;
    }

    private static Set<String> createEditedFilesSet(MergeFiles mergeFiles) {
        Set<String> editedFiles = new HashSet<>();
        mergeFiles.getFilesOnBranchOne().stream().forEach((editedFile) -> {
            editedFiles.add("'" + editedFile.getFileName() + "'");
        });
        mergeFiles.getFilesOnBranchTwo().stream().forEach((editedFile) -> {
            editedFiles.add("'" + editedFile.getFileName() + "'");
        });
        return editedFiles;
    }

    private static RankingGenerator getRankingGen(MergeFiles mergeFiles, List<Dependency> dependencies) {
        int filesInCommon = mergeFiles.getFilesOnBothBranch().size();
        boolean hasFilesDependencies = !(dependencies.get(0).getDependencyMap().isEmpty() && dependencies.get(1).getDependencyMap().isEmpty());

        if ((filesInCommon > 0) || (hasFilesDependencies)) {
            System.out.println("\tGerando Ranking....");
            RankingGenerator rankingGen = new RankingGenerator();
            rankingGen.createMedals(mergeFiles, dependencies.get(0).getDependencyMap(), dependencies.get(1).getDependencyMap());
            return rankingGen;
        }
        return null;

    }
}
