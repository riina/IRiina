package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.FloatBounds;
import net.cyriaca.riina.misc.iriina.generic.IntBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ComboEvent extends MapEvent {

    private static final String KEY_EVENT_COMBO_HR_NAME = "event_combo_hr_name";

    private static final String KEY_EVENT_COMBO_TITLE_TOGGLES = "event_combo_title_toggles";

    private List<EventProperty> toggles;

    private EventProperty setBGColorToggle;
    private List<EventProperty> setBGColorProperties;
    private EventProperty setBGColorLabel;

    private EventProperty setPlayerDistanceToggle;
    private List<EventProperty> setPlayerDistanceProperties;
    private EventProperty setPlayerDistanceLabel;

    private EventProperty showBgSpriteToggle;
    private List<EventProperty> showBgSpriteProperties;
    private EventProperty showBgSpriteLabel;

    private EventProperty showFgSpriteToggle;
    private List<EventProperty> showFgSpriteProperties;
    private EventProperty showFgSpriteLabel;

    private EventProperty showTitleToggle;
    private List<EventProperty> showTitleProperties;
    private EventProperty showTitleLabel;

    private EventProperty spawnObjToggle;

    private ArcProperty arcProperty;
    private ResourceProperty foregroundResourceProperty;
    private ResourceProperty backgroundResourceProperty;

    private EventProperty foregroundResourceValue;
    private EventProperty backgroundResourceValue;

    public ComboEvent(float time) {
        super(false, true, KEY_EVENT_COMBO_HR_NAME, Type.COMBO, "Combo", time);
        arcProperty = new ArcProperty();

        setTimedEventProperty(new TimedEventProperty());

        foregroundResourceProperty = new ResourceProperty();
        backgroundResourceProperty = new ResourceProperty();

        toggles = new ArrayList<>();

        toggles.add(new EventProperty(KEY_EVENT_COMBO_TITLE_TOGGLES, EventProperty.Type.LABEL));
        setBGColorToggle = new EventProperty(SetBGColorEvent.KEY_EVENT_SET_B_G_COLOR_HR_NAME,
                EventProperty.Type.BOOLEAN);
        toggles.add(setBGColorToggle);
        setPlayerDistanceToggle = new EventProperty(SetPlayerDistanceEvent.KEY_EVENT_SET_PLAYER_DISTANCE_HR_NAME,
                EventProperty.Type.BOOLEAN);
        toggles.add(setPlayerDistanceToggle);
        showBgSpriteToggle = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_BACKGROUND,
                EventProperty.Type.BOOLEAN);
        toggles.add(showBgSpriteToggle);
        showFgSpriteToggle = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_FOREGROUND,
                EventProperty.Type.BOOLEAN);
        toggles.add(showFgSpriteToggle);
        showTitleToggle = new EventProperty(ShowTitleEvent.KEY_EVENT_SHOW_TITLE_HR_NAME, EventProperty.Type.BOOLEAN);
        toggles.add(showTitleToggle);
        spawnObjToggle = new EventProperty(SpawnObjEvent.KEY_EVENT_SPAWN_OBJ_HR_NAME, EventProperty.Type.BOOLEAN);
        toggles.add(spawnObjToggle);

        setBGColorLabel = new EventProperty(SetBGColorEvent.KEY_EVENT_SET_B_G_COLOR_HR_NAME, EventProperty.Type.LABEL);
        setBGColorProperties = new ArrayList<>();

        EventProperty colorProperty = new EventProperty(SetBGColorEvent.KEY_EVENT_SET_B_G_COLOR_PROPERTY_RGB, EventProperty.Type.COLOR);
        setBGColorProperties.add(colorProperty);
        EventProperty lerpSpeedProperty = new EventProperty(SetBGColorEvent.KEY_EVENT_SET_B_G_COLOR_PROPERTY_LERP_SPEED,
                EventProperty.Type.FLOAT);
        lerpSpeedProperty.setFloatBounds(
                new FloatBounds(SetBGColorEvent.LERP_SPEED_MIN, false, SetBGColorEvent.LERP_SPEED_DEF, true));
        lerpSpeedProperty.setFloat(SetBGColorEvent.LERP_SPEED_DEF);
        setBGColorProperties.add(lerpSpeedProperty);

        setPlayerDistanceLabel = new EventProperty(SetPlayerDistanceEvent.KEY_EVENT_SET_PLAYER_DISTANCE_HR_NAME, EventProperty.Type.LABEL);
        setPlayerDistanceProperties = new ArrayList<>();
        EventProperty distanceProperty = new EventProperty(
                SetPlayerDistanceEvent.KEY_EVENT_SET_PLAYER_DISTANCE_PROPERTY_DISTANCE, EventProperty.Type.INT);
        distanceProperty.setIntBounds(
                new IntBounds(SetPlayerDistanceEvent.DISTANCE_MIN, false, SetPlayerDistanceEvent.DISTANCE_MAX, false));
        distanceProperty.setInt(SetPlayerDistanceEvent.DISTANCE_DEF);

        setPlayerDistanceProperties.add(distanceProperty);

        showBgSpriteLabel = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_BACKGROUND, EventProperty.Type.LABEL);
        showBgSpriteProperties = new ArrayList<>();

        EventProperty bgPosProperty = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS, EventProperty.Type.SELECTOR);
        Map<String, String> bgPosPropertyMap = new TreeMap<>();
        bgPosPropertyMap.put(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_BACKGROUND_HR_NAME, ShowSpriteEvent.POS_VALUE_BACKGROUND);
        bgPosPropertyMap.put(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_FOREGROUND_HR_NAME, ShowSpriteEvent.POS_VALUE_FOREGROUND);
        bgPosProperty.setSelectorMap(bgPosPropertyMap);
        bgPosProperty.setSelector(ShowSpriteEvent.POS_VALUE_BACKGROUND);
        backgroundResourceValue = bgPosProperty;

        EventProperty bgSpriteARProperty = new EventProperty(
                ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_KEEP_ASPECT_RATIO, EventProperty.Type.BOOLEAN);
        showBgSpriteProperties.add(bgSpriteARProperty);
        EventProperty bgSpriteDurationProperty = new EventProperty(
                ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_DURATION, EventProperty.Type.FLOAT);
        bgSpriteDurationProperty.setFloatBounds(
                new FloatBounds(ShowSpriteEvent.DURATION_MIN, false, ShowSpriteEvent.DURATION_MAX_DEF, true));
        bgSpriteDurationProperty.setFloat(ShowSpriteEvent.DURATION_DEF);
        showBgSpriteProperties.add(bgSpriteDurationProperty);

        showFgSpriteLabel = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_HR_NAME_KEY_FOREGROUND, EventProperty.Type.LABEL);
        showFgSpriteProperties = new ArrayList<>();

        EventProperty fgPosProperty = new EventProperty(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS, EventProperty.Type.SELECTOR);
        Map<String, String> fgPosPropertyMap = new TreeMap<>();
        fgPosPropertyMap.put(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_BACKGROUND_HR_NAME, ShowSpriteEvent.POS_VALUE_BACKGROUND);
        fgPosPropertyMap.put(ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_POS_VALUE_FOREGROUND_HR_NAME, ShowSpriteEvent.POS_VALUE_FOREGROUND);
        fgPosProperty.setSelectorMap(fgPosPropertyMap);
        fgPosProperty.setSelector(ShowSpriteEvent.POS_VALUE_FOREGROUND);
        foregroundResourceValue = fgPosProperty;

        EventProperty fgSpriteARProperty = new EventProperty(
                ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_KEEP_ASPECT_RATIO, EventProperty.Type.BOOLEAN);
        showFgSpriteProperties.add(fgSpriteARProperty);
        EventProperty fgSpriteDurationProperty = new EventProperty(
                ShowSpriteEvent.KEY_EVENT_SHOW_SPRITE_PROPERTY_DURATION, EventProperty.Type.FLOAT);
        fgSpriteDurationProperty.setFloatBounds(
                new FloatBounds(ShowSpriteEvent.DURATION_MIN, false, ShowSpriteEvent.DURATION_MAX_DEF, true));
        fgSpriteDurationProperty.setFloat(ShowSpriteEvent.DURATION_DEF);
        showFgSpriteProperties.add(fgSpriteDurationProperty);

        showTitleLabel = new EventProperty(ShowTitleEvent.KEY_EVENT_SHOW_TITLE_HR_NAME, EventProperty.Type.LABEL);
        showTitleProperties = new ArrayList<>();
        EventProperty titleProperty = new EventProperty(ShowTitleEvent.KEY_EVENT_SHOW_TITLE_PROPERTY_TITLE,
                EventProperty.Type.STRING);
        titleProperty.setString(ShowTitleEvent.TITLE_DEF);
        showTitleProperties.add(titleProperty);
        EventProperty durationProperty = new EventProperty(ShowTitleEvent.KEY_EVENT_SHOW_TITLE_PROPERTY_DURATION,
                EventProperty.Type.FLOAT);
        durationProperty.setFloatBounds(
                new FloatBounds(ShowTitleEvent.DURATION_MIN, false, ShowTitleEvent.DURATION_MAX_DEF, true));
        durationProperty.setFloat(ShowTitleEvent.DURATION_DEF);
        showTitleProperties.add(durationProperty);

        setEventProperties(toggles);

        propertyUpdated(null);
    }

    public void reevaluateProperties() {
        List<EventProperty> showProperties = new ArrayList<>(toggles);
        if (setBGColorToggle.getBoolean()) {
            showProperties.add(setBGColorLabel);
            showProperties.addAll(setBGColorProperties);
        }
        if (setPlayerDistanceToggle.getBoolean()) {
            showProperties.add(setPlayerDistanceLabel);
            showProperties.addAll(setPlayerDistanceProperties);
        }
        if (showBgSpriteToggle.getBoolean()) {
            showProperties.add(showBgSpriteLabel);
            showProperties.addAll(showBgSpriteProperties);
        }
        setBackgroundResourceProperty(showBgSpriteToggle.getBoolean() ? backgroundResourceProperty : null);
        if (showFgSpriteToggle.getBoolean()) {
            showProperties.add(showFgSpriteLabel);
            showProperties.addAll(showFgSpriteProperties);
        }
        setForegroundResourceProperty(showFgSpriteToggle.getBoolean() ? foregroundResourceProperty : null);
        if (showTitleToggle.getBoolean()) {
            showProperties.add(showTitleLabel);
            showProperties.addAll(showTitleProperties);
        }
        setArcProperty(spawnObjToggle.getBoolean() ? arcProperty : null);

        setEventProperties(showProperties);

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

    public MapEvent clone() {
        ComboEvent out = new ComboEvent(getTime());
        out.cloneEventProperties(getEventProperties());
        out.internalSetArcProperty(getArcProperty().clone());
        out.setTimedEventProperty(getTimedEventProperty().clone());
        out.internalSetForegroundResourceProperty(getForegroundResourceProperty().clone());
        out.internalSetBackgroundResourceProperty(getBackgroundResourceProperty().clone());
        out.foregroundResourceValue = foregroundResourceValue.clone();
        out.backgroundResourceValue = backgroundResourceValue.clone();
        return out;
    }

    private void internalSetArcProperty(ArcProperty arcProperty) {
        this.arcProperty = arcProperty;
    }

    private void internalSetForegroundResourceProperty(ResourceProperty foregroundResourceProperty) {
        this.foregroundResourceProperty = foregroundResourceProperty;
    }

    private void internalSetBackgroundResourceProperty(ResourceProperty backgroundResourceProperty) {
        this.backgroundResourceProperty = backgroundResourceProperty;
    }

    public void addMetaChild(MapEvent event) {
        List<EventProperty> properties = event.getEventProperties();
        if (event instanceof SetBGColorEvent) {
            setBGColorProperties = new ArrayList<>(properties);
            setBGColorToggle.setBoolean(true);
        } else if (event instanceof SetPlayerDistanceEvent) {
            setPlayerDistanceProperties = new ArrayList<>(properties);
            setPlayerDistanceToggle.setBoolean(true);
        } else if (event instanceof ShowSpriteEvent) {
            if (properties.get(0).getValue().equals(ShowSpriteEvent.POS_VALUE_BACKGROUND)) {
                backgroundResourceProperty = event.getMainResourceProperty();
                setBackgroundResourceProperty(backgroundResourceProperty);
                backgroundResourceValue = properties.get(0);
                showBgSpriteProperties = new ArrayList<>();
                showBgSpriteProperties.add(properties.get(1));
                showBgSpriteProperties.add(properties.get(2));
                showBgSpriteToggle.setBoolean(true);
            } else {
                foregroundResourceProperty = event.getMainResourceProperty();
                setForegroundResourceProperty(foregroundResourceProperty);
                foregroundResourceValue = properties.get(0);
                showFgSpriteProperties = new ArrayList<>();
                showFgSpriteProperties.add(properties.get(1));
                showFgSpriteProperties.add(properties.get(2));
                showFgSpriteToggle.setBoolean(true);
            }
        } else if (event instanceof ShowTitleEvent) {
            showTitleProperties = new ArrayList<>(properties);
            showTitleToggle.setBoolean(true);
        } else if (event instanceof SpawnObjEvent) {
            arcProperty = event.getArcProperty();
            setArcProperty(arcProperty);
            spawnObjToggle.setBoolean(true);
        }
        reevaluateProperties();
    }

    public List<MapEvent> getMetaChildren() {
        reevaluateProperties();
        List<MapEvent> kids = new ArrayList<>();
        if (setBGColorToggle.getBoolean()) {
            SetBGColorEvent setBGColorEvent = new SetBGColorEvent(getTime());
            setBGColorEvent.cloneEventProperties(setBGColorProperties);
            setBGColorEvent.setMetaId(getMetaId());
            kids.add(setBGColorEvent);
        }
        if (setPlayerDistanceToggle.getBoolean()) {
            SetPlayerDistanceEvent setPlayerDistanceEvent = new SetPlayerDistanceEvent(getTime());
            setPlayerDistanceEvent.cloneEventProperties(setPlayerDistanceProperties);
            setPlayerDistanceEvent.setMetaId(getMetaId());
            kids.add(setPlayerDistanceEvent);
        }
        if (showBgSpriteToggle.getBoolean()) {
            ShowSpriteEvent showBgSpriteEvent = new ShowSpriteEvent(getTime());
            ArrayList<EventProperty> props = new ArrayList<>();
            props.add(backgroundResourceValue);
            props.addAll(showBgSpriteProperties);
            showBgSpriteEvent.cloneEventProperties(props);
            showBgSpriteEvent.setMainResourceProperty(getBackgroundResourceProperty());
            showBgSpriteEvent.setMetaId(getMetaId());
            kids.add(showBgSpriteEvent);
        }
        if (showFgSpriteToggle.getBoolean()) {
            ShowSpriteEvent showFgSpriteEvent = new ShowSpriteEvent(getTime());
            ArrayList<EventProperty> props = new ArrayList<>();
            props.add(foregroundResourceValue);
            props.addAll(showFgSpriteProperties);
            showFgSpriteEvent.cloneEventProperties(props);
            showFgSpriteEvent.setMainResourceProperty(getForegroundResourceProperty());
            showFgSpriteEvent.setMetaId(getMetaId());
            kids.add(showFgSpriteEvent);
        }
        if (showTitleToggle.getBoolean()) {
            ShowTitleEvent showTitleEvent = new ShowTitleEvent(getTime());
            showTitleEvent.cloneEventProperties(showTitleProperties);
            showTitleEvent.setMetaId(getMetaId());
            kids.add(showTitleEvent);
        }
        if (spawnObjToggle.getBoolean()) {
            SpawnObjEvent spawnObjEvent = new SpawnObjEvent(getTime());
            spawnObjEvent.setArcProperty(getArcProperty());
            spawnObjEvent.setMetaId(getMetaId());
            kids.add(spawnObjEvent);
        }
        return kids;
    }
}
