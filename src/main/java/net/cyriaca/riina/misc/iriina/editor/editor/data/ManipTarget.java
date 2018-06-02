package net.cyriaca.riina.misc.iriina.editor.editor.data;

import net.cyriaca.riina.misc.iriina.intralism.data.Checkpoint;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;

public class ManipTarget {

    private Type type;
    private MapEvent target;
    private Checkpoint checkpointTarget;

    public ManipTarget() {
        type = Type.NO_TARGET;
        target = null;
    }

    public ManipTarget(Type type, MapEvent target) {
        this.type = type;
        this.target = target;
        this.checkpointTarget = null;
    }

    public ManipTarget(Checkpoint checkpointTarget) {
        this.type = Type.CHECKPOINT_DRAG;
        this.target = null;
        this.checkpointTarget = checkpointTarget;
    }

    public Type getType() {
        return type;
    }

    public MapEvent getTarget() {
        return target;
    }

    public Checkpoint getCheckpointTarget() {
        return checkpointTarget;
    }

    public enum Type {
        NO_TARGET,
        SELECTION_DRAG,
        EVENT_TIME_DRAG,
        CHECKPOINT_DRAG,
        TIMING_EVENT_LENGTH_DRAG,
        TIMING_EVENT_ROOT_TIME_DRAG
    }
}
