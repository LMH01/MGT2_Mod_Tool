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
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files with the system "[Key]Value".
 */
public abstract class AbstractAdvancedMod extends AbstractBaseMod {

    List<Map<String, String>> fileContent;

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addModToFile(T t) throws ModProcessingException {
        try {
            //This map contains the contents of the mod that should be added
            Map<String, String> map = (Map<String, String>) t;
            analyzeFile();
            sendLogMessage("Adding new " + getType() + ": " + map.get("NAME EN"));
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
            for(Map<String, String> fileContent : getFileContent()){
                printValues(fileContent, bw);
                bw.write("\r\n");
            }
            printValues(map, bw);
            bw.write("\r\n");
            bw.write("[EOF]");
            bw.close();
        } catch (ClassCastException e) {
            throw new ModProcessingException("T is invalid: Should be Map<String, String>", true);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType() + ": " + e.getMessage());
        }
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        try {
            analyzeFile();
            int modId = getContentIdByName(name);
            sendLogMessage("Removing " + getType() + ": " + name);
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
            for(Map<String, String> fileContent : getFileContent()){
                if (Integer.parseInt(fileContent.get("ID")) != modId) {
                    printValues(fileContent, bw);
                    bw.write("\r\n");
                }
            }
            bw.write("[EOF]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + name);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType() + ": " + e.getMessage());
        }
    }

