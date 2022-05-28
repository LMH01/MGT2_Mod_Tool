package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.TimeHelper;
import com.github.lmh01.mgt2mt.util.manager.ExportType;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractBaseContentManager implements BaseContentManager {
    private final String mainTranslationKey;
    private final String defaultContentFileName;
    private String[] defaultContent;
    private final String exportType;
    private final ArrayList<JMenuItem> modMenuItems;
    private final JMenuItem exportMenuItem;
    protected final File gameFile;
    private final Charset gameFileCharset;
    private ImportHelperMap currentImportHelperMap;
    private int maxId = 0;

    public AbstractBaseContentManager(String mainTranslationKey, String exportType, String defaultContentFileName, File gameFile, Charset gameFileCharset) {
        this.mainTranslationKey = mainTranslationKey;
        this.defaultContentFileName = defaultContentFileName;
        this.defaultContent = null;
        this.exportType = exportType;
        this.modMenuItems = getInitialModMenuItems();
        this.exportMenuItem = getInitialExportMenuItem();
        this.gameFile = gameFile;
        this.gameFileCharset = gameFileCharset;
    }

    protected final ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> addModMenuItemAction());
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(removeModItem);
        return menuItems;
    }

    protected final JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> exportMenuItemAction());
        return menuItem;
    }

    private void addModMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            openAddModGui();
        }, "runnableAdd" + getType().replaceAll("\\s+", ""));
    }

    private void removeModMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            OperationHelper.process(this::removeContents, getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false, this);
        }, "runnableRemove" + getType().replaceAll("\\s+", ""));
    }

    protected final void exportMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            OperationHelper.process(contents -> SharingManager.export(ExportType.ALL_SINGLE, (ArrayList<AbstractBaseContent>) contents), getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true, this);
        }, "runnableExport" + getType().replaceAll("\\s+", ""));
    }

    protected final String getMainTranslationKey() {
        return this.mainTranslationKey;
    }

    protected final void setMaxId(int id) {
        this.maxId = id;
    }

    /**
     * Initializes the default content
     */
    public final void initializeDefaultContent() {
        this.defaultContent = getDefaultContentFromFiles();
    }

    /**
     * @return The default content read from the default content files
     */
    protected String[] getDefaultContentFromFiles() {
        try {
            if (this instanceof AbstractSimpleContentManager) {
                return ReadDefaultContent.getDefault(defaultContentFileName, ((AbstractSimpleContentManager)this)::getReplacedLine);
            } else {
                return ReadDefaultContent.getDefault(defaultContentFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Shows a message that the mod has been added successfully.
     */
    protected void showModAddedMessage(String name) {
        JOptionPane.showMessageDialog(null, name + " \"" + getType() + "\" " + I18n.INSTANCE.get("commonText.hasSuccessfullyBeenAddedToTheGame"), "frame.title.success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * If the map key contains the missing dependency in its value it is replaced with the replacement.
     * If the replacement already exists in the map the missing dependency is removed and not replaced.
     * Value formatting: {@literal <DATA> (DATA = missingDependency}
     * Note: Also works if {@literal <> is missing}
     *
     * @param mapKey The map key for which the values should be replaced
     * @see DependentContentManager#replaceMissingDependency(Map, String, String) Parameters
     */
    protected final void replaceMapEntry(Map<String, Object> map, String missingDependency, String replacement, String mapKey) {
        if (map.containsKey(mapKey)) {
            if (map.get(mapKey).toString().contains(missingDependency) && !map.get(mapKey).toString().contains(replacement)) {
                map.replace(mapKey, map.get(mapKey).toString().replaceAll(missingDependency, replacement));
            } else {
                map.replace(mapKey, map.get(mapKey).toString().replaceAll(missingDependency, "").replaceAll("<>", ""));
            }
        }
    }

    @Override
    public void addContent(AbstractBaseContent content) throws ModProcessingException {
        ArrayList<AbstractBaseContent> contents = new ArrayList<>();
        contents.add(content);
        addContents(contents);
    }

    @Override
    public void addContents(List<AbstractBaseContent> contents) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper();
        try {
            analyzeFile();
            createBackup(false);
            // Get amount of contents that require images
            int contentsRequiringImages = 0;
            for (AbstractBaseContent content : contents) {
                if (content instanceof RequiresPictures) {
                    contentsRequiringImages += 1;
                }
                // Check if the id has been set already, if not the id will be set here
                if (content.id == null) {
                    content.id = getFreeId();
                }
            }
            if (contentsRequiringImages > 0) {
                ProgressBarHelper.initializeProgressBar(0, contentsRequiringImages, I18n.INSTANCE.get("progressBar.copyingImages"));
                for (AbstractBaseContent content : contents) {
                    if (content instanceof RequiresPictures) {
                        try {
                            TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.copyingImages"), content.name));
                            ((RequiresPictures) content).addPictures();
                        } catch (IOException | NullPointerException e) {
                            throw new ModProcessingException("Unable to add image files for " + content.name, e);
                        }
                    }
                    ProgressBarHelper.increment();
                }
                ProgressBarHelper.resetProgressBar();
            }
            editTextFiles(contents, ContentAction.ADD_MOD);
            TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.addedAllContents"), contents.size(), getType(), timeHelper.getMeasuredTimeDisplay()));
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Unable to add contents of type " + getType(), e);
        }
    }

    @Override
    public void removeContent(String name) throws ModProcessingException {
        ArrayList<AbstractBaseContent> contents = new ArrayList<>();
        contents.add(constructContentFromName(name));
        removeContents(contents);
    }

    @Override
    public void removeContents(List<AbstractBaseContent> contents) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper();
        try {
            analyzeFile();
            createBackup(false);
            // Get amount of contents that require images
            int contentsRequiringImages = 0;
            for (AbstractBaseContent content : contents) {
                if (content instanceof RequiresPictures) {
                    contentsRequiringImages += 1;
                }
            }
            if (contentsRequiringImages > 0) {
                ProgressBarHelper.initializeProgressBar(0, contentsRequiringImages, I18n.INSTANCE.get("progressBar.removingImages"));
                for (AbstractBaseContent content : contents) {
                    if (content instanceof RequiresPictures) {
                        try {
                            TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.deletingImages"), content.name));
                            ((RequiresPictures) content).removePictures();
                        } catch (IOException e) {
                            throw new ModProcessingException("Unable to remove image files for " + content.name, e);
                        }
                    }
                    ProgressBarHelper.increment();
                }
                ProgressBarHelper.resetProgressBar();
            }
            editTextFiles(contents, ContentAction.REMOVE_MOD);
            TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.removedAllContents"), contents.size(), getType(), timeHelper.getMeasuredTimeDisplay()));
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Unable to remove contents of type" + getType(), e);
        }
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        if (content instanceof AbstractSimpleContent && this instanceof AbstractSimpleContentManager) {
            ArrayList<AbstractBaseContent> simpleContents = new ArrayList<>();
            simpleContents.add(content);
            ((AbstractSimpleContentManager)this).editTextFiles(simpleContents, action);
        } else if (content instanceof AbstractAdvancedContent && this instanceof AbstractAdvancedContentManager) {
            ArrayList<AbstractBaseContent> advancedContents = new ArrayList<>();
            advancedContents.add(content);
            ((AbstractAdvancedContentManager)this).editTextFiles(advancedContents, action);
        } else {
            throw new ModProcessingException("Unable to edit game files: No implementation found! This happened because the input content does not implement SimpleContent or AdvancedContent.");
        }
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return modMenuItems;
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public void setMainMenuButtonAvailability() throws ModProcessingException {
        String[] customContentString = getCustomContentString();
        for (JMenuItem menuItem : getModMenuItems()) {
            if (menuItem.getText().replace("R", "r").replace("A", "a").contains(I18n.INSTANCE.get("commonText.remove"))) {
                if (customContentString.length > 0 || Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
                    menuItem.setEnabled(true);
                    menuItem.setToolTipText("");
                } else {
                    menuItem.setEnabled(false);
                    menuItem.setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
                }
            }
        }
        if (customContentString.length > 0 || Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
            getExportMenuItem().setEnabled(true);
            getExportMenuItem().setToolTipText("");
        } else {
            getExportMenuItem().setEnabled(false);
            getExportMenuItem().setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
        }
    }

    @Override
    public String getDefaultContentFileName() {
        return defaultContentFileName;
    }

    @Override
    public String[] getDefaultContent() {
        return defaultContent;
    }

    @Override
    public String[] getCustomContentString() throws ModProcessingException {
        String[] contentByAlphabet = getContentByAlphabet();
        ArrayList<String> arrayListCustomContent = new ArrayList<>();
        for (String s : contentByAlphabet) {
            boolean isDefaultContent = false;
            for (String contentName : getDefaultContent()) {
                if (s.equals(contentName)) {
                    isDefaultContent = true;
                    break;
                }
            }
            if (!isDefaultContent) {
                arrayListCustomContent.add(s);
            }
        }
        try {
            Collections.sort(arrayListCustomContent);
        } catch (NullPointerException ignored) {

        }
        String[] string = new String[arrayListCustomContent.size()];
        arrayListCustomContent.toArray(string);
        return string;
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey());
    }

    @Override
    public String getTypeUpperCase() {
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase.plural");
    }

    @Override
    public String getExportType() {
        return exportType;
    }

    @Override
    public void createBackup(boolean initialBackup) throws ModProcessingException {
        try {
            Backup.createBackup(gameFile, initialBackup, false);
        } catch (IOException e) {
            throw new ModProcessingException("Unable to create backup of file " + gameFile.getName(), e);
        }
    }

    @Override
    public File getGameFile() {
        return gameFile;
    }

    @Override
    public Charset getCharset() {
        return gameFileCharset;
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public int getFreeId() {
        maxId+=1;
        return maxId;
    }

    @Override
    public void initializeImportHelperMap() throws ModProcessingException {
        currentImportHelperMap = new ImportHelperMap(this);
    }

    @Override
    public void resetImportHelperMap() {
        currentImportHelperMap = null;
    }

    @Override
    public ImportHelperMap getImportHelperMap() throws ModProcessingException {
        if (currentImportHelperMap != null) {
            return currentImportHelperMap;
        } else {
            throw new ModProcessingException("Unable to return import helper map: Import helper map is not initialized!");
        }
    }

    @Override
    public String getExportImageName(String identifier, String name) {
        return getExportType().toLowerCase().replaceAll(" ", "_").replaceAll("/", "").replaceAll("[^0-9a-zA-Z]", "") + "_" + name.toLowerCase().replaceAll(" ", "_").replaceAll("/", "").replaceAll("[^0-9a-zA-Z]", "") + "_" + identifier;
    }
}
