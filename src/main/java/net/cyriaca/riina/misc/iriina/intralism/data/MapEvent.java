package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Base class for events, has framework for properties
 * required of event implementations
 */
public abstract class MapEvent implements Comparable<MapEvent> {

    public static final int META_ID_DEF = -1;
    private float time;
    private MapData parent;
    private int metaId;
    private List<EventProperty> eventProperties;
    private ArcProperty arcProperty;
    private TimedEventProperty timedEventProperty;
    private TimingProperty timingProperty;
    private ResourceProperty foregroundResourceProperty;
    private ResourceProperty backgroundResourceProperty;
    private ResourceProperty mainResourceProperty;
    private boolean coreEvent;
    private boolean metaEvent;
    private String eventHrNameKey;
    private Type type;
    private String eventName;
    private List<Float> pastTimes;
    private List<Integer> pastTimedMetaIds;

    public MapEvent(boolean coreEvent, boolean metaEvent, String eventHrNameKey, Type type, String eventName,
                    float time) {
        this.coreEvent = coreEvent;
        this.metaEvent = metaEvent;
        this.eventHrNameKey = eventHrNameKey;
        this.type = type;
        this.eventName = eventName;
        pastTimes = new ArrayList<>();
        pastTimedMetaIds = new ArrayList<>();
        parent = null;
        this.time = Math.max(0.001f, time);
        metaId = META_ID_DEF;
        eventProperties = Collections.unmodifiableList(new ArrayList<EventProperty>());
        arcProperty = null;
        timedEventProperty = null;
        timingProperty = null;
        foregroundResourceProperty = null;
        backgroundResourceProperty = null;
        mainResourceProperty = null;
    }

    public final float getTime() {
        return time;
    }

    /*
     * Modify time and remove source timing event if synced
     * @param time Target time
     */
    public final void setTime(float time) {
        if (timedEventProperty != null && timedEventProperty.getTimingEventId() != TimedEventProperty.NO_TIMING_EVENT)
            timedEventProperty.setTimingEventId(TimedEventProperty.NO_TIMING_EVENT);
        setTimeRaw(time);
    }

    /*
     * Modify time and remove source timing event if synced
     * @param time Target time
     */
    public final void setTimeLight(float time) {
        if (timedEventProperty != null && timedEventProperty.getTimingEventId() != TimedEventProperty.NO_TIMING_EVENT)
            timedEventProperty.setTimingEventId(TimedEventProperty.NO_TIMING_EVENT);
        setTimeRawPart1(time);
    }

    /*
     * Change time back to previously concrete time
     */
    public final void revertTime() {
        if (pastTimes.size() != 0) {
            setTimeRaw(pastTimes.remove(pastTimes.size() - 1));
        }
    }

    /*
     * Store current time for later reversal
     * with <pre>revertTime</pre>
     */
    public final void concreteTime() {
        pastTimes.add(time);
    }

    /*
     * Change timing event ID to previously concrete ID
     */
    public final void revertTimedMetaId() {
        if (pastTimedMetaIds.size() != 0) {
            setTimedMetaId(pastTimedMetaIds.remove(pastTimedMetaIds.size() - 1));
        }
    }

    public final int getTimedMetaId() {
        if (timedEventProperty != null) {
            return timedEventProperty.getTimingEventId();
        } else {
            return TimedEventProperty.NO_TIMING_EVENT;
        }
    }

    /*
     * Set timing event ID
     */
    public final void setTimedMetaId(int timedMetaId) {
        if (timedEventProperty != null) {
            timedEventProperty.setTimingEventId(timedMetaId);
        }
    }

    /*
     * Store current timing event ID for later reversal
     * with <pre>revertTimedMetaId</pre>
     */
    public final void concreteTimedMetaId() {
        if (timedEventProperty != null) {
            pastTimedMetaIds.add(timedEventProperty.getTimingEventId());
        }
    }

    /*
     * Modify time by requesting parent MapData to remove this event,
     * set its internal time, and re-add it to its sorted collections,
     * or just set internal time if there is no parent
     * @param time Target time
     */
    public final void setTimeRaw(float time) {
        if (parent != null)
            parent.repositionEvent(this, time);
        else
            internalSetTime(time);
    }

