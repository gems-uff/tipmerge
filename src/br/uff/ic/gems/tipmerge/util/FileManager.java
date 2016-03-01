/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gems.tipmerge.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf, Catarina
 *
 */
public class FileManager {

    public static void writeToFile(String path, String text) {
        try {
            File file = new File(path);
            BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void writeToFile(String dir, String path, String text) {
        try {
            File dire = new File(dir);
            dire.mkdirs();
            File file = new File(dir, path);
            BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
