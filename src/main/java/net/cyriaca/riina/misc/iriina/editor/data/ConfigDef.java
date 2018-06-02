package net.cyriaca.riina.misc.iriina.editor.data;

import java.io.File;
import java.util.Date;

public class ConfigDef {
    private Type type;
    private File projectDirectory;
    private File configFile;
    private long lastModified;

    public ConfigDef(Type type, File projectDirectory, File configFile) {
        if (projectDirectory == null)
            throw new NullPointerException("Project directory was null!");
        this.type = type;
        this.projectDirectory = projectDirectory;
        this.configFile = configFile;
        lastModified = configFile.lastModified();
    }

    public Type getType() {
        return type;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public File getConfigFile() {
        return configFile;
    }

    public String toString() {
        switch (type) {
            case CONFIG:
                return "Project (Last modified " + new Date(lastModified) + ")";
            case BACKUP:
                return "Backup - " + configFile.getName() + " (Last modified " + new Date(lastModified) + ")";
            default:
                return null;
        }
    }

    public enum Type {
        CONFIG, BACKUP
    }

}
