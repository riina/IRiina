package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.Result;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

public class MapParseResult extends Result {

    private static final String KEY_RESULT_MAP_PARSE_FAIL_UNDEFINED = "result_map_parse_fail_undefined";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_NO_JSON_DATA = "result_map_parse_fail_no_json_data";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_MISSING_ARRAY_ELEMENT = "result_map_parse_fail_missing_array_element";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_MISSING_VALUE = "result_map_parse_fail_missing_value";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_INVALID_VALUE = "result_map_parse_fail_invalid_value";
    private static final String VALUE = "%value%";
    private static final String VALUE_TYPE = "%valueType%";
    private static final String EVENT_TYPE = "%eventType%";
    private static final String KEY_RESULT_MAP_PARSE_VALUE_TYPE_STRING = "result_map_parse_value_type_string";
    private static final String KEY_RESULT_MAP_PARSE_VALUE_TYPE_ARRAY = "result_map_parse_value_type_array";
    private static final String KEY_RESULT_MAP_PARSE_VALUE_TYPE_NUMBER = "result_map_parse_value_type_number";
    private static final String KEY_RESULT_MAP_PARSE_VALUE_TYPE_OBJECT = "result_map_parse_value_type_object";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_MULTI_HAND_MAP = "result_map_parse_fail_multi_hand_map";
    private static final String KEY_RESULT_MAP_PARSE_FAIL_UNRECOGNIZED_EVENT = "result_map_parse_fail_unrecognized_event";
    private MapData mapData;
    private FailureSource failureSource;
    private String value;
    private ValueType valueType;
    private String eventName;

    public MapParseResult(MapData mapData) {
        super(ResultType.SUCCESS);
        this.mapData = mapData;
        this.failureSource = FailureSource.UNDEFINED;
        this.value = null;
        this.valueType = null;
        this.eventName = null;
    }

    public MapParseResult(FailureSource failureSource) {
        super(ResultType.FAILURE);
        this.mapData = null;
        this.failureSource = failureSource;
        this.value = null;
        this.valueType = null;
        this.eventName = null;
    }

    public MapParseResult(FailureSource failureSource, String value) {
        super(ResultType.FAILURE);
        this.mapData = null;
        this.failureSource = failureSource;
        this.value = value;
        this.valueType = null;
        this.eventName = null;
    }

    public MapParseResult(FailureSource failureSource, String value, ValueType valueType) {
        super(ResultType.FAILURE);
        this.mapData = null;
        this.failureSource = failureSource;
        this.value = value;
        this.valueType = valueType;
        this.eventName = null;
    }

    public static MapParseResult createSuccessResult(MapData mapData) {
        return new MapParseResult(mapData);
    }

    public static MapParseResult createFailureResult(FailureSource failureSource) {
        return new MapParseResult(failureSource);
    }

    public static MapParseResult createUnrecognizedEventResult(String eventName) {
        MapParseResult o = new MapParseResult(FailureSource.UNRECOGNIZED_EVENT);
        o.eventName = eventName;
        return o;
    }

    public static MapParseResult createMissingValueResult(String value) {
        return new MapParseResult(FailureSource.MISSING_VALUE, value);
    }

    public static MapParseResult createInvalidValueResult(String value, ValueType valueType) {
        return new MapParseResult(FailureSource.INVALID_VALUE, value, valueType);
    }

    public static MapParseResult createMissingArrayElementResult(String value) {
        return new MapParseResult(FailureSource.MISSING_ARRAY_ELEMENT, value);
    }

    public MapData getMapData() {
        return mapData;
    }

    public FailureSource getFailureSource() {
        return failureSource;
    }

    public String getValue() {
        return value == null ? "Null" : value;
    }

    public String getLocalizedFailureInfo(Locale l) {
        if (getResultType() == ResultType.SUCCESS)
            return "";
        switch (failureSource) {
            case INVALID_VALUE:
                String str = l.getKey(KEY_RESULT_MAP_PARSE_FAIL_INVALID_VALUE);
                switch (valueType) {
                    case ARRAY:
                        str = str.replaceAll(VALUE_TYPE, l.getKey(KEY_RESULT_MAP_PARSE_VALUE_TYPE_ARRAY));
                    case NUMBER:
                        str = str.replaceAll(VALUE_TYPE, l.getKey(KEY_RESULT_MAP_PARSE_VALUE_TYPE_NUMBER));
                    case OBJECT:
                        str = str.replaceAll(VALUE_TYPE, l.getKey(KEY_RESULT_MAP_PARSE_VALUE_TYPE_OBJECT));
                    case STRING:
                        str = str.replaceAll(VALUE_TYPE, l.getKey(KEY_RESULT_MAP_PARSE_VALUE_TYPE_STRING));
                    default:
                        break;
                }
                return str.replace(VALUE, value);
            case MISSING_ARRAY_ELEMENT:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_MISSING_ARRAY_ELEMENT).replaceAll(VALUE, value);
            case MISSING_VALUE:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_MISSING_VALUE).replaceAll(VALUE, value);
            case MULTI_HAND_MAP:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_MULTI_HAND_MAP);
            case NO_JSON_DATA_PROVIDED:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_NO_JSON_DATA);
            case UNDEFINED:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_UNDEFINED);
            case UNRECOGNIZED_EVENT:
                return l.getKey(KEY_RESULT_MAP_PARSE_FAIL_UNRECOGNIZED_EVENT).replaceAll(EVENT_TYPE, eventName);
            default:
                break;
        }
        return "";
    }

    public enum FailureSource {
        UNDEFINED, NO_JSON_DATA_PROVIDED, MISSING_ARRAY_ELEMENT, MISSING_VALUE, INVALID_VALUE, MULTI_HAND_MAP, UNRECOGNIZED_EVENT
    }

    public enum ValueType {
        STRING, ARRAY, NUMBER, OBJECT
    }
}
