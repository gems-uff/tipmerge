/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Conciliator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author j2cf, Catarina
 */
public class Statistics {
	
	
	public static double getMedian(Integer[] values) {
		int tam = values.length;
		Integer[] valuesCopy = new Integer[tam];
		System.arraycopy(values, 0, valuesCopy, 0, tam);
		Arrays.sort(valuesCopy);

		if (tam % 2 == 0){
		   return ((double)valuesCopy[(tam / 2) - 1] + (double)valuesCopy[tam / 2]) / 2.0;
		}  else {
		   return (double)valuesCopy[tam / 2];
		}
    }
	
	public static double getMedian(double[] values) {
		int tam = values.length;
		double[] valuesCopy = new double[tam];
		System.arraycopy(values, 0, valuesCopy, 0, tam);
		Arrays.sort(valuesCopy);

		if (tam % 2 == 0){
		   return (valuesCopy[(valuesCopy.length / 2) - 1] + valuesCopy[valuesCopy.length / 2]) / 2.0;
		}  else {
		   return valuesCopy[valuesCopy.length / 2];
		}
    }
	
	public static double getMAD(List<Integer> values ){
		double median = getMedian((Integer[])values.toArray());
		double[] newValues = new double[values.size()];
		int i =0 ;
		for (Integer listValue : values){
			newValues[i++] = Math.abs(listValue - median);
		}
		return getMedian(newValues);
	}
	
	
	public static List<Double> getMZScore(List<Integer> values){
		
		double median = getMedian((Integer[])values.toArray());
		double mad = getMAD(values);
		
		List<Double> zScoreValues = new ArrayList<>();
			
		for (Integer value : values){
			double zScoreValue = 0.6745 * (value - median)/mad;
			zScoreValues.add(zScoreValue);
		}
		
		return zScoreValues;
	}
	
	public static List<Committer> getMZScoreCommitter(List<Committer> committer){
		List<Integer> values = new ArrayList();
		for (Committer cmter : committer){
			values.add(cmter.getCommits());
		}
		List<Double> zScoreM = getMZScore(values);
		for (int i=0; i <= zScoreM.size(); i++){
			committer.get(i).setzScoreM(zScoreM.get(i));
		}
		return committer;
		
	}
	
	
}
