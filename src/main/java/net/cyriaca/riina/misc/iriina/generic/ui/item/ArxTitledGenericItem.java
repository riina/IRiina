package net.cyriaca.riina.misc.iriina.generic.ui.item;

import javax.swing.*;
import java.awt.*;

public abstract class ArxTitledGenericItem extends JPanel {

    private static final int FONT_SIZE = 18;

    private BoxLayout layout;
    private JPanel labelHolder;
    private FlowLayout labelHolderLayout;
    private JLabel label;

    private boolean modEnabled = false;

    public ArxTitledGenericItem() {
        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        labelHolderLayout = new FlowLayout(FlowLayout.LEFT);
        labelHolder = new JPanel(labelHolderLayout);
        label = new JLabel();
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
        labelHolder.add(label);
        add(labelHolder);
    }

    public String getTitle() {
        return label.getText();
    }

    public void setTitle(String title) {
        label.setText(title);
    }

    protected void internalSetModEnabled(boolean value) {
        modEnabled = value;
    }

    public boolean getModEnabled() {
        return modEnabled;
    }

    public abstract void setModEnabled(boolean value);

}
