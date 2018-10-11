package net.cyriaca.riina.misc.iriina.intralism.data;

import java.util.Arrays;
import java.util.List;

/*
 * Represents an arc stored in an event
 */
public class ArcProperty extends Property {

    public static final int ARR_LENGTH = 5;
    public static final byte MASK_POWER_UP = 0x10;
    public static final byte MASK_UP = 0x01;
    public static final byte MASK_RIGHT = 0x02;
    public static final byte MASK_DOWN = 0x04;
    public static final byte MASK_LEFT = 0x08;
    private static final String STRING_POWER_UP = "PowerUp";
    private static final String STRING_UP = "Up";
    private static final String STRING_RIGHT = "Right";
    private static final String STRING_DOWN = "Down";
    private static final String STRING_LEFT = "Left";
    private byte value;

    public ArcProperty() {
        this(MASK_UP);
    }

    public ArcProperty(byte value) {
        this.value = value;
    }

    public ArcProperty(String dataValue) {
        this(getMaskFromDataValue(dataValue));
    }

    public static byte getMaskFromDataValue(String dataValue) {
        if (dataValue.contains(STRING_POWER_UP))
            return MASK_POWER_UP;
        byte value = 0;
        if (dataValue.contains(STRING_UP))
            value += MASK_UP;
        if (dataValue.contains(STRING_RIGHT))
            value += MASK_RIGHT;
        if (dataValue.contains(STRING_DOWN))
            value += MASK_DOWN;
        if (dataValue.contains(STRING_LEFT))
            value += MASK_LEFT;
        return value;
    }

    public String getDataValue() {
        if ((value & MASK_POWER_UP) != 0)
            return "[PowerUp]";
        String out = "";
        if ((value & MASK_UP) != 0)
            out += "-" + STRING_UP;
        if ((value & MASK_RIGHT) != 0)
            out += "-" + STRING_RIGHT;
        if ((value & MASK_DOWN) != 0)
            out += "-" + STRING_DOWN;
        if ((value & MASK_LEFT) != 0)
            out += "-" + STRING_LEFT;
        if (out.length() != 0)
            out = out.substring(1, out.length());
        return "[" + out + "]";
    }

    public void setDataValue(List<String> dataValueElements) {
        if (dataValueElements.size() != 1)
            return;
        setMask(getMaskFromDataValue(dataValueElements.get(0)));
        notifyParentOfChange();

    }

    public void setDataValue(String dataValue) {
        setDataValue(Arrays.asList(dataValue.split("\\s*,\\s*")));
    }

    public boolean containsArcFromMask(byte mask) {
        return (mask & value) != 0;
    }

    public boolean[] getArr() {
        boolean[] out = new boolean[ARR_LENGTH];
        out[0] = (value & MASK_POWER_UP) != 0;
        out[1] = (value & MASK_UP) != 0;
        out[2] = (value & MASK_RIGHT) != 0;
        out[3] = (value & MASK_DOWN) != 0;
        out[4] = (value & MASK_LEFT) != 0;
        return out;
    }

    public void setMask(byte value) {
        this.value = value;
        notifyParentOfChange();
    }

    public void addToMask(byte value) {
        this.value = (byte) (this.value & value);
        notifyParentOfChange();
    }

    public void toggleValues(byte mask) {
        this.value = (byte) ((~this.value & mask) | (this.value & ~mask));
    }

    public void removeFromMask(byte value) {
        this.value = (byte) (this.value & ~value);
        notifyParentOfChange();
    }

    public ArcProperty clone() {
        return new ArcProperty(value);
    }

}
