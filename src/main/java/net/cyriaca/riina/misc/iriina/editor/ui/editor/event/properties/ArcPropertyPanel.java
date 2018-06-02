package net.cyriaca.riina.misc.iriina.editor.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.editor.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.ArcProperty;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;

public class ArcPropertyPanel extends PropertyPanel {

    private static final String KEY_PANEL_ARC_PROPERTY_NAME = "panel_arc_property_name";

    private ArcProperty arcProperty;
    private JLabel title;
    private ArcRenderPanel arcRenderPanel;
    private EditorFrame host;

    public ArcPropertyPanel() {
        title = new JLabel("Arc");
        arcProperty = null;
        host = null;
        title.setFont(TITLE_FONT);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        arcRenderPanel = new ArcRenderPanel();
        add(arcRenderPanel, BorderLayout.CENTER);
        setVisible(false);
    }

    public void setArcProperty(ArcProperty arcProperty) {
        if (this.arcProperty != null) {
        }
        this.arcProperty = arcProperty;
        if (this.arcProperty != null) {
            arcRenderPanel.setArcProperty(this.arcProperty);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void setHost(EditorFrame host) {
        this.host = host;
    }

    public void writeValues() {
    }

    public void localize(Locale l) {
        title.setText(l.getKey(KEY_PANEL_ARC_PROPERTY_NAME));
    }

    public void update() {
        if (arcProperty != null) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    class ArcRenderPanel extends JLabel implements MouseListener {

        private ArcProperty arcProperty;

        ArcRenderPanel() {
            arcProperty = null;
            setPreferredSize(new Dimension(100, 100));
            addMouseListener(this);
        }

        void setArcProperty(ArcProperty arcProperty) {
            this.arcProperty = arcProperty;
        }

        public void paintComponent(Graphics g) {
            if (arcProperty != null) {
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                float centerX = (float) w / 2.0f;
                float centerY = (float) h / 2.0f;

                float outsideRad = Math.min((float) w, (float) h) / 2.0f;
                float middleRad = outsideRad * 0.75f;
                float insideRad = outsideRad * 0.25f;
                float outsideTopLeftX = centerX - outsideRad;
                float outsideTopLeftY = centerY - outsideRad;
                float middleTopLeftX = centerX - middleRad;
                float middleTopLeftY = centerY - middleRad;
                float insideTopLeftX = centerX - insideRad;
                float insideTopLeftY = centerY - insideRad;
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_UP))
                    g2.setColor(Color.CYAN);
                else
                    g2.setColor(Color.BLUE);
                g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 44.9f, 90.2f, Arc2D.PIE));
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_RIGHT))
                    g2.setColor(Color.CYAN);
                else
                    g2.setColor(Color.BLUE);
                g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 314.9f, 90.2f, Arc2D.PIE));
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_DOWN))
                    g2.setColor(Color.CYAN);
                else
                    g2.setColor(Color.BLUE);
                g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 224.9f, 90.2f, Arc2D.PIE));
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_LEFT))
                    g2.setColor(Color.CYAN);
                else
                    g2.setColor(Color.BLUE);
                g2.fill(new Arc2D.Float(outsideTopLeftX, outsideTopLeftY, outsideRad * 2.0f, outsideRad * 2.0f, 134.9f, 90.2f, Arc2D.PIE));
                g2.setColor(getBackground());
                g2.fill(new Arc2D.Float(middleTopLeftX, middleTopLeftY, middleRad * 2.0f, middleRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_POWER_UP))
                    g2.setColor(Color.CYAN);
                else
                    g2.setColor(Color.BLUE);
                g2.fill(new Arc2D.Float(insideTopLeftX, insideTopLeftY, insideRad * 2.0f, insideRad * 2.0f, 0.0f, 360.0f, Arc2D.PIE));
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            int mx = e.getX();
            int my = e.getY();

            float width = getWidth();
            float height = getHeight();
            float centerX = width / 2.0f;
            float centerY = height / 2.0f;
            float outsideRad = Math.min(width, height) / 2.0f;
            float middleRad = outsideRad * 0.75f;
            float insideRad = outsideRad * 0.25f;

            float dist = dist(mx, my, centerX, centerY);
            boolean mod = false;
            MapEvent evt = arcProperty.getParent();
            String oldData = evt.getEventData();
            String oldExtraData = evt.getEventExtraData();
            float oldTime = evt.getTime();
            if (dist <= insideRad) {
                arcProperty.toggleValues(ArcProperty.MASK_POWER_UP);
                mod = true;
            } else if (dist <= outsideRad && dist >= middleRad) {
                if (my - centerY <= 0) {
                    if (mx - centerX >= 0) {
                        if (Math.abs(my - centerY) <= Math.abs(mx - centerX)) {
                            arcProperty.toggleValues(ArcProperty.MASK_RIGHT);
                        } else {
                            arcProperty.toggleValues(ArcProperty.MASK_UP);
                        }
                    } else {
                        if (Math.abs(my - centerY) <= Math.abs(mx - centerX)) {
                            arcProperty.toggleValues(ArcProperty.MASK_LEFT);
                        } else {
                            arcProperty.toggleValues(ArcProperty.MASK_UP);
                        }
                    }
                } else {
                    if (mx - centerX >= 0) {
                        if (Math.abs(my - centerY) <= Math.abs(mx - centerX)) {
                            arcProperty.toggleValues(ArcProperty.MASK_RIGHT);
                        } else {
                            arcProperty.toggleValues(ArcProperty.MASK_DOWN);
                        }
                    } else {
                        if (Math.abs(my - centerY) <= Math.abs(mx - centerX)) {
                            arcProperty.toggleValues(ArcProperty.MASK_LEFT);
                        } else {
                            arcProperty.toggleValues(ArcProperty.MASK_DOWN);
                        }
                    }
                }
                mod = true;
            }
            if (mod) {
                String data = evt.getEventData();
                String extraData = evt.getEventExtraData();
                float time = evt.getTime();
                host.addOperationForEventDataMod(arcProperty.getParent(), oldData, oldExtraData, oldTime, data, extraData, time);
            }
        }

        private float dist(float x1, float y1, float x2, float y2) {
            return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
