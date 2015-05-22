/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import java.awt.Component;
import javax.swing.ImageIcon;
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
 
        if(value instanceof JLabel){
           //This time return only the JLabel without icon
            return (JLabel)value;
        }
 
        else
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
     }
    protected void setValue(Object value){
       if(value instanceof ImageIcon){
          if(value != null){
             ImageIcon imageIcon = (ImageIcon) value;
             setIcon(imageIcon);
          }
          else{
             setText("");
             setIcon(null);
          }
       }
       else{
           super.setValue(value);
       }
    }
}
