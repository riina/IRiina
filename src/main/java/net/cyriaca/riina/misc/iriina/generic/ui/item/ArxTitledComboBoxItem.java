package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArxTitledComboBoxItem<E> extends ArxTitledGenericItem implements ActionListener {

    private JPanel comboboxHolder;
    private FlowLayout comboboxHolderLayout;
    private JComboBox<E> combobox;
    private List<ActionListener> listeners;

    public ArxTitledComboBoxItem() {
        super();
        comboboxHolderLayout = new FlowLayout(FlowLayout.LEFT);
        comboboxHolder = new JPanel(comboboxHolderLayout);
        combobox = new JComboBox<>();
        comboboxHolder.add(combobox);
        add(comboboxHolder);
        listeners = new ArrayList<>();
        combobox.addActionListener(this);
    }

    public ArxTitledComboBoxItem(Collection<E> initElements, E initSelection) {
        super();
        comboboxHolderLayout = new FlowLayout(FlowLayout.LEFT);
        comboboxHolder = new JPanel(comboboxHolderLayout);
        combobox = new JComboBox<>();
        comboboxHolder.add(combobox);
        add(comboboxHolder);
        listeners = new ArrayList<>();
        for (E initElement : initElements) addItem(initElement);
        combobox.setSelectedItem(initSelection);
        combobox.addActionListener(this);
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        combobox.setEnabled(value);
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }

    public void addItem(E item) {
        combobox.removeActionListener(this);
        combobox.addItem(item);
        combobox.addActionListener(this);
    }

    public void setElements(Collection<E> elements) {
        combobox.removeActionListener(this);
        combobox.removeAllItems();
        for (E element : elements) combobox.addItem(element);
        combobox.addActionListener(this);
    }

    public void clearElements() {
        combobox.removeActionListener(this);
        combobox.removeAllItems();
        combobox.addActionListener(this);
    }

    public void setRenderer(ListCellRenderer<? super E> renderer) {
        combobox.setRenderer(renderer);
    }

    public E getItemAt(int index) {
        return combobox.getItemAt(index);
    }

    public int getSelectedIndex() {
        return combobox.getSelectedIndex();
    }

    public void setSelectedIndex(int index) {
        combobox.removeActionListener(this);
        if (index >= 0 && index < combobox.getItemCount())
            combobox.setSelectedIndex(index);
        combobox.addActionListener(this);
        combobox.getSelectedIndex();
    }

    public E getSelectedItem() {
        return combobox.getItemAt(combobox.getSelectedIndex());
    }

    public void setSelectedItem(E item) {
        combobox.removeActionListener(this);
        combobox.setSelectedItem(item);
        combobox.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == combobox) {
            e.setSource(this);
            for (ActionListener l : listeners)
                l.actionPerformed(e);
        }
    }

}
