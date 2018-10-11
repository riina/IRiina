package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.generic.FloatBoundedValue;
import net.cyriaca.riina.misc.iriina.generic.FloatBounds;
import net.cyriaca.riina.misc.iriina.generic.IntBoundedValue;
import net.cyriaca.riina.misc.iriina.generic.IntBounds;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.*;
import java.util.List;

/*
 * A container with fields for required elements
 * of a UI-displayable property in an event
 */
public class EventProperty extends Property {

    private static final ColorSpace COLOR_SPACE = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
    private Type type;
    private String hrNameKey;
    private String stringProperty;
    private FloatBoundedValue floatProperty;
    private IntBoundedValue intProperty;
    private boolean booleanProperty;
    private String selectorPropertyKey;
    private String selectorPropertyValue;
    private Map<String, String> selectorPropertyMap;
    private List<String> selectorPropertyKeys;
    private List<String> selectorPropertyValues;
    private Color colorProperty;
    private float[] colorVals;

    private EventProperty() {
        this.type = Type.STRING;
        this.hrNameKey = "Property";
        this.stringProperty = "";
        this.floatProperty = new FloatBoundedValue();
        this.intProperty = new IntBoundedValue();
        this.booleanProperty = false;
        this.selectorPropertyKey = null;
        this.selectorPropertyValue = null;
        this.selectorPropertyMap = Collections.unmodifiableMap(new TreeMap<>());
        this.selectorPropertyKeys = Collections.unmodifiableList(new ArrayList<>());
        this.selectorPropertyValues = Collections.unmodifiableList(new ArrayList<>());
        this.colorProperty = Color.CYAN;
        this.colorVals = colorProperty.getRGBColorComponents(null);
    }

    public EventProperty(String hrNameKey, Type type) {
        this();
        this.hrNameKey = hrNameKey;
        this.type = type;
    }

    public String getDataValue() {
        return getValue();
    }

    public void setSelectorByIndex(int index) {
        if (index < 0 || index >= selectorPropertyMap.size())
            return;
        selectorPropertyKey = selectorPropertyKeys.get(index);
        selectorPropertyValue = selectorPropertyValues.get(index);
        notifyParentOfChange();
    }

    public void setSelectorMap(Map<String, String> selectorPropertyMap) {
        if (selectorPropertyMap == null)
            return;
        this.selectorPropertyMap = Collections.unmodifiableMap(new TreeMap<>(selectorPropertyMap));
        List<String> keys = new ArrayList<>(selectorPropertyMap.keySet());
        this.selectorPropertyKeys = Collections.unmodifiableList(keys);
        List<String> values = new ArrayList<>(selectorPropertyMap.values());
        this.selectorPropertyValues = Collections.unmodifiableList(values);
        if (selectorPropertyKeys.size() == 0) {
            selectorPropertyKey = null;
            selectorPropertyValue = null;
        } else {
            selectorPropertyKey = selectorPropertyKeys.get(0);
            selectorPropertyValue = selectorPropertyValues.get(0);
        }
        notifyParentOfChange();
    }

    public String getHrNameKey() {
        return hrNameKey;
    }

    public FloatBounds getFloatBounds() {
        return floatProperty.getBounds();
    }

    public void setFloatBounds(FloatBounds floatBounds) {
        if (floatBounds == null)
            return;
        this.floatProperty.setBounds(floatBounds);
        notifyParentOfChange();
    }

    public IntBounds getIntBounds() {
        return intProperty.getBounds();
    }

    public void setIntBounds(IntBounds intBounds) {
        if (intBounds == null)
            return;
        this.intProperty.setBounds(intBounds);
        notifyParentOfChange();
    }

    public String getString() {
        return stringProperty;
    }

    public void setString(String stringProperty) {
        if (stringProperty != null)
            this.stringProperty = stringProperty;
        notifyParentOfChange();
    }

    public float getFloat() {
        return floatProperty.getValue();
    }

    public void setFloat(float floatProperty) {
        this.floatProperty.setValue(floatProperty);
        notifyParentOfChange();
    }

    public int getInt() {
        return intProperty.getValue();
    }

    public void setInt(int intProperty) {
        this.intProperty.setValue(intProperty);
        notifyParentOfChange();
    }

    public boolean getBoolean() {
        return booleanProperty;
    }

    public void setBoolean(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
        notifyParentOfChange();
    }

    public String getSelector() {
        return selectorPropertyValue;
    }

    public void setSelector(String selectorProperty) {
        if (selectorProperty == null)
            return;
        int pos = selectorPropertyValues.indexOf(selectorProperty);
        if (pos == -1)
            return;
        selectorPropertyKey = selectorPropertyKeys.get(pos);
        selectorPropertyValue = selectorProperty;
        notifyParentOfChange();
    }

