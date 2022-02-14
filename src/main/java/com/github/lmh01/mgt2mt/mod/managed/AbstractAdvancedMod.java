package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files with the system "[Key]Value".
 * If the mod needs dependencies use {@link AbstractAdvancedDependentMod}
 * If image files should be processed create a new mod using {@link AbstractComplexMod}
 */
public abstract class AbstractAdvancedMod extends AbstractBaseMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdvancedMod.class);

    List<Map<String, String>> fileContent;

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addModToFile(T t) throws ModProcessingException {
        try {
            //This map contains the contents of the mod that should be added
            Map<String, String> map = (Map<String, String>) t;
            analyzeFile();
            LOGGER.info("Adding new " + getType() + ": " + map.get("NAME EN"));
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if (fileToEdit.exists()) {
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            for (Map<String, String> fileContent : getFileContent()) {
                printValues(fileContent, bw);
                bw.write("\r\n");
            }
            printValues(map, bw);
            bw.write("\r\n");
            bw.write("[EOF]");
            bw.close();
        } catch (ClassCastException e) {
            throw new ModProcessingException("T is invalid: Should be Map<String, String>", e);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
        }
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        try {
            analyzeFile();
            int modId = getContentIdByName(name);
            LOGGER.info("Removing " + getType() + ": " + name);
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if (fileToEdit.exists()) {
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            for (Map<String, String> fileContent : getFileContent()) {
                if (Integer.parseInt(fileContent.get("ID")) != modId) {
                    printValues(fileContent, bw);
                    bw.write("\r\n");
                }
            }
            bw.write("[EOF]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + name);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
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
                        try {
                            int currentId = Integer.parseInt(entry.getValue());
                            if (currentMaxId < currentId) {
                                currentMaxId = currentId;
                            }
                        } catch (NumberFormatException e) {
                            throw new IOException(I18n.INSTANCE.get("errorMessages.gameFileCorrupted"));
                        }
                    }
                }
            }
            setMaxId(currentMaxId);
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze game file for mod " + getType() + ": " + e.getMessage(), e);
        }
    }

    /**
     * @return The analyzed file content: {@literal List<Map<String, String>>}
     */
    public final List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, String> modMap;
        try {
            modMap = getChangedExportMap(getSingleContentMapByName(name), name);
        } catch (NumberFormatException e) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.warningExportMapNotChangedProperly") + ":");
            TextAreaHelper.printStackTrace(e);
            modMap = getSingleContentMapByName(name);
        }
        Map<String, Object> map = new HashMap<>(modMap);
        map.remove("ID");
        map.remove("PIC");
        map.put("mod_name", name);
        return map;
    }

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeFile();
        Map<String, String> importMap = getChangedImportMap(Utils.transformObjectMapToStringMap(map));
        importMap.put("ID", Integer.toString(getModIdByNameFromImportHelperMap(map.get("mod_name").toString())));
        addModToFile(importMap);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    @Override
    public String[] getContentByAlphabet() throws ModProcessingException {
        try {
            ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
            for (Map<String, String> map : getFileContent()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("NAME EN")) {
                        arrayListAvailableThingsSorted.add(entry.getValue());
                    }
                }
            }
            Collections.sort(arrayListAvailableThingsSorted);
            String[] string = new String[arrayListAvailableThingsSorted.size()];
            arrayListAvailableThingsSorted.toArray(string);
            return string;
        } catch (NullPointerException e) {
            throw new ModProcessingException("Could not return the file content: This is caused because " + getType() + " mod was not analyzed.", e);
        }
    }

    @Override
    public int getFileContentSize() {
        return getFileContent().size();
    }

    @Override
    public final int getContentIdByName(String name) throws ModProcessingException {
        analyzeFile();
        try {
            for (Map<String, String> map : getFileContent()) {
                if (map.get("NAME EN").equals(name)) {
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
    @Override
    public final String getContentNameById(int id) throws ModProcessingException {
        try {
            if (id >= 0) {
                Map<Integer, String> idNameMap = new HashMap<>();
                for (Map<String, String> map : getFileContent()) {
                    idNameMap.put(Integer.parseInt(map.get("ID")), map.get("NAME EN"));
                }
                return idNameMap.get(id);
            } else {
                throw new ModProcessingException("The name of the sub-mod with id " + id + " for mod " + getType() + " could not be returned. The id is invalid.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ModProcessingException("The name of the sub-mod with id " + id + " for mod " + getType() + " could not be returned. The id is invalid.", e);
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
        for (Map<String, String> map : list) {
            if (map.get("ID").equals(idToSearch)) {
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
        for (int i = 0; i < getFileContent().size(); i++) {
            if (getFileContent().get(i).get("ID").equals(Integer.toString(genreId))) {
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
        for (Map<String, String> map : getFileContent()) {
            try {
                activeIds.add(Integer.parseInt(map.get("ID")));
            } catch (NumberFormatException ignored) {

            }
        }
        return activeIds;
    }

    @Override
    public String[] getDefaultContent() {
        if (defaultContent.length == 0) {
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
     *
     * @throws IOException Is thrown if something went wrong when the file is being written
     */
    protected abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;


    /**
     * @return The map that contains the import values
     * Can be overwritten to adjust specific values. Useful if mod name should be replaced with mod id.
     */
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        return map;
    }

    /**
     * @param name The name of the mod for which the export map should be changed
     *             Can be overwritten to adjust specific map values. Useful if mod id should be replaced with mod name.
     * @return The map that contains the export values.
     */
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        return map;
    }

    /**
     * Transforms the generic to an {@literal Map<String, String>}.
     *
     * @throws ModProcessingException If transformation fails
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, String> transformGenericToMap(T t) throws ModProcessingException {
        try {
            return (Map<String, String>) t;
        } catch (ClassCastException e) {
            throw new ModProcessingException("T is invalid: Should be Map<String, String>", e);
        }
    }

    /**
     * Use this function to replace the value behind the key with the id specific for that mod-name.
     * If the key does not exist or the id is not found a warning message is printed into the text area
     * and a random replacement id will be chosen.
     * Does only work if the value is a single name for which an id has been set.
     * Example replacement: Horror -> 18
     *
     * @param map    The map that contains the value that should be replaced
     * @param mapKey The map key for which the value should be replaced
     * @param mod    The mod for which the name should be replaced with the id
     * @see AbstractBaseMod#getModIdByNameFromImportHelperMap(String) Function that is used to get the id by the name
     * @see AbstractBaseMod#addEntryToImportHelperMap(String) Function that adds an entry to the import helper map
     * */
    protected void replaceImportMapEntry(Map<String, String> map, String mapKey, AbstractBaseMod mod) {
        try {
            map.replace(mapKey, Integer.toString(mod.getModIdByNameFromImportHelperMap(map.get(mapKey))));
        } catch (ModProcessingException e) {
            map.replace(mapKey, Integer.toString(Utils.getRandomNumber(0, mod.getMaxId())));
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.replacingOfMapEntryFailed") + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initializeImportHelperMap() throws ModProcessingException {
        Map<String, Integer> helperMap = new HashMap<>();
        for (Map<String, String> map : getFileContent()) {
            try {
                helperMap.put(map.get("NAME EN"), Integer.parseInt(map.get("ID")));
            } catch (NullPointerException e) {
                throw new ModProcessingException("Import helper map can not be initialized", e);
            }
        }
        importHelperMap = helperMap;
    }
}
