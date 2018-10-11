package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.swing.*;

public class ControlReferenceFrame extends JFrame {

    private static final String KEY_FRAME_CONTROL_REFERENCE_TITLE = "frame_control_reference_title";
    private static final String KEY_FRAME_CONTROL_REFERENCE_TEXT = "frame_control_reference_text";


    private JTextArea text;

    private JScrollPane scrollPane;

    public ControlReferenceFrame() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        text = new JTextArea();
        text.setEditable(false);
        scrollPane = new JScrollPane(text);
        add(scrollPane);
        IRiina.brandFrameWithGloriousEmblem(this);
    }

    public void showFrame() {
        setSize(400, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void hideFrame() {
        setVisible(false);
    }

    public void localize(Locale l) {
        setTitle(l.getKey(KEY_FRAME_CONTROL_REFERENCE_TITLE));
        text.setText(l.getKey(KEY_FRAME_CONTROL_REFERENCE_TEXT));
    }

}
