package net.cyriaca.riina.misc.iriina.generic;

public class IntBoundedValue {

    private IntBounds bounds = null;
    private int value = -1;

    public IntBoundedValue() {
        this(new IntBounds());
    }

    public IntBoundedValue(IntBounds bounds) {
        this(bounds, (bounds.getUpperLimit() + bounds.getLowerLimit()) / 2);
    }

    public IntBoundedValue(IntBounds bounds, int value) {
        this.bounds = bounds == null ? new IntBounds() : bounds;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < bounds.getLowerLimit()) {
            if (bounds.isLowerUnbounded()) {
                this.value = value;
                bounds = new IntBounds(value, bounds.isLowerUnbounded(), bounds.getUpperLimit(),
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getLowerLimit();
            }
        } else if (value > bounds.getUpperLimit()) {
            if (bounds.isUpperUnbounded()) {
                this.value = value;
                bounds = new IntBounds(bounds.getLowerLimit(), bounds.isLowerUnbounded(), value,
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getUpperLimit();
            }
        } else {
            this.value = value;
        }
    }

    public IntBounds getBounds() {
        return bounds;
    }

    public void setBounds(IntBounds bounds) {
        if (bounds == null)
            return;
        if (value < bounds.getLowerLimit()) {
            if (bounds.isLowerUnbounded()) {
                bounds = new IntBounds(value, bounds.isLowerUnbounded(), bounds.getUpperLimit(),
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getLowerLimit();
            }
        } else if (value > bounds.getUpperLimit()) {
            if (bounds.isUpperUnbounded()) {
                bounds = new IntBounds(bounds.getLowerLimit(), bounds.isLowerUnbounded(), value,
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getUpperLimit();
            }
        }
        this.bounds = bounds;
    }

    public int getLowerLimit() {
        return bounds.getLowerLimit();
    }

    public int getUpperLimit() {
        return bounds.getUpperLimit();
    }

    public IntBoundedValue clone() {
        return new IntBoundedValue(bounds.clone(), value);
    }

}
