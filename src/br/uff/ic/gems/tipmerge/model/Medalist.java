package br.uff.ic.gems.tipmerge.model;

import java.util.Objects;

/**
 * This class has information about committers who have medals.
 * @author Eduardo
 */
public class Medalist
{
    private Committer committer;
    private int goldMedals = 0;
    private int silverMedals = 0;
    private int bronzeMedals = 0;

    public Medalist(Committer committer) {
        this.committer = committer;
    }

    public Committer getCommitter() {
        return committer;
    }

    public void setCommitter(Committer committer) {
        this.committer = committer;
    }

    public int getGoldMedals() {
        return goldMedals;
    }

    public void setGoldMedals(int goldMedals) {
        this.goldMedals = goldMedals;
    }

    public int getSilverMedals() {
        return silverMedals;
    }

    public void setSilverMedals(int silverMedals) {
        this.silverMedals = silverMedals;
    }

    public int getBronzeMedals() {
        return bronzeMedals;
    }

    public void setBronzeMedals(int bronzeMedals) {
        this.bronzeMedals = bronzeMedals;
    }
	
	@Override
	public String toString(){
		return this.getCommitter().getName() + "\t"
			+ this.getGoldMedals()  + "\t"
			+ this.getSilverMedals()  + "\t"
			+ this.getBronzeMedals();
		
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + Objects.hashCode(this.committer);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Medalist other = (Medalist) obj;
		if (!Objects.equals(this.committer, other.committer)) {
			return false;
		}
		return true;
	}

	public void addGoldMedal() {
		this.goldMedals++;
	}
	public void addSilverMedal() {
		this.silverMedals++;
	}
	public void addBronzeMedal() {
		this.bronzeMedals++;
	}
	
	
	
}
