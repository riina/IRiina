package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.FloatBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class ShowSpriteEvent extends MapEvent {

    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME = "event_show_sprite_hr_name";
    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME_MAIN = "event_show_sprite_hr_name_main";
    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_FOREGROUND = "event_show_sprite_hr_name_key_foreground";
    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_BACKGROUND = "event_show_sprite_hr_name_key_background";
    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME_FOREGROUND = "event_show_sprite_hr_name_foreground";
    public static final String KEY_EVENT_SHOW_SPRITE_HR_NAME_BACKGROUND = "event_show_sprite_hr_name_background";
    public static final String KEY_EVENT_SHOW_SPRITE_PROPERTY_POS = "event_show_sprite_property_pos";
    public static final String KEY_EVENT_SHOW_SPRITE_PROPERTY_KEEP_ASPECT_RATIO = "event_show_sprite_property_keep_aspect_ratio";
    public static final String KEY_EVENT_SHOW_SPRITE_PROPERTY_DURATION = "event_show_sprite_property_duration";
    public static final String KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_BACKGROUND_HR_NAME = "event_show_sprite_property_pos_value_background_hr_name";
    public static final String POS_VALUE_BACKGROUND = "0";
    public static final String KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_FOREGROUND_HR_NAME = "event_show_sprite_property_pos_value_foreground_hr_name";
    public static final String POS_VALUE_FOREGROUND = "1";

    public static final float DURATION_MIN = 0.1f;
    public static final float DURATION_DEF = 1.0f;
    public static final float DURATION_MAX_DEF = 2.0f;

    public ShowSpriteEvent(float time) {
        super(true, false, KEY_EVENT_SHOW_SPRITE_HR_NAME, Type.VISUAL, "ShowSprite", time);
        List<EventProperty> propertiesTemp = new ArrayList<>();
        ResourceProperty resProperty = new ResourceProperty();
        setMainResourceProperty(resProperty);
        EventProperty posProperty = new EventProperty(KEY_EVENT_SHOW_SPRITE_PROPERTY_POS, EventProperty.Type.SELECTOR);
        Map<String, String> posPropertyMap = new TreeMap<>();
        posPropertyMap.put(KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_BACKGROUND_HR_NAME, POS_VALUE_BACKGROUND);
        posPropertyMap.put(KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_FOREGROUND_HR_NAME, POS_VALUE_FOREGROUND);
        posProperty.setSelectorMap(posPropertyMap);
        posProperty.setSelector(POS_VALUE_BACKGROUND);
        propertiesTemp.add(posProperty);
        propertiesTemp
                .add(new EventProperty(KEY_EVENT_SHOW_SPRITE_PROPERTY_KEEP_ASPECT_RATIO, EventProperty.Type.BOOLEAN));
        EventProperty durationProperty = new EventProperty(KEY_EVENT_SHOW_SPRITE_PROPERTY_DURATION,
                EventProperty.Type.FLOAT);
        durationProperty.setFloatBounds(new FloatBounds(DURATION_MIN, false, DURATION_MAX_DEF, true));
        durationProperty.setFloat(DURATION_DEF);
        propertiesTemp.add(durationProperty);
        setEventProperties(propertiesTemp);
    }

    public String getEventData() {
        List<EventProperty> properties = getEventProperties();
        return getMainResourceProperty().getResourceName() + "," + properties.get(0).getDataValue() + ","
                + properties.get(1).getDataValue() + "," + properties.get(2).getDataValue();
    }

    public void setEventData(String data) {
        String[] eventData = data.split("\\s*,\\s*");
        if (eventData.length != 4)
            return;
        getMainResourceProperty().setResourceName(eventData[0]);
        List<EventProperty> properties = getEventProperties();
        for (int i = 0; i < 3; i++)
            properties.get(i).setValue(eventData[i + 1]);
    }

    public MapEvent clone() {
        ShowSpriteEvent out = new ShowSpriteEvent(getTime());
        out.setMainResourceProperty(getMainResourceProperty().clone());
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