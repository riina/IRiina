package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledIntItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;

public class GenericIntPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;
    private ArxTitledIntItem intItem;

    public GenericIntPropertyPanel() {
        title = new JLabel("Int");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        intItem = new ArxTitledIntItem(null, 0);
        add(intItem, BorderLayout.CENTER);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));
            intItem.setValueBounds(property.getIntBounds());
            intItem.setValue(property.getInt());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.INT;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            property.setIntBounds(intItem.getValueBounds());
            property.setInt(intItem.getValue());
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
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}

