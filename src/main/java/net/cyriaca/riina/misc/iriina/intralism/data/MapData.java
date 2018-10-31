package net.cyriaca.riina.misc.iriina.intralism.data;

import org.tritonus.share.ArraySet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Function;

/*
 * Represents a map configuration
 */
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
    private float speed;
    private int lives;
    private String musicFile;
    private float musicTime;
    private String iconFile;
    private Image iconImage;
    private int generationType;
    private int environmentType;

    private Map<Float, Checkpoint> checkpoints;
    private Set<MapEvent> eventSet;
    private List<MapEvent> eventList;

    private Map<Integer, List<MapEvent>> eventsByMetaId;
    private Map<Integer, MapEvent> metaEventsByMetaId;
    private Map<Integer, MapEvent> timingEventsByMetaId;
    private Map<MapEvent.Type, Set<MapEvent>> eventSetByType;
    private Map<MapEvent.Type, List<MapEvent>> eventListByType;
    private Map<MapEvent, Integer> offsetsByEvent;
    private Set<MapEvent> timedEventSet;
    private List<MapEvent> timedEventList;
    private MapEvent onlyEndEvent;
    private int nextMetaId;
    private float checkpointTimingOffset;
    private float eventTimingOffset;

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
        timingEventsByMetaId = new TreeMap<>();
        eventSetByType = new TreeMap<>();
        eventListByType = new TreeMap<>();
        offsetsByEvent = new TreeMap<>();
        timedEventSet = new ArraySet<>();
        timedEventList = new ArrayList<>();
        onlyEndEvent = null;
        nextMetaId = 0;
        checkpointTimingOffset = 0.0f;
        eventTimingOffset = 0.0f;
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
                    Set<MapEvent> ts = eventSetByType.get(e.getType());
                    if (ts != null) {
                        ts.remove(e);
                        List<MapEvent> tl = eventListByType.get(evt.getType());
                        tl.clear();
                        tl.addAll(ts);
                    }
                    if (e.getTimedEventProperty() != null) {
                        timedEventSet.remove(e);
                        timedEventList.clear();
                        timedEventList.addAll(timedEventSet);
                    }
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
            if (tp != null) {
                resyncExistingTimedEventsForTimingEvent(evt);
                timingEventsByMetaId.put(evt.getMetaId(), evt);
            }
            TimedEventProperty tep = evt.getTimedEventProperty();
            if (tep != null)
                tep.refresh();
            eventSet.add(evt);
            eventList.clear();
            eventList.addAll(eventSet);
            Set<MapEvent> ts = eventSetByType.computeIfAbsent(evt.getType(), k -> new TreeSet<>());
            ts.add(evt);
            List<MapEvent> tl = eventListByType.get(evt.getType());
            if (tl == null) {
                tl = new ArrayList<>();
                eventListByType.put(evt.getType(), tl);
            } else
                tl.clear();
            tl.addAll(ts);
            if (evt.getTimedEventProperty() != null) {
                timedEventSet.add(evt);
                timedEventList.clear();
                timedEventList.addAll(timedEventSet);
            }

            evt.setParent(this);
        }
        offsetsByEvent.clear();
        for (int i = 0; i < eventList.size(); i++)
            offsetsByEvent.put(eventList.get(i), i);
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
        if (evt.getTimingProperty() != null)
            timingEventsByMetaId.put(evt.getMetaId(), evt);
        Set<MapEvent> ts = eventSetByType.computeIfAbsent(evt.getType(), k -> new TreeSet<>());
        ts.add(evt);
        List<MapEvent> tl = eventListByType.get(evt.getType());
        if (tl == null) {
            tl = new ArrayList<>();
            eventListByType.put(evt.getType(), tl);
        } else
            tl.clear();
        tl.addAll(ts);
        if (evt.getTimedEventProperty() != null) {
            timedEventSet.remove(evt);
            timedEventSet.add(evt);
            timedEventList.clear();
            timedEventList.addAll(timedEventSet);
        }
        offsetsByEvent.clear();
        for (int i = 0; i < eventList.size(); i++)
            offsetsByEvent.put(eventList.get(i), i);
    }

    public void removeEvent(MapEvent evt) {
        if (evt != null) {
            evt.setParent(null);
            eventSet.remove(evt);
            eventList.remove(evt);
            List<MapEvent> li = eventsByMetaId.get(evt.getMetaId());
            if (li != null)
                li.remove(evt);
            timingEventsByMetaId.remove(evt.getMetaId());
            metaEventsByMetaId.remove(evt.getMetaId());
            Set<MapEvent> ts = eventSetByType.get(evt.getType());
            if (ts != null)
                ts.remove(evt);
            List<MapEvent> tl = eventListByType.get(evt.getType());
            if (tl != null)
                tl.remove(evt);
            if (evt.getTimedEventProperty() != null) {
                timedEventSet.remove(evt);
                timedEventList.clear();
                timedEventList.addAll(timedEventSet);
            }
            offsetsByEvent.clear();
            for (int i = 0; i < eventList.size(); i++)
                offsetsByEvent.put(eventList.get(i), i);
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
            Set<MapEvent> ts = eventSetByType.get(evt.getType());
            ts.remove(evt);
            if (evt.getTimedEventProperty() != null) {
                timedEventSet.remove(evt);
            }
            evt.internalSetTime(time);
            eventSet.add(evt);
            eventList.clear();
            eventList.addAll(eventSet);
            ts.add(evt);
            List<MapEvent> tl = eventListByType.get(evt.getType());
            tl.clear();
            tl.addAll(ts);
            if (evt.getTimedEventProperty() != null) {
                timedEventSet.add(evt);
                timedEventList.clear();
                timedEventList.addAll(timedEventSet);
            }
            offsetsByEvent.clear();
            for (int i = 0; i < eventList.size(); i++)
                offsetsByEvent.put(eventList.get(i), i);
        }
    }

    public void repositionEventStart(MapEvent evt, float time) {
        if (evt == null)
            return;
        if (eventSet.remove(evt)) {
            Set<MapEvent> ts = eventSetByType.get(evt.getType());
            ts.remove(evt);
            if (evt.getTimedEventProperty() != null) {
                timedEventSet.remove(evt);
            }
            evt.internalSetTime(time);
        }
    }

    public void repositionEventsFinalize(List<MapEvent> evts) {
        for (MapEvent evt : evts) {
            eventSet.add(evt);
            Set<MapEvent> ts = eventSetByType.get(evt.getType());
            ts.add(evt);
            if (evt.getTimedEventProperty() != null)
                timedEventSet.add(evt);
        }
        for (MapEvent.Type t : MapEvent.Type.values()) {
            Set<MapEvent> ts = eventSetByType.get(t);
            if (ts != null) {
                List<MapEvent> tl = eventListByType.get(t);
                tl.clear();
                tl.addAll(ts);
            }
        }
        eventList.clear();
        eventList.addAll(eventSet);
        timedEventList.clear();
        timedEventList.addAll(timedEventSet);
        offsetsByEvent.clear();
        for (int i = 0; i < eventList.size(); i++)
            offsetsByEvent.put(eventList.get(i), i);
    }

    public MapEvent getClosestEvent(float time) {
        return searchEventsUseType(time, 0, null, MapEvent::getTime, true);
    }

    public MapEvent getClosestEventRight(float time) {
        return searchEventsUseType(time, 1, null, MapEvent::getTime, true);
    }

    public MapEvent getClosestEventLeft(float time) {
        return searchEventsUseType(time, -1, null, MapEvent::getTime, true);
    }

    public MapEvent getClosestEventRight(float time, MapEvent.Type type) {
        return searchEventsUseType(time, 1, type, MapEvent::getTime, true);
    }

    public MapEvent getClosestEventLeft(float time, MapEvent.Type type) {
        return searchEventsUseType(time, -1, type, MapEvent::getTime, true);
    }

    public int getClosestEventOffsetRight(float time) {
        return offsetsByEvent.get(searchEventsUseType(time, 1, null, MapEvent::getTime, true));
    }

    public int getClosestEventOffsetLeft(float time) {
        return offsetsByEvent.get(searchEventsUseType(time, -1, null, MapEvent::getTime, true));
    }

    public MapEvent getClosestEvent(float time, MapEvent.Type eventType) {
        return searchEventsUseType(time, 0, eventType, MapEvent::getTime, true);
    }

    public MapEvent getClosestTimingRootTimeHandle(float time) {
        return searchTimingEventRootTimeHandle(time, 0);
    }

    public MapEvent getClosestTimingLengthHandle(float time) {
        return searchTimingEventLengthHandle(time, 0);
    }

    public MapEvent getFirstTimingEventForTime(float time) {
        if (eventList.size() == 0)
            return null;
        List<MapEvent> evtList = eventListByType.get(MapEvent.Type.TIMING);
        if (evtList == null || evtList.size() == 0)
            return null;
        for (MapEvent evt : evtList)
            if (evt.getTime() <= time && evt.getTime() + evt.getTimingProperty().getLength() >= time)
                return evt;
        return null;
    }

    public List<MapEvent> getEventsBetweenTimes(float t1, float t2, boolean includeTimingLength) {
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        Set<MapEvent> evtSet = new TreeSet<>();
        int off1 = searchEventsByOffset(eventList, t1, 1, MapEvent::getTime, true);
        int off2 = searchEventsByOffset(eventList, t2, -1, MapEvent::getTime, true);
        if (includeTimingLength) {
            List<MapEvent> timingList = eventListByType.get(MapEvent.Type.TIMING);
            if (timingList != null)
                for (MapEvent evt : timingList) {
                    float time = evt.getTime();
                    if (time < t2 && t1 < time + evt.getTimingProperty().getLength())
                        evtSet.add(evt);
                }
        }
        if (off2 < off1 || off1 == -1 || off2 == -1)
            return new ArrayList<>(evtSet);
        for (int i = off1; i <= off2; i++) {
            evtSet.add(eventList.get(i));
        }
        return new ArrayList<>(evtSet);
    }

    public List<MapEvent> getEventsBetweenTimes(float t1, float t2, boolean includeTimingLength, MapEvent.Type type) {
        List<MapEvent> out = new ArrayList<>();
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        if (type == MapEvent.Type.TIMING && includeTimingLength) {
            List<MapEvent> timingList = eventListByType.get(MapEvent.Type.TIMING);
            if (timingList != null)
                for (MapEvent evt : timingList) {
                    float time = evt.getTime();
                    if (time < t2 && t1 < time + evt.getTimingProperty().getLength())
                        out.add(evt);
                }
        } else {
            List<MapEvent> list = eventListByType.get(type);
            if (list == null)
                return out;
            int off1 = searchEventsByOffset(list, t1, 1, MapEvent::getTime, true);
            int off2 = searchEventsByOffset(list, t2, -1, MapEvent::getTime, true);
            if (off2 < off1 || off1 == -1 || off2 == -1)
                return out;
            for (int i = off1; i <= off2; i++) {
                out.add(list.get(i));
            }
        }
        return out;
    }

    public List<MapEvent> getTimedEventsForTimingEvent(MapEvent event) {
        List<MapEvent> out = new ArrayList<>();
        if (event == null)
            return out;
        int eventId = event.getMetaId();
        for (MapEvent e : eventList) {
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
        if (timedEventList.size() == 0)
            return out;
        if (t1 > t2) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        int off1 = searchEventsByOffset(timedEventList, t1, 1, MapEvent::getTime, true);
        int off2 = searchEventsByOffset(timedEventList, t2, -1, MapEvent::getTime, true);
        for (int i = off1; i <= off2; i++)
            out.add(timedEventList.get(i));
        return out;
    }

    public List<MapEvent> getEventList() {
        return Collections.unmodifiableList(eventList);
    }

    public int getEventCount() {
        return eventList.size();
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
        return (TimingEvent) timingEventsByMetaId.get(id);
    }

    private int searchEventsByOffset(List<MapEvent> list, float time, int dir, Function<MapEvent, Float> operator, boolean sorted) {
        if (list == null || list.size() == 0)
            return -1;
        if (sorted)
            switch (dir) {
                case -1:
                    MapEvent e0 = list.get(0);
                    if (time < operator.apply(e0)) {
                        return -1;
                    }
                    MapEvent ex = list.get(list.size() - 1);
                    if (time > operator.apply(ex)) {
                        return list.size() - 1;
                    }
                    int lo = 0;
                    int hi = list.size() - 1;
                    while (lo <= hi) {
                        int mid = (hi + lo) / 2;
                        MapEvent evtMid = list.get(mid);
                        if (time < operator.apply(evtMid)) {
                            hi = mid - 1;
                        } else if (time > operator.apply(evtMid)) {
                            lo = mid + 1;
                        } else {
                            return mid;
                        }
                    }
                    return hi;
                case 0:
                    e0 = list.get(0);
                    if (time < operator.apply(e0)) {
                        return 0;
                    }
                    ex = list.get(list.size() - 1);
                    if (time > operator.apply(ex)) {
                        return list.size() - 1;
                    }
                    lo = 0;
                    hi = list.size() - 1;
                    while (lo <= hi) {
                        int mid = (hi + lo) / 2;
                        MapEvent evtMid = list.get(mid);
                        if (time < operator.apply(evtMid)) {
                            hi = mid - 1;
                        } else if (time > operator.apply(evtMid)) {
                            lo = mid + 1;
                        } else {
                            return mid;
                        }
                    }
                    MapEvent left = list.get(lo);
                    MapEvent right = list.get(hi);
                    return (operator.apply(left) - time) < (time - operator.apply(right)) ? lo : hi;
                case 1:
                    e0 = list.get(0);
                    if (time < operator.apply(e0)) {
                        return 0;
                    }
                    ex = list.get(list.size() - 1);
                    if (time > operator.apply(ex)) {
                        return -1;
                    }
                    lo = 0;
                    hi = list.size() - 1;
                    while (lo <= hi) {
                        int mid = (hi + lo) / 2;
                        MapEvent evtMid = list.get(mid);
                        if (time < operator.apply(evtMid)) {
                            hi = mid - 1;
                        } else if (time > operator.apply(evtMid)) {
                            lo = mid + 1;
                        } else {
                            return mid;
                        }
                    }
                    return lo;
            }
        else
            switch (dir) {
                case -1:
                    boolean found = false;
                    float closestVal = -1.0f;
                    int closestOff = -1;
                    for (int i = 0; i < list.size(); i++) {
                        MapEvent evt = list.get(i);
                        if (!found) {
                            closestVal = operator.apply(evt) - time;
                            if (closestVal <= 0.0f) {
                                found = true;
                                closestOff = 0;
                            }
                        } else {
                            float val = operator.apply(evt) - time;
                            if (val <= 0.0f && closestVal < val) {
                                closestVal = val;
                                closestOff = i;
                            }
                        }
                    }
                    return closestOff;
                case 0:
                    closestVal = Math.abs(operator.apply(list.get(0)) - time);
                    closestOff = 0;
                    for (int i = 0; i < list.size(); i++) {
                        MapEvent evt = list.get(i);
                        float val = Math.abs(operator.apply(evt) - time);
                        if (closestVal > val) {
                            closestVal = val;
                            closestOff = i;
                        }
                    }
                    return closestOff;
                case 1:
                    found = false;
                    closestVal = -1.0f;
                    closestOff = -1;
                    for (int i = 0; i < list.size(); i++) {
                        MapEvent evt = list.get(i);
                        if (!found) {
                            closestVal = operator.apply(evt) - time;
                            if (closestVal >= 0.0f) {
                                found = true;
                                closestOff = 0;
                            }
                        } else {
                            float val = operator.apply(evt) - time;
                            if (val >= 0.0f && closestVal > val) {
                                closestVal = val;
                                closestOff = i;
                            }
                        }
                    }
                    return closestOff;
            }
        return -1;
    }

    private MapEvent searchTimingEventLengthHandle(float time, int dir) {
        List<MapEvent> list = eventListByType.get(MapEvent.Type.TIMING);
        int pos = searchEventsByOffset(list, time, dir, event -> event.getTime() + event.getTimingProperty().getLength(), false);
        return pos == -1 ? null : list.get(pos);
    }

    private MapEvent searchTimingEventRootTimeHandle(float time, int dir) {
        List<MapEvent> list = eventListByType.get(MapEvent.Type.TIMING);
        int pos = searchEventsByOffset(list, time, dir, event -> event.getTimingProperty().getRootTime(), false);
        return pos == -1 ? null : list.get(pos);
    }

    private MapEvent searchEventsUseList(List<MapEvent> list, float time, int dir, Function<MapEvent, Float> operator, boolean sorted) {
        int pos = searchEventsByOffset(list, time, dir, operator, sorted);
        return pos == -1 ? null : list.get(pos);
    }

    private MapEvent searchEventsUseType(float time, int dir, MapEvent.Type type, Function<MapEvent, Float> operator, boolean sorted) {
        List<MapEvent> list = type == null ? eventList : eventListByType.get(type);
        int pos = searchEventsByOffset(list, time, dir, operator, sorted);
        return pos == -1 ? null : list.get(pos);
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
        for (MapEvent evt : getTimedEventsBetweenTimes(event.getTime(), event.getTime() + prop.getLength()))
            evt.getTimedEventProperty().setTimingEventId(id);
    }

    public void resyncExistingTimedEventsForTimingEvent(MapEvent timingEvent) {
        for (MapEvent event : getTimedEventsForTimingEvent(timingEvent))
            event.getTimedEventProperty().refresh();
    }

    public void clearTimedEventsForTimingEvent(MapEvent event) {
        for (MapEvent evt : getTimedEventsForTimingEvent(event))
            evt.getTimedEventProperty().clearTimingEventId();
    }

    public float getCheckpointTimingOffset() {
        return checkpointTimingOffset;
    }

    public void setCheckpointTimingOffset(float checkpointTimingOffset) {
        this.checkpointTimingOffset = checkpointTimingOffset;
    }

    public float getEventTimingOffset() {
        return eventTimingOffset;
    }

    public void setEventTimingOffset(float eventTimingOffset) {
        this.eventTimingOffset = eventTimingOffset;
    }
}
