package net.cyriaca.riina.misc.iriina.editor.data;

import net.cyriaca.riina.misc.iriina.intralism.data.MapData;

import javax.sound.sampled.Clip;
import java.io.File;

public class LoadTarget {

    private File projectDirectory;
    private File backupFile;
    private Clip clip;
    private MapData mapData;
    private boolean isBackupValue;

    public LoadTarget(File projectDirectory, File backupFile, Clip clip, MapData mapData, boolean isBackup) {
        this.projectDirectory = projectDirectory;
        this.backupFile = backupFile;
        this.clip = clip;
        this.mapData = mapData;
        this.isBackupValue = isBackup;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public File getBackupFile() {
        return backupFile;
    }

    public Clip getClip() {
        return clip;
    }

    public MapData getMapData() {
        return mapData;
    }

    public boolean isBackup() {
        return isBackupValue;
    }

}
