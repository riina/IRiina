package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.AsheTypeConvert;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;
import net.cyriaca.riina.misc.iriina.intralism.data.TimedEventProperty;
import net.cyriaca.riina.misc.iriina.intralism.data.TimingProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimingPropertyPanel extends PropertyPanel implements ActionListener {

    public static final String KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_NAME = "panel_timing_property_mode_selector_name";
    public static final String KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_MEASURE_NAME = "panel_timing_property_mode_selector_value_measure_name";
    public static final String KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_TICK_NAME = "panel_timing_property_mode_selector_value_tick_name";
    private static final String KEY_PANEL_TIMING_PROPERTY_NAME = "panel_timing_property_name";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_ROOT_TIME = "key_panel_timing_property_elem_root_time";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_LENGTH = "key_panel_timing_property_elem_length";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_TICK_TICKS_PER_SECOND = "key_panel_timing_property_elem_tick_ticks_per_second";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_TICKS_PER_BEAT = "key_panel_timing_property_elem_measure_ticks_per_beat";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEATS_PER_MEASURE = "key_panel_timing_property_elem_measure_beats_per_measure";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEATS_PER_MINUTE = "key_panel_timing_property_elem_measure_beats_per_minute";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_CALCULATE = "key_panel_timing_property_elem_measure_beat_calculate";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_HIT = "key_panel_timing_property_elem_measure_beat_hit";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_COUNT_BEATS = "key_panel_timing_property_elem_measure_beat_count_beats";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_COUNT_BEATS_PER_MINUTE = "key_panel_timing_property_elem_measure_beat_count_beats_per_minute";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_RESET = "key_panel_timing_property_elem_measure_beat_reset";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_APPLY = "key_panel_timing_property_elem_measure_beat_apply";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_ACTION_SYNC_EVENTS = "key_panel_timing_property_elem_action_sync_events";
    private static final String KEY_PANEL_TIMING_PROPERTY_ELEM_ACTION_RELEASE_EVENTS = "key_panel_timing_property_elem_action_release_events";
    private String[] modes;
    private JLabel title;
    private ArxTitledComboBoxItem<String> modeSelector;
    private JLabel label_RootTime;
    private JFormattedTextField rootTime;
    private JLabel label_Length;
    private JFormattedTextField length;
    private JPanel panel_Tick_TicksPerSecond;
    private JLabel label_Tick_TicksPerSecond;
    private JFormattedTextField tick_TicksPerSecond;
    private JPanel panel_Measure_TicksPerBeat;
    private JLabel label_Measure_TicksPerBeat;
    private JFormattedTextField measure_TicksPerBeat;
    private JPanel panel_Measure_BeatsPerMeasure;
    private JLabel label_Measure_BeatsPerMeasure;
    private JFormattedTextField measure_BeatsPerMeasure;
    private JPanel panel_Measure_BeatsPerMinute;
    private JLabel label_Measure_BeatsPerMinute;
    private JFormattedTextField measure_BeatsPerMinute;
    private JLabel labelTitle_Measure_BeatCalculate;
    private JButton measure_Beat_Hit;
    private long measure_Beat_Store_InitialTime;
    private long measure_Beat_Store_LastTime;
    private int measure_Beat_Store_TotalHits;
    private JPanel panel_Measure_Beat_Count_Beats;
    private JLabel label_Measure_Beat_Count_Beats;
    private JTextField measure_Beat_Count_Beats;
    private JPanel panel_Measure_Beat_Count_BeatsPerMinute;
    private JLabel label_Measure_Beat_Count_BeatsPerMinute;
    private JTextField measure_Beat_Count_BeatsPerMinute;
    private JButton measure_Beat_Reset;
    private JButton measure_Beat_Apply;
    private JButton actionSyncEvents;
    private JButton actionReleaseEvents;

    private TimingProperty timingProperty = null;

    private EditorFrame host = null;

    public TimingPropertyPanel() {
        title = new JLabel("Timing");
        measure_Beat_Store_InitialTime = -1L;
        measure_Beat_Store_LastTime = -1L;
        measure_Beat_Store_TotalHits = -1;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        modes = new String[2];
        modes[0] = "Tick";
        modes[1] = "Measure";

        JPanel mainPanel = new JPanel();
        BoxLayout bl = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(bl);
        modeSelector = new ArxTitledComboBoxItem<>();
        modeSelector.setElements(Arrays.asList(modes));
        modeSelector.addActionListener(this);
        mainPanel.add(modeSelector);

        JPanel panel_RootTime = new JPanel(new BorderLayout());
        label_RootTime = new JLabel("RootTime");
        rootTime = new JFormattedTextField(NumberFormat.getInstance());
        panel_RootTime.add(label_RootTime, BorderLayout.WEST);
        panel_RootTime.add(rootTime, BorderLayout.CENTER);
        mainPanel.add(panel_RootTime);

        JPanel panel_Length = new JPanel(new BorderLayout());
        label_Length = new JLabel("Length");
        length = new JFormattedTextField(NumberFormat.getInstance());
        panel_Length.add(label_Length, BorderLayout.WEST);
        panel_Length.add(length, BorderLayout.CENTER);
        mainPanel.add(panel_Length);

        panel_Tick_TicksPerSecond = new JPanel(new BorderLayout());
        label_Tick_TicksPerSecond = new JLabel("TicksPerSecond");
        tick_TicksPerSecond = new JFormattedTextField(NumberFormat.getInstance());
        panel_Tick_TicksPerSecond.add(label_Tick_TicksPerSecond, BorderLayout.WEST);
        panel_Tick_TicksPerSecond.add(tick_TicksPerSecond, BorderLayout.CENTER);
        mainPanel.add(panel_Tick_TicksPerSecond);

        panel_Measure_TicksPerBeat = new JPanel(new BorderLayout());
        label_Measure_TicksPerBeat = new JLabel("TicksPerBeat");
        measure_TicksPerBeat = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Measure_TicksPerBeat.add(label_Measure_TicksPerBeat, BorderLayout.WEST);
        panel_Measure_TicksPerBeat.add(measure_TicksPerBeat, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_TicksPerBeat);

        panel_Measure_BeatsPerMeasure = new JPanel(new BorderLayout());
        label_Measure_BeatsPerMeasure = new JLabel("BeatsPerMeasure");
        measure_BeatsPerMeasure = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel_Measure_BeatsPerMeasure.add(label_Measure_BeatsPerMeasure, BorderLayout.WEST);
        panel_Measure_BeatsPerMeasure.add(measure_BeatsPerMeasure, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_BeatsPerMeasure);

        panel_Measure_BeatsPerMinute = new JPanel(new BorderLayout());
        label_Measure_BeatsPerMinute = new JLabel("BeatsPerMinute");
        measure_BeatsPerMinute = new JFormattedTextField(NumberFormat.getInstance());
        panel_Measure_BeatsPerMinute.add(label_Measure_BeatsPerMinute, BorderLayout.WEST);
        panel_Measure_BeatsPerMinute.add(measure_BeatsPerMinute, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_BeatsPerMinute);

        JPanel panel_Measure_BeatCalculate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelTitle_Measure_BeatCalculate = new JLabel("BPM Timer");
        labelTitle_Measure_BeatCalculate.setFont(SUB_TITLE_FONT);
        panel_Measure_BeatCalculate.add(labelTitle_Measure_BeatCalculate);
        mainPanel.add(panel_Measure_BeatCalculate);

        JPanel panel_Measure_Beat_Hit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        measure_Beat_Hit = new JButton("MeasureBeatHit");
        panel_Measure_Beat_Hit.add(measure_Beat_Hit);
        mainPanel.add(panel_Measure_Beat_Hit);
        measure_Beat_Hit.addActionListener(this);

        panel_Measure_Beat_Count_Beats = new JPanel(new BorderLayout());
        label_Measure_Beat_Count_Beats = new JLabel("RecordedBeats");
        measure_Beat_Count_Beats = new JTextField();
        measure_Beat_Count_Beats.setEditable(false);
        panel_Measure_Beat_Count_Beats.add(label_Measure_Beat_Count_Beats, BorderLayout.WEST);
        panel_Measure_Beat_Count_Beats.add(measure_Beat_Count_Beats, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_Beat_Count_Beats);

        panel_Measure_Beat_Count_BeatsPerMinute = new JPanel(new BorderLayout());
        label_Measure_Beat_Count_BeatsPerMinute = new JLabel("RecordedBPM");
        measure_Beat_Count_BeatsPerMinute = new JTextField();
        measure_Beat_Count_BeatsPerMinute.setEditable(false);
        panel_Measure_Beat_Count_BeatsPerMinute.add(label_Measure_Beat_Count_BeatsPerMinute, BorderLayout.WEST);
        panel_Measure_Beat_Count_BeatsPerMinute.add(measure_Beat_Count_BeatsPerMinute, BorderLayout.CENTER);
        mainPanel.add(panel_Measure_Beat_Count_BeatsPerMinute);

        JPanel panel_Measure_Beat_Reset = new JPanel(new FlowLayout(FlowLayout.LEFT));
        measure_Beat_Reset = new JButton("Reset BPM Measurement");
        panel_Measure_Beat_Reset.add(measure_Beat_Reset);
        mainPanel.add(panel_Measure_Beat_Reset);
        measure_Beat_Reset.addActionListener(this);

        JPanel panel_Measure_Beat_Apply = new JPanel(new FlowLayout(FlowLayout.LEFT));
        measure_Beat_Apply = new JButton("Apply BPM");
        panel_Measure_Beat_Apply.add(measure_Beat_Apply);
        mainPanel.add(panel_Measure_Beat_Apply);
        measure_Beat_Apply.addActionListener(this);

        JPanel panel_ActionSyncEvents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionSyncEvents = new JButton("Sync Events In Range");
        panel_ActionSyncEvents.add(actionSyncEvents);
        mainPanel.add(panel_ActionSyncEvents);
        actionSyncEvents.addActionListener(this);

        JPanel panel_ActionReleaseEvents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionReleaseEvents = new JButton("Release Synced Events");
        panel_ActionReleaseEvents.add(actionReleaseEvents);
        mainPanel.add(panel_ActionReleaseEvents);
        actionReleaseEvents.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(false);
    }

    public void setTimingProperty(TimingProperty timingProperty) {
        this.timingProperty = timingProperty;
        if (this.timingProperty != null) {
            setUIForTimingProperty(true);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void writeValues() {
        if (timingProperty != null) {
            timingProperty.setTimingMode(TimingProperty.TimingMode.values()[modeSelector.getSelectedIndex()]);
            Object rootTimeX = rootTime.getValue();
            float rootTimeVal = timingProperty.getRootTime();
            if (rootTimeX != null)
                rootTimeVal = AsheTypeConvert.toFloat(rootTimeX);
            Object lengthX = length.getValue();
            float lengthVal = timingProperty.getLength();
            if (lengthX != null)
                lengthVal = AsheTypeConvert.toFloat(lengthX);
            timingProperty.setLength(lengthVal);
            timingProperty.setRootTime(rootTimeVal);
            switch (timingProperty.getTimingMode()) {
                case TICK:
                    Object ticksPerSecondX = tick_TicksPerSecond.getValue();
                    float ticksPerSecondValue = timingProperty.getTicksPerSecond();
                    if (ticksPerSecondX != null)
                        ticksPerSecondValue = AsheTypeConvert.toFloat(ticksPerSecondX);
                    timingProperty.setTicksPerSecond(ticksPerSecondValue);
                    break;
                case MEASURE:
                    Object ticksPerBeatX = measure_TicksPerBeat.getValue();
                    int ticksPerBeatValue = timingProperty.getBeatTicks();
                    if (ticksPerBeatX != null)
                        ticksPerBeatValue = AsheTypeConvert.toInt(ticksPerBeatX);
                    timingProperty.setBeatTicks(ticksPerBeatValue);
                    Object beatsPerMeasureX = measure_BeatsPerMeasure.getValue();
                    int beatsPerMeasureValue = timingProperty.getMeasureBeats();
                    if (beatsPerMeasureX != null)
                        beatsPerMeasureValue = AsheTypeConvert.toInt(beatsPerMeasureX);
                    timingProperty.setMeasureBeats(beatsPerMeasureValue);
                    Object bpmX = measure_BeatsPerMinute.getValue();
                    float bpmVal = timingProperty.getBeatsPerMinute();
                    if (bpmX != null)
                        bpmVal = AsheTypeConvert.toFloat(bpmX);
                    timingProperty.setBeatsPerMinute(bpmVal);
                    break;
            }
        }
    }

    private void setUIForTimingProperty(boolean pullTimingMode) {
        if (timingProperty != null) {
            rootTime.setValue(timingProperty.getRootTime());
            length.setValue(timingProperty.getLength());
            if (pullTimingMode)
                modeSelector.setSelectedIndex(timingProperty.getTimingMode().ordinal());
            switch (TimingProperty.TimingMode.values()[modeSelector.getSelectedIndex()]) {

                case TICK:
                    tick_TicksPerSecond.setValue(timingProperty.getTicksPerSecond());
                    panel_Tick_TicksPerSecond.setVisible(true);
                    panel_Measure_TicksPerBeat.setVisible(false);
                    panel_Measure_BeatsPerMeasure.setVisible(false);
                    panel_Measure_BeatsPerMinute.setVisible(false);
                    labelTitle_Measure_BeatCalculate.setVisible(false);
                    measure_Beat_Hit.setVisible(false);
                    panel_Measure_Beat_Count_Beats.setVisible(false);
                    panel_Measure_Beat_Count_BeatsPerMinute.setVisible(false);
                    measure_Beat_Reset.setVisible(false);
                    measure_Beat_Apply.setVisible(false);
                    break;
                case MEASURE:
                    panel_Tick_TicksPerSecond.setVisible(false);
                    measure_TicksPerBeat.setValue(timingProperty.getBeatTicks());
                    panel_Measure_TicksPerBeat.setVisible(true);
                    measure_BeatsPerMeasure.setValue(timingProperty.getMeasureBeats());
                    panel_Measure_BeatsPerMeasure.setVisible(true);
                    measure_BeatsPerMinute.setValue(timingProperty.getBeatsPerMinute());
                    panel_Measure_BeatsPerMinute.setVisible(true);
                    labelTitle_Measure_BeatCalculate.setVisible(true);
                    measure_Beat_Hit.setVisible(true);
                    measure_Beat_Store_TotalHits = 0;
                    measure_Beat_Count_Beats.setText(Integer.toString(measure_Beat_Store_TotalHits));
                    panel_Measure_Beat_Count_Beats.setVisible(true);
                    measure_Beat_Count_BeatsPerMinute.setText("");
                    panel_Measure_Beat_Count_BeatsPerMinute.setVisible(true);
                    measure_Beat_Reset.setVisible(true);
                    measure_Beat_Reset.setEnabled(false);
                    measure_Beat_Apply.setVisible(true);
                    measure_Beat_Apply.setEnabled(false);
                    break;
            }
        }
    }

    public void localize(Locale l) {
        title.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_NAME));
        modes[0] = l.getKey(KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_TICK_NAME);
        modes[1] = l.getKey(KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_VALUE_MEASURE_NAME);
        modeSelector.setTitle(l.getKey(KEY_PANEL_TIMING_PROPERTY_MODE_SELECTOR_NAME));
        modeSelector.setElements(Arrays.asList(modes));
        label_RootTime.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_ROOT_TIME));
        label_Length.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_LENGTH));
        label_Tick_TicksPerSecond.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_TICK_TICKS_PER_SECOND));
        label_Measure_TicksPerBeat.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_TICKS_PER_BEAT));
        label_Measure_BeatsPerMeasure.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEATS_PER_MEASURE));
        label_Measure_BeatsPerMinute.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEATS_PER_MINUTE));
        labelTitle_Measure_BeatCalculate.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_CALCULATE));
        measure_Beat_Hit.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_HIT));
        label_Measure_Beat_Count_Beats.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_COUNT_BEATS));
        label_Measure_Beat_Count_BeatsPerMinute.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_COUNT_BEATS_PER_MINUTE));
        measure_Beat_Reset.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_RESET));
        measure_Beat_Apply.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_MEASURE_BEAT_APPLY));
        actionSyncEvents.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_ACTION_SYNC_EVENTS));
        actionReleaseEvents.setText(l.getKey(KEY_PANEL_TIMING_PROPERTY_ELEM_ACTION_RELEASE_EVENTS));
    }


    public void update() {
        if (timingProperty != null) {
            //setUIForTimingProperty();
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void setHost(EditorFrame host) {
        this.host = host;
    }


    public void actionPerformed(ActionEvent e) {
        if (timingProperty != null) {
            if (e.getSource() == modeSelector) {
                setUIForTimingProperty(false);
            }
            MapData data = host.getMapData();
            MapEvent evt = timingProperty.getParent();
            if (e.getSource() == measure_Beat_Hit) {
                measure_Beat_Store_TotalHits++;
                measure_Beat_Store_LastTime = System.currentTimeMillis();
                if (measure_Beat_Store_TotalHits > 1) {
                    measure_Beat_Count_Beats.setText(Integer.toString(measure_Beat_Store_TotalHits));
                    long delta = measure_Beat_Store_LastTime - measure_Beat_Store_InitialTime;
                    float bpm = (measure_Beat_Store_TotalHits - 1) / (delta / 1000.0f) * 60.0f;
                    measure_Beat_Count_BeatsPerMinute.setText(Float.toString(bpm));
                    measure_Beat_Apply.setEnabled(true);
                } else {
                    measure_Beat_Store_InitialTime = measure_Beat_Store_LastTime;
                    measure_Beat_Count_Beats.setText(Integer.toString(measure_Beat_Store_TotalHits));
                    measure_Beat_Count_BeatsPerMinute.setText("");
                }
                measure_Beat_Reset.setEnabled(true);
            }
            if (e.getSource() == measure_Beat_Reset) {
                measure_Beat_Store_TotalHits = 0;
                measure_Beat_Count_Beats.setText(Integer.toString(measure_Beat_Store_TotalHits));
                measure_Beat_Count_BeatsPerMinute.setText("");
                measure_Beat_Reset.setEnabled(false);
                measure_Beat_Apply.setEnabled(false);
            }
            if (e.getSource() == measure_Beat_Apply) {
                if (measure_Beat_Store_TotalHits > 1) {
                    long delta = measure_Beat_Store_LastTime - measure_Beat_Store_InitialTime;
                    float bpm = (measure_Beat_Store_TotalHits - 1) / (delta / 1000.0f) * 60.0f;
                    measure_BeatsPerMinute.setValue(bpm);
                }
            }
            if (e.getSource() == actionSyncEvents) {
                List<MapEvent> targets = new ArrayList<>(data.getTimedEventsBetweenTimes(evt.getTime(), evt.getTime() + timingProperty.getLength()));
                List<String> oldDataList = new ArrayList<>(targets.size());
                List<String> oldExtraDataList = new ArrayList<>(targets.size());
                List<Float> oldTimeList = new ArrayList<>(targets.size());
                List<String> dataList = new ArrayList<>(targets.size());
                List<String> extraDataList = new ArrayList<>(targets.size());
                List<Float> timeList = new ArrayList<>(targets.size());
                for (MapEvent t : targets) {
                    oldDataList.add(t.getEventData());
                    oldExtraDataList.add(t.getEventExtraData());
                    oldTimeList.add(t.getTime());
                    TimedEventProperty tep = t.getTimedEventProperty();
                    tep.setTimingEventIdRefreshLight(evt.getMetaId());
                    dataList.add(t.getEventData());
                    extraDataList.add(t.getEventExtraData());
                    timeList.add(t.getTime());
                }
                data.repositionEventsFinalize(targets);
                host.addOperationForEventGroupDataMod(targets, oldDataList, oldExtraDataList, oldTimeList, dataList, extraDataList, timeList);
            }
            if (e.getSource() == actionReleaseEvents) {
                List<MapEvent> targets = new ArrayList<>(data.getTimedEventsForTimingEvent(evt));
                List<String> oldDataList = new ArrayList<>(targets.size());
                List<String> oldExtraDataList = new ArrayList<>(targets.size());
                List<Float> oldTimeList = new ArrayList<>(targets.size());
                List<String> dataList = new ArrayList<>(targets.size());
                List<String> extraDataList = new ArrayList<>(targets.size());
                List<Float> timeList = new ArrayList<>(targets.size());
                for (MapEvent t : targets) {
                    oldDataList.add(t.getEventData());
                    oldExtraDataList.add(t.getEventExtraData());
                    oldTimeList.add(t.getTime());
                    TimedEventProperty tep = t.getTimedEventProperty();
                    tep.setTimingEventId(TimedEventProperty.NO_TIMING_EVENT);
                    dataList.add(t.getEventData());
                    extraDataList.add(t.getEventExtraData());
                    timeList.add(t.getTime());
                }
                host.addOperationForEventGroupDataMod(targets, oldDataList, oldExtraDataList, oldTimeList, dataList, extraDataList, timeList);
            }
        }
    }
}
