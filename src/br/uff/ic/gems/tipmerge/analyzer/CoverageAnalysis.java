/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dominoes.DominoesFiles;
import br.uff.ic.gems.tipmerge.experiment.Parameter;
import br.uff.ic.gems.tipmerge.gui.JFrameDependencies;
import br.uff.ic.gems.tipmerge.model.CombinedCommitter;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Dependencies;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import br.uff.ic.gems.tipmerge.util.RunGit;
import domain.Dominoes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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
 * Para executar esta classe é necessário ter um diretorio chamado files/ no mesmo nível, 
 * com os clones dos projetos
 * e um arquivo de texto chamado merges.txt com os hashes
 * @author jjcfigueiredo
 */
public class CoverageAnalysis {

    public static void main(String[] args) throws IOException {

        //cria o arquivo com o nome resultados no mesmo diretório dos projetos...
        File directory = new File("files/");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String outputFileName = directory.toString() + "/Coverage-Results-" + dateFormat.format(date) + ".txt";
        ArrayList<String> mergetTarget = loadMerges(directory);
        Map<String, Integer> result = new HashMap<>();

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, false)));
        out.println("Coverage Analysis");
        out.close();

        for (File project : directory.listFiles()) {
            if (project.isDirectory()) {

                System.out.println("\nAnalyzing the project\t" + project.getName());
                out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                out.println("\nAnalyzing the project\t" + project.getName());
                out.close();

                List<String> merges = RunGit.getListOfResult("git log --merges --all --pretty=%H%x09%ad --date=short", project);
                for (String merge : merges) {
                    String mergeHash = merge.split("\t")[0];
                    if (mergetTarget.contains(mergeHash)) {
                        System.out.println("merge\t" + merge);
                        String firstHash = RunGit.getResult("git rev-list --max-parents=0 HEAD", project);

                        MergeFiles mergeFiles = getMergeFiles(project, firstHash, mergeHash);
                        List<Map<EditedFile, Set<EditedFile>>> dependencies = getFilesDependencies(project, mergeFiles, firstHash);
                        int filesInCommon = mergeFiles.getFilesOnBothBranch().size();
                        boolean hasFilesDependencies = !(dependencies.get(0).isEmpty() && dependencies.get(1).isEmpty());
                        if ((filesInCommon > 0) || (hasFilesDependencies)) {

                            RankingGenerator rGenerator = getRanking(dependencies, mergeFiles);
                            //DefaultListModel availableModel = new DefaultListModel();
                            List<Integer> availableIndexes = new ArrayList<>();
                            BitSet availableDevelopers = new BitSet(rGenerator.getDevelopersQuantity());

                            int index = 0;
                            for (Medalist medalist : rGenerator.getDevelopers()) {
                                //    availableModel.addElement(medalist.getCommitter().getName());
                                availableIndexes.add(index++);
                            }

                            for (Integer bit : availableIndexes) {
                                availableDevelopers.set(bit);
                            }

                            out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                            out.println("merge\t" + merge);
                            Committer integrator = CommitterDao.getCommitter1(mergeHash, project);
                            System.out.println("\tMerge Integrator:\t" + " " + "\t" + integrator);
                            out.println("\tMerge Integrator:\t" + " " + "\t" + integrator);
                            out.close();
                            DecimalFormat decimal = new DecimalFormat("#.#");
                            for (Double i = 1.0; i >= 0.1; i = i - 0.1) {
                                for (Double j = i - 0.1; j >= 0.1; j = j - 0.1) {
                                    for (Double k = j - 0.1; k >= 0.1; k = k - 0.1) {
                                        String key = decimal.format(i) + "-" + decimal.format(j) + "-" + decimal.format(k) + ";2";
                                        System.out.println(key);
                                        rGenerator.combineDevelopers(
                                                2, //numero de developers
                                                0.0, //cobertura mínina
                                                300, //iterações
                                                300, //duracao
                                                -1,
                                                i, //valor pra ouro
                                                j, //valor para prata
                                                k, //valor para bronze
                                                1,
                                                availableDevelopers
                                        );
                                        System.out.println("Combine 2\t" + rGenerator.getRanking().get(0) + "\tIntegrator: " + integrator);
                                        System.out.println(rGenerator.getRanking().get(0));
                                        CombinedCommitter combCommitter = (CombinedCommitter) rGenerator.getRanking().get(0).getCommitter();
                                        System.out.println(combCommitter.getNameEmail());

                                        if (combCommitter.getCommitters().contains(integrator)) {
                                            Integer value = ((result.get(key) == null) ? 1 : result.get(key) + 1);
                                            result.put(key, value);
                                            System.out.println("contem com 2");
                                        } else {
                                            System.out.println("não contém com 2");
                                        }

                                        resetRank(rGenerator);

                                        key = decimal.format(i) + "-" + decimal.format(j) + "-" + decimal.format(k) + ";3";
                                        System.out.println(key);
                                        rGenerator.combineDevelopers(
                                                3, //numero de developers
                                                0, //cobertura mínina
                                                300, //iterações
                                                300, //duracao
                                                -1,
                                                i, //valor pra ouro
                                                j, //valor para prata
                                                k, //valor para bronze
                                                1,
                                                availableDevelopers
                                        );
                                        System.out.println("Combine 3\t" + rGenerator.getRanking().get(0) + "\tIntegrator: " + integrator);
                                        combCommitter = (CombinedCommitter) rGenerator.getRanking().get(0).getCommitter();
                                        System.out.println(combCommitter.getNameEmail());

                                        if (combCommitter.getCommitters().contains(integrator)) {
                                            Integer value = ((result.get(key) == null) ? 1 : result.get(key) + 1);
                                            result.put(key, value);
                                            System.out.println("contem com 3");
                                        } else {
                                            System.out.println("não contém com 3");
                                        }

                                        resetRank(rGenerator);

                                    }
                                }
                            }
                        }
                    }
                }
                out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                out.println(result.toString() + "\n");
                out.close();

                System.out.println(result.toString());
            }
        }
    }

    private static void resetRank(RankingGenerator rGenerator) {
        rGenerator.setGoldWeight(1.0);
        rGenerator.setSilverWeight(0.5);
        rGenerator.setBronzeWeight(0.1);
        rGenerator.setFitness(0);
        rGenerator.reset();
    }

    private static MergeFiles getMergeFiles(File project, String firstHash, String mergeHash) {
        MergeFilesDao mergeFilesDao = new MergeFilesDao();

        MergeFiles mergeSelected = mergeFilesDao.getMerge(mergeHash, project);

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
            List<Committer> whoEdited = cmterDao.getWhoEditedFile(
                    firstHash,
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

    private static List<Map<EditedFile, Set<EditedFile>>> getFilesDependencies(File project, MergeFiles mergeFiles, String firstHash) {

        List<Map<EditedFile, Set<EditedFile>>> depList = new ArrayList<>();

        MergeCommitsDao mCommitsDao = new MergeCommitsDao(project);
        List<String> hashsOnPreviousHistory = mCommitsDao.getHashs(firstHash, mergeFiles.getHashBase());

        try {

            Set<String> editedFiles = new HashSet<>();
            mergeFiles.getFilesOnBranchOne().stream().forEach((editedFile) -> {
                editedFiles.add("'" + editedFile.getFileName() + "'");
            });
            mergeFiles.getFilesOnBranchTwo().stream().forEach((editedFile) -> {
                editedFiles.add("'" + editedFile.getFileName() + "'");
            });

            List<Integer> matrices = new ArrayList<>(Arrays.asList(7));
            System.out.println("Creating the dominoes of dependencies");
            List<Dominoes> dominoesHistory
                    = DominoesFiles.loadMatrices(Parameter.DATABASE, project.getName(), "CPU", hashsOnPreviousHistory, editedFiles, matrices);
            Dominoes domCF = dominoesHistory.get(0);
            Dominoes domCFt = domCF.cloneNoMatrix();
            domCFt.transpose();

            //System.out.println("Matrix 1 and 2 non-zeros\t" + domCF.getMat().getNonZeroData().size() + "\t" + domCFt.getMat().getNonZeroData().size());
            Dominoes domFF = domCFt.multiply(domCF);
            domFF.confidence();

            Dependencies dependencies = new Dependencies(domFF);
            double threshold = Parameter.THRESHOLD;

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

    private static RankingGenerator getRanking(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
        RankingGenerator rGenerator = new RankingGenerator();
        rGenerator.createMedals(mergeFiles, dependencies.get(0), dependencies.get(1));
        return rGenerator;
    }

    private static ArrayList<String> loadMerges(File directory) {

        String fileName = directory + "/merges.txt";
        ArrayList<String> result = new ArrayList<>();
        File inputFile = new File(fileName);

        //Arquivo existe
        String linha;
        if (inputFile.exists()) {

            try {
                //abrindo arquivo para leitura
                FileReader reader = new FileReader(fileName);
                //leitor do arquivo
                BufferedReader leitor = new BufferedReader(reader);
                while (true) {
                    linha = leitor.readLine();
                    if (linha == null) {
                        break;
                    }
                    result.add(linha);
                }
            } catch (Exception erro) {
                System.out.println("error on file reading");
            }
        } //Se nao existir
        else {
            System.out.println("file not exists");
        }
        return result;
    }

}
