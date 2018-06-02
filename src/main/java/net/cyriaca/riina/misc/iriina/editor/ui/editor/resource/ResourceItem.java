package net.cyriaca.riina.misc.iriina.editor.ui.editor.resource;

import net.cyriaca.riina.misc.iriina.editor.ui.editor.ResourceInfoDataPanel;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResourceItem extends JPanel implements ActionListener {

    private JLabel image;
    private JLabel name;
    private JLabel nameValue;
    private JLabel path;
    private JLabel pathValue;

    private JButton removeButton;
    private JButton modButton;

    private ResourceInfoDataPanel parent;

    private MapResource res = null;

    public ResourceItem(ResourceInfoDataPanel parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        image = new JLabel();

        add(image, BorderLayout.WEST);

        JPanel rightHold = new JPanel();
        BoxLayout bl = new BoxLayout(rightHold, BoxLayout.Y_AXIS);
        rightHold.setLayout(bl);

        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        Font textFont = new Font("SansSerif", Font.PLAIN, 12);
        name = new JLabel("Name");
        name.setFont(titleFont);
        rightHold.add(name);
        nameValue = new JLabel("NAME HERE");
        nameValue.setFont(textFont);
        rightHold.add(nameValue);
        path = new JLabel("Path");
        path.setFont(titleFont);
        rightHold.add(path);
        pathValue = new JLabel("PATH HERE");
        pathValue.setFont(textFont);
        rightHold.add(pathValue);

        add(rightHold, BorderLayout.CENTER);

        JPanel lowerHolder = new JPanel(new BorderLayout());
        modButton = new JButton("Modify");
        removeButton = new JButton("Remove");
        lowerHolder.add(modButton, BorderLayout.WEST);
        lowerHolder.add(removeButton, BorderLayout.EAST);

        add(lowerHolder, BorderLayout.SOUTH);

        modButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    public MapResource getResource() {
        return res;
    }

    public void setResource(MapResource res) {
        this.res = res;
        if (res == null)
            return;
        nameValue.setText(res.getName());
        pathValue.setText(res.getPath());

        Image targImage = res.getImage();
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(targImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (targImage.getWidth(null) == -1)
            return;
        int imageSideSize = 80;
        int w;
        float iw = targImage.getWidth(null);
        int h;
        float ih = targImage.getHeight(null);
        if (iw > ih) {
            w = imageSideSize;
            h = (int) Math.ceil(ih / iw * (float) imageSideSize);
        } else {
            h = imageSideSize;
            w = (int) Math.ceil(iw / ih * (float) imageSideSize);
        }
        ImageIcon imageIcon = new ImageIcon(targImage.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        image.setIcon(imageIcon);
        res.setSmallIcon(imageIcon);
    }

    public void reset() {
        this.res = null;
        nameValue.setText("Undefined");
        pathValue.setText("Undefined");
        image.setIcon(null);
    }

    public void setLocaleStrings(String nameStr, String pathStr, String modifyButtonStr, String deleteButtonStr) {
        name.setText(nameStr);
        path.setText(pathStr);
        modButton.setText(modifyButtonStr);
        removeButton.setText(deleteButtonStr);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeButton)
            parent.uiRemoveMapResource(res);
        if (e.getSource() == modButton)
            parent.uiModifyMapResource(res);
    }
}
