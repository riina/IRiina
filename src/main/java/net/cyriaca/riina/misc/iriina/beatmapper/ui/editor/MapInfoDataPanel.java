package net.cyriaca.riina.misc.iriina.beatmapper.ui.editor;

import net.cyriaca.riina.misc.iriina.beatmapper.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.generic.FloatBounds;
import net.cyriaca.riina.misc.iriina.generic.IntBounds;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.*;
import net.cyriaca.riina.misc.iriina.intralism.data.MapData;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapInfoDataPanel extends JPanel {

    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_TITLE = "frame_editor_component_map_info_title";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_NAME = "frame_editor_component_map_info_name";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_DESCRIPTION = "frame_editor_component_map_info_description";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_ICON = "frame_editor_component_map_info_icon";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_SELECT_ICON = "frame_editor_component_map_info_select_icon";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_SPEED = "frame_editor_component_map_info_speed";
    private static final String KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_ENVIRONMENT = "frame_editor_component_map_info_environment";

    private static final int FONT_SIZE = 18;

    private static final int TEXT_COLS = 10;
    private static final int IMAGE_SIDE_SIZE = 100;

    private JLabel label;

    private ArxTitledStringItem nameField;
    private ArxTitledStringAreaItem descriptionArea;
    private ArxTitledImageSelectorItem iconSelectorItem;
    private ArxTitledBooleanChecklistItem tagListItem;
    private ArxTitledIntItem lifeCountItem;
    private ArxTitledFloatItem speedModifierItem;
    private ArxTitledIntItem envTypeItem;

    public MapInfoDataPanel(EditorFrame parent) {
        BorderLayout bl = new BorderLayout();
        setLayout(bl);
        label = new JLabel("Map Meta Editor");
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
        add(label, BorderLayout.NORTH);

        JPanel c = new JPanel();
        BoxLayout cBl = new BoxLayout(c, BoxLayout.Y_AXIS);
        c.setLayout(cBl);

        nameField = new ArxTitledStringItem();
        nameField.setTitle("Name");
        nameField.setFieldCols(TEXT_COLS);
        c.add(nameField);

        descriptionArea = new ArxTitledStringAreaItem();
        descriptionArea.setTitle("Description");
        descriptionArea.setAreaCols(TEXT_COLS);
        c.add(descriptionArea);

        iconSelectorItem = new ArxTitledImageSelectorItem();
        iconSelectorItem.setTitle("Icon");
        iconSelectorItem.setButtonText("Select icon");
        iconSelectorItem.setImageSideSize(IMAGE_SIDE_SIZE);
        iconSelectorItem.setFieldCols(TEXT_COLS);
        c.add(iconSelectorItem);

        tagListItem = new ArxTitledBooleanChecklistItem();
        tagListItem.setTitle("Tags");
        tagListItem.setBoxes(MapData.getValidTags().toArray());
        c.add(tagListItem);

        lifeCountItem = new ArxTitledIntItem(new IntBounds(MapData.LIVES_MIN, false, MapData.LIVES_MAX, false), MapData.LIVES_MIN);
        lifeCountItem.setTitle("Lives");
        c.add(lifeCountItem);

        speedModifierItem = new ArxTitledFloatItem(new FloatBounds(MapData.SPEED_MODIFIER_MIN, false, MapData.SPEED_MODIFIER_MAX, false), MapData.SPEED_MODIFIER_MIN);
        speedModifierItem.setTitle("Speed Modifier");
        c.add(speedModifierItem);

        envTypeItem = new ArxTitledIntItem(new IntBounds(MapData.ENV_MIN, false, MapData.ENV_MAX, false), MapData.ENV_MIN);
        envTypeItem.setTitle("Environment ID");
        c.add(envTypeItem);

        JScrollPane children = new JScrollPane(c);

        add(children, BorderLayout.CENTER);


        setPreferredSize(new Dimension(240, 800));
    }

    public String getName() {
        return nameField.getValue();
    }

    public void setName(String name) {
        nameField.setValue(name);
    }

    public String getDescription() {
        return descriptionArea.getValue();
    }

    public void setDescription(String description) {
        descriptionArea.setValue(description);
    }

    public void setIconFile(File iconFile, Image icon) {
        iconSelectorItem.setFile(iconFile.getAbsolutePath(), icon);
    }

    public File getIconFile() {
        return new File(iconSelectorItem.getFile());
    }

    public List<String> getTags() {
        List<String> allowedTags = MapData.getValidTags();
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < tagListItem.getLength(); i++)
            if (tagListItem.getSelected(i))
                tags.add(allowedTags.get(i));
        return tags;
    }

    public void setTags(List<String> tags) {
        tagListItem.setSelectedAll(false);
        List<String> allowedTags = MapData.getValidTags();
        for (String tag : tags) {
            int i = allowedTags.indexOf(tag);
            if (i != -1)
                tagListItem.setSelected(i, true);
        }
    }

    public int getLives() {
        return lifeCountItem.getValue();
    }

    public void setLives(int lives) {
        lifeCountItem.setValue(lives);
    }

    public float getSpeedModifier() {
        return speedModifierItem.getValue();
    }

    public void setSpeedModifier(float speedModifier) {
        speedModifierItem.setValue(speedModifier);
    }

    public int getEnvType() {
        return envTypeItem.getValue();
    }

    public void setEnvType(int envType) {
        envTypeItem.setValue(envType);
    }

    public void localizeStrings(Locale l) {
        label.setText(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_TITLE));
        nameField.setTitle(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_NAME));
        descriptionArea.setTitle(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_DESCRIPTION));
        iconSelectorItem.setTitle(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_ICON));
        iconSelectorItem.setButtonText(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_SELECT_ICON));
        speedModifierItem.setTitle(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_SPEED));
        envTypeItem.setTitle(l.getKey(KEY_FRAME_EDITOR_COMPONENT_MAP_INFO_ENVIRONMENT));
    }

}
