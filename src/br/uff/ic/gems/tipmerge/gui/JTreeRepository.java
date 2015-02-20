/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.model.Merge;
import br.uff.ic.gems.tipmerge.model.Repository;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author j2cf
 */
public class JTreeRepository extends JTree{
	
	Repository repo;

	public JTreeRepository(Repository repository) {
		this.repo = repository;
		update();
	}
	
	public void update(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(repo.getName());
		
		DefaultMutableTreeNode mergeNode = new DefaultMutableTreeNode("Merges");
		DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode("Branches");
		
		repo.getBranches().stream().map((branch) -> new DefaultMutableTreeNode(branch)).forEach((newNode) -> {
			branchNode.add(newNode);
		});

		//repo.getMerges().stream().map((merge) -> new DefaultMutableTreeNode(merge.getHash())).forEach((mNode) -> {
		//	mergeNode.add(mNode);
		//});
		
		int i = 1;
		for (Merge merge: repo.getMerges()){
			mergeNode.add(new DefaultMutableTreeNode("(" + i++ + ") " + merge.getHash()));
		}
		
		root.add(mergeNode);
		root.add(branchNode);
		this.setModel(new DefaultTreeModel(root));
	}
	
	
}
