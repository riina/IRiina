package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledStringItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;

public class GenericStringPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;
    private ArxTitledStringItem stringItem;

    public GenericStringPropertyPanel() {
        title = new JLabel("String");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        stringItem = new ArxTitledStringItem();
        add(stringItem, BorderLayout.CENTER);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));
            stringItem.setValue(property.getString());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.STRING;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            property.setString(stringItem.getValue());
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
            stringItem.setValue(property.getString());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
