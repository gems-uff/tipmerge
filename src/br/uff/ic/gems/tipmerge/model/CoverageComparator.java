/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author joao
 */
public class CoverageComparator implements Comparator<Medalist> {

    private static double GOLD = 1.0;
    private static double SILVER = 0.5;
    private static double BRONZE = 0.1;
    
    private final Medalist fullCoverage;
    private final double totalGoldBranch1;
    private final double totalGoldBranch2;
    private final double totalSilver;
    private final double totalBronze;
    private final double totalCoverage;
    
    public CoverageComparator(Medalist fullCoverage) {
        this.fullCoverage = fullCoverage;
        this.totalGoldBranch1 = fullCoverage.getGoldListBranch1().size();
        this.totalGoldBranch2 = fullCoverage.getGoldListBranch2().size();
        this.totalSilver = fullCoverage.getSilverList().size();
        this.totalBronze = fullCoverage.getBronzeList().values().stream()
            .map((bronze) -> (bronze.getDirection() == 9) ? 2 : 1).mapToInt(Integer::intValue).sum();
        this.totalCoverage = GOLD * totalGoldBranch1 +
            GOLD * totalGoldBranch2 +
            SILVER * totalSilver +
            BRONZE * totalBronze;
        
    }
    
    public double coverage(Medalist medalist) {
        double partialGoldBranch1 = medalist.getGoldListBranch1().size();
        double partialGoldBranch2 = medalist.getGoldListBranch2().size();
        double partialSilver = medalist.getSilverList().size();
        double partialBronze = medalist.getBronzeList().values().stream()
            .map((bronze) -> (bronze.getDirection() == 9) ? 2 : 1).mapToInt(Integer::intValue).sum();
        
        double partialCoverage = GOLD * partialGoldBranch1 +
            GOLD * partialGoldBranch2 +
            SILVER * partialSilver +
            BRONZE * partialBronze;
        return partialCoverage / this.totalCoverage;
    }
    
    @Override
    public int compare(Medalist o1, Medalist o2) {
        return -Double.compare(this.coverage(o1), this.coverage(o2));
    }

    public Medalist getFullCoverage() {
        return this.fullCoverage;
    }
    
}

