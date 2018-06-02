package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.List;

public class TimingEvent extends MapEvent {

    public static final String KEY_EVENT_TIMING_HR_NAME = "event_timing_hr_name";

    public TimingEvent(float time) {
        super(false, false, KEY_EVENT_TIMING_HR_NAME, Type.TIMING, "Timing", time);
        setTimingProperty(new TimingProperty());
        getTimingProperty().setRootTime(time);
    }

    public String getEventData() {
        return getTimingProperty().getDataValue();
    }

    public void setEventData(String data) {
        getTimingProperty().setDataValue(data);
    }

    public MapEvent clone() {
        TimingEvent out = new TimingEvent(getTime());
        out.setTimingProperty(getTimingProperty().clone());
        return out;
    }

    public String getEventExtraData() {
        return null;
    }

    public void setEventExtraData(String extraData) {
    }

    public void propertyUpdated(Property property) {
    }

    public void addMetaChild(MapEvent event) {
    }

    public List<MapEvent> getMetaChildren() {
        return null;
    }

}
