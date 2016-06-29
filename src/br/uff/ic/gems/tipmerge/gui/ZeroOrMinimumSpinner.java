/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZeroOrMinimumSpinner extends JSpinner {

    private static final long serialVersionUID = 1L;

    private SpinnerNumberModel model;
    private int stepMinimum;

    public ZeroOrMinimumSpinner() {
        super();
        // Model setup
        model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        this.setModel(model);
        this.stepMinimum = 2;
    }

    public Object getNextValue() {
        int value = (Integer) getValue();
        if (value == 0) {
            return this.getStepMinimum();
        }
        return super.getNextValue();
    }
    
    public Object getPreviousValue() {
        int value = (Integer) getValue();
        if (value == this.getStepMinimum()) {
            return 0;
        }
        return super.getPreviousValue();
    }
    
    public int getStepMinimum() {
        return stepMinimum;
    }

    public void setStepMinimum(int stepMinimum) {
        this.stepMinimum = stepMinimum;
    }

   
}