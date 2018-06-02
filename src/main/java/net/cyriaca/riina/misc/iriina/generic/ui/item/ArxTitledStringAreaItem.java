package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;

public class ArxTitledStringAreaItem extends ArxTitledGenericItem {

    private JTextArea textArea;

    public ArxTitledStringAreaItem() {
        textArea = new JTextArea();
        add(textArea);
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        textArea.setEnabled(value);
    }

    public int getAreaCols() {
        return textArea.getColumns();
    }

    public void setAreaCols(int cols) {
        textArea.setColumns(cols);
    }

    public String getValue() {
        return textArea.getText();
    }

    public void setValue(String value) {
        textArea.setText(value);
    }

}
