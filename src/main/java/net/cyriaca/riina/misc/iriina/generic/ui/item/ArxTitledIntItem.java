package net.cyriaca.riina.misc.iriina.generic.ui.item;

import net.cyriaca.riina.misc.iriina.generic.IntBoundedValue;
import net.cyriaca.riina.misc.iriina.generic.IntBounds;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ArxTitledIntItem extends ArxTitledGenericItem implements ActionListener, FocusListener, ChangeListener {

    private JFormattedTextField field;
    private NumberFormat format;
    private IntBoundedValue boundedValue;
    private JSlider slider;

    private List<ActionListener> actionListeners;
    private List<ChangeListener> changeListeners;

    public ArxTitledIntItem(IntBounds bounds, int initValue) {
        super();
        actionListeners = new ArrayList<>();
        changeListeners = new ArrayList<>();
        boundedValue = new IntBoundedValue(bounds, initValue);
        format = NumberFormat.getIntegerInstance();
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

    public ArxTitledIntItem(IntBounds bounds) {
        this(bounds, (bounds.getLowerLimit() + bounds.getUpperLimit()) / 2);
    }

    public void setSliderVisibility(boolean value){
        slider.setVisible(value);
    }

    public void setFieldFocusLostBehaviour(int value){
        //field.setFocusLostBehavior(value);
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEnabled(value);
        slider.setEnabled(value);
    }

    public IntBounds getValueBounds() {
        return boundedValue.getBounds();
    }

    public void setValueBounds(IntBounds bounds) {
        boundedValue.setBounds(bounds);
        setValue(boundedValue.getValue());
    }

    public int getValue() {
        return boundedValue.getValue();
    }

    public void setValue(int value) {
        field.removeActionListener(this);
        slider.removeChangeListener(this);
        boundedValue.setValue(value);
        field.setValue(boundedValue.getValue());
        slider.setMinimum(boundedValue.getLowerLimit());
        slider.setMaximum(boundedValue.getUpperLimit());
        slider.setValue(boundedValue.getValue());
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
            setValue(Integer.parseInt(field.getValue().toString()));
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            setValue(slider.getValue());
        }
    }

    public void focusGained(FocusEvent e) {

    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == field) {
            try {
                field.commitEdit();
                setValue(Integer.parseInt(field.getValue().toString()));
            } catch (ParseException ignored) {
                setValue(boundedValue.getValue());
            }
            for (ActionListener l : actionListeners)
                l.actionPerformed(new ActionEvent(field, 0, ""));
        }
    }
}
