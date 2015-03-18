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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class gets list of files changed between two commits hash
 * @author j2cf, Catarina
 */
public class EditedFilesDao {
    
	//get one list of files changed between two commits hash
    public List<String> getFilesList(String base, String parent, File path){
       // String command = "git diff --name-only " + base + ".." + parent;
		String command = "git show --pretty=\"format:\" --name-only " + base + ".." + parent;
		System.out.println("testar método filesList");
        List<String> data = RunGit.getListOfResult(command, path);
        return data;
    }
    //We are using this code. This method gets one list of Editedfiles (String fileName) changed between two commits hash
    public List<EditedFile> getFiles(String base, String parent, File path){
        //String command = "git diff --name-only " + base + ".." + parent;
		String command = "git show --pretty=\"format:\" --name-only " + base + ".." + parent;
        List<String> data = RunGit.getListOfResult(command, path);
		Set<EditedFile> files = new HashSet<>();
        for (String file : data){
			if (!file.equals("")){
				files.add(new EditedFile(file));
			}
        }
        
        return new ArrayList<>(files);
    }
    
    
}
