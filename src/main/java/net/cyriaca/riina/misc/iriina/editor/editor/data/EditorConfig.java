package net.cyriaca.riina.misc.iriina.editor.editor.data;

import java.util.prefs.Preferences;

public class EditorConfig {

    public static final String PREF_SUBNODE = "editorConfig";

    private boolean autoMergeOnDragEnabled;
    private float maxManipGrabDistanceSec;
    private float displayXScale;
    private float playheadWidth;
    private float eventWidth;
    private float eventHeight;
    private float eventRowHeight;
    private float arcDecayLength;

    public EditorConfig() {
        this.autoMergeOnDragEnabled = true;
        this.maxManipGrabDistanceSec = 1.0f;
        this.displayXScale = 0.01f;
        this.playheadWidth = 2.0f;
        this.eventWidth = 4.0f;
        this.eventHeight = 16.0f;
        this.eventRowHeight = 4.0f;
        this.arcDecayLength = 1.0f;
    }

    public static EditorConfig constructInstanceFromPreferencesNode(Preferences prefs) {
        EditorConfig out = new EditorConfig();
        out.autoMergeOnDragEnabled = Boolean.parseBoolean(prefs.get("autoMergeOnDrag", Boolean.toString(true)));
        out.maxManipGrabDistanceSec = Float.parseFloat(prefs.get("maxManipGrabDistanceSec", "1.0"));
        out.displayXScale = Float.parseFloat(prefs.get("displayXScale", "0.01"));
        out.playheadWidth = Float.parseFloat(prefs.get("playheadWidth", "2.0"));
        out.eventWidth = Float.parseFloat(prefs.get("eventWidth", "4.0"));
        out.eventHeight = Float.parseFloat(prefs.get("eventHeight", "16.0"));
        out.eventRowHeight = Float.parseFloat(prefs.get("eventRowHeight", "4.0"));
        out.arcDecayLength = Float.parseFloat(prefs.get("arcDecayLength", "1.0"));
        return out;
    }

    public static void setPreferencesDataFromInstance(Preferences prefs, EditorConfig config) {
        prefs.put("autoMergeOnDrag", Boolean.toString(config.autoMergeOnDragEnabled));
        prefs.put("maxManipGrabDistanceSec", Float.toString(config.maxManipGrabDistanceSec));
        prefs.put("displayXScale", Float.toString(config.displayXScale));
        prefs.put("playheadWidth", Float.toString(config.playheadWidth));
        prefs.put("eventWidth", Float.toString(config.eventWidth));
        prefs.put("eventHeight", Float.toString(config.eventHeight));
        prefs.put("eventRowHeight", Float.toString(config.eventRowHeight));
        prefs.put("arcDecayLength", Float.toString(config.arcDecayLength));
    }

    public void setAutoMergeOnDrag(boolean enable) {
        autoMergeOnDragEnabled = enable;
    }

    public boolean isAutoMergeOnDragEnabled() {
        return autoMergeOnDragEnabled;
    }

    public float getMaxManipGrabDistanceSec() {
        return maxManipGrabDistanceSec;
    }

    public void setMaxManipGrabDistanceSec(int value) {
        maxManipGrabDistanceSec = value;
    }

    public float getDisplayXScale() {
        return displayXScale;
    }

    public void setDisplayXScale(float value) {
        displayXScale = value;
    }

    public float getPlayheadWidth() {
        return playheadWidth;
    }

    public void setPlayheadWidth(float value) {
        playheadWidth = value;
    }

    public float getEventWidth() {
        return eventWidth;
    }

    public void setEventWidth(float value) {
        eventWidth = value;
    }

    public float getEventHeight() {
        return eventHeight;
    }

    public void setEventHeight(float value) {
        eventHeight = value;
    }

    public float getEventRowHeight() {
        return eventRowHeight;
    }

    public void setEventRowHeight(float value) {
        eventRowHeight = value;
    }

    public float getArcDecayLength() {
        return arcDecayLength;
    }

    public void setArcDecayLength(float value) {
        arcDecayLength = value;
    }

    public void setDefault() {
        this.autoMergeOnDragEnabled = true;
        this.maxManipGrabDistanceSec = 1.0f;
        this.displayXScale = 0.01f;
        this.playheadWidth = 2.0f;
        this.eventWidth = 4.0f;
        this.eventHeight = 16.0f;
        this.eventRowHeight = 4.0f;
        this.arcDecayLength = 1.0f;
    }

    public EditorConfig clone() {
        EditorConfig out = new EditorConfig();
        out.autoMergeOnDragEnabled = this.autoMergeOnDragEnabled;
        out.maxManipGrabDistanceSec = this.maxManipGrabDistanceSec;
        out.displayXScale = this.displayXScale;
        out.playheadWidth = this.playheadWidth;
        out.eventWidth = this.eventWidth;
        out.eventHeight = this.eventHeight;
        out.eventRowHeight = this.eventRowHeight;
        out.arcDecayLength = this.arcDecayLength;
        return out;
    }

}
