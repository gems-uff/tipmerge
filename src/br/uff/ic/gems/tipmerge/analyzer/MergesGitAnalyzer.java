/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.model.Committer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jjcfigueiredo
 */
public class MergesGitAnalyzer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String folderName = "tmp/";

        List<String> inputProjects = readFile(folderName);
        
        //System.out.println("inputProjects\t" + inputProjects.toString());

        //List<String> projects = new ArrayList<>(
        //        Arrays.asList("https://github.com/mhagger/git-imerge", "https://github.com/clojure/clojure", "https://github.com/angular/angular")
        //);

        for (String projetctUrl : inputProjects) {

            System.out.println("Analyzing:\t" + projetctUrl);

            Analyzer analyzer = new Analyzer(projetctUrl);

            analyzer.saveToFile(folderName);

            System.out.println("Finalizado");

        }

    }

    private static List<String> readFile(String folderName) {
        String fileName = folderName + "input_projects.txt";
        List<String> result = new ArrayList<>();
        String linha;
        
        File inputFile = new File(fileName);

        //Arquivo existe
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
