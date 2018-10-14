package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Represents the timing scheme for a TimingEvent
 */
public class TimingProperty extends Property {

    private static final float LENGTH_MIN = 1.0f;

    private float rootTime;
    private float ticksPerSecond;
    private int measureBeats;
    private int beatTicks;
    private TimingMode timingMode;
    private float length;
    private List<Float> pastLengths;
    private List<Float> pastRootTimes;

    public TimingProperty() {
        rootTime = 1.0f;
        ticksPerSecond = 36.0f;
        measureBeats = 4;
        beatTicks = 6;
        timingMode = TimingMode.MEASURE;
        length = 1.0f;
        pastLengths = new ArrayList<>();
        pastRootTimes = new ArrayList<>();
    }

    public TimingProperty(float rootTime, float ticksPerSecond, int measureBeats, int beatTicks, TimingMode timingMode,
                          float length) {
        this.rootTime = rootTime;
        this.ticksPerSecond = ticksPerSecond;
        this.measureBeats = measureBeats;
        this.beatTicks = beatTicks;
        this.timingMode = timingMode;
        this.length = length;
        pastLengths = new ArrayList<>();
        pastRootTimes = new ArrayList<>();
    }

    public TimingProperty(String extraDataValue) {
        this(Arrays.asList(extraDataValue.split("\\s*,\\s*")));
    }

    public TimingProperty(List<String> dataValueElements) {
        this();
        setDataValue(dataValueElements);
    }

    public final void revertLength() {
        if (pastLengths.size() != 0) {
            setLength(pastLengths.remove(pastLengths.size() - 1));
        }
    }

    public final void concreteLength() {
        pastLengths.add(length);
    }

    public final void revertRootTime() {
        if (pastRootTimes.size() != 0) {
            setRootTime(pastRootTimes.remove(pastRootTimes.size() - 1));
        }
    }

    public final void concreteRootTime() {
        pastRootTimes.add(rootTime);
    }

    public String getDataValue() {
        return rootTime + "," + ticksPerSecond + "," + measureBeats + "," + beatTicks + "," + timingMode.name() + ","
                + length;
    }

    public void setDataValue(List<String> dataValueElements) {
        if (dataValueElements == null)
            return;
        if (dataValueElements.size() != 6)
            return;
        try {
            this.rootTime = Float.parseFloat(dataValueElements.get(0));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            this.ticksPerSecond = Float.parseFloat(dataValueElements.get(1));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            this.measureBeats = Integer.parseInt(dataValueElements.get(2));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            this.beatTicks = Integer.parseInt(dataValueElements.get(3));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            this.timingMode = TimingMode.valueOf(dataValueElements.get(4));
        } catch (IllegalArgumentException e) {
            return;
        }
        try {
            this.length = Float.parseFloat(dataValueElements.get(5));
        } catch (NumberFormatException e) {
            return;
        }
        notifyParentOfChange();
    }

    public void setDataValue(String dataValue) {
        if (dataValue != null)
            setDataValue(Arrays.asList(dataValue.split("\\s*,\\s*")));
    }

    public float getRootTime() {
        return rootTime;
    }

    public void setRootTime(float value) {
        MapEvent parent = getParent();
        if (parent != null) {
            rootTime = Math.max(getParent().getTime(), Math.min(value, getParent().getTime() + length));
            MapData data = parent.getParent();
            if (data != null) {
                List<MapEvent> evts = data.getTimedEventsForTimingEvent(parent);
                for (MapEvent e : evts) {
                    TimedEventProperty tep = e.getTimedEventProperty();
                    if (tep != null)
                        tep.refresh();
                }
            }
        } else
            rootTime = value;
        notifyParentOfChange();
    }