    /*
     * Modify time by requesting parent MapData to remove this event and
     * set its internal time, (without re-adding it to its sorted collections),
     * or just set internal time if there is no parent
     * @param time Target time
     */
    public final void setTimeRawPart1(float time) {
        if (parent != null)
            parent.repositionEventStart(this, time);
        else
            internalSetTime(time);
    }

    /*
     * Set the event time
     * @param time Target time
     */
    public final void internalSetTime(float time) {
        this.time = Math.max(0.001f, time);
    }

    public final MapData getParent() {
        return parent;
    }

    public final void setParent(MapData parent) {
        this.parent = parent;
    }

    public void reevaluateProperties() {
    }

    public final int getMetaId() {
        return metaId;
    }

    public final void setMetaId(int metaId) {
        this.metaId = metaId;
    }

    protected final void cloneEventProperties(List<EventProperty> eventProperties) {
        if (eventProperties == null)
            return;
        List<EventProperty> propertiesTemp = new ArrayList<>();
        for (EventProperty property : eventProperties)
            propertiesTemp.add(property.clone());
        this.eventProperties = Collections.unmodifiableList(propertiesTemp);
    }

    public final List<EventProperty> getEventProperties() {
        return eventProperties;
    }

    protected final void setEventProperties(List<EventProperty> eventProperties) {
        if (eventProperties == null)
            return;
        this.eventProperties = Collections.unmodifiableList(new ArrayList<>(eventProperties));
    }

    public final ArcProperty getArcProperty() {
        return arcProperty;
    }

    protected final void setArcProperty(ArcProperty arcProperty) {
        this.arcProperty = arcProperty;
        if (arcProperty != null)
            arcProperty.setParent(this);
    }

    public final TimedEventProperty getTimedEventProperty() {
        return timedEventProperty;
    }

    protected final void setTimedEventProperty(TimedEventProperty timedEventProperty) {
        this.timedEventProperty = timedEventProperty;
        if (timedEventProperty != null)
            timedEventProperty.setParent(this);
    }

    public final TimingProperty getTimingProperty() {
        return timingProperty;
    }

    protected final void setTimingProperty(TimingProperty timingProperty) {
        this.timingProperty = timingProperty;
        if (timingProperty != null)
            timingProperty.setParent(this);
    }

    public final ResourceProperty getBackgroundResourceProperty() {
        return this.backgroundResourceProperty;
    }

    protected final void setBackgroundResourceProperty(ResourceProperty backgroundResourceProperty) {
        this.backgroundResourceProperty = backgroundResourceProperty;
        if (backgroundResourceProperty == null)
            return;
        backgroundResourceProperty.setParent(this);
        backgroundResourceProperty.setHrKey(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_BACKGROUND);
    }

    public final ResourceProperty getForegroundResourceProperty() {
        return this.foregroundResourceProperty;
    }

    protected final void setForegroundResourceProperty(ResourceProperty foregroundResourceProperty) {
        this.foregroundResourceProperty = foregroundResourceProperty;
        if (foregroundResourceProperty == null)
            return;
        foregroundResourceProperty.setParent(this);
        foregroundResourceProperty.setHrKey(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_FOREGROUND);
    }

    public final ResourceProperty getMainResourceProperty() {
        return this.mainResourceProperty;
    }

    protected final void setMainResourceProperty(ResourceProperty mainResourceProperty) {
        this.mainResourceProperty = mainResourceProperty;
        if (mainResourceProperty == null)
            return;
        mainResourceProperty.setParent(this);
        mainResourceProperty.setHrKey(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_MAIN);
    }

    public final boolean isCoreEvent() {
        return coreEvent;
    }

    public final boolean isMetaEvent() {
        return metaEvent;
    }

    public final String getEventHrNameKey() {
        return eventHrNameKey;
    }

    public final Type getType() {
        return type;
    }

    public final String getEventName() {
        return eventName;
    }

    public abstract String getEventData();

    public abstract void setEventData(String data);

    public abstract String getEventExtraData();

    public abstract void setEventExtraData(String extraData);

    public abstract void addMetaChild(MapEvent event);

    public abstract List<MapEvent> getMetaChildren();

    public abstract MapEvent clone();

    public int compareTo(MapEvent event) {
        if (time != event.time)
            return (int) (1 * Math.signum(getTime() - event.getTime()));
        else {
            if (metaId != event.metaId)
                return metaId - event.metaId;
            else
                return eventName.compareTo(event.eventName);
        }
    }

    public enum Type {
        VISUAL, ARC, TIMING, COMBO
    }

}
