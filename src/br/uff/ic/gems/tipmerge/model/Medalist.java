package br.uff.ic.gems.tipmerge.model;

/**
 * This class has information about committers who have medals.
 * @author Eduardo
 */
public class Medalist
{
    private Committer committer;
    private int goldMedals;
    private int silverMedals;
    private int bronzeMedals;

    public Medalist(Committer committer, int goldMedals, int silverMedals, int bronzeMedals) {
        this.committer = committer;
        this.goldMedals = goldMedals;
        this.silverMedals = silverMedals;
        this.bronzeMedals = bronzeMedals;
    }

    public Committer getCommitter() {
        return committer;
    }

    public int getGoldMedals() {
        return goldMedals;
    }

    public int getSilverMedals() {
        return silverMedals;
    }

    public int getBronzeMedals() {
        return bronzeMedals;
    }
    
}
