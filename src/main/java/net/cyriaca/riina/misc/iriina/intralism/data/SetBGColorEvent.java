package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.FloatBounds;

import java.util.ArrayList;
import java.util.List;

public class SetBGColorEvent extends MapEvent {

    public static final String KEY_EVENT_SET_B_G_COLOR_HR_NAME = "event_set_b_g_color_hr_name";
    public static final String KEY_EVENT_SET_B_G_COLOR_PROPERTY_RGB = "event_set_b_g_color_property_rgb";
    public static final String KEY_EVENT_SET_B_G_COLOR_PROPERTY_LERP_SPEED = "event_set_b_g_color_property_lerp_speed";
    public static final float LERP_SPEED_MIN = 0.1f;
    public static final float LERP_SPEED_DEF = 10.0f;

    public SetBGColorEvent(float time) {
        super(true, false, KEY_EVENT_SET_B_G_COLOR_HR_NAME, Type.VISUAL, "SetBGColor", time);
        List<EventProperty> propertiesTemp = new ArrayList<>();
        EventProperty colorProperty = new EventProperty(KEY_EVENT_SET_B_G_COLOR_PROPERTY_RGB, EventProperty.Type.COLOR);
        propertiesTemp.add(colorProperty);
        EventProperty lerpSpeedProperty = new EventProperty(KEY_EVENT_SET_B_G_COLOR_PROPERTY_LERP_SPEED,
                EventProperty.Type.FLOAT);
        lerpSpeedProperty.setFloatBounds(new FloatBounds(LERP_SPEED_MIN, false, LERP_SPEED_DEF, true));
        lerpSpeedProperty.setFloat(LERP_SPEED_DEF);
        propertiesTemp.add(lerpSpeedProperty);
        setEventProperties(propertiesTemp);
    }

    public String getEventData() {
        List<EventProperty> properties = getEventProperties();
        return properties.get(0).getDataValue() + "," + properties.get(1).getDataValue();
    }

    public void setEventData(String data) {
        String[] eventData = data.split("\\s*,\\s*");
        if (eventData.length != 4)
            return;
        List<EventProperty> properties = getEventProperties();
        properties.get(0).setValue(data.substring(0, data.lastIndexOf(',')));
        properties.get(1).setValue(eventData[3]);
    }

    public MapEvent clone() {
        SetBGColorEvent out = new SetBGColorEvent(getTime());
        out.cloneEventProperties(getEventProperties());
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