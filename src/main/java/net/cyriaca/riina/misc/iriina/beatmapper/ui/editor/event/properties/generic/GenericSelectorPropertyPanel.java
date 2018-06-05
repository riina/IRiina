package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GenericSelectorPropertyPanel extends EventPropertyPanel {

    private Locale l;
    private JLabel title;
    private ArxTitledComboBoxItem<String> selectorItem;

    public GenericSelectorPropertyPanel() {
        title = new JLabel("Selector");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        selectorItem = new ArxTitledComboBoxItem<>();
        add(selectorItem, BorderLayout.CENTER);
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null)
            oldProperty.removeModListener(this);
        if (property != null) {
            property.addModListener(this);
            title.setText(l.getKey(property.getHrNameKey()));

            List<String> localizedStrs = new ArrayList<>();
            for (String str : property.getSelectorKeys())
                localizedStrs.add(l.getKey(str));
            selectorItem.setElements(localizedStrs);
            int index = property.getSelectorValues().indexOf(property.getSelector());
            if (index != -1)
                selectorItem.setSelectedItem(localizedStrs.get(index));

            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.SELECTOR;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            property.setSelectorByIndex(selectorItem.getSelectedIndex());
        }
    }

    public void localize(Locale l) {
        this.l = l;
        EventProperty property = getEventProperty();
        if (property != null) {
            title.setText(l.getKey(property.getHrNameKey()));

            List<String> localizedStrs = new ArrayList<>();
            for (String str : property.getSelectorKeys())
                localizedStrs.add(l.getKey(str));
            selectorItem.setElements(localizedStrs);
            int index = property.getSelectorValues().indexOf(property.getSelector());
            if (index != -1)
                selectorItem.setSelectedItem(localizedStrs.get(index));
        }
    }

    public void update() {
        EventProperty property = getEventProperty();
        if (property != null) {

            List<String> localizedStrs = new ArrayList<>();
            for (String str : property.getSelectorKeys())
                localizedStrs.add(l.getKey(str));
            selectorItem.setElements(localizedStrs);
            int index = property.getSelectorValues().indexOf(property.getSelector());
            if (index != -1)
                selectorItem.setSelectedItem(localizedStrs.get(index));

            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
