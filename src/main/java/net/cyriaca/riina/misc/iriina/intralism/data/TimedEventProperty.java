package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimedEventProperty extends Property {

	public static final int NO_TIMING_EVENT = -1;

	private int timingEventId = -1;
	private int tick = -1;

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
        if (timing == null) // this is fine, if in the middle of loading a map, we can just wait until the
            // timing event is loaded. If there's never gonna be such an event, well...
            // Great!
            return;
        event.setTimeRaw(timing.getTimingProperty().getTimeFromTick(this.tick));
        //System.out.println("Targeted tick " + this.tick);
        notifyParentOfChange();
    }

	public String getExtraDataValue() {
		return timingEventId + "," + tick;
	}

	public List<String> getExtraDataValueElements() {
		List<String> out = new ArrayList<>(2);
		out.add(Integer.toString(timingEventId));
		out.add(Integer.toString(tick));
		return out;
	}

	public void setTimingEventId(int value) {
		timingEventId = value;
        MapEvent event = getParent();
        if (event != null) {
            MapData data = event.getParent();
            if (data != null) {
                TimingEvent timing = data.getTimingEventById(timingEventId);
                if (timing != null){
                    TimingProperty tp = timing.getTimingProperty();
                    if(tp != null){
                        tick = tp.getClosestTickFromTime(event.getTime());
                    }else{
                        System.err.println("ERROR: Timing event at time " + timing.getTime() + " with data \"" + timing.getEventData() + "\" and extra data \"" + timing.getEventExtraData() + "\" doesn't have a timing property!");
                        System.exit(4043);
                    }
                }
            }
        }
		refresh();
	}

	public void setTick(int tick) {
		this.tick = tick;
		refresh();
	}

	public void setExtraDataValue(String extraDataValue) {
		if (extraDataValue != null)
			setExtraDataValue(Arrays.asList(extraDataValue.split("\\s*,\\s*")));
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

	public int getTimingEventId() {
		return timingEventId;
	}

	public int getTick() {
		return tick;
	}

	public void clearTimingEventId() {
		timingEventId = NO_TIMING_EVENT;
	}

	public TimedEventProperty clone() {
		return new TimedEventProperty(timingEventId, tick);
	}

}
