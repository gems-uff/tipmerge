/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.RunGit;

/**
 *
 * @author j2cf
 */
public class CommitterDao {


	public Committer getCommitter(String hash, Repository repository){
		String datas = RunGit.getResult("git show --format=\"%an#%ae \" " + hash, repository.getProject());
		Committer cmt = new Committer(datas.split("#")[0], datas.split("#")[1]);
		return cmt;
	}

}
