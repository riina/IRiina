package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.editor.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.AsheTypeConvert;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;
import net.cyriaca.riina.misc.iriina.intralism.data.TimedEventProperty;
import net.cyriaca.riina.misc.iriina.intralism.data.TimingProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Arrays;

public class TimedEventPropertyPanel extends PropertyPanel implements ActionListener {

    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_NAME = "panel_timed_event_property_name";

    private static final String KEY_PANEL_TIMED_EVENT_NOT_SYNCED = "panel_timed_event_not_synced";

    private static final String KEY_PANEL_TIMED_EVENT_TIMING_EVENT_NOT_FOUND = "panel_timed_event_timing_event_not_found";

    private static final String TIMING_EVENT_META_ID = "%timingEventMetaId%";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_TIMING_EVENT_ID = "key_panel_timed_event_property_elem_timing_event_id";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_TICK_TICK = "key_panel_timed_event_property_elem_tick_tick";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_MEASURE = "key_panel_timed_event_property_elem_measure_measure";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_BEAT = "key_panel_timed_event_property_elem_measure_beat";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_SUB_TICK = "key_panel_timed_event_property_elem_sub_tick";
    private static final String KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_RELEASE_BUTTON = "key_panel_timed_event_property_elem_release_button";

    private String notSynced;
    private String timingEventNotFound = null;
    private String[] modes;
    private JLabel title;
    private ArxTitledComboBoxItem<String> modeSelector;
    private TimedEventProperty timedEventProperty;
    private JLabel label_TimingEventId;
    private JTextField field_TimingEventId;
    private JPanel panel_Tick_Tick;
    private JLabel label_Tick_Tick;
    private JFormattedTextField field_Tick_Tick;
    private JPanel panel_Measure_Measure;
    private JLabel label_Measure_Measure;
    private JFormattedTextField field_Measure_Measure;
    private JPanel panel_Measure_Beat;
    private JLabel label_Measure_Beat;
    private JFormattedTextField field_Measure_Beat;
    private JPanel panel_Measure_SubTick;
    private JLabel label_Measure_SubTick;
    private JFormattedTextField field_Measure_SubTick;
    private JButton releaseButton;
    private EditorFrame host = null;

    public TimedEventPropertyPanel() {
        title = new JLabel("Timed Event");
        timedEventProperty = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        modes = new String[2];
        modes[0] = "Tick";
        modes[1] = "Measure";

        notSynced = "Not Synced";

        JPanel mainPanel = new JPanel();
        BoxLayout bl = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(bl);
        modeSelector = new ArxTitledComboBoxItem<>();
        modeSelector.setElements(Arrays.asList(modes));
        modeSelector.addActionListener(this);
        mainPanel.add(modeSelector);

        JPanel panel_TimingEventId = new JPanel(new BorderLayout());
        label_TimingEventId = new JLabel("Timing Event ID");
        field_TimingEventId = new JTextField();
        field_TimingEventId.setEditable(false);
        panel_TimingEventId.add(label_TimingEventId, BorderLayout.WEST);
        panel_TimingEventId.add(field_TimingEventId, BorderLayout.CENTER);
        mainPanel.add(panel_TimingEventId);

        panel_Tick_Tick = new JPanel(new BorderLayout());
        label_Tick_Tick = new JLabel("Tick");
        field_Tick_Tick = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Tick_Tick.add(label_Tick_Tick, BorderLayout.WEST);
        panel_Tick_Tick.add(field_Tick_Tick, BorderLayout.CENTER);
        mainPanel.add(panel_Tick_Tick);

        panel_Measure_Measure = new JPanel(new BorderLayout());
        label_Measure_Measure = new JLabel("Measure");
        field_Measure_Measure = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Measure_Measure.add(label_Measure_Measure, BorderLayout.WEST);
        panel_Measure_Measure.add(field_Measure_Measure, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_Measure);

        panel_Measure_Beat = new JPanel(new BorderLayout());
        label_Measure_Beat = new JLabel("Beat");
        field_Measure_Beat = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Measure_Beat.add(label_Measure_Beat, BorderLayout.WEST);
        panel_Measure_Beat.add(field_Measure_Beat, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_Beat);

        panel_Measure_SubTick = new JPanel(new BorderLayout());
        label_Measure_SubTick = new JLabel("Subtick");
        field_Measure_SubTick = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Measure_SubTick.add(label_Measure_SubTick, BorderLayout.WEST);
        panel_Measure_SubTick.add(field_Measure_SubTick, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_SubTick);

        releaseButton = new JButton("Release");
        mainPanel.add(releaseButton);
        releaseButton.addActionListener(this);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(false);
    }

