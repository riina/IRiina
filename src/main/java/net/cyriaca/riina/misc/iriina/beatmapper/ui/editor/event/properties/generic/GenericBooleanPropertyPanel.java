package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledBooleanItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;

public class GenericBooleanPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;
    private ArxTitledBooleanItem booleanItem;

    public GenericBooleanPropertyPanel() {
        title = new JLabel("Boolean");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        booleanItem = new ArxTitledBooleanItem();
        add(booleanItem, BorderLayout.CENTER);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));
            booleanItem.setSelected(property.getBoolean());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.BOOLEAN;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            property.setBoolean(booleanItem.getSelected());
        }
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
            booleanItem.setSelected(property.getBoolean());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
