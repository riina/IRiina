package net.cyriaca.riina.misc.iriina.generic.ui.item;

import net.cyriaca.riina.misc.iriina.generic.FloatBoundedValue;
import net.cyriaca.riina.misc.iriina.generic.FloatBounds;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ArxTitledFloatItem extends ArxTitledGenericItem implements ActionListener, FocusListener, ChangeListener {

    private static final int FLOAT_PRECISION = 1000;
    private JFormattedTextField field = null;
    private NumberFormat format = null;
    private FloatBoundedValue boundedValue = null;
    private JSlider slider = null;

    private List<ActionListener> actionListeners = null;
    private List<ChangeListener> changeListeners = null;

    public ArxTitledFloatItem(FloatBounds bounds, float initValue) {
        super();
        actionListeners = new ArrayList<>();
        changeListeners = new ArrayList<>();
        boundedValue = new FloatBoundedValue(bounds, initValue);
        format = NumberFormat.getInstance();
        field = new JFormattedTextField(format);
        field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getPreferredSize().height));
        slider = new JSlider();
        add(field);
        add(slider);
        field.addActionListener(this);
        field.addFocusListener(this);
        slider.addChangeListener(this);
        setValue(initValue);
    }

    public ArxTitledFloatItem(FloatBounds bounds) {
        this(bounds, (bounds.getLowerLimit() + bounds.getUpperLimit()) / 2.0f);
    }

    public FloatBounds getValueBounds() {
        return boundedValue.getBounds();
    }

    public void setValueBounds(FloatBounds bounds) {
        boundedValue.setBounds(bounds);
        setValue(boundedValue.getValue());
    }

    public float getValue() {
        return boundedValue.getValue();
    }

    public void setValue(float value) {
        field.removeActionListener(this);
        slider.removeChangeListener(this);
        boundedValue.setValue(value);
        field.setValue(boundedValue.getValue());
        slider.setMinimum((int) (Math.floor(boundedValue.getLowerLimit()) * FLOAT_PRECISION));
        slider.setMaximum((int) (Math.ceil(boundedValue.getUpperLimit()) * FLOAT_PRECISION));
        slider.setValue((int) (boundedValue.getValue() * FLOAT_PRECISION));
        slider.repaint();
        field.addActionListener(this);
        slider.addChangeListener(this);
    }

    public void addActionListener(ActionListener l) {
        actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        actionListeners.remove(l);
    }

    public void addChangeListener(ChangeListener l) {
        changeListeners.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(l);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == field) {
            setValue(Float.parseFloat(field.getValue().toString()));
            for (ActionListener l : actionListeners)
                l.actionPerformed(e);
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            setValue(((float) slider.getValue()) / FLOAT_PRECISION);
            for (ChangeListener l : changeListeners)
                l.stateChanged(e);
        }
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEnabled(value);
        slider.setEnabled(value);
    }

    public void focusGained(FocusEvent e) {

    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == field) {
            setValue(Float.parseFloat(field.getValue().toString()));
            for (ActionListener l : actionListeners)
                l.actionPerformed(new ActionEvent(field, 0, ""));
        }
    }
}
