package net.cyriaca.riina.misc.iriina.intralism.data;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * Represents an image used in a map
 */
public class MapResource {
    private String name;
    private String path;
    private Image image;
    private ImageIcon smallIcon;
    private MapData parent;

    public MapResource(String name, String path, Image image) {
        this.name = name == null ? "Undefined" : name;
        this.path = path == null ? "Undefined" : path;
        this.image = image == null ? new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB) : image;
        this.parent = null;
        this.smallIcon = null;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return path;
    }

    public Image getImage() {
        return image;
    }

    public MapData getParent() {
        return parent;
    }

    public void setParent(MapData parent) {
        this.parent = parent;
    }

    public ImageIcon getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(ImageIcon smallIcon) {
        this.smallIcon = smallIcon;
    }

    public MapResource clone() {
        return new MapResource(name, path, image);
    }
}
