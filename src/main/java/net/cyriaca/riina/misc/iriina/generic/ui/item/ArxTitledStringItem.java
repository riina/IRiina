package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;
import java.awt.*;

public class ArxTitledStringItem extends ArxTitledGenericItem {

    private JTextField field;

    public ArxTitledStringItem() {
        field = new JTextField();
        field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getPreferredSize().height));
        add(field);
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEnabled(value);
    }

    public int getFieldCols() {
        return field.getColumns();
    }

    public void setFieldCols(int cols) {
        field.setColumns(cols);
    }

    public String getValue() {
        return field.getText();
    }

    public void setValue(String value) {
        field.setText(value);
    }

}
