package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.*;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.data.LoadTarget;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ProjectMeta;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ReversibleOperation;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.ArcRenderItem;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EditorConfig;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EventRenderItem;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.ManipTarget;
import net.cyriaca.riina.misc.iriina.beatmapper.ui.editor.*;
import net.cyriaca.riina.misc.iriina.generic.FloatBounds;
import net.cyriaca.riina.misc.iriina.generic.IntBounds;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFloatItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFolderSelectorItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledIntItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledStringItem;
import net.cyriaca.riina.misc.iriina.intralism.data.*;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;

public class EditorFrame extends JFrame implements IViewFrame, WindowListener, LocaleChangeListener, ActionListener, LineListener {

    public static final int UPDATE_PERIOD = 16;

    public static final Color INDIGO = new Color(75, 0, 130);
    public static final float EVT_FWD_BACK_MAX_ERR = 0.1f;
    private static final float SKIP_AMT = 5.0f;
    private static final String KEY_PANEL_EVENT_MOD_WRITE_CHANGES = "panel_event_mod_write_changes";
    private static final String KEY_FRAME_EDITOR_TITLE = "frame_editor_title";
    private static final String PROJECT_NAME = "%projectName%";
    private static final String KEY_UI_DIALOG_INFORMATION = "ui_dialog_information";
    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_FRAME_EDITOR_COPYING_ICON = "frame_editor_copying_icon";
    private static final String KEY_FRAME_EDITOR_SAVING_PROJECT = "frame_editor_saving_project";
    private static final String KEY_FRAME_EDITOR_SAVING_BACKUP = "frame_editor_saving_backup";
    private static final String EXCEPTION = "%exception%";
    private static final String KEY_FRAME_EDITOR_SAVE_SUCCESS = "frame_editor_save_success";
    private static final String KEY_FRAME_EDITOR_SAVE_FAIL_CONFIG = "frame_editor_save_fail_config";
    private static final String CONFIG_FILE = "%configFile%";
    private static final String KEY_FRAME_EDITOR_SAVE_FAIL_BACKUP = "frame_editor_save_fail_backup";
    private static final String BACKUP_FILE = "%backupFile%";
    private static final String KEY_MENU_FILE = "menu_file";
    private static final String KEY_MENU_FILE_NEW_PROJECT = "menu_file_new_project";
    private static final String KEY_MENU_FILE_IMPORT_MAP = "menu_file_import_map";
    private static final String KEY_MENU_FILE_OPEN_PROJECT = "menu_file_open_project";
    private static final String KEY_MENU_FILE_SAVE_PROJECT = "menu_file_save_project";
    private static final String KEY_MENU_FILE_RELOAD_PROJECT = "menu_file_reload_project";
    private static final String KEY_MENU_FILE_EXPORT_PROJECT = "menu_file_export_project";
    private static final String KEY_MENU_EDIT = "menu_edit";
    private static final String KEY_MENU_EDIT_UNDO = "menu_edit_undo";
    private static final String KEY_MENU_EDIT_UNDO_OPERATION_EXISTS = "menu_edit_undo_operation_exists";
    private static final String KEY_MENU_EDIT_REDO = "menu_edit_redo";
    private static final String KEY_MENU_EDIT_REDO_OPERATION_EXISTS = "menu_edit_redo_operation_exists";
    private static final String KEY_MENU_EDIT_CUT = "menu_edit_cut";
    private static final String KEY_MENU_EDIT_COPY = "menu_edit_copy";
    private static final String KEY_MENU_EDIT_PASTE = "menu_edit_paste";
    private static final String KEY_MENU_EDIT_DELETE = "menu_edit_delete";
    private static final String KEY_MENU_EDIT_SELECT_ALL = "menu_edit_select_all";
    private static final String KEY_MENU_EDIT_WRITE_CHANGES_TOP = "menu_edit_write_changes_bot";
    private static final String KEY_MENU_EDIT_WRITE_CHANGES_BOT = "menu_edit_write_changes_top";
    private static final String KEY_MENU_EDIT_SEQUENCE_SELECTION = "menu_edit_sequence_selection";
    private static final String KEY_MENU_EDIT_GENERATE_SEQUENCE = "menu_edit_generate_sequence";
    private static final String KEY_MENU_CONTROLS = "menu_controls";
    private static final String KEY_MENU_CONTROLS_TOGGLE_PLAY = "menu_controls_toggle_play";
    private static final String KEY_MENU_CONTROLS_POSITION_BACK = "menu_controls_position_back";
    private static final String KEY_MENU_CONTROLS_POSITION_FWD = "menu_controls_position_fwd";
    private static final String KEY_MENU_CONTROLS_EVT_BACK = "menu_controls_evt_back";
    private static final String KEY_MENU_CONTROLS_EVT_FWD = "menu_controls_evt_fwd";
    private static final String KEY_MENU_HELP = "menu_help";
    private static final String KEY_MENU_HELP_ABOUT = "menu_help_about";
    private static final String KEY_MENU_HELP_PREFERENCES = "menu_help_preferences";
    private static final String KEY_FRAME_EDITOR_EXIT_TITLE = "frame_editor_exit_title";
    private static final String KEY_FRAME_EDITOR_EXIT_CONFIRM = "frame_editor_exit_confirm";
    private static final String KEY_FRAME_EDITOR_EXIT_OPTION_SAVE = "frame_editor_exit_option_save";
    private static final String KEY_FRAME_EDITOR_EXIT_OPTION_LEAVE = "frame_editor_exit_option_leave";
    private static final String KEY_FRAME_EDITOR_EXIT_OPTION_CANCEL = "frame_editor_exit_option_cancel";
    private static final String KEY_FRAME_EDITOR_RELOAD_TITLE = "frame_editor_reload_title";
    private static final String KEY_FRAME_EDITOR_RELOAD_CONFIRM = "frame_editor_reload_confirm";
    private static final String KEY_FRAME_EDITOR_RELOAD_OPTION_LEAVE = "frame_editor_reload_option_leave";
    private static final String KEY_FRAME_EDITOR_RELOAD_OPTION_CANCEL = "frame_editor_reload_option_cancel";
    private static final String KEY_FRAME_EDITOR_EXPORT_TITLE = "frame_editor_export_title";
    private static final String KEY_FRAME_EDITOR_EXPORT_OPTION_CONFIRM = "frame_editor_export_option_confirm";
    private static final String KEY_FRAME_EDITOR_EXPORT_OPTION_CANCEL = "frame_editor_export_option_cancel";
    private static final String KEY_FRAME_EDITOR_EXPORT_EDITOR_DIRECTORY = "frame_editor_export_editor_directory";
    private static final String KEY_FRAME_EDITOR_EXPORT_EDITOR_DIRECTORY_SELECT = "frame_editor_export_editor_directory_select";
    private static final String KEY_FRAME_EDITOR_EXPORT_FOLDER_NAME = "frame_editor_export_folder_name";
    private static final String KEY_FRAME_EDITOR_EXPORT_CHECKPOINT_TIMING_OFFSET = "frame_editor_export_checkpoint_timing_offset";
    private static final String KEY_FRAME_EDITOR_EXPORT_EVENT_TIMING_OFFSET = "frame_editor_export_event_timing_offset";
    private static final String KEY_FRAME_EDITOR_EXPORT_FAIL_NO_EDITOR_DIRECTORY = "frame_editor_export_fail_no_editor_directory";
    private static final String KEY_FRAME_EDITOR_EXPORT_FAIL_NO_FOLDER_NAME = "frame_editor_export_fail_no_folder_name";
    private static final String KEY_FRAME_EDITOR_EXPORT_FAIL_TARGET_IS_FILE = "frame_editor_export_fail_target_is_file";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_TITLE = "frame_editor_sequence_selection_title";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_TICK = "frame_editor_sequence_selection_base_tick";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_MEASURE = "frame_editor_sequence_selection_base_measure";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_BEAT = "frame_editor_sequence_selection_base_beat";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_SUBTICK = "frame_editor_sequence_selection_base_subtick";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_TICK_SEPARATION = "frame_editor_sequence_selection_tick_separation";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_SUBTICK_SEPARATION = "frame_editor_sequence_selection_subtick_separation";
    private static final String KEY_FRAME_EDITOR_GENERATE_SEQUENCE_TITLE = "frame_editor_generate_sequence_title";
    private static final String KEY_FRAME_EDITOR_GENERATE_SEQUENCE_EVENT_COUNT = "frame_editor_generate_sequence_event_count";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_OPTION_CONFIRM = "frame_editor_sequence_selection_option_confirm";
    private static final String KEY_FRAME_EDITOR_SEQUENCE_SELECTION_OPTION_CANCEL = "frame_editor_sequence_selection_option_cancel";
    private static final String KEY_FRAME_EDITOR_GENERATE_SEQUENCE_OPTION_CONFIRM = "frame_editor_generate_sequence_option_confirm";
    private static final String KEY_FRAME_EDITOR_GENERATE_SEQUENCE_OPTION_CANCEL = "frame_editor_generate_sequence_option_cancel";
    private static final String TARGET_DIRECTORY = "%targetDirectory%";
    private static final String KEY_FRAME_EDITOR_EXPORT_FAIL_TARGET_DIRECTORY_CREATION_FAIL = "frame_editor_export_fail_target_directory_creation_fail";
    private static final String KEY_FRAME_EDITOR_EXPORT_PROGRESSING = "frame_editor_export_progressing";
    private static final String EXPORT_PROGRESS = "%exportProgress%";
    private static final String KEY_FRAME_EDITOR_EXPORT_SUCCESS = "frame_editor_export_success";
    private static final String KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE = "frame_editor_error_invalid_resource";
    private static final String EVENT_NAME = "%eventName%";
    private static final String EVENT_TIME = "%eventTime%";
    private static final String EVENT_DATA = "%eventData%";
    private static final String RESOURCE_NAME = "%resourceName%";
    private static final String RESOURCE_NAME_LIST = "%resourceNameList%";
    private static final String OPERATION = "%operation%";
    private IRiina parent;
    private ControlReferenceFrame controlReferenceFrame;
    private EventDisplayPanel eventDisplayPanel;
    private PlaybackControlPanel playbackControlPanel;
    private ResourceInfoDataPanel resourceInfoDataPanel;
    private MapInfoDataPanel mapInfoDataPanel;
    private MapAndResourceContainerPanel mapAndResourceContainerPanel;
    private EventModContainerPanel eventModContainerPanel;
    private EventDisplayControlPanel eventDisplayControlPanel;
    private JPanel contentPane;
    private BorderLayout contentLayout;
    private File projectDirectory;
    private boolean isBackup;
    private File backupFile;
    private Clip clip;
    private MapData mapData;
    private boolean play;
    private List<MapEvent> prevSelection;
    private List<MapEvent> selection;
    private List<MapEvent> realSelection;
    private ManipTarget manipTarget;
    private Timer timer;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newProjectItem;
    private JMenuItem openProjectItem;
    private JMenuItem importMapItem;
    private JMenuItem saveProjectItem;
    private JMenuItem reloadProjectItem;
    private JMenuItem exportMapItem;
    private JMenu editMenu;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem deleteItem;
    private JMenuItem selectAllItem;
    private JMenuItem applyChangesItem;
    private JMenuItem applyChangesLeftItem;
    private JMenuItem applyChangesRightItem;
    private JMenuItem sequenceSelectionItem;
    private JMenuItem generateSequenceItem;
    private JMenu controlsMenu;
    private JMenuItem togglePlay;
    private JMenuItem positionBack;
    private JMenuItem positionFwd;
    private JMenuItem evtBack;
    private JMenuItem evtFwd;
    private JMenu helpMenu;
    private JMenuItem aboutItem;
    private JMenuItem preferencesItem;
    private String leaveTitle;
    private String leaveText;
    private String[] leaveOptions;
    private String reloadTitle;
    private String reloadText;
    private String[] reloadOptions;
    private String exportTitle;
    private String[] exportOptions;
    private String sequenceSelectionTitle;
    private String sequenceSelectionBaseTick;
    private String sequenceSelectionBaseMeasure;
    private String sequenceSelectionBaseBeat;
    private String sequenceSelectionBaseSubtick;
    private String sequenceSelectionTickSeparation;
    private String sequenceSelectionSubtickSeparation;
    private String generateSequenceTitle;
    private String generateSequenceEventCount;
    private String[] sequenceSelectionOptions;
    private String[] generateSequenceOptions;
    private String exportEditorDirectory;
    private String exportEditorDirectorySelect;
    private String exportFolderName;
    private String exportCheckpointTimingOffset;
    private String exportEventTimingOffset;
    private List<ReversibleOperation> operations;
    private int opPoint;
    private List<MapEvent> clipboard;
    private float clipboardGrabPos;
    private int evtTarg;
    private boolean renderArcs;
    private boolean renderEvents;
    private boolean renderBeats;
    private boolean storedTimeThisFrame;
    private float storedTime;
    private float length;
    private boolean remapMode;

