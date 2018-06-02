package net.cyriaca.riina.misc.iriina.editor.ui.editor;

import net.cyriaca.riina.misc.iriina.editor.editor.data.ArcRenderItem;
import net.cyriaca.riina.misc.iriina.editor.editor.data.EditorConfig;
import net.cyriaca.riina.misc.iriina.editor.editor.data.EventRenderItem;
import net.cyriaca.riina.misc.iriina.editor.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class EventDisplayPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING = "panel_event_display_state_recording";
    private static final String KEY_PANEL_EVENT_DISPLAY_STATE_WAITING = "panel_event_display_state_waiting";
    private static final String KEY_PANEL_EVENT_DISPLAY_TYPE_TITLE = "panel_event_display_type_title";
    private static final String TYPE = "%type%";
    private static final String SELCOUNT = "%selCount%";
    private EditorFrame parent;
    private String recording = null;
    private String waiting = null;
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

        if ((e.getModifiersEx() & (KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.SHIFT_DOWN_MASK)) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_I) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_J) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_K) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_LEFT + ArcProperty.MASK_DOWN));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT + ArcProperty.MASK_DOWN));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_U) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_LEFT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_O) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_UP + ArcProperty.MASK_RIGHT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_M) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_COMMA) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_PERIOD) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_RIGHT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_P) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_DOWN + ArcProperty.MASK_UP));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask((byte) (ArcProperty.MASK_LEFT + ArcProperty.MASK_RIGHT));
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_I) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask(ArcProperty.MASK_UP);
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_J) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask(ArcProperty.MASK_LEFT);
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_K) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask(ArcProperty.MASK_DOWN);
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L) {
                MapEvent evt = new SpawnObjEvent(parent.getPlayHeadPos());
                evt.getArcProperty().setMask(ArcProperty.MASK_RIGHT);
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_U) {
                MapEvent evt = new TimingEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_O) {
                MapEvent evt = new ShowTitleEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_M) {
                MapEvent evt = new ShowSpriteEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_COMMA) {
                MapEvent evt = new SetPlayerDistanceEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_PERIOD) {
                MapEvent evt = new SetBGColorEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_P) {
                MapEvent evt = new ComboEvent(parent.getPlayHeadPos());
                data.addNewEvent(evt);
                parent.addOperationForEventAdd(evt);
            }
            if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_SEMICOLON) {
                Checkpoint checkpoint = new Checkpoint(parent.getPlayHeadPos(), parent.getMapData());
                data.addCheckpoint(checkpoint);
                parent.addOperationForCheckpointAdd(checkpoint);
            }
        }

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
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        if (message != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.setBackground(Color.BLACK);
            super.paintComponent(g);
            g.setColor(Color.CYAN);
            g.fillRect(0, (h / 2) - 10, w, 20);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(message);
            int th = fm.getHeight();
            g2.setColor(Color.BLACK);
            g2.drawString(message, w / 2.0f - tw / 2.0f, h / 2.0f + th / 4.0f);
            return;
        }

        parent.updateEventDisplayPanel();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(new Font("SansSerif", Font.PLAIN, 20));

        float centerX = (float) w / 2.0f;
        float centerY = (float) h / 2.0f;

        float minRad = Math.min((float) w, (float) h) / 2;

        super.setBackground(Color.BLACK);
        super.paintComponent(g);
        if (parent.getRenderArcs() || parent.getRenderBeats()) {
            if (arcRenderItem != null) {
                float outsideRad = Math.min((float) w, (float) h) / 2.0f;
                float middleRad = outsideRad * 0.75f;
                float middleInsideRad = outsideRad * 0.50f;
                float insideRad = outsideRad * 0.25f;
                float outsideTopLeftX = centerX - outsideRad;
                float outsideTopLeftY = centerY - outsideRad;
                float middleTopLeftX = centerX - middleRad;
                float middleTopLeftY = centerY - middleRad;
                float middleInsideTopLeftX = centerX - middleInsideRad;
                float middleInsideTopLeftY = centerY - middleInsideRad;
                float insideTopLeftX = centerX - insideRad;
                float insideTopLeftY = centerY - insideRad;
                float[] arr = arcRenderItem.getLevels();
                if (parent.getRenderArcs()) {
                    //1: up
                    g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[1]));
                    g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 44.9f, 90.2f, Arc2D.PIE));

                    //2: right
                    g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[2]));
                    g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 314.9f, 90.2f, Arc2D.PIE));

                    //3: down
                    g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[3]));
                    g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 224.9f, 90.2f, Arc2D.PIE));

                    //4: left
                    g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[4]));
                    g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 134.9f, 90.2f, Arc2D.PIE));

                    g2.setColor(Color.BLACK);
                    g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));

                    if (parent.getRenderBeats()) {
                        float[] arr2 = arcRenderItem.getTicks();
                        if (arr2 != null) {
                            float targSize = 360.0f / arr2.length;
                            for (int i = 0; i < arr2.length; i++) {
                                g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr2[i]));
                                g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, -(i * targSize), targSize + 0.2f, Arc2D.PIE));
                            }
                        }
                    } else {
                        g2.setColor(Color.BLACK);
                        g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));
                    }

                    g2.setColor(Color.BLACK);
                    g2.fill(new Arc2D.Float(middleInsideTopLeftX, middleInsideTopLeftY, middleInsideRad * 2.0f, middleInsideRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));

                    //0: powerup
                    g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr[0]));
                    g2.fill(new Arc2D.Float(insideTopLeftX, insideTopLeftY, insideRad * 2.0f, insideRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));

                } else {
                    if (parent.getRenderBeats()) {
                        float[] arr2 = arcRenderItem.getTicks();
                        if (arr2 != null) {
                            float targSize = 360.0f / arr2.length;
                            for (int i = 0; i < arr2.length; i++) {
                                g2.setColor(getColorForThing(Color.BLACK, Color.CYAN, arr2[i]));
                                g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, -(i * targSize), targSize + 0.2f, Arc2D.PIE));
                            }
                        }
                    } else {
                        g2.setColor(Color.BLACK);
                        g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));
                    }
                    g2.setColor(Color.BLACK);
                    g2.fill(new Arc2D.Float(middleInsideTopLeftX, middleInsideTopLeftY, middleInsideRad * 2.0f, middleInsideRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));

                }
            }
        } else
            g2.drawImage(tempImg, (int) (((float) w / 2.0f) - minRad), (int) (((float) h / 2) - minRad), (int) (minRad * 2.0f), (int) (minRad * 2.0f), null);
        if (parent.getRenderEvents() && eventRenderItems != null) {
            EditorConfig cfg = parent.getConfig();
            float rowHeight = cfg.getEventRowHeight();
            float playHeadWidth = cfg.getPlayheadWidth();
            float eventHeight = (float) h / 8.0f;
            float eventWidth = cfg.getEventWidth();
            g2.setColor(Color.GRAY);
            g2.fill(new Rectangle2D.Float(0, ((float) h * 0.0f / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f), (float) w, rowHeight));
            g2.fill(new Rectangle2D.Float(0, ((float) h * 1.0f / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f), (float) w, rowHeight));
            g2.fill(new Rectangle2D.Float(0, ((float) h * 2.0f / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f), (float) w, rowHeight));
            g2.fill(new Rectangle2D.Float(0, ((float) h * 3.0f / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f), (float) w, rowHeight));
            g2.fill(new Rectangle2D.Float(((float) w / 2.0f) - (playHeadWidth / 2.0f), 0, playHeadWidth, (float) h));
            MapEvent.Type[] types = MapEvent.Type.values();

            g2.setColor(Color.LIGHT_GRAY);
            for (Float f : checkpointItems) {
                g2.fill(new Rectangle2D.Float((f - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), 0, eventWidth, (float) h));
            }

            for (EventRenderItem item : eventRenderItems) {
                int yMod = item.getType().ordinal();
                if (item.isSelected())
                    g2.setColor(EditorFrame.INDIGO);
                else
                    g2.setColor(item.getMainColor());
                if (item.getType() != MapEvent.Type.TIMING)
                    g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), eventWidth, eventHeight));
                else
                    g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight));
                Color topColor = item.getTopColor();
                if (topColor != null) {
                    g2.setColor(topColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), eventWidth, eventHeight / 2.0f));
                    else
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight / 2.0f));
                }
                Color botColor = item.getBotColor();
                if (botColor != null) {
                    g2.setColor(botColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f), eventWidth, eventHeight / 2.0f));
                    else
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight / 2.0f));
                }
                if (item.getType() == MapEvent.Type.TIMING) {
                    Color mainColor = item.getMainColor();
                    g2.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 120));
                    g2.fill(new Rectangle2D.Float((item.getRootTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), 0, eventWidth, (float) h));
                }
            }

            for (EventRenderItem item : targetEventRenderItems) {
                int yMod = item.getType().ordinal();
                if (item.isSelected())
                    g2.setColor(EditorFrame.INDIGO);
                else
                    g2.setColor(item.getMainColor());
                if (item.getType() != MapEvent.Type.TIMING)
                    g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), eventWidth, eventHeight));
                else
                    g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight));
                Color topColor = item.getTopColor();
                if (topColor != null) {
                    g2.setColor(topColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), eventWidth, eventHeight / 2.0f));
                    else
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f) - (eventHeight / 2.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight / 2.0f));
                }
                Color botColor = item.getBotColor();
                if (botColor != null) {
                    g2.setColor(botColor);
                    if (item.getType() != MapEvent.Type.TIMING)
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f), eventWidth, eventHeight / 2.0f));
                    else
                        g2.fill(new Rectangle2D.Float((item.getStartTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), ((float) h * ((float) yMod) / 4.0f) + ((float) h / 8.0f), (item.getEndTime() - item.getStartTime()) / (t2 - t1) * (float) w + eventWidth, eventHeight / 2.0f));
                }
                if (item.getType() == MapEvent.Type.TIMING) {
                    Color mainColor = item.getMainColor();
                    g2.setColor(new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 120));
                    g2.fill(new Rectangle2D.Float((item.getRootTime() - t1) / (t2 - t1) * (float) w - (eventWidth / 2.0f), 0, eventWidth, (float) h));
                }
            }

            g.setColor(Color.WHITE);
            for (int i = 0; i < types.length; i++) {
                g.drawString(typeTitle.replaceAll(TYPE, types[i].name()).replaceAll(SELCOUNT, Integer.toString(selCount[i])), 0, (int) (((float) h * i / 4.0f) + ((float) h / 8.0f) - (rowHeight / 2.0f)));
            }
        }
        if (curHover != -1) {
            Color col = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100);
            g2.setColor(col);
            g2.fill(new Rectangle2D.Float(0.0f, curHover * (float) h / 4.0f, (float) w, (float) h / 4.0f));
            g2.drawLine((int) ((curTimeHover - t1) / (t2 - t1) * (float) w), 0, (int) ((curTimeHover - t1) / (t2 - t1) * (float) w), (int) (float) h);
        }
        if (selectionState == SelectionState.SELECT) {
            Color col = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 100);
            g2.setColor(col);
            g2.fill(new Rectangle2D.Float((Math.min(selT1, selT2) - t1) / (t2 - t1) * (float) w, ((float) h * ((float) initSelectionType.ordinal()) / 4.0f), Math.abs(selT2 - selT1) / (t2 - t1) * (float) w, (float) h / 4.0f));
        }
        g.setColor(Color.WHITE);
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)
            g.drawString(recording, 0, getHeight());
        else
            g.drawString(waiting, 0, getHeight());
        g.setColor(Color.WHITE);
        g.drawString(Float.toString(time), getWidth() / 2, getHeight());
    }

    public void pretendLikeIWasntSelectingAnything() {
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
    }

    public void mouseEntered(MouseEvent e) {
        parent.updateEventDisplayPanelTimes();
        float width = getWidth();
        curHover = Math.max(0, Math.min(3, (int) (4.0f * ((float) e.getY()) / ((float) getHeight()))));
        curTimeHover = ((float) e.getX()) / width * (t2 - t1) + t1;
    }

    public void mouseExited(MouseEvent e) {
        curHover = -1;
    }

    public void mouseDragged(MouseEvent e) {
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
                if (selectionState == SelectionState.MOVE_CHECKPOINT) {
                    selT2 = ((float) e.getX()) / width * (t2 - t1) + t1;
                    parent.move(selT2 - selT1);
                    selT1 = selT2;
                }
            } else {
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
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
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
    }

    public void localize(Locale l) {
        recording = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_RECORDING);
        waiting = l.getKey(KEY_PANEL_EVENT_DISPLAY_STATE_WAITING);
        typeTitle = l.getKey(KEY_PANEL_EVENT_DISPLAY_TYPE_TITLE);
    }

    private enum SelectionState {
        SELECT,
        MOVE,
        MOVE_CHECKPOINT,
        NONE
    }
}
