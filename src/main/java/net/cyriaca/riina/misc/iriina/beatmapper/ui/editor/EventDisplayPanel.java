package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.ArcRenderItem;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EditorConfig;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EventRenderItem;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EventDisplayPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING = "panel_event_display_state_recording";
    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_WAITING = "panel_event_display_state_waiting";
    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING_REMAP = "panel_event_display_state_recording_remap";
    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_WAITING_REMAP = "panel_event_display_state_waiting_remap";
    private static final String KEY_PANEL_EVENT_DISPLAY_TYPE_TITLE = "panel_event_display_type_title";
    private static final String TYPE = "%type%";
    private static final String SELCOUNT = "%selCount%";
    private EditorFrame parent;
    private String recording = null;
    private String waiting = null;
    private String recordingRemap = null;
    private String waitingRemap = null;
    private String typeTitle = null;

    private ArcRenderItem arcRenderItem;
    private List<EventRenderItem> eventRenderItems;
    private List<EventRenderItem> targetEventRenderItems;
    private float t1;
    private float t2;
    private int[] selCount;
    private List<Float> checkpointItems = null;

    private float selT1 = -1.0f;
    private float selT2 = -1.0f;

    private Image tempImg;

    private MapEvent.Type initSelectionType;
    private int curHover;
    private float curTimeHover;
    private EditorFrame.SelectionMode selMode;

    private String message = null;

    private float time = -1.0f;
    private SelectionState selectionState;

    private int menuMask;

    public EventDisplayPanel(EditorFrame parent) {
        tempImg = parent.getIRiina().getTempImg();

        this.parent = parent;
        this.arcRenderItem = null;
        this.eventRenderItems = null;
        this.targetEventRenderItems = null;
        this.selCount = new int[4];
        this.t1 = -1.0f;
        this.t2 = -1.0f;
        this.curHover = -1;
        this.selMode = EditorFrame.SelectionMode.ADDITIVE;
        this.curTimeHover = -1.0f;

        this.initSelectionType = MapEvent.Type.VISUAL;
        this.selectionState = SelectionState.NONE;

        menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        addMouseListener(this);
        addMouseMotionListener(this);

        addKeyListener(this);
    }

    private static Color getColorForThing(Color bg, Color arc, float percent) {
        float[] bgArr = bg.getRGBColorComponents(null);
        float[] arcArr = arc.getRGBColorComponents(null);
        return new Color(((arcArr[0] - bgArr[0]) * percent) + bgArr[0], ((arcArr[1] - bgArr[1]) * percent) + bgArr[1], ((arcArr[2] - bgArr[2]) * percent) + bgArr[2]);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        MapData data = parent.getMapData();
        if ((e.getModifiersEx() & (menuMask)) == (menuMask))
            return;
        if ((e.getModifiersEx() & (KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.SHIFT_DOWN_MASK)) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_I) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        ArcProperty arcProperty = evt.getArcProperty();
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_J) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_K) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_LEFT + ArcProperty.MASK_DOWN));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_LEFT + ArcProperty.MASK_DOWN));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_DOWN));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_DOWN));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_U) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_O) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_M) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_COMMA) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_PERIOD) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_RIGHT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_RIGHT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_P) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_UP));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_UP));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask((byte) (ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask((byte) (ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_G) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask(ArcProperty.MASK_POWER_UP);
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask(ArcProperty.MASK_POWER_UP);
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_I) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask(ArcProperty.MASK_UP);
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask(ArcProperty.MASK_UP);
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_J) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask(ArcProperty.MASK_LEFT);
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask(ArcProperty.MASK_LEFT);
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_K) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask(ArcProperty.MASK_DOWN);
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask(ArcProperty.MASK_DOWN);
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L) {
                if (parent.isRemapMode()) {
                    List<MapEvent> evtList = parent.getEventTargets();
                    if (evtList.size() != 0) {
                        MapEvent evt = evtList.get(0);
                        String d = evt.getEventData();
                        String ed = evt.getEventExtraData();
                        ArcProperty arcProperty = evt.getArcProperty();
                        if (arcProperty != null)
                            arcProperty.setMask(ArcProperty.MASK_RIGHT);
                        parent.evtFwd();
                        parent.addOperationForEventDataMod(evt, d, ed, evt.getTime(), evt.getEventData(), evt.getEventExtraData(), evt.getTime());
                    }
                } else {
                    MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                    evt.getArcProperty().setMask(ArcProperty.MASK_RIGHT);
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                }
            }
            if (!parent.isRemapMode()) {
                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_U) {
                    MapEvent evt = new TimingEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_O) {
                    MapEvent evt = new ShowTitleEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_M) {
                    MapEvent evt = new ShowSpriteEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_COMMA) {
                    MapEvent evt = new SetPlayerDistanceEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_PERIOD) {
                    MapEvent evt = new SetBGColorEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_P) {
                    MapEvent evt = new ComboEvent(parent.getPlayHeadPos());
                    data.addNewEvent(evt);
                    parent.addOperationForEventAdd(evt);
                } else if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                    Checkpoint checkpoint = new Checkpoint(parent.getPlayHeadPos(), parent.getMapData());
                    data.addCheckpoint(checkpoint);
                    parent.addOperationForCheckpointAdd(checkpoint);
                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
                    parent.evtBack();
                    parent.updateEventTargets();
                }
                else if (e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
                    parent.evtFwd();
                    parent.updateEventTargets();
                }
            }
        }
        parent.queueRender();
    }

    public void keyReleased(KeyEvent e) {

    }

    public void updateTimes(float t1, float t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public void update(ArcRenderItem arcRenderItem, List<EventRenderItem> eventRenderItems, List<EventRenderItem> targetEventRenderItems, List<Float> checkpointItems, int[] selCount, float t1, float t2, float time) {
        this.arcRenderItem = arcRenderItem;
        this.eventRenderItems = eventRenderItems;
        this.targetEventRenderItems = targetEventRenderItems;
        this.checkpointItems = checkpointItems;
        this.selCount = selCount;
        this.time = time;
        updateTimes(t1, t2);
    }

    public void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        if (message != null) {
            super.setBackground(Color.BLACK);
            super.paintComponent(g);
            g.setColor(Color.CYAN);
            g.fillRect(0, (h / 2) - 10, w, 20);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(message);
            int th = fm.getHeight();
            g.setColor(Color.BLACK);
            g.drawString(message, w / 2 - tw / 2, h / 2 + th / 4);
            return;
        }

        parent.updateEventDisplayPanel();

        g.setFont(new Font("SansSerif", Font.PLAIN, 20));

        int centerX = w / 2;
        int centerY = h / 2;

        int minRad = Math.min(w, h) / 2;

        super.setBackground(Color.BLACK);
        super.paintComponent(g);
        if (parent.getRenderArcs() || parent.getRenderBeats()) {
            if (arcRenderItem != null) {
                int outsideRad = Math.min(w, h) / 2;
                int middleRad = (int) (outsideRad * 0.75f);
                int middleInsideRad = (int) (outsideRad * 0.50f);
                int insideRad = (int) (outsideRad * 0.25f);
                int outsideTopLeftX = centerX - outsideRad;
                int outsideTopLeftY = centerY - outsideRad;
                int middleTopLeftX = centerX - middleRad;
                int middleTopLeftY = centerY - middleRad;
                int middleInsideTopLeftX = centerX - middleInsideRad;
                int middleInsideTopLeftY = centerY - middleInsideRad;
                int insideTopLeftX = centerX - insideRad;
                int insideTopLeftY = centerY - insideRad;
                float[] arr = arcRenderItem.getLevels();
                if (parent.getRenderArcs()) {
                    //1: up
                    g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[1]));
                    g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 45, 90);

                    //2: right
                    g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[2]));
                    g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 315, 90);

                    //3: down
                    g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[3]));
                    g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 225, 90);

                    //4: left
                    g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[4]));
                    g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 135, 90);

                    g.setColor(Color.BLACK);
                    g.fillOval(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2);

                    if (parent.getRenderBeats()) {
                        float[] arr2 = arcRenderItem.getTicks();
                        if (arr2 != null) {
                            int targSize = 360 / arr2.length;
                            for (int i = 0; i < arr2.length; i++) {
                                g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr2[i]));
                                g.fillArc(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2, -(i * targSize), targSize);
                            }
                        }
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillOval(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2);
                    }

                    g.setColor(Color.BLACK);
                    g.fillOval(middleInsideTopLeftX, middleInsideTopLeftY, middleInsideRad * 2, middleInsideRad * 2);

                    //0: powerup
                    g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[0]));
                    g.fillOval(insideTopLeftX, insideTopLeftY, insideRad * 2, insideRad * 2);

                } else {
                    if (parent.getRenderBeats()) {
                        float[] arr2 = arcRenderItem.getTicks();
                        if (arr2 != null) {
                            int targSize = 360 / arr2.length;
                            for (int i = 0; i < arr2.length; i++) {
                                g.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr2[i]));
                                g.fillArc(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2, -(i * targSize), targSize);
                            }
                        }
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillOval(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2);
                    }
                    g.setColor(Color.BLACK);
                    g.fillOval(middleInsideTopLeftX, middleInsideTopLeftY, middleInsideRad * 2, middleInsideRad * 2);

                }
            }
        } else
            g.drawImage(tempImg, (int) (((float) w / 2.0f) - minRad), (int) (((float) h / 2) - minRad), (int) (minRad * 2.0f), (int) (minRad * 2.0f), null);
        if (parent.getRenderEvents() && eventRenderItems != null) {
            EditorConfig cfg = parent.getConfig();
            int rowHeight = (int) cfg.getEventRowHeight();
            int playHeadWidth = (int) cfg.getPlayheadWidth();
            int eventHeight = h / 8;
            int eventWidth = (int) cfg.getEventWidth();
            g.setColor(Color.GRAY);
            g.fillRect(0, (h / 8) - (rowHeight / 2), w, rowHeight);
            g.fillRect(0, (h / 4) + (h / 8) - (rowHeight / 2), w, rowHeight);
            g.fillRect(0, (h * 2 / 4) + (h / 8) - (rowHeight / 2), w, rowHeight);
            g.fillRect(0, (h * 3 / 4) + (h / 8) - (rowHeight / 2), w, rowHeight);
            g.fillRect((w / 2) - (playHeadWidth / 2), 0, playHeadWidth, h);
            MapEvent.Type[] types = MapEvent.Type.values();

            g.setColor(Color.LIGHT_GRAY);
            for (Float f : checkpointItems) {
                g.fillRect((int) ((f - t1) / (t2 - t1) * w - (eventWidth / 2)), 0, eventWidth, h);
            }

            for (EventRenderItem item : eventRenderItems) {
                int yMod = item.getType().ordinal();
                if (item.isSelected())
                    g.setColor(EditorFrame.INDIGO);
                else
                    g.setColor(item.getMainColor());
                if (item.getType() != MapEvent.Type.TIMING)
                    g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), eventWidth, eventHeight);
                else
                    g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight);
                Color topColor = item.getTopColor();
                if (topColor != null) {
                    g.setColor(topColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), eventWidth, eventHeight / 2);
                    else
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight / 2);
                }
                Color botColor = item.getBotColor();
                if (botColor != null) {
                    g.setColor(botColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8), eventWidth, eventHeight / 2);
                    else
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight / 2);
                }
                if (item.getType() == MapEvent.Type.TIMING) {
                    Color mainColor = item.getMainColor();
                    g.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 120));
                    g.fillRect((int) ((item.getRootTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), 0, eventWidth, h);
                }
            }

            for (EventRenderItem item : targetEventRenderItems) {
                int yMod = item.getType().ordinal();
                if (item.isSelected())
                    g.setColor(EditorFrame.INDIGO);
                else
                    g.setColor(item.getMainColor());
                if (item.getType() != MapEvent.Type.TIMING)
                    g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), eventWidth, eventHeight);
                else
                    g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight);
                Color topColor = item.getTopColor();
                if (topColor != null) {
                    g.setColor(topColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), eventWidth, eventHeight / 2);
                    else
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8) - (eventHeight / 2), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight / 2);
                }
                Color botColor = item.getBotColor();
                if (botColor != null) {
                    g.setColor(botColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8), eventWidth, eventHeight / 2);
                    else
                        g.fillRect((int) ((item.getStartTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), (h * (yMod) / 4) + (h / 8), (int) ((item.getEndTime() - item.getStartTime()) / (t2 - t1) * w + eventWidth), eventHeight / 2);
                }
                if (item.getType() == MapEvent.Type.TIMING) {
                    Color mainColor = item.getMainColor();
                    g.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 120));
                    g.fillRect((int) ((item.getRootTime() - t1) / (t2 - t1) * w - (eventWidth / 2)), 0, eventWidth, h);
                }
            }

            g.setColor(Color.WHITE);
            for (int i = 0; i < types.length; i++) {
                g.drawString(typeTitle.replaceAll(TYPE, types[i].name()).replaceAll(SELCOUNT, Integer.toString(selCount[i])), 0, (int) (((float) h * i / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f)));
            }
        }
        if (curHover != -1) {
            Color col = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100);
            g.setColor(col);
            g.fillRect(0, (curHover * h / 4), w, h / 4);
            g.drawLine((int) ((curTimeHover - t1) / (t2 - t1) * (float) w), 0, (int) ((curTimeHover - t1) / (t2 - t1) * (float) w), h);
        }
        if (selectionState == SelectionState.SELECT) {
            Color col = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 100);
            g.setColor(col);
            g.fillRect((int) ((Math.min(selT1, selT2) - t1) / (t2 - t1) * w), (h * (initSelectionType.ordinal()) / 4), (int) (Math.abs(selT2 - selT1) / (t2 - t1) * w), h / 4);
        }
        g.setColor(Color.WHITE);
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)
            g.drawString(parent.isRemapMode() ? recordingRemap : recording, 0, getHeight());
        else
            g.drawString(parent.isRemapMode() ? waitingRemap : waiting, 0, getHeight());
        g.setColor(Color.WHITE);
        g.drawString(Float.toString(time), getWidth() / 2, getHeight());
    }

    public void deselect() {
        switch (selectionState) {
            case SELECT:
                parent.setSelectionUp();
                break;
            case MOVE:
                parent.clearDrag();
                break;
            case MOVE_CHECKPOINT:
                parent.clearDrag();
                break;
            case NONE:
                break;
        }
        selectionState = SelectionState.NONE;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        requestFocus();
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if ((e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) == MouseEvent.SHIFT_DOWN_MASK)
                parent.deleteClosest(((float) e.getX()) / width * (t2 - t1) + t1, MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))]);
            else if ((e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) == MouseEvent.CTRL_DOWN_MASK) {
                parent.deleteClosestCheckpoint(((float) e.getX()) / width * (t2 - t1) + t1);
            } else if ((e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) == (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) {
                if (selectionState == SelectionState.NONE) {
                    selectionState = SelectionState.MOVE_CHECKPOINT;
                    selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                    parent.prepareCheckpointDrag(selT1);
                }
            } else if (selectionState == SelectionState.NONE) {
                selectionState = SelectionState.MOVE;
                initSelectionType = MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))];
                selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                parent.prepareDrag(selT1, initSelectionType);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (selectionState == SelectionState.NONE) {
                selectionState = SelectionState.SELECT;
                initSelectionType = MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))];
                selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                selT2 = selT1;
                int mask = e.getModifiersEx();
                int shiftMask = MouseEvent.SHIFT_DOWN_MASK;
                int ctrlMask = MouseEvent.CTRL_DOWN_MASK;
                if ((mask & (shiftMask | ctrlMask)) == (shiftMask | ctrlMask))
                    selMode = EditorFrame.SelectionMode.INVERT;
                else if ((mask & shiftMask) == shiftMask)
                    selMode = EditorFrame.SelectionMode.ADDITIVE;
                else if ((mask & ctrlMask) == ctrlMask)
                    selMode = EditorFrame.SelectionMode.SUBTRACTIVE;
                else
                    selMode = EditorFrame.SelectionMode.REPLACE;
                parent.setSelectionDown(selMode, initSelectionType, selT1, selT2, false);
            }
        }
        parent.queueRender();
    }

    public void mouseReleased(MouseEvent e) {
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        if (curHover != -1)
            curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (selectionState == SelectionState.MOVE) {
                selectionState = SelectionState.NONE;
                parent.clearDrag();
            }
            if (selectionState == SelectionState.MOVE_CHECKPOINT) {
                selectionState = SelectionState.NONE;
                parent.clearDrag();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (selectionState == SelectionState.SELECT) {
                selectionState = SelectionState.NONE;
                selT2 = ((float) e.getX()) / width * (t2 - t1) + t1;
                parent.setSelectionDown(selMode, initSelectionType, Math.min(selT1, selT2), Math.max(selT1, selT2), true);
                parent.setSelectionUp();
            }
        }
        parent.queueRender();
    }

    public void mouseEntered(MouseEvent e) {
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
        parent.queueRender();
    }

    public void mouseExited(MouseEvent e) {
        curHover = -1;
        parent.queueRender();
    }

    public void mouseDragged(MouseEvent e) {
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            switch (e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) {
                case MouseEvent.SHIFT_DOWN_MASK:
                    parent.deleteClosest(((float) e.getX()) / width * (t2 - t1) + t1, MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))]);
                    break;
                case MouseEvent.CTRL_DOWN_MASK:
                    parent.deleteClosestCheckpoint(((float) e.getX()) / width * (t2 - t1) + t1);
                    break;
                case (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK):
                    if (selectionState == SelectionState.NONE) {
                        selectionState = SelectionState.MOVE_CHECKPOINT;
                        selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                        parent.prepareCheckpointDrag(selT1);
                    }
                    if (selectionState == SelectionState.MOVE_CHECKPOINT) {
                        selT2 = ((float) e.getX()) / width * (t2 - t1) + t1;
                        parent.move(selT2 - selT1);
                        selT1 = selT2;
                    }
                    break;
                default:
                    if (selectionState == SelectionState.NONE) {
                        selectionState = SelectionState.MOVE;
                        initSelectionType = MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))];
                        selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                        parent.prepareDrag(selT1, initSelectionType);
                    }
                    if (selectionState == SelectionState.MOVE) {
                        selT2 = ((float) e.getX()) / width * (t2 - t1) + t1;
                        parent.move(selT2 - selT1);
                        selT1 = selT2;
                    }
                    break;
            }
        } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
            if (selectionState == SelectionState.NONE) {
                selectionState = SelectionState.SELECT;
                initSelectionType = MapEvent.Type.values()[Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))))];
                selT1 = ((float) e.getX()) / width * (t2 - t1) + t1;
                int mask = e.getModifiersEx();
                int shiftMask = MouseEvent.SHIFT_DOWN_MASK;
                int ctrlMask = MouseEvent.CTRL_DOWN_MASK;
                if ((mask & (shiftMask | ctrlMask)) == (shiftMask | ctrlMask))
                    selMode = EditorFrame.SelectionMode.INVERT;
                else if ((mask & shiftMask) == shiftMask)
                    selMode = EditorFrame.SelectionMode.ADDITIVE;
                else if ((mask & ctrlMask) == ctrlMask)
                    selMode = EditorFrame.SelectionMode.SUBTRACTIVE;
                else
                    selMode = EditorFrame.SelectionMode.REPLACE;
            }
            if (selectionState == SelectionState.SELECT) {
                selT2 = ((float) e.getX()) / width * (t2 - t1) + t1;
                parent.setSelectionDown(selMode, initSelectionType, Math.min(selT1, selT2), Math.max(selT1, selT2), false);
            }
        }
        parent.queueRender();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void mouseMoved(MouseEvent e) {
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        if (curHover != -1)
            curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
        repaint();
    }

    public void localize(Locale l) {
        recording = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING);
        waiting = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_WAITING);
        recordingRemap = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING_REMAP);
        waitingRemap = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_WAITING_REMAP);
        typeTitle = l.getKey(KEY_PANEL_EVENT_DISPLAY_TYPE_TITLE);
    }

    private enum SelectionState {
        SELECT,
        MOVE,
        MOVE_CHECKPOINT,
        NONE
    }
}
