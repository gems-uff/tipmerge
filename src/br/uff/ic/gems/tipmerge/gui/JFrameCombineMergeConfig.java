/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

/**
 *
 * @author j2cf
 */
public class JFrameCombineMergeConfig extends javax.swing.JFrame {

    final private JFrameRankingCoverageFile jFrameRankingCoverageFile;
    private RankingGenerator rGenerator;
    
    private DefaultListModel availableModel;
    private DefaultListModel unavailableModel;
    private List<Integer> availableIndexes;
    private List<Integer> unavailableIndexes;
    


    JFrameCombineMergeConfig(RankingGenerator rGenerator, JFrameRankingCoverageFile jFrameRankingCoverageFile) {
        initComponents();
        JFrameCombineMergeConfig athis = this;
        this.rGenerator = rGenerator;
        this.jFrameRankingCoverageFile = jFrameRankingCoverageFile;
        this.availableIndexes = new ArrayList<>();
        this.unavailableIndexes = new ArrayList<>();
        
        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                athis.rGenerator.setMaxIterations(-1);

                athis.setVisible(false);
                jFrameRankingCoverageFile.enableCombinedMergeButton();
                jFrameRankingCoverageFile.showCoverage(rGenerator);
                //frame.dispose();
            }
        });
        
        int devs = rGenerator.getDevelopersQuantity();
        ((SpinnerNumberModel) this.devNumberSpinner.getModel()).setMinimum(0);
        ((SpinnerNumberModel) this.devNumberSpinner.getModel()).setMaximum(devs);
        ((SpinnerNumberModel) this.timeSpinner.getModel()).setMinimum(0);
        ((SpinnerNumberModel) this.iterationsSpinner.getModel()).setMinimum(0);
        this.showHideButtonActionPerformed(null);
        
        this.availableModel = (DefaultListModel) availableList.getModel();
        this.unavailableModel = (DefaultListModel) unavailableList.getModel();
        
        availableModel.clear();
        unavailableModel.clear();
        
        int index = 0;
        for (Medalist medalist : rGenerator.getDevelopers()) {
           availableModel.addElement(medalist.getCommitter().getName());
           availableIndexes.add(index++);
        }
       
    }
    
    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private void extendAndShowCoverage() {
        BitSet availableDevelopers = new BitSet(rGenerator.getDevelopersQuantity());
        for (Integer bit : availableIndexes) {
            availableDevelopers.set(bit);
        }
        
        Runnable r = () -> {
            labelLoading.setVisible(true);
            combineButton.setEnabled(false);
            resetButton.setEnabled(false);
            rGenerator.combineDevelopers(
                    (Integer) devNumberSpinner.getValue(),
                    ((DoubleSpinner) coverageSpinner).getDouble(),
                    (Integer) iterationsSpinner.getValue(),
                    (Integer) timeSpinner.getValue(),
                    -1,
                    ((DoubleSpinner) goldSpinner).getDouble(),
                    ((DoubleSpinner) silverSpinner).getDouble(),
                    ((DoubleSpinner) bronzeSpinner).getDouble(),
                    1,
                    availableDevelopers
            );
            combineButton.setEnabled(true);
            resetButton.setEnabled(true);
            labelLoading.setVisible(false);
            close();
        };
        Thread t = new Thread(r);
        t.start();
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mergeRequirements = new javax.swing.JPanel();
        devNumberSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        coverageSpinner = new DoubleSpinner(0.0, 1.0, 0.01);
        jLabel2 = new javax.swing.JLabel();
        coverageWeights = new javax.swing.JPanel();
        bronzeSpinner = new DoubleSpinner(-1000.0, 1000.0, -0.1);
        jLabel11 = new javax.swing.JLabel();
        silverSpinner = new DoubleSpinner(-1000.0, 1000.0, -0.1);
        jLabel10 = new javax.swing.JLabel();
        goldSpinner = new DoubleSpinner(-1000.0, 1000.0, -0.1);
        jLabel9 = new javax.swing.JLabel();
        stopConditions = new javax.swing.JPanel();
        iterationsLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        timeSpinner = new javax.swing.JSpinner();
        iterationsSpinner = new javax.swing.JSpinner();
        showHideButton = new javax.swing.JButton();
        developersSelection = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        unavailableList = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        availableList = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        addSelectedButton = new javax.swing.JButton();
        addAllButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        labelLoading = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        combineButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Collaborative Merge");

        mergeRequirements.setBorder(javax.swing.BorderFactory.createTitledBorder("Merge Requirements"));

        devNumberSpinner.setValue(1);

        jLabel1.setText("Maximum number of developers");

        coverageSpinner.setValue(0.0);

        jLabel2.setText("Minimum coverage");

        javax.swing.GroupLayout mergeRequirementsLayout = new javax.swing.GroupLayout(mergeRequirements);
        mergeRequirements.setLayout(mergeRequirementsLayout);
        mergeRequirementsLayout.setHorizontalGroup(
            mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mergeRequirementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(44, 44, 44)
                .addGroup(mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(coverageSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(devNumberSpinner, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        mergeRequirementsLayout.setVerticalGroup(
            mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mergeRequirementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(devNumberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mergeRequirementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(coverageSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        coverageWeights.setBorder(javax.swing.BorderFactory.createTitledBorder("Configure Medal Weights"));

        bronzeSpinner.setValue(0.1);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/bronze1.png"))); // NOI18N
        jLabel11.setText("Bronze");

        silverSpinner.setValue(0.5);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/silver1.png"))); // NOI18N
        jLabel10.setText("Silver");

        goldSpinner.setValue(1.0);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/gold1.png"))); // NOI18N
        jLabel9.setText("Gold");

        javax.swing.GroupLayout coverageWeightsLayout = new javax.swing.GroupLayout(coverageWeights);
        coverageWeights.setLayout(coverageWeightsLayout);
        coverageWeightsLayout.setHorizontalGroup(
            coverageWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coverageWeightsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goldSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(silverSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bronzeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        coverageWeightsLayout.setVerticalGroup(
            coverageWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coverageWeightsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coverageWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(goldSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(silverSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(bronzeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        stopConditions.setBorder(javax.swing.BorderFactory.createTitledBorder("Stop Conditions"));

        iterationsLabel.setText("Iterations without improvements");

        timeLabel.setText("Maximum duration (seconds)");

        timeSpinner.setValue(300);

        iterationsSpinner.setValue(300);

        showHideButton.setText("Show");
        showHideButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHideButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout stopConditionsLayout = new javax.swing.GroupLayout(stopConditions);
        stopConditions.setLayout(stopConditionsLayout);
        stopConditionsLayout.setHorizontalGroup(
            stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stopConditionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showHideButton, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(stopConditionsLayout.createSequentialGroup()
                        .addGroup(stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(iterationsLabel)
                            .addComponent(timeLabel))
                        .addGap(41, 41, 41)
                        .addGroup(stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(iterationsSpinner)
                            .addComponent(timeSpinner))))
                .addContainerGap())
        );
        stopConditionsLayout.setVerticalGroup(
            stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stopConditionsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(showHideButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stopConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iterationsLabel)
                    .addComponent(iterationsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        developersSelection.setBorder(javax.swing.BorderFactory.createTitledBorder("Developers"));

        unavailableList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(unavailableList);

        jLabel3.setText("Unavailable");

        availableList.setModel(new DefaultListModel());
        availableList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(availableList);

        jLabel4.setText("Available");

        addSelectedButton.setText(">");
        addSelectedButton.setToolTipText("Add selected");
        addSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSelectedButtonActionPerformed(evt);
            }
        });

        addAllButton.setText(">>");
        addAllButton.setToolTipText("Add all");
        addAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAllButtonActionPerformed(evt);
            }
        });

        removeAllButton.setText("<<");
        removeAllButton.setToolTipText("Remove all");
        removeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllButtonActionPerformed(evt);
            }
        });

        removeSelectedButton.setText("<");
        removeSelectedButton.setToolTipText("Remove selected");
        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout developersSelectionLayout = new javax.swing.GroupLayout(developersSelection);
        developersSelection.setLayout(developersSelectionLayout);
        developersSelectionLayout.setHorizontalGroup(
            developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(developersSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(developersSelectionLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(developersSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        developersSelectionLayout.setVerticalGroup(
            developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(developersSelectionLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(developersSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(developersSelectionLayout.createSequentialGroup()
                        .addComponent(addSelectedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeSelectedButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        labelLoading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/loading1.gif"))); // NOI18N
        labelLoading.setText("Loading ...");
        labelLoading.setVisible(false);

        closeButton.setText("Cancel");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        combineButton.setText("Run");
        combineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combineButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stopConditions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(coverageWeights, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(mergeRequirements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(developersSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(labelLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mergeRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(coverageWeights, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stopConditions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(developersSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelLoading))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        Runnable r = () -> {
            labelLoading.setVisible(true);
            resetButton.setEnabled(false);
            rGenerator.reset();
            resetButton.setEnabled(true);
            labelLoading.setVisible(false);
            close();
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void combineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combineButtonActionPerformed
        this.extendAndShowCoverage();
    }//GEN-LAST:event_combineButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void showHideButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHideButtonActionPerformed
        // TODO add your handling code here:
        boolean visible = timeSpinner.isVisible();
        timeSpinner.setVisible(!visible);
        timeLabel.setVisible(!visible);
        iterationsSpinner.setVisible(!visible);
        iterationsLabel.setVisible(!visible);
        showHideButton.setText(visible? "Show" : "Hide");
        
    }//GEN-LAST:event_showHideButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        this.move(availableList, availableIndexes, unavailableList, unavailableIndexes);
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        int end = availableList.getModel().getSize() - 1;
        if (end >= 0) {
            availableList.setSelectionInterval(0, end);
            this.move(availableList, availableIndexes, unavailableList, unavailableIndexes);
        }
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void addSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSelectedButtonActionPerformed
        this.move(unavailableList, unavailableIndexes, availableList, availableIndexes);
    }//GEN-LAST:event_addSelectedButtonActionPerformed

    private void addAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAllButtonActionPerformed
        int end = unavailableList.getModel().getSize() - 1;
        if (end >= 0) {
            unavailableList.setSelectionInterval(0, end);
            this.move(unavailableList, unavailableIndexes, availableList, availableIndexes);
        }
    }//GEN-LAST:event_addAllButtonActionPerformed

    private void move(JList<String> source, List<Integer> sourceList, JList<String> target, List<Integer> targetList) {
        DefaultListModel sourceModel = (DefaultListModel) source.getModel();
        DefaultListModel targetModel = (DefaultListModel) target.getModel();
        
        int count = source.getSelectedIndices().length;
        for (int i = 0; i < count; i++) {
            int index = source.getSelectedIndex();
            targetModel.addElement(sourceModel.getElementAt(index));
            targetList.add(sourceList.get(index));
            sourceModel.removeElementAt(index);
            sourceList.remove(index);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAllButton;
    private javax.swing.JButton addSelectedButton;
    private javax.swing.JList<String> availableList;
    private javax.swing.JSpinner bronzeSpinner;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton combineButton;
    private javax.swing.JSpinner coverageSpinner;
    private javax.swing.JPanel coverageWeights;
    private javax.swing.JSpinner devNumberSpinner;
    private javax.swing.JPanel developersSelection;
    private javax.swing.JSpinner goldSpinner;
    private javax.swing.JLabel iterationsLabel;
    private javax.swing.JSpinner iterationsSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelLoading;
    private javax.swing.JPanel mergeRequirements;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton showHideButton;
    private javax.swing.JSpinner silverSpinner;
    private javax.swing.JPanel stopConditions;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSpinner timeSpinner;
    private javax.swing.JList<String> unavailableList;
    // End of variables declaration//GEN-END:variables
   
}
