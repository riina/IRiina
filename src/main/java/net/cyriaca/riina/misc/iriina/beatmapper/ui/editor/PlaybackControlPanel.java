package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class PlaybackControlPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int PLAYBACK_BAR_HEIGHT = 30;

    private EditorFrame parent;

    public PlaybackControlPanel(EditorFrame parent) {
        this.parent = parent;

        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(-1, PLAYBACK_BAR_HEIGHT));
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            parent.setPlayHeadPercent(((float) e.getX()) / getWidth());
            parent.queueRender();
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(parent.isPlaying() ? Color.RED : Color.GREEN);
        g.fillRect(0, 0, (int) (getWidth() * parent.getPlayHeadPercent()), getHeight());
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            parent.togglePlaying();
            parent.queueRender();
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            parent.setPlayHeadPercent(((float) e.getX()) / getWidth());
            parent.queueRender();
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
