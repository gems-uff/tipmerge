/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.gui.JFrameDependencies;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Dependencies;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import br.uff.ic.gems.tipmerge.model.Repository;
import dao.DominoesSQLDao2;
import domain.Dominoes;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j2cf
 */
public class Experiment {
	
	private final Repository repository;
	
	public Experiment(Repository repository){
		this.repository = repository;
	}
	
	public Repository getRepo(){
		return this.repository;
	}

	public Map<String, Integer[]> getDatasFromMerges() {
		Map<String, Integer[]> datas = new HashMap<>();
		
		for(String hashMerge : this.getRepo().getListOfMerges()){
			
			Integer[] values = new Integer[4];
			
			DateFormat timeFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss.SSS");  
			String formattedDate = timeFormat.format(new Date());  
			
			System.out.println("Merge under review: " + hashMerge + "\tTime: " + formattedDate);
			
			MergeFiles mergeFiles = getFilesAnalisys(hashMerge.split(" ")[0]);
			boolean hasDevelopers = hasEnoughtDevelopers(mergeFiles);
			
			if (hasDevelopers)
				values[0] = 1;
			else
				values[0] = 0;
			
			if(hasDevelopers){
				
				System.out.println("\t1. Enough Committer:\tYES");

				int filesInCommon = mergeFiles.getFilesOnBothBranch().size();
				values[1] = filesInCommon;
				
				if (filesInCommon == 0) {

					System.out.println("\t2. Files in common:\tNO");				

				}else{

					System.out.println("\t2. Files in common:\tYES\t" + filesInCommon);				

				}

				List<Map<EditedFile,Set<EditedFile>>> dependencies = getFilesDependencies(mergeFiles);
				boolean hasNoDependencies = (dependencies.get(0).isEmpty() && dependencies.get(1).isEmpty());

				if (hasNoDependencies) {
					values[2] = 0;
					System.out.println("\t3. Dependencies:\tNO");
					
				}else{
					values[2] = 1;
					System.out.println("\t3. Dependencies:\tYES");

				}
				if((filesInCommon > 0) || (!hasNoDependencies) ){
					
					RankingGenerator rGenerator = getRanking(dependencies, mergeFiles);
					List<Medalist> ranking = rGenerator.getRanking();
					
					Committer committer = CommitterDao.getCommitter1(hashMerge.split(" ")[0], this.getRepo());
					
					int position = ranking.indexOf(new Medalist(committer)) + 1;
					values[3] = position;
					System.out.println("\t4. Rank position:\t" + position + "\t" + committer.getName() );
					
					//System.out.println(rGenerator.toString());
					
				}
			}
			else
				System.out.println("\t1. Enough Commiter:\tNO");

			datas.put(hashMerge, values);
			System.gc();
		}
		
		return datas;
	}

	private RankingGenerator getRanking(List<Map<EditedFile, Set<EditedFile>>> dependencies, MergeFiles mergeFiles) {
		RankingGenerator rGenerator = new RankingGenerator();
		
		Set<EditedFile> filesOfInterest = new HashSet<>();
		
		for(EditedFile file : dependencies.get(0).keySet()){
			filesOfInterest.add(file);
			filesOfInterest.addAll(dependencies.get(0).get(file));
		}
		for(EditedFile file : dependencies.get(1).keySet()){
			filesOfInterest.add(file);
			filesOfInterest.addAll(dependencies.get(1).get(file));
		}
		filesOfInterest.addAll(mergeFiles.getFilesOnBothBranch());
		
		Set<EditedFile> excepiontFiles = rGenerator.setMedalsFilesEditedBothBranches(mergeFiles);
		excepiontFiles = rGenerator.setMedalFromDependencies(dependencies.get(0), mergeFiles, excepiontFiles);
		excepiontFiles = rGenerator.setMedalFromDependencies(dependencies.get(1), mergeFiles, excepiontFiles);
		excepiontFiles.removeAll(excepiontFiles);
		
		return rGenerator;
	}
	
