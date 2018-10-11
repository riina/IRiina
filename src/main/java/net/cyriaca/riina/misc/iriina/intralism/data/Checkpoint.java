package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents a checkpoint in a map
 */
public class Checkpoint {

    private float time;

    private List<Float> pastTimes;

    private MapData parent;

    public Checkpoint(float time, MapData parent) {
        pastTimes = new ArrayList<>();
        this.time = time;
        this.parent = parent;
    }

    public final float getTime() {
        return time;
    }

    public final void setTime(float time) {
        this.time = Math.max(0.001f, time);
        if (parent != null)
            parent.repositionCheckpoint(this);
    }

    public final void revertTime() {
        if (pastTimes.size() != 0) {
            setTime(pastTimes.remove(pastTimes.size() - 1));
        }
    }

    public final void concreteTime() {
        pastTimes.add(this.time);
    }

    public void setParent(MapData parent) {
        this.parent = parent;
    }
}
