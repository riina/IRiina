package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.FloatBounds;

import java.util.ArrayList;
import java.util.List;

/*
 * Event that defines text to be displayed in
 * an in-game banner
 */
public final class ShowTitleEvent extends MapEvent {

    public static final String KEY_EVENT_SHOW_TITLE_HR_NAME = "event_show_title_hr_name";
    public static final String KEY_EVENT_SHOW_TITLE_PROPERTY_TITLE = "event_show_title_property_title";
    public static final String KEY_EVENT_SHOW_TITLE_PROPERTY_DURATION = "event_show_title_property_duration";

    public static final String TITLE_DEF = "~Text~";
    public static final float DURATION_MIN = 0.1f;
    public static final float DURATION_DEF = 1.0f;
    public static final float DURATION_MAX_DEF = 2.0f;

    public ShowTitleEvent(float time) {
        super(true, false, KEY_EVENT_SHOW_TITLE_HR_NAME, Type.VISUAL, "ShowTitle", time);
        List<EventProperty> propertiesTemp = new ArrayList<>();
        EventProperty titleProperty = new EventProperty(KEY_EVENT_SHOW_TITLE_PROPERTY_TITLE, EventProperty.Type.STRING);
        titleProperty.setString(TITLE_DEF);
        propertiesTemp.add(titleProperty);
        EventProperty durationProperty = new EventProperty(KEY_EVENT_SHOW_TITLE_PROPERTY_DURATION,
                EventProperty.Type.FLOAT);
        durationProperty.setFloatBounds(new FloatBounds(DURATION_MIN, false, DURATION_MAX_DEF, true));
        durationProperty.setFloat(DURATION_DEF);
        propertiesTemp.add(durationProperty);
        setEventProperties(propertiesTemp);
    }

    public String getEventData() {
        List<EventProperty> properties = getEventProperties();
        return properties.get(0).getDataValue() + "," + properties.get(1).getDataValue();
    }

    public void setEventData(String data) {
        int lastPos = data.lastIndexOf(',');
        if (lastPos == -1)
            return;
        String titleStr = data.substring(0, lastPos);
        String durationStr = data.substring(lastPos + 1, data.length());
        List<EventProperty> properties = getEventProperties();
        properties.get(0).setValue(titleStr);
        properties.get(1).setValue(durationStr);
    }

    public MapEvent clone() {
        ShowTitleEvent out;
        out = new ShowTitleEvent(getTime());
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