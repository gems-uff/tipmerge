/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.model.SortByCommit;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.experiment.Experiment;
import br.uff.ic.gems.tipmerge.experiment.Git;
import br.uff.ic.gems.tipmerge.experiment.RevisionAnalyzer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

/**
 * This class is in charge of showing all basic information about the repository
 * project selected and direct to other possible analyzes
 *
 * @author j2cf, Catarina
 */
public class JFrameMainWindow extends javax.swing.JFrame {

    Repository repository;

    /**
     * Creates new form JFrameMainWindows
     */
    public JFrameMainWindow() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        btSelectProject = new javax.swing.JButton();
        jtProjectName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txLast = new javax.swing.JTextField();
        btShow = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txTotalAuthors = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        InternalFrame = new javax.swing.JInternalFrame();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        menuGeneral = new javax.swing.JMenuItem();
        menuCommit = new javax.swing.JMenuItem();
        menuFile = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        menuAssign = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TIPMerge - Tool to assIgn develoPers to Merge on Git");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(200, 200));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setToolTipText("");

        btSelectProject.setText("Source...");
        btSelectProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectProjectActionPerformed(evt);
            }
        });

        jtProjectName.setEditable(false);

        jLabel2.setText("Select a project:");

        jLabel4.setText("Last Commit");

        txLast.setEditable(false);

        btShow.setText("Show Details");
        btShow.setEnabled(false);
        btShow.setMaximumSize(new java.awt.Dimension(140, 29));
        btShow.setMinimumSize(new java.awt.Dimension(140, 29));
        btShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btShowActionPerformed(evt);
            }
        });

        jLabel6.setText("Committers");

        txTotalAuthors.setEditable(false);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/loading1.gif"))); // NOI18N
        jLabel8.setText("Loading ...");
        jLabel8.setVisible(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txLast)
                                    .addComponent(txTotalAuthors)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addComponent(btShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jtProjectName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btSelectProject)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSelectProject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txTotalAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(btShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );

        jPanel1.setAutoscrolls(true);

        InternalFrame.setTitle("Committers Names");
        InternalFrame.setAutoscrolls(true);
        InternalFrame.setVisible(true);

        javax.swing.GroupLayout InternalFrameLayout = new javax.swing.GroupLayout(InternalFrame.getContentPane());
        InternalFrame.getContentPane().setLayout(InternalFrameLayout);
        InternalFrameLayout.setHorizontalGroup(
            InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );
        InternalFrameLayout.setVerticalGroup(
            InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(InternalFrame)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(InternalFrame)
        );

        try {
            InternalFrame.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jMenu2.setText("Analysis");

        menuGeneral.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK));
        menuGeneral.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/general.png"))); // NOI18N
        menuGeneral.setText("General");
        menuGeneral.setEnabled(false);
        menuGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGeneralActionPerformed(evt);
            }
        });
        jMenu2.add(menuGeneral);

        menuCommit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK));
        menuCommit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/commits.png"))); // NOI18N
        menuCommit.setText("Commits");
        menuCommit.setEnabled(false);
        menuCommit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCommitActionPerformed(evt);
            }
        });
        jMenu2.add(menuCommit);

        menuFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK));
        menuFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/search99.png"))); // NOI18N
        menuFile.setText("Files");
        menuFile.setEnabled(false);
        menuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileActionPerformed(evt);
            }
        });
        jMenu2.add(menuFile);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Recommendation");

        menuAssign.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK));
        menuAssign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/assign.png"))); // NOI18N
        menuAssign.setText("Get a Ranking");
        menuAssign.setEnabled(false);
        menuAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAssignActionPerformed(evt);
            }
        });
        jMenu4.add(menuAssign);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("Help");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/help.png"))); // NOI18N
        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem2.setText("Experimentation");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Merge Authors");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	//Allows user to select a project in a directory and show basics information (total merges, total branches, last commit, ...)
    private void btSelectProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectProjectActionPerformed
        clearAllFields();

        JFileChooser projetctFile = new JFileChooser(new File("."));
        projetctFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = projetctFile.showDialog(this.getParent(), "Select");
        Runnable r = () -> {
            jLabel8.setVisible(true);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                jtProjectName.setText(projetctFile.getSelectedFile().toString());
                RevisionAnalyzer.gitReset(projetctFile.getSelectedFile().toString());

                try {

                    repository = new RepositoryDao(projetctFile.getSelectedFile()).getRepository();
                    txLast.setText(repository.getLastCommit().format(
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US)));

                    txTotalAuthors.setText(String.valueOf(repository.getCommitters().size()));

                    btShow.setEnabled(true);
                    menuCommit.setEnabled(true);
                    menuFile.setEnabled(true);
                    menuAssign.setEnabled(true);
                    menuGeneral.setEnabled(true);

                    clearAllFields();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Please, select a git project folder.",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
            jLabel8.setVisible(false);
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btSelectProjectActionPerformed

    static class MyChartPanel extends PartPanel implements ChangeListener {

        private static final long serialVersionUID = 1L;
        JScrollBar scroller;
        SlidingCategoryDataset dataset;

        private static JFreeChart createChart(CategoryDataset dataset) {
            JFreeChart chart;
            CategoryPlot plot;
            CategoryAxis categoryAxis;
            CategoryAxis domainAxis;
            BarRenderer renderer;
            if (dataset.getColumnCount() < 15){
                chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, true, false);
                categoryAxis = new CategoryAxis("");
                ValueAxis valueAxis = new NumberAxis("");
                plot = new CategoryPlot(dataset,categoryAxis,valueAxis, new LayeredBarRenderer());
                    plot.setOrientation(PlotOrientation.HORIZONTAL);
                LayeredBarRenderer render= (LayeredBarRenderer) plot.getRenderer();
                //SeriesBarWidth define o tamanho da barra
                    render.setSeriesBarWidth(0,0.3);
                    render.setItemMargin(0.02);  
                    domainAxis = plot.getDomainAxis();     
                    domainAxis.setMaximumCategoryLabelWidthRatio(0.8F);
                    domainAxis.setLowerMargin(0.02D);
                    domainAxis.setUpperMargin(0.02D);
            renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
            GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
            renderer.setSeriesPaint(0, gradientpaint);
            renderer.setPlot(plot);
            chart.getCategoryPlot().setRenderer(renderer);
             return chart;
            }
            else{
            chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, true, false);
            plot = (CategoryPlot) chart.getPlot();
            domainAxis = plot.getDomainAxis();
            domainAxis.setMaximumCategoryLabelWidthRatio(0.8F);
            domainAxis.setLowerMargin(0.02D);
            domainAxis.setUpperMargin(0.02D);
            NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
            valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
            GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
            renderer.setSeriesPaint(0, gradientpaint);
             return chart;
            }
        }

        public void stateChanged(ChangeEvent changeevent) {
            //SlidingCategoryDataset
            //A {@link CategoryDataset} implementation that presents a subset of the
            // categories in an underlying dataset.  The index of the first "visible"
            // category can be modified, which provides a means of "sliding" through
            // the categories in the underlying dataset.
            dataset.setFirstCategoryIndex(scroller.getValue());
        }

        public MyChartPanel(CategoryDataset dateRepository) {
            super(new BorderLayout());
            CategoryDataset categoryData = dateRepository;
            int sizeTab = categoryData.getColumnCount();
            dataset = new SlidingCategoryDataset(categoryData, 0, 15);
            JFreeChart jfreechart = createChart(dataset);
            addChart(jfreechart);
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            if (sizeTab < 15) {
                sizeTab = 15;
            }
            scroller = new JScrollBar(1, 0, 15, 0, sizeTab);
            add(chartpanel);
            scroller.getModel().addChangeListener(this);
            JPanel jpanel = new JPanel(new BorderLayout());
            jpanel.add(scroller);
            jpanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            jpanel.setBackground(Color.white);
            add(jpanel, "East");
        }
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            Committer[] committers = new Committer[repository.getCommitters().size()];
        List<Committer> committer = new ArrayList<>();
