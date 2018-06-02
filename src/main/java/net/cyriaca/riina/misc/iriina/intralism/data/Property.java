package net.cyriaca.riina.misc.iriina.intralism.data;

import net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties.PropertyPanel;

import java.util.HashSet;
import java.util.Set;

public class Property {

	private MapEvent parent;
	private boolean modifiable;

	private Set<PropertyPanel> listeners;

	public Property() {
		parent = null;
		modifiable = true;
		listeners = new HashSet<>();
	}

	public void setParent(MapEvent parent) {
		this.parent = parent;
	}

	public MapEvent getParent() {
		return parent;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public boolean isModifiable() {
		return modifiable;
	}

	public void notifyParentOfChange() {
		if (parent != null)
			parent.propertyUpdated(this);
		for(PropertyPanel panel : listeners)
			panel.update();
	}

	public void addModListener(PropertyPanel panel){
		listeners.add(panel);
	}

	public void removeModListener(PropertyPanel panel){
		listeners.remove(panel);
	}

}
