/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gems.assignmerge;

import java.io.BufferedWriter;
import java.io.File;

/**
 *
 * @author catarinacosta
 *
 */
public class FileWriter {

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

}
