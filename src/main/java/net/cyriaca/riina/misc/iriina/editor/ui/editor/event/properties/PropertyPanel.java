package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.swing.*;
import java.awt.*;

public abstract class PropertyPanel extends JPanel {

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);

    public static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 18);

    public abstract void writeValues();

    public abstract void localize(Locale l);

    public abstract void update();

}
