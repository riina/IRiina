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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IRiina {

    public static final boolean IS_MACOS;
    public static final boolean IS_WINDOWS;
    public static final boolean IS_LINUX;
    public static final String OS_ID;
    private static final Image[] EMBLEMS = new Image[4];
    private static final ImageIcon EMBLEM64_ICON;

    static {
        Module m = IRiina.class.getModule();
        String osId = null;
        try {
            osId = System.getProperty("os.name");
        } catch (SecurityException ignored) {
        }
        OS_ID = osId == null ? "" : osId;
        IS_MACOS = OS_ID.startsWith("Mac OS X");
        IS_LINUX = OS_ID.toLowerCase().startsWith("linux");
        IS_WINDOWS = OS_ID.startsWith("Windows");
        try {
            EMBLEMS[0] = ImageIO.read(m.getResourceAsStream("img/iriina_16.png"));
        } catch (IOException e) {
            EMBLEMS[0] = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        }
        try {
            EMBLEMS[1] = ImageIO.read(m.getResourceAsStream("img/iriina_32.png"));
        } catch (IOException e) {
            EMBLEMS[1] = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        }
        try {
            EMBLEMS[2] = ImageIO.read(m.getResourceAsStream("img/iriina_64.png"));
        } catch (IOException e) {
            EMBLEMS[2] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        }
        try {
            EMBLEMS[3] = ImageIO.read(m.getResourceAsStream("img/iriina_128.png"));
        } catch (IOException e) {
            EMBLEMS[3] = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        }
        EMBLEM64_ICON = new ImageIcon(EMBLEMS[2]);
    }

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
            tempImg = ImageIO.read(Objects.requireNonNull(IRiina.class.getModule().getResourceAsStream("img/iriina.png")));
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
        if (IS_MACOS)
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        IRiina editor = new IRiina();
        editor.execute();
    }

    public static void brandFrameWithGloriousEmblem(Frame frame) {
        if (IS_WINDOWS)
            frame.setIconImages(Arrays.asList(EMBLEMS));
    }

    public static ImageIcon getIcon64() {
        return EMBLEM64_ICON;
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
