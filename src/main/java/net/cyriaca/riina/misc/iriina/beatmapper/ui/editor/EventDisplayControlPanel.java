package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventDisplayControlPanel extends JPanel implements ChangeListener, ActionListener {

    private static final String KEY_PANEL_EVENT_DISPLAY_SHOW_CONTROLS = "panel_event_display_show_controls";
    private static final String KEY_PANEL_EVENT_DISPLAY_RENDER_ARCS = "panel_event_display_render_arcs";
    private static final String KEY_PANEL_EVENT_DISPLAY_RENDER_BEATS = "panel_event_display_render_beats";
    private static final String KEY_PANEL_EVENT_DISPLAY_RENDER_EVENTS = "panel_event_display_render_events";
    private static final String KEY_PANEL_EVENT_DISPLAY_SCALE = "panel_event_display_scale";
    private static final String KEY_PANEL_EVENT_DISPLAY_REMAP_MODE = "panel_event_display_remap_mode";

    private static final String KEY_PANEL_EVENT_DISPLAY_TOGGLE_MAP_INFO_AND_RESOURCE_INFO = "panel_event_display_toggle_map_info_and_resource_info";
    private static final String KEY_PANEL_EVENT_DISPLAY_TOGGLE_EVENT_MOD = "panel_event_display_toggle_event_mod";

    private JButton showControlsButton;
    private JCheckBox renderArcBox;
    private JLabel renderArcLabel;
    private JCheckBox renderBeatBox;
    private JLabel renderBeatLabel;
    private JCheckBox renderEventBox;
    private JLabel renderEventLabel;
    private JSlider scaleSlider;
    private JLabel scaleLabel;
    private JCheckBox remapButton;
    private JLabel remapLabel;

    private JButton toggleMapInfoAndResourceInfo;
    private JButton toggleEventMod;

    private EditorFrame parent;

    public EventDisplayControlPanel(EditorFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        JPanel holder = new JPanel();

        showControlsButton = new JButton("Show controls");
        renderArcBox = new JCheckBox();
        renderArcBox.setSelected(false);
        parent.setRenderArcs(renderArcBox.isSelected());
        renderArcLabel = new JLabel("Render pulse");
        renderEventBox = new JCheckBox();
        renderEventBox.setSelected(true);
        parent.setRenderEvents(renderEventBox.isSelected());
        renderEventLabel = new JLabel("Render events");
        renderBeatBox = new JCheckBox();
        renderBeatBox.setSelected(false);
        parent.setRenderBeats(renderBeatBox.isSelected());
        renderBeatLabel = new JLabel("Render beats");
        scaleSlider = new JSlider();
        scaleSlider.setMinimum(10);
        scaleSlider.setMaximum(4000);
        scaleSlider.setValue((int) (parent.getConfig().getDisplayXScale() * 100000.0f));
        scaleLabel = new JLabel("Scale");
        remapButton = new JCheckBox();
        remapButton.setSelected(parent.isRemapMode());
        remapLabel = new JLabel("Remap mode");
        holder.add(showControlsButton);
        holder.add(renderArcBox);
        holder.add(renderArcLabel);
        holder.add(renderBeatBox);
        holder.add(renderBeatLabel);
        holder.add(renderEventBox);
        holder.add(renderEventLabel);
        holder.add(scaleSlider);
        holder.add(scaleLabel);
        holder.add(remapButton);
        holder.add(remapLabel);

        add(holder, BorderLayout.CENTER);

        toggleMapInfoAndResourceInfo = new JButton("Toggle Map / Resource Info");
        add(toggleMapInfoAndResourceInfo, BorderLayout.WEST);

        toggleEventMod = new JButton("Toggle Event Mod");
        add(toggleEventMod, BorderLayout.EAST);

        showControlsButton.addActionListener(this);
        scaleSlider.addChangeListener(this);
        renderArcBox.addActionListener(this);
        renderBeatBox.addActionListener(this);
        renderEventBox.addActionListener(this);
        remapButton.addActionListener(this);

        toggleMapInfoAndResourceInfo.addActionListener(this);
        toggleEventMod.addActionListener(this);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == scaleSlider) {
            parent.getConfig().setDisplayXScale(((float) scaleSlider.getValue() / 100000.0f));
            parent.queueRender();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showControlsButton)
            parent.showControlReference();
        if (e.getSource() == renderArcBox)
            parent.setRenderArcs(renderArcBox.isSelected());
        if (e.getSource() == renderBeatBox)
            parent.setRenderBeats(renderBeatBox.isSelected());
        if (e.getSource() == renderEventBox)
            parent.setRenderEvents(renderEventBox.isSelected());
        if (e.getSource() == toggleMapInfoAndResourceInfo)
            parent.toggleMapInfoAndResourceInfo();
        if (e.getSource() == toggleEventMod)
            parent.toggleEventMod();
        if (e.getSource() == remapButton)
            parent.setRemapMode(remapButton.isSelected());
        parent.queueRender();
    }

    public void localize(Locale l) {
        showControlsButton.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_SHOW_CONTROLS));
        renderArcLabel.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_RENDER_ARCS));
        renderBeatLabel.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_RENDER_BEATS));
        renderEventLabel.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_RENDER_EVENTS));
        scaleLabel.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_SCALE));
        toggleMapInfoAndResourceInfo.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_TOGGLE_MAP_INFO_AND_RESOURCE_INFO));
        toggleEventMod.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_TOGGLE_EVENT_MOD));
        remapLabel.setText(l.getKey(KEY_PANEL_EVENT_DISPLAY_REMAP_MODE));
    }
}
