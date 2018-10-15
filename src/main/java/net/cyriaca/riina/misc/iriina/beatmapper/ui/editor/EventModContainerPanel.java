package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.EventModPanel;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.EventPropertyPanel;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties.generic.*;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.intralism.data.EventProperty;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EventModContainerPanel extends JPanel {

    private static final String KEY_PANEL_EVENT_MOD_TYPE_LEFT_RIGHT_ARC_AND_COMBO = "panel_event_mod_type_left_right_arc_and_combo";
    private static final String KEY_PANEL_EVENT_MOD_TYPE_COMBO_AND_ARC_WITH_TIMING = "panel_event_mod_type_combo_and_arc_with_timing";
    private static final String KEY_PANEL_EVENT_MOD_TYPE_LEFT_RIGHT_VISUAL = "panel_event_mod_type_left_right_visual";

    private ArxTitledComboBoxItem<String> selector;
    private EditorFrame parent;
    private Map<EventProperty.Type, List<EventPropertyPanel>> pooledPanels;
    private EventModPanel topPanel;
    private EventModPanel botPanel;

    public EventModContainerPanel(EditorFrame parent) {
        this.parent = parent;
        BorderLayout bl = new BorderLayout();
        setLayout(bl);
        selector = new ArxTitledComboBoxItem<>();
        selector.setTitle("Event Editor");
        List<String> elems = new ArrayList<>();
        elems.add("LeftRightArcAndCombo");
        elems.add("ComboAndArcWithTiming");
        elems.add("LeftRightVisual");
        selector.setElements(elems);
        selector.setSelectedIndex(1);
        add(selector, BorderLayout.NORTH);

        pooledPanels = new TreeMap<>();
        for (EventProperty.Type t : EventProperty.Type.values())
            pooledPanels.put(t, new ArrayList<>());

        JPanel holder = new JPanel();
        BoxLayout boxL = new BoxLayout(holder, BoxLayout.X_AXIS);
        holder.setLayout(boxL);
        topPanel = new EventModPanel(this);
        botPanel = new EventModPanel(this);
        holder.add(topPanel);
        holder.add(botPanel);
        add(holder, BorderLayout.CENTER);

        setPreferredSize(new Dimension(560, 800));
    }

    public boolean botActive() {
        return botPanel.getTargetEvent() != null;
    }

    public boolean topActive() {
        return topPanel.getTargetEvent() != null;
    }

    public List<EventPropertyPanel> getPanels(List<EventProperty.Type> types) {
        List<EventPropertyPanel> out = new ArrayList<>();
        for (EventProperty.Type type : types) {
            List<EventPropertyPanel> pooledForType = pooledPanels.get(type);
            EventPropertyPanel panel = null;
            if (pooledForType.size() > 0)
                panel = pooledForType.remove(0);
            else {
                switch (type) {
                    case STRING:
                        panel = new GenericStringPropertyPanel();
                        break;
                    case FLOAT:
                        panel = new GenericFloatPropertyPanel();
                        break;
                    case INT:
                        panel = new GenericIntPropertyPanel();
                        break;
                    case BOOLEAN:
                        panel = new GenericBooleanPropertyPanel();
                        break;
                    case SELECTOR:
                        panel = new GenericSelectorPropertyPanel();
                        break;
                    case LABEL:
                        panel = new GenericLabelPropertyPanel();
                        break;
                    case COLOR:
                        panel = new GenericColorPropertyPanel();
                        break;
                }
                panel.localize(parent.getIRiina().getLocale());
            }
            out.add(panel);
        }
        return out;
    }

    public void returnPanels(List<EventPropertyPanel> panels) {
        for (EventPropertyPanel panel : panels) {
            pooledPanels.get(panel.getType()).add(panel);
        }
    }

    public void reevaluateTargets() {
        topPanel.reevaluateTarget();
        botPanel.reevaluateTarget();
    }

    public void applyChanges() {
        topPanel.subApplyChanges();
        botPanel.subApplyChanges();
        updateEventTargets();
        reevaluateTargets();
    }

    public void applyChangesBot() {
        botPanel.subApplyChanges();
        botPanel.reevaluateTarget();
        reevaluateTargets();
    }

    public void applyChangesTop() {
        topPanel.subApplyChanges();
        topPanel.reevaluateTarget();
        reevaluateTargets();
    }

    public void updateEventTargets() {
        MapData data = parent.getMapData();
        float time = parent.getPlayHeadPos();
        switch (selector.getSelectedIndex()) {
            case -1:
                break;
            case 0:
                MapEvent leftArcEvent = data.getClosestEventLeft(time, MapEvent.Type.ARC);
                MapEvent leftComboEvent = data.getClosestEventLeft(time, MapEvent.Type.COMBO);
                if (leftArcEvent != null && leftComboEvent != null) {
                    if (Math.abs(leftArcEvent.getTime() - time) < Math.abs(leftComboEvent.getTime() - time))
                        topPanel.setTargetEvent(leftArcEvent);
                    else
                        topPanel.setTargetEvent(leftComboEvent);
                } else if (leftArcEvent != null) {
                    topPanel.setTargetEvent(leftArcEvent);
                } else
                    topPanel.setTargetEvent(leftComboEvent);

                MapEvent rightArcEvent = data.getClosestEventRight(time, MapEvent.Type.ARC);
                MapEvent rightComboEvent = data.getClosestEventRight(time, MapEvent.Type.COMBO);
                if (rightArcEvent != null && rightComboEvent != null) {
                    if (Math.abs(rightArcEvent.getTime() - time) < Math.abs(rightComboEvent.getTime() - time))
                        botPanel.setTargetEvent(rightArcEvent);
                    else
                        botPanel.setTargetEvent(rightComboEvent);
                } else if (rightArcEvent != null) {
                    botPanel.setTargetEvent(rightArcEvent);
                } else
                    botPanel.setTargetEvent(rightComboEvent);
                break;
            case 1:
                MapEvent closestArc = data.getClosestEvent(time, MapEvent.Type.ARC);
                MapEvent closestCombo = data.getClosestEvent(time, MapEvent.Type.COMBO);

                int evtTarg = parent.getEvtTarg();
                MapEvent targ = null;
                if (evtTarg != -1 && evtTarg < data.getEventList().size())
                    targ = data.getEventList().get(evtTarg);

                if (closestArc != null && closestCombo != null) {
                    if (Math.abs(closestArc.getTime() - time) < Math.abs(closestCombo.getTime() - time)) {
                        if (Math.abs(closestArc.getTime() - time) <= EditorFrame.EVT_FWD_BACK_MAX_ERR) {
                            if (targ != null) {
                                if (Math.abs(targ.getTime() - time) <= Math.abs(closestArc.getTime() - time)) {
                                    topPanel.setTargetEvent(targ);
                                }
                            } else {
                                topPanel.setTargetEvent(closestArc);
                            }
                        } else
                            topPanel.setTargetEvent(closestArc);
                    } else {
                        if (Math.abs(closestCombo.getTime() - time) <= EditorFrame.EVT_FWD_BACK_MAX_ERR) {
                            if (targ != null) {
                                if (Math.abs(targ.getTime() - time) <= Math.abs(closestCombo.getTime() - time)) {
                                    topPanel.setTargetEvent(targ);
                                }
                            } else {
                                topPanel.setTargetEvent(closestCombo);
                            }
                        } else
                            topPanel.setTargetEvent(closestCombo);
                    }
                } else if (closestArc != null) {
                    if (Math.abs(closestArc.getTime() - time) <= EditorFrame.EVT_FWD_BACK_MAX_ERR) {
                        if (targ != null) {
                            if (Math.abs(targ.getTime() - time) <= Math.abs(closestArc.getTime() - time)) {
                                topPanel.setTargetEvent(targ);
                            }
                        } else {
                            topPanel.setTargetEvent(closestArc);
                        }
                    } else
                        topPanel.setTargetEvent(closestArc);
                } else if (closestCombo != null) {
                    if (Math.abs(closestCombo.getTime() - time) <= EditorFrame.EVT_FWD_BACK_MAX_ERR) {
                        if (targ != null) {
                            if (Math.abs(targ.getTime() - time) <= Math.abs(closestCombo.getTime() - time)) {
                                topPanel.setTargetEvent(targ);
                            }
                        } else {
                            topPanel.setTargetEvent(closestCombo);
                        }
                    } else
                        topPanel.setTargetEvent(closestCombo);
                } else
                    topPanel.setTargetEvent(null);
                MapEvent timing = data.getFirstTimingEventForTime(time);
                botPanel.setTargetEvent(timing);
                break;
            case 2:
                MapEvent leftEvent = data.getClosestEventLeft(time, MapEvent.Type.VISUAL);
                topPanel.setTargetEvent(leftEvent);
                MapEvent rightEvent = data.getClosestEventRight(time, MapEvent.Type.VISUAL);
                botPanel.setTargetEvent(rightEvent);
                break;
        }
    }

    public void updateResourceList(List<MapResource> resources) {
        topPanel.updateResourceList(resources);
        botPanel.updateResourceList(resources);
    }

    public List<MapEvent> getTargets() {
        List<MapEvent> out = new ArrayList<>();
        MapEvent topTarget = topPanel.getTargetEvent();
        if (topTarget != null)
            out.add(topTarget);
        MapEvent botTarget = botPanel.getTargetEvent();
        if (botTarget != null)
            out.add(botTarget);
        return out;
    }

    public EditorFrame getEditor() {
        return parent;
    }

    public void localize(Locale l) {
        List<String> elems = new ArrayList<>();
        int i = selector.getSelectedIndex();
        elems.add(l.getKey(KEY_PANEL_EVENT_MOD_TYPE_LEFT_RIGHT_ARC_AND_COMBO));
        elems.add(l.getKey(KEY_PANEL_EVENT_MOD_TYPE_COMBO_AND_ARC_WITH_TIMING));
        elems.add(l.getKey(KEY_PANEL_EVENT_MOD_TYPE_LEFT_RIGHT_VISUAL));
        selector.setElements(elems);
        selector.setSelectedIndex(i);
        topPanel.setLocale(l);
        botPanel.setLocale(l);
    }
}
