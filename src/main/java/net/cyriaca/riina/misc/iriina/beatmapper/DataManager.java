package net.cyriaca.riina.misc.iriina.beatmapper;

import net.cyriaca.riina.misc.iriina.beatmapper.data.IRiinaPreferences;
import net.cyriaca.riina.misc.iriina.beatmapper.data.editor.EditorConfig;
import net.cyriaca.riina.misc.iriina.generic.localization.LocaleSystem;
import net.cyriaca.riina.misc.iriina.intralism.MapManager;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;
import net.cyriaca.riina.misc.iriina.intralism.data.MapParseResult;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DataManager {

    public static boolean isIntralismSupportedImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg");
    }

    public static MapParseResult parseMap(File inFile) throws FileNotFoundException {
        JsonReader reader = Json.createReader(new FileReader(inFile));
        JsonObject obj = reader.readObject();
        reader.close();
        return MapManager.parseMap(obj);
    }

    public static MapParseResult parseMap(String inFile) throws FileNotFoundException {
        return parseMap(new File(inFile));
    }

    public static void exportMap(MapData mapData, File outFile, boolean pure) throws IOException {
        JsonWriter writer = Json.createWriter(new FileWriter(outFile));
        JsonObject obj = MapManager.exportMap(mapData, pure);
        writer.write(obj);
        writer.close();
    }

    public static void exportMap(MapData mapData, String outFile, boolean pure) throws IOException {
        exportMap(mapData, new File(outFile), pure);
    }

    public static DirectoryCreationResult createDirectory(File path) {
        if (path.exists())
            return DirectoryCreationResult.createSuccessResult(path);
        else if (!path.mkdirs())
            return DirectoryCreationResult.createFailureResult(path);
        return DirectoryCreationResult.createSuccessResult(path);
    }

    public static IRiinaPreferences readPrefs() {
        Preferences prefs = Preferences.userRoot().node(IRiinaConstants.PREFERENCE_NODE);
        IRiinaPreferences out = new IRiinaPreferences();
        String locale = prefs.get("locale", LocaleSystem.DEFAULT_LOCALE_ID);
        out.setLocale(locale);
        String workspaceDirectory = prefs.get("workspaceDirectory", "");
        if (workspaceDirectory.length() == 0) {
            workspaceDirectory = new File(System.getProperty("user.home"),
                    IRiinaConstants.DEFAULT_WORKSPACE_DIRECTORY).getAbsolutePath();
            out.setWorkspaceDirectory(workspaceDirectory);
        } else {
            if (!out.setWorkspaceDirectory(workspaceDirectory)) {
                workspaceDirectory = new File(System.getProperty("user.home"),
                        IRiinaConstants.DEFAULT_WORKSPACE_DIRECTORY).getAbsolutePath();
                if (!out.setWorkspaceDirectory(workspaceDirectory)) {
                    System.err.println("Failed to create / use directory " + workspaceDirectory + " as workspace directory!");
                    System.exit(102);
                }
            }
        }
        out.setShowPreWelcomeWindow(prefs.getBoolean("showPreWelcomeWindow", true));
        out.setEditorConfig(EditorConfig.constructInstanceFromPreferencesNode(prefs.node(EditorConfig.PREF_SUBNODE)));
        return out;
    }

    public static void writePrefs(IRiinaPreferences in) {
        Preferences prefs = Preferences.userRoot().node(IRiinaConstants.PREFERENCE_NODE);
        String locale = in.getLocale();
        String workspaceDirectory = in.getWorkspaceDirectory();
        boolean showPreWelcomeWindow = in.getShowPreWelcomeWindow();
        prefs.put("locale", locale);
        prefs.put("workspaceDirectory", workspaceDirectory);
        prefs.putBoolean("showPreWelcomeWindow", showPreWelcomeWindow);
        EditorConfig.setPreferencesDataFromInstance(prefs.node(EditorConfig.PREF_SUBNODE), in.getEditorConfig());
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private static AudioInputStream getDecodedAudioStream(File musicFile)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream din;
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
        AudioFormat baseFormat = audioIn.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        din = AudioSystem.getAudioInputStream(decodedFormat, audioIn);
        return din;
    }

    public static Image getImage(File image) {
        return getImage(image.getAbsolutePath());
    }

    public static Image getImage(String image) {
        return Toolkit.getDefaultToolkit().createImage(image);
    }

    public static CopyFileResult copyFile(File file, File inDirectory, File outDirectory) {
        return copyFile(file.getName(), inDirectory.getAbsolutePath(), outDirectory.getAbsolutePath());
    }

    public static CopyFileResult copyFile(File file, File outDirectory) {
        return copyFile(file.getName(), file.getParent(), outDirectory.getAbsolutePath());
    }

    public static CopyFileResult copyFile(String file, String inDirectory, String outDirectory) {
        Path inPath = FileSystems.getDefault().getPath(inDirectory, file);
        Path outPath = FileSystems.getDefault().getPath(outDirectory, file);
        try {
            Files.copy(inPath, outPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return CopyFileResult.createFailureResult(inPath.toFile(), outPath.toFile(), e);
        }
        return CopyFileResult.createSuccessResult(inPath.toFile(), outPath.toFile());
    }

    public static ClipLoadResult loadClip(File musicFile) {
        Clip out = null;
        AudioInputStream din;
        try {
            din = getDecodedAudioStream(musicFile);
        } catch (UnsupportedAudioFileException e1) {
            return ClipLoadResult.createFailureResult(musicFile, ClipLoadResult.FailureSource.UNSUPPORTED_AUDIO_FILE, e1);
        } catch (IOException e1) {
            return ClipLoadResult.createFailureResult(musicFile, ClipLoadResult.FailureSource.IO, e1);
        }
        try {
            out = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1144);
        }
        try {
            out.open(din);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
            System.exit(1145);
        }
        return ClipLoadResult.createSuccessResult(musicFile, out);
    }

    public static ClipLoadResult loadClip(String musicFile) {
        return loadClip(new File(musicFile));
    }

    public static void closeClip(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
