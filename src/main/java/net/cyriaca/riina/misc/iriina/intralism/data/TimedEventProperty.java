package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimedEventProperty extends Property {

    public static final int NO_TIMING_EVENT = -1;

    private int timingEventId;
    private int tick;

    public TimedEventProperty() {
        this(NO_TIMING_EVENT, -1);
    }

    public TimedEventProperty(int timingEventId) {
        this(timingEventId, -1);
    }

    public TimedEventProperty(int timingEventId, int tick) {
        this.timingEventId = timingEventId;
        this.tick = tick;
    }

    public TimedEventProperty(String extraDataValue) {
        this();
        setExtraDataValue(extraDataValue);
    }

    public TimedEventProperty(List<String> extraDataValueElements) {
        this();
        setExtraDataValue(extraDataValueElements);
    }

    public void refresh() {
        MapEvent event = getParent();
        if (event == null)
            return;
        MapData data = event.getParent();
        if (data == null)
            return;
        TimingEvent timing = data.getTimingEventById(timingEventId);
        if (timing == null)
            return;
        event.setTimeRaw(timing.getTimingProperty().getTimeFromTick(this.tick));
        notifyParentOfChange();
    }

    public void refreshLight() {
        MapEvent event = getParent();
        if (event == null)
            return;
        MapData data = event.getParent();
        if (data == null)
            return;
        TimingEvent timing = data.getTimingEventById(timingEventId);
        if (timing == null)
            return;
        event.setTimeRawPart1(timing.getTimingProperty().getTimeFromTick(this.tick));
        notifyParentOfChange();
    }


    public String getExtraDataValue() {
        return timingEventId + "," + tick;
    }

    public void setExtraDataValue(List<String> extraDataValueElements) {
        if (extraDataValueElements == null)
            return;
        if (extraDataValueElements.size() != 2)
            return;
        try {
            this.timingEventId = Integer.parseInt(extraDataValueElements.get(0));
        } catch (NumberFormatException e) {
            return;
        }
        try {
            this.tick = Integer.parseInt(extraDataValueElements.get(1));
        } catch (NumberFormatException e) {
            return;
        }
        refresh();
    }

    public void setExtraDataValue(String extraDataValue) {
        if (extraDataValue != null)
            setExtraDataValue(Arrays.asList(extraDataValue.split("\\s*,\\s*")));
    }

    public List<String> getExtraDataValueElements() {
        List<String> out = new ArrayList<>(2);
        out.add(Integer.toString(timingEventId));
        out.add(Integer.toString(tick));
        return out;
    }

    public int getTimingEventId() {
        return timingEventId;
    }

    public void setTimingEventId(int value) {
        timingEventId = value;
        if (timingEventId == NO_TIMING_EVENT)
            return;
        MapEvent event = getParent();
        if (event != null) {
            MapData data = event.getParent();
            if (data != null) {
                TimingEvent timing = data.getTimingEventById(timingEventId);
                if (timing != null) {
                    TimingProperty tp = timing.getTimingProperty();
                    if (tp != null) {
                        tick = tp.getClosestTickFromTime(event.getTime());
                    } else {
                        System.err.println("ERROR: Timing event at time " + timing.getTime() + " with data \"" + timing.getEventData() + "\" and extra data \"" + timing.getEventExtraData() + "\" doesn't have a timing property!");
                        System.exit(4043);
                    }
                }
            }
        }
        refresh();
    }

    public void setTimingEventIdRefreshLight(int value) {
        timingEventId = value;
        if (timingEventId == NO_TIMING_EVENT)
            return;
        MapEvent event = getParent();
        if (event != null) {
            MapData data = event.getParent();
            if (data != null) {
                TimingEvent timing = data.getTimingEventById(timingEventId);
                if (timing != null) {
                    TimingProperty tp = timing.getTimingProperty();
                    if (tp != null) {
                        tick = tp.getClosestTickFromTime(event.getTime());
                    } else {
                        System.err.println("ERROR: Timing event at time " + timing.getTime() + " with data \"" + timing.getEventData() + "\" and extra data \"" + timing.getEventExtraData() + "\" doesn't have a timing property!");
                        System.exit(4043);
                    }
                }
            }
        }
        refreshLight();
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
        refresh();
    }

    public void setTickRefreshLight(int tick) {
        this.tick = tick;
        refreshLight();
    }

    public void clearTimingEventId() {
        timingEventId = NO_TIMING_EVENT;
    }

    public TimedEventProperty clone() {
        return new TimedEventProperty(timingEventId, tick);
    }

}
