package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.EventModContainerPanel;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.*;
import net.cyriaca.riina.misc.iriina.generic.AsheTypeConvert;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class EventModPanel extends JPanel implements ActionListener {

    private static final Font EVENT_NAME_FONT = new Font("SansSerif", Font.BOLD, 14);

    private static final String KEY_PANEL_EVENT_MOD_WRITE_CHANGES = "panel_event_mod_write_changes";
    private static final String KEY_PANEL_EVENT_MOD_NO_EVENT = "panel_event_mod_no_event";
    private static final String KEY_PANEL_EVENT_MOD_LOCK_SELECTION = "panel_event_mod_lock_selection";
    private static final String KEY_PANEL_EVENT_MOD_EVENT_NAME_FORMAT = "panel_event_mod_event_name_format";
    private static final String EVENT_NAME = "%eventName%";
    private static final String EVENT_META_ID = "%eventMetaId%";
    private static final String EVENT_OFFSET = "%eventOffset%";

    private String eventNameFormat = null;
    private JLabel lockLabel;
    private JCheckBox lockBox;
    private JLabel eventNameLabel;
    private JFormattedTextField timeField;
    private MapEvent targetEvent = null;
    private ArcPropertyPanel arcPropertyPanel;
    private ResourcePropertyPanel mainResourcePropertyPanel;
    private ResourcePropertyPanel foreResourcePropertyPanel;
    private ResourcePropertyPanel backResourcePropertyPanel;
    private TimedEventPropertyPanel timedEventPropertyPanel;
    private TimingPropertyPanel timingPropertyPanel;
    private List<EventPropertyPanel> eventPropertyPanels;
    private JPanel holder;
    private JButton writeButton;
    private Locale l = null;
    private EventModContainerPanel parent;

    public EventModPanel(EventModContainerPanel parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        eventNameLabel = new JLabel("Event Name");
        eventNameLabel.setFont(EVENT_NAME_FONT);
        NumberFormat format = NumberFormat.getInstance();
        timeField = new JFormattedTextField(format);
        timeField.setColumns(6);
        lockBox = new JCheckBox();
        lockLabel = new JLabel("LockSelection");
        topPanel.add(eventNameLabel, BorderLayout.NORTH);
        topPanel.add(lockBox, BorderLayout.WEST);
        topPanel.add(lockLabel, BorderLayout.CENTER);
        topPanel.add(timeField, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        holder = new JPanel();
        BoxLayout bl = new BoxLayout(holder, BoxLayout.Y_AXIS);
        holder.setLayout(bl);
        arcPropertyPanel = new ArcPropertyPanel();
        arcPropertyPanel.setHost(parent.getEditor());
        holder.add(arcPropertyPanel);
        mainResourcePropertyPanel = new ResourcePropertyPanel();
        holder.add(mainResourcePropertyPanel);
        foreResourcePropertyPanel = new ResourcePropertyPanel();
        holder.add(foreResourcePropertyPanel);
        backResourcePropertyPanel = new ResourcePropertyPanel();
        holder.add(backResourcePropertyPanel);
        timedEventPropertyPanel = new TimedEventPropertyPanel();
        timedEventPropertyPanel.setHost(parent.getEditor());
        holder.add(timedEventPropertyPanel);
        timingPropertyPanel = new TimingPropertyPanel();
        timingPropertyPanel.setHost(parent.getEditor());
        holder.add(timingPropertyPanel);
        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(holder, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(minHolder);
        add(scrollPane, BorderLayout.CENTER);
        eventPropertyPanels = new ArrayList<>();
        writeButton = new JButton("WriteChanges");
        timeField.setValue(-1.0f);
        timeField.setEditable(false);
        writeButton.setEnabled(false);
        writeButton.addActionListener(this);
        add(writeButton, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(280, 800));
    }

    public void reevaluateTarget() {
        for (EventPropertyPanel panel : eventPropertyPanels) {
            panel.setEventProperty(null);
            holder.remove(panel);
        }
        parent.returnPanels(eventPropertyPanels);
        eventPropertyPanels.clear();
        ArcProperty arcProperty = null;
        ResourceProperty mainResourceProperty = null;
        ResourceProperty foreResourceProperty = null;
        ResourceProperty backResourceProperty = null;
        TimedEventProperty timedEventProperty = null;
        TimingProperty timingProperty = null;
        if (this.targetEvent != null) {
            List<EventProperty> props = this.targetEvent.getEventProperties();
            List<EventProperty.Type> propTypes = new ArrayList<>();
            for (EventProperty prop : props)
                propTypes.add(prop.getType());
            List<EventPropertyPanel> propPanels = parent.getPanels(propTypes);
            for (int i = 0; i < props.size(); i++) {
                EventPropertyPanel panel = propPanels.get(i);
                panel.setEventProperty(props.get(i));
                holder.add(panel);
                eventPropertyPanels.add(panel);
            }
            arcProperty = this.targetEvent.getArcProperty();
            mainResourceProperty = this.targetEvent.getMainResourceProperty();
            foreResourceProperty = this.targetEvent.getForegroundResourceProperty();
            backResourceProperty = this.targetEvent.getBackgroundResourceProperty();
            timedEventProperty = this.targetEvent.getTimedEventProperty();
            timingProperty = this.targetEvent.getTimingProperty();
            int offset = parent.getEditor().getMapData().indexOf(this.targetEvent);
            String name = l.getKey(this.targetEvent.getEventHrNameKey());
            int metaId = this.targetEvent.getMetaId();
            eventNameLabel.setText(eventNameFormat.replaceAll(EVENT_OFFSET, Integer.toString(offset)).replaceAll(EVENT_NAME, name).replaceAll(EVENT_META_ID, Integer.toString(metaId)));
            timeField.setValue(this.targetEvent.getTime());
            timeField.setEditable(true);
            writeButton.setEnabled(true);
        } else {
            eventNameLabel.setText(l.getKey(KEY_PANEL_EVENT_MOD_NO_EVENT));
            timeField.setValue(-1.0f);
            timeField.setEditable(false);
            writeButton.setEnabled(false);
        }
        arcPropertyPanel.setArcProperty(arcProperty);
        mainResourcePropertyPanel.setResourceProperty(mainResourceProperty);
        foreResourcePropertyPanel.setResourceProperty(foreResourceProperty);
        backResourcePropertyPanel.setResourceProperty(backResourceProperty);
        timedEventPropertyPanel.setTimedEventProperty(timedEventProperty);
        timingPropertyPanel.setTimingProperty(timingProperty);
    }

    public void updateResourceList(List<MapResource> resourceList) {
        mainResourcePropertyPanel.updateResourceList(resourceList);
        foreResourcePropertyPanel.updateResourceList(resourceList);
        backResourcePropertyPanel.updateResourceList(resourceList);
    }

    public MapEvent getTargetEvent() {
        return targetEvent;
    }

    public void setTargetEvent(MapEvent targetEvent) {
        if (lockBox.isSelected()) {
            if (this.targetEvent != null && !parent.getEditor().getMapData().containsEvent(this.targetEvent))
                this.targetEvent = null;
        } else {
            if (targetEvent != this.targetEvent) {
                this.targetEvent = targetEvent;
                reevaluateTarget();
            }
        }
    }

    public void setLocale(Locale l) {
        this.l = l;
        writeButton.setText(l.getKey(KEY_PANEL_EVENT_MOD_WRITE_CHANGES));
        lockLabel.setText(l.getKey(KEY_PANEL_EVENT_MOD_LOCK_SELECTION));
        eventNameFormat = l.getKey(KEY_PANEL_EVENT_MOD_EVENT_NAME_FORMAT);
        if (targetEvent == null)
            eventNameLabel.setText(l.getKey(KEY_PANEL_EVENT_MOD_NO_EVENT));
        else {
            int offset = parent.getEditor().getMapData().indexOf(this.targetEvent);
            String name = l.getKey(this.targetEvent.getEventHrNameKey());
            int metaId = this.targetEvent.getMetaId();
            eventNameLabel.setText(eventNameFormat.replaceAll(EVENT_OFFSET, Integer.toString(offset)).replaceAll(EVENT_NAME, name).replaceAll(EVENT_META_ID, Integer.toString(metaId)));
        }
        arcPropertyPanel.localize(l);
        mainResourcePropertyPanel.localize(l);
        foreResourcePropertyPanel.localize(l);
        backResourcePropertyPanel.localize(l);
        timedEventPropertyPanel.localize(l);
        timingPropertyPanel.localize(l);
        for (EventPropertyPanel panel : eventPropertyPanels)
            panel.localize(l);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == writeButton) {
            if (targetEvent != null) {
                applyChanges();
            }
        }
    }

    public void subApplyChanges() {
        if (targetEvent == null)
            return;
        String oldData = targetEvent.getEventData();
        String oldExtraData = targetEvent.getEventExtraData();
        float oldTime = targetEvent.getTime();
        arcPropertyPanel.writeValues();
        mainResourcePropertyPanel.writeValues();
        foreResourcePropertyPanel.writeValues();
        backResourcePropertyPanel.writeValues();
        timedEventPropertyPanel.writeValues();
        timingPropertyPanel.writeValues();
        for (EventPropertyPanel panel : eventPropertyPanels)
            panel.writeValues();
        TimedEventProperty tep = targetEvent.getTimedEventProperty();
        if (tep == null || tep.getTimingEventId() == TimedEventProperty.NO_TIMING_EVENT) {
            Object timeX = timeField.getValue();
            float timeValue = targetEvent.getTime();
            if (timeX != null) {
                timeValue = AsheTypeConvert.toFloat(timeX);
            }
            targetEvent.setTime(timeValue);
        }

        targetEvent.reevaluateProperties();

        for (EventPropertyPanel panel : eventPropertyPanels) {
            panel.setEventProperty(null);
            holder.remove(panel);
        }
        parent.returnPanels(eventPropertyPanels);
        eventPropertyPanels.clear();

        List<EventProperty> props = targetEvent.getEventProperties();
        List<EventProperty.Type> propTypes = new ArrayList<>();
        for (EventProperty prop : props)
            propTypes.add(prop.getType());
        List<EventPropertyPanel> propPanels = parent.getPanels(propTypes);
        for (int i = 0; i < props.size(); i++) {
            EventPropertyPanel panel = propPanels.get(i);
            panel.setEventProperty(props.get(i));
            holder.add(panel);
            eventPropertyPanels.add(panel);
        }

        arcPropertyPanel.setArcProperty(targetEvent.getArcProperty());
        mainResourcePropertyPanel.setResourceProperty(targetEvent.getMainResourceProperty());
        foreResourcePropertyPanel.setResourceProperty(targetEvent.getForegroundResourceProperty());
        backResourcePropertyPanel.setResourceProperty(targetEvent.getBackgroundResourceProperty());
        timedEventPropertyPanel.setTimedEventProperty(targetEvent.getTimedEventProperty());
        timingPropertyPanel.setTimingProperty(targetEvent.getTimingProperty());

        int offset = parent.getEditor().getMapData().indexOf(this.targetEvent);
        String name = l.getKey(this.targetEvent.getEventHrNameKey());
        int metaId = this.targetEvent.getMetaId();
        eventNameLabel.setText(eventNameFormat.replaceAll(EVENT_OFFSET, Integer.toString(offset)).replaceAll(EVENT_NAME, name).replaceAll(EVENT_META_ID, Integer.toString(metaId)));

        timeField.setValue(targetEvent.getTime());

        String data = targetEvent.getEventData();
        String extraData = targetEvent.getEventExtraData();
        float time = targetEvent.getTime();

        parent.getEditor().subAddOperationForEventDataMod(targetEvent, oldData, oldExtraData, oldTime, data, extraData, time);

    }

    private void applyChanges() {
        String oldData = targetEvent.getEventData();
        String oldExtraData = targetEvent.getEventExtraData();
        float oldTime = targetEvent.getTime();

        arcPropertyPanel.writeValues();
        mainResourcePropertyPanel.writeValues();
        foreResourcePropertyPanel.writeValues();
        backResourcePropertyPanel.writeValues();
        timedEventPropertyPanel.writeValues();
        timingPropertyPanel.writeValues();
        for (EventPropertyPanel panel : eventPropertyPanels)
            panel.writeValues();
        TimedEventProperty tep = targetEvent.getTimedEventProperty();
        if (tep == null || tep.getTimingEventId() == TimedEventProperty.NO_TIMING_EVENT) {
            Object timeX = timeField.getValue();
            float timeValue = targetEvent.getTime();
            if (timeX != null) {
                timeValue = AsheTypeConvert.toFloat(timeX);
            }
            targetEvent.setTime(timeValue);
        }

        targetEvent.reevaluateProperties();

        for (EventPropertyPanel panel : eventPropertyPanels) {
            panel.setEventProperty(null);
            holder.remove(panel);
        }
        parent.returnPanels(eventPropertyPanels);
        eventPropertyPanels.clear();

        List<EventProperty> props = targetEvent.getEventProperties();
        List<EventProperty.Type> propTypes = new ArrayList<>();
        for (EventProperty prop : props)
            propTypes.add(prop.getType());
        List<EventPropertyPanel> propPanels = parent.getPanels(propTypes);
        for (int i = 0; i < props.size(); i++) {
            EventPropertyPanel panel = propPanels.get(i);
            panel.setEventProperty(props.get(i));
            holder.add(panel);
            eventPropertyPanels.add(panel);
        }

        arcPropertyPanel.setArcProperty(targetEvent.getArcProperty());
        mainResourcePropertyPanel.setResourceProperty(targetEvent.getMainResourceProperty());
        foreResourcePropertyPanel.setResourceProperty(targetEvent.getForegroundResourceProperty());
        backResourcePropertyPanel.setResourceProperty(targetEvent.getBackgroundResourceProperty());
        timedEventPropertyPanel.setTimedEventProperty(targetEvent.getTimedEventProperty());
        timingPropertyPanel.setTimingProperty(targetEvent.getTimingProperty());

        int offset = parent.getEditor().getMapData().indexOf(this.targetEvent);
        String name = l.getKey(this.targetEvent.getEventHrNameKey());
        int metaId = this.targetEvent.getMetaId();
        eventNameLabel.setText(eventNameFormat.replaceAll(EVENT_OFFSET, Integer.toString(offset)).replaceAll(EVENT_NAME, name).replaceAll(EVENT_META_ID, Integer.toString(metaId)));

        timeField.setValue(targetEvent.getTime());

        String data = targetEvent.getEventData();
        String extraData = targetEvent.getEventExtraData();
        float time = targetEvent.getTime();
        parent.getEditor().addOperationForEventDataMod(targetEvent, oldData, oldExtraData, oldTime, data, extraData, time);
    }
}
