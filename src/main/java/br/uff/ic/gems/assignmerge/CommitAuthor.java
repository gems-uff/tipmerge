/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

/**
 *
 * @author catarinacosta
 */
public class CommitAuthor implements Comparable<CommitAuthor>{
    
    private String name;
    
    private Integer commits;
    
    private String email;
 
    public CommitAuthor(String name, String email){
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
		this.setCommits((Integer) (this.getCommits() + 1));
    }

    public void addCommit(Integer add){
		this.setCommits((Integer) (this.getCommits() + add));
    }
    
    @Override
    public String toString(){
        //return defineSize(this.name, 20) + "\t" + defineSize(this.email, 30)+  "\t" + this.getCommits();
		return this.name + " (" + this.getCommits() + ")";
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

    public int compareTo(CommitAuthor cmt) {
        if ( this.getName().equals(cmt.getName()) || this.getEmail().equals(cmt.getEmail()) )
            return 1;
        return 0;
    }
    
    public CommitAuthor clone(){
        CommitAuthor cmt = new CommitAuthor(this.name, this.email);
        cmt.addCommit(this.getCommits());
        return cmt;
    }

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(Integer commits) {
		this.commits = commits;
	}

}