    public String getSelectorKey() {
        return selectorPropertyKey;
    }

    public void setSelectorKey(String selectorPropertyKey) {
        if (selectorPropertyKey == null)
            return;
        String val = selectorPropertyMap.get(selectorPropertyKey);
        if (val == null)
            return;
        this.selectorPropertyKey = selectorPropertyKey;
        this.selectorPropertyValue = val;
        notifyParentOfChange();
    }

    public String getSelectorValue() {
        return selectorPropertyValue;
    }

    public List<String> getSelectorKeys() {
        return selectorPropertyKeys;
    }

    public List<String> getSelectorValues() {
        return selectorPropertyValues;
    }

    public Color getColor() {
        return colorProperty;
    }

    public void setColor(Color color) {
        colorProperty = color;
        colorProperty.getRGBColorComponents(colorVals);
        notifyParentOfChange();
    }

    public float getColorRed() {
        return colorVals[0];
    }

    public void setColorRed(float value) {
        colorVals[0] = Math.max(0.0f, Math.min(value, 1.0f));
        colorProperty = new Color(COLOR_SPACE, colorVals, 1.0f);
        notifyParentOfChange();
    }

    public float getColorGreen() {
        return colorVals[1];
    }

    public void setColorGreen(float value) {
        colorVals[1] = Math.max(0.0f, Math.min(value, 1.0f));
        colorProperty = new Color(COLOR_SPACE, colorVals, 1.0f);
        notifyParentOfChange();
    }

    public float getColorBlue() {
        return colorVals[2];
    }

    public void setColorBlue(float value) {
        colorVals[2] = Math.max(0.0f, Math.min(value, 1.0f));
        colorProperty = new Color(COLOR_SPACE, colorVals, 1.0f);
        notifyParentOfChange();
    }

    public String getValue() {
        switch (type) {
            case BOOLEAN:
                return booleanProperty ? "True" : "False";
            case FLOAT:
                return Float.toString(floatProperty.getValue());
            case INT:
                return Integer.toString(intProperty.getValue());
            case STRING:
                return stringProperty;
            case SELECTOR:
                return selectorPropertyValue;
            case LABEL:
                return "";
            case COLOR:
                return Float.toString(colorVals[0]) + "," + Float.toString(colorVals[1]) + "," + Float.toString(colorVals[2]);
            default:
                return "";
        }
    }

    public void setValue(String value) {
        if (value == null)
            return;
        switch (type) {
            case BOOLEAN:
                booleanProperty = Boolean.parseBoolean(value);
                break;
            case FLOAT:
                try {
                    floatProperty.setValue(Float.parseFloat(value));
                } catch (NumberFormatException ignored) {
                }
                break;
            case INT:
                try {
                    intProperty.setValue(Integer.parseInt(value));
                } catch (NumberFormatException ignored) {
                }
                break;
            case STRING:
                stringProperty = value;
                break;
            case SELECTOR:
                setSelector(value);
                break;
            case LABEL:
                break;
            case COLOR:
                String[] split = value.split("\\s*,\\s*");
                if (split.length < 3)
                    break;
                float red = 0.0f;
                float green = 0.0f;
                float blue = 0.0f;
                try {
                    red = Float.parseFloat(split[0]);
                } catch (NumberFormatException ignored) {
                }
                try {
                    green = Float.parseFloat(split[1]);
                } catch (NumberFormatException ignored) {
                }
                try {
                    blue = Float.parseFloat(split[2]);
                } catch (NumberFormatException ignored) {
                }
                colorProperty = new Color(red, green, blue);
                colorProperty.getRGBColorComponents(colorVals);
                break;
            default:
                break;
        }
        notifyParentOfChange();
    }

    public Type getType() {
        return type;
    }

    public EventProperty clone() {
        EventProperty out = new EventProperty();
        out.type = type;
        out.hrNameKey = hrNameKey;
        out.stringProperty = stringProperty;
        out.floatProperty = floatProperty.clone();
        out.intProperty = intProperty.clone();
        out.booleanProperty = booleanProperty;
        out.selectorPropertyKey = selectorPropertyKey;
        out.selectorPropertyValue = selectorPropertyValue;
        out.selectorPropertyMap = Collections.unmodifiableMap(new TreeMap<>(selectorPropertyMap));
        out.selectorPropertyKeys = Collections.unmodifiableList(new ArrayList<>(selectorPropertyKeys));
        out.selectorPropertyValues = Collections.unmodifiableList(new ArrayList<>(selectorPropertyValues));
        out.colorProperty = colorProperty;
        out.colorVals = colorVals.clone();
        return out;
    }

    public enum Type {
        STRING, FLOAT, INT, BOOLEAN, SELECTOR, LABEL, COLOR
    }

}
