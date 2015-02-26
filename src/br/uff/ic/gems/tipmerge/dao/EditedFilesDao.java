/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Catarina
 */
public class EditedFilesDao {
    
    public List<String> getFilesList(String base, String parent, File path){
        String command = "git diff --name-only " + base + ".." + parent;
        System.out.print(command + " ");
        System.out.println(path.toString());
        List<String> data = RunGit.getListOfResult(command, path);
        return data;
    }
    
    public List<EditedFile> getFiles(String base, String parent, File path){
        List<EditedFile> list = new ArrayList<>();
        String command = "git diff --name-only " + base + ".." + parent;
        List<String> data = RunGit.getListOfResult(command, path);
        for (String file : data){
            list.add(new EditedFile(file));
        }
        
        return list;
    }
    
    
}
