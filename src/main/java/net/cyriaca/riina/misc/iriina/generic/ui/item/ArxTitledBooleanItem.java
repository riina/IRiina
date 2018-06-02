package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;
import java.awt.*;

public class ArxTitledBooleanItem extends ArxTitledGenericItem {

    private JPanel checkboxHolder;
    private FlowLayout checkboxHolderLayout;
    private JCheckBox checkbox;

    private JLabel label;

    public ArxTitledBooleanItem() {
        super();
        checkboxHolderLayout = new FlowLayout(FlowLayout.LEFT);
        checkboxHolder = new JPanel(checkboxHolderLayout);
        checkbox = new JCheckBox();
        checkboxHolder.add(checkbox);
        label = new JLabel();
        checkboxHolder.add(label);
        add(checkboxHolder);
    }

    public void setTitle(String title) {
        super.setTitle(title);
        label.setText(title);
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        checkbox.setEnabled(value);
    }

    public boolean getSelected() {
        return checkbox.isSelected();
    }

    public void setSelected(boolean value) {
        checkbox.setSelected(value);
    }

}
