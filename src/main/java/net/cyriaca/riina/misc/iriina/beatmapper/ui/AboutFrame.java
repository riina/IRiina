package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiinaConstants;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeEvent;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeListener;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Scanner;

public class AboutFrame extends JFrame implements IViewFrame, LocaleChangeListener {

    private static final String KEY_FRAME_ABOUT_TITLE = "frame_about_title";
    private static final String KEY_FRAME_ABOUT_TEXT_ABOUT_HEADER = "frame_about_text_about_header";
    private static final String KEY_FRAME_ABOUT_TEXT_ABOUT = "frame_about_text_about";
    private static final String KEY_FRAME_ABOUT_TEXT_CONTACT_HEADER = "frame_about_text_contact_header";
    private static final String KEY_FRAME_ABOUT_TEXT_CONTACT = "frame_about_text_contact";
    private static final String KEY_FRAME_ABOUT_TEXT_LICENSES_HEADER = "frame_about_text_licenses_header";
    private Locale l;
    private JTabbedPane tabbedPane;
    private JTextArea aboutArea;
    private JPanel aboutTab;
    private JTextArea contactArea;
    private JPanel contactTab;
    private JPanel licensesTab;

    public AboutFrame(IRiina parent) {
        super();
        l = parent.getLocale();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        InputStream stream2 = AboutFrame.class.getClassLoader().getResourceAsStream(IRiinaConstants.VERSION_FILE_LOC);
        Scanner s2 = new Scanner(stream2);
        s2.useDelimiter("\\Z");
        String versionStr = s2.next();
        s2.close();
        JLabel header = new JLabel("IRiina " + versionStr);
        header.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        headerPanel.add(header);
        add(headerPanel);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        BorderLayout aboutLayout = new BorderLayout(10, 10);
        aboutTab = new JPanel();
        aboutTab.setLayout(aboutLayout);
        aboutArea = new JTextArea();
        aboutArea.setEditable(false);
        aboutArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT));
        aboutArea.setLineWrap(true);

        JScrollPane aboutScrollPane = new JScrollPane(aboutArea);
        JScrollBar aboutScrollBar = aboutScrollPane.createVerticalScrollBar();
        aboutScrollPane.setVerticalScrollBar(aboutScrollBar);
        aboutScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        aboutTab.add(aboutScrollPane, BorderLayout.CENTER);

        aboutTab.add(aboutScrollPane, BorderLayout.CENTER);
        BorderLayout contactLayout = new BorderLayout(10, 10);
        contactTab = new JPanel();
        contactTab.setLayout(contactLayout);
        contactArea = new JTextArea();
        contactArea.setEditable(false);
        contactArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT));
        contactArea.setLineWrap(true);

        JScrollPane contactScrollPane = new JScrollPane(contactArea);
        JScrollBar contactScrollBar = contactScrollPane.createVerticalScrollBar();
        contactScrollPane.setVerticalScrollBar(contactScrollBar);
        contactScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactTab.add(contactScrollPane, BorderLayout.CENTER);

        BorderLayout licensesLayout = new BorderLayout(10, 10);
        licensesTab = new JPanel();
        licensesTab.setLayout(licensesLayout);
        JTextArea licensesArea = new JTextArea();
        licensesArea.setEditable(false);
        InputStream stream = AboutFrame.class.getClassLoader().getResourceAsStream(IRiinaConstants.LICENSE_FILE_LOC);
        Scanner s = new Scanner(stream);
        s.useDelimiter("\\Z");
        String licensesStr = s.next();
        s.close();
        licensesArea.setText(licensesStr);
        licensesArea.setLineWrap(true);

        JScrollPane licensesScrollPane = new JScrollPane(licensesArea);
        JScrollBar licensesScrollBar = licensesScrollPane.createVerticalScrollBar();
        licensesScrollPane.setVerticalScrollBar(licensesScrollBar);
        licensesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        licensesTab.add(licensesScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT_HEADER), aboutTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT_HEADER), contactTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_LICENSES_HEADER), licensesTab);
        add(tabbedPane);
    }

    public void setupAndShowFrame() {
        setTitle(l.getKey(KEY_FRAME_ABOUT_TITLE));
        setSize(600, 300);
        for (int i = 0; i < tabbedPane.getTabCount(); i++)
            tabbedPane.removeTabAt(0);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT_HEADER), aboutTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT_HEADER), contactTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_LICENSES_HEADER), licensesTab);
        aboutArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT));
        contactArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_ABOUT_TITLE));
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT_HEADER), aboutTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT_HEADER), contactTab);
        tabbedPane.addTab(l.getKey(KEY_FRAME_ABOUT_TEXT_LICENSES_HEADER), licensesTab);
        aboutArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_ABOUT));
        if (!aboutArea.getText().contains("CorruptTurret") || !contactArea.getText().contains("\nhttps://discord.gg/425Me2c\n")) {
            aboutArea.setText("Original program by CorruptTurret\nv0.0.8 maintenance by Riina\nv0.0.9+ hosted on GitHub at\nhttps://github.com/cyriaca-technologies/IRiina");
            contactArea.setText("Discord:\nhttps://discord.gg/425Me2c\n");
        }
        contactArea.setText(l.getKey(KEY_FRAME_ABOUT_TEXT_CONTACT));
    }

}
