package net.cyriaca.riina.misc.iriina.beatmapper.data;

import net.cyriaca.riina.misc.iriina.intralism.data.MapData;

import javax.sound.sampled.Clip;
import java.io.File;

public class ProjectMeta {

    private File projectDirectory;
    private boolean isBackup;
    private File backupFile;
    private MapData mapData;
    private Clip clip;

    public ProjectMeta(File projectDirectory, boolean isBackup, File backupFile, MapData mapData, Clip clip) {
        this.projectDirectory = projectDirectory;
        this.isBackup = isBackup;
        this.backupFile = backupFile;
        this.mapData = mapData;
        this.clip = clip;
    }

    public MapData getLevelData() {
        return mapData;
    }

    public Clip getClip() {
        return clip;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public File getBackupFile() {
        return backupFile;
    }

    public boolean isBackup() {
        return isBackup;
    }

}
