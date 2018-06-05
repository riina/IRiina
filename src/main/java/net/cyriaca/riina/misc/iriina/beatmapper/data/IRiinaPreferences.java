package net.cyriaca.riina.misc.iriina.beatmapper.data;

import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EditorConfig;
import net.cyriaca.riina.misc.iriina.generic.localization.LocaleSystem;

import java.io.File;
import java.util.Arrays;

public class IRiinaPreferences {

    private String locale;
    private String workspaceDirectory;
    private boolean showPreWelcomeWindow;
    private EditorConfig editorConfig;

    public IRiinaPreferences() {
        locale = LocaleSystem.DEFAULT_LOCALE_ID;
        workspaceDirectory = System.getProperty("user.home");
        showPreWelcomeWindow = true;
        editorConfig = new EditorConfig();
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        if (Arrays.asList(LocaleSystem.getValidLocales()).contains(locale))
            this.locale = locale;
        else
            this.locale = LocaleSystem.DEFAULT_LOCALE_ID;
    }

    public String getWorkspaceDirectory() {
        return workspaceDirectory;
    }

    public boolean setWorkspaceDirectory(String workspaceDirectory) {
        File f = new File(workspaceDirectory);
        if (f.exists()) {
            if (f.isDirectory()) {
                this.workspaceDirectory = workspaceDirectory;
                return true;
            } else
                return false;
        } else {
            if (!f.mkdirs())
                return false;
            else
                this.workspaceDirectory = workspaceDirectory;
            return true;
        }
    }

    public boolean getShowPreWelcomeWindow() {
        return showPreWelcomeWindow;
    }

    public void setShowPreWelcomeWindow(boolean showPreWelcomeWindow) {
        this.showPreWelcomeWindow = showPreWelcomeWindow;
    }

    public EditorConfig getEditorConfig() {
        return editorConfig;
    }

    public void setEditorConfig(EditorConfig editorConfig) {
        this.editorConfig = editorConfig;
    }

    public IRiinaPreferences clone() {
        IRiinaPreferences out = new IRiinaPreferences();
        out.locale = locale;
        out.showPreWelcomeWindow = showPreWelcomeWindow;
        out.workspaceDirectory = workspaceDirectory;
        out.editorConfig = editorConfig.clone();
        return out;
    }
}
