package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.*;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ProjectMeta;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFolderSelectorItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledStringItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import net.cyriaca.riina.misc.iriina.intralism.data.MapParseResult;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MapImportFrame extends JFrame implements IViewFrame, LocaleChangeListener, WindowListener, ActionListener {

    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_FRAME_MAP_IMPORT_TITLE = "frame_map_import_title";

    private static final String KEY_FRAME_MAP_IMPORT_UI_PROJ_CHOOSER_TITLE = "frame_map_import_ui_proj_chooser_title";

    private static final String KEY_FRAME_MAP_IMPORT_UI_MAP_SELECTOR_TITLE = "frame_map_import_ui_map_selector_title";
    private static final String KEY_FRAME_MAP_IMPORT_UI_MAP_SELECTOR_SELECT = "frame_map_import_ui_map_selector_select";

    private static final String KEY_FRAME_MAP_IMPORT_UI_IMPORT_BUTTON = "frame_map_import_ui_import_button";

    private static final String KEY_FRAME_MAP_IMPORT_ERR_PROJ_DIR_EMPTY = "frame_map_import_err_proj_dir_empty";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_PROJ_DIR_EXISTS = "frame_map_import_err_proj_dir_exists";
    private static final String PROJ_DIR = "%projDir%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_EMPTY = "frame_map_import_err_map_dir_empty";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_DNE = "frame_map_import_err_map_dir_dne";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_IS_FILE = "frame_map_import_err_map_dir_is_file";
    private static final String MAP_DIR = "%mapDir%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_CONFIG_FILE_DNE = "frame_map_import_err_config_file_dne";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_CONFIG_FILE_IS_DIRECTORY = "frame_map_import_err_config_file_is_directory";
    private static final String CONFIG_FILE = "%configFile%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_DNE = "frame_map_import_err_icon_file_dne";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_IS_DIRECTORY = "frame_map_import_err_icon_file_is_directory";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_TYPE_ERR = "frame_map_import_err_icon_file_is_type_err";
    private static final String ICON_FILE = "%iconFile%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_MUSIC_FILE_DNE = "frame_map_import_err_music_file_dne";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_MUSIC_FILE_IS_DIRECTORY = "frame_map_import_err_music_file_is_directory";
    private static final String MUSIC_FILE = "%musicFile%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_DNE = "frame_map_import_err_resource_file_dne";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_IS_DIRECTORY = "frame_map_import_err_resource_file_is_directory";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_TYPE_ERR = "frame_map_import_err_resource_file_type_err";
    private static final String RESOURCE_FILE = "%resourceFile%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_DIR_CREATION_FAILED = "frame_map_import_err_resource_dir_creation_failed";
    private static final String RESOURCE_DIR = "%resourceDir%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_WRITE_CONFIG_ERR = "frame_map_import_err_write_config_err";
    private static final String CP_OUT = "%cpOut%";
    private static final String KEY_FRAME_MAP_IMPORT_ERR_STRIPPING_METADATA_FAILED = "frame_map_import_err_stripping_metadata_failed";
    private static final String EXCEPTION = "%exception%";

    private JPanel contentPane;

    private JPanel panel;
    private BoxLayout panelLayout;
    private ArxTitledStringItem projectDirectoryChooser;
    private ArxTitledFolderSelectorItem mapDirectorySelector;
    private JButton importButton;
    private JProgressBar progressBar;
    private JScrollPane scrollPane;
    private IRiina parent;

    public MapImportFrame(IRiina parent) {
        super();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.parent = parent;

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        panel = new JPanel();
        panelLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(panelLayout);

        projectDirectoryChooser = new ArxTitledStringItem();
        mapDirectorySelector = new ArxTitledFolderSelectorItem();

        progressBar = new JProgressBar(0, 100);

        JPanel importPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        importButton = new JButton();
        importPanel.add(importButton);

        panel.add(mapDirectorySelector);
        panel.add(projectDirectoryChooser);
        panel.add(importPanel);

        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(panel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(minHolder);
        add(scrollPane, BorderLayout.CENTER);

        add(progressBar, BorderLayout.SOUTH);
        addWindowListener(this);
        importButton.addActionListener(this);
        IRiina.brandFrameWithGloriousEmblem(this);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void setupAndShowFrame() {
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);
        projectDirectoryChooser.setValue("");
        mapDirectorySelector.setDirectory("");
        progressBar.setValue(0);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_MAP_IMPORT_TITLE));
        projectDirectoryChooser.setTitle(l.getKey(KEY_FRAME_MAP_IMPORT_UI_PROJ_CHOOSER_TITLE));
        mapDirectorySelector.setTitle(l.getKey(KEY_FRAME_MAP_IMPORT_UI_MAP_SELECTOR_TITLE));
        mapDirectorySelector.setButtonText(l.getKey(KEY_FRAME_MAP_IMPORT_UI_MAP_SELECTOR_SELECT));
        importButton.setText(l.getKey(KEY_FRAME_MAP_IMPORT_UI_IMPORT_BUTTON));
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == importButton) {
            Locale l = parent.getLocale();
            String projDirStr = projectDirectoryChooser.getValue();
            if (projDirStr == null || projDirStr.length() == 0) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_MAP_IMPORT_ERR_PROJ_DIR_EMPTY),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            String mapDirStr = mapDirectorySelector.getDirectory();
            if (mapDirStr == null || mapDirStr.length() == 0) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_EMPTY),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 1 - check directories
            progressBar.setValue(0);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            File projDir = new File(parent.getPreferences().getWorkspaceDirectory(), projDirStr);
            if (projDir.exists()) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_MAP_IMPORT_ERR_PROJ_DIR_EXISTS).replaceAll(
                        PROJ_DIR, Matcher.quoteReplacement(projDir.getAbsolutePath())), l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // check if map directory exists / actually is a directory (need it)
            File mapDir = new File(mapDirStr);
            if (!mapDir.exists()) {
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_DNE).replaceAll(MAP_DIR, Matcher.quoteReplacement(mapDir.getAbsolutePath())),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            } else if (!mapDir.isDirectory()) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_MAP_IMPORT_ERR_MAP_DIR_IS_FILE).replaceAll(
                        MAP_DIR, Matcher.quoteReplacement(mapDir.getAbsolutePath())), l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 2 - load config
            progressBar.setValue(10);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            File configFile = new File(mapDir, IRiinaConstants.CONFIG_FILE);
            if (!configFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_MAP_IMPORT_ERR_CONFIG_FILE_DNE).replaceAll(CONFIG_FILE,
                                Matcher.quoteReplacement(configFile.getAbsolutePath())),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            } else if (!configFile.isFile()) {
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_MAP_IMPORT_ERR_CONFIG_FILE_IS_DIRECTORY).replaceAll(CONFIG_FILE,
                                Matcher.quoteReplacement(configFile.getAbsolutePath())),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // load config data
            MapData data;
            MapParseResult parseResult = null;
            try {
                parseResult = DataManager.parseMap(configFile.getAbsolutePath(), 0.0f, 0.0f);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                System.exit(3044);
            }
            if (parseResult.failed()) {
                JOptionPane.showMessageDialog(this, parseResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            data = parseResult.getMapData();

            File musicFile = new File(mapDir, data.getMusicFile());
            if (!musicFile.exists()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_MUSIC_FILE_DNE).replaceAll(MUSIC_FILE,
                                        Matcher.quoteReplacement(musicFile.getAbsolutePath())),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            } else if (!musicFile.isFile()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_MUSIC_FILE_IS_DIRECTORY).replaceAll(MUSIC_FILE,
                                        Matcher.quoteReplacement(musicFile.getAbsolutePath())),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }

            // STEP 3 - load icon
            progressBar.setValue(20);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            File iconFile = new File(mapDir, data.getIconFile());
            if (!iconFile.exists()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_DNE).replaceAll(ICON_FILE,
                                        Matcher.quoteReplacement(iconFile.getAbsolutePath())),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            } else if (!iconFile.isFile()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_IS_DIRECTORY).replaceAll(ICON_FILE,
                                        Matcher.quoteReplacement(iconFile.getAbsolutePath())),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            if (!DataManager.isIntralismSupportedImage(iconFile)) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_ICON_FILE_TYPE_ERR).replaceAll(ICON_FILE,
                                        Matcher.quoteReplacement(iconFile.getAbsolutePath())),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            data.setIconImage(DataManager.getImage(iconFile));

            // STEP 4 - load resources
            progressBar.setValue(30);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            List<MapResource> resources = data.getMapResources();
            List<File> resourceFiles = new ArrayList<>();
            List<MapResource> newResources = new ArrayList<>();
            for (MapResource resource : resources) {
                File resFile = new File(mapDir, resource.getPath());
                if (!resFile.exists()) {
                    JOptionPane.showMessageDialog(this,
                            l.getKey(KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_DNE).replaceAll(RESOURCE_FILE,
                                    Matcher.quoteReplacement(resFile.getAbsolutePath())),
                            l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    return;
                } else if (!resFile.isFile()) {
                    JOptionPane.showMessageDialog(this,
                            l.getKey(KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_IS_DIRECTORY).replaceAll(RESOURCE_FILE,
                                    Matcher.quoteReplacement(resFile.getAbsolutePath())),
                            l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    return;
                }
                if (!DataManager.isIntralismSupportedImage(resFile)) {
                    JOptionPane.showMessageDialog(this,
                            l.getKey(KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_FILE_TYPE_ERR).replaceAll(RESOURCE_FILE,
                                    Matcher.quoteReplacement(resFile.getAbsolutePath())),
                            l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    return;
                }
                newResources.add(new MapResource(resource.getName(), resource.getPath(), DataManager.getImage(resFile)));
                resourceFiles.add(resFile);
            }
            data.setMapResources(newResources);
            // STEP 5 - create project directories
            progressBar.setValue(40);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            DirectoryCreationResult projectDirectoryCreationResult = DataManager.createDirectory(projDir);
            if (projectDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this, projectDirectoryCreationResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // create subdirs
            File resourcesDir = new File(projDir, IRiinaConstants.PROJECT_DIR_RESOURCES);
            DirectoryCreationResult resourcesDirectoryCreationResult = DataManager.createDirectory(resourcesDir);
            if (resourcesDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_MAP_IMPORT_ERR_RESOURCE_DIR_CREATION_FAILED).replaceAll(RESOURCE_DIR,
                                Matcher.quoteReplacement(resourcesDir.getAbsolutePath())),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            File backupDir = new File(projDir, IRiinaConstants.PROJECT_DIR_BACKUP);
            DirectoryCreationResult backupDirectoryCreationResult = DataManager.createDirectory(backupDir);
            if (backupDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this, backupDirectoryCreationResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 6 - write config
            progressBar.setValue(50);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            File configOut = new File(projDir, IRiinaConstants.CONFIG_FILE);
            try {
                DataManager.exportMap(data, configOut, 0.0f, 0.0f, false);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_MAP_IMPORT_ERR_WRITE_CONFIG_ERR).replaceAll(
                        CP_OUT, Matcher.quoteReplacement(configOut.getAbsolutePath())), l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 7 - copy icon
            progressBar.setValue(60);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            CopyFileResult iconCopyResult = DataManager.copyFile(iconFile, resourcesDir);
            if (iconCopyResult.failed()) {
                JOptionPane.showMessageDialog(this, iconCopyResult.getLocalizedFailureInfo(l), l.getKey(KEY_UI_DIALOG_ERROR),
                        JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 8 - copy music
            progressBar.setValue(70);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            CopyFileResult musicCopyResult = DataManager.copyFile(musicFile, projDir);
            if (musicCopyResult.failed()) {
                JOptionPane.showMessageDialog(this, musicCopyResult.getLocalizedFailureInfo(l), l.getKey(KEY_UI_DIALOG_ERROR),
                        JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            File copiedMusicFile = new File(projDir, musicFile.getName());
            AudioFile af;
            try {
                af = AudioFileIO.read(copiedMusicFile);
                VorbisCommentTag ovtag = (VorbisCommentTag) af.getTag();
                ovtag.deleteField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);
                ovtag.deleteField(VorbisCommentFieldKey.COVERART);
                ovtag.deleteField(VorbisCommentFieldKey.COVERARTMIME);
                af.commit();
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e1) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_MAP_IMPORT_ERR_STRIPPING_METADATA_FAILED).replaceAll(EXCEPTION,
                                        e1.getMessage()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // STEP 9 - load music
            progressBar.setValue(80);
            contentPane.paintImmediately(contentPane.getVisibleRect());

            Clip clip;
            ClipLoadResult clipLoadResult = DataManager.loadClip(copiedMusicFile);
            if (clipLoadResult.failed()) {
                JOptionPane.showMessageDialog(this, clipLoadResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            clip = clipLoadResult.getClip();

            // STEP 10 - copy resources
            progressBar.setValue(90);
            contentPane.paintImmediately(contentPane.getVisibleRect());
            for (File resourceFile : resourceFiles) {
                CopyFileResult resCopyResult = DataManager.copyFile(resourceFile, resourcesDir);
                if (resCopyResult.failed()) {
                    JOptionPane.showMessageDialog(this, resCopyResult.getLocalizedFailureInfo(l), l.getKey(KEY_UI_DIALOG_ERROR),
                            JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    return;
                }
            }
            ProjectMeta meta = new ProjectMeta(projDir, false, null, data, clip);
            parent.openEditor(meta);
        }
    }

}
