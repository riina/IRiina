package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;
import net.cyriaca.riina.misc.iriina.intralism.data.ResourceProperty;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ResourcePropertyPanel extends PropertyPanel {

    private Locale l;
    private JLabel title;
    private ResourceProperty resourceProperty;
    private ArxTitledComboBoxItem<MapResource> resourceSelector;
    private List<MapResource> resources;

    public ResourcePropertyPanel() {
        title = new JLabel("Resource");
        l = null;
        resourceProperty = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        resources = new ArrayList<>();

        ResourceElementRenderer rer = new ResourceElementRenderer();
        resourceSelector = new ArxTitledComboBoxItem<>();
        resourceSelector.setRenderer(rer);
        add(resourceSelector, BorderLayout.CENTER);

        setResourceProperty(null);
    }

    public void setResourceProperty(ResourceProperty resourceProperty) {
        if (this.resourceProperty != null)
            this.resourceProperty.removeModListener(this);
        this.resourceProperty = resourceProperty;
        if (resourceProperty != null) {
            resourceProperty.addModListener(this);
            title.setText(l.getKey(resourceProperty.getHrKey()));
            for (int i = 0; i < resources.size(); i++) {
                if (resources.get(i).getName().equals(resourceProperty.getResourceName())) {
                    resourceSelector.setSelectedIndex(i);
                    break;
                }
            }
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void updateResourceList(List<MapResource> resources) {
        this.resources = new ArrayList<>(resources);
        resourceSelector.setElements(this.resources);
    }

    public void writeValues() {
        if (resourceProperty != null) {
            MapResource selected = resourceSelector.getSelectedItem();
            if (selected != null)
                resourceProperty.setResourceName(selected.getName());
        }
    }

    public void localize(Locale l) {
        this.l = l;
        if (resourceProperty != null) {
            title.setText(l.getKey(resourceProperty.getHrKey()));
        }
    }

    public void update() {
        if (resourceProperty != null) {
            for (int i = 0; i < resources.size(); i++) {
                if (resources.get(i).getName().equals(resourceProperty.getResourceName())) {
                    resourceSelector.setSelectedIndex(i);
                    break;
                }
            }
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    class ResourceElementRenderer extends JLabel
            implements ListCellRenderer<MapResource> {

        private ResourceElementRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, MapResource value, int index, boolean isSelected, boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (value != null) {
                ImageIcon icon = value.getSmallIcon();
                String str = value.getName();
                setIcon(icon);
                setText(str);
            }
            setFont(list.getFont());

            return this;
        }
    }
}