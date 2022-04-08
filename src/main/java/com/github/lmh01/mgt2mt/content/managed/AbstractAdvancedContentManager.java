package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractAdvancedContentManager extends AbstractBaseContentManager {
    public List<Map<String, String>> fileContent;

    public AbstractAdvancedContentManager(String mainTranslationKey, String exportType, String defaultContentFileName, File gameFile, Charset gameFileCharset) {
        super(mainTranslationKey, exportType, defaultContentFileName, gameFile, gameFileCharset);
    }
    
    /**
     * This function is called by {@link AbstractBaseContentManager#addContent(AbstractBaseContent)} and {@link AbstractBaseContentManager#removeContent(String)}.
     * The values that are stored in the map will be written to the file by the buffered writer.
     *
     * @throws IOException Is thrown if something went wrong when the file is being written
     */
    protected abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;

    /**
     * Constructs the base content from the input map.
     */
    public abstract AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException;

    /**
     * @param name The content for which the map should be returned.
     * @return A map containing all values for the specified content.
     */
    public Map<String, String> getSingleContentMap(String name) throws ModProcessingException {
        List<Map<String, String>> list = fileContent;
        String idToSearch = Integer.toString(getContentIdByName(name));
        Map<String, String> mapSingleContent = null;
        for (Map<String, String> map : list) {
            if (map.get("ID").equals(idToSearch)) {
                mapSingleContent = map;
            }
        }
        return mapSingleContent;
    }

    /**
     * Returns the id if it is contained within the map.
     * Returns null otherwise.
     */
    protected Integer getIdFromMap(Map<String, ?> map) {
        Integer id = null;
        if (map.containsKey("ID")) {
            id = Integer.parseInt((String) map.get("ID"));
        }
        return id;
    }

    /**
     * Checks if the integrity of the fileContent is violated.
     * The integrity is violated if an id is missing from an entry, if
     * an id is assigned more than once or if the ID or NAME EN tag are missing from  the map.
     */
    public void verifyContentIntegrity() throws ModProcessingException {
        Map<Integer, String> usedIds = new HashMap<>();// Stores the id and the names
        for (Map<String, String> map : fileContent) {
            if (!map.containsKey("ID") | !map.containsKey("NAME EN")) {
                throw new ModProcessingException("The integrity of game file \"" + gameFile.getName() +  "\" is not valid. Reason: The [NAME EN] or [ID] tag of " + map.get("NAME EN") + " of type " + getType() + " is missing.");
            }
            try {
                int id = Integer.parseInt(map.get("ID"));
                if (usedIds.containsKey(id)) {
                    throw new ModProcessingException("The integrity of game file \"" + gameFile.getName() +  "\" is not valid. Reason: The id " + id + " of " + map.get("NAME EN") + " of type " + getType() + " is already used by " + usedIds.get(id) + ".");
                }
                usedIds.put(id, map.get("NAME EN"));
            } catch (NumberFormatException e) {
                throw new ModProcessingException("The integrity of game file  \"" + gameFile.getName() +  "\" is not valid. Reason: The [ID] field of " + map.get("NAME EN") + " of type " + getType() + " is invalid.", e);
            }
        }
    }

    /**
     * Should be overwritten if content is dependent on other contents.
     */
    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        return constructContentFromMap(Utils.transformObjectMapToStringMap(map));
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        return constructContentFromMap(getSingleContentMap(name));
    }

    @Override
    public void analyzeFile() throws ModProcessingException {
        try {
            fileContent = DataStreamHelper.parseDataFile(gameFile);
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
            throw new ModProcessingException("Unable to analyze game file for mod " + getType(), e);
        }
    }

    @Override
    public String[] getContentByAlphabet() throws ModProcessingException {
        try {
            ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
            for (Map<String, String> map : fileContent) {
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
    public int getContentIdByName(String name) throws ModProcessingException {
        try {
            for (Map<String, String> map : fileContent) {
                if (map.get("NAME EN").equals(name)) {
                    return Integer.parseInt(map.get("ID"));
                }
            }
        } catch (NullPointerException e) {
            throw new ModProcessingException("The id for sub-mod '" + name + "' of type " + getType() + " was not found.", e);
        }
        throw new ModProcessingException("The id for sub-mod '" + name + "' of type " + getType() + " was not found.");
    }

    @Override
    public String getContentNameById(int id) throws ModProcessingException {
        if (id >= 0) {
            for (Map<String, String> map : fileContent) {
                if (Integer.parseInt(map.get("ID")) == id) {
                    return map.get("NAME EN");
                }
            }
        } else {
            throw new ModProcessingException("The name of sub-mod with id " + id + " for type " + getType() + " could not be returned. The id is invalid.");
        }
        throw new ModProcessingException("The name of sub-mod with id " + id + " for type " + getType() + " could not be returned. The id is invalid.");
    }

    @Override
    public ArrayList<Integer> getActiveIds() {
        ArrayList<Integer> activeIds = new ArrayList<>();
        for (Map<String, String> map : fileContent) {
            try {
                activeIds.add(Integer.parseInt(map.get("ID")));
            } catch (NumberFormatException ignored) {

            }
        }
        return activeIds;
    }
}