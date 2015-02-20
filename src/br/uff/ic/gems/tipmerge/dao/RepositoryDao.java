/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jair Figueiredo
 */
public class RepositoryDao {
	
	private File path = null;
	
	public RepositoryDao(File path){
		this.path = path;
	}
	
	public Repository getRepository(){
		
		//seta o diretório e nome do projeto
		Repository repo = new Repository(path);
				
		//insere a informação do ultimo commit
		String[] result = RunGit.getResult("git show -s --format=%ci%x09%H", path).split("\t");
		String data = result[0].substring(0, 19);
		String lastHash = result[1];

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		repo.setLastCommit(LocalDateTime.parse(data, formatter));

		//insere a informação do primeiro commit
		repo.setFirstCommit(RunGit.getResult("git rev-list --max-parents=0 HEAD", path));

		//insere a informação da quantidade total de commits
		repo.setCommits(Long.valueOf(RunGit.getResult("git rev-list HEAD --count", path)));
		
		//insere os merges somente com o seu hash
		MergeDao mergeDao = new MergeDao(path);
		repo.setMerges(mergeDao.getMerges());

		//insere a informação da quantidade total de branches
		repo.setBranches(RunGit.getListOfResult("git branch -a",path));
		repo.getBranches().remove(1);
		repo.getBranches().remove(0);
		//repo.getBranches().add(0, " origin/master");
				
		//insere a lista de autores com o total de commits de cada um
		repo.setAuthors(RunGit.getListOfResult("git shortlog -sne " + repo.getFirstCommit() + ".." + lastHash, path));
		return repo;
	}
	

}
