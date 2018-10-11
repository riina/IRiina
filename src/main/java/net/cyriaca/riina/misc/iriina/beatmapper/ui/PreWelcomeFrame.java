package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeEvent;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeListener;
import net.cyriaca.riina.misc.iriina.beatmapper.data.IRiinaPreferences;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.localization.LocaleDefinition;
import net.cyriaca.riina.misc.iriina.generic.localization.LocaleSystem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledBooleanItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledComboBoxItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFolderSelectorItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.Collections;

public class PreWelcomeFrame extends JFrame
        implements IViewFrame, LocaleChangeListener, WindowListener, ActionListener {

    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_FRAME_PRE_WELCOME_TITLE = "frame_pre_welcome_title";
    private static final String KEY_FRAME_PRE_WELCOME_TEXT_LANGUAGE = "frame_pre_welcome_text_language";
    private static final String KEY_FRAME_PRE_WELCOME_TEXT_WORKSPACE_DIRECTORY = "frame_pre_welcome_text_workspace_directory";
    private static final String KEY_FRAME_PRE_WELCOME_TEXT_WORKSPACE_DIRECTORY_SELECT = "frame_pre_welcome_text_workspace_directory_select";
    private static final String KEY_FRAME_PRE_WELCOME_TEXT_SHOW_PRE_WELCOME_ON_START = "frame_pre_welcome_text_show_pre_welcome_on_start";
    private static final String KEY_FRAME_PRE_WELCOME_TEXT_OPEN_EDITOR = "frame_pre_welcome_text_open_editor";
    private static final String KEY_FRAME_PRE_WELCOME_SAVE_FAIL_WORKSPACE_DIRECTORY_USE_FAILED = "frame_pre_welcome_save_fail_workspace_directory_use_failed";
    private IRiina parent;
    private ArxTitledComboBoxItem<LocaleDefinition> localeSelect;
    private ArxTitledFolderSelectorItem workspaceSelector;
    private ArxTitledBooleanItem showPreWelcome;
    private JButton editorButton;

    private JPanel contentPane;
    private JPanel holder;
    private JScrollPane scrollPane;

    private Collection<LocaleDefinition> localeCollection;

    public PreWelcomeFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        holder = new JPanel();
        BoxLayout bl = new BoxLayout(holder, BoxLayout.Y_AXIS);
        holder.setLayout(bl);

        IRiinaPreferences prefs = parent.getPreferences();
        localeCollection = Collections.unmodifiableCollection(LocaleSystem.getLocales().values());
        LocaleDefinition initSelection = null;
        for (LocaleDefinition aLocaleCollection : localeCollection) {
            String loc = prefs.getLocale();
            LocaleDefinition ld = aLocaleCollection;
            if (loc.equals(ld.getLocaleId()))
                initSelection = ld;
        }
        localeSelect = new ArxTitledComboBoxItem<>(localeCollection, initSelection);
        localeSelect.addActionListener(this);
        workspaceSelector = new ArxTitledFolderSelectorItem();

        showPreWelcome = new ArxTitledBooleanItem();

        editorButton = new JButton();
        editorButton.addActionListener(this);

        holder.add(localeSelect);
        holder.add(workspaceSelector);
        holder.add(showPreWelcome);

        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(holder, BorderLayout.NORTH);
        scrollPane = new JScrollPane(minHolder);

        add(scrollPane, BorderLayout.CENTER);

        add(editorButton, BorderLayout.SOUTH);
        addWindowListener(this);
        IRiina.brandFrameWithGloriousEmblem(this);
    }

    public void setupAndShowFrame() {
        setSize(640, 300);
        setLocationRelativeTo(null);
        IRiinaPreferences prefs = parent.getPreferences();
        String loc = prefs.getLocale();
        for (LocaleDefinition ld : localeCollection) {
            if (loc.equals(ld.getLocaleId()))
                localeSelect.setSelectedItem(ld);
        }
        workspaceSelector.setDirectory(prefs.getWorkspaceDirectory());
        showPreWelcome.setSelected(prefs.getShowPreWelcomeWindow());
        setVisible(true);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (updatePrefsToParent(parent.getPreferences()))
            System.exit(0);
    }

    private boolean updatePrefsToParent(IRiinaPreferences prev) {
        IRiinaPreferences prefs = new IRiinaPreferences();
        prefs.setLocale(localeSelect.getSelectedItem().getLocaleId());
        if (!prefs.setWorkspaceDirectory(workspaceSelector.getDirectory()))
            return false;
        prefs.setShowPreWelcomeWindow(showPreWelcome.getSelected());
        prefs.setEditorConfig(prev.getEditorConfig());
        parent.updatePreferences(prefs);
        return true;
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == localeSelect) {
            IRiinaPreferences prefs = parent.getPreferences().clone();
            prefs.setLocale(localeSelect.getSelectedItem().getLocaleId());
            parent.updatePreferences(prefs);
        } else if (e.getSource() == editorButton) {
            if (updatePrefsToParent(parent.getPreferences()))
                parent.setView(View.WelcomeFrame);
            else {
                Locale l = parent.getLocale();
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_PRE_WELCOME_SAVE_FAIL_WORKSPACE_DIRECTORY_USE_FAILED),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_PRE_WELCOME_TITLE));
        localeSelect.setTitle(l.getKey(KEY_FRAME_PRE_WELCOME_TEXT_LANGUAGE));
        workspaceSelector.setTitle(l.getKey(KEY_FRAME_PRE_WELCOME_TEXT_WORKSPACE_DIRECTORY));
        workspaceSelector.setButtonText(l.getKey(KEY_FRAME_PRE_WELCOME_TEXT_WORKSPACE_DIRECTORY_SELECT));
        showPreWelcome.setTitle(l.getKey(KEY_FRAME_PRE_WELCOME_TEXT_SHOW_PRE_WELCOME_ON_START));
        editorButton.setText(l.getKey(KEY_FRAME_PRE_WELCOME_TEXT_OPEN_EDITOR));
    }
}
