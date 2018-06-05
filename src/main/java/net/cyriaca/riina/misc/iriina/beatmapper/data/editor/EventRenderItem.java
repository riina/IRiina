package net.cyriaca.riina.misc.iriina.beatmapper.data.editor;

import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;

import java.awt.*;

public class EventRenderItem {

    private Color mainColor;
    private Color topColor;
    private Color botColor;
    private float startTime;
    private float endTime;
    private float rootTime;
    private MapEvent.Type type;
    private boolean selected;

    public EventRenderItem(Color mainColor, Color topColor, Color botColor, float startTime, float endTime, float rootTime, MapEvent.Type type, boolean selected) {
        this.mainColor = mainColor;
        this.topColor = topColor;
        this.botColor = botColor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rootTime = rootTime;
        this.type = type;
        this.selected = selected;
    }

    public Color getMainColor() {
        return mainColor;
    }

    public Color getTopColor() {
        return topColor;
    }

    public Color getBotColor() {
        return botColor;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public float getRootTime() {
        return rootTime;
    }

    public MapEvent.Type getType() {
        return type;
    }

    public boolean isSelected() {
        return selected;
    }
}
