package net.cyriaca.riina.misc.iriina.generic;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogs {

    public static File fileDialog(Frame parent, File initPath, boolean allFiles) {
        if (!IRiina.IS_MACOS) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(allFiles);
            chooser.setCurrentDirectory(initPath);
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        } else {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            FileDialog dialog = new FileDialog(parent);
            dialog.setMode(FileDialog.LOAD);
            dialog.setDirectory(initPath.getAbsolutePath());
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String file = dialog.getFile();
            if (directory != null && file != null) {
                return new File(directory, file);
            } else {
                return null;
            }
        }
    }

    public static File folderDialog(Frame parent, File initPath) {
        if (!IRiina.IS_MACOS) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setCurrentDirectory(initPath);
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        } else {
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            FileDialog dialog = new FileDialog(parent);
            dialog.setMode(FileDialog.LOAD);
            dialog.setDirectory(initPath.getAbsolutePath());
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String file = dialog.getFile();
            if (file != null) {
                return new File(directory, file);
            } else {
                return null;
            }
        }
    }

}
