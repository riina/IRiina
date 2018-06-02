package net.cyriaca.riina.misc.iriina.generic.ui.item;

import net.cyriaca.riina.misc.iriina.generic.FileDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ArxTitledFolderSelectorItem extends ArxTitledGenericItem implements ActionListener {

    private JTextField field;
    private JPanel buttonHolder;
    private FlowLayout buttonHolderLayout;
    private JButton button;

    public ArxTitledFolderSelectorItem() {
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

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEnabled(value);
        button.setEnabled(value);
    }

    public int getFieldCols() {
        return field.getColumns();
    }

    public void setFieldCols(int cols) {
        field.setColumns(cols);
    }

    public String getDirectory() {
        return field.getText();
    }

    public void setDirectory(String directory) {
        field.setText(directory);
    }

    public String getButtonText() {
        return button.getText();
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            File f = FileDialogs.folderDialog(null, new File(field.getText()));
            if (f != null)
                field.setText(f.getAbsolutePath());
        }
    }

}
