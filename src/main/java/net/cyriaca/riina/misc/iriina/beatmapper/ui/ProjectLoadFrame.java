package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.*;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.data.LoadTarget;
import net.cyriaca.riina.misc.iriina.beatmapper.data.ProjectMeta;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import net.cyriaca.riina.misc.iriina.intralism.data.MapParseResult;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProjectLoadFrame extends JFrame implements IViewFrame, LocaleChangeListener {

    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_FRAME_PROJECT_LOAD_TITLE = "frame_project_load_title";
    private static final String KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_MAP_DATA = "frame_project_load_stage_loading_map_data";
    private static final String MAP_DATA_FILE = "%mapDataFile%";
    private static final String KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_BACKUP_MAP_DATA = "frame_project_load_stage_loading_backup_map_data";
    private static final String BACKUP_MAP_DATA_FILE = "%backupMapDataFile%";
    private static final String KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_CLIP = "frame_project_load_stage_loading_clip";
    private static final String AUDIO_FILE = "%audioFile%";
    private static final String KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_ICON = "frame_project_load_stage_loading_map_icon";
    private static final String MAP_ICON_FILE = "%iconFile%";
    private static final String KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_RESOURCES = "frame_project_load_stage_loading_resources";
    private static final String RESOURCE_COUNT = "%resourceCount%";
    private static final String KEY_FRAME_PROJECT_LOAD_FAILURE_PROJECT_DIRECTORY_NULL = "frame_project_load_failure_project_directory_null";
    private static final String KEY_FRAME_PROJECT_LOAD_FAILURE_CONFIG_NOT_FOUND = "frame_project_load_failure_config_not_found";
    private static final String KEY_FRAME_PROJECT_LOAD_FAILURE_BACKUP_NOT_FOUND = "frame_project_load_failure_backup_not_found";
    private JPanel contentPane;
    private BorderLayout progressLabelPanelLayout;
    private JPanel progressLabelPanel;
    private JLabel progressLabel;
    private BorderLayout progressBarPanelLayout;
    private JPanel progressBarPanel;
    private JProgressBar progressBar;

    private IRiina parent;

    public ProjectLoadFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        progressLabelPanelLayout = new BorderLayout();
        progressLabelPanel = new JPanel(progressLabelPanelLayout);
        progressLabel = new JLabel(" ");
        progressLabelPanel.add(progressLabel, BorderLayout.CENTER);
        contentPane.add(progressLabelPanel);
        progressBarPanelLayout = new BorderLayout();
        progressBarPanel = new JPanel(progressBarPanelLayout);
        progressBar = new JProgressBar(0, 100);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);
        contentPane.add(progressBarPanel);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void setupAndShowFrame() {
        setSize(500, 150);
        setLocationRelativeTo(null);
        setVisible(true);
        progressBar.setValue(0);
        progressLabel.setText(" ");
        contentPane.paintImmediately(contentPane.getVisibleRect());
        Locale l = parent.getLocale();
        LoadTarget target = parent.getLoadTarget();
        File projectDirectory = target.getProjectDirectory();
        if (projectDirectory == null) {
            JOptionPane.showMessageDialog(this, KEY_FRAME_PROJECT_LOAD_FAILURE_PROJECT_DIRECTORY_NULL,
                    l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
            progressBar.setValue(0);
            progressLabel.setText(" ");
            contentPane.paintImmediately(contentPane.getVisibleRect());
            parent.setView(View.WelcomeFrame);
            return;
        }
        MapData data;
        File backupFile = target.getBackupFile();
        if (!target.isBackup()) {
            data = target.getMapData();
            if (data == null) {
                File dataFile = new File(projectDirectory, "config.txt");
                progressLabel.setText(l.getKey(KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_MAP_DATA).replaceAll(MAP_DATA_FILE,
                        dataFile.getAbsolutePath()));
                contentPane.paintImmediately(contentPane.getVisibleRect());
                MapParseResult mapParseResult;
                try {
                    mapParseResult = DataManager.parseMap(dataFile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            l.getKey(KEY_FRAME_PROJECT_LOAD_FAILURE_CONFIG_NOT_FOUND).replaceAll(MAP_DATA_FILE,
                                    dataFile.getAbsolutePath()),
                            l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    progressLabel.setText(" ");
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    parent.setView(IRiina.View.WelcomeFrame);
                    parent.setView(View.WelcomeFrame);
                    return;
                }
                if (mapParseResult.failed()) {
                    JOptionPane.showMessageDialog(this,
                            mapParseResult.getLocalizedFailureInfo(l) + "\n(\"" + dataFile.getAbsolutePath() + "\")",
                            l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    progressLabel.setText(" ");
                    contentPane.paintImmediately(contentPane.getVisibleRect());
                    parent.setView(View.WelcomeFrame);
                    return;
                }
                data = mapParseResult.getMapData();
            }
        } else {
            progressLabel.setText(l.getKey(KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_BACKUP_MAP_DATA)
                    .replaceAll(BACKUP_MAP_DATA_FILE, backupFile.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            MapParseResult mapParseResult;
            try {
                mapParseResult = DataManager.parseMap(backupFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        l.getKey(KEY_FRAME_PROJECT_LOAD_FAILURE_BACKUP_NOT_FOUND).replaceAll(MAP_DATA_FILE,
                                backupFile.getAbsolutePath()),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                parent.setView(IRiina.View.WelcomeFrame);
                parent.setView(View.WelcomeFrame);
                return;
            }
            if (mapParseResult.failed()) {
                JOptionPane.showMessageDialog(this,
                        mapParseResult.getLocalizedFailureInfo(l) + "\n(\"" + backupFile.getAbsolutePath() + "\")",
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                parent.setView(IRiina.View.WelcomeFrame);
                parent.setView(View.WelcomeFrame);
                return;
            }
            data = mapParseResult.getMapData();
        }
        Clip clip = target.getClip();
        if (clip == null) {
            File musicFile = new File(projectDirectory, data.getMusicFile());
            progressBar.setValue(25);
            progressLabel.setText(l.getKey(KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_CLIP).replaceAll(AUDIO_FILE,
                    musicFile.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            ClipLoadResult clipLoadResult = DataManager.loadClip(musicFile.getAbsolutePath());
            if (clipLoadResult.failed()) {
                JOptionPane.showMessageDialog(this, clipLoadResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                parent.setView(IRiina.View.WelcomeFrame);
                parent.setView(View.WelcomeFrame);
                return;
            }
            clip = clipLoadResult.getClip();
        }
        String imgPath = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES, data.getIconFile()).toAbsolutePath().toString();
        progressBar.setValue(50);
        progressLabel
                .setText(l.getKey(KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_ICON).replaceAll(MAP_ICON_FILE, imgPath));
        contentPane.paintImmediately(contentPane.getVisibleRect());
        Image image = DataManager.getImage(imgPath);
        data.setIconImage(image);
        List<MapResource> resources = data.getMapResources();
        List<MapResource> newResources = new ArrayList<>();
        progressBar.setValue(75);
        progressLabel.setText(l.getKey(KEY_FRAME_PROJECT_LOAD_STAGE_LOADING_RESOURCES).replaceAll(RESOURCE_COUNT,
                Integer.toString(resources.size())));
        contentPane.paintImmediately(contentPane.getVisibleRect());
        for (MapResource r : resources) {
            String resImgPath = Paths.get(projectDirectory.getAbsolutePath(), IRiinaConstants.PROJECT_DIR_RESOURCES, r.getPath()).toAbsolutePath().toString();
            Image resImage = DataManager.getImage(resImgPath);
            newResources.add(new MapResource(r.getName(), r.getPath(), resImage));
        }
        data.setMapResources(newResources);
        progressBar.setValue(100);
        contentPane.paintImmediately(contentPane.getVisibleRect());
        ProjectMeta meta = new ProjectMeta(projectDirectory, target.isBackup(), target.getBackupFile(), data, clip);
        parent.setProjectMeta(meta);
        parent.setView(View.EditorFrame);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_PROJECT_LOAD_TITLE));
    }
}
