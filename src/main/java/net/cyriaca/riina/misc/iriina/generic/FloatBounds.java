package net.cyriaca.riina.misc.iriina.generic;

public class FloatBounds {
    private float lowerLimit;
    private boolean lowerUnbounded;
    private float upperLimit;
    private boolean upperUnbounded;

    public FloatBounds() {
        this.lowerLimit = 0.0f;
        this.lowerUnbounded = true;
        this.upperLimit = 10.0f;
        this.upperUnbounded = true;
    }

    public FloatBounds(float lowerLimit, boolean lowerUnbounded, float upperLimit, boolean upperUnbounded) {
        if (lowerLimit <= upperLimit) {
            this.lowerLimit = lowerLimit;
            this.lowerUnbounded = lowerUnbounded;
            this.upperLimit = upperLimit;
            this.upperUnbounded = upperUnbounded;
        } else {
            this.lowerLimit = upperLimit;
            this.lowerUnbounded = upperUnbounded;
            this.upperLimit = lowerLimit;
            this.upperUnbounded = lowerUnbounded;
        }
    }

    public float getLowerLimit() {
        return lowerLimit;
    }

    public boolean isLowerUnbounded() {
        return lowerUnbounded;
    }

    public float getUpperLimit() {
        return upperLimit;
    }

    public boolean isUpperUnbounded() {
        return upperUnbounded;
    }

    public FloatBounds clone() {
        return new FloatBounds(lowerLimit, lowerUnbounded, upperLimit, upperUnbounded);
    }

}
