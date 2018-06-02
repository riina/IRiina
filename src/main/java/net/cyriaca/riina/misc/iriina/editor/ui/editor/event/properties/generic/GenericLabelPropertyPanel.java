package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;

public class GenericLabelPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;

    public GenericLabelPropertyPanel() {
        title = new JLabel("Label");
        l = null;
        title.setFont(LABEL_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.LABEL;
    }

    public void writeValues() {
    }

    public void localize(Locale l) {
        this.l = l;
        EventProperty property = getEventProperty();
        if (property != null) {
            title.setText(l.getKey(property.getHrNameKey()));
        }
    }

    public void update() {
        EventProperty property = getEventProperty();
        if (property != null) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