    public List<MapEvent> setRootTimeLight(float value) {
        MapEvent parent = getParent();
        if (parent != null) {
            rootTime = Math.max(getParent().getTime(), Math.min(value, getParent().getTime() + length));
            MapData data = parent.getParent();
            if (data != null) {
                List<MapEvent> evts = data.getTimedEventsForTimingEvent(parent);
                for (MapEvent e : evts) {
                    TimedEventProperty tep = e.getTimedEventProperty();
                    if (tep != null)
                        tep.refreshLight();

                }
                return evts;
            }
        } else
            rootTime = value;
        notifyParentOfChange();
        return null;
    }

    public TimingMode getTimingMode() {
        return timingMode;
    }

    public void setTimingMode(TimingMode mode) {
        timingMode = mode;
        notifyParentOfChange();
    }

    public float getTicksPerSecond() {
        return ticksPerSecond;
    }

    public void setTicksPerSecond(float value) {
        ticksPerSecond = value;
        notifyParentOfChange();
    }

    public int getMeasureBeats() {
        return measureBeats;
    }

    public void setMeasureBeats(int value) {
        measureBeats = value;
        notifyParentOfChange();
    }

    public float getBeatsPerMinute() {
        return ticksPerSecond * 60.0f / ((float) beatTicks);
    }

    public void setBeatsPerMinute(float bpm) {
        ticksPerSecond = bpm / 60.0f * ((float) beatTicks);
    }

    public int getBeatTicks() {
        return beatTicks;
    }

    public void setBeatTicks(int value) {
        beatTicks = value;
        notifyParentOfChange();
    }

    public float getLength() {
        return length;
    }

    public void setLength(float value) {
        length = Math.max(value, LENGTH_MIN);
        notifyParentOfChange();
    }

    public int getClosestMeasureFromTime(float time) {
        return getMeasureFromTick(getClosestTickFromTime(time));
    }

    public int getClosestBeatFromTime(float time) {
        return getBeatFromTick(getClosestTickFromTime(time));
    }

    public int getClosestInMeasureTickFromTime(float time) {
        return getInMeasureTickFromTick(getClosestTickFromTime(time));
    }

    public int getClosestTickFromTime(float time) {
        return Math.round((time - rootTime) * ticksPerSecond);
    }

    public int getClosestBeatTickBeforeTime(float time) {
        int tick = (int) Math.floor((time - rootTime) * ticksPerSecond);
        return getTickFromMeasureAndBeat(getMeasureFromTick(tick), getBeatFromTick(tick));
    }

    public int getTicksPerMeasure() {
        return beatTicks * measureBeats;
    }

    public int getMeasureFromTick(int tick) {
        return Math.floorDiv(tick, getTicksPerMeasure());
    }

    public int getBeatFromTick(int tick) {
        int measureTick = tick % getTicksPerMeasure();
        if (measureTick < 0)
            measureTick += getTicksPerMeasure();
        return Math.floorDiv(measureTick, getBeatTicks());
    }

    public int getInMeasureTickFromTick(int tick) {
        return tick % getTicksPerMeasure();
    }

    public int getSubtickFromTick(int tick) {
        return tick > 0 ? tick % beatTicks : (tick % beatTicks + beatTicks) % beatTicks;
    }

    public int getTickFromMeasureAndBeat(int measure, int beat) {
        return measure * getTicksPerMeasure() + beat * beatTicks;
    }

    public int getTickFromMeasureAndBeatAndSubtick(int measure, int beat, int subtick) {
        return measure * getTicksPerMeasure() + beat * beatTicks + subtick;
    }

    public float getTimeFromTick(int tick) {
        return rootTime + tick / ticksPerSecond;
    }

    public float getTimeFromMeasureAndBeat(int measure, int beat) {
        return getTimeFromTick(getTickFromMeasureAndBeat(measure, beat));
    }

    public TimingProperty clone() {
        TimingProperty out = new TimingProperty();
        out.rootTime = rootTime;
        out.ticksPerSecond = ticksPerSecond;
        out.measureBeats = measureBeats;
        out.beatTicks = beatTicks;
        out.timingMode = timingMode;
        return out;
    }

    public enum TimingMode {
        TICK, MEASURE
    }

}
