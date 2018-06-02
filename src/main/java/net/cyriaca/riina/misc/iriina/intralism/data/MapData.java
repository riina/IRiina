package net.cyriaca.riina.misc.iriina.intralism.data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class MapData {

    public static final float SPEED_MODIFIER_MIN = 5.0f;
    public static final float SPEED_MODIFIER_MAX = 30.0f;
    public static final int LIVES_MIN = 1;
    public static final int LIVES_MAX = 50;
    public static final int ENV_MIN = 0;
    public static final int ENV_MAX = 6;
    private static final String[] VALID_TAGS = {"Alternative", "Anime", "Blues", "Children", "Classical", "Dance",
            "Electronic", "Folk", "Hip-hop", "Indie", "Instrumental", "Jazz", "Metal", "OneHand", "Other", "Pop", "Rap",
            "Rock", "Soundtrack", "NewEditor"};
    private String id;
    private String name;
    private String info;
    private List<MapResource> mapResources;
    private List<String> tags;
    private String moreInfoURL;
    private float speed; // def 15
    private int lives; // def. 5
    private String musicFile;
    private float musicTime;
    private String iconFile;
    private Image iconImage;
    private int generationType;
    private int environmentType; // def. 0

    private Map<Float, Checkpoint> checkpoints;
    private Set<MapEvent> eventSet;
    private List<MapEvent> eventList;

    private Map<Integer, List<MapEvent>> eventsByMetaId;
    private Map<Integer, MapEvent> metaEventsByMetaId;
    private MapEvent onlyEndEvent;
    private int nextMetaId;

    public MapData() {
        id = "Undefined";
        name = "Undefined";
        info = "Undefined";
        mapResources = new ArrayList<>();
        tags = new ArrayList<>();
        moreInfoURL = "";
        speed = 15.0f;
        lives = 5;
        musicFile = "Undefined.ogg";
        musicTime = -1.0f;
        iconFile = "Undefined.png";
        iconImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        generationType = 2;
        environmentType = 0;
        checkpoints = new TreeMap<>();
        eventSet = new TreeSet<>();
        eventList = new ArrayList<>();
        eventsByMetaId = new TreeMap<>();
        metaEventsByMetaId = new TreeMap<>();
        onlyEndEvent = null;
        nextMetaId = 0;
    }

    public static MapEvent createMapEvent(String type, float time) {
        MapEvent out = null;
        switch (type) {
            case "MapEnd":
                out = new MapEndEvent(time);
                break;
            case "SetBGColor":
                out = new SetBGColorEvent(time);
                break;
            case "ShowSprite":
                out = new ShowSpriteEvent(time);
                break;
            case "ShowTitle":
                out = new ShowTitleEvent(time);
                break;
            case "SetPlayerDistance":
                out = new SetPlayerDistanceEvent(time);
                break;
            case "SpawnObj":
                out = new SpawnObjEvent(time);
                break;
            case "Timing":
                out = new TimingEvent(time);
                break;
            case "Combo":
                out = new ComboEvent(time);
                break;
        }
        return out;
    }

    public static List<String> getValidTags() {
        return Collections.unmodifiableList(Arrays.asList(VALID_TAGS));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null)
            this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        if (info != null)
            this.info = info;
    }

    public List<MapResource> getMapResources() {
        return Collections.unmodifiableList(mapResources);
    }

    public void setMapResources(List<MapResource> mapResources) {
        if (mapResources == null)
            return;
        this.mapResources = new ArrayList<>(mapResources.size());
        for (MapResource l : mapResources)
            addMapResource(l);
    }

    public void addMapResource(MapResource resource) {
        if (resource == null)
            return;
        if (mapResources.contains(resource))
            return;
        for (MapResource l : mapResources)
            if (l.getName().equals(resource.getName()))
                return;
        mapResources.add(resource);
    }

    public void removeMapResource(MapResource resource) {
        if (resource == null)
            return;
        mapResources.remove(resource);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void setTags(List<String> tags) {
        if (tags == null)
            return;
        this.tags = new ArrayList<>(tags.size());
        for (String s : tags)
            addTag(s);
    }

    public void addTag(String tag) {
        if (tag == null)
            return;
        if (!tags.contains(tag) && Arrays.asList(VALID_TAGS).contains(tag))
            tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public String getMoreInfoURL() {
        return moreInfoURL;
    }

    public void setMoreInfoURL(String moreInfoURL) {
        if (moreInfoURL != null)
            this.moreInfoURL = moreInfoURL;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(SPEED_MODIFIER_MIN, Math.min(SPEED_MODIFIER_MAX, speed));
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = Math.max(LIVES_MIN, Math.min(LIVES_MAX, lives));
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(String musicFile) {
        if (musicFile != null)
            this.musicFile = musicFile;
    }

    public float getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(float musicTime) {
        this.musicTime = musicTime;
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        if (iconFile != null)
            this.iconFile = iconFile;
    }

    public Image getIconImage() {
        return iconImage;
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }

    public int getGenerationType() {
        return generationType;
    }

    public void setGenerationType(int generationType) {
        this.generationType = generationType;
    }

    public int getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(int environmentType) {
        if (environmentType >= ENV_MIN && environmentType <= ENV_MAX) {
            this.environmentType = environmentType;
        }

    }

    public List<Checkpoint> getCheckpoints() {
        return Collections.unmodifiableList(new ArrayList<>(checkpoints.values()));
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        if (checkpoints != null) {
            this.checkpoints.clear();
            for (Checkpoint c : checkpoints)
                addCheckpoint(c);
        }
    }

    public List<Checkpoint> getCheckpointsBetweenTimes(float t1, float t2) {
        List<Checkpoint> checkpoints = new ArrayList<>();
        if (t2 < t1) {
            float temp = t2;
            t2 = t1;
            t1 = temp;
        }
        for (Checkpoint c : this.checkpoints.values()) {
            if (c.getTime() > t1 && c.getTime() < t2)
                checkpoints.add(c);
        }
        return checkpoints;
    }

    public List<Float> getCheckpointTimesBetweenTimes(float t1, float t2) {
        List<Float> checkpoints = new ArrayList<>();
        if (t2 < t1) {
            float temp = t2;
            t2 = t1;
            t1 = temp;
        }
        for (Checkpoint c : this.checkpoints.values()) {
            if (c.getTime() > t1 && c.getTime() < t2)
                checkpoints.add(c.getTime());
        }
        return checkpoints;
    }

    public void repositionCheckpoint(Checkpoint checkpoint) {
        checkpoints.values().remove(checkpoint);
        checkpoints.put(checkpoint.getTime(), checkpoint);
    }

    public Checkpoint getClosestCheckpoint(float time) {
        if (checkpoints.size() == 0)
            return null;
        List<Checkpoint> lis = new ArrayList<>(checkpoints.values());
        float closestVal = Math.abs(lis.get(0).getTime() - time);
        Checkpoint targ = lis.get(0);
        for (Checkpoint c : lis)
            if (closestVal > Math.abs(c.getTime() - time)) {
                closestVal = Math.abs(c.getTime() - time);
                targ = c;
            } else
                return targ;
        return targ;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.put(checkpoint.getTime(), checkpoint);
        checkpoint.setParent(this);
    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        checkpoints.values().remove(checkpoint);
        checkpoint.setParent(null);

    }

    public void addLoadedEvent(MapEvent evt) {
        if (evt == null)
            return;
        if (eventSet.contains(evt))
            return;
        registerImportedEvent(evt);
        boolean useEvent = true;
        if (evt.isMetaEvent()) {
            List<MapEvent> metaChildren = eventsByMetaId.get(evt.getMetaId());
            if (metaChildren != null)
                for (MapEvent e : metaChildren) {
                    evt.addMetaChild(e);
                    eventSet.remove(e);
                    eventList.remove(e);
                    e.setParent(null);
                }
            metaEventsByMetaId.put(evt.getMetaId(), evt);
            eventsByMetaId.remove(evt.getMetaId());
        } else {
            MapEvent metaEvent = metaEventsByMetaId.get(evt.getMetaId());
            if (metaEvent != null) {
                useEvent = false;
                metaEvent.addMetaChild(evt);
            } else {
                if (evt instanceof MapEndEvent)
                    if (onlyEndEvent != null)
                        return;
                    else
                        onlyEndEvent = evt;
                if (!eventsByMetaId.containsKey(evt.getMetaId()))
                    eventsByMetaId.put(evt.getMetaId(), new ArrayList<>());
                eventsByMetaId.get(evt.getMetaId()).add(evt);
            }
        }
        if (useEvent) {
            TimingProperty tp = evt.getTimingProperty();
            if (tp != null)
                resyncExistingTimedEventsForTimingEvent(evt);
            TimedEventProperty tep = evt.getTimedEventProperty();
            if (tep != null)
                tep.refresh();
            eventSet.add(evt);
            eventList.clear();
            eventList.addAll(eventSet);

            evt.setParent(this);
        }
    }

    public void addNewEvent(MapEvent evt) {
        if (evt == null)
            return;
        if (eventSet.contains(evt))
            return;
        if (evt instanceof MapEndEvent) {
            if (onlyEndEvent != null)
                return;
            else
                onlyEndEvent = evt;
        }
        registerEvent(evt);
        evt.setParent(this);
        eventSet.add(evt);
        eventList.clear();
        eventList.addAll(eventSet);
    }

    public void removeEvent(MapEvent evt) {
        if (evt != null) {
            evt.setParent(null);
            eventSet.remove(evt);
            eventList.remove(evt);
        }
    }

    public boolean containsEvent(MapEvent evt) {
        return eventSet.contains(evt);
    }

    public int indexOf(MapEvent evt) {
        return eventList.indexOf(evt);
    }

    public void repositionEvent(MapEvent evt, float time) {
        if (evt == null)
            return;
        if (eventSet.remove(evt)) {
            evt.internalSetTime(time);
            eventSet.add(evt);
            eventList.clear();
            eventList.addAll(eventSet);
        }
    }

    public MapEvent getClosestEvent(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList();
        float closestValue = Math.abs(evtList.get(0).getTime() - time);
        MapEvent target = evtList.get(0);
        for (int i = 1; i < evtList.size(); i++) {
            MapEvent evt = evtList.get(i);
            if (closestValue > Math.abs(evt.getTime() - time)) {
                closestValue = Math.abs(evt.getTime() - time);
                target = evt;
            } else
                return target;
        }
        return target;
    }

    public MapEvent getClosestEventRight(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList();
        MapEvent target = null;
        for (int i = evtList.size() - 1; i >= 0; i--) {
            MapEvent evt = evtList.get(i);
            if (evt.getTime() > time)
                target = evt;
            else
                return target;
        }
        return target;
    }

    public MapEvent getClosestEventLeft(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList();
        MapEvent target = null;
        for (MapEvent evt : evtList) {
            if (evt.getTime() <= time)
                target = evt;
            else
                return target;
        }
        return target;
    }

    public MapEvent getClosestEventRight(float time, MapEvent.Type type) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(type);
        MapEvent target = null;
        for (int i = evtList.size() - 1; i >= 0; i--) {
            MapEvent evt = evtList.get(i);
            if (evt.getTime() > time)
                target = evt;
            else
                return target;
        }
        return target;
    }

    public MapEvent getClosestEventLeft(float time, MapEvent.Type type) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(type);
        MapEvent target = null;
        for (MapEvent evt : evtList) {
            if (evt.getTime() <= time)
                target = evt;
            else
                return target;
        }
        return target;
    }

    public int getClosestEventOffsetRight(float time) {
        if (eventList.size() == 0)
            return -1;
        List<MapEvent> evtList = getEventList();
        int target = -1;
        for (int i = evtList.size() - 1; i >= 0; i--) {
            MapEvent evt = evtList.get(i);
            if (evt.getTime() > time)
                target = i;
            else
                return target;
        }
        return target;
    }

    public int getClosestEventOffsetLeft(float time) {
        if (eventList.size() == 0)
            return -1;
        List<MapEvent> evtList = getEventList();
        int target = -1;
        for (int i = 0; i < evtList.size(); i++) {
            MapEvent evt = evtList.get(i);
            if (evt.getTime() <= time)
                target = i;
            else
                return target;
        }
        return target;
    }

    public MapEvent getClosestEvent(float time, MapEvent.Type eventType) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(eventType);
        if (evtList.size() == 0)
            return null;
        MapEvent targ = evtList.get(0);
        float closestVal = Math.abs(targ.getTime() - time);
        for (int i = 1; i < evtList.size(); i++) {
            MapEvent evt = evtList.get(i);
            if (closestVal > Math.abs(evt.getTime() - time)) {
                closestVal = Math.abs(evt.getTime() - time);
                targ = evt;
            } else
                return targ;
        }
        return targ;
    }

    public MapEvent getClosestTimingRootTimeHandle(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(MapEvent.Type.TIMING);
        if (evtList.size() == 0)
            return null;
        float closestVal = Math.abs(evtList.get(0).getTimingProperty().getRootTime() - time);
        MapEvent targ = evtList.get(0);
        for (int i = 1; i < evtList.size(); i++) {
            MapEvent evt = evtList.get(i);
            if (closestVal > Math.abs(evt.getTimingProperty().getRootTime() - time)) {
                closestVal = Math.abs(evt.getTimingProperty().getRootTime() - time);
                targ = evt;
            } else
                return targ;
        }
        return targ;
    }

    public MapEvent getClosestTimingLengthHandle(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(MapEvent.Type.TIMING);
        if (evtList.size() == 0)
            return null;
        float closestVal = Math.abs(evtList.get(0).getTime() + evtList.get(0).getTimingProperty().getLength() - time);
        MapEvent targ = evtList.get(0);
        for (int i = 1; i < evtList.size(); i++) {
            MapEvent evt = evtList.get(i);
            if (closestVal > Math.abs(evt.getTime() + evt.getTimingProperty().getLength() - time)) {
                closestVal = Math.abs(evt.getTime() + evt.getTimingProperty().getLength() - time);
                targ = evt;
            } else
                return targ;
        }
        return targ;
    }

    public MapEvent getFirstTimingEventForTime(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = getEventList(MapEvent.Type.TIMING);
        if (evtList.size() == 0)
            return null;
        for (MapEvent evt : evtList)
            if (evt.getTime() <= time && evt.getTime() + evt.getTimingProperty().getLength() >= time)
                return evt;
        return null;
    }

    public List<MapEvent> getEventsBetweenTimes(float t1, float t2, boolean includeTimingLength) {
        List<MapEvent> out = new ArrayList<>();
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        List<MapEvent> evtList = getEventList();
        for (MapEvent e : evtList) {

            float time = e.getTime();
            TimingProperty tp = e.getTimingProperty();
            if (includeTimingLength && tp != null) {
                if (time < t2 && t1 < time + tp.getLength())
                    out.add(e);
            } else if (time >= t1 && time <= t2)
                out.add(e);
        }
        return out;
    }

    public List<MapEvent> getEventsBetweenTimes(float t1, float t2, boolean includeTimingLength, MapEvent.Type type) {
        List<MapEvent> out = new ArrayList<>();
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        List<MapEvent> evtList = getEventList();
        for (MapEvent e : evtList) {
            float time = e.getTime();
            TimingProperty tp = e.getTimingProperty();
            if (includeTimingLength && tp != null) {
                if (e.getType() == type && time < t2 && t1 < time + tp.getLength())
                    out.add(e);
            } else if (e.getType() == type && time >= t1 && time <= t2)
                out.add(e);
        }
        return out;
    }

    public List<MapEvent> getTimedEventsForTimingEvent(MapEvent event) {
        if (event == null)
            return null;
        List<MapEvent> out = new ArrayList<>();
        List<MapEvent> evtList = getEventList();
        int eventId = event.getMetaId();
        for (MapEvent e : evtList) {
            TimedEventProperty prop = e.getTimedEventProperty();
            if (prop != null && prop.getTimingEventId() == eventId)
                out.add(e);
        }
        return out;
    }

    public void displaceTimingEvent(MapEvent event, float delta) {
        if (event == null)
            return;
        event.setTimeRaw(event.getTime() + delta);
        for (MapEvent evt : getTimedEventsForTimingEvent(event)) {
            evt.setTimeRaw(evt.getTime() + delta);
        }
    }

    public void setTimingEventTime(MapEvent event, float time) {
        if (event == null)
            return;
        float delta = time - event.getTime();
        displaceTimingEvent(event, delta);
    }

    public List<MapEvent> getTimedEventsBetweenTimes(float t1, float t2) {
        List<MapEvent> out = new ArrayList<>();
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        List<MapEvent> evtList = getEventList();
        for (MapEvent e : evtList) {
            float time = e.getTime();
            if (time >= t1 && time <= t2 && e.getTimedEventProperty() != null)
                out.add(e);
        }
        return out;
    }

    public List<MapEvent> getEventList() {
        return Collections.unmodifiableList(eventList);
    }

    public int getEventCount() {
        return eventList.size();
    }

    public List<MapEvent> getEventList(MapEvent.Type type) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> out = new ArrayList<>();
        for (MapEvent f : eventList)
            if (f.getType() == type)
                out.add(f);
        return out;
    }

    public MapData clone() {
        MapData out = new MapData();
        out.id = id;
        out.name = name;
        out.info = info;
        for (MapResource res : mapResources)
            out.addMapResource(res);
        Collections.copy(out.tags, tags);
        out.moreInfoURL = moreInfoURL;
        out.speed = speed;
        out.lives = lives;
        out.musicFile = musicFile;
        out.musicTime = musicTime;
        out.iconFile = iconFile;
        out.generationType = generationType;
        out.environmentType = environmentType;
        for (Checkpoint c : checkpoints.values())
            out.checkpoints.put(c.getTime(), c);
        for (MapEvent l : eventList)
            out.addLoadedEvent(l.clone());
        return out;
    }

    private int grabMetaId() {
        return nextMetaId++;
    }

    public TimingEvent getTimingEventById(int id) {
        List<MapEvent> evts;
        evts = getEventList(MapEvent.Type.TIMING);
        for (MapEvent evt : evts)
            if (evt.getMetaId() == id && evt.getTimingProperty() != null)
                return (TimingEvent) evt;
        return null;
    }

    private void registerImportedEvent(MapEvent event) {
        if (event == null)
            return;
        if (event.getMetaId() == MapEvent.META_ID_DEF)
            event.setMetaId(grabMetaId());
        nextMetaId = Math.max(nextMetaId - 1, event.getMetaId()) + 1;
    }

    private void registerEvent(MapEvent event) {
        if (event != null)
            event.setMetaId(grabMetaId());
    }

    public void assignTimedEventsForTimingEvent(MapEvent event) {
        if (event == null)
            return;
        clearTimedEventsForTimingEvent(event);
        TimingProperty prop = event.getTimingProperty();
        int id = event.getMetaId();
        List<MapEvent> events = getTimedEventsBetweenTimes(event.getTime(), event.getTime() + prop.getLength());
        for (MapEvent evt : events)
            evt.getTimedEventProperty().setTimingEventId(id);
    }

    public void resyncExistingTimedEventsForTimingEvent(MapEvent timingEvent) {
        for (MapEvent event : getTimedEventsForTimingEvent(timingEvent))
            event.getTimedEventProperty().refresh();
    }

    public void clearTimedEventsForTimingEvent(MapEvent event) {
        if (event == null)
            return;
        List<MapEvent> events = getTimedEventsForTimingEvent(event);
        for (MapEvent evt : events)
            evt.getTimedEventProperty().clearTimingEventId();
    }

}