	private MergeFiles getFilesAnalisys(String merge) {

		MergeFilesDao mergeFilesDao = new MergeFilesDao();

		MergeFiles mergeSelected = mergeFilesDao.getMerge(merge, this.getRepo().getProject());
			
		EditedFilesDao filesDao = new EditedFilesDao();
		mergeSelected.setFilesOnBranchOne(filesDao.getFiles(mergeSelected.getHashBase(), 
                                                                    mergeSelected.getParents()[0], 
                                                                    mergeSelected.getPath(),
                                                                    Parameter.EXTENSION));
		mergeSelected.setFilesOnBranchTwo(filesDao.getFiles(mergeSelected.getHashBase(), 
                                                                    mergeSelected.getParents()[1], 
                                                                    mergeSelected.getPath(),
                                                                    Parameter.EXTENSION));
		
		CommitterDao cmterDao = new CommitterDao();
		List<EditedFile> files = new LinkedList<>();
		
		for(EditedFile editedFile : mergeSelected.getFilesOnBranchOne()){
			List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(), 
										mergeSelected.getParents()[0], 
										editedFile.getFileName(), 
										mergeSelected.getPath());
			if(whoEdited.size() > 0){
				editedFile.setWhoEditTheFile(whoEdited);
				files.add(editedFile);
			}
		}mergeSelected.setFilesOnBranchOne(files);
		
		files = new LinkedList<>();
		for(EditedFile editedFile : mergeSelected.getFilesOnBranchTwo()){
			List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(), 
										mergeSelected.getParents()[1], 
										editedFile.getFileName(), 
										mergeSelected.getPath());
			if(whoEdited.size() > 0){
				editedFile.setWhoEditTheFile(whoEdited);
				files.add(editedFile);
			}
		}
		mergeSelected.setFilesOnBranchTwo(files);
		
		files = new LinkedList<>();
		for(EditedFile editedFile : mergeSelected.getFilesOnPreviousHistory()){
			List<Committer> whoEdited = cmterDao.getWhoEditedFile(this.getRepo().getFirstCommit(), 
										mergeSelected.getHashBase(), 
										editedFile.getFileName(), 
										mergeSelected.getPath());
			if(whoEdited.size() > 0){
				editedFile.setWhoEditTheFile(whoEdited);
				files.add(editedFile);
			}
		}mergeSelected.setFilesOnPreviousHistory(new HashSet<>(files));
				
		return mergeSelected;
		
	}

	private List<Map<EditedFile, Set<EditedFile>>> getFilesDependencies(MergeFiles mergeFiles) {
		
		List<Map<EditedFile, Set<EditedFile>>> depList = new ArrayList<>();
		depList.add(new HashMap<>());
		depList.add(new HashMap<>());
		
		MergeCommitsDao mCommitsDao = new MergeCommitsDao(this.getRepo().getProject());
		List<String> hashsOnPreviousHistory = mCommitsDao.getHashs(this.getRepo().getFirstCommit() , mergeFiles.getHashBase());

		try {

			//System.out.println("\nCreating the dominoes of History");
			List<Dominoes> dominoesHistory = DominoesSQLDao2.loadAllMatrices(Parameter.DATABASE, this.getRepo().getName(), "CPU", hashsOnPreviousHistory);

			Dominoes domCF = dominoesHistory.get(6);
			Dominoes domCFt = domCF.cloneNoMatrix();
			domCFt.transpose();
			Dominoes domFF = domCFt.multiply(domCF);
			domFF.confidence();

			Dependencies dependencies = new Dependencies(domFF);
			double threshold = Parameter.THRESHOLD;

			//System.out.println("Dependencies Branch One");
			depList.add(0, dependencies.getDependenciesAcrossBranches(
					mergeFiles.getFilesOnBranchOne(), 
					mergeFiles.getFilesOnBranchTwo(),
					threshold));
			depList.add(1, dependencies.getDependenciesAcrossBranches(
					mergeFiles.getFilesOnBranchTwo(), 
					mergeFiles.getFilesOnBranchOne(),
					threshold));
		} catch (SQLException ex) {
			Logger.getLogger(JFrameDependencies.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(JFrameDependencies.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return depList;
		
	}

	private boolean hasEnoughtDevelopers(MergeFiles mergeFiles) {
		if (mergeFiles.getCommittersOnBranchOne().isEmpty() || mergeFiles.getCommittersOnBranchTwo().isEmpty())
			return false;
		if (mergeFiles.getCommittersOnBranchOne().size() == 1 && mergeFiles.getCommittersOnBranchTwo().size() == 1)
			return !mergeFiles.getCommittersOnBranchOne().get(0).equals(mergeFiles.getCommittersOnBranchTwo().get(0));
		return true;

	}
	
}
