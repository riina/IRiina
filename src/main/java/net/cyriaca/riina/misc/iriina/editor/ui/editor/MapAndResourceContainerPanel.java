package net.cyriaca.riina.misc.iriina.editor.ui.editor;

import javax.swing.*;
import java.awt.*;

public class MapAndResourceContainerPanel extends JPanel {

    public MapAndResourceContainerPanel(MapInfoDataPanel midp, ResourceInfoDataPanel ridp) {
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);

        add(midp);
        add(ridp);

        setPreferredSize(new Dimension(240, 800));
    }

}
