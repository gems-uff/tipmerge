/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.RepoFiles;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.Export;
import br.uff.ic.gems.tipmerge.util.Statistics;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 * This class is in charge of showing all results about the files analysis
 *
 * @author j2cf, Catarina
 */
public class JFrameFilesAnalysis extends javax.swing.JFrame {

    private final RepoFiles repoFiles;
    private MergeFiles mergeFiles;

    /**
     * Creates new form JFrameCommitsAnalysis
     *
     * @param repository
     */
    public JFrameFilesAnalysis(Repository repository) {
        this.repoFiles = new RepoFiles(repository);
        if(repoFiles.getMergeFiles() == null || repoFiles.getMergeFiles().isEmpty()){
            RepositoryDao rdao = new RepositoryDao(repository.getProject());
            rdao.setDetails(repository);
        }
        initComponents();
        setParameters();            
    }

    //1- Shows the project branches that the user can select to merge - 2- shows all the existing merges - 3 - and put the name of the project
    private void setParameters() {
        jcMerge1.setModel(
                new JComboBox(
                        repoFiles.getRepository().getListOfMerges().toArray())
                .getModel()
        );
        jPanel1.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Project " + repoFiles.getRepository().getName())
        );
        txRepositoryName.setText(repoFiles.getRepository().getName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hash1 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        btRun = new javax.swing.JButton();
        jLSelecByExt = new javax.swing.JLabel();
        comboFileExtension = new javax.swing.JComboBox();
        jcMerge1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        labelRepository = new javax.swing.JLabel();
        txRepositoryName = new javax.swing.JTextField();
        btExport = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButtonDependencies = new javax.swing.JButton();
        btnChart2 = new javax.swing.JButton();
        btnChart1 = new javax.swing.JButton();

        hash1.setText("<hash>");

        jFrame1.setBounds(500, 150, 500, 500);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Files Analysis");

        btRun.setText("Run");
        btRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRunActionPerformed(evt);
            }
        });

        jLSelecByExt.setText("File Extensions");

        comboFileExtension.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Files", ".java", ".c", ".html", ".js", ".py", ".php", ".rb", ".xml" }));

        jcMerge1.setToolTipText("Select a previous merge");
        jcMerge1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcMerge1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Select a Merge");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/loading1.gif"))); // NOI18N
        jLabel1.setText("Loading ...");
        jLabel1.setVisible(false);

        labelRepository.setText("Repository Name");

        txRepositoryName.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLSelecByExt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboFileExtension, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelRepository)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcMerge1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txRepositoryName))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRepository)
                    .addComponent(txRepositoryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcMerge1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboFileExtension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRun)
                    .addComponent(jLabel1)
                    .addComponent(jLSelecByExt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btExport.setText("Export");
        btExport.setEnabled(false);
        btExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExportActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTable1);

        jTabbedPane1.addTab("Branch1", jScrollPane5);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTable2);

        jTabbedPane1.addTab("Branch2", jScrollPane6);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTable3);

        jTabbedPane1.addTab("Both Branches", jScrollPane7);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(jTable4);

        jTabbedPane1.addTab("Previous History", jScrollPane8);

        jButtonDependencies.setText("See Logical Dependencies");
        jButtonDependencies.setEnabled(false);
        jButtonDependencies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDependenciesActionPerformed(evt);
            }
        });

        btnChart2.setText("Chart2");
        btnChart2.setEnabled(false);
        btnChart2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChart2ActionPerformed(evt);
            }
        });

        btnChart1.setText("Chart1");
        btnChart1.setEnabled(false);
        btnChart1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChart1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChart1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChart2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDependencies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExport)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btExport)
                    .addComponent(jButtonDependencies)
                    .addComponent(btnChart2)
                    .addComponent(btnChart1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRunActionPerformed
        Runnable r = () -> {
            jLabel1.setVisible(true);
            btRun.setEnabled(false);

            /**
             * At this time a merge will be created or - the selection of
             * branches - by selecting one merge from the history
             */
            MergeFilesDao mergeFilesDao = new MergeFilesDao();
            String hash = codHash(jcMerge1.getSelectedItem().toString());
            MergeFiles mergeSelected = mergeFilesDao.getMerge(hash, repoFiles.getRepository().getProject());

            /**
             * From now the merge already exists with parents and merge base,
             * next steps are: Set the files of that merge and committers that
             * changed that files.
             */
            //System.out.println(mergeSelected.getHash() + "\n" + mergeSelected.getHashBase() + "\n" + Arrays.toString(mergeSelected.getParents()));
            EditedFilesDao filesDao = new EditedFilesDao();
            mergeSelected.setFilesOnBranchOne(filesDao.getFiles(mergeSelected.getHashBase(),
                    mergeSelected.getParents()[0],
                    mergeSelected.getPath(),
                    comboFileExtension.getSelectedItem().toString()));
            mergeSelected.setFilesOnBranchTwo(filesDao.getFiles(mergeSelected.getHashBase(),
                    mergeSelected.getParents()[1],
                    mergeSelected.getPath(),
                    comboFileExtension.getSelectedItem().toString()));

            CommitterDao cmterDao = new CommitterDao();
            List<EditedFile> files = new LinkedList<>();

            for (EditedFile editedFile : mergeSelected.getFilesOnBranchOne()) {
                List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(),
                        mergeSelected.getParents()[0],
                        editedFile.getFileName(),
                        mergeSelected.getPath());
                if (whoEdited.size() > 0) {
                    editedFile.setWhoEditTheFile(whoEdited);
                    files.add(editedFile);
                }
            }
            mergeSelected.setFilesOnBranchOne(files);

            files = new LinkedList<>();
            for (EditedFile editedFile : mergeSelected.getFilesOnBranchTwo()) {
                List<Committer> whoEdited = cmterDao.getWhoEditedFile(mergeSelected.getHashBase(),
                        mergeSelected.getParents()[1],
                        editedFile.getFileName(),
                        mergeSelected.getPath());
                if (whoEdited.size() > 0) {
                    editedFile.setWhoEditTheFile(whoEdited);
                    files.add(editedFile);
                }
            }
            mergeSelected.setFilesOnBranchTwo(files);

            files = new LinkedList<>();
            for (EditedFile editedFile : mergeSelected.getFilesOnPreviousHistory()) {
                List<Committer> whoEdited = cmterDao.getWhoEditedFile(repoFiles.getRepository().getFirstCommit(),
                        mergeSelected.getHashBase(),
                        editedFile.getFileName(),
                        mergeSelected.getPath());
                if (whoEdited.size() > 0) {
                    editedFile.setWhoEditTheFile(whoEdited);
                    files.add(editedFile);
                }
            }
            mergeSelected.setFilesOnPreviousHistory(new HashSet<>(files));

            //prints on the command line
            //showCommitters(mergeSelected);
            repoFiles.getMergeFiles().add(mergeSelected);

            this.setMergeFiles(mergeSelected);

            showResultsTable(this.getMergeFiles());
            //showResultsTable(this.getMergeFiles(),true);
            //showResIntersection(mCommits.getCommittersBothBranches());

            btExport.setEnabled(true);
            btnChart1.setEnabled(true);
            btnChart2.setEnabled(true);
            jButtonDependencies.setEnabled(true);
            jLabel1.setVisible(false);
            btRun.setEnabled(true);
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btRunActionPerformed

    public void showResultsTable(MergeFiles merge) {
        //organizes the data in the table
        showResBranch1(merge, false);
        showResBranch2(merge, false);
        showResIntersection(merge.getFilesOnBothBranch());
        showResPreviousHistory(merge, false);
    }

    public void showResultsTable(MergeFiles merge, Boolean showScoreZ) {
        //organizes the data in the table
        showResBranch1(merge, showScoreZ);
        showResBranch2(merge, showScoreZ);
        showResPreviousHistory(merge, showScoreZ);
    }

    private void btExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExportActionPerformed
        Map<String, TableModel> sheet = new LinkedHashMap<>();
        sheet.put("Branch1", jTable1.getModel());
        sheet.put("Branch2", jTable2.getModel());
        sheet.put("Both Branches", jTable3.getModel());
        sheet.put("Previous History", jTable4.getModel());

        Export.toExcel(sheet);
        JOptionPane.showMessageDialog(this, "File was sucessfully saved", null, JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_btExportActionPerformed


    private void jButtonDependenciesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDependenciesActionPerformed

        JFrameDependencies filesDependence = new JFrameDependencies(this.repoFiles.getRepository(), this.getMergeFiles());
        filesDependence.setLocationRelativeTo(this.getFocusOwner());
        filesDependence.setVisible(true);

    }//GEN-LAST:event_jButtonDependenciesActionPerformed

    private void btnChart1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChart1ActionPerformed
        // Botão do Chart 1:
        newGraphic(this.getMergeFiles(), 1);
    }//GEN-LAST:event_btnChart1ActionPerformed

    private void btnChart2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChart2ActionPerformed
        //Botão do Chart 2:
        newGraphic(this.getMergeFiles(), 2);
    }//GEN-LAST:event_btnChart2ActionPerformed

    private void jcMerge1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcMerge1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcMerge1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btExport;
    private javax.swing.JButton btRun;
    private javax.swing.JButton btnChart1;
    private javax.swing.JButton btnChart2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox comboFileExtension;
    private javax.swing.JLabel hash1;
    private javax.swing.JButton jButtonDependencies;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLSelecByExt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JComboBox jcMerge1;
    private javax.swing.JLabel labelRepository;
    private javax.swing.JTextField txRepositoryName;
    // End of variables declaration//GEN-END:variables

    /*
    private void showCommitters(MergeFiles mergeFiles) {
        System.out.println("Merge: " + mergeFiles.getHash());
        System.out.println("Branch One");
        mergeFiles.getFilesOnBranchOne().stream().forEach((file) -> {
            System.out.println(file.getFileName());
            file.getWhoEditTheFile().stream().forEach((cmter) -> {
                System.out.println("\t" + cmter.toString());
            });
        });
        System.out.println("Branch Two");
        mergeFiles.getFilesOnBranchTwo().stream().forEach((file) -> {
            System.out.println(file.getFileName());
            file.getWhoEditTheFile().stream().forEach((cmter) -> {
                System.out.println("\t" + cmter.toString());
            });
        });
        
        mergeFiles.getCommittersOnMege().stream().forEach((cmt) -> {
            System.out.println(cmt.toString());
        });
         
    }
    */

    //shows the number of commits by committers in each file on Branch 1
    private void showResBranch1(MergeFiles mergeSelected, Boolean showScoreZ) {
        DefaultTableModel dftModel = new DefaultTableModel(new Object[]{"File name"}, 0);

        List<Committer> committers = mergeSelected.getCommittersOnBranchOne();

        //Includes columns with the names of all developers (branches 1 and 2)
        committers.stream().forEach((committer) -> {
            dftModel.addColumn(committer.getName());
        });

        //dftModel.addRow(new Object[]{"BRANCH ONE"});
        mergeSelected.getFilesOnBranchOne().stream().forEach((editedfile) -> {
            dftModel.addRow(getValueToRow(editedfile, committers, showScoreZ));
            //dftModel.addRow(new Object[]{file.getFileName()});

        });

        jTable1.setModel(dftModel);
    }

    //shows the number of commits by committers in each file on Branch 2
    private void showResBranch2(MergeFiles mergeSelected, Boolean showScoreZ) {
        DefaultTableModel dftModel = new DefaultTableModel(new Object[]{"File name"}, 0);

        List<Committer> committers = mergeSelected.getCommittersOnBranchTwo();

        //Includes columns with the names of all developers (branches 1 and 2)
        committers.stream().forEach((committer) -> {
            dftModel.addColumn(committer.getName());
        });

        //dftModel.addRow(new Object[]{"BRANCH TWO"});
        mergeSelected.getFilesOnBranchTwo().stream().forEach((file) -> {
            dftModel.addRow(getValueToRow(file, committers, showScoreZ));

        });

        jTable2.setModel(dftModel);
        //tbResultsBranch1.update(tbResultsBranch1.getGraphics());	
    }

    private void showResIntersection(Set<EditedFile> filesOnBothBranch) {
        DefaultTableModel dftModel = new DefaultTableModel(new Object[]{"File name"}, 0);

        filesOnBothBranch.stream().forEach((file) -> {
            dftModel.addRow(new String[]{file.getFileName(), ""});
        });

        jTable3.setModel(dftModel);
    }

    //shows the number of commits by committers in each file (changed on any branch) that was changed in the history before the branch
    private void showResPreviousHistory(MergeFiles mergeSelected, Boolean showScoreZ) {

        DefaultTableModel dftModel = new DefaultTableModel(new Object[]{"File name"}, 0);

        List<Committer> committers = mergeSelected.getCommittersOnPreviousHistory();

        //	Arrays.sort(committers);
        //Includes columns with the names of all developers (branches 1 and 2)
        committers.stream().forEach((committer) -> {
            dftModel.addColumn(committer.getName());
            //	System.out.println(committer.toString());
        });

        //dftModel.addRow(new Object[]{"PREVIOUS HISTORY"});
        mergeSelected.getFilesOnPreviousHistory().stream().forEach((file) -> {
            if (file.getWhoEditTheFile().size() > 0) {
                dftModel.addRow(getValueToRow(file, committers, showScoreZ));
            }

        });

        jTable4.setModel(dftModel);
    }

    private String[] getValueToRow(EditedFile editedFile, List<Committer> committers, Boolean showScoreZ) {

        String fileName = editedFile.getFileName();
        Integer[] values = new Integer[committers.size()];
        editedFile.getWhoEditTheFile().stream().forEach((cmtrFile) -> {
            int index = 0;
            for (Committer cmter : committers) {
                if (cmtrFile.equals(cmter)) {
                    values[index] = cmtrFile.getCommits();
                    break;
                }
                index++;
            }

        });
        String[] result = getArrayResult(fileName, values, showScoreZ);

        return result;
    }

    /**
     * @return the mergeFiles
     */
    public MergeFiles getMergeFiles() {
        return mergeFiles;
    }

    /**
     * @param mergeFiles the mergeFiles to set
     */
    public void setMergeFiles(MergeFiles mergeFiles) {
        this.mergeFiles = mergeFiles;
    }

    private String[] getArrayResult(String fileName, Integer[] values, Boolean showScoreZ) {
        String[] result = new String[values.length + 1];
        result[0] = fileName;

        for (int i = 1; i < result.length; i++) {
            result[i] = values[i - 1] == null ? (values[i - 1] = 0).toString() : values[i - 1].toString();
        }

        if (showScoreZ) {
            List<Double> scores = Statistics.getMZScore(values);
            for (int i = 0; i < scores.size(); i++) {
                result[i + 1] = scores.get(i).toString();
            }
            return result;
        }

        return result;
    }

    private String codHash(String hash) {
        String temp = hash;
        hash = "";
        String temp2;
        boolean valid = true;
        for (int i = 0; i < temp.length(); i++) {
            temp2 = String.valueOf(temp.charAt(i));
            if (temp2.equals(" ")) {
                valid = false;
            } else {
                if (valid == true) {
                    hash = hash + temp2;
                }
            }
        }
        return hash;
    }

    private String limit(String fileName) {
        int length = fileName.length();
        if (length > 30) {
            String[] parts = fileName.split("/");
//                          System.out.println(parts[parts.length - 1]);
            return parts[parts.length - 1];
        }
        return fileName;
    }

    private CategoryDataset createBranch(int botao) {
        DefaultCategoryDataset dataBranch1 = new DefaultCategoryDataset();
        JTable table12 = jTable1;
        if (jTable1.isShowing() || jTable2.isShowing()) {
            String fileName;
            if (jTable1.isShowing()) {
                table12 = jTable1;
            } else {
                if (jTable2.isShowing()) {
                    table12 = jTable2;
                }
            }
            if (botao == 1) {
                double num;
                for (int i = 1; i < table12.getColumnCount(); i++) {
                    for (int j = 0; j < table12.getRowCount(); j++) {
                        num = Integer.parseInt((String) table12.getValueAt(j, i));
                        if (num != 0) {
                            fileName = limit((String) table12.getValueAt(j, 0));
                            dataBranch1.addValue(num, fileName, table12.getColumnName(i));
                        }
                    }
                }
            }

        }
        if (jTable4.isShowing()) {
            if (botao == 1) {
                double cont = 0;
                for (int i = 1; i < jTable4.getColumnCount(); i++) {
                    for (int j = 0; j < jTable4.getRowCount(); j++) {
                        if (Integer.parseInt((String) jTable4.getValueAt(j, i)) == 0) {

                        } else {
                            cont++;
                        }
                    }
                    dataBranch1.addValue(cont, jTable4.getColumnName(i), "Number of Files");
                    cont = 0;
                }
            }
        }
        if (botao == 2) {
            String fileName = " ";
            for (int k = 0; k < jTable3.getRowCount(); k++) {
                for (EditedFile file : mergeFiles.getFilesOnBranchOne()) {
                    for (Committer comitter : file.getWhoEditTheFile()) {
                        if (file.getFileName().equals(jTable3.getValueAt(k, 0))) {
                            if (comitter.getCommits() != 0) {
                                fileName = limit(file.getFileName());
                                dataBranch1.addValue(comitter.getCommits(), comitter.getName(), "B1 " + fileName);
                            }
                        }
                    }
                }
            }
            for (int k = 0; k < jTable3.getRowCount(); k++) {
                for (EditedFile file : mergeFiles.getFilesOnBranchTwo()) {
                    for (Committer comitter : file.getWhoEditTheFile()) {
                        if (file.getFileName().equals(jTable3.getValueAt(k, 0))) {
                            if (comitter.getCommits() != 0) {
                                fileName = limit(file.getFileName());
                                dataBranch1.addValue(comitter.getCommits(), comitter.getName(), "B2 " + fileName);
                            }
                        }
                    }
                }
            }
            double num;
            for (int i = 0; i < jTable3.getRowCount(); i++) {
                for (int j = 0; j < jTable4.getRowCount(); j++) {
                    if (jTable4.getValueAt(j, 0).equals(jTable3.getValueAt(i, 0))) {
                        for (int k = 1; k < jTable4.getColumnCount(); k++) {
                            num = Integer.parseInt((String) jTable4.getValueAt(j, k));
                            if (num != 0) {
                                fileName = limit((String) jTable4.getValueAt(j, 0));
                                dataBranch1.addValue(num, jTable4.getColumnName(k), "H " + fileName);
                            }
                        }
                    }
                }
            }

        }
        return dataBranch1;
    }

    public void newGraphic(MergeFiles merge, int botao) {
        jFrame1.setVisible(true);
        CategoryDataset cdsBranch1 = createBranch(botao);
        String title = "";
        if (jTable1.isShowing()) {
            title = "Branch 1";
        }
        if (jTable2.isShowing()) {
            title = "Branch 2";
        }
        if (jTable3.isShowing()) {
            title = "Both Branches";
        }
        if (jTable4.isShowing()) {
            title = "Previous History";
        }
        JFreeChart graphic;
        if (botao == 1) {
            graphic = ChartFactory.createBarChart3D(title, "Names", "Commit", cdsBranch1, PlotOrientation.VERTICAL, true, true, true);
        } else {
            graphic = ChartFactory.createStackedBarChart("Modified Files in Both Branches", "History/Branch2/Branch1", "Commiters Name", cdsBranch1, PlotOrientation.HORIZONTAL, true, true, true);
        }
        CategoryPlot plot = graphic.getCategoryPlot();
        CategoryItemRenderer itemRerender = plot.getRenderer();
        itemRerender.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0")));
        itemRerender.setItemLabelsVisible(true);
        ChartPanel chartPanel = new ChartPanel(graphic);
        chartPanel.setPreferredSize(new java.awt.Dimension(590, 350));
        jFrame1.setContentPane(chartPanel);
        jFrame1.pack();
        RefineryUtilities.centerFrameOnScreen(jFrame1);
        jFrame1.setVisible(true);

    }

}
