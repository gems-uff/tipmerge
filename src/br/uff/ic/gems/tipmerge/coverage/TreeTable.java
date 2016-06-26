/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.coverage;

import br.uff.ic.gems.tipmerge.gui.JTableRenderer;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.IconManager;
import br.uff.ic.gems.tipmerge.model.Medalist;
import br.uff.ic.gems.tipmerge.model.RankingGenerator;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author jjcfigueiredo
 */
public class TreeTable {

    private final String[] headings = {"Position/Committer", "Gold", "Silver", "Bronze"};
    private final List<Object[]> content;
    private final RankingGenerator rankGen;

    private Node devNode;
    private JXTreeTable treeTable;
    private DefaultTreeTableModel model = new DefaultTreeTableModel();

    public TreeTable(RankingGenerator rankingGen) {

        this.rankGen = rankingGen;
        List<Medalist> ranking = rankingGen.getRanking();
        this.content = new ArrayList<>();

        int position = 1;
        IconManager iconManager = new IconManager();
        for (Medalist medalist : ranking) {
            String name = medalist.getCommitter().getName();
            if (name.length() > 50) {
                name = medalist.getCommitter().getInitial();
            }
            this.content.add(new Object[]{position++ + " - "
                + name,
                iconManager.createImageIcon(medalist.getGoldMedals()),
                iconManager.createImageIcon(medalist.getSilverMedals()),
                iconManager.createImageIcon(medalist.getBronzeMedals())
            });

            Map<String, Object[]> filesList = medalist.getFilesList();

            for (String file : filesList.keySet()) {
                //int gold = filesList.get(file)[0] , silver = filesList.get(file)[1] , bronze = filesList.get(file)[2];
                this.content.add(new Object[]{file, filesList.get(file)[0], filesList.get(file)[1], filesList.get(file)[2], medalist.getCommitter().getName()});
            }

        }
    }

    public JXTreeTable getTreeTable() {
        devNode = new RootNode("Root");

        //Node catNode = new RootNode("");
        ChildNode childNode = null;

        for (Object[] data : this.content) {

            ChildNode actualNode = new ChildNode(data);

            switch (data.length) {
                case 4:
                    //Object[] values = new Object[]{data[0] + " - " + data[1], data[2], data[3], data[4]};
                    //actualNode = new ChildNode(values);
                    devNode.add(actualNode);
                    childNode = actualNode;
                    break;
                default:
                    childNode.add(actualNode);
                    break;
            }
        }

        model = new DefaultTreeTableModel(devNode, Arrays.asList(headings));

        treeTable = new JXTreeTable(model) {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            @Override
            public Class getColumnClass(int column) {

                if (column == 1 || column == 2 || column == 3) {

                    //if(getRowCount() == devNode.getChildCount())
                    //    return Integer.class;
                    return ImageIcon.class;
                }
                return super.getColumnClass(column);
            }

            private Medalist findMedalist(int row) {
                try {
                    int rowTemp = row;
                    String aut;
                    do {
                        aut = getValueAt(rowTemp--, 0).toString();
                    } while (!aut.contains(" - "));

                    return rankGen.getRanking().get(Integer.parseInt(aut.split(" - ")[0]) - 1);
                } catch (RuntimeException e1) {
                    System.err.println(e1);
                    //catch null pointer exception if mouse is over an empty line
                }
                return null;
            }


            @Override
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int row = rowAtPoint(p);
                int col = columnAtPoint(p);

                if (col == 0) {
                    Medalist medalist = this.findMedalist(row);
                    if (medalist != null) {
                        tip = "<html>" +
                            medalist.getCommitter().getNameEmail()
                                .replace("<", "&lt;")
                                .replace(">", "&gt;")
                                .replace("\n", "<br>") +
                            "</html>";
                    }
                }
                if (col == 3 && getValueAt(row, col) instanceof ImageIcon) {
                    Medalist medalist = this.findMedalist(row);
                    if (medalist != null) {
                        String file = getValueAt(row, 0).toString();
                        tip = (new ToolTipMessage()).getMessage(medalist, file);
                    }
                }

                return tip;
            }

        };

        //treeTable.getTreeTableModel().getColumnClass(0);
        treeTable.setRowHeight(30);
        treeTable.setShowGrid(true, true);
        treeTable.setColumnControlVisible(true);
        treeTable.packAll();
        setHeadings();

        return treeTable;
    }

    private void setHeadings() {
        JLabel lblGold = new JLabel("Gold");
        JLabel lblSilver = new JLabel("Silver");
        JLabel lblBronze = new JLabel("Bronze");
        lblGold.setIcon(new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/gold1.png")));
        lblSilver.setIcon(new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/silver1.png")));
        lblBronze.setIcon(new ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/bronze1.png")));

        JTableRenderer jTableRender = new JTableRenderer();
        treeTable.getColumnModel().getColumn(1).setHeaderValue(lblGold);
        treeTable.getColumnModel().getColumn(1).setHeaderRenderer(jTableRender);
        //treeTable.getColumnModel().getColumn(1).setCellRenderer(jTableRender);
        treeTable.getColumnModel().getColumn(2).setHeaderValue(lblSilver);
        treeTable.getColumnModel().getColumn(2).setHeaderRenderer(jTableRender);
        //treeTable.getColumnModel().getColumn(2).setCellRenderer(jTableRender);
        treeTable.getColumnModel().getColumn(3).setHeaderValue(lblBronze);
        treeTable.getColumnModel().getColumn(3).setHeaderRenderer(jTableRender);
        //treeTable.getColumnModel().getColumn(3).setCellRenderer(jTableRender);

        //treeTable.getModel().
    }

}
