package net.cyriaca.riina.misc.iriina.generic;

public class FloatBoundedValue {

    private FloatBounds bounds = null;
    private float value = -1;

    public FloatBoundedValue() {
        this(new FloatBounds());
    }

    public FloatBoundedValue(FloatBounds bounds) {
        this(bounds, (bounds.getUpperLimit() + bounds.getLowerLimit()) / 2.0f);
    }

    public FloatBoundedValue(FloatBounds bounds, float value) {
        this.bounds = bounds == null ? new FloatBounds() : bounds.clone();
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (value < bounds.getLowerLimit()) {
            if (bounds.isLowerUnbounded()) {
                this.value = value;
                bounds = new FloatBounds(value, bounds.isLowerUnbounded(), bounds.getUpperLimit(),
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getLowerLimit();
            }
        } else if (value > bounds.getUpperLimit()) {
            if (bounds.isUpperUnbounded()) {
                this.value = value;
                bounds = new FloatBounds(bounds.getLowerLimit(), bounds.isLowerUnbounded(), value,
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getUpperLimit();
            }
        } else {
            this.value = value;
        }
    }

    public FloatBounds getBounds() {
        return bounds;
    }

    public void setBounds(FloatBounds bounds) {
        if (bounds == null)
            return;
        if (value < bounds.getLowerLimit()) {
            if (bounds.isLowerUnbounded()) {
                bounds = new FloatBounds(value, bounds.isLowerUnbounded(), bounds.getUpperLimit(),
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getLowerLimit();
            }
        } else if (value > bounds.getUpperLimit()) {
            if (bounds.isUpperUnbounded()) {
                bounds = new FloatBounds(bounds.getLowerLimit(), bounds.isLowerUnbounded(), value,
                        bounds.isUpperUnbounded());
            } else {
                this.value = bounds.getUpperLimit();
            }
        }
        this.bounds = bounds;
    }

    public float getLowerLimit() {
        return bounds.getLowerLimit();
    }

    public float getUpperLimit() {
        return bounds.getUpperLimit();
    }

    public FloatBoundedValue clone() {
        return new FloatBoundedValue(bounds.clone(), value);
    }

}
