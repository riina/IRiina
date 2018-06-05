package net.cyriaca.riina.misc.iriina.beatmapper;

import net.cyriaca.riina.misc.iriina.beatmapper.data.IRiinaPreferences;
import net.cyriaca.riina.misc.iriina.beatmapper.data.LoadTarget;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ProjectMeta;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.*;
import net.cyriaca.riina.misc.iriina.generic.MissingResourceException;
import net.cyriaca.riina.misc.iriina.generic.localization.InvalidLocaleException;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.localization.LocaleSystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IRiina {

    private Locale locale = null;
    private IRiinaPreferences prefs;
    private List<LocaleChangeListener> localeChangeListeners;
    private View currentView;
    private PreWelcomeFrame preWelcomeFrame;
    private WelcomeFrame welcomeFrame;
    private NewProjectDialogFrame newProjectDialogFrame;
    private MapImportFrame mapImportFrame;
    private OpenProjectDialogFrame openProjectDialogFrame;
    private ProjectLoadFrame projectLoadFrame;
    private EditorFrame editorFrame;
    private AboutFrame aboutFrame;
    private PreferencesFrame preferencesFrame;
    private LoadTarget loadTarget = null;
    private ProjectMeta meta = null;
    private Image tempImg = null;

    public IRiina() {
        try {
            tempImg = ImageIO.read(Objects.requireNonNull(EditorFrame.class.getClassLoader().getResource("img/iriina.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        localeChangeListeners = new ArrayList<>();
        currentView = View.InitStateNoWindow;
        prefs = DataManager.readPrefs();
        preWelcomeFrame = new PreWelcomeFrame(this);
        addLocaleChangeListener(preWelcomeFrame);
        welcomeFrame = new WelcomeFrame(this);
        addLocaleChangeListener(welcomeFrame);
        aboutFrame = new AboutFrame(this);
        addLocaleChangeListener(aboutFrame);
        preferencesFrame = new PreferencesFrame(this);
        addLocaleChangeListener(preferencesFrame);
        newProjectDialogFrame = new NewProjectDialogFrame(this);
        addLocaleChangeListener(newProjectDialogFrame);
        openProjectDialogFrame = new OpenProjectDialogFrame(this);
        addLocaleChangeListener(openProjectDialogFrame);
        projectLoadFrame = new ProjectLoadFrame(this);
        addLocaleChangeListener(projectLoadFrame);
        mapImportFrame = new MapImportFrame(this);
        addLocaleChangeListener(mapImportFrame);
        editorFrame = new EditorFrame(this);
        addLocaleChangeListener(editorFrame);
        updateLocale(prefs.getLocale());
    }

    public static void main(String[] args) {
        String os = System.getProperty("os.name");
        if (os.contains("Mac OS X"))
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        IRiina editor = new IRiina();
        editor.execute();
    }

    public Image getTempImg() {
        return tempImg;
    }

    private void execute() {
        if (prefs.getShowPreWelcomeWindow())
            setView(View.PreWelcomeFrame);
        else
            setView(View.WelcomeFrame);
    }

    public IRiinaPreferences getPreferences() {
        return prefs;
    }

    private void addLocaleChangeListener(LocaleChangeListener listener) {
        localeChangeListeners.add(listener);
    }

    public void setView(View newView) {
        switch (currentView) {
            case PreWelcomeFrame:
                preWelcomeFrame.cleanupAndHideFrame();
                break;
            case WelcomeFrame:
                welcomeFrame.cleanupAndHideFrame();
                break;
            case EditorFrame:
                editorFrame.cleanupAndHideFrame();
                break;
            case InitStateNoWindow:
                break;
            case MapImportFrame:
                mapImportFrame.cleanupAndHideFrame();
                break;
            case ProjectLoadFrame:
                projectLoadFrame.cleanupAndHideFrame();
                break;
            case NewProjectDialogFrame:
                newProjectDialogFrame.cleanupAndHideFrame();
                break;
            case OpenProjectDialogFrame:
                openProjectDialogFrame.cleanupAndHideFrame();
                break;
            default:
                break;
        }
        currentView = newView;
        switch (newView) {
            case PreWelcomeFrame:
                preWelcomeFrame.setupAndShowFrame();
                break;
            case WelcomeFrame:
                welcomeFrame.setupAndShowFrame();
                break;
            case EditorFrame:
                editorFrame.setupAndShowFrame();
                break;
            case InitStateNoWindow:
                break;
            case MapImportFrame:
                mapImportFrame.setupAndShowFrame();
                break;
            case ProjectLoadFrame:
                projectLoadFrame.setupAndShowFrame();
                break;
            case NewProjectDialogFrame:
                newProjectDialogFrame.setupAndShowFrame();
                break;
            case OpenProjectDialogFrame:
                openProjectDialogFrame.setupAndShowFrame();
                break;
            default:
                break;
        }
    }

    public Locale getLocale() {
        if (locale == null)
            try {
                locale = LocaleSystem.loadLocale(prefs.getLocale());
            } catch (InvalidLocaleException e) {
                e.printStackTrace();
                System.exit(1254);
            } catch (MissingResourceException e) {
                e.printStackTrace();
                System.exit(1255);
            }
        return locale;
    }

    private void updateLocale(String newLocale) {
        try {
            locale = LocaleSystem.loadLocale(newLocale);
        } catch (InvalidLocaleException e) {
            e.printStackTrace();
            System.exit(3042);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            System.exit(3043);
        }
        for (LocaleChangeListener lcl : localeChangeListeners)
            lcl.localeChangePerformed(new LocaleChangeEvent(locale));
    }

    public void updatePreferences(IRiinaPreferences prefs) {
        this.prefs = prefs;
        updateLocale(this.prefs.getLocale());
        DataManager.writePrefs(this.prefs);
    }

    public LoadTarget getLoadTarget() {
        return loadTarget;
    }

    private void setLoadTarget(LoadTarget loadTarget) {
        this.loadTarget = loadTarget;
    }

    public void openEditor(ProjectMeta meta) {
        this.meta = meta;
        setView(View.EditorFrame);
    }

    public ProjectMeta getProjectMeta() {
        return meta;
    }

    public void setProjectMeta(ProjectMeta meta) {
        this.meta = meta;
    }

    public void loadProject(LoadTarget target) {
        setLoadTarget(target);
        setView(View.ProjectLoadFrame);
    }

    public void showAboutFrame() {
        aboutFrame.setupAndShowFrame();
    }

    public void showPreferencesFrame() {
        preferencesFrame.setupAndShowFrame();
    }

    public enum View {
        PreWelcomeFrame, WelcomeFrame, NewProjectDialogFrame, OpenProjectDialogFrame, ProjectLoadFrame, MapImportFrame, EditorFrame, InitStateNoWindow
    }

}
