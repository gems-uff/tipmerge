package br.uff.ic.gems.tipmerge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 * This class has information about Committers who have medals.
 *
 * @author Eduardo
 */
public class Medalist {

    private final Committer committer;
    private List<String> goldListBranch1 = new ArrayList();
    private List<String> goldListBranch2 = new ArrayList();
    private List<String> silverList = new ArrayList();
    private List<MedalBronze> bronzeList = new ArrayList<>();
    //private Map<String,Integer> bronzeList = new HashMap<>();

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

    public void setBronzeList(List<MedalBronze> files) {
        this.bronzeList = files;
    }

    public List<MedalBronze> getBronzeList() {
        return this.bronzeList;
    }
    
    public List<String> getBronzeFilesName(){
        List<String> list = new ArrayList<>();
        for(MedalBronze mb : this.bronzeList)
            list.add(mb.getFile());
        return list;       
    }
    
    public int directionFromFileDepend(String file){
        for(MedalBronze mb : this.bronzeList)
            if(mb.getFile().equals(file))
                return mb.getDirection();
        return -1;
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

    public void addBronzeMedal(String fileName, String fileDep, Integer direction) {
        if(direction == 2){
            MedalBronze medalBronze = this.bronzeList.get(this.getBronzeFilesName().indexOf(fileName));
            medalBronze.setDirection(direction);
            medalBronze.addFileDepend(fileName, direction);
        }else
            this.bronzeList.add(new MedalBronze(fileName, fileDep, direction));
        
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
        for (MedalBronze medalBronze : this.bronzeList) {
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
