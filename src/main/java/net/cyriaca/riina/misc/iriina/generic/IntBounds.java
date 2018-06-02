package net.cyriaca.riina.misc.iriina.generic;

public class IntBounds {
    private int lowerLimit;
    private boolean lowerUnbounded;
    private int upperLimit;
    private boolean upperUnbounded;

    public IntBounds() {
        this.lowerLimit = 0;
        this.lowerUnbounded = true;
        this.upperLimit = 10;
        this.upperUnbounded = true;
    }

    public IntBounds(int lowerLimit, boolean lowerUnbounded, int upperLimit, boolean upperUnbounded) {
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

    public int getLowerLimit() {
        return lowerLimit;
    }

    public boolean isLowerUnbounded() {
        return lowerUnbounded;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public boolean isUpperUnbounded() {
        return upperUnbounded;
    }

    public IntBounds clone() {
        return new IntBounds(lowerLimit, lowerUnbounded, upperLimit, upperUnbounded);
    }

}