    public EditorFrame(IRiina parent) {
        super();

        this.parent = parent;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentLayout = new BorderLayout();
        contentPane = new JPanel(contentLayout);
        setContentPane(contentPane);

        controlReferenceFrame = new ControlReferenceFrame();

        eventDisplayControlPanel = new EventDisplayControlPanel(this);
        contentPane.add(eventDisplayControlPanel, BorderLayout.NORTH);
        eventDisplayPanel = new EventDisplayPanel(this);
        contentPane.add(eventDisplayPanel, BorderLayout.CENTER);
        playbackControlPanel = new PlaybackControlPanel(this);
        contentPane.add(playbackControlPanel, BorderLayout.SOUTH);
        eventModContainerPanel = new EventModContainerPanel(this);
        contentPane.add(eventModContainerPanel, BorderLayout.EAST);
        mapInfoDataPanel = new MapInfoDataPanel(this);
        resourceInfoDataPanel = new ResourceInfoDataPanel(this);
        mapAndResourceContainerPanel = new MapAndResourceContainerPanel(mapInfoDataPanel, resourceInfoDataPanel);
        contentPane.add(mapAndResourceContainerPanel, BorderLayout.WEST);

        timer = new Timer(UPDATE_PERIOD, this);
        leaveTitle = "Exiting editor";
        leaveText = "Do you want to save your map before exiting?";
        leaveOptions = new String[3];
        leaveOptions[0] = "Save map";
        leaveOptions[1] = "Don't save map";
        leaveOptions[2] = "Cancel";

        reloadTitle = "Reloading project";
        reloadText = "Are you sure you want to reload your project? This will revert any unsaved changes!";
        reloadOptions = new String[2];
        reloadOptions[0] = "Reload";
        reloadOptions[1] = "Cancel";

        exportTitle = "Exporting project";
        exportOptions = new String[2];
        exportOptions[0] = "Export";
        exportOptions[1] = "Cancel";

        sequenceSelectionTitle = "Sequence selection";
        sequenceSelectionBaseTick = "Base tick";
        sequenceSelectionBaseMeasure = "Base measure";
        sequenceSelectionBaseBeat = "Base beat";
        sequenceSelectionBaseSubtick = "Base subtick";
        sequenceSelectionTickSeparation = "Tick separation";
        sequenceSelectionSubtickSeparation = "Subtick separation";
        sequenceSelectionOptions = new String[2];
        sequenceSelectionOptions[0] = "Sequence";
        sequenceSelectionOptions[1] = "Cancel";

        generateSequenceTitle = "Generate sequence";
        generateSequenceEventCount = "Event count";
        generateSequenceOptions = new String[2];
        generateSequenceOptions[0] = "Generate";
        generateSequenceOptions[1] = "Cancel";

        operations = new ArrayList<>();
        opPoint = operations.size() - 1;

        clipboard = new ArrayList<>();
        clipboardGrabPos = 0.0f;

        storedTimeThisFrame = false;

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        menuBar.add(fileMenu);
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        newProjectItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        openProjectItem = menuItem;
        fileMenu.addSeparator();
        menuItem = new JMenuItem();
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        importMapItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        exportMapItem = menuItem;
        fileMenu.addSeparator();
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        saveProjectItem = menuItem;
        menuItem = new JMenuItem();
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        reloadProjectItem = menuItem;

        editMenu = new JMenu();
        menuBar.add(editMenu);
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        undoItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | KeyEvent.SHIFT_DOWN_MASK));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        redoItem = menuItem;
        editMenu.addSeparator();
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        cutItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        copyItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        pasteItem = menuItem;
        editMenu.addSeparator();
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        selectAllItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        deleteItem = menuItem;
        editMenu.addSeparator();
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        applyChangesItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        applyChangesLeftItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        applyChangesRightItem = menuItem;
        editMenu.addSeparator();
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        sequenceSelectionItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        editMenu.add(menuItem);
        menuItem.addActionListener(this);
        generateSequenceItem = menuItem;

        controlsMenu = new JMenu();
        menuBar.add(controlsMenu);
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        controlsMenu.add(menuItem);
        menuItem.addActionListener(this);
        togglePlay = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        controlsMenu.add(menuItem);
        menuItem.addActionListener(this);
        positionBack = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        controlsMenu.add(menuItem);
        menuItem.addActionListener(this);
        positionFwd = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        controlsMenu.add(menuItem);
        menuItem.addActionListener(this);
        evtBack = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        controlsMenu.add(menuItem);
        menuItem.addActionListener(this);
        evtFwd = menuItem;

        helpMenu = new JMenu();
        menuBar.add(helpMenu);
        menuItem = new JMenuItem();
        helpMenu.add(menuItem);
        menuItem.addActionListener(this);
        aboutItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        helpMenu.add(menuItem);
        menuItem.addActionListener(this);
        preferencesItem = menuItem;
        setJMenuBar(menuBar);

        addWindowListener(this);

        setIgnoreRepaint(true);
        exportEditorDirectory = null;
        exportEditorDirectorySelect = null;
        exportFolderName = null;
        exportCheckpointTimingOffset = null;
        exportEventTimingOffset = null;
        evtTarg = -1;
        renderArcs = false;
        renderEvents = true;
        renderBeats = false;
        projectDirectory = null;
        isBackup = false;
        backupFile = null;
        clip = null;
        mapData = null;
        play = false;
        prevSelection = null;
        selection = null;
        realSelection = null;
        manipTarget = null;
        remapMode = false;
        IRiina.brandFrameWithGloriousEmblem(this);
    }

    public boolean isRemapMode() {
        return remapMode;
    }

    public void setRemapMode(boolean value) {
        remapMode = value;
    }

    public List<MapEvent> getEventTargets() {
        return eventModContainerPanel.getTargets();
    }

    public File getResPath() {
        return Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES).toFile();
    }

    public boolean getRenderBeats() {
        return renderBeats;
    }

    public void setRenderBeats(boolean value) {
        renderBeats = value;
    }

    public boolean getRenderArcs() {
        return renderArcs;
    }

    public void setRenderArcs(boolean value) {
        renderArcs = value;
    }

    public boolean getRenderEvents() {
        return renderEvents;
    }

    public void setRenderEvents(boolean value) {
        renderEvents = value;
    }

    public IRiina getIRiina() {
        return parent;
    }

    public void evtBack() {
        int evtCount = mapData.getEventCount();
        if (evtCount == 0)
            setPlayHeadPos(0.0f);
        evtTarg = Math.max(0, Math.min(evtCount - 1, evtTarg));
        MapEvent evt = mapData.getEventList().get(evtTarg);
        if (Math.abs(evt.getTime() - getPlayHeadPos()) <= EVT_FWD_BACK_MAX_ERR) {
            evtTarg = Math.max(0, Math.min(evtCount - 1, evtTarg - 1));
            setPlayHeadPos(mapData.getEventList().get(evtTarg).getTime());
        } else {
            evtTarg = mapData.getClosestEventOffsetLeft(getPlayHeadPos());
            if (evtTarg == -1)
                setPlayHeadPos(0.0f);
            else
                setPlayHeadPos(mapData.getEventList().get(evtTarg).getTime());
        }
    }

    public void evtFwd() {
        int evtCount = mapData.getEventCount();
        if (evtCount == 0)
            setPlayHeadPos(0.0f);
        evtTarg = Math.max(0, Math.min(evtCount - 1, evtTarg));
        MapEvent evt = mapData.getEventList().get(evtTarg);
        if (Math.abs(evt.getTime() - getPlayHeadPos()) <= EVT_FWD_BACK_MAX_ERR) {
            evtTarg = Math.max(0, Math.min(evtCount - 1, evtTarg + 1));
            setPlayHeadPos(mapData.getEventList().get(evtTarg).getTime());
        } else {
            evtTarg = mapData.getClosestEventOffsetRight(getPlayHeadPos());
            if (evtTarg == -1)
                setPlayHeadPos(0.0f);
            else
                setPlayHeadPos(mapData.getEventList().get(evtTarg).getTime());
        }
    }

    public int getEvtTarg() {
        return evtTarg;
    }

    public void setupAndShowFrame() {
        setSize(1400, 950);
        setLocationRelativeTo(null);
        ProjectMeta meta = parent.getProjectMeta();
        projectDirectory = meta.getProjectDirectory();
        isBackup = meta.isBackup();
        backupFile = meta.getBackupFile();
        mapData = meta.getLevelData();
        setTitle(parent.getLocale().getKey(KEY_FRAME_EDITOR_TITLE).replaceAll(PROJECT_NAME, mapData == null ? "Undefined" : mapData.getName()));
        clip = meta.getClip();
        clip.addLineListener(this);
        clip.stop();
        length = (float) clip.getMicrosecondLength() / 1000000.0f;

        prevSelection = new ArrayList<>();
        selection = new ArrayList<>();
        realSelection = new ArrayList<>();
        manipTarget = new ManipTarget();
        operations.clear();
        opPoint = -1;
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        mapInfoDataPanel.setName(mapData.getName());
        mapInfoDataPanel.setDescription(mapData.getInfo());
        mapInfoDataPanel.setIconFile(Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES, mapData.getIconFile()).toFile(), mapData.getIconImage());
        mapInfoDataPanel.setTags(mapData.getTags());
        mapInfoDataPanel.setLives(mapData.getLives());
        mapInfoDataPanel.setSpeedModifier(mapData.getSpeed());
        mapInfoDataPanel.setEnvType(mapData.getEnvironmentType());

        resourceInfoDataPanel.setMapResources(mapData.getMapResources());
        System.out.println("Opened map with " + mapData.getEventList().size() + " events.");

        setVisible(true);
        timer.start();
        queueRender();
    }

    public void updateResourceList(List<MapResource> resources) {
        eventModContainerPanel.updateResourceList(resources);
    }

    private void copy() {
        clearDrag();
        clipboard.clear();
        for (MapEvent evt : realSelection)
            clipboard.add(evt.clone());
        clipboardGrabPos = getPlayHeadPos();
    }

    private void cut() {
        clearDrag();
        clipboard.clear();
        for (MapEvent evt : realSelection)
            clipboard.add(evt.clone());
        deleteSelection();
        clipboardGrabPos = getPlayHeadPos();
    }

    private void paste() {
        float newPos = getPlayHeadPos();
        List<MapEvent> theCreated = new ArrayList<>();
        for (MapEvent evt : clipboard) {
            MapEvent evtC = evt.clone();
            evtC.setTime(evtC.getTime() + (newPos - clipboardGrabPos));
            mapData.addNewEvent(evtC);
            theCreated.add(evtC);
        }
        addOperation(ReversibleOperation.createEventGroupCreateOperation(theCreated));
    }


    public void cleanupAndHideFrame() {
        timer.stop();
        setVisible(false);
        clip.stop();
        clip.removeLineListener(this);
        DataManager.closeClip(clip);
        prevSelection = new ArrayList<>();
        selection = new ArrayList<>();
        realSelection = new ArrayList<>();
        manipTarget = new ManipTarget();
        operations.clear();
        opPoint = operations.size() - 1;
        mapData = null;
        projectDirectory = null;
        backupFile = null;
        resourceInfoDataPanel.setMapResources(null);
        System.gc();

        controlReferenceFrame.hideFrame();
    }

    private void generateSequence() {
        MapEvent te = mapData.getFirstTimingEventForTime(getPlayHeadPos());
        if (te == null)
            return;
        TimingProperty tp = te.getTimingProperty();
        int baseTick = tp.getClosestTickFromTime(getPlayHeadPos());
        int tickSeparation = tp.getTimingMode() == TimingProperty.TimingMode.MEASURE ? tp.getBeatTicks() : 4;
        ArxTitledIntItem tickSeparationItem = new ArxTitledIntItem(new IntBounds(0, false, tickSeparation, true), tickSeparation);
        tickSeparationItem.setSliderVisibility(false);
        int eventCount = tp.getTimingMode() == TimingProperty.TimingMode.MEASURE ? tp.getMeasureBeats() : 4;
        ArxTitledIntItem eventCountItem = new ArxTitledIntItem(new IntBounds(0, false, eventCount, true), eventCount);
        eventCountItem.setTitle(generateSequenceEventCount);
        eventCountItem.setSliderVisibility(false);
        if (tp.getTimingMode() == TimingProperty.TimingMode.TICK) {
            tickSeparationItem.setTitle(sequenceSelectionTickSeparation);
            ArxTitledIntItem baseTickItem = new ArxTitledIntItem(new IntBounds(0, false, baseTick, true), baseTick);
            baseTickItem.setTitle(sequenceSelectionBaseTick);
            baseTickItem.setSliderVisibility(false);
            JComponent[] options = new JComponent[]{
                    baseTickItem,
                    tickSeparationItem,
                    eventCountItem
            };
            int x = JOptionPane.showOptionDialog(this, options, generateSequenceTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), generateSequenceOptions, generateSequenceOptions[0]);
            if (x == 1 || x == JOptionPane.CLOSED_OPTION)
                return;
            baseTick = baseTickItem.getValue();
        } else {
            tickSeparationItem.setTitle(sequenceSelectionSubtickSeparation);
            int baseMeasure = tp.getMeasureFromTick(baseTick);
            ArxTitledIntItem baseMeasureItem = new ArxTitledIntItem(new IntBounds(0, false, baseMeasure, true), baseMeasure);
            baseMeasureItem.setTitle(sequenceSelectionBaseMeasure);
            baseMeasureItem.setSliderVisibility(false);
            ArxTitledIntItem baseBeatItem = new ArxTitledIntItem(new IntBounds(0, false, tp.getMeasureBeats(), false), tp.getBeatFromTick(baseTick));
            baseBeatItem.setTitle(sequenceSelectionBaseBeat);
            baseBeatItem.setSliderVisibility(false);
            ArxTitledIntItem baseSubtickItem = new ArxTitledIntItem(new IntBounds(0, false, tp.getBeatTicks(), false), tp.getSubtickFromTick(baseTick));
            baseSubtickItem.setTitle(sequenceSelectionBaseSubtick);
            baseSubtickItem.setSliderVisibility(false);
            JComponent[] options = new JComponent[]{
                    baseMeasureItem,
                    baseBeatItem,
                    baseSubtickItem,
                    tickSeparationItem,
                    eventCountItem
            };
            int x = JOptionPane.showOptionDialog(this, options, generateSequenceTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), generateSequenceOptions, generateSequenceOptions[0]);
            if (x == 1 || x == JOptionPane.CLOSED_OPTION)
                return;
            baseTick = tp.getTickFromMeasureAndBeatAndSubtick(baseMeasureItem.getValue(), baseBeatItem.getValue(), baseSubtickItem.getValue());
        }
        eventCount = eventCountItem.getValue();
        tickSeparation = tickSeparationItem.getValue();
        List<MapEvent> theCreated = new ArrayList<>();
        for (int i = 0; i < eventCount; i++) {
            MapEvent evt = new SpawnObjEvent(0.0f);
            mapData.addNewEvent(evt);
            TimedEventProperty tep = evt.getTimedEventProperty();
            tep.setTimingEventId(te.getMetaId());
            tep.setTickRefreshLight(baseTick + tickSeparation * i);
            theCreated.add(evt);
        }
        mapData.repositionEventsFinalize(theCreated);
        addOperation(ReversibleOperation.createEventGroupCreateOperation(theCreated));
    }

    private void sequenceSelection() {
        if (realSelection.size() != 0) {
            MapEvent e0 = realSelection.get(0);
            MapEvent te = mapData.getFirstTimingEventForTime(e0.getTime());
            if (te == null)
                return;
            TimingProperty tp = te.getTimingProperty();
            int baseTick = tp.getClosestTickFromTime(e0.getTime());
            int tickSeparation = tp.getTimingMode() == TimingProperty.TimingMode.MEASURE ? tp.getBeatTicks() : 4;
            ArxTitledIntItem tickSeparationItem = new ArxTitledIntItem(new IntBounds(0, false, tickSeparation, true), tickSeparation);
            tickSeparationItem.setSliderVisibility(false);
            if (tp.getTimingMode() == TimingProperty.TimingMode.TICK) {
                tickSeparationItem.setTitle(sequenceSelectionTickSeparation);
                ArxTitledIntItem baseTickItem = new ArxTitledIntItem(new IntBounds(0, false, baseTick, true), baseTick);
                baseTickItem.setTitle(sequenceSelectionBaseTick);
                baseTickItem.setSliderVisibility(false);
                JComponent[] options = new JComponent[]{
                        baseTickItem,
                        tickSeparationItem
                };
                int x = JOptionPane.showOptionDialog(this, options, sequenceSelectionTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), sequenceSelectionOptions, sequenceSelectionOptions[0]);
                if (x == 1 || x == JOptionPane.CLOSED_OPTION)
                    return;
                baseTick = baseTickItem.getValue();
            } else {
                tickSeparationItem.setTitle(sequenceSelectionSubtickSeparation);
                int baseMeasure = tp.getMeasureFromTick(baseTick);
                ArxTitledIntItem baseMeasureItem = new ArxTitledIntItem(new IntBounds(0, false, baseMeasure, true), baseMeasure);
                baseMeasureItem.setTitle(sequenceSelectionBaseMeasure);
                baseMeasureItem.setSliderVisibility(false);
                ArxTitledIntItem baseBeatItem = new ArxTitledIntItem(new IntBounds(0, false, tp.getMeasureBeats(), false), tp.getBeatFromTick(baseTick));
                baseBeatItem.setTitle(sequenceSelectionBaseBeat);
                baseBeatItem.setSliderVisibility(false);
                ArxTitledIntItem baseSubtickItem = new ArxTitledIntItem(new IntBounds(0, false, tp.getBeatTicks(), false), tp.getSubtickFromTick(baseTick));
                baseSubtickItem.setTitle(sequenceSelectionBaseSubtick);
                baseSubtickItem.setSliderVisibility(false);
                JComponent[] options = new JComponent[]{
                        baseMeasureItem,
                        baseBeatItem,
                        baseSubtickItem,
                        tickSeparationItem
                };
                int x = JOptionPane.showOptionDialog(this, options, sequenceSelectionTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), sequenceSelectionOptions, sequenceSelectionOptions[0]);
                if (x == 1 || x == JOptionPane.CLOSED_OPTION)
                    return;
                baseTick = tp.getTickFromMeasureAndBeatAndSubtick(baseMeasureItem.getValue(), baseBeatItem.getValue(), baseSubtickItem.getValue());
            }
            tickSeparation = tickSeparationItem.getValue();
            int offset = 0;
            int len = realSelection.size();
            List<String> oldDataList = new ArrayList<>(len), oldExtraDataList = new ArrayList<>(len), dataList = new ArrayList<>(len), extraDataList = new ArrayList<>(len);
            List<Float> oldTimeList = new ArrayList<>(len), timeList = new ArrayList<>(len);
            for (MapEvent evt : realSelection) {
                oldDataList.add(evt.getEventData());
                String extraData = evt.getEventExtraData();
                if (extraData == null)
                    extraData = "";
                oldExtraDataList.add(extraData);
                oldTimeList.add(evt.getTime());
                TimedEventProperty tep = evt.getTimedEventProperty();
                if (tep != null) {
                    tep.setTimingEventId(te.getMetaId());
                    tep.setTick(baseTick + tickSeparation * offset);
                }
                dataList.add(evt.getEventData());
                extraData = evt.getEventExtraData();
                if (extraData == null)
                    extraData = "";
                extraDataList.add(extraData);
                timeList.add(evt.getTime());
                offset++;
            }
            addOperationForEventGroupDataMod(realSelection, oldDataList, oldExtraDataList, oldTimeList, dataList, extraDataList, timeList);
        }
    }

    public void addOperationForResourceAdd(MapResource res) {
        addOperation(ReversibleOperation.createResourceAddOperation(res));
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void addOperationForResourceMod(MapResource oldRes, MapResource newRes) {
        addOperation(ReversibleOperation.createResourceModOperation(oldRes, newRes));
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void addOperationForResourceRemove(MapResource res) {
        addOperation(ReversibleOperation.createResourceDeleteOperation(res));
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void subAddOperationForEventDataMod(MapEvent event, String oldData, String oldExtraData, float oldTime, String data, String extraData, float time) {
        addOperation(ReversibleOperation.createEventDataModOperation(event, oldData, oldExtraData, oldTime, data, extraData, time));
    }

    public void addOperationForEventDataMod(MapEvent event, String oldData, String oldExtraData, float oldTime, String data, String extraData, float time) {
        subAddOperationForEventDataMod(event, oldData, oldExtraData, oldTime, data, extraData, time);
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void addOperationForEventGroupDataMod(List<MapEvent> eventList, List<String> oldDataList, List<String> oldExtraDataList, List<Float> oldTimeList, List<String> dataList, List<String> extraDataList, List<Float> timeList) {
        addOperation(ReversibleOperation.createEventGroupDataModOperation(eventList, oldDataList, oldExtraDataList, oldTimeList, dataList, extraDataList, timeList));
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void addOperationForEventAdd(MapEvent event) {
        addOperation(ReversibleOperation.createEventCreateOperation(event));
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void addOperationForCheckpointAdd(Checkpoint checkpoint) {
        addOperation(ReversibleOperation.createCheckpointCreateOperation(checkpoint));
    }

    public void deleteClosest(float time, MapEvent.Type type) {
        MapEvent event = mapData.getClosestEvent(time, type);
        if (event != null && Math.abs(event.getTime() - time) < getConfig().getMaxManipGrabDistanceSec()) {
            addOperation(ReversibleOperation.createEventDeleteOperation(event));
            mapData.removeEvent(event);
        }
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public void deleteClosestCheckpoint(float time) {
        Checkpoint checkpoint = mapData.getClosestCheckpoint(time);
        if (checkpoint != null && Math.abs(checkpoint.getTime() - time) < getConfig().getMaxManipGrabDistanceSec()) {
            addOperation(ReversibleOperation.createCheckpointDeleteOperation(checkpoint));
            mapData.removeCheckpoint(checkpoint);
        }
    }

    private void deleteSelection() {
        selection.clear();
        prevSelection.clear();
        for (MapEvent evt : realSelection)
            mapData.removeEvent(evt);
        addOperation(ReversibleOperation.createEventGroupDeleteOperation(new ArrayList<>(realSelection)));
        eventDisplayPanel.deselect();
        realSelection.clear();
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    private void addOperation(ReversibleOperation operation) {
        while (operations.size() > opPoint + 1)
            operations.remove(operations.size() - 1);
        operations.add(operation);
        opPoint = operations.size() - 1;
        Locale l = parent.getLocale();
        undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint).getOperationKey())));
        redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO));
        undoItem.setEnabled(true);
        redoItem.setEnabled(false);
    }

    private void reloadProject() {
        parent.loadProject(new LoadTarget(projectDirectory, backupFile, null, null, isBackup));
    }

    public void toggleMapInfoAndResourceInfo() {
        mapAndResourceContainerPanel.setVisible(!mapAndResourceContainerPanel.isVisible());
    }

    public void toggleEventMod() {
        eventModContainerPanel.setVisible(!eventModContainerPanel.isVisible());
    }

    private void saveProject() {
        Locale l = parent.getLocale();
        setPlaying(false);
        mapData.setName(mapInfoDataPanel.getName());
        setTitle(l.getKey(KEY_FRAME_EDITOR_TITLE).replaceAll(PROJECT_NAME, mapData == null ? "Undefined" : mapData.getName()));
        mapData.setInfo(mapInfoDataPanel.getDescription());
        File iconFile = mapInfoDataPanel.getIconFile();
        File resDirectory = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES).toFile();
        if (!resDirectory.equals(iconFile.getParentFile())) {
            eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_COPYING_ICON));
            eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
            CopyFileResult result = DataManager.copyFile(iconFile, resDirectory);
            if (result.failed()) {
                result.getException().printStackTrace();
                JOptionPane.showMessageDialog(this,
                        result.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
        mapData.setIconFile(iconFile.getName());
        mapData.setTags(mapInfoDataPanel.getTags());
        mapData.setLives(mapInfoDataPanel.getLives());
        mapData.setSpeed(mapInfoDataPanel.getSpeedModifier());
        mapData.setEnvironmentType(mapInfoDataPanel.getEnvType());

        mapData.setMapResources(resourceInfoDataPanel.getMapResources());

        List<MapResource> mapResources = mapData.getMapResources();
        List<String> resourceNames = new ArrayList<>();
        for (MapResource res : mapResources)
            resourceNames.add(res.getName());
        List<MapEvent> events = mapData.getEventList();
        for (MapEvent event : events) {
            ResourceProperty mainProp = event.getMainResourceProperty();
            if (mainProp != null)
                if (!resourceNames.contains(mainProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, mainProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
            ResourceProperty foreProp = event.getForegroundResourceProperty();
            if (foreProp != null)
                if (!resourceNames.contains(foreProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, foreProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
            ResourceProperty backProp = event.getBackgroundResourceProperty();
            if (backProp != null)
                if (!resourceNames.contains(backProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, backProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
        }

        File configTarget = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.CONFIG_FILE).toFile();
        eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_SAVING_PROJECT));
        eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
        try {
            DataManager.exportMap(mapData, configTarget, false);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    l.getKey(KEY_FRAME_EDITOR_SAVE_FAIL_CONFIG).replaceAll(CONFIG_FILE, Matcher.quoteReplacement(configTarget.getAbsolutePath())).replaceAll(EXCEPTION, e.toString()),
                    l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
        }
        eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_SAVING_BACKUP));
        eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
        String odt = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd kk mm ss SSSS"));
        File backupTarget = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_BACKUP, (odt) + ".ibm").toFile();
        try {
            DataManager.exportMap(mapData, backupTarget, false);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    l.getKey(KEY_FRAME_EDITOR_SAVE_FAIL_BACKUP).replaceAll(BACKUP_FILE, Matcher.quoteReplacement(backupTarget.getAbsolutePath())).replaceAll(EXCEPTION, e.toString()),
                    l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
        }
        isBackup = false;
        eventDisplayPanel.setMessage(null);
        JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_EDITOR_SAVE_SUCCESS).replaceAll(CONFIG_FILE, Matcher.quoteReplacement(configTarget.getAbsolutePath())).replaceAll(BACKUP_FILE, Matcher.quoteReplacement(backupTarget.getAbsolutePath())),
                l.getKey(KEY_UI_DIALOG_INFORMATION), JOptionPane.PLAIN_MESSAGE, IRiina.getIcon64());
    }

    private void showError(String title, String err) {
        JOptionPane.showMessageDialog(this,
                err,
                title, JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String title, String mess) {
        JOptionPane.showMessageDialog(this, mess, title, JOptionPane.INFORMATION_MESSAGE, IRiina.getIcon64());
    }

    private void exportProject() {
        ArxTitledFolderSelectorItem folderSelector = new ArxTitledFolderSelectorItem();
        if (IRiina.IS_MACOS)
            folderSelector.setDirectory(Paths.get(System.getProperty("user.home"), IRiinaConstants.INTRALISM_MAC_EDITOR_DIRECTORY).toFile().getAbsolutePath());
        else
            folderSelector.setDirectory(Paths.get(IRiinaConstants.INTRALISM_WIN_EDITOR_DIRECTORY).toFile().getAbsolutePath());
        folderSelector.setTitle(exportEditorDirectory);
        folderSelector.setButtonText(exportEditorDirectorySelect);
        ArxTitledStringItem folderNameSelector = new ArxTitledStringItem();
        folderNameSelector.setTitle(exportFolderName);
        folderNameSelector.setFieldCols(11);
        String mapName = mapInfoDataPanel.getName();
        if (mapName == null || mapName.length() == 0)
            mapName = "SetMapNameHere";
        folderNameSelector.setValue(mapName);
        ArxTitledFloatItem checkpointTimingOffsetSelector = new ArxTitledFloatItem(new FloatBounds(-1.0f, true, 1.0f, true), mapData.getCheckpointTimingOffset());
        checkpointTimingOffsetSelector.setTitle(exportCheckpointTimingOffset);
        ArxTitledFloatItem eventTimingOffsetSelector = new ArxTitledFloatItem(new FloatBounds(-1.0f, true, 1.0f, true), mapData.getCheckpointTimingOffset());
        eventTimingOffsetSelector.setTitle(exportEventTimingOffset);
        JComponent[] options = new JComponent[]{
                folderSelector,
                folderNameSelector,
                checkpointTimingOffsetSelector,
                eventTimingOffsetSelector
        };

        Locale l = parent.getLocale();
        int x = JOptionPane.showOptionDialog(this, options, exportTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), exportOptions, exportOptions[0]);
        if (x == 1 || x == JOptionPane.CLOSED_OPTION)
            return;
        String editorDirectory = folderSelector.getDirectory();
        if (editorDirectory == null) {
            showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_EXPORT_FAIL_NO_EDITOR_DIRECTORY));
        }
        String folderName = folderNameSelector.getValue();
        if (folderName == null) {
            showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_EXPORT_FAIL_NO_FOLDER_NAME));
        }
        assert editorDirectory != null;
        File targetDirectory = Paths.get(editorDirectory, folderName).toFile();
        if (targetDirectory.exists()) {
            if (targetDirectory.isFile()) {
                showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_EXPORT_FAIL_TARGET_IS_FILE).replaceAll(TARGET_DIRECTORY, Matcher.quoteReplacement(targetDirectory.getAbsolutePath())));
            }
        } else {
            if (!targetDirectory.mkdirs())
                showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_EXPORT_FAIL_TARGET_DIRECTORY_CREATION_FAIL.replaceAll(TARGET_DIRECTORY, Matcher.quoteReplacement(targetDirectory.getAbsolutePath()))));
        }
        setPlaying(false);
        mapData.setName(mapInfoDataPanel.getName());
        setTitle(l.getKey(KEY_FRAME_EDITOR_TITLE).replaceAll(PROJECT_NAME, mapData == null ? "Undefined" : mapData.getName()));
        mapData.setInfo(mapInfoDataPanel.getDescription());
        File iconFile = mapInfoDataPanel.getIconFile();
        File resDirectory = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES).toFile();
        if (!resDirectory.equals(iconFile.getParentFile())) {
            eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_COPYING_ICON));
            eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
            CopyFileResult result = DataManager.copyFile(iconFile, resDirectory);
            if (result.failed()) {
                result.getException().printStackTrace();
                JOptionPane.showMessageDialog(this,
                        result.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
        mapData.setIconFile(iconFile.getName());
        mapData.setTags(mapInfoDataPanel.getTags());
        mapData.setLives(mapInfoDataPanel.getLives());
        mapData.setSpeed(mapInfoDataPanel.getSpeedModifier());
        mapData.setEnvironmentType(mapInfoDataPanel.getEnvType());

        mapData.setMapResources(resourceInfoDataPanel.getMapResources());

        List<MapResource> mapResources = mapData.getMapResources();
        List<String> resourceNames = new ArrayList<>();
        for (MapResource res : mapResources)
            resourceNames.add(res.getName());
        List<MapEvent> events = mapData.getEventList();
        for (MapEvent event : events) {
            ResourceProperty mainProp = event.getMainResourceProperty();
            if (mainProp != null)
                if (!resourceNames.contains(mainProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, mainProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
            ResourceProperty foreProp = event.getForegroundResourceProperty();
            if (foreProp != null)
                if (!resourceNames.contains(foreProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, foreProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
            ResourceProperty backProp = event.getBackgroundResourceProperty();
            if (backProp != null)
                if (!resourceNames.contains(backProp.getResourceName())) {
                    showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_ERROR_EVENT_INVALID_RESOURCE).replaceAll(EVENT_NAME, l.getKey(event.getEventHrNameKey())).replaceAll(EVENT_TIME, Float.toString(event.getTime())).replaceAll(EVENT_DATA, event.getEventData()).replaceAll(RESOURCE_NAME, backProp.getResourceName()).replaceAll(RESOURCE_NAME_LIST, Arrays.toString(resourceNames.toArray())));
                    return;
                }
        }
        mapData.setCheckpointTimingOffset(checkpointTimingOffsetSelector.getValue());
        mapData.setEventTimingOffset(eventTimingOffsetSelector.getValue());

        int progress = 0;
        //step 1 - save config
        eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_EXPORT_PROGRESSING).replaceAll(EXPORT_PROGRESS, Integer.toString(progress) + "%"));
        eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
        File configTarget = Paths.get(targetDirectory.getAbsolutePath(), IRiinaConstants.CONFIG_FILE).toFile();
        try {
            DataManager.exportMap(mapData, configTarget, true);
        } catch (IOException e) {
            e.printStackTrace();
            showError(l.getKey(KEY_UI_DIALOG_ERROR), l.getKey(KEY_FRAME_EDITOR_SAVE_FAIL_CONFIG).replaceAll(CONFIG_FILE, Matcher.quoteReplacement(configTarget.getAbsolutePath())).replaceAll(EXCEPTION, e.toString()));
        }
        //step 2 - save icon
        progress = 25;
        eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_EXPORT_PROGRESSING).replaceAll(EXPORT_PROGRESS, Integer.toString(progress) + "%"));
        eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
        File iconFilePath = Paths.get(resDirectory.getAbsolutePath(), mapData.getIconFile()).toFile();
        CopyFileResult iconCopyResult = DataManager.copyFile(iconFilePath, targetDirectory);
        if (iconCopyResult.failed()) {
            iconCopyResult.getException().printStackTrace();
            showError(l.getKey(KEY_UI_DIALOG_ERROR), iconCopyResult.getLocalizedFailureInfo(l));
        }

        //step 3 - save music
        progress = 50;
        eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_EXPORT_PROGRESSING).replaceAll(EXPORT_PROGRESS, Integer.toString(progress) + "%"));
        eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
        File musicFilePath = Paths.get(projectDirectory.getAbsolutePath(), mapData.getMusicFile()).toFile();
        CopyFileResult musicCopyResult = DataManager.copyFile(musicFilePath, targetDirectory);
        if (musicCopyResult.failed()) {
            musicCopyResult.getException().printStackTrace();
            showError(l.getKey(KEY_UI_DIALOG_ERROR), musicCopyResult.getLocalizedFailureInfo(l));
        }
        //step 4 - save resources
        for (int i = 0; i < mapResources.size(); i++) {
            MapResource res = mapResources.get(i);
            progress = 75 + ((25 * i) / mapResources.size());
            eventDisplayPanel.setMessage(l.getKey(KEY_FRAME_EDITOR_EXPORT_PROGRESSING).replaceAll(EXPORT_PROGRESS, Integer.toString(progress) + "%"));
            eventDisplayPanel.paintImmediately(eventDisplayPanel.getVisibleRect());
            File resourceFilePath = Paths.get(resDirectory.getAbsolutePath(), res.getPath()).toFile();
            CopyFileResult resourceCopyResult = DataManager.copyFile(resourceFilePath, targetDirectory);
            if (resourceCopyResult.failed()) {
                resourceCopyResult.getException().printStackTrace();
                showError(l.getKey(KEY_UI_DIALOG_ERROR), resourceCopyResult.getLocalizedFailureInfo(l));
            }
        }
        showMessage(l.getKey(KEY_UI_DIALOG_INFORMATION), l.getKey(KEY_FRAME_EDITOR_EXPORT_SUCCESS).replaceAll(TARGET_DIRECTORY, Matcher.quoteReplacement(targetDirectory.getAbsolutePath())));
        eventDisplayPanel.setMessage(null);
    }

    private boolean confirmExitSave() {
        int x = JOptionPane.showOptionDialog(this, leaveText, leaveTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), leaveOptions, leaveOptions[2]);
        if (x == 2 || x == JOptionPane.CLOSED_OPTION)
            return false;
        else if (x == 1)
            return true;
        saveProject();
        return true;
    }

    private boolean confirmReload() {
        int x = JOptionPane.showOptionDialog(this, reloadText, reloadTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, IRiina.getIcon64(), reloadOptions, reloadOptions[1]);
        return x == 0;
    }

    public void move(float deltaTime) {
        switch (manipTarget.getType()) {
            case NO_TARGET:
                break;
            case SELECTION_DRAG:
                Set<MapEvent> eset = new TreeSet<>();
                for (MapEvent e : realSelection) {
                    e.setTimeLight(e.getTime() + deltaTime);
                    TimingProperty tp = e.getTimingProperty();
                    if (tp != null)
                        eset.addAll(tp.setRootTimeLight(tp.getRootTime()));
                    eset.add(e);
                }
                mapData.repositionEventsFinalize(new ArrayList<>(eset));
                break;
            case CHECKPOINT_DRAG:
                manipTarget.getCheckpointTarget().setTime(manipTarget.getCheckpointTarget().getTime() + deltaTime);
                break;
            case EVENT_TIME_DRAG:
                manipTarget.getTarget().setTime(manipTarget.getTarget().getTime() + deltaTime);
                TimingProperty tp = manipTarget.getTarget().getTimingProperty();
                if (tp != null) {
                    eset = new TreeSet<>(tp.setRootTimeLight(tp.getRootTime()));
                    mapData.repositionEventsFinalize(new ArrayList<>(eset));
                }
                break;
            case TIMING_EVENT_LENGTH_DRAG:
                manipTarget.getTarget().getTimingProperty().setLength(manipTarget.getTarget().getTimingProperty().getLength() + deltaTime);
                tp = manipTarget.getTarget().getTimingProperty();
                if (tp != null) {
                    eset = new TreeSet<>(tp.setRootTimeLight(tp.getRootTime()));
                    mapData.repositionEventsFinalize(new ArrayList<>(eset));
                }
                break;
            case TIMING_EVENT_ROOT_TIME_DRAG:
                tp = manipTarget.getTarget().getTimingProperty();
                eset = new TreeSet<>(tp.setRootTimeLight(tp.getRootTime() + deltaTime));
                mapData.repositionEventsFinalize(new ArrayList<>(eset));
                break;
        }
    }

    public void prepareDrag(float initTarget, MapEvent.Type type) {
        if (type == MapEvent.Type.TIMING) {
            MapEvent closest = mapData.getClosestEvent(initTarget, type);
            MapEvent closestHandle = mapData.getClosestTimingLengthHandle(initTarget);
            MapEvent closestRoot = mapData.getClosestTimingRootTimeHandle(initTarget);

            if (closest != null) {
                float closeT = closest.getTime();
                float closeH = closestHandle.getTime() + closestHandle.getTimingProperty().getLength();
                float closeR = closestRoot.getTimingProperty().getRootTime();
                if (Math.abs(closeT - initTarget) < Math.abs(closeH - initTarget) && Math.abs(closeT - initTarget) < Math.abs(closeR - initTarget)) {
                    if (Math.abs(closeT - initTarget) < getConfig().getMaxManipGrabDistanceSec() && !realSelection.contains(closest)) {
                        clearSelection();
                        closest.concreteTime();
                        closest.concreteTimedMetaId();
                        closest.getTimingProperty().concreteRootTime();
                        manipTarget = new ManipTarget(ManipTarget.Type.EVENT_TIME_DRAG, closest);
                    } else if (realSelection.size() != 0) {
                        for (MapEvent evt : realSelection) {
                            evt.concreteTime();
                            evt.concreteTimedMetaId();
                            TimingProperty tp = evt.getTimingProperty();
                            if (tp != null)
                                tp.concreteRootTime();
                        }
                        manipTarget = new ManipTarget(ManipTarget.Type.SELECTION_DRAG, null);
                    }
                } else {
                    if (Math.abs(closeH - initTarget) < Math.abs(closeR - initTarget)) {
                        if (Math.abs(closeH - initTarget) < getConfig().getMaxManipGrabDistanceSec()) {
                            clearSelection();
                            closestHandle.getTimingProperty().concreteLength();
                            manipTarget = new ManipTarget(ManipTarget.Type.TIMING_EVENT_LENGTH_DRAG, closestHandle);
                        }
                    } else {
                        if (Math.abs(closeR - initTarget) < Math.abs(closeH - initTarget)) {
                            if (Math.abs(closeR - initTarget) < getConfig().getMaxManipGrabDistanceSec()) {
                                clearSelection();
                                closestRoot.getTimingProperty().concreteRootTime();
                                manipTarget = new ManipTarget(ManipTarget.Type.TIMING_EVENT_ROOT_TIME_DRAG, closestRoot);
                            }
                        } else {
                            if (initTarget >= closeH) {
                                if (Math.abs(closeH - initTarget) < getConfig().getMaxManipGrabDistanceSec()) {
                                    clearSelection();
                                    closestHandle.getTimingProperty().concreteLength();
                                    manipTarget = new ManipTarget(ManipTarget.Type.TIMING_EVENT_LENGTH_DRAG, closestHandle);
                                }
                            } else {
                                if (Math.abs(closeR - initTarget) < getConfig().getMaxManipGrabDistanceSec()) {
                                    clearSelection();
                                    closestRoot.getTimingProperty().concreteRootTime();
                                    manipTarget = new ManipTarget(ManipTarget.Type.TIMING_EVENT_ROOT_TIME_DRAG, closestRoot);
                                }
                            }
                        }
                    }
                }
            } else if (realSelection.size() != 0) {
                for (MapEvent evt : realSelection) {
                    evt.concreteTime();
                    evt.concreteTimedMetaId();
                    TimingProperty tp = evt.getTimingProperty();
                    if (tp != null)
                        tp.concreteRootTime();
                }
                manipTarget = new ManipTarget(ManipTarget.Type.SELECTION_DRAG, null);
            }

        } else {
            MapEvent closest = mapData.getClosestEvent(initTarget, type);
            if (closest != null) {
                if (Math.abs(closest.getTime() - initTarget) < getConfig().getMaxManipGrabDistanceSec() && !realSelection.contains(closest)) {
                    clearSelection();
                    closest.concreteTime();
                    closest.concreteTimedMetaId();
                    manipTarget = new ManipTarget(ManipTarget.Type.EVENT_TIME_DRAG, closest);
                } else if (realSelection.size() != 0) {
                    for (MapEvent evt : realSelection) {
                        evt.concreteTime();
                        evt.concreteTimedMetaId();
                        TimingProperty tp = evt.getTimingProperty();
                        if (tp != null)
                            tp.concreteRootTime();
                    }
                    manipTarget = new ManipTarget(ManipTarget.Type.SELECTION_DRAG, null);
                }
            }
        }
    }

    public void prepareCheckpointDrag(float initTarget) {
        Checkpoint c = mapData.getClosestCheckpoint(initTarget);
        if (c != null) {
            if (Math.abs(c.getTime() - initTarget) < getConfig().getMaxManipGrabDistanceSec()) {
                c.concreteTime();
                manipTarget = new ManipTarget(c);
            } else
                manipTarget = new ManipTarget();
        } else
            manipTarget = new ManipTarget();
    }

    public void clearDrag() {
        switch (manipTarget.getType()) {
            case NO_TARGET:
                break;
            case SELECTION_DRAG:
                List<Float> times = new ArrayList<>();
                List<Integer> timedMetaIds = new ArrayList<>();
                for (MapEvent evt : realSelection) {
                    times.add(evt.getTime());
                    timedMetaIds.add(evt.getTimedMetaId());
                }
                addOperation(ReversibleOperation.createEventGroupMoveOperation(realSelection, timedMetaIds, times));
                break;
            case EVENT_TIME_DRAG:
                MapEvent target = manipTarget.getTarget();
                addOperation(ReversibleOperation.createEventMoveOperation(target, target.getTimedMetaId(), target.getTime()));
                break;
            case CHECKPOINT_DRAG:
                addOperation(ReversibleOperation.createCheckpointMoveOperation(manipTarget.getCheckpointTarget(), manipTarget.getCheckpointTarget().getTime()));
                break;
            case TIMING_EVENT_LENGTH_DRAG:
                addOperation(ReversibleOperation.createTimingEventLengthModOperation(manipTarget.getTarget(), manipTarget.getTarget().getTimingProperty().getLength()));
                break;
            case TIMING_EVENT_ROOT_TIME_DRAG:
                addOperation(ReversibleOperation.createTimingEventRootTimeModOperation(manipTarget.getTarget(), manipTarget.getTarget().getTimingProperty().getRootTime()));
                break;
        }
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
        manipTarget = new ManipTarget();
    }

    public float getPlayHeadPercent() {
        return getPlayHeadPos() / length;
    }

    public void setPlayHeadPercent(float percent) {
        setPlayHeadPos(percent * length);
    }

    public float getPlayHeadPos() {
        if (!storedTimeThisFrame) {
            storedTime = (float) clip.getMicrosecondPosition() / 1000000.0f;
            storedTimeThisFrame = true;
        }
        return storedTime;
    }

    private void setPlayHeadPos(float posSec) {
        long targetMicroSecondValue = Math.max(0, Math.min((long) (posSec * 1000000.0f), clip.getMicrosecondLength() - 1));
        storedTime = (float) targetMicroSecondValue / 1000000.0f;
        storedTimeThisFrame = true;
        clip.setMicrosecondPosition(targetMicroSecondValue);
    }

    public void setSelectionDown(SelectionMode mode, MapEvent.Type type, float t1, float t2, boolean last) {
        if (t1 > t2) {
            float temp = t2;
            t2 = t1;
            t1 = temp;
        }
        selection.clear();
        List<MapEvent> l = null;
        if (t1 == t2 && last) {
            MapEvent targ = mapData.getClosestEvent(t1, type);
            if (targ == null)
                return;
            else {
                if (Math.abs(targ.getTime() - t1) < getConfig().getMaxManipGrabDistanceSec()) {
                    l = new ArrayList<>();
                    l.add(targ);
                }
            }
        } else
            l = mapData.getEventsBetweenTimes(t1, t2, true, type);

        if (l == null)
            return;

        for (MapEvent e2 : l) {
            selection.add(e2);
            TimedEventProperty tep = e2.getTimedEventProperty();
            if (tep != null)
                for (MapEvent e1 : prevSelection) {
                    TimingProperty tp = e1.getTimingProperty();
                    if (tp != null && tep.getTimingEventId() == e1.getMetaId())
                        selection.remove(e2);
                }
        }
        for (MapEvent e2 : l) {
            TimingProperty tp = e2.getTimingProperty();
            if (tp != null)
                for (MapEvent e1 : prevSelection) {
                    TimedEventProperty tep = e1.getTimedEventProperty();
                    if (tep != null && tep.getTimingEventId() == e1.getMetaId())
                        selection.remove(e2);
                }
        }
        realSelection.clear();
        if (mode != SelectionMode.REPLACE)
            realSelection.addAll(prevSelection);
        for (MapEvent e : selection) {
            switch (mode) {
                case ADDITIVE:
                    if (!realSelection.contains(e))
                        realSelection.add(e);
                    break;
                case SUBTRACTIVE:
                    realSelection.remove(e);
                    break;
                case INVERT:
                    if (realSelection.contains(e))
                        realSelection.remove(e);
                    else
                        realSelection.add(e);
                    break;
                case REPLACE:
                    realSelection.add(e);
            }
        }
    }

    public void setSelectionUp() {
        prevSelection.clear();
        prevSelection.addAll(realSelection);
        selection.clear();
    }

    private void selectAll() {
        clearDrag();
        clearSelection();

        List<MapEvent> l = mapData.getEventList();

        List<MapEvent> okEvents = new ArrayList<>(l);

        for (MapEvent evt : l) {
            TimedEventProperty tep = evt.getTimedEventProperty();
            if (tep != null) {
                for (MapEvent x : l) {
                    TimingProperty tp = x.getTimingProperty();
                    if (tp != null) {
                        if (tep.getTimingEventId() == x.getMetaId())
                            okEvents.remove(evt);
                    }
                }
            }
        }

        realSelection.addAll(okEvents);
        prevSelection.addAll(okEvents);
    }

    private void clearSelection() {
        realSelection.clear();
        selection.clear();
        prevSelection.clear();
    }

    private void undo() {
        clearDrag();
        clearSelection();
        if (operations.size() != 0 && opPoint != -1) {
            ReversibleOperation op = operations.get(opPoint);
            switch (op.getType()) {
                case RESOURCE_MOD:
                    resourceInfoDataPanel.replaceMapResource(op.getRes(), op.getOldRes());
                    break;
                case RESOURCE_ADD:
                    resourceInfoDataPanel.removeMapResource(op.getRes());
                    break;
                case RESOURCE_DELETE:
                    resourceInfoDataPanel.addMapResource(op.getRes());
                    break;
                case EVENT_CREATE:
                    mapData.removeEvent(op.getEvent());
                    break;
                case EVENT_DELETE:
                    mapData.addLoadedEvent(op.getEvent());
                    break;
                case EVENT_MOVE:
                    MapEvent event = op.getEvent();
                    event.revertTime();
                    event.revertTimedMetaId();
                    TimingProperty tp = event.getTimingProperty();
                    if (tp != null)
                        tp.revertRootTime();
                    break;
                case TIMING_EVENT_LENGTH_MOD:
                    tp = op.getEvent().getTimingProperty();
                    tp.revertLength();
                    tp.revertRootTime();
                    break;
                case TIMING_EVENT_ROOT_TIME_MOD:
                    op.getEvent().getTimingProperty().revertRootTime();
                    break;
                case EVENT_GROUP_CREATE:
                    for (MapEvent evt : op.getEventList())
                        mapData.removeEvent(evt);
                    break;
                case EVENT_GROUP_DELETE:
                    for (MapEvent evt : op.getEventList())
                        mapData.addLoadedEvent(evt);
                    break;
                case EVENT_GROUP_MOVE:
                    for (int i = 0; i < op.getEventList().size(); i++) {
                        MapEvent evt = op.getEventList().get(i);
                        evt.revertTime();
                        evt.revertTimedMetaId();
                        TimingProperty tpX = evt.getTimingProperty();
                        if (tpX != null)
                            tpX.revertRootTime();
                    }
                    break;
                case CHECKPOINT_CREATE:
                    mapData.removeCheckpoint(op.getCheckpoint());
                    break;
                case CHECKPOINT_DELETE:
                    mapData.addCheckpoint(op.getCheckpoint());
                    break;
                case CHECKPOINT_MOVE:
                    op.getCheckpoint().revertTime();
                    break;
                case EVENT_DATA_MOD:
                    event = op.getEvent();
                    event.setEventData(op.getOldData());
                    event.setEventExtraData(op.getOldExtraData());
                    event.setTimeRaw(op.getOldTime());
                    break;
                case EVENT_GROUP_DATA_MOD:
                    List<MapEvent> events = op.getEventList();
                    for (int i = 0; i < events.size(); i++) {
                        MapEvent evt = events.get(i);
                        evt.setEventData(op.getOldDataList().get(i));
                        evt.setEventExtraData(op.getOldExtraDataList().get(i));
                        evt.setTimeRaw(op.getOldTimeList().get(i));
                    }
                    break;
            }
            opPoint = Math.max(opPoint - 1, -1);
        }
        Locale l = parent.getLocale();
        boolean enableUndo = operations.size() != 0 && opPoint != -1;
        if (enableUndo)
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint).getOperationKey())));
        else
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO));
        undoItem.setEnabled(enableUndo);
        boolean enableRedo = opPoint + 1 < operations.size();
        if (enableRedo)
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint + 1).getOperationKey())));
        else
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO));
        redoItem.setEnabled(enableRedo);
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    private void redo() {
        clearDrag();
        clearSelection();
        if (opPoint + 1 < operations.size()) {
            ReversibleOperation op = operations.get(opPoint + 1);
            switch (op.getType()) {
                case RESOURCE_MOD:
                    resourceInfoDataPanel.replaceMapResource(op.getOldRes(), op.getRes());
                    break;
                case RESOURCE_ADD:
                    resourceInfoDataPanel.addMapResource(op.getRes());
                    break;
                case RESOURCE_DELETE:
                    resourceInfoDataPanel.removeMapResource(op.getRes());
                    break;
                case EVENT_CREATE:
                    mapData.addLoadedEvent(op.getEvent());
                    break;
                case EVENT_DELETE:
                    mapData.removeEvent(op.getEvent());
                    break;
                case EVENT_MOVE:
                    MapEvent event = op.getEvent();
                    event.concreteTimedMetaId();
                    event.concreteTime();
                    event.setTime(op.getTime());
                    event.setTimedMetaId(op.getTimedMetaId());
                    TimingProperty tp = op.getEvent().getTimingProperty();
                    if (tp != null)
                        tp.setRootTime(tp.getRootTime());
                    break;
                case TIMING_EVENT_LENGTH_MOD:
                    tp = op.getEvent().getTimingProperty();
                    tp.concreteLength();
                    tp.setLength(op.getLength());
                    tp.setRootTime(tp.getRootTime());
                    break;
                case TIMING_EVENT_ROOT_TIME_MOD:
                    tp = op.getEvent().getTimingProperty();
                    tp.concreteRootTime();
                    tp.setRootTime(op.getRootTime());
                case EVENT_GROUP_CREATE:
                    for (MapEvent evt : op.getEventList())
                        mapData.addLoadedEvent(evt);
                    break;
                case EVENT_GROUP_DELETE:
                    for (MapEvent evt : op.getEventList())
                        mapData.removeEvent(evt);
                    break;
                case EVENT_GROUP_MOVE:
                    for (int i = 0; i < op.getEventList().size(); i++) {
                        MapEvent evt = op.getEventList().get(i);
                        evt.concreteTimedMetaId();
                        evt.concreteTime();
                        evt.setTime(op.getTimeList().get(i));
                        evt.setTimedMetaId(op.getTimedMetaIds().get(i));
                        TimingProperty tpX = evt.getTimingProperty();
                        if (tpX != null) {
                            tpX.concreteRootTime();
                            tpX.setRootTime(tpX.getRootTime());
                        }
                    }
                    break;
                case CHECKPOINT_CREATE:
                    mapData.addCheckpoint(op.getCheckpoint());
                    break;
                case CHECKPOINT_DELETE:
                    mapData.removeCheckpoint(op.getCheckpoint());
                    break;
                case CHECKPOINT_MOVE:
                    op.getCheckpoint().concreteTime();
                    op.getCheckpoint().setTime(op.getTime());
                    break;
                case EVENT_DATA_MOD:
                    event = op.getEvent();
                    event.setEventData(op.getData());
                    event.setEventExtraData(op.getExtraData());
                    event.setTimeRaw(op.getTime());
                    break;
                case EVENT_GROUP_DATA_MOD:
                    List<MapEvent> events = op.getEventList();
                    for (int i = 0; i < events.size(); i++) {
                        MapEvent evt = events.get(i);
                        evt.setEventData(op.getDataList().get(i));
                        evt.setEventExtraData(op.getExtraDataList().get(i));
                        evt.setTimeRaw(op.getTimeList().get(i));
                    }
                    break;
            }
            opPoint++;
        }
        Locale l = parent.getLocale();
        boolean enableUndo = operations.size() != 0 && opPoint != -1;
        if (enableUndo)
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint).getOperationKey())));
        else
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO));
        undoItem.setEnabled(enableUndo);
        boolean enableRedo = opPoint + 1 < operations.size();
        if (enableRedo)
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint + 1).getOperationKey())));
        else
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO));
        redoItem.setEnabled(enableRedo);
        eventModContainerPanel.updateEventTargets();
        eventModContainerPanel.reevaluateTargets();
    }

    public boolean isPlaying() {
        return play;
    }

    private void setPlaying(boolean play) {
        if (play)
            clip.start();
        else
            clip.stop();
    }

    public void togglePlaying() {
        setPlaying(!play);
    }

    public EditorConfig getConfig() {
        return parent.getPreferences().getEditorConfig();
    }

    public void updateEventDisplayPanelTimes() {
        float rad = eventDisplayPanel.getWidth() * getConfig().getDisplayXScale();
        float pos = getPlayHeadPos();
        eventDisplayPanel.updateTimes(pos - rad, pos + rad);
    }

    public void updateEventDisplayPanel() {
        float pos = getPlayHeadPos();

        ArcRenderItem arcRenderItem;
        if (renderArcs || renderBeats) {
            float[] ticksArr = null;
            MapEvent timing = mapData.getFirstTimingEventForTime(pos);
            if (timing != null) {
                float tickMinPos = pos - getConfig().getArcDecayLength();
                TimingProperty p = timing.getTimingProperty();
                ticksArr = new float[p.getMeasureBeats()];

                int closestBeatTickBefore = p.getClosestBeatTickBeforeTime(pos);
                for (int i = 0; i < ticksArr.length; i++) {
                    int x = (p.getBeatFromTick(closestBeatTickBefore) - i) % p.getMeasureBeats();
                    if (x < 0)
                        x += p.getMeasureBeats();
                    ticksArr[x] = Math.max(0.0f, Math.min(1.0f, ((p.getTimeFromTick(closestBeatTickBefore - (i * p.getBeatTicks()))) - tickMinPos) / (pos - tickMinPos)));
                }
            }
            float[] arcRenderArr = new float[ArcProperty.ARR_LENGTH];
            float arcMinPos = pos - getConfig().getArcDecayLength();
            List<MapEvent> arcRenderEvents = mapData.getEventsBetweenTimes(arcMinPos, pos, false);
            for (MapEvent evt : arcRenderEvents) {
                ArcProperty arcProp = evt.getArcProperty();
                if (arcProp != null) {
                    float arcTime = evt.getTime();
                    boolean[] arr = arcProp.getArr();
                    for (int i = 0; i < arr.length; i++)
                        if (arr[i])
                            arcRenderArr[i] = (arcTime - arcMinPos) / (pos - arcMinPos);
                }
            }
            arcRenderItem = new ArcRenderItem(arcRenderArr, ticksArr);
        } else
            arcRenderItem = new ArcRenderItem(new float[ArcProperty.ARR_LENGTH], null);

        List<EventRenderItem> eventRenderItems = new ArrayList<>();
        List<EventRenderItem> targetEventRenderItems = new ArrayList<>();
        float rad = eventDisplayPanel.getWidth() * getConfig().getDisplayXScale();
        int[] selCount = new int[4];
        List<Float> checkpoints = new ArrayList<>();
        if (renderEvents) {
            List<MapEvent> evts = mapData.getEventsBetweenTimes(pos - rad, pos + rad, true);
            List<MapEvent> targeted = eventModContainerPanel.getTargets();
            for (MapEvent evt : realSelection)
                selCount[evt.getType().ordinal()]++;
            for (MapEvent evt : evts) {
                Color mainColor = null;
                Color topColor = null;
                Color botColor;
                float startTime = evt.getTime();
                float endTime = startTime;
                float rootTime = startTime;
                TimingProperty tp = evt.getTimingProperty();
                if (tp != null) {
                    endTime = startTime + tp.getLength();
                    rootTime = tp.getRootTime();
                }
                switch (evt.getType()) {
                    case VISUAL:
                        mainColor = Color.DARK_GRAY;
                        break;
                    case ARC:
                        mainColor = Color.CYAN;
                        break;
                    case TIMING:
                        mainColor = Color.BLUE;
                        break;
                    case COMBO:
                        mainColor = Color.MAGENTA;
                        break;
                }
                TimedEventProperty tep = evt.getTimedEventProperty();
                if (tep != null)
                    if (tep.getTimingEventId() != MapEvent.META_ID_DEF)
                        topColor = Color.BLUE;
                if (targeted.contains(evt)) {
                    botColor = Color.RED;
                    targetEventRenderItems.add(new EventRenderItem(mainColor, topColor, botColor, startTime, endTime, rootTime, evt.getType(), realSelection.contains(evt)));
                } else {
                    eventRenderItems.add(new EventRenderItem(mainColor, topColor, null, startTime, endTime, rootTime, evt.getType(), realSelection.contains(evt)));
                }
            }
            checkpoints.addAll(mapData.getCheckpointTimesBetweenTimes(pos - rad, pos + rad));
        }

        eventDisplayPanel.update(arcRenderItem, eventRenderItems, targetEventRenderItems, checkpoints, selCount, pos - rad, pos + rad, pos);
    }

    public void showControlReference() {
        controlReferenceFrame.showFrame();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (confirmExitSave())
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

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_EDITOR_TITLE).replaceAll(PROJECT_NAME, mapData == null ? "Undefined" : mapData.getName()));

        fileMenu.setText(l.getKey(KEY_MENU_FILE));
        newProjectItem.setText(l.getKey(KEY_MENU_FILE_NEW_PROJECT));
        openProjectItem.setText(l.getKey(KEY_MENU_FILE_OPEN_PROJECT));
        importMapItem.setText(l.getKey(KEY_MENU_FILE_IMPORT_MAP));
        saveProjectItem.setText(l.getKey(KEY_MENU_FILE_SAVE_PROJECT));
        exportMapItem.setText(l.getKey(KEY_MENU_FILE_EXPORT_PROJECT));
        reloadProjectItem.setText(l.getKey(KEY_MENU_FILE_RELOAD_PROJECT));
        editMenu.setText(l.getKey(KEY_MENU_EDIT));

        if (operations.size() != 0 && opPoint != -1)
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint).getOperationKey())));
        else
            undoItem.setText(l.getKey(KEY_MENU_EDIT_UNDO));
        if (opPoint + 1 < operations.size())
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO_OPERATION_EXISTS).replaceAll(OPERATION, l.getKey(operations.get(opPoint).getOperationKey())));
        else
            redoItem.setText(l.getKey(KEY_MENU_EDIT_REDO));

        cutItem.setText(l.getKey(KEY_MENU_EDIT_CUT));
        copyItem.setText(l.getKey(KEY_MENU_EDIT_COPY));
        pasteItem.setText(l.getKey(KEY_MENU_EDIT_PASTE));
        deleteItem.setText(l.getKey(KEY_MENU_EDIT_DELETE));
        selectAllItem.setText(l.getKey(KEY_MENU_EDIT_SELECT_ALL));
        applyChangesItem.setText(l.getKey(KEY_PANEL_EVENT_MOD_WRITE_CHANGES));
        applyChangesLeftItem.setText(l.getKey(KEY_MENU_EDIT_WRITE_CHANGES_BOT));
        applyChangesRightItem.setText(l.getKey(KEY_MENU_EDIT_WRITE_CHANGES_TOP));
        sequenceSelectionItem.setText(l.getKey(KEY_MENU_EDIT_SEQUENCE_SELECTION));
        generateSequenceItem.setText(l.getKey(KEY_MENU_EDIT_GENERATE_SEQUENCE));

        controlsMenu.setText(l.getKey(KEY_MENU_CONTROLS));
        togglePlay.setText(l.getKey(KEY_MENU_CONTROLS_TOGGLE_PLAY));
        positionBack.setText(l.getKey(KEY_MENU_CONTROLS_POSITION_BACK));
        positionFwd.setText(l.getKey(KEY_MENU_CONTROLS_POSITION_FWD));
        evtBack.setText(l.getKey(KEY_MENU_CONTROLS_EVT_BACK));
        evtFwd.setText(l.getKey(KEY_MENU_CONTROLS_EVT_FWD));

        helpMenu.setText(l.getKey(KEY_MENU_HELP));
        aboutItem.setText(l.getKey(KEY_MENU_HELP_ABOUT));
        preferencesItem.setText(l.getKey(KEY_MENU_HELP_PREFERENCES));

        leaveTitle = l.getKey(KEY_FRAME_EDITOR_EXIT_TITLE);
        leaveText = l.getKey(KEY_FRAME_EDITOR_EXIT_CONFIRM);
        leaveOptions[0] = l.getKey(KEY_FRAME_EDITOR_EXIT_OPTION_SAVE);
        leaveOptions[1] = l.getKey(KEY_FRAME_EDITOR_EXIT_OPTION_LEAVE);
        leaveOptions[2] = l.getKey(KEY_FRAME_EDITOR_EXIT_OPTION_CANCEL);

        reloadTitle = l.getKey(KEY_FRAME_EDITOR_RELOAD_TITLE);
        reloadText = l.getKey(KEY_FRAME_EDITOR_RELOAD_CONFIRM);
        reloadOptions[0] = l.getKey(KEY_FRAME_EDITOR_RELOAD_OPTION_LEAVE);
        reloadOptions[1] = l.getKey(KEY_FRAME_EDITOR_RELOAD_OPTION_CANCEL);

        exportTitle = l.getKey(KEY_FRAME_EDITOR_EXPORT_TITLE);
        exportOptions[0] = l.getKey(KEY_FRAME_EDITOR_EXPORT_OPTION_CONFIRM);
        exportOptions[1] = l.getKey(KEY_FRAME_EDITOR_EXPORT_OPTION_CANCEL);
        exportEditorDirectory = l.getKey(KEY_FRAME_EDITOR_EXPORT_EDITOR_DIRECTORY);
        exportEditorDirectorySelect = l.getKey(KEY_FRAME_EDITOR_EXPORT_EDITOR_DIRECTORY_SELECT);
        exportFolderName = l.getKey(KEY_FRAME_EDITOR_EXPORT_FOLDER_NAME);
        exportCheckpointTimingOffset = l.getKey(KEY_FRAME_EDITOR_EXPORT_CHECKPOINT_TIMING_OFFSET);
        exportEventTimingOffset = l.getKey(KEY_FRAME_EDITOR_EXPORT_EVENT_TIMING_OFFSET);

        sequenceSelectionTitle = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_TITLE);
        sequenceSelectionOptions[0] = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_OPTION_CONFIRM);
        sequenceSelectionOptions[1] = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_OPTION_CANCEL);
        sequenceSelectionBaseTick = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_TICK);
        sequenceSelectionBaseMeasure = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_MEASURE);
        sequenceSelectionBaseBeat = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_BEAT);
        sequenceSelectionBaseSubtick = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_BASE_SUBTICK);
        sequenceSelectionTickSeparation = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_TICK_SEPARATION);
        sequenceSelectionSubtickSeparation = l.getKey(KEY_FRAME_EDITOR_SEQUENCE_SELECTION_SUBTICK_SEPARATION);
        generateSequenceTitle = l.getKey(KEY_FRAME_EDITOR_GENERATE_SEQUENCE_TITLE);
        generateSequenceEventCount = l.getKey(KEY_FRAME_EDITOR_GENERATE_SEQUENCE_EVENT_COUNT);
        generateSequenceOptions[0] = l.getKey(KEY_FRAME_EDITOR_GENERATE_SEQUENCE_OPTION_CONFIRM);
        generateSequenceOptions[1] = l.getKey(KEY_FRAME_EDITOR_GENERATE_SEQUENCE_OPTION_CANCEL);

        eventDisplayPanel.localize(l);
        controlReferenceFrame.localize(l);
        eventDisplayControlPanel.localize(l);
        mapInfoDataPanel.localizeStrings(l);
        resourceInfoDataPanel.localize(l);

        eventModContainerPanel.localize(l);

    }

    public MapData getMapData() {
        return mapData;
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void queueRender() {
        if (!isPlaying())
            contentPane.repaint();
    }

    public void updateEventTargets(){
        eventModContainerPanel.updateEventTargets();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            storedTimeThisFrame = false;
            cutItem.setEnabled(realSelection.size() != 0);
            copyItem.setEnabled(realSelection.size() != 0);
            pasteItem.setEnabled(clipboard.size() != 0);
            boolean lA = eventModContainerPanel.topActive(), rA = eventModContainerPanel.botActive();
            applyChangesItem.setEnabled(lA || rA);
            applyChangesLeftItem.setEnabled(lA);
            applyChangesRightItem.setEnabled(rA);
            sequenceSelectionItem.setEnabled(realSelection.size() != 0);
            eventModContainerPanel.updateEventTargets();
            if (isPlaying())
                contentPane.repaint();
        } else {
            if (e.getSource() == newProjectItem) {
                setPlaying(false);
                if (confirmExitSave())
                    parent.setView(View.NewProjectDialogFrame);
            } else if (e.getSource() == importMapItem) {
                setPlaying(false);
                if (confirmExitSave())
                    parent.setView(View.MapImportFrame);
            } else if (e.getSource() == openProjectItem) {
                setPlaying(false);
                if (confirmExitSave())
                    parent.setView(View.OpenProjectDialogFrame);
            } else if (e.getSource() == aboutItem) {
                parent.showAboutFrame();
            } else if (e.getSource() == preferencesItem) {
                parent.showPreferencesFrame();
            } else if (e.getSource() == undoItem) {
                undo();
            } else if (e.getSource() == redoItem) {
                redo();
            } else if (e.getSource() == cutItem) {
                cut();
            } else if (e.getSource() == copyItem) {
                copy();
            } else if (e.getSource() == pasteItem) {
                paste();
            } else if (e.getSource() == saveProjectItem) {
                saveProject();
            } else if (e.getSource() == exportMapItem) {
                exportProject();
            } else if (e.getSource() == reloadProjectItem) {
                if (confirmReload())
                    reloadProject();
            } else if (e.getSource() == togglePlay) {
                togglePlaying();
            } else if (e.getSource() == positionBack) {
                setPlayHeadPos(getPlayHeadPos() - SKIP_AMT);
            } else if (e.getSource() == positionFwd) {
                setPlayHeadPos(getPlayHeadPos() + SKIP_AMT);
            } else if (e.getSource() == evtBack) {
                evtBack();
            } else if (e.getSource() == evtFwd) {
                evtFwd();
            } else if (e.getSource() == deleteItem) {
                deleteSelection();
            } else if (e.getSource() == selectAllItem) {
                selectAll();
            } else if (e.getSource() == applyChangesItem) {
                eventModContainerPanel.applyChanges();
            } else if (e.getSource() == applyChangesLeftItem) {
                eventModContainerPanel.applyChangesTop();
            } else if (e.getSource() == applyChangesRightItem) {
                eventModContainerPanel.applyChangesBot();
            } else if (e.getSource() == sequenceSelectionItem) {
                sequenceSelection();
            } else if (e.getSource() == generateSequenceItem) {
                generateSequence();
            }
            queueRender();
        }
    }

    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP)
            play = false;
        else if (event.getType() == LineEvent.Type.START)
            play = true;
    }

    public enum SelectionMode {
        REPLACE,
        ADDITIVE,
        SUBTRACTIVE,
        INVERT

    }
}
