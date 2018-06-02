package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;
import java.awt.*;

public class ArxTitledBooleanChecklistItem extends ArxTitledGenericItem {

    private JPanel[] checkboxHolders;
    private JCheckBox[] checkboxes;

    public ArxTitledBooleanChecklistItem() {
        super();
        checkboxHolders = new JPanel[0];
        checkboxes = new JCheckBox[0];
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        for (JCheckBox box : checkboxes)
            box.setEnabled(value);
    }

    public void setSelectedAll(boolean value) {
        for (JCheckBox checkboxe : checkboxes) {
            checkboxe.setSelected(value);
        }
    }

    public void setBoxes(Object[] names) {
        for (JPanel holder : checkboxHolders)
            remove(holder);
        if (names == null) {
            checkboxHolders = new JPanel[0];
            checkboxes = new JCheckBox[0];
            return;
        }
        checkboxHolders = new JPanel[names.length];
        checkboxes = new JCheckBox[names.length];
        for (int i = 0; i < names.length; i++) {
            checkboxes[i] = new JCheckBox();
            JLabel label = new JLabel(names[i].toString());
            checkboxHolders[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
            checkboxHolders[i].add(checkboxes[i]);
            checkboxHolders[i].add(label);
            add(checkboxHolders[i]);
        }
    }

    public int getLength() {
        return checkboxes.length;
    }

    public boolean getSelected(int i) {
        return checkboxes[i].isSelected();
    }

    public void setSelected(int i, boolean value) {
        checkboxes[i].setSelected(value);
    }

}
