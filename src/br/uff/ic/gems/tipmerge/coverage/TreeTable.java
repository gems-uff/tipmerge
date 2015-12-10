/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.coverage;

import java.util.Arrays;
import java.util.List;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author jjcfigueiredo
 */
public class TreeTable {

    private String[] headings = {"Position/Committer", "Gold", "Silver", "Bronze"};
    private Node devNode;
    private DefaultTreeTableModel model;
    private JXTreeTable table;
    private List<String[]> content;

    public TreeTable(List<String[]> content) {
        this.content = content;
    }

    public JXTreeTable getTreeTable() {
        devNode = new RootNode("Root");

        //Node catNode = new RootNode("");

        ChildNode childNode = null;

        for (String[] data : this.content) {
            ChildNode actualNode = new ChildNode(data);
            switch (data.length) {
                case 5:
                    String[] values = new String[]{data[0] + ": " + data[1],data[2],data[3],data[4]};
                    actualNode = new ChildNode(values);
                    devNode.add(actualNode);
                    childNode = actualNode;
                    //catNode = actualNode;
                    break;
                /*
                case 2:
                    catNode.add(actualNode);
                    childNode = actualNode;
                    break;
                */
                default:
                    childNode.add(actualNode);
                    break;
            }
        }

        model = new DefaultTreeTableModel(devNode, Arrays.asList(headings));
        table = new JXTreeTable(model);
        table.setShowGrid(true, true);
        table.setColumnControlVisible(true);
        table.packAll();

        return table;
    }

}