    @Override
    public final void analyzeFile() throws ModProcessingException {
        try {
            fileContent = DataStreamHelper.parseDataFile(getGameFile());
            int currentMaxId = 0;
            for (Map<String, String> map : fileContent) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("ID")) {
                        try{
                            int currentId = Integer.parseInt(entry.getValue());
                            if (currentMaxId < currentId) {
                                currentMaxId = currentId;
                            }
                        }catch(NumberFormatException e){
                            throw new IOException(I18n.INSTANCE.get("errorMessages.gameFileCorrupted"));
                        }
                    }
                }
            }
            setMaxId(currentMaxId);
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze game file for mod " + getType() + ": " + e.getMessage());
        }
    }

    /**
     * @return The analyzed file content: {@literal List<Map<String, String>>}
     */
    public final List<Map<String, String>> getFileContent(){
        return fileContent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, String> modMap = getChangedExportMap(getSingleContentMapByName(name), name);
        Map<String, Object> map = new HashMap<>(modMap);
        Map<String, Object> dependencyMap = getDependencyMap(modMap);
        for (AbstractBaseMod mod : ModManager.mods) {
            try {
                Set<String> set = (Set<String>) dependencyMap.get(mod.getExportType());
                if (set != null) {
                    if (!set.isEmpty()) {
                        map.put("dependencies", getDependencyMap(modMap));
                    }
                }
            } catch (ClassCastException e) {
                throw new ModProcessingException("Unable to cast map entry to Set<String>", e, true);
            }
        }
        map.remove("ID");
        map.remove("PIC");
        return map;
    }

    @Override
    protected <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        return new HashMap<>();
    }

    /**
     * Imports the mod.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the mod has been imported successfully. Returns "false" when the mod already exists. Returns mod tool version of import mod when mod is not compatible with current mod tool.
     */
    @Override
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
        map.put("ID", Integer.toString(getFreeId()));
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + map.get("NAME EN"));
            return getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingContent : getFileContent()){
            for(Map.Entry<String, String> entry : existingContent.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + map.get("NAME EN"));
                    return "false";
                }
            }
        }
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(getChangedImportMap(map))) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        if(addFeature){
            try {
                addModToFile(getChangedImportMap(map));
            } catch (NullPointerException | NumberFormatException e) {
                throw new ModProcessingException("The import map could not be changed", e);
            }
            doOtherImportThings(importFolderPath, map.get("NAME EN"));
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("NAME EN"));
        }
        return "true";
    }

    /**
     * Exports all image files that belong to the mod and returns a map that contains the names of the image files
     * @param name The mod name for which the image files should be exported
     * @param assetsFolder The folder where the image files should be copied to
     * @return A map that contains the names of the image files.
     * These entries will be searched by {@link AbstractAdvancedMod#importImages()} when the mod is imported again.
     */
    public Map<String, String> exportImages(String name, Path assetsFolder) throws ModProcessingException {
        return new HashMap<>();
    }

    public void importImages() throws ModProcessingException {//TODO When import function is written use this function and write java doc

    }

    /**
     * Put things in this function that should be executed when the txt file has been imported.
     */
    public void doOtherImportThings(Path importFolderPath, String name) throws ModProcessingException {

    }

    /**
     * @return The map that contains the import values
     * Can be overwritten to adjust specific values. Useful if mod name should be replaced with mod id.
     */
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        return map;
    }

    /**
     * @return The map that contains the export values.
     * @param name The name of the mod for which the export map should be changed
     * Can be overwritten to adjust specific map values. Useful if mod id should be replaced with mod name.
     */
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        return map;
    }

    @Override
    public String[] getContentByAlphabet() throws ModProcessingException {
        try {
            ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
            for(Map<String, String> map : getFileContent()){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    if(entry.getKey().equals("NAME EN")){
                        arrayListAvailableThingsSorted.add(entry.getValue());
                    }
                }
            }
            Collections.sort(arrayListAvailableThingsSorted);
            String[] string = new String[arrayListAvailableThingsSorted.size()];
            arrayListAvailableThingsSorted.toArray(string);
            return string;
        } catch (NullPointerException e) {
            throw new ModProcessingException("Could not return the file content: This is caused because " + getType() + " mod was not analyzed.", true);
        }
    }

    @Override
    public int getFileContentSize() {
        return getFileContent().size();
    }

    @Override
    public final int getContentIdByName(String name) throws ModProcessingException{
        analyzeFile();
        try {
            for(Map<String, String> map : getFileContent()){
                if(map.get("NAME EN").equals(name)){
                    return Integer.parseInt(map.get("ID"));
                }
            }
        } catch (NullPointerException e) {
            throw new ModProcessingException("The id for sub-mod '" + name + "' of mod " + getType() + " was not found.", e);
        }
        throw new ModProcessingException("The id for sub-mod '" + name + "' of mod " + getType() + " was not found.");
    }

    /**
     * @param id The id
     * @return Returns the specified content name by id.
     * @throws ModProcessingException Is thrown when the requested content id does not exist in the map.
     */
    public final String getContentNameById(int id) throws ModProcessingException{
        try {
            if (id >= 0) {
                Map<Integer, String> idNameMap = new HashMap<>();
                for(Map<String, String> map : getFileContent()){
                    idNameMap.put(Integer.parseInt(map.get("ID")), map.get("NAME EN"));
                }
                return idNameMap.get(id);
            } else {
                throw new ModProcessingException("The name of the sub-mod with id " + id + " for mod " + getType() + " could not be returned. The id is invalid.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ModProcessingException("The name of the sub-mod with id " + id + " for mod " + getType() + " could not be returned. The id is invalid.", e, true);
        }
    }

    /**
     * @param contentNameEn The content for which the map should be returned.
     * @return A map containing all values for the specified content.
     */
    public Map<String, String> getSingleContentMapByName(String contentNameEn) throws ModProcessingException {
        List<Map<String, String>> list = getFileContent();
        String idToSearch = Integer.toString(getContentIdByName(contentNameEn));
        Map<String, String> mapSingleContent = null;
        for(Map<String, String> map : list){
            if(map.get("ID").equals(idToSearch)){
                mapSingleContent = map;
            }
        }
        return mapSingleContent;
    }

    /**
     * @param genreId The content id for which the position should be returned
     * @return The position in the fileContent list where the input id is stored in.
     */
    public int getPositionInFileContentListById(int genreId) throws ModProcessingException {
        for(int i=0; i<getFileContent().size(); i++){
            if(getFileContent().get(i).get("ID").equals(Integer.toString(genreId))){
                return i;
            }
        }
        throw new ModProcessingException("The genre id '" + genreId + "' was not found in the file content list!");
    }

    /**
     * @return An array list containing all active ids for this mod
     */
    public ArrayList<Integer> getActiveIds() {
        ArrayList<Integer> activeIds = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
            try{
                activeIds.add(Integer.parseInt(map.get("ID")));
            } catch (NumberFormatException ignored){

            }
        }
        return activeIds;
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault(getDefaultContentFileName());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    /**
     * This function is called by {@link AbstractAdvancedMod#addModToFile(Object)}. The values that are stored in the map will be written to the file by the buffered writer.
     * @throws IOException Is thrown if something went wrong when the file is being written
     */
    protected abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;

    /**
     * Transforms the generic to an {@literal Map<String, String>}.
     * @throws ModProcessingException If transformation fails
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, String> transformGenericToMap(T t) throws ModProcessingException{
        try {
            return (Map<String, String>) t;
        } catch (ClassCastException e) {
            throw new ModProcessingException("T is invalid: Should be Map<String, String>", true);
        }
    }

    /**
     * Use this function to simply transform the map entry from the mod name to the mod id. If the mod name is not found then
     * a random id is placed in the map.
     * @param map The map that contains the value that should be replaced
     * @param mapKey The map key for which the value should be replaced
     * @param mod The mod for which the name should be replaced with the id
     */
    protected void replaceImportMapEntry(Map<String, String> map, String mapKey, AbstractBaseMod mod) {
        try {
            map.replace(mapKey, Integer.toString(mod.getContentIdByName(map.get(mapKey))));
        } catch (ModProcessingException e) {
            map.replace(mapKey, Integer.toString(Utils.getRandomNumber(0, mod.getMaxId())));
        }
    }
}
