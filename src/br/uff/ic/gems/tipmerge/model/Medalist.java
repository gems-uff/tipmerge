package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.ImageIcon;

/**
 * This class has information about Committers who have medals.
 *
 * @author Eduardo
 */
public class Medalist {

    private final Committer committer;
    private Set<String> goldListBranch1 = new TreeSet();
    private Set<String> goldListBranch2 = new TreeSet();
    private Set<String> silverList = new TreeSet();
    private Map<String, MedalBronze> bronzeList = new HashMap<>();
    private BitSet configuration;

    public BitSet getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BitSet configuration) {
        this.configuration = configuration;
    }

    public Medalist(Committer committer) {
        this.committer = committer;
    }

    public Committer getCommitter() {
        return committer;
    }

    public int getGoldMedals() {
        //TODO como contar as medalhas de ouro
        return goldListBranch1.size() + goldListBranch2.size();

    }

    public Set<String> getGoldListBranch1() {
        return goldListBranch1;
    }

    public Set<String> getGoldListBranch2() {
        return goldListBranch2;
    }

    public void setGoldListBranch1(Set<String> files) {
        this.goldListBranch1 = files;
    }

    public void setGoldListBranch2(Set<String> files) {
        this.goldListBranch2 = files;
    }

    public int getSilverMedals() {
        return silverList.size();
    }

    public void setSilverList(Set<String> files) {
        this.silverList = files;
    }

    public Set<String> getSilverList() {
        return this.silverList;
    }

    public int getBronzeMedals() {
        return bronzeList.size();
    }

    public void setBronzeList(Map<String, MedalBronze> files) {
        this.bronzeList = files;
    }

    public Map<String, MedalBronze> getBronzeList() {
        return this.bronzeList;
    }
    
    public List<String> getBronzeFilesName(){
        List<String> list = new ArrayList<>();
        for(MedalBronze mb : this.bronzeList.values())
            list.add(mb.getFile());
        return list;       
    }
    
    public int directionFromFileDepend(String file){
        for(MedalBronze mb : this.bronzeList.values())
            if(mb.getFile().equals(file))
                return mb.getDirection();
        return -1;
    }

    public void addGoldMedalBranch1(String fileName) {
        this.goldListBranch1.add(fileName);
    }

    public void addGoldMedalBranch2(String fileName) {
        this.goldListBranch2.add(fileName);
    }
    
    public void addGoldMedal(String fileName, int branch) {
        if (branch == 1) {
            this.addGoldMedalBranch1(fileName);
        } else {
            this.addGoldMedalBranch2(fileName);
        }
    }

    public void addSilverMedal(String fileName) {
        this.silverList.add(fileName);
    }

    public void addBronzeMedal(String fileName, String fileDep, Integer direction) {
        if(direction == 2){
            MedalBronze medalBronze = this.bronzeList.get(fileName);
            medalBronze.setDirection(direction);
            medalBronze.addFileDepend(fileName, direction);
        }else
            this.bronzeList.put(fileName, new MedalBronze(fileName, fileDep, direction));
        
    }

    @Override
    public String toString() {
        String name = this.getCommitter().getName();
        if (name.length() > 20) {
            name = this.getCommitter().getInitial().substring(0, Math.min(20, this.getCommitter().getInitial().length()));
        }
        return name + "\t"
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
        for (MedalBronze medalBronze : this.bronzeList.values()) {
            ImageIcon medalIcon = null;
            //if(null != this.bronzeList.get(medalBronze))
                
            switch (medalBronze.getDirection()) {
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
            if (fileMedal.containsKey(medalBronze.getFile())) {
                fileMedal.put(medalBronze.getFile(), new Object[]{fileMedal.get(medalBronze.getFile())[0], fileMedal.get(medalBronze.getFile())[1], medalIcon});
            } else {
                fileMedal.put(medalBronze.getFile(), new Object[]{"", "", medalIcon});
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
