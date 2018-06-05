package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;

public abstract class EventPropertyPanel extends PropertyPanel {

    private EventProperty eventProperty = null;

    public EventProperty getEventProperty() {
        return eventProperty;
    }

    public void setEventProperty(EventProperty eventProperty) {
        EventProperty oldProperty = this.eventProperty;
        this.eventProperty = eventProperty;
        internalSetEventProperty(oldProperty, this.eventProperty);
    }

    public abstract void internalSetEventProperty(EventProperty oldProperty, EventProperty property);

    public abstract EventProperty.Type getType();

}
