/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

/**
 *
 * @author catarinacosta
 */
public class Committer implements Comparable<Committer>{
    
    private String name;
    
    private Integer commits;
    
    private String email;
 
    public Committer(String name, String email){
        this.name = name;
        this.email = email;
        this.commits = 1;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
/*    public void setNome(String name) {
        this.name = name;
    }
*/
    /**
     * @return the commits
     */
    public Integer getCommits() {
        return commits;
    }

    /**
     * @param commits the nroCommits to set
     */
/*    public void setCommits(Integer commits) {
        this.commits = commits;
    }
*/    
    public void addCommit(){
        this.commits++;
    }

    public void addCommit(Integer add){
        this.commits += add;
    }
    
    @Override
    public String toString(){
        return defineSize(this.name, 20) + "\t" + defineSize(this.email, 30)+  "\t" + this.commits;
    }
    
    private String defineSize(String str, Integer lenght) {
        String newStr = str;
        for (int i = newStr.length(); i < lenght; i++)  
            newStr += " ";
        return newStr;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public int compareTo(Committer cmt) {
        if ( this.getName().equals(cmt.getName()) || this.getEmail().equals(cmt.getEmail()) )
            return 1;
        return 0;
    }
    
    public Committer clone(){
        Committer cmt = new Committer(this.name, this.email);
        cmt.addCommit(this.commits);
        return cmt;
    }

}
