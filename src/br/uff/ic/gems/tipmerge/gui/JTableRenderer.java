/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Jhunior
 */
public class JTableRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int column)
     {
   JLabel cell = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof JLabel){
           //This time return only the JLabel without icon
            return (JLabel)value;
        }
        if(value instanceof Object){
        if(column==2)
            setHorizontalAlignment(JLabel.LEFT);
        if(column==3)
            setBackground(Color.YELLOW);
            setForeground(Color.BLACK);
        if(column==4)
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
        if(column==5)
            setBackground(Color.ORANGE);
            setForeground(Color.BLACK);
            
        }
           return cell;
     }
    protected void setValue(Object value){
       setHorizontalAlignment(JLabel.CENTER);
       setBackground(Color.WHITE);
       setForeground(Color.BLACK);
       super.setValue(value);
    }
}
