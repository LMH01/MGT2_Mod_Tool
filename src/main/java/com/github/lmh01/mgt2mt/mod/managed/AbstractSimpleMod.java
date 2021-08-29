package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

//TODO Klasse aufräumen -> Funktionen schön sortieren

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files where each mod is written in one line.
 */
public abstract class AbstractSimpleMod extends AbstractBaseMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleMod.class);


    Map<Integer, String> fileContent;

    @Override
    public void analyzeFile() throws ModProcessingException {
        try {
            fileContent = DataStreamHelper.getContentFromFile(getGameFile(), getModFileCharset());
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze mod-files for mod " + getType() + ": " + e.getMessage());
        }
    }

    @Override
    public <T> void addMod(T t) throws ModProcessingException {
        if (t instanceof String) {
            String string = (String) t;
            editFile(true, string);
        } else {
            throw new ModProcessingException("T is invalid: Should be String", true);
        }
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {
        editFile(false, name);
    }

    /**
     * Exports the mod
     * @param name The name for the mod that should be exported
     * @param exportAsRestorePoint True when the mod should be exported as restore point. False otherwise
     * @return Returns true when the mod has been exported successfully. Returns false when the mod has already been exported.
     */
    @Override
    public boolean exportMod(String name, boolean exportAsRestorePoint) throws ModProcessingException {
        try {
            analyzeFile();
            String string = getLine(name);
            Path exportFolder;
            if(exportAsRestorePoint){
                exportFolder = ModManagerPaths.CURRENT_RESTORE_POINT.getPath();
            }else{
                exportFolder = ModManagerPaths.EXPORT.getPath();
            }
            final Path EXPORTED_MOD_MAIN_FOLDER_PATH = exportFolder.resolve(getExportFolder() + "/" + name.replaceAll("[^a-zA-Z0-9]", ""));
            LOGGER.info("ExportFolder path: " + exportFolder);
            LOGGER.info("Exported mod main folder path: " + EXPORTED_MOD_MAIN_FOLDER_PATH);
            File fileExportedMod = EXPORTED_MOD_MAIN_FOLDER_PATH.resolve(getImportExportFileName()).toFile();
            if(fileExportedMod.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + getMainTranslationKey() + " - " + name + ": " + I18n.INSTANCE.get("sharer.modAlreadyExported"));
                return false;
            }else{
                Files.createDirectories(EXPORTED_MOD_MAIN_FOLDER_PATH);
            }
            fileExportedMod.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedMod), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + "\r\n");
            bw.write("[" + getTypeCaps() + " START]" + "\r\n");
            bw.write("[LINE]" + getModifiedExportLine(string) + "\r\n");
            bw.write("[" + getTypeCaps() + " END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exported") + " " + getMainTranslationKey() + " - " + name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + getMainTranslationKey() + " - " + name + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Imports the mod.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the mod has been imported successfully. Returns "false" when the mod already exists. Returns mod tool version of import mod when mod is not compatible with current mod tool.
     */
    public String importMod(Path importFolderPath, boolean showMessages) throws ModProcessingException {
        analyzeFile();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + getType());
        File fileToImport = importFolderPath.resolve(getImportExportFileName()).toFile();
        Map<String, String> map;
        try {
            map = DataStreamHelper.parseDataFile(fileToImport).get(0);
        } catch (IOException e) {
            throw new ModProcessingException("File could not be parsed '" + fileToImport.getName() + "': " +  e.getMessage());
        }
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + getReplacedLine(map.get("LINE")));
            return getType() + " [" + getReplacedLine(map.get("LINE")) + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + map.get("MGT2MT VERSION");
        }
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            if(entry.getValue().equals(map.get("LINE"))){
                sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + getReplacedLine(map.get("LINE")));
                return "false";
            }
        }
        String importLine = getModifiedImportLine(map.get("LINE"));
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(importLine)) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        if(addFeature){
            addMod(importLine);
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + getReplacedLine(map.get("LINE")) + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + getReplacedLine(map.get("LINE")));
        }
        return "true";
    }

    /**
     * Edits the mod file
     * @param addMod If true the mod will be added. If false the mod fill be removed
     * @param mod If add mod is true: This string will be printed into the text file. If add mod is false: The string is the mod name which mod should be removed
     */
    private void editFile(boolean addMod, String mod) throws ModProcessingException {
        try {
            analyzeFile();
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if(fileToEdit.exists()){
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if(charset.equals(StandardCharsets.UTF_8)){
                bw.write("\ufeff");
            }
            boolean firstLine = true;
            for(int i=1; i<=getFileContent().size(); i++){
                if(addMod){
                    if(!firstLine){
                        bw.write("\r\n");
                    }else{
                        firstLine = false;
                    }
                    bw.write(getFileContent().get(i));
                }else{
                    if(!getReplacedLine(getFileContent().get(i)).equals(mod)){
                        if(!firstLine){
                            bw.write("\r\n");
                        }else{
                            firstLine = false;
                        }
                        bw.write(getFileContent().get(i));
                    }
                }
            }
            if(addMod){
                bw.write("\r\n");
                bw.write(mod);
            }else{
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + mod);
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to edit mod file for mod " + getType() + ": " + e.getMessage());
        }
    }

    /**
     * returns that analyzed file
     */
    public final Map<Integer, String> getFileContent() {
        return fileContent;
    }

    /**
     * @return String containing all active things sorted by alphabet.
     */
    @Override
    public final String[] getContentByAlphabet() throws ModProcessingException {
        try {
            ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
            for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
                arrayListAvailableThingsSorted.add(getReplacedLine(entry.getValue()));
            }
            Collections.sort(arrayListAvailableThingsSorted);
            String[] string = new String[arrayListAvailableThingsSorted.size()];
            arrayListAvailableThingsSorted.toArray(string);
            return string;
        } catch (NullPointerException e) {
            throw new ModProcessingException("Could not return the file content: This is caused because " + getType() + " mod was not analyzed.", true);
        }
    }

    /**
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);

    /**
     * This function can be used to change the import line
     */
    public String getModifiedImportLine(String importLine) throws ModProcessingException {
        return importLine;
    }

    /**
     * This function can be used to change the export line
     */
    public String getModifiedExportLine(String exportLine) throws ModProcessingException {
        return exportLine;
    }

    /**
     * @param name The content name for which the position should be returned
     * @return The position in the fileContent list where the input id is stored in.
     */
    public final int getPositionInFileContentListByName(String name) throws ModProcessingException{
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            if(getReplacedLine(entry.getValue()).equals(name)){
                return entry.getKey();
            }
        }
        throw new ModProcessingException("Position of '" + name + "' in the file content list could not be returned: The name was not found in the file content list!");
    }

    /**
     * @return The line content where the name stands
     */
    public final String getLine(String name) throws ModProcessingException {
        try {
            return getFileContent().get(getPositionInFileContentListByName(name));
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Line content could not be returned. The name " + name + "' was not found in the file content list!");
        }
    }

    @Override
    public int getFileContentSize() {
        return fileContent.size();
    }

    @Override
    public int getContentIdByName(String name) throws ModProcessingException {
        return getPositionInFileContentListByName(name);
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault(getDefaultContentFileName(), this::getReplacedLine);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    /**
     * @return The charset in which the mod file is written
     */
    public abstract String getModFileCharset();//TODO Remove and replace with AbstractBaseMod.getCharset(); -> Enum

    /**
     * Transforms the generic to an {@literal String}.
     * @throws ModProcessingException If transformation fails
     */
    @SuppressWarnings("unchecked")
    public <T> String transformGenericToString(T t) throws ModProcessingException{
        if (t instanceof String) {
            return (String)t;
        } else {
            throw new ModProcessingException("T is invalid: Should be String", true);
        }
    }
}
