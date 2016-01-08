package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This class has information about Committers who have medals.
 *
 * @author Eduardo
 */
public class Medalist {

    private Committer committer;
    private List<String> goldListBranch1 = new ArrayList();
    private List<String> goldListBranch2 = new ArrayList();
    private List<String> silverList = new ArrayList();
    //private Set<String> bronzeList = new HashSet<>();
    private Map<String,Integer> bronzeList = new HashMap<String, Integer>();

    public Medalist(Committer committer) {
        this.committer = committer;
    }

    public Committer getCommitter() {
        return committer;
    }

    /*
    public void setCommitter(Committer committer) {
        this.committer = committer;
    }
     */

    public int getGoldMedals() {
        //TODO como contar as medalhas de ouro
        return getGoldListBranch1().size() + getGoldListBranch2().size();

    }

    public List<String> getGoldListBranch1() {
        return goldListBranch1;
    }

    public List<String> getGoldListBranch2() {
        return goldListBranch2;
    }

    public void setgoldListBranch1(List<String> files) {
        this.goldListBranch1 = files;
    }

    public void setgoldListBranch2(List<String> files) {
        this.goldListBranch2 = files;
    }

    public int getSilverMedals() {
        return silverList.size();
    }

    public void setsilverList(List<String> files) {
        this.silverList = files;
    }

    public List<String> getSilverList() {
        return this.silverList;
    }

    public int getBronzeMedals() {
        return bronzeList.size();
    }

    public void setBronzeList(Map<String,Integer> files) {
        this.bronzeList = files;
    }

    public Map<String,Integer> getBronzeList() {
        return this.bronzeList;
    }

    public void addGoldMedalBranch1(String fileName) {
        this.getGoldListBranch1().add(fileName);
    }

    public void addGoldMedalBranch2(String fileName) {
        this.getGoldListBranch2().add(fileName);
    }

    public void addSilverMedal(String fileName) {
        this.silverList.add(fileName);
    }

    public void addBronzeMedal(String fileName, Integer direction) {
        //this.bronzeList.add(fileName);
        this.bronzeList.put(fileName, direction);
    }

    @Override
    public String toString() {
        return this.getCommitter().getName().substring(0, Math.min(20, this.getCommitter().getName().length())) + "\t"
                + this.getGoldMedals() + "\t"
                + this.getSilverMedals() + "\t"
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

    public Map<String, Object[]> getFilesList() {
        //Integer[] medals = new Integer[3];

        Map<String, Object[]> fileMedal = new HashMap<>();
        for (String file : this.bronzeList.keySet()) {
            ImageIcon medalIcon = null;
            if(null != this.bronzeList.get(file))
                switch (this.bronzeList.get(file)) {
                case 0:
                    medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/DEP1.png"));
                    break;
                case 1:
                    medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/DEP2.png"));
                    break;
                default:
                    medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/DEP_BI.png"));
                    break;
            }
            if (fileMedal.containsKey(file)) {
                fileMedal.put(file, new Object[]{fileMedal.get(file)[0], fileMedal.get(file)[1], medalIcon});
            } else {
                fileMedal.put(file, new Object[]{"", "", medalIcon});
            }
        }
        for (String file : this.silverList) {
            ImageIcon medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/HIS.png"));
            if (fileMedal.containsKey(file)) {
                fileMedal.put(file, new Object[]{fileMedal.get(file)[0], medalIcon, fileMedal.get(file)[2]});
            } else {
                fileMedal.put(file, new Object[]{"", medalIcon, ""});
            }
        }
        for (String file : this.goldListBranch1) {
            ImageIcon medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/B1.png"));
            if (fileMedal.containsKey(file)) {
                fileMedal.put(file, new Object[]{medalIcon, fileMedal.get(file)[1], fileMedal.get(file)[2]});
            } else {
                fileMedal.put(file, new Object[]{medalIcon, "", ""});
            }
        }
        for (String file : this.goldListBranch2) {
            ImageIcon medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/B2.png"));
            if (fileMedal.containsKey(file)) {
                if(!fileMedal.get(file)[0].equals("")){
                    medalIcon = new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/B1B2.png"));
                }
                fileMedal.put(file, new Object[]{medalIcon, fileMedal.get(file)[1], fileMedal.get(file)[2]});
            } else {
                fileMedal.put(file, new Object[]{medalIcon, "", ""});
            }
        }

        return fileMedal;
    }

}
