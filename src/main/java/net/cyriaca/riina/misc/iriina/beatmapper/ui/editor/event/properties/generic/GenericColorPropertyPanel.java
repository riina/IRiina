package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.generic;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.generic.FloatBounds;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFloatItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenericColorPropertyPanel extends EventPropertyPanel implements ActionListener, ChangeListener {

    private static final String KEY_PANEL_GENERIC_COLOR_RED = "panel_generic_color_red";
    private static final String KEY_PANEL_GENERIC_COLOR_GREEN = "panel_generic_color_green";
    private static final String KEY_PANEL_GENERIC_COLOR_BLUE = "panel_generic_color_blue";

    private Locale l;
    private JLabel title;
    private ArxTitledFloatItem redItem;
    private ArxTitledFloatItem greenItem;
    private ArxTitledFloatItem blueItem;

    private ColorPanel colorPanel;

    private float[] components;

    public GenericColorPropertyPanel() {
        title = new JLabel("Color");
        l = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        components = new float[3];

        colorPanel = new ColorPanel();

        JPanel oatsmonIsCool = new JPanel();
        BoxLayout boxLayout = new BoxLayout(oatsmonIsCool, BoxLayout.Y_AXIS);
        oatsmonIsCool.setLayout(boxLayout);
        FloatBounds colorBounds = new FloatBounds(0.0f, false, 1.0f, false);
        redItem = new ArxTitledFloatItem(colorBounds);
        redItem.setTitle("RedColor");
        greenItem = new ArxTitledFloatItem(colorBounds);
        greenItem.setTitle("GreenColor");
        blueItem = new ArxTitledFloatItem(colorBounds);
        blueItem.setTitle("BlueColor");
        oatsmonIsCool.add(colorPanel);
        oatsmonIsCool.add(redItem);
        oatsmonIsCool.add(greenItem);
        oatsmonIsCool.add(blueItem);
        updateColor();

        add(oatsmonIsCool, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        pullColor();
    }

    public void stateChanged(ChangeEvent e) {
        pullColor();
    }

    private void pullColor() {
        components[0] = redItem.getValue();
        components[1] = greenItem.getValue();
        components[2] = blueItem.getValue();
        colorPanel.setColor(getColor());
    }

    private void updateColor() {
        redItem.setValue(components[0]);
        greenItem.setValue(components[1]);
        blueItem.setValue(components[2]);
        colorPanel.setColor(getColor());
    }

    private Color getColor() {
        return new Color(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), components, 1.0f);
    }

    private void setColor(Color color) {
        color.getRGBColorComponents(components);
        updateColor();
    }

    public void internalSetEventProperty(EventProperty oldProperty, EventProperty property) {
        if (oldProperty != null) {
            oldProperty.removeModListener(this);
            redItem.removeActionListener(this);
            redItem.removeChangeListener(this);
            greenItem.removeActionListener(this);
            greenItem.removeChangeListener(this);
            blueItem.removeActionListener(this);
            blueItem.removeChangeListener(this);
        }
        if (property != null) {
            property.addModListener(this);

            redItem.addActionListener(this);
            redItem.addChangeListener(this);
            greenItem.addActionListener(this);
            greenItem.addChangeListener(this);
            blueItem.addActionListener(this);
            blueItem.addChangeListener(this);

            title.setText(l.getKey(property.getHrNameKey()));
            redItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_RED));
            greenItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_GREEN));
            blueItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_BLUE));
            setColor(property.getColor());

            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public EventProperty.Type getType() {
        return EventProperty.Type.COLOR;
    }

    public void writeValues() {
        EventProperty property = getEventProperty();
        if (property != null) {
            pullColor();
            property.setColor(getColor());
        }
    }

    public void localize(Locale l) {
        this.l = l;
        EventProperty property = getEventProperty();
        if (property != null) {
            title.setText(l.getKey(property.getHrNameKey()));
            redItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_RED));
            greenItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_GREEN));
            blueItem.setTitle(l.getKey(KEY_PANEL_GENERIC_COLOR_BLUE));
        }
    }

    public void update() {
        EventProperty property = getEventProperty();
        if (property != null) {
            setColor(property.getColor());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    class ColorPanel extends JPanel {

        private Color color = Color.RED;

        public ColorPanel() {
            setMinimumSize(new Dimension(30, 30));
        }

        public void paintComponent(Graphics g) {
            setBackground(color);
            super.paintComponent(g);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

    }
}
