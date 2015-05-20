/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template antecendentTmp, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import arch.Cell;
import arch.IMatrix2D;
import domain.Dominoes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author j2cf
 */
public class Dependencies {
	
	private Dominoes dominoes;
	
	public Dependencies(Dominoes dominoes){
		this.dominoes = dominoes;
	}
	
	
	/**
	 * @return the dominoes
	 */
	public Dominoes getDominoes() {
		return dominoes;
	}

	/**
	 * @param dominoes the dominoes to set
	 */
	public void setDominoes(Dominoes dominoes) {
		this.dominoes = dominoes;
	}
	
	/**
	 * Returns a list of antecedentFiles that have some dependence with the list of filesEdited antecedentFiles, following the defined threshold.
	 * @param antecedentFiles @param threshold @param consequentFiles @param excepiontFiles
	 * @return fileDependencies
	 */
	public Map<EditedFile,Set<EditedFile>> getDependenciesAcrossBranches(
									List<EditedFile> antecedentFiles, 
									List<EditedFile> consequentFiles, double threshold) {
		
		Map<EditedFile,Set<EditedFile>> dependenciesAcrossBranches = new HashMap<>();

		IMatrix2D matrix = getDominoes().getMat();

		int rows = matrix.getMatrixDescriptor().getNumRows();
		int cols = matrix.getMatrixDescriptor().getNumCols();
		
		List<Cell> cells = matrix.getNonZeroData();
		
		for(int i = 0 ; i < rows ; i++){
			
			EditedFile antecendentTmp = new EditedFile(matrix.getMatrixDescriptor().getRowAt(i));
			Set<EditedFile> fileDependencies = new HashSet<>();
			boolean hasDependencies = false;

			int indexOf = antecedentFiles.indexOf(antecendentTmp);
			if(indexOf > -1){
				
				antecendentTmp = antecedentFiles.get(indexOf);
				
				for(int j = 0 ; j < cols ; j++){
					EditedFile consequentTmp = new EditedFile(matrix.getMatrixDescriptor().getColumnAt(j));
					indexOf = consequentFiles.indexOf(consequentTmp);
					if((i != j) && (indexOf > -1))

						for(Cell c : cells)
							if((c.value >= threshold) && (c.row == i) && (c.col == j)){						
								fileDependencies.add(consequentFiles.get(indexOf));
								hasDependencies = true;
							}
				}
			}
			if(hasDependencies) dependenciesAcrossBranches.put(antecendentTmp, fileDependencies);
		}
		return dependenciesAcrossBranches;
	}
	
}
