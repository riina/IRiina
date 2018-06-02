package net.cyriaca.riina.misc.iriina.editor.ui.editor;

import net.cyriaca.riina.misc.iriina.editor.CopyFileResult;
import net.cyriaca.riina.misc.iriina.editor.DataManager;
import net.cyriaca.riina.misc.iriina.editor.ui.EditorFrame;
import net.cyriaca.riina.misc.iriina.editor.ui.editor.resource.ResourceItem;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledImageSelectorItem;
import net.cyriaca.riina.misc.iriina.generic.ui.item.ArxTitledStringItem;
import net.cyriaca.riina.misc.iriina.intralism.data.MapResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceInfoDataPanel extends JPanel implements ActionListener {

    private static final int FONT_SIZE = 18;
    private static final String KEY_UI_DIALOG_ERROR = "ui_dialog_error";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_NAME_EXISTS = "panel_resource_info_data_name_exists";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_PATH_EXISTS = "panel_resource_info_data_path_exists";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_EMPTY_NAME = "panel_resource_info_data_empty_name";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_EMPTY_PATH = "panel_resource_info_data_empty_path";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_NONEXISTENT_FILE = "panel_resource_info_data_nonexistent_file";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_UNSUPPORTED_FILE = "panel_resource_info_data_unsupported_file";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_CREATE_RES_TITLE = "panel_resource_info_data_create_res_title";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_CREATE_RES_MESSAGE = "panel_resource_info_data_create_res_message";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_MODIFY_RES_TITLE = "panel_resource_info_data_modify_res_title";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_MODIFY_RES_MESSAGE = "panel_resource_info_data_modify_res_message";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_DELETE_RES_TITLE = "panel_resource_info_data_delete_res_title";
    private static final String KEY_PANEL_RESOURCE_INFO_DATA_DELETE_RES_MESSAGE = "panel_resource_info_data_delete_res_message";
    private static final String KEY_RESOURCE_ITEM_PROP_NAME_OLD = "resource_item_prop_name_old";
    private static final String KEY_RESOURCE_ITEM_PROP_PATH_OLD = "resource_item_prop_path_old";
    private static final String KEY_RESOURCE_ITEM_PROP_NAME_NEW = "resource_item_prop_name_new";
    private static final String KEY_RESOURCE_ITEM_PROP_PATH_NEW = "resource_item_prop_path_new";
    private static final String KEY_RESOURCE_ITEM_PROP_NAME = "resource_item_prop_name";
    private static final String KEY_RESOURCE_ITEM_PROP_PATH = "resource_item_prop_path";
    private static final String KEY_RESOURCE_ITEM_UI_LABEL_TITLE = "resource_item_ui_label_title";
    private static final String KEY_RESOURCE_ITEM_UI_BUTTON = "resource_item_ui_button";
    private static final String KEY_RESOURCE_ITEM_BUTTON_MODIFY = "resource_item_button_modify";
    private static final String KEY_RESOUREC_ITEM_BUTTON_DELETE = "resource_item_button_delete";
    private static final String KEY_RESOURCE_ITEM_BUTTON_CREATE = "resource_item_button_create";
    private EditorFrame parent;
    private JLabel label;
    private JButton addButton;
    private List<MapResource> mapResources;
    private List<ResourceItem> activeItems;
    private List<ResourceItem> pooledItems;
    private JScrollPane scrollPane;
    private JPanel holder;
    private String locErrTitle = "Error";
    private String locErrNameExistsMessage = "Resource with that name already exists!";
    private String locErrPathExistsMessage = "Resource with that path already exists!";
    private String locErrEmptyNameMessage = "No name specified!";
    private String locErrEmptyPathMessage = "No path specified!";
    private String locErrNonexistentFileMessage = "Invalid path specified!";
    private String locErrUnsupportedFileMessage = "Unsupported file!";
    private String locNewResourceTitle = "Create New Resource";
    private String locNewResourceMessage = "Define resource path and name";
    private String locModResourceTitle = "Modify Resource";
    private String locModResourceMessage = "Define new resource properties";
    private String locDelResourceTitle = "Delete Resource";
    private String locDelResourceMessage = "Delete resource?";
    private String resItemPropNameOld = "Current name";
    private String resItemPropPathOld = "Current path";
    private String resItemPropNameNew = "New name";
    private String resItemPropPathNew = "New path";
    private String resItemPropName = "Name";
    private String resItemPropPath = "Path";
    private String resItemLabelTitle = "Resource path";
    private String resItemButton = "Select Image";
    private String resItemButtonModify = "Modify";
    private String resItemButtonDelete = "Delete";
    private String resItemButtonCreate = "Create";

    public ResourceInfoDataPanel(EditorFrame parent) {
        this.parent = parent;
        BorderLayout bl = new BorderLayout();
        setLayout(bl);
        label = new JLabel("Resource Editor");
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));

        activeItems = new ArrayList<>();
        pooledItems = new ArrayList<>();

        mapResources = new ArrayList<>();

        add(label, BorderLayout.NORTH);

        addButton = new JButton("Add Resource");

        addButton.addActionListener(this);

        holder = new JPanel();
        BoxLayout hBl = new BoxLayout(holder, BoxLayout.Y_AXIS);
        holder.setLayout(hBl);

        JPanel minHolder = new JPanel(new BorderLayout());
        minHolder.add(holder, BorderLayout.NORTH);
        scrollPane = new JScrollPane(minHolder);
        add(scrollPane, BorderLayout.CENTER);

        add(addButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(240, 800));
    }

    public List<MapResource> getMapResources() {
        return Collections.unmodifiableList(mapResources);
    }

    public void setMapResources(List<MapResource> mapResources) {
        this.mapResources.clear();
        holder.removeAll();
        for (ResourceItem item : activeItems)
            item.reset();
        pooledItems.addAll(activeItems);
        activeItems.clear();
        if (mapResources != null)
            for (MapResource res : mapResources)
                addMapResource(res);
    }

    public void uiModifyMapResource(MapResource res) {
        if (res == null)
            return;
        JLabel inputTitle = new JLabel(locModResourceMessage);
        ArxTitledStringItem inputOldName = new ArxTitledStringItem();
        inputOldName.setValue(res.getName());
        inputOldName.setTitle(resItemPropNameOld);
        inputOldName.setModEnabled(false);
        ArxTitledImageSelectorItem inputOldPath = new ArxTitledImageSelectorItem();
        inputOldPath.setFile(Paths.get(parent.getResPath().getAbsolutePath(), res.getPath()).toString(), res.getImage());
        inputOldPath.setTitle(resItemPropPathOld);
        inputOldPath.setLabelTitle(resItemLabelTitle);
        inputOldPath.setButtonText(resItemButton);
        inputOldPath.setModEnabled(false);
        ArxTitledStringItem inputName = new ArxTitledStringItem();
        inputName.setValue(res.getName());
        inputName.setTitle(resItemPropNameNew);
        ArxTitledImageSelectorItem inputPath = new ArxTitledImageSelectorItem();
        inputPath.setFile(Paths.get(parent.getResPath().getAbsolutePath(), res.getPath()).toString(), res.getImage());
        inputPath.setTitle(resItemPropPathNew);
        inputPath.setLabelTitle(resItemLabelTitle);
        inputPath.setButtonText(resItemButton);
        JComponent[] inputs = new JComponent[]{
                inputTitle,
                inputOldPath,
                inputOldName,
                inputPath,
                inputName
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, locModResourceTitle, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nameStr = inputName.getValue();
            if (nameStr == null || nameStr.length() == 0) {
                showError(locErrTitle, locErrEmptyNameMessage);
                return;
            }
            File path;
            String pathStr = inputPath.getFile();
            if (pathStr == null || pathStr.length() == 0) {
                showError(locErrTitle, locErrEmptyPathMessage);
                return;
            }

            path = new File(pathStr);
            if (!path.exists() || !path.isFile()) {
                showError(locErrTitle, locErrNonexistentFileMessage);
                return;
            }
            if (!DataManager.isIntralismSupportedImage(path)) {
                showError(locErrTitle, locErrUnsupportedFileMessage);
                return;
            }

            if (!res.getName().equals(nameStr)) {
                for (MapResource r : mapResources)
                    if (nameStr.equals(r.getName())) {
                        showError(locErrTitle, locErrNameExistsMessage);
                        return;
                    }
            }
            Image img;
            if (!res.getPath().equals(path.getName())) {
                for (MapResource r : mapResources)
                    if (path.getName().equals(r.getPath())) {
                        showError(locErrTitle, locErrPathExistsMessage);
                        return;
                    }
                CopyFileResult opResult = DataManager.copyFile(path, parent.getResPath());
                if (opResult.failed()) {
                    showError(locErrTitle, opResult.getLocalizedFailureInfo(parent.getIRiina().getLocale()));
                    return;
                }
                img = DataManager.getImage(path);
            } else
                img = res.getImage();
            MapResource newRes = new MapResource(nameStr, path.getName(), img);
            replaceMapResource(res, newRes);
            parent.addOperationForResourceMod(res, newRes);

        }
    }

    private void showError(String title, String err) {
        JOptionPane.showMessageDialog(this,
                err,
                title, JOptionPane.ERROR_MESSAGE);
    }

    public void uiCreateMapResource() {
        JLabel inputTitle = new JLabel(locNewResourceMessage);
        ArxTitledStringItem inputName = new ArxTitledStringItem();
        inputName.setTitle(resItemPropName);
        ArxTitledImageSelectorItem inputPath = new ArxTitledImageSelectorItem();
        inputPath.setTitle(resItemPropPath);
        inputPath.setLabelTitle(resItemLabelTitle);
        inputPath.setButtonText(resItemButton);
        JComponent[] inputs = new JComponent[]{
                inputTitle,
                inputPath,
                inputName
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, locNewResourceTitle, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nameStr = inputName.getValue();
            if (nameStr == null || nameStr.length() == 0) {
                showError(locErrTitle, locErrEmptyNameMessage);
                return;
            }
            File path;
            String pathStr = inputPath.getFile();
            if (pathStr == null || pathStr.length() == 0) {
                showError(locErrTitle, locErrEmptyPathMessage);
                return;
            }
            path = new File(pathStr);
            if (!path.exists() || !path.isFile()) {
                showError(locErrTitle, locErrNonexistentFileMessage);
                return;
            }
            if (!DataManager.isIntralismSupportedImage(path)) {
                showError(locErrTitle, locErrUnsupportedFileMessage);
                return;
            }
            for (MapResource r : mapResources)
                if (path.getName().equals(r.getPath())) {
                    showError(locErrTitle, locErrPathExistsMessage);
                    return;
                }
            for (MapResource r : mapResources)
                if (nameStr.equals(r.getName())) {
                    showError(locErrTitle, locErrNameExistsMessage);
                    return;
                }
            CopyFileResult opResult = DataManager.copyFile(path, parent.getResPath());
            if (opResult.failed()) {
                showError(locErrTitle, opResult.getLocalizedFailureInfo(parent.getIRiina().getLocale()));
                return;
            }
            Image img = DataManager.getImage(path);
            MapResource newRes = new MapResource(nameStr, path.getName(), img);
            addMapResource(newRes);
            parent.addOperationForResourceAdd(newRes);
        }
    }

    public void uiRemoveMapResource(MapResource res) {
        JLabel inputTitle = new JLabel(locDelResourceMessage);
        ArxTitledStringItem inputName = new ArxTitledStringItem();
        inputName.setValue(res.getName());
        inputName.setTitle(resItemPropName);
        inputName.setModEnabled(false);
        ArxTitledImageSelectorItem inputPath = new ArxTitledImageSelectorItem();
        inputPath.setFile(Paths.get(parent.getResPath().getAbsolutePath(), res.getPath()).toString(), res.getImage());
        inputPath.setLabelTitle(resItemLabelTitle);
        inputPath.setTitle(resItemPropPath);
        inputPath.setButtonText(resItemButton);
        inputPath.setModEnabled(false);
        JComponent[] inputs = new JComponent[]{
                inputTitle,
                inputPath,
                inputName
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, locDelResourceTitle, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            removeMapResource(res);
            parent.addOperationForResourceRemove(res);
        }
    }

    public void replaceMapResource(MapResource oldRes, MapResource newRes) {
        int i = mapResources.indexOf(oldRes);
        if (i != -1) {
            ResourceItem item = activeItems.get(i);
            item.setResource(newRes);
            mapResources.set(i, newRes);
        }
        parent.updateResourceList(mapResources);
    }

    public void addMapResource(MapResource res) {
        if (mapResources.indexOf(res) == -1) {
            ResourceItem item;
            if (pooledItems.size() == 0) {
                item = new ResourceItem(this);
            } else {
                item = pooledItems.remove(0);
            }
            item.setLocaleStrings(resItemPropName, resItemPropPath, resItemButtonModify, resItemButtonDelete);
            mapResources.add(res);
            activeItems.add(item);
            holder.add(item);
            item.setResource(res);
        }
        parent.updateResourceList(mapResources);
    }

    public void removeMapResource(MapResource res) {
        int i = mapResources.indexOf(res);
        if (i != -1) {
            mapResources.remove(i);
            ResourceItem targ = activeItems.get(i);
            holder.remove(targ);
            targ.reset();
            activeItems.remove(targ);
            pooledItems.add(targ);
        }
        parent.updateResourceList(mapResources);
    }

    public void localize(Locale l) {
        locErrTitle = l.getKey(KEY_UI_DIALOG_ERROR);
        locErrNameExistsMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_NAME_EXISTS);
        locErrPathExistsMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_PATH_EXISTS);
        locErrEmptyNameMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_EMPTY_NAME);
        locErrEmptyPathMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_EMPTY_PATH);
        locErrNonexistentFileMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_NONEXISTENT_FILE);
        locErrUnsupportedFileMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_UNSUPPORTED_FILE);

        locNewResourceTitle = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_CREATE_RES_TITLE);
        locNewResourceMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_CREATE_RES_MESSAGE);

        locModResourceTitle = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_MODIFY_RES_TITLE);
        locModResourceMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_MODIFY_RES_MESSAGE);

        locDelResourceTitle = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_DELETE_RES_TITLE);
        locDelResourceMessage = l.getKey(KEY_PANEL_RESOURCE_INFO_DATA_DELETE_RES_MESSAGE);

        resItemPropNameOld = l.getKey(KEY_RESOURCE_ITEM_PROP_NAME_OLD);
        resItemPropPathOld = l.getKey(KEY_RESOURCE_ITEM_PROP_PATH_OLD);
        resItemPropNameNew = l.getKey(KEY_RESOURCE_ITEM_PROP_NAME_NEW);
        resItemPropPathNew = l.getKey(KEY_RESOURCE_ITEM_PROP_PATH_NEW);
        resItemPropName = l.getKey(KEY_RESOURCE_ITEM_PROP_NAME);
        resItemPropPath = l.getKey(KEY_RESOURCE_ITEM_PROP_PATH);
        resItemButton = l.getKey(KEY_RESOURCE_ITEM_UI_BUTTON);
        resItemLabelTitle = l.getKey(KEY_RESOURCE_ITEM_UI_LABEL_TITLE);
        resItemButtonModify = l.getKey(KEY_RESOURCE_ITEM_BUTTON_MODIFY);
        resItemButtonDelete = l.getKey(KEY_RESOUREC_ITEM_BUTTON_DELETE);
        resItemButtonCreate = l.getKey(KEY_RESOURCE_ITEM_BUTTON_CREATE);

        addButton.setText(l.getKey(resItemButtonCreate));

        for (ResourceItem res : activeItems)
            res.setLocaleStrings(resItemPropName, resItemPropPath, resItemButtonModify, resItemButtonDelete);
        for (ResourceItem res : pooledItems)
            res.setLocaleStrings(resItemPropName, resItemPropPath, resItemButtonModify, resItemButtonDelete);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton)
            uiCreateMapResource();
    }
}
