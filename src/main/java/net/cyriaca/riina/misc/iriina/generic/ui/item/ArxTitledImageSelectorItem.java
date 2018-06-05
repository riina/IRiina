package net.cyriaca.riina.misc.iriina.generic.ui.item;

import net.cyriaca.riina.misc.iriina.beatmapper.DataManager;
import net.cyriaca.riina.misc.iriina.generic.FileDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ArxTitledImageSelectorItem extends ArxTitledGenericItem implements ActionListener {

    private JLabel leftLabel;
    private JPanel rightPanel;
    private JTextField field;
    private JPanel buttonHolder;
    private FlowLayout buttonHolderLayout;
    private JButton button;

    private JLabel labelTitle;

    private int imageSideSize = 80;

    private Image image;

    private JPanel holder;

    public ArxTitledImageSelectorItem() {
        super();
        holder = new JPanel();
        holder.setLayout(new BorderLayout());
        leftLabel = new JLabel();
        holder.add(leftLabel, BorderLayout.WEST);

        rightPanel = new JPanel();
        BoxLayout rpBl = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(rpBl);

        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        labelTitle = new JLabel("Image path");
        labelTitle.setFont(titleFont);
        JPanel titleHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleHolder.add(labelTitle);
        rightPanel.add(titleHolder);

        field = new JTextField(null, 0);
        field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getPreferredSize().height));
        rightPanel.add(field);

        buttonHolderLayout = new FlowLayout(FlowLayout.LEFT);
        buttonHolder = new JPanel(buttonHolderLayout);
        button = new JButton();
        buttonHolder.add(button);
        rightPanel.add(buttonHolder);

        holder.add(rightPanel, BorderLayout.CENTER);

        image = new BufferedImage(imageSideSize, imageSideSize, BufferedImage.TYPE_INT_ARGB);

        if (imageSideSize != -1 && imageSideSize != 0) {
            int w;
            float iw = image.getWidth(null);
            int h;
            float ih = image.getHeight(null);
            if (iw > ih) {
                w = imageSideSize;
                h = (int) Math.ceil(ih / iw * (float) imageSideSize);
            } else {
                h = imageSideSize;
                w = (int) Math.ceil(iw / ih * (float) imageSideSize);
            }
            ImageIcon imageIcon = new ImageIcon(this.image.getScaledInstance(w, h, Image.SCALE_SMOOTH));
            leftLabel.setIcon(imageIcon);
        }

        button.addActionListener(this);
        add(holder);
    }

    public String getLabelTitle() {
        return labelTitle.getText();
    }

    public void setLabelTitle(String title) {
        labelTitle.setText(title);
    }

    public String getFile() {
        return field.getText();
    }

    public void setFile(String file, Image image) {
        if (file == null) {
            this.image = new BufferedImage(imageSideSize, imageSideSize, BufferedImage.TYPE_INT_ARGB);
            field.setText("");
        } else {
            field.setText(file);
            if (image == null)
                this.image = DataManager.getImage(file);
            else
                this.image = image;
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(this.image, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.image.getWidth(null) == -1)
            return;
        if (imageSideSize != -1 && imageSideSize != 0) {
            int w;
            float iw = this.image.getWidth(null);
            int h;
            float ih = this.image.getHeight(null);
            if (iw > ih) {
                w = imageSideSize;
                h = (int) Math.ceil(ih / iw * (float) imageSideSize);
            } else {
                h = imageSideSize;
                w = (int) Math.ceil(iw / ih * (float) imageSideSize);
            }
            ImageIcon imageIcon = new ImageIcon(this.image.getScaledInstance(w, h, Image.SCALE_SMOOTH));
            leftLabel.setIcon(imageIcon);
        }
    }

    public int getImageSideSize() {
        return imageSideSize;
    }

    public void setImageSideSize(int imageSideSize) {
        this.imageSideSize = imageSideSize;
    }

    public void setFieldCols(int cols) {
        field.setColumns(cols);
    }

    public String getButtonText() {
        return button.getText();
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            File f = FileDialogs.fileDialog(null, new File(field.getText()), true);
            if (f != null)
                setFile(f.getAbsolutePath(), null);
        }
    }

    public void setModEnabled(boolean value) {
        internalSetModEnabled(value);
        field.setEditable(value);
        button.setEnabled(value);
    }
}