    public void setTimedEventProperty(TimedEventProperty timedEventProperty) {
        this.timedEventProperty = timedEventProperty;
        if (this.timedEventProperty != null) {
            MapEvent timingEvent = host.getMapData().getTimingEventById(timedEventProperty.getTimingEventId());
            if (timingEvent != null) {
                TimingProperty timingProperty = timingEvent.getTimingProperty();
                if (timingProperty != null) {
                    modeSelector.setSelectedIndex(timingProperty.getTimingMode().ordinal());
                }
            }
            setUIForTimedEventProperty();
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void setHost(EditorFrame host) {
        this.host = host;
    }

    private void setUIForTimedEventProperty() {
        if (timedEventProperty != null) {
            int id = timedEventProperty.getTimingEventId();
            if (id != TimedEventProperty.NO_TIMING_EVENT) {
                modeSelector.setModEnabled(true);
                releaseButton.setEnabled(true);
                field_TimingEventId.setText(Integer.toString(id));
                MapEvent timingEvent = host.getMapData().getTimingEventById(timedEventProperty.getTimingEventId());
                if (timingEvent != null) {
                    TimingProperty timingProperty = timingEvent.getTimingProperty();
                    if (timingProperty != null) {
                        panel_Tick_Tick.setVisible(true);
                        field_Tick_Tick.setValue(timedEventProperty.getTick());
                        switch (TimingProperty.TimingMode.values()[modeSelector.getSelectedIndex()]) {
                            case TICK:
                                panel_Measure_Measure.setVisible(false);

                                panel_Measure_Beat.setVisible(false);

                                panel_Measure_SubTick.setVisible(false);

                                field_Tick_Tick.setEditable(true);
                                break;
                            case MEASURE:
                                validateFields();
                                int tick = timedEventProperty.getTick();
                                panel_Measure_Measure.setVisible(true);
                                field_Measure_Measure.setValue(timingProperty.getMeasureFromTick(tick));

                                panel_Measure_Beat.setVisible(true);
                                field_Measure_Beat.setValue(timingProperty.getBeatFromTick(tick));

                                panel_Measure_SubTick.setVisible(true);
                                field_Measure_SubTick.setValue(timingProperty.getSubtickFromTick(tick));

                                field_Tick_Tick.setEditable(false);
                                break;
                        }
                    } else {
                        modeSelector.setModEnabled(false);
                        releaseButton.setEnabled(false);
                        field_TimingEventId.setText(notSynced);

                        panel_Tick_Tick.setVisible(false);

                        panel_Measure_Measure.setVisible(false);

                        panel_Measure_Beat.setVisible(false);

                        panel_Measure_SubTick.setVisible(false);
                    }
                } else {
                    modeSelector.setModEnabled(false);
                    releaseButton.setEnabled(false);
                    field_TimingEventId.setText(timingEventNotFound.replaceAll(TIMING_EVENT_META_ID, Integer.toString(id)));

                    panel_Tick_Tick.setVisible(false);

                    panel_Measure_Measure.setVisible(false);

                    panel_Measure_Beat.setVisible(false);

                    panel_Measure_SubTick.setVisible(false);
                }
            } else {
                modeSelector.setModEnabled(false);
                releaseButton.setEnabled(false);
                field_TimingEventId.setText(notSynced);

                panel_Tick_Tick.setVisible(false);

                panel_Measure_Measure.setVisible(false);

                panel_Measure_Beat.setVisible(false);

                panel_Measure_SubTick.setVisible(false);
            }
        }
    }

    public void writeValues() {
        if (timedEventProperty != null) {
            switch (TimingProperty.TimingMode.values()[modeSelector.getSelectedIndex()]) {
                case TICK:
                    Object tickX = field_Tick_Tick.getValue();
                    int tickVal = timedEventProperty.getTick();
                    if (tickX != null) {
                        tickVal = AsheTypeConvert.toInt(tickX);
                    }
                    timedEventProperty.setTick(tickVal);
                    break;
                case MEASURE:
                    validateFields();
                    MapEvent timingEvent = host.getMapData().getTimingEventById(timedEventProperty.getTimingEventId());
                    if (timingEvent != null) {
                        TimingProperty timingProperty = timingEvent.getTimingProperty();
                        if (timingProperty != null) {
                            int tick = timedEventProperty.getTick();
                            Object measureX = field_Measure_Measure.getValue();
                            int measureVal = timingProperty.getMeasureFromTick(tick);
                            if (measureX != null) {
                                measureVal = AsheTypeConvert.toInt(measureX);
                            }
                            Object beatX = field_Measure_Beat.getValue();
                            int beatVal = timingProperty.getBeatFromTick(tick);
                            if (measureX != null) {
                                beatVal = AsheTypeConvert.toInt(beatX);
                            }
                            Object subTickX = field_Measure_SubTick.getValue();
                            int subTickVal = timingProperty.getSubtickFromTick(tick);
                            if (subTickX != null) {
                                subTickVal = AsheTypeConvert.toInt(subTickX);
                            }
                            timedEventProperty.setTick(timingProperty.getTickFromMeasureAndBeatAndSubtick(measureVal, beatVal, subTickVal));
                        }
                    }
                    break;
            }
        }
    }

    private void validateFields() {
        if (timedEventProperty != null) {
            MapEvent timingEvent = host.getMapData().getTimingEventById(timedEventProperty.getTimingEventId());
            if (timingEvent != null) {
                TimingProperty timingProperty = timingEvent.getTimingProperty();
                if (timingProperty != null) {
                    int tick = timedEventProperty.getTick();
                    Object beatX = field_Measure_Beat.getValue();
                    int beatValue = timingProperty.getBeatFromTick(tick);
                    if (beatX != null)
                        beatValue = AsheTypeConvert.toInt(beatX);
                    beatValue = Math.max(0, Math.min(timingProperty.getMeasureBeats() - 1, beatValue));
                    field_Measure_Beat.setValue(beatValue);
                    Object subTickX = field_Measure_SubTick.getValue();
                    int subTickVal = timingProperty.getSubtickFromTick(tick);
                    if (subTickX != null)
                        subTickVal = AsheTypeConvert.toInt(subTickX);
                    subTickVal = Math.max(0, Math.min(timingProperty.getBeatTicks() - 1, subTickVal));
                    field_Measure_SubTick.setValue(subTickVal);
                }
            }
        }
    }

    public void localize(Locale l) {
        title.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_NAME));
        modes[0] = l.getKey(TimingPropertyPanel.KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_TICK_NAME);
        modes[1] = l.getKey(TimingPropertyPanel.KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_MEASURE_NAME);
        modeSelector.setTitle(l.getKey(TimingPropertyPanel.KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_NAME));
        modeSelector.setElements(Arrays.asList(modes));
        notSynced = l.getKey(KEY_PANEL_TIMED_EVENT_NOT_SYNCED);
        timingEventNotFound = l.getKey(KEY_PANEL_TIMED_EVENT_TIMING_EVENT_NOT_FOUND);
        label_TimingEventId.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_TIMING_EVENT_ID));
        label_Tick_Tick.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_TICK_TICK));
        label_Measure_Measure.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_MEASURE));
        label_Measure_Beat.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_BEAT));
        label_Measure_SubTick.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_MEASURE_SUB_TICK));
        releaseButton.setText(l.getKey(KEY_PANEL_TIMED_EVENT_PROPERTY_ELEM_RELEASE_BUTTON));
    }

    public void update() {
        if (timedEventProperty != null) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (timedEventProperty != null) {
            if (e.getSource() == releaseButton) {
                MapEvent event = timedEventProperty.getParent();
                if (event != null) {
                    String oldData = event.getEventData();
                    String oldExtraData = event.getEventExtraData();
                    float oldTime = event.getTime();
                    timedEventProperty.setTimingEventId(TimedEventProperty.NO_TIMING_EVENT);
                    String data = event.getEventData();
                    String extraData = event.getEventExtraData();
                    float time = event.getTime();
                    host.addOperationForEventDataMod(event, oldData, oldExtraData, oldTime, data, extraData, time);
                }
            }
            if (e.getSource() == modeSelector) {
                setUIForTimedEventProperty();
            }
        }
    }
}
