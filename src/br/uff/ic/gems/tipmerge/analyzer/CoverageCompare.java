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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Para executar esta classe é necessário ter um diretorio chamado files/ no
 * mesmo nível, com os clones dos projetos e um arquivo de texto chamado
 * merges.txt com os hashes
 *
 * @author jjcfigueiredo
 */
public class CoverageCompare {

    public static void main(String[] args) throws IOException {

        //cria o arquivo com o nome resultados no mesmo diretório dos projetos...
        File directory = new File("files/");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String outputFileName = directory.toString() + "/Coverage-Compare-" + dateFormat.format(date) + ".txt";
        ArrayList<String> mergetTarget = loadMerges(directory);

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, false)));
        out.println("Coverage Analysis");
        out.close();

        for (File project : directory.listFiles()) {
            if (project.isDirectory()) {

                System.out.println("\nAnalyzing the project\t" + project.getName());
                out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                out.println("\nAnalyzing the project\t" + project.getName());
                out.println("Merge's Hash\t"
                        + "Merge date\t"
                        + "2 devs origin rank\t"
                        + "2 devs combined rank\t"
                        + "Improvement with 2\t"
                        + "3 devs origin rank\t"
                        + "3 devs combined rank\t"
                        + "Improvement with 3\t");
                out.close();

                Map<String, Double> analysisResult = new LinkedHashMap<>();
                Double gold = 1.0, silver = 0.8, bronze = 0.3;
                Double coverDef, coverOpt, coverNorm;

                List<String> merges = RunGit.getListOfResult("git log --merges --all --pretty=%H%x09%ad --date=short", project);
                for (String merge : merges) {
                    String mergeHash = merge.split("\t")[0];
                    if (mergetTarget.contains(mergeHash)) {
                        String firstHash = RunGit.getResult("git rev-list --max-parents=0 HEAD", project);

                        MergeFiles mergeFiles = getMergeFiles(project, firstHash, mergeHash);
                        List<Map<EditedFile, Set<EditedFile>>> dependencies = getFilesDependencies(project, mergeFiles, firstHash);
                        int filesInCommon = mergeFiles.getFilesOnBothBranch().size();
                        boolean hasFilesDependencies = !(dependencies.get(0).isEmpty() && dependencies.get(1).isEmpty());

                        if ((filesInCommon > 0) || (hasFilesDependencies)) {

                            RankingGenerator rGenerator = getRanking(dependencies, mergeFiles);

                            out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                            out.print(merge);
                            System.out.println("\nmerge\t" + merge);
                            System.out.println("\nRank Original\n" + rGenerator.toString());

                            System.out.println("2 do rank original");
                            coverDef = evaluateCoverage(rGenerator, out, new Double[]{gold, silver, bronze}, 2, 2);
                            System.out.println("2 do rank otimizado");
                            coverOpt = evaluateCoverage(rGenerator, out, new Double[]{gold, silver, bronze}, 2, rGenerator.getDevelopersQuantity());

                            coverNorm = coverOpt > coverDef ? (coverOpt - coverDef) / (1 - coverDef) : (coverOpt - coverDef) / coverDef;
                            out.print("\t" + coverNorm);
                            if (coverNorm > 0.0) {
                                analysisResult.put("2devs", ((analysisResult.get("2devs") == null) ? 1 : analysisResult.get("2devs") + 1));
                            }

                            System.out.println("3 do rank original");
                            coverDef = evaluateCoverage(rGenerator, out, new Double[]{gold, silver, bronze}, 3, 3);
                            System.out.println("3 do rank otimizado");
                            coverOpt = evaluateCoverage(rGenerator, out, new Double[]{gold, silver, bronze}, 3, rGenerator.getDevelopersQuantity());

                            coverNorm = coverOpt > coverDef ? (coverOpt - coverDef) / (1 - coverDef) : (coverOpt - coverDef) / coverDef;
                            out.print("\t" + coverNorm + "\n");
                            if (coverNorm > 0.0) {
                                analysisResult.put("3devs", ((analysisResult.get("3devs") == null) ? 1 : analysisResult.get("3devs") + 1));
                            }
                            out.close();
                        }
                    }
                }
                out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)));
                for (Map.Entry<String, Double> result : analysisResult.entrySet()) {
                    System.out.println("Total merges with improvement\t" + result.getKey() + "\t" + result.getValue());
                    out.println("Total merges with improvement\t" + result.getKey() + "\t" + result.getValue());
                }
                out.close();
                //System.out.println(analysisResult.toString());
            }
        }
    }

    private static Double evaluateCoverage(RankingGenerator rGenerator, PrintWriter out, Double[] weights, int devsToCombine, int devsToConsider) {
        //int avDevs = rGenerator.getDevelopersQuantity();
        int avDevs = devsToConsider;
        List<Integer> availableIndexes = new ArrayList<>();
        BitSet availableDevelopers = new BitSet(avDevs);
        for (int index = 0; index < avDevs; index++) {
            availableIndexes.add(index);
        }
        availableIndexes.stream().forEach((bit) -> {
            availableDevelopers.set(bit);
        });
        rGenerator.combineDevelopers(devsToCombine, //numero de developers a serem combinados
                0.0, //cobertura mínina
                300, //iterações
                300, //duracao
                -1,
                weights[0], //valor pra ouro
                weights[1], //valor para prata
                weights[2], //valor para bronze
                1,
                availableDevelopers
        );
        Double coverage = rGenerator.getBestMedalist().getCoverage();
        Committer combCommitter;
        try {
            combCommitter = (CombinedCommitter) rGenerator.getRanking().get(0).getCommitter();
        } catch (ClassCastException cce) {
            combCommitter = rGenerator.getRanking().get(0).getCommitter();
        }

        //System.out.println("\tIntegrator: " + integrator);
        System.out.println("DEVs\n" + combCommitter.getNameEmail());
        System.out.println("COVER: " + coverage);
        out.print("\t" + coverage);
        //out.println("COVER: " + coverage);

        resetRank(rGenerator);
        return coverage;
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
