package net.cyriaca.riina.misc.iriina.editor.ui;

import net.cyriaca.riina.misc.iriina.editor.IRiina;
import net.cyriaca.riina.misc.iriina.editor.LocaleChangeEvent;
import net.cyriaca.riina.misc.iriina.editor.LocaleChangeListener;
import net.cyriaca.riina.misc.iriina.editor.data.IRiinaPreferences;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class PreferencesFrame extends JFrame implements IViewFrame, LocaleChangeListener, ActionListener {

    private static String KEY_UI_DIALOG_INFORMATION = "ui_dialog_information";
    private static String KEY_UI_DIALOG_ERROR = "ui_dialog_error";

    private static String KEY_FRAME_PREFERENCES_SAVE_FAIL_WORKSPACE_DIRECTORY_USE_FAILED = "frame_preferences_save_fail_workspace_directory_use_failed";

    private static String KEY_FRAME_PREFERENCES_SAVE_SUCCESS = "frame_preferences_save_success";

    private static String KEY_FRAME_PREFERENCES_TITLE = "frame_preferences_title";
    private static String KEY_FRAME_PREFERENCES_TEXT_LANGUAGE = "frame_preferences_text_language";
    private static String KEY_FRAME_PREFERENCES_TEXT_WORKSPACE_DIRECTORY = "frame_preferences_text_workspace_directory";
    private static String KEY_FRAME_PREFERENCES_TEXT_WORKSPACE_DIRECTORY_SELECT = "frame_preferences_text_workspace_directory_select";
    private static String KEY_FRAME_PREFERENCES_TEXT_SHOW_PRE_WELCOME_ON_START = "frame_preferences_text_show_pre_welcome_on_start";
    private static String KEY_FRAME_PREFERENCES_TEXT_SAVE_PREFERENCES = "frame_preferences_text_save_preferences";
    private IRiina parent;
    private ArxTitledComboBoxItem<LocaleDefinition> localeSelect;
    private ArxTitledFolderSelectorItem workspaceSelector;
    private ArxTitledBooleanItem showPreWelcome;
    private JButton savePreferencesButton;

    private JPanel contentPane;
    private JPanel holder;
    private JScrollPane scrollPane;

    private Collection<LocaleDefinition> localeCollection;

    public PreferencesFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        holder = new JPanel();
        BoxLayout bl = new BoxLayout(holder, BoxLayout.Y_AXIS);
        holder.setLayout(bl);
        IRiinaPreferences prefs = parent.getPreferences();
        localeCollection = Collections.unmodifiableCollection(LocaleSystem.getLocales().values());
        LocaleDefinition initSelection = null;
        Iterator<LocaleDefinition> it = localeCollection.iterator();
        while (it.hasNext()) {
            String loc = prefs.getLocale();
            LocaleDefinition ld = it.next();
            if (loc.equals(ld.getLocaleId()))
                initSelection = ld;
        }
        localeSelect = new ArxTitledComboBoxItem<>(localeCollection, initSelection);
        localeSelect.addActionListener(this);
        workspaceSelector = new ArxTitledFolderSelectorItem();

        showPreWelcome = new ArxTitledBooleanItem();

        savePreferencesButton = new JButton();
        savePreferencesButton.addActionListener(this);

        holder.add(localeSelect);
        holder.add(workspaceSelector);
        holder.add(showPreWelcome);
        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(holder, BorderLayout.NORTH);
        scrollPane = new JScrollPane(minHolder);
        add(scrollPane, BorderLayout.CENTER);
        add(savePreferencesButton, BorderLayout.SOUTH);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == localeSelect) {
            IRiinaPreferences prefs = parent.getPreferences().clone();
            prefs.setLocale(localeSelect.getSelectedItem().getLocaleId());
            parent.updatePreferences(prefs);
        } else if (e.getSource() == savePreferencesButton) {
            Locale l = parent.getLocale();
            if (updatePrefsToParent(parent.getPreferences())) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_PREFERENCES_SAVE_SUCCESS),
                        l.getKey(KEY_UI_DIALOG_INFORMATION), JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_PREFERENCES_SAVE_FAIL_WORKSPACE_DIRECTORY_USE_FAILED),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_PREFERENCES_TITLE));
        localeSelect.setTitle(l.getKey(KEY_FRAME_PREFERENCES_TEXT_LANGUAGE));
        workspaceSelector.setTitle(l.getKey(KEY_FRAME_PREFERENCES_TEXT_WORKSPACE_DIRECTORY));
        workspaceSelector.setButtonText(l.getKey(KEY_FRAME_PREFERENCES_TEXT_WORKSPACE_DIRECTORY_SELECT));
        showPreWelcome.setTitle(l.getKey(KEY_FRAME_PREFERENCES_TEXT_SHOW_PRE_WELCOME_ON_START));
        savePreferencesButton.setText(l.getKey(KEY_FRAME_PREFERENCES_TEXT_SAVE_PREFERENCES));
    }
}
