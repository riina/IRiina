package net.cyriaca.riina.misc.iriina.editor.ui;

import net.cyriaca.riina.misc.iriina.editor.*;
import net.cyriaca.riina.misc.iriina.editor.IRiina.View;
import net.cyriaca.riina.misc.iriina.editor.data.ProjectMeta;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledFileSelectorItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledImageSelectorItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledStringItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import javax.activation.MimetypesFileTypeMap;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NewProjectDialogFrame extends JFrame
        implements IViewFrame, ActionListener, LocaleChangeListener, WindowListener {

    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_FRAME_NEW_PROJECT_TITLE = "frame_new_project_title";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_MUSIC_FILE = "frame_new_project_text_music_file";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_MUSIC_FILE_SELECT = "frame_new_project_text_music_file_select";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_MAP_ID = "frame_new_project_text_map_id";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_ICON_FILE = "frame_new_project_text_icon_file";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_ICON_FILE_SELECT = "frame_new_project_text_icon_file_select";
    private static final String KEY_FRAME_NEW_PROJECT_TEXT_CREATE = "frame_new_project_text_create";

    private static final String KEY_FRAME_NEW_PROJECT_EMPTY_ID_VALUE = "frame_new_project_empty_id_value";
    private static final String KEY_FRAME_NEW_PROJECT_ERR_ICON_FILE_MISSING = "frame_new_project_err_icon_file_missing";
    private static final String ICON_FILE = "%iconFile%";
    private static final String KEY_FRAME_NEW_PROJECT_ERR_AUDIO_FILE_MISSING = "frame_new_project_err_audio_file_missing";
    private static final String AUDIO_FILE = "%audioFile%";
    private static final String KEY_FRAME_NEW_PROJECT_ERR_ICON_FILE_READ_ERROR = "frame_new_project_err_icon_file_read_error";
    private static final String KEY_FRAME_NEW_PROJECT_PROG_LOAD_AUDIO = "frame_new_project_prog_load_audio";
    private static final String KEY_FRAME_NEW_PROJECT_PROG_COPY_MUSIC_FILE = "frame_new_project_prog_copy_music_file";
    private static final String TARGET_DIRECTORY = "%targetDirectory%";
    private static final String KEY_FRAME_NEW_PROJECT_PROG_COPY_ICON_FILE = "frame_new_project_prog_copy_icon_file";
    private static final String KEY_FRAME_NEW_PROJECT_PROG_WRITE_LEVEL_DATA = "frame_new_project_prog_write_level_data";
    private static final String KEY_FRAME_NEW_PROJECT_ERR_WRITE_LEVEL_DATA_FAILED = "frame_new_project_err_write_level_data_failed";
    private static final String KEY_FRAME_NEW_PROJECT_PROG_STRIPPING_METADATA = "frame_new_project_prog_stripping_metadata";
    private static final String KEY_FRAME_NEW_PROJECT_ERR_STRIPPING_METADATA_FAILED = "frame_new_project_err_stripping_metadata_failed";
    private static final String IO_EXCEPTION = "%ioException%";
    private static final String EXCEPTION = "%exception%";

    private JPanel panel;
    private BoxLayout panelLayout;
    private ArxTitledFileSelectorItem musicFileItem;
    private ArxTitledStringItem mapIdItem;
    private ArxTitledImageSelectorItem iconItem;
    private FlowLayout createPanelLayout;
    private JPanel createPanel;
    private JButton create;
    private BoxLayout progressLayout;
    private JPanel progress;
    private BorderLayout progressLabelPanelLayout;
    private JPanel progressLabelPanel;
    private JLabel progressLabel;
    private BorderLayout progressBarPanelLayout;
    private JPanel progressBarPanel;
    private JProgressBar progressBar;
    private JScrollPane scrollPane;
    private JPanel contentPane;
    private BorderLayout mainLayout;
    private IRiina parent;

    public NewProjectDialogFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainLayout = new BorderLayout();
        contentPane = new JPanel(mainLayout);
        setContentPane(contentPane);

        panel = new JPanel();
        panelLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(panelLayout);

        mapIdItem = new ArxTitledStringItem();
        musicFileItem = new ArxTitledFileSelectorItem();
        iconItem = new ArxTitledImageSelectorItem();

        createPanelLayout = new FlowLayout(FlowLayout.LEFT);
        createPanel = new JPanel(createPanelLayout);
        create = new JButton();
        createPanel.add(create);

        progress = new JPanel();
        progressLayout = new BoxLayout(progress, BoxLayout.Y_AXIS);
        progress.setLayout(progressLayout);
        progressLabelPanelLayout = new BorderLayout();
        progressLabelPanel = new JPanel(progressLabelPanelLayout);
        progressLabel = new JLabel();
        progressLabelPanel.add(progressLabel, BorderLayout.CENTER);
        progress.add(progressLabelPanel);
        progressBarPanelLayout = new BorderLayout();
        progressBarPanel = new JPanel(progressBarPanelLayout);
        progressBar = new JProgressBar(0, 100);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);
        progress.add(progressBarPanel);

        panel.add(mapIdItem);
        panel.add(musicFileItem);
        panel.add(iconItem);
        panel.add(createPanel);

        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(panel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(minHolder);
        add(scrollPane, BorderLayout.CENTER);
        add(progress, BorderLayout.SOUTH);
        addWindowListener(this);
        create.addActionListener(this);
    }

    public void setupAndShowFrame() {
        setSize(500, 380);
        setLocationRelativeTo(null);
        musicFileItem.setFile("");
        mapIdItem.setValue("");
        iconItem.setFile("", null);
        progressBar.setValue(0);
        progressLabel.setText(" ");
        setVisible(true);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_NEW_PROJECT_TITLE));
        musicFileItem.setTitle(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_MUSIC_FILE));
        musicFileItem.setButtonText(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_MUSIC_FILE_SELECT));
        mapIdItem.setTitle(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_MAP_ID));
        iconItem.setTitle(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_ICON_FILE));
        iconItem.setLabelTitle(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_ICON_FILE));
        iconItem.setButtonText(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_ICON_FILE_SELECT));
        create.setText(l.getKey(KEY_FRAME_NEW_PROJECT_TEXT_CREATE));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            Locale l = parent.getLocale();
            String projectName = mapIdItem.getValue();
            if (projectName == null || projectName.length() == 0) {
                JOptionPane.showMessageDialog(this, l.getKey(KEY_FRAME_NEW_PROJECT_EMPTY_ID_VALUE),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            File musicFile = new File(musicFileItem.getFile());
            File iconFile = new File(iconItem.getFile());
            // CHECK ICON EXISTS
            if (!iconFile.exists()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_ICON_FILE_MISSING).replaceAll(ICON_FILE,
                                        iconFile.getAbsolutePath()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // CHECK IMAGE VALID
            String iconFileName = iconFile.getName();
            if (!iconFileName.endsWith(".png") && !iconFileName.endsWith(".jpeg") && !iconFileName.endsWith(".jpg")) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_ICON_FILE_READ_ERROR).replaceAll(ICON_FILE,
                                        iconFile.getAbsolutePath()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            MimetypesFileTypeMap map = new MimetypesFileTypeMap();
            map.addMimeTypes("image png tif jpg jpeg bmp");
            List<String> types = Arrays.asList(map.getContentType(iconFile).split("/"));
            if (!(types.contains("image") || types.contains("png") || types.contains("jpeg")
                    || types.contains("jpg"))) {
                System.err.println("DETTYPE - " + Arrays.toString(types.toArray()));
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_ICON_FILE_READ_ERROR).replaceAll(ICON_FILE,
                                        iconFile.getAbsolutePath()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            // CHECK MUSIC EXISTS
            if (!musicFile.exists()) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_AUDIO_FILE_MISSING).replaceAll(AUDIO_FILE,
                                        musicFile.getAbsolutePath()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            String workspaceDirectory = parent.getPreferences().getWorkspaceDirectory();
            File projectFolder = new File(workspaceDirectory, projectName);
            Image img = DataManager.getImage(iconFile.getAbsolutePath());
            // CREATE PROJECT DIR
            File projectDirectory = new File(workspaceDirectory, projectName);
            DirectoryCreationResult projectDirectoryCreationResult = DataManager.createDirectory(projectDirectory);
            if (projectDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this, projectDirectoryCreationResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            DirectoryCreationResult resourcesDirectoryCreationResult = DataManager
                    .createDirectory(new File(projectDirectory, IRiinaConstants.PROJECT_DIR_RESOURCES));
            if (resourcesDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this, resourcesDirectoryCreationResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            DirectoryCreationResult backupDirectoryCreationResult = DataManager
                    .createDirectory(new File(projectDirectory, IRiinaConstants.PROJECT_DIR_BACKUP));
            if (backupDirectoryCreationResult.failed()) {
                JOptionPane.showMessageDialog(this, backupDirectoryCreationResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            //setup progress bar
            progressBar.setValue(0);
            progressLabel.setText(l.getKey(KEY_FRAME_NEW_PROJECT_PROG_COPY_MUSIC_FILE)
                    .replaceAll(AUDIO_FILE, musicFile.getAbsolutePath())
                    .replaceAll(TARGET_DIRECTORY, projectFolder.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            // COPY AUDIO FILE
            CopyFileResult musicFileCopyFileResult = DataManager.copyFile(musicFile.getName(),
                    musicFile.getAbsoluteFile().getParent(), projectFolder.getAbsolutePath());
            if (musicFileCopyFileResult.failed()) {
                JOptionPane.showMessageDialog(this, musicFileCopyFileResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            progressBar.setValue(20);
            progressLabel.setText(l.getKey(KEY_FRAME_NEW_PROJECT_PROG_STRIPPING_METADATA));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            // STRIP AUDIO FILE
            File copiedMusicFile = new File(projectFolder, musicFile.getName());
            AudioFile af;
            try {
                af = AudioFileIO.read(copiedMusicFile);
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e1) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_STRIPPING_METADATA_FAILED).replaceAll(EXCEPTION,
                                        e1.getMessage()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            VorbisCommentTag ovtag = (VorbisCommentTag) af.getTag();
            ovtag.deleteField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);
            ovtag.deleteField(VorbisCommentFieldKey.COVERART);
            ovtag.deleteField(VorbisCommentFieldKey.COVERARTMIME);
            try {
                af.commit();
            } catch (CannotWriteException e1) {
                JOptionPane
                        .showMessageDialog(this,
                                l.getKey(KEY_FRAME_NEW_PROJECT_ERR_STRIPPING_METADATA_FAILED).replaceAll(EXCEPTION,
                                        e1.getMessage()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            progressBar.setValue(40);
            progressLabel.setText(l.getKey(KEY_FRAME_NEW_PROJECT_PROG_LOAD_AUDIO).replaceAll(AUDIO_FILE,
                    musicFile.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            //LOAD AUDIO FILE
            Clip clip;
            ClipLoadResult clipLoadResult = DataManager.loadClip(copiedMusicFile.getAbsolutePath());
            if (clipLoadResult.failed()) {
                JOptionPane.showMessageDialog(this, clipLoadResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            clip = clipLoadResult.getClip();
            progressBar.setValue(60);
            progressLabel.setText(l.getKey(KEY_FRAME_NEW_PROJECT_PROG_COPY_ICON_FILE)
                    .replaceAll(ICON_FILE, iconFile.getAbsolutePath())
                    .replaceAll(TARGET_DIRECTORY, projectFolder.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            // COPY ICON FILE
            CopyFileResult iconFileCopyFileResult = DataManager.copyFile(iconFile.getName(),
                    iconFile.getAbsoluteFile().getParent(),
                    new File(projectFolder, IRiinaConstants.PROJECT_DIR_RESOURCES).getAbsolutePath());
            if (iconFileCopyFileResult.failed()) {
                JOptionPane.showMessageDialog(this, iconFileCopyFileResult.getLocalizedFailureInfo(l),
                        l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            MapData mapData = new MapData();
            mapData.setMusicTime(clip.getMicrosecondLength() / 1000000.0f);
            mapData.setId(projectName);
            mapData.setMusicFile(musicFile.getName());
            mapData.setIconFile(iconFile.getName());
            mapData.setIconImage(img);
            progressBar.setValue(80);
            progressLabel.setText(l.getKey(KEY_FRAME_NEW_PROJECT_PROG_WRITE_LEVEL_DATA).replaceAll(TARGET_DIRECTORY,
                    projectFolder.getAbsolutePath()));
            contentPane.paintImmediately(contentPane.getVisibleRect());
            // WRITE LEVEL DATA
            try {
                DataManager.exportMap(mapData, new File(projectFolder, "config.txt").getAbsolutePath());
            } catch (IOException e1) {
                JOptionPane
                        .showMessageDialog(
                                this, l.getKey(KEY_FRAME_NEW_PROJECT_ERR_WRITE_LEVEL_DATA_FAILED)
                                        .replaceAll(IO_EXCEPTION, e1.getMessage()),
                                l.getKey(KEY_UI_DIALOG_ERROR), JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
                progressLabel.setText(" ");
                contentPane.paintImmediately(contentPane.getVisibleRect());
                return;
            }
            ProjectMeta meta = new ProjectMeta(projectDirectory, false, null, mapData, clip);
            parent.setProjectMeta(meta);
            progressBar.setValue(100);
            progressLabel.setText(">>>");
            contentPane.paintImmediately(contentPane.getVisibleRect());
            parent.setView(View.EditorFrame);
        }
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
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
