package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class PlaybackControlPanel extends JPanel implements MouseListener, MouseMotionListener {

    private EditorFrame parent;

    public PlaybackControlPanel(EditorFrame parent) {
        this.parent = parent;

        addMouseListener(this);
        addMouseMotionListener(this);
        setMinimumSize(new Dimension(30, 30));
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            parent.setPlayHeadPercent(((float) e.getX()) / getWidth());
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
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            parent.setPlayHeadPercent(((float) e.getX()) / getWidth());
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
