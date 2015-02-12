/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.assignmerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author j2cf
 */
public class AdvancedEvaluator{
	

	public static StringBuilder getMergeStatistics(MergeBranches merge){

		List<Candidate> candidates = new ArrayList<Candidate>();
		List<CommitAuthor> lcaBranch2 = new ArrayList(merge.getAuthorsBranchTwo());
		
		for(CommitAuthor caBranch1 : merge.getAuthorsBranchOne()){
			Candidate cand = new Candidate(caBranch1);
			cand.setCbOne(caBranch1.getCommits());
			CommitAuthor caTemp = null;
			for(CommitAuthor caBranch2 : lcaBranch2){
				if(cand.getAuthor().equals(caBranch2.getName())){
					cand.setCbTwo(caBranch2.getCommits());
					cand.setcBooth(Math.min(cand.getCbOne(), cand.getCbTwo()));
					caTemp = caBranch2;
					break;
				}
			}if(caTemp != null) lcaBranch2.remove(caTemp);
			candidates.add(cand);
		}
		
		for(CommitAuthor caBranch2 : lcaBranch2){
			Candidate cand = new Candidate(caBranch2);
			cand.setCbTwo(caBranch2.getCommits());
			candidates.add(cand);
		}
		

		String command = "git shortlog -s -n " + merge.getProject().getFirstCommit()  + ".." +  merge.getMergeBase();
		List<String> commitsPerAuthor = CommandLine.getMultiplesResults(command, merge.getProject().getProjectDirectory());
		
		StringBuilder result = new StringBuilder();
		
		result.append(merge.getCommitHash()).append("\n");
		for(String str : commitsPerAuthor){
			str = str.trim();
			String historyDatas[]= str.split("\t");
			String authorName = historyDatas[1], commits = historyDatas[0];
			boolean isOnBranches = false;
			for (Candidate cand : candidates){
				if (cand.getAuthor().equals(authorName)){
					cand.setcHistory(Integer.valueOf(commits));
					isOnBranches = true;
				}
			}
			if (!isOnBranches){
				Candidate newCand = new Candidate(new CommitAuthor(authorName, ""));
				newCand.setcHistory(Integer.valueOf(commits));
				candidates.add(newCand);
			}
		}
		for(Candidate cand : candidates)
			result.append("\t").append(cand.toString()).append("\n");
		
		return result;
		
	}

	public static List<Candidate> getCandidatesOfMerge(MergeBranches merge) {
		List<Candidate> candidates = new ArrayList<Candidate>();
		List<CommitAuthor> lcaBranch2 = new ArrayList(merge.getAuthorsBranchTwo());
		
		for(CommitAuthor caBranch1 : merge.getAuthorsBranchOne()){
			Candidate cand = new Candidate(caBranch1);
			cand.setCbOne(caBranch1.getCommits());
			CommitAuthor caTemp = null;
			for(CommitAuthor caBranch2 : lcaBranch2){
				if(cand.getAuthor().equals(caBranch2.getName())){
					cand.setCbTwo(caBranch2.getCommits());
					cand.setcBooth(Math.min(cand.getCbOne(), cand.getCbTwo()));
					caTemp = caBranch2;
					break;
				}
			}if(caTemp != null) lcaBranch2.remove(caTemp);
			candidates.add(cand);
		}
		
		for(CommitAuthor caBranch2 : lcaBranch2){
			Candidate cand = new Candidate(caBranch2);
			cand.setCbTwo(caBranch2.getCommits());
			candidates.add(cand);
		}
		
		String command = "git shortlog -s -n " + merge.getProject().getFirstCommit()  + ".." +  merge.getMergeBase();
		List<String> commitsPerAuthor = CommandLine.getMultiplesResults(command, merge.getProject().getProjectDirectory());
		
		for(String str : commitsPerAuthor){
			str = str.trim();
			String historyDatas[]= str.split("\t");
			String authorName = historyDatas[1], commits = historyDatas[0];
			boolean isOnBranches = false;
			for (Candidate cand : candidates){
				if (cand.getAuthor().equals(authorName)){
					cand.setcHistory(Integer.valueOf(commits));
					isOnBranches = true;
				}
			}
			if (!isOnBranches){
				Candidate newCand = new Candidate(new CommitAuthor(authorName, ""));
				newCand.setcHistory(Integer.valueOf(commits));
				candidates.add(newCand);
			}
		}
		
		return candidates;	
	}
	
}
