package net.cyriaca.riina.misc.iriina.generic.ui.item;

import net.cyriaca.riina.misc.iriina.generic.FileDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ArxTitledFileSelectorItem extends ArxTitledGenericItem implements ActionListener {

    private JTextField field;
    private JPanel buttonHolder;
    private FlowLayout buttonHolderLayout;
    private JButton button;

    public ArxTitledFileSelectorItem() {
        super();
        field = new JTextField(null, 0);
        field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getPreferredSize().height));
        add(field);
        buttonHolderLayout = new FlowLayout(FlowLayout.LEFT);
        buttonHolder = new JPanel(buttonHolderLayout);
        button = new JButton();
        buttonHolder.add(button);
        add(buttonHolder);
        button.addActionListener(this);
    }

    public int getFieldCols() {
        return field.getColumns();
    }

    public void setFieldCols(int cols) {
        field.setColumns(cols);
    }

    public String getFile() {
        return field.getText();
    }

    public void setFile(String file) {
        field.setText(file);
    }

    public String getButtonText() {
        return button.getText();
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            File f = FileDialogs.fileDialog(null, new File(field.getText()), true);
            if (f != null)
                field.setText(f.getAbsolutePath());
        }
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEditable(value);
        button.setEnabled(value);
    }
}
