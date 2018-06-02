package net.cyriaca.riina.misc.iriina.intralism;

import net.cyriaca.riina.misc.iriina.intralism.data.*;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

    public static MapParseResult parseMap(JsonObject json) {
        if (json == null) {
            return MapParseResult.createFailureResult(MapParseResult.FailureSource.NO_JSON_DATA_PROVIDED);
        }
        MapData out = new MapData();
        try {
            out.setName(json.getString("name"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("name");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("name", MapParseResult.ValueType.STRING);
        }
        try {
            out.setId(json.getString("id"));
        } catch (NullPointerException | ClassCastException e) {
            out.setId(out.getName());
        }
        try {
            out.setInfo(json.getString("info"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("info");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("info", MapParseResult.ValueType.STRING);
        }
        JsonArray levelResourcesArr;
        try {
            levelResourcesArr = json.getJsonArray("levelResources");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("levelResources", MapParseResult.ValueType.ARRAY);
        }
        if (levelResourcesArr == null) {
            return MapParseResult.createMissingValueResult("levelResources");
        }
        List<MapResource> mapResources = new ArrayList<>(levelResourcesArr.size());
        for (int i = 0; i < levelResourcesArr.size(); i++) {
            JsonObject obj = levelResourcesArr.getJsonObject(i);
            String name;
            try {
                name = obj.getString("name");
            } catch (NullPointerException e) {
                return MapParseResult.createMissingValueResult("levelResources[" + i + "]>name");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("levelResources[" + i + "]>name", MapParseResult.ValueType.STRING);
            }
            String path;
            try {
                path = obj.getString("path");
            } catch (NullPointerException e) {
                return MapParseResult.createMissingValueResult("levelResources[" + i + "]>path");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("levelResources[" + i + "]>path", MapParseResult.ValueType.STRING);
            }
            MapResource r = new MapResource(name, path, null);
            mapResources.add(r);
        }
        out.setMapResources(mapResources);

        try {
            byte handCount = (byte) json.getInt("handCount");
            if (handCount != 1)
                return MapParseResult.createFailureResult(MapParseResult.FailureSource.MULTI_HAND_MAP);
        } catch (NullPointerException | ClassCastException ignored) {
        }
        JsonArray tagsArr;
        try {
            tagsArr = json.getJsonArray("tags");
        } catch (NullPointerException e) {
            return MapParseResult.createInvalidValueResult("tags", MapParseResult.ValueType.ARRAY);
        }
        if (tagsArr == null)
            return MapParseResult.createMissingValueResult("tags");
        for (int i = 0; i < tagsArr.size(); i++) {
            String tag;
            try {
                tag = tagsArr.getString(i);
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("tags[" + i + "]", MapParseResult.ValueType.STRING);
            }
            out.addTag(tag);
        }
        try {
            out.setMoreInfoURL(json.getString("moreInfoURL"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("moreInfoURL");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("moreInfoURL", MapParseResult.ValueType.STRING);
        }
        try {
            out.setSpeed((float) json.getJsonNumber("speed").doubleValue());
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("speed");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("speed", MapParseResult.ValueType.NUMBER);
        }
        try {
            out.setLives(json.getInt("lives"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("lives");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("lives", MapParseResult.ValueType.NUMBER);
        }
        try {
            out.setMusicFile(json.getString("musicFile"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("musicFile");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("musicFile", MapParseResult.ValueType.STRING);
        }
        try {
            out.setMusicTime((float) json.getJsonNumber("musicTime").doubleValue());
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("musicTime");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("musicTime", MapParseResult.ValueType.NUMBER);
        }
        try {
            out.setIconFile(json.getString("iconFile"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("iconFile");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("iconFile", MapParseResult.ValueType.STRING);
        }
        try {
            out.setGenerationType(json.getInt("generationType"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("generationType");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("generationType", MapParseResult.ValueType.NUMBER);
        }
        try {
            out.setEnvironmentType(json.getInt("environmentType"));
        } catch (NullPointerException e) {
            return MapParseResult.createMissingValueResult("environmentType");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("environmentType", MapParseResult.ValueType.NUMBER);
        }
        JsonArray checkpointsArr;
        try {
            checkpointsArr = json.getJsonArray("checkpoints");
        } catch (ClassCastException e) {
            return MapParseResult.createInvalidValueResult("checkpoints", MapParseResult.ValueType.ARRAY);
        }
        if (checkpointsArr == null)
            return MapParseResult.createMissingValueResult("checkpoints");
        List<Checkpoint> checkpoints = new ArrayList<>();
        for (int i = 0; i < checkpointsArr.size(); i++) {
            JsonNumber num;
            try {
                num = checkpointsArr.getJsonNumber(i);
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("checkpoints[" + i + "]", MapParseResult.ValueType.NUMBER);
            }
            checkpoints.add(new Checkpoint((float) num.doubleValue(), out));
        }
        out.setCheckpoints(checkpoints);
        JsonArray eventsArr = json.getJsonArray("events");
        for (int i = 0; i < eventsArr.size(); i++) {
            MapEvent evt = null;
            JsonObject evtJson;
            try {
                evtJson = eventsArr.getJsonObject(i);
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("events[" + i + "]", MapParseResult.ValueType.OBJECT);
            }
            JsonArray dataArr;
            try {
                dataArr = evtJson.getJsonArray("data");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("events[" + i + "]>data", MapParseResult.ValueType.ARRAY);
            }
            if (dataArr == null)
                return MapParseResult.createMissingValueResult("events[" + i + "]>data");
            String evtData;
            try {
                evtData = dataArr.getString(1);
            } catch (IndexOutOfBoundsException e) {
                return MapParseResult.createMissingArrayElementResult("events[" + i + "]>data[1]");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("events[" + i + "]>data[1]", MapParseResult.ValueType.STRING);
            }
            float time;
            try {
                time = (float) evtJson.getJsonNumber("time").doubleValue();
            } catch (NullPointerException e) {
                return MapParseResult.createMissingValueResult("events[" + i + "]>time");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("events[" + i + "]>time", MapParseResult.ValueType.NUMBER);
            }
            int metaId = MapEvent.META_ID_DEF;
            try {
                metaId = evtJson.getJsonNumber("metaId").intValueExact();
            } catch (NullPointerException | ClassCastException ignored) {
            }
            String eventName;
            try {
                eventName = dataArr.getString(0);
            } catch (IndexOutOfBoundsException e) {
                return MapParseResult.createMissingArrayElementResult("events[" + i + "]>data[0]");
            } catch (ClassCastException e) {
                return MapParseResult.createInvalidValueResult("events[" + i + "]>data[0]", MapParseResult.ValueType.STRING);
            }
            String extraData = null;
            try {
                extraData = evtJson.getString("extraData");
            } catch (NullPointerException | ClassCastException ignored) {
            }
            switch (eventName) {
                case "SpawnObj":
                    evt = new SpawnObjEvent(time);
                    break;
                case "SetBGColor":
                    evt = new SetBGColorEvent(time);
                    break;
                case "SetPlayerDistance":
                    evt = new SetPlayerDistanceEvent(time);
                    break;
                case "ShowTitle":
                    evt = new ShowTitleEvent(time);
                    break;
                case "ShowSprite":
                    evt = new ShowSpriteEvent(time);
                    break;
                case "MapEnd":
                    evt = new MapEndEvent(time);
                    break;
                case "Timing":
                    evt = new TimingEvent(time);
                    break;
                case "Combo":
                    evt = new ComboEvent(time);
                    break;
                default:
                    break;
            }
            if (evt != null)
                evt.setEventData(evtData);
            else {
                return MapParseResult.createUnrecognizedEventResult(eventName);
            }
            if (extraData != null)
                evt.setEventExtraData(extraData);
            evt.setMetaId(metaId);
            out.addLoadedEvent(evt);
        }
        return MapParseResult.createSuccessResult(out);
    }

    public static JsonObject exportMap(MapData mapData) {
        JsonBuilderFactory f = Json.createBuilderFactory(null);
        JsonArrayBuilder levelResourcesBuilder = f.createArrayBuilder();
        List<MapResource> mapResources = mapData.getMapResources();
        for (MapResource r : mapResources) {
            levelResourcesBuilder.add(f.createObjectBuilder().add("name", r.getName()).add("type", "Sprite")
                    .add("path", r.getPath()).build());
        }
        JsonArrayBuilder tagsBuilder = f.createArrayBuilder();
        List<String> tags = mapData.getTags();
        for (String tag : tags) {
            tagsBuilder.add(tag);
        }
        JsonArrayBuilder checkpointsBuilder = f.createArrayBuilder();
        List<Checkpoint> checkpoints = mapData.getCheckpoints();
        for (Checkpoint checkpoint : checkpoints) {
            checkpointsBuilder.add((double) checkpoint.getTime());
        }
        JsonArrayBuilder eventsBuilder = f.createArrayBuilder();
        List<MapEvent> events = mapData.getEventList();
        for (MapEvent evt : events) {
            JsonArrayBuilder eventDataBuilder = f.createArrayBuilder();
            String eventData = evt.getEventData();
            if (eventData == null)
                eventData = "";
            eventDataBuilder.add(evt.getEventName()).add(eventData);
            JsonObjectBuilder eventBuilder = f.createObjectBuilder().add("time", evt.getTime())
                    .add("data", eventDataBuilder.build()).add("metaId", evt.getMetaId());
            String eventExtraData = evt.getEventExtraData();
            if (eventExtraData != null)
                eventBuilder.add("extraData", eventExtraData);
            eventsBuilder.add(eventBuilder);
            List<MapEvent> metaChildren = evt.getMetaChildren();
            if (metaChildren != null) {
                for (MapEvent metaEvt : metaChildren) {
                    JsonArrayBuilder metaEventDataBuilder = f.createArrayBuilder();
                    String metaEventData = metaEvt.getEventData();
                    if (metaEventData == null)
                        metaEventData = "";
                    metaEventDataBuilder.add(metaEvt.getEventName()).add(metaEventData);
                    JsonObjectBuilder metaEventBuilder = f.createObjectBuilder().add("time", metaEvt.getTime())
                            .add("data", metaEventDataBuilder.build()).add("metaId", metaEvt.getMetaId());
                    String metaEventExtraData = metaEvt.getEventExtraData();
                    if (metaEventExtraData != null)
                        metaEventBuilder.add("extraData", metaEventExtraData);
                    eventsBuilder.add(metaEventBuilder);
                }
            }
        }
        return f.createObjectBuilder().add("id", mapData.getId()).add("name", mapData.getName())
                .add("info", mapData.getInfo()).add("levelResources", levelResourcesBuilder.build())
                .add("tags", tagsBuilder.build()).add("handCount", 1).add("moreInfoURL", mapData.getMoreInfoURL())
                .add("speed", mapData.getSpeed()).add("lives", mapData.getLives()).add("maxLives", mapData.getLives())
                .add("musicFile", mapData.getMusicFile()).add("musicTime", mapData.getMusicTime())
                .add("iconFile", mapData.getIconFile()).add("generationType", mapData.getGenerationType())
                .add("environmentType", mapData.getEnvironmentType()).add("checkpoints", checkpointsBuilder.build())
                .add("events", eventsBuilder.build()).build();
    }

    public static JsonObject exportPureMap(MapData mapData) {
        JsonBuilderFactory f = Json.createBuilderFactory(null);
        JsonArrayBuilder levelResourcesBuilder = f.createArrayBuilder();
        List<MapResource> mapResources = mapData.getMapResources();
        for (MapResource r : mapResources) {
            levelResourcesBuilder.add(f.createObjectBuilder().add("name", r.getName()).add("type", "Sprite")
                    .add("path", r.getPath()).build());
        }
        JsonArrayBuilder tagsBuilder = f.createArrayBuilder();
        List<String> tags = mapData.getTags();
        for (String tag : tags) {
            tagsBuilder.add(tag);
        }
        JsonArrayBuilder checkpointsBuilder = f.createArrayBuilder();
        List<Checkpoint> checkpoints = mapData.getCheckpoints();
        for (Checkpoint checkpoint : checkpoints) {
            checkpointsBuilder.add((double) checkpoint.getTime());
        }
        JsonArrayBuilder eventsBuilder = f.createArrayBuilder();
        List<MapEvent> events = mapData.getEventList();
        for (MapEvent evt : events) {
            if (evt.isCoreEvent()) {
                JsonArrayBuilder eventDataBuilder = f.createArrayBuilder();
                String eventData = evt.getEventData();
                if (eventData == null)
                    eventData = "";
                eventDataBuilder.add(evt.getEventName()).add(eventData);
                JsonObjectBuilder eventBuilder = f.createObjectBuilder().add("time", evt.getTime()).add("data",
                        eventDataBuilder.build());
                eventsBuilder.add(eventBuilder);
            }
            List<MapEvent> metaChildren = evt.getMetaChildren();
            if (metaChildren != null) {
                for (MapEvent metaEvt : metaChildren) {
                    if (metaEvt.isCoreEvent()) {
                        JsonArrayBuilder metaEventDataBuilder = f.createArrayBuilder();
                        String metaEventData = metaEvt.getEventData();
                        if (metaEventData == null)
                            metaEventData = "";
                        metaEventDataBuilder.add(metaEvt.getEventName()).add(metaEventData);
                        JsonObjectBuilder metaEventBuilder = f.createObjectBuilder().add("time", metaEvt.getTime())
                                .add("data", metaEventDataBuilder.build()).add("metaId", metaEvt.getMetaId());
                        String metaEventExtraData = metaEvt.getEventExtraData();
                        if (metaEventExtraData != null)
                            metaEventBuilder.add("extraData", metaEventExtraData);
                        eventsBuilder.add(metaEventBuilder);
                    }
                }
            }
        }
        return f.createObjectBuilder().add("id", mapData.getId()).add("name", mapData.getName())
                .add("info", mapData.getInfo()).add("levelResources", levelResourcesBuilder.build())
                .add("tags", tagsBuilder.build()).add("handCount", 1).add("moreInfoURL", mapData.getMoreInfoURL())
                .add("speed", mapData.getSpeed()).add("lives", mapData.getLives()).add("maxLives", mapData.getLives())
                .add("musicFile", mapData.getMusicFile()).add("musicTime", mapData.getMusicTime())
                .add("iconFile", mapData.getIconFile()).add("generationType", mapData.getGenerationType())
                .add("environmentType", mapData.getEnvironmentType()).add("checkpoints", checkpointsBuilder.build())
                .add("events", eventsBuilder.build()).build();
    }
}
