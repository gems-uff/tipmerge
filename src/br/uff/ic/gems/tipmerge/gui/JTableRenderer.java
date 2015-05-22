/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Jhunior
 */
public class JTableRenderer extends DefaultTableCellRenderer {
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
