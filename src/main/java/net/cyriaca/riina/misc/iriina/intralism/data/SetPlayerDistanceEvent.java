package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.IntBounds;

import java.util.ArrayList;
import java.util.List;

public final class SetPlayerDistanceEvent extends MapEvent {

    public static final String KEY_EVENT_SET_PLAYER_DISTANCE_HR_NAME = "event_set_player_distance_hr_name";
    public static final String KEY_EVENT_SET_PLAYER_DISTANCE_PROPERTY_DISTANCE = "event_set_player_distance_property_distance";

    public static byte DISTANCE_MIN = 4;
    public static byte DISTANCE_MAX = 28;
    public static byte DISTANCE_DEF = 14;

    public SetPlayerDistanceEvent(float time) {
        super(true, false, KEY_EVENT_SET_PLAYER_DISTANCE_HR_NAME, Type.VISUAL, "SetPlayerDistance", time);
        List<EventProperty> propertiesTemp = new ArrayList<>();
        EventProperty distanceProperty = new EventProperty(KEY_EVENT_SET_PLAYER_DISTANCE_PROPERTY_DISTANCE,
                EventProperty.Type.INT);
        distanceProperty.setIntBounds(new IntBounds(DISTANCE_MIN, false, DISTANCE_MAX, false));
        distanceProperty.setInt(DISTANCE_DEF);
        propertiesTemp.add(distanceProperty);
        setEventProperties(propertiesTemp);
    }

    public String getEventData() {
        return getEventProperties().get(0).getDataValue();
    }

    public void setEventData(String data) {
        String[] eventData = data.split("\\s*,\\s*");
        if (eventData.length != 1)
            return;
        try {
            getEventProperties().get(0).setInt(Integer.parseInt(eventData[0]));
        } catch (NumberFormatException ignored) {
        }
    }

    public MapEvent clone() {
        SetPlayerDistanceEvent out = new SetPlayerDistanceEvent(getTime());
        out.cloneEventProperties(getEventProperties());
        return out;
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