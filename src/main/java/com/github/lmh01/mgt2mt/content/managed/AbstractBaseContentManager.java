package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractBaseContentManager implements BaseContentManager {
    private final String mainTranslationKey;
    private final String defaultContentFileName;
    private final String[] defaultContent;
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
        this.defaultContent = getDefaultContentFromFiles();
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
            OperationHelper.process(this::removeContent, getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
        }, "runnableRemove" + getType().replaceAll("\\s+", ""));
    }

    protected final void exportMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            Path path;
            if (Settings.enableExportStorage) {
                path = ModManagerPaths.EXPORT.getPath().resolve("single/" + Utils.getCurrentDateTime());
            } else {
                path = ModManagerPaths.EXPORT.getPath().resolve("single");
            }
            throw new ModProcessingException("Not yet implemented! SharingManager.exportSingleMod has to be rewritten, or i have to find a better way to export a single mod.");
            //OperationHelper.process((string) -> SharingManager.exportSingleMod(this, string, path), getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
        }, "runnableExport" + getType().replaceAll("\\s+", ""));
    }

    protected final String getMainTranslationKey() {
        return this.mainTranslationKey;
    }

    protected final void setMaxId(int id) {
        this.maxId = id;
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
        try {
            analyzeFile();
            createBackup(false);
            // Check if the id has been set already, if not the id will be set here
            if (content.id == null) {
                content.id = getFreeId();
            }
            if (content instanceof RequiresPictures) {
                try {
                    ((RequiresPictures) content).addPictures();
                } catch (IOException | NullPointerException e) {
                    throw new ModProcessingException("Unable to add image files", e);
                }
            }
            editTextFiles(content, ContentAction.ADD_MOD);
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + getTypeUpperCase() + " - " + content.name);
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Unable to add " + content.name + " of type " + getType(), e);
        }
    }

    @Override
    public void removeContent(String name) throws ModProcessingException {
        try {
            analyzeFile();
            createBackup(false);
            AbstractBaseContent content = constructContentFromName(name);
            if (content instanceof RequiresPictures) {
                try {
                    ((RequiresPictures) content).removePictures();
                } catch (IOException e) {
                    throw new ModProcessingException("Unable to remove image files", e);
                }
            }
            editTextFiles(content, ContentAction.REMOVE_MOD);
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getTypeUpperCase() + " - " + content.name);
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Unable to remove " + name + " of type " + getType(), e);
        }
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        if (content instanceof SimpleContent && this instanceof AbstractSimpleContentManager) {
            SimpleContent sc = (SimpleContent) content;
            AbstractSimpleContentManager scm = (AbstractSimpleContentManager) this;
            try {
                Charset charset = getCharset();
                Path gameFilePath = gameFile.toPath();
                if (Files.exists(gameFilePath)) {
                    Files.delete(gameFilePath);
                }
                Files.createFile(gameFilePath);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFilePath.toFile()), charset));
                if (charset.equals(StandardCharsets.UTF_8)) {
                    bw.write("\ufeff");
                }
                boolean firstLine = true;
                for (int i = 0; i < scm.fileContent.size(); i++) {
                    if (action.equals(ContentAction.ADD_MOD)) {
                        if (!firstLine) {
                            bw.write("\r\n");
                        } else {
                            firstLine = false;
                        }
                        bw.write(scm.fileContent.get(i));
                    } else {
                        if (!scm.getReplacedLine(scm.fileContent.get(i)).equals(content.name)) {
                            if (!firstLine) {
                                bw.write("\r\n");
                            } else {
                                firstLine = false;
                            }
                            bw.write(scm.fileContent.get(i));
                        }
                    }
                }
                if (action.equals(ContentAction.ADD_MOD)) {
                    bw.write("\r\n");
                    bw.write(sc.getLine());
                }
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Unable to edit mod file for mod " + getType(), e);
            }
        } else if (content instanceof AbstractAdvancedContent && this instanceof AbstractAdvancedContentManager) {
            AbstractAdvancedContent ac = (AbstractAdvancedContent) content;
            AbstractAdvancedContentManager acm = (AbstractAdvancedContentManager) this;
            if (action.equals(ContentAction.ADD_MOD)) {
                try {
                    Charset charset = getCharset();
                    Path gameFilePath = gameFile.toPath();
                    if (Files.exists(gameFilePath)) {
                        Files.delete(gameFilePath);
                    }
                    Files.createFile(gameFilePath);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                    if (charset.equals(StandardCharsets.UTF_8)) {
                        bw.write("\ufeff");
                    }
                    for (Map<String, String> fileContent : acm.fileContent) {
                        acm.printValues(fileContent, bw);
                        bw.write("\r\n");
                    }
                    Map<String, String> map = ac.getMap();
                    acm.printValues(ac.getMap(), bw);
                    bw.write("\r\n");
                    bw.write("[EOF]");
                    bw.close();
                } catch (IOException e) {
                    throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
                }
            } else {
                try {
                    int contentId = content.id;
                    Charset charset = getCharset();
                    Path gameFilePath = gameFile.toPath();
                    if (Files.exists(gameFilePath)) {
                        Files.delete(gameFilePath);
                    }
                    Files.createFile(gameFilePath);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                    if (charset.equals(StandardCharsets.UTF_8)) {
                        bw.write("\ufeff");
                    }
                    for (Map<String, String> fileContent : acm.fileContent) {
                        if (Integer.parseInt(fileContent.get("ID")) != contentId) {
                            acm.printValues(fileContent, bw);
                            bw.write("\r\n");
                        }
                    }
                    bw.write("[EOF]");
                    bw.close();
                } catch (IOException e) {
                    throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
                }
            }
        } else {
            throw new ModProcessingException("Unable to edit game files: No implementation found! This is caused because content does not implement SimpleContent or AdvancedContent.");
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
                if (customContentString.length > 0) {
                    menuItem.setEnabled(true);
                    menuItem.setToolTipText("");
                } else {
                    menuItem.setEnabled(false);
                    menuItem.setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
                }
            }
        }
        if (customContentString.length > 0) {
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
        return getExportType().toLowerCase().replaceAll(" ", "_") + "_" + name.toLowerCase().replaceAll(" ", "_") + "_" + identifier;
    }
}
