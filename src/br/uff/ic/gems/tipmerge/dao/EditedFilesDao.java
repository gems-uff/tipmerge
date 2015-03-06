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
 * This class gets list of files changed between two commits hash
 * @author j2cf, Catarina
 */
public class EditedFilesDao {
    
	//get one list of files changed between two commits hash
    public List<String> getFilesList(String base, String parent, File path){
        String command = "git diff --name-only " + base + ".." + parent;
        List<String> data = RunGit.getListOfResult(command, path);
        return data;
    }
    //get one list of Editedfiles (String fileName and List<Committer>) changed between two commits hash
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
