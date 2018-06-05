package net.cyriaca.riina.misc.iriina.beatmapper.data.editor;

import net.cyriaca.riina.misc.iriina.intralism.data.ArcProperty;

public class ArcRenderItem {

    private float[] levels;
    private float[] ticks;

    public ArcRenderItem(float[] levels, float[] ticks) {
        if (levels != null && levels.length == ArcProperty.ARR_LENGTH)
            this.levels = levels;
        else
            this.levels = null;
        this.ticks = ticks;
    }

    public float[] getLevels() {
        return levels;
    }

    public float[] getTicks() {
        return ticks;
    }

}
