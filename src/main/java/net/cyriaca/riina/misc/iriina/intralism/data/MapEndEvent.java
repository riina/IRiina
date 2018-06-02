package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.List;

public class MapEndEvent extends MapEvent {

    private static final String KEY_EVENT_MAP_END_HR_NAME = "event_map_end_hr_name";

    public MapEndEvent(float time) {
        super(true, false, KEY_EVENT_MAP_END_HR_NAME, Type.VISUAL, "MapEnd", time);
    }

    public MapEvent clone() {
        MapEvent out = new MapEndEvent(getTime());
        return out;
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

    public void propertyUpdated(Property property) {
    }

    public void addMetaChild(MapEvent event) {
    }

    public List<MapEvent> getMetaChildren() {
        return null;
    }
}