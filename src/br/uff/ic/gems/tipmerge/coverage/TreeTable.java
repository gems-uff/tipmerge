/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.coverage;

import br.uff.ic.gems.tipmerge.gui.JTableRenderer;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author jjcfigueiredo
 */
public class TreeTable {

    private DefaultTreeTableModel model = new DefaultTreeTableModel();

    private String[] headings = {"Position/Committer", "Gold", "Silver", "Bronze"};
    private Node devNode;
    //private DefaultTreeTableModel model;
    private JXTreeTable treeTable;
    private List<Object[]> content;

    public TreeTable(List<Object[]> content) {
        this.content = content;
    }

    public JXTreeTable getTreeTable() {
        devNode = new RootNode("Root");

        //Node catNode = new RootNode("");
        ChildNode childNode = null;

        for (Object[] data : this.content) {

            ChildNode actualNode = new ChildNode(data);

            switch (data.length) {
                case 5:
                    Object[] values = new Object[]{data[0] + ". " + data[1], data[2], data[3], data[4]};
                    actualNode = new ChildNode(values);
                    devNode.add(actualNode);
                    childNode = actualNode;
                    break;
                default:
                    childNode.add(actualNode);
                    break;
            }
        }

        model = new DefaultTreeTableModel(devNode, Arrays.asList(headings));

        treeTable = new JXTreeTable(model)
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            @Override
            public Class getColumnClass(int column){

                if(column == 1 || column == 2 || column == 3){
                    
                    /*
                    for(int i = 0 ; i < getRowCount() ; i++){
                        if((getValueAt(i, 1) instanceof Integer) && (getValueAt(i, 2) instanceof Integer) && (getValueAt(i, 3) instanceof Integer)){
                            getColumnModel().getColumn(column).setCellRenderer(new TreeTableRenderer());
                            //return Integer.class;
                        }
                    }
                    */
                    if(getRowCount() == devNode.getChildCount())
                        return Integer.class;
                    
                    return ImageIcon.class;
                }
                return super.getColumnClass(column);
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
