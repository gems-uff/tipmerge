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

public class DoubleSpinner extends JSpinner {

    private static final long serialVersionUID = 1L;

    private SpinnerNumberModel model;

    public DoubleSpinner(double minimum, double maximum, double step) {
        super();
        // Model setup
        model = new SpinnerNumberModel(0.0, minimum, maximum, step);
        this.setModel(model);

        // Step recalculation
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = getDouble();
                // Steps are sensitive to the current magnitude of the value
                if (step == -0.1) {
                    if (value > -5.0 && value < 5.0) {
                        model.setStepSize(0.1);
                    } else {
                        model.setStepSize(1.0);
                    }
                }
            }
        });
    }

    /**
     * Returns the current value as a Double
     */
    public double getDouble() {
        return ((Double)getValue()).doubleValue();
    }

}