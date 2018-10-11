package net.cyriaca.riina.misc.iriina.intralism.data;

/*
 * Represents a resource used by an event, referenced
 * by resource name
 */
public class ResourceProperty extends Property {

    private String resName;

    private String hrKey;

    public ResourceProperty() {
        this.resName = "Undefined";
        this.hrKey = "";
    }

    public String getDataValue() {
        return getValue();
    }

    public String getResourceName() {
        return resName;
    }

    public void setResourceName(String resourceName) {
        this.resName = resourceName == null ? "Undefined" : resourceName;
        notifyParentOfChange();
    }

    public String getValue() {
        return getResourceName();
    }

    public void setValue(String value) {
        setResourceName(value);
    }

    public String getHrKey() {
        return hrKey;
    }

    public void setHrKey(String hrKey) {
        this.hrKey = hrKey;
    }

    public ResourceProperty clone() {
        ResourceProperty out = new ResourceProperty();
        out.resName = resName;
        out.hrKey = hrKey;
        return out;
    }

}
