package net.cyriaca.riina.misc.iriina.generic;

public class AsheTypeConvert {

    public static Float toFloat(Object object) {
        if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof Long) {
            return ((Long) object).floatValue();
        } else if (object instanceof Integer) {
            return ((Integer) object).floatValue();
        } else if (object instanceof Double) {
            return ((Double) object).floatValue();
        } else
            return null;
    }

    public static Integer toInt(Object object) {
        if (object instanceof Float) {
            return ((Float) object).intValue();
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Double) {
            return ((Double) object).intValue();
        } else
            return null;
    }

}
