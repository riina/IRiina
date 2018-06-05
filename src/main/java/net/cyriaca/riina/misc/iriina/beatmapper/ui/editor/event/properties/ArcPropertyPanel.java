package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.event.properties;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.ArcProperty;
import net.cyriaca.riina.misc.iriina.intralism.data.MapEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
                int w = getWidth();
                int h = getHeight();
                int centerX = w / 2;
                int centerY = h / 2;

                int outsideRad = Math.min(w, h) / 2;
                int middleRad = (int) (outsideRad * 0.75f);
                int insideRad = (int) (outsideRad * 0.25f);
                int outsideTopLeftX = centerX - outsideRad;
                int outsideTopLeftY = centerY - outsideRad;
                int middleTopLeftX = centerX - middleRad;
                int middleTopLeftY = centerY - middleRad;
                int insideTopLeftX = centerX - insideRad;
                int insideTopLeftY = centerY - insideRad;
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_UP))
                    g.setColor(Color.CYAN);
                else
                    g.setColor(Color.BLUE);
                g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 45, 90);
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_RIGHT))
                    g.setColor(Color.CYAN);
                else
                    g.setColor(Color.BLUE);
                g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 315, 90);
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_DOWN))
                    g.setColor(Color.CYAN);
                else
                    g.setColor(Color.BLUE);
                g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 225, 90);
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_LEFT))
                    g.setColor(Color.CYAN);
                else
                    g.setColor(Color.BLUE);
                g.fillArc(outsideTopLeftX, outsideTopLeftY, outsideRad * 2, outsideRad * 2, 135, 90);
                g.setColor(getBackground());
                g.fillOval(middleTopLeftX, middleTopLeftY, middleRad * 2, middleRad * 2);
                if (arcProperty.containsArcFromMask(ArcProperty.MASK_POWER_UP))
                    g.setColor(Color.CYAN);
                else
                    g.setColor(Color.BLUE);
                g.fillOval(insideTopLeftX, insideTopLeftY, insideRad * 2, insideRad * 2);
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
