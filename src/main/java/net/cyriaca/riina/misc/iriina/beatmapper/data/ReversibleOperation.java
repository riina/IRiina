package net.cyriaca.riina.misc.iriina.beatmapper.data;

import net.cyriaca.riina.misc.iriina.intralism.data.Checkpoint;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;

import java.util.ArrayList;
import java.util.List;

public class ReversibleOperation {

    private Type type = Type.EVENT_MOVE;
    private MapResource oldRes = null;
    private MapResource res = null;
    private MapEvent event = null;
    private List<MapEvent> eventList = null;
    private List<Float> timeList = null;
    private Checkpoint checkpoint = null;
    private float time = -1.0f;
    private float length = -1.0f;
    private float rootTime = -1.0f;
    private String oldData = null;
    private String oldExtraData = null;
    private String data = null;
    private String extraData = null;
    private List<String> oldDataList = null;
    private List<String> oldExtraDataList = null;
    private List<String> dataList = null;
    private List<String> extraDataList = null;
    private float oldTime = -1.0f;
    private List<Float> oldTimeList = null;
    private List<Integer> timedMetaIds = null;
    private int timedMetaId = -1;

    public ReversibleOperation(){
    }

    public static ReversibleOperation createResourceModOperation(MapResource oldRes, MapResource res) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.RESOURCE_MOD;
        out.oldRes = oldRes;
        out.res = res;
        return out;
    }

    public static ReversibleOperation createResourceAddOperation(MapResource res) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.RESOURCE_ADD;
        out.res = res;
        return out;
    }

    public static ReversibleOperation createResourceDeleteOperation(MapResource res) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.RESOURCE_DELETE;
        out.res = res;
        return out;
    }

    public static ReversibleOperation createEventCreateOperation(MapEvent event) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_CREATE;
        out.event = event;
        return out;
    }

    public static ReversibleOperation createEventDeleteOperation(MapEvent event) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_DELETE;
        out.event = event;
        return out;
    }

    public static ReversibleOperation createEventMoveOperation(MapEvent event, int timedMetaId, float time) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_MOVE;
        out.timedMetaId = timedMetaId;
        out.event = event;
        out.time = time;
        return out;
    }

    public static ReversibleOperation createTimingEventLengthModOperation(MapEvent event, float length) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.TIMING_EVENT_LENGTH_MOD;
        out.event = event;
        out.length = length;
        return out;
    }

    public static ReversibleOperation createTimingEventRootTimeModOperation(MapEvent event, float rootTime) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.TIMING_EVENT_ROOT_TIME_MOD;
        out.event = event;
        out.rootTime = rootTime;
        return out;
    }

    public static ReversibleOperation createEventGroupCreateOperation(List<MapEvent> eventList) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_GROUP_CREATE;
        out.eventList = new ArrayList<>(eventList);
        return out;
    }

    public static ReversibleOperation createEventGroupDeleteOperation(List<MapEvent> eventList) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_GROUP_DELETE;
        out.eventList = new ArrayList<>(eventList);
        return out;
    }

    public static ReversibleOperation createEventGroupMoveOperation(List<MapEvent> eventList, List<Integer> timedMetaIds, List<Float> timeList) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_GROUP_MOVE;
        out.timedMetaIds = new ArrayList<>(timedMetaIds);
        out.eventList = new ArrayList<>(eventList);
        out.timeList = new ArrayList<>(timeList);
        return out;
    }

    public static ReversibleOperation createCheckpointCreateOperation(Checkpoint checkpoint) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.CHECKPOINT_CREATE;
        out.checkpoint = checkpoint;
        return out;
    }

    public static ReversibleOperation createCheckpointDeleteOperation(Checkpoint checkpoint) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.CHECKPOINT_DELETE;
        out.checkpoint = checkpoint;
        return out;
    }

    public static ReversibleOperation createCheckpointMoveOperation(Checkpoint checkpoint, float time) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.CHECKPOINT_MOVE;
        out.checkpoint = checkpoint;
        out.time = time;
        return out;
    }

    public static ReversibleOperation createEventDataModOperation(MapEvent event, String oldData, String oldExtraData, float oldTime, String data, String extraData, float time) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_DATA_MOD;
        out.event = event;
        out.oldData = oldData;
        out.oldExtraData = oldExtraData;
        out.oldTime = oldTime;
        out.data = data;
        out.extraData = extraData;
        out.time = time;
        return out;
    }

    public static ReversibleOperation createEventGroupDataModOperation(List<MapEvent> eventList, List<String> oldDataList, List<String> oldExtraDataList, List<Float> oldTimeList, List<String> dataList, List<String> extraDataList, List<Float> timeList) {
        ReversibleOperation out = new ReversibleOperation();
        out.type = Type.EVENT_GROUP_DATA_MOD;
        out.eventList = new ArrayList<>(eventList);
        out.oldDataList = new ArrayList<>(oldDataList);
        out.oldExtraDataList = new ArrayList<>(oldExtraDataList);
        out.oldTimeList = new ArrayList<>(oldTimeList);
        out.dataList = new ArrayList<>(dataList);
        out.extraDataList = new ArrayList<>(extraDataList);
        out.timeList = new ArrayList<>(timeList);
        return out;
    }

    public Type getType() {
        return type;
    }

    public MapResource getOldRes() {
        return oldRes;
    }

    public MapResource getRes() {
        return res;
    }

    public MapEvent getEvent() {
        return event;
    }

    public List<MapEvent> getEventList() {
        return eventList;
    }

    public List<Float> getTimeList() {
        return timeList;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public float getTime() {
        return time;
    }

    public float getLength() {
        return length;
    }

    public float getRootTime() {
        return rootTime;
    }

    public String getOldData() {
        return oldData;
    }

    public String getOldExtraData() {
        return oldExtraData;
    }

    public String getData() {
        return data;
    }

    public String getExtraData() {
        return extraData;
    }

    public List<String> getOldDataList() {
        return oldDataList;
    }

    public List<String> getOldExtraDataList() {
        return oldExtraDataList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public List<String> getExtraDataList() {
        return extraDataList;
    }

    public List<Float> getOldTimeList() {
        return oldTimeList;
    }

    public List<Integer> getTimedMetaIds() {
        return timedMetaIds;
    }

    public int getTimedMetaId() {
        return timedMetaId;
    }

    public float getOldTime() {
        return oldTime;
    }

    public enum Type {
        RESOURCE_MOD,
        RESOURCE_ADD,
        RESOURCE_DELETE,
        EVENT_CREATE,
        EVENT_DELETE,
        EVENT_MOVE,
        TIMING_EVENT_LENGTH_MOD,
        TIMING_EVENT_ROOT_TIME_MOD,
        EVENT_GROUP_CREATE,
        EVENT_GROUP_DELETE,
        EVENT_GROUP_MOVE,
        CHECKPOINT_CREATE,
        CHECKPOINT_DELETE,
        CHECKPOINT_MOVE,
        EVENT_DATA_MOD,
        EVENT_GROUP_DATA_MOD
    }
}
