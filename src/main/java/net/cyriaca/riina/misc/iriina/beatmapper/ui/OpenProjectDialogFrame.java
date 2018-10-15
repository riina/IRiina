package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiinaConstants;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeEvent;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeListener;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ConfigDef;
import net.cyriaca.riina.misc.iriina.beatmapper.data.LoadTarget;
import net.cyriaca.riina.misc.iriina.generic.FileDialogs;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class OpenProjectDialogFrame extends JFrame
        implements IViewFrame, LocaleChangeListener, ActionListener, WindowListener {

    private static final String KEY_FRAME_OPEN_PROJECT_TITLE = "frame_open_project_title";
    private static final String KEY_FRAME_OPEN_PROJECT_SELECTOR_TEXT = "frame_open_project_selector_text";
    private static final String KEY_FRAME_OPEN_PROJECT_LAUNCH = "frame_open_project_launch";

    private IRiina parent;
    private JPanel contentPane;
    private ArxTitledComboBoxItem<ConfigDef> selector;
    private JButton launch;

    public OpenProjectDialogFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        selector = new ArxTitledComboBoxItem<>();
        add(selector, BorderLayout.NORTH);
        launch = new JButton("Launch Editor");
        add(launch, BorderLayout.SOUTH);
        launch.addActionListener(this);
        addWindowListener(this);
        IRiina.brandFrameWithGloriousEmblem(this);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void setupAndShowFrame() {
        setSize(500, 140);
        setLocationRelativeTo(null);
        setVisible(true);
        File projectDirectory = FileDialogs.folderDialog(this, new File(parent.getPreferences().getWorkspaceDirectory()));
        if (projectDirectory == null) {
            parent.setView(View.WelcomeFrame);
            return;
        }
        File configFile = new File(projectDirectory, "config.txt");
        if (configFile.exists())
            selector.addItem(new ConfigDef(ConfigDef.Type.CONFIG, projectDirectory, configFile));
        File backupDirectory = new File(projectDirectory, IRiinaConstants.PROJECT_DIR_BACKUP);
        if (backupDirectory.exists() && backupDirectory.isDirectory()) {
            File[] backupDirItems = backupDirectory.listFiles(File::isFile);
            for (File backupDirItem : backupDirItems != null ? backupDirItems : new File[0]) {
                selector.addItem(new ConfigDef(ConfigDef.Type.BACKUP, projectDirectory, backupDirItem));
            }
        }
        getRootPane().setDefaultButton(launch);
    }

    public void cleanupAndHideFrame() {
        selector.clearElements();
        setVisible(false);
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_OPEN_PROJECT_TITLE));
        selector.setTitle(l.getKey(KEY_FRAME_OPEN_PROJECT_SELECTOR_TEXT));
        launch.setText(l.getKey(KEY_FRAME_OPEN_PROJECT_LAUNCH));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == launch) {
            ConfigDef def = selector.getSelectedItem();
            if (def != null) {
                parent.loadProject(new LoadTarget(def.getProjectDirectory(), def.getConfigFile(), null, null, def.getType() == ConfigDef.Type.BACKUP));
            }
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        parent.setView(View.WelcomeFrame);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
