/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer.git;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.util.List;

/**
 *
 * @author jjaircf
 */
public class GitRepository {
        
    public static List<String> getMerges(File gitRepo){
        return RunGit.getListOfResult("git log --merges --all --pretty=%H%x09%ad --date=short", gitRepo); 
    }

    public static Committer getWhoMerge(File gitRepo, String hash) {
        String author = RunGit.getResult("git show --format=%an#%ae " + hash, gitRepo);
        return new Committer(author.split("#")[0], author.split("#")[1]);
    }

    public static String getFirstHash(File gitRepo, String merge) {
        return RunGit.getResult("git rev-list --max-parents=0 HEAD", gitRepo);
    }

    public static List<String> getHashList(File gitRepo, String firstH, String baseH) {
        String command = "git log --format='%H' --no-merges " + firstH + ".." + baseH;
        return RunGit.getListOfResult(command, gitRepo);
    }
    
}