//                for (int i =0 ; i< repository.getCommitters().size() ; i++)
//			committers[i]= repository.getCommitters().get(i);     
        for (int i = 0; i < repository.getCommitters().size(); i++) {
            committer.add(repository.getCommitters().get(i));
        }

//		Arrays.sort(committers);   
        Collections.sort(committer, new SortByCommit());

        for (Committer cmtr : committer) {
            dataset.addValue((double) cmtr.getCommits(), "", cmtr.getName());
        }
        return dataset;
    }//shows the committers name information and the Jtree with the merge branch information 
    private void btShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btShowActionPerformed

        RepositoryDao rdao = new RepositoryDao(this.repository.getProject());
        rdao.setDetails(this.repository);

        updateJTree();
        InternalFrame.setContentPane(new MyChartPanel(createDataset()));
    }//GEN-LAST:event_btShowActionPerformed
    //Directs for the commits analysis
    private void menuCommitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCommitActionPerformed
        JFrameCommitsAnalysis commits = new JFrameCommitsAnalysis(this.repository);
        commits.setLocationRelativeTo(this.getFocusOwner());
        commits.setVisible(true);
    }//GEN-LAST:event_menuCommitActionPerformed
    //Directs for the files analysis
    private void menuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileActionPerformed
        JFrameFilesAnalysis files = new JFrameFilesAnalysis(repository);
        files.setLocationRelativeTo(this.getFocusOwner());
        files.setVisible(true);
    }//GEN-LAST:event_menuFileActionPerformed

    private void menuAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAssignActionPerformed
        JFrameAssign assign = new JFrameAssign(repository);
        assign.setLocationRelativeTo(this.getFocusOwner());
        assign.setVisible(true);

        /*
		 JFrameRankingCoverage jfAssignMerge = new JFrameRankingCoverage(repository);
		 jfAssignMerge.setLocationRelativeTo(this.getFocusOwner());
		 jfAssignMerge.setVisible(true);
         */
    }//GEN-LAST:event_menuAssignActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            // TODO add your handling code here:
            
            Experiment experimentMerges = new Experiment(repository);
            
            Map<String, Integer> result = experimentMerges.getDatasFromMerges(repository.getProject());
            //  System.out.println(result.toString());
            //result.put("Merges", 0);
            System.out.println("Number of merges with Enough developers " + result.get("Merges"));
            System.out.println("Number of merges with changed files in both branches " + result.get("Files"));
            System.out.println("Number of merges with dependencies across branches " + result.get("Dependencies"));
            System.out.println("Number of merges with conflicts " + result.get("Conflicts"));
            int rankingTotal = result.get("1stPosition") + result.get("2ndPosition") + result.get("3thPosition") + result.get("isInRank") + result.get("outOfRank");
            System.out.println("Number of merges with raking " + rankingTotal);
            System.out.println("Matched with 1st Position " + result.get("1stPosition"));
            System.out.println("Matched with 2nd Position " + result.get("2ndPosition"));
            System.out.println("Matched with 3th Position " + result.get("3thPosition"));
            System.out.println("Matched with other Position " + result.get("isInRank"));
            System.out.println("Out of the Ranking " + result.get("outOfRank"));
            // merges | developers | files | dependencies | position
            // string | integer
        } catch (IOException ex) {
            Logger.getLogger(JFrameMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        Experiment experimentMerges = new Experiment(repository);

        List<String> authors = experimentMerges.getAuthorsFromMerges();

        for (String author : authors) {
            System.out.println("\t" + author);
        }

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void menuGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGeneralActionPerformed
        JFrameGeneralAnalysis gAnalysis = new JFrameGeneralAnalysis(this.repository);
        gAnalysis.setLocationRelativeTo(this.getFocusOwner());
        gAnalysis.setVisible(true);
    }//GEN-LAST:event_menuGeneralActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame InternalFrame;
    private javax.swing.JButton btSelectProject;
    private javax.swing.JButton btShow;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JTextField jtProjectName;
    private javax.swing.JMenuItem menuAssign;
    private javax.swing.JMenuItem menuCommit;
    private javax.swing.JMenuItem menuFile;
    private javax.swing.JMenuItem menuGeneral;
    private javax.swing.JTextField txLast;
    private javax.swing.JTextField txTotalAuthors;
    // End of variables declaration//GEN-END:variables

    private void clearAllFields() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(root));
        jScrollPane1.setViewportView(jTree1);

    }

    private void updateJTree() {
        jTree1 = new JTreeRepository(this.repository);
        jScrollPane1.setViewportView(jTree1);
    }

}
