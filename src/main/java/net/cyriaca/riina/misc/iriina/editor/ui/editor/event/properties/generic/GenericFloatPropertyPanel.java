package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFloatItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;

public class GenericFloatPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;
    private ArxTitledFloatItem floatItem;

    public GenericFloatPropertyPanel() {
        title = new JLabel("Float");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        floatItem = new ArxTitledFloatItem(null, 0.0f);
        add(floatItem, BorderLayout.CENTER);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));
            floatItem.setValueBounds(property.getFloatBounds());
            floatItem.setValue(property.getFloat());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.FLOAT;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            property.setFloatBounds(floatItem.getValueBounds());
            property.setFloat(floatItem.getValue());
        }
    }

    public void localize(Locale l) {
        this.l = l;
        EventProperty property = getEventProperty();
        if (property != null) {
            title.setText(l.getKey(property.getHrNameKey()));
        }
    }

    @Override
    public void update() {
        EventProperty property = getEventProperty();
        if (property != null) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
