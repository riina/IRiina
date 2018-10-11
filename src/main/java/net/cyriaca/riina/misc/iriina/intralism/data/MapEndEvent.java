package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.List;

/*
 * Event that marks the end of a level prior to the
 * audio ending
 */
public class MapEndEvent extends MapEvent {

    private static final String KEY_EVENT_MAP_END_HR_NAME = "event_map_end_hr_name";

    public MapEndEvent(float time) {
        super(true, false, KEY_EVENT_MAP_END_HR_NAME, Type.VISUAL, "MapEnd", time);
    }

    public MapEvent clone() {
        return new MapEndEvent(getTime());
    }

    public String getEventData() {
        return "";
    }

    public void setEventData(String data) {
    }

    public String getEventExtraData() {
        return null;
    }

    public void setEventExtraData(String extraData) {
    }

    public void addMetaChild(MapEvent event) {
    }

    public List<MapEvent> getMetaChildren() {
        return null;
    }
}