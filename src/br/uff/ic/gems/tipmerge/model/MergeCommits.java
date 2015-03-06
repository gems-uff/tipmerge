/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2cf, Catarina
 */
public class MergeCommits extends Merge{
	
	private List<Committer> cmtBranchOne;
	private List<Committer> cmtBranchTwo;
	private List<Committer> cmtCommon;
	private List<Committer> cmtBeforeMerge;

	public MergeCommits(String hashOfMerge, File pathToRepository) {
		super(hashOfMerge, pathToRepository);
	}

	/**
	 * @return the cmtBeforeMerge
	 */
	public List<Committer> getCommittersBeforeBranches() {
		return cmtBeforeMerge;
	}

	/**
	 * @param cmtBeforeMerge the cmtBeforeMerge to set
	 */
	public void setCommittersBeforeBranches(List<Committer> cmtBeforeMerge) {
		this.cmtBeforeMerge = cmtBeforeMerge;
	}

	/**
	 * @return the cmtBranchOne
	 */
	public List<Committer> getCommittersBranchOne() {
		return cmtBranchOne;
	}

	/**
	 * @param authorsBranchOne the cmtBranchOne to set
	 */
	public void setCommittersBranchOne(List<Committer> authorsBranchOne) {
		this.cmtBranchOne = authorsBranchOne;
	}

	/**
	 * @return the cmtBranchTwo
	 */
	public List<Committer> getCommittersBranchTwo() {
		return cmtBranchTwo;
	}

	/**
	 * @param cmtBranchTwo the cmtBranchTwo to set
	 */
	public void setCommittersBranchTwo(List<Committer> cmtBranchTwo) {
		this.cmtBranchTwo = cmtBranchTwo;
	}

	/**
	 * @return the cmtCommon
	 */
	public List<Committer> getCommittersInCommon() {
		if (cmtCommon == null){
			this.getCommittersBranchOne().stream().forEach((cmter1) -> {
				for(Committer cmter2 : this.getCommittersBranchTwo()){
					if (cmter1.equals(cmter2)){
						if (cmter1.getCommits() < cmter2.getCommits())
							this.addCommitterCommon(cmter1);
						else
							this.addCommitterCommon(cmter2);
					}
				}
			});
		}
		return cmtCommon;
	}

	/**
	 * @param cmtInCommon the cmtCommon to set
	 */
	public void setCommittersCommon(List<Committer> cmtInCommon) {
		this.cmtCommon = cmtInCommon;
	}
	
	public void addCommitterCommon(Committer cmtInCommon){
		if(this.cmtCommon == null)
			this.cmtCommon = new ArrayList<>();
		this.cmtCommon.add(cmtInCommon);
	}

	public List<Conciliator> getConciliators() {
		List<Conciliator> conciliatorCandidates = new ArrayList<>();
		List<Committer> committersOnBranch2 = new ArrayList(this.getCommittersBranchTwo());
		
		for(Committer committer1 : this.getCommittersBranchOne()){
			Conciliator conciliator = new Conciliator(committer1);
			conciliator.setCommitsBranch1(committer1.getCommits());
			
			Committer cmtTemp = null;
			for(Committer committer2 : committersOnBranch2){
				if (committer1.equals(committer2)){
					conciliator.setCommitsBranch2(committer2.getCommits());
					conciliator.setCommitsBoothBranchs(Math.min(conciliator.getCommitsBranch1(), conciliator.getCommitsBranch2()));
					cmtTemp = committer2;
					break;					
				}
			}if(cmtTemp != null) committersOnBranch2.remove(cmtTemp);
				conciliatorCandidates.add(conciliator);
		}
		if (committersOnBranch2.size() > 0)
			committersOnBranch2.stream().map((committer2) -> {
				Conciliator conciliator = new Conciliator(committer2);
				conciliator.setCommitsBranch2(committer2.getCommits());
				return conciliator;
			}).forEach((conciliator) -> {
				conciliatorCandidates.add(conciliator);
			});
		
		//agora será incluída a lista de todos aqueles que estavam "fora dos ramos"
		this.getCommittersBeforeBranches().stream().forEach((cmter) -> {
			boolean isOnBranches = false;
			for (Conciliator conciliator : conciliatorCandidates){
				if (conciliator.getCommitter().equals(cmter)){
					conciliator.setCommitsHistory(cmter.getCommits());
					isOnBranches = true;
				}
			}
			if (!isOnBranches) {
				Conciliator newConciliator = new Conciliator(cmter);
				newConciliator.setCommitsHistory(cmter.getCommits());
				conciliatorCandidates.add(newConciliator);
			}
		});
		return conciliatorCandidates;
	}
	
	public boolean isMergeOfBranches(){
		return (this.getCommittersBranchOne().size() >= 2) && (this.getCommittersBranchTwo().size() >= 2);
	}
}
