package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.List;

/*
 * Event that defines an arc pattern to spawn
 */
public final class SpawnObjEvent extends MapEvent {

    // ORDER (precedence): Up Right Down Left
    public static final String KEY_EVENT_SPAWN_OBJ_HR_NAME = "event_spawn_obj_hr_name";

    public SpawnObjEvent(float time) {
        super(true, false, KEY_EVENT_SPAWN_OBJ_HR_NAME, Type.ARC, "SpawnObj", time);
        setArcProperty(new ArcProperty());
        setTimedEventProperty(new TimedEventProperty());
    }

    public String getEventData() {
        return getArcProperty().getDataValue();
    }

    public void setEventData(String data) {
        if (data.indexOf(',') != -1)
            data = data.substring(0, data.indexOf(','));
        getArcProperty().setDataValue(data);
    }

    public MapEvent clone() {
        SpawnObjEvent out = new SpawnObjEvent(getTime());
        out.setArcProperty(getArcProperty().clone());
        out.setTimedEventProperty(getTimedEventProperty().clone());
        return out;
    }

    public String getEventExtraData() {
        return getTimedEventProperty().getExtraDataValue();
    }

    public void setEventExtraData(String extraData) {
        getTimedEventProperty().setExtraDataValue(extraData);
    }

    public void addMetaChild(MapEvent event) {
    }

    public List<MapEvent> getMetaChildren() {
        return null;
    }
}