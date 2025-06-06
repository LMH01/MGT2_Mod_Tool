package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractAdvancedContentManager extends AbstractBaseContentManager {
    final Logger LOGGER = LoggerFactory.getLogger(AbstractAdvancedContentManager.class);

    public List<Map<String, String>> fileContent;
    // Contains the documentation on what the contents of the game file mean
    // thas is written in the games files before the actual content starts.
    private List<String> fileDocumentationContent;
    private Map<String, Integer> contentIdsByNames;
    private Map<Integer, String> contentNamesByIds;

    public AbstractAdvancedContentManager(String mainTranslationKey, String id, File gameFile, Charset gameFileCharset) {
        super(mainTranslationKey, id, gameFile, gameFileCharset);
    }

    /**
     * This function is called by {@link AbstractBaseContentManager#addContent(AbstractBaseContent)} and {@link AbstractBaseContentManager#removeContent(String)}.
     * The values that are stored in the map will be written to the file by the buffered writer.
     *
     * @throws IOException Is thrown if something went wrong when the file is being written
     */
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        for (DataLine dl : getDataLines()) {
            dl.print(map, bw);
        }
    }

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
     *
     * @return StringBuilder that contains content integrity violations
     */
    @Override
    public String verifyContentIntegrity() {
        StringBuilder integrityViolations = new StringBuilder();
        Map<Integer, String> usedIds = new HashMap<>();// Stores the id and the names
        for (Map<String, String> map : fileContent) {
            // It is not checked for the ID and NAME EN tag because the program will throw an exception if they are missing even before this function is called
            int id = Integer.parseInt(map.get("ID"));
            if (usedIds.containsKey(id)) {
                integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation1"), gameFile.getName(), id, map.get("NAME EN"), getType(), usedIds.get(id))).append("\n");
            }
            usedIds.put(id, map.get("NAME EN"));
            for (DataLine dl : getDataLines()) {
                if (dl.required || map.get(dl.key) != null) {
                    if (!map.containsKey(dl.key)) {
                        integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation2"), gameFile.getName(), dl.key, map.get("NAME EN"), getType())).append("\n");
                    } else {
                        if (dl.dataType.equals(DataType.INT)) {
                            try {
                                Integer.parseInt(map.get(dl.key));
                            } catch (NumberFormatException e) {
                                integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation3"), gameFile.getName(), dl.key, map.get("NAME EN"), getType(), map.get(dl.key))).append("\n");
                            }
                        } else if (dl.dataType.equals(DataType.EMPTY)) {
                            if (!map.get(dl.key).isEmpty()) {
                                integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation4"), gameFile.getName(), dl.key, map.get("NAME EN"), getType(), map.get(dl.key))).append("\n");
                            }
                        } else if (dl.dataType.equals(DataType.BOOLEAN)) {
                            if (!map.get(dl.key).equals("true") && !map.get(dl.key).equals("false")) {
                                integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation5"), gameFile.getName(), dl.key, map.get("NAME EN"), getType(), map.get(dl.key))).append("\n");
                            }
                        } else if (dl.dataType.equals(DataType.INT_LIST)) {
                            try {
                                Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get(dl.key)));
                            } catch (NumberFormatException e) {
                                integrityViolations.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.variation6"), gameFile.getName(), dl.key, map.get("NAME EN"), getType(), e.getMessage())).append("\n");
                            }
                        }
                    }
                }
            }
            String specialCases = analyzeSpecialCases(map);
            if (!specialCases.isEmpty()) {
                integrityViolations.append(specialCases);
            }

        }
        return integrityViolations.toString();
    }

    /**
     * Returns a list of the data lines for this content.
     * Does not contain data lines for the name and the id.
     * This function is used by {@link AbstractAdvancedContentManager#printValues(Map, BufferedWriter)} and {@link AbstractAdvancedContentManager#verifyContentIntegrity()}
     */
    protected abstract List<DataLine> getDataLines();

    /**
     * This function can be overwritten to add special cases to the content verification.
     * This function is used by {@link AbstractAdvancedContentManager#verifyContentIntegrity()}
     *
     * @param map Contains the data of the content
     * @return A string containing problems, empty if no problems where found
     */
    protected String analyzeSpecialCases(Map<String, String> map) {
        return "";
    }

    /**
     * Edits the games text file(s) to add or remove the content.
     * This function should be used when multiple contents should be removed/added simultaneously because the
     * text file is only written once this way.
     *
     * @param contents A list containing the contents that should be added/removed.
     * @param action   The action that should be performed
     */
    public void editTextFiles(List<AbstractBaseContent> contents, ContentAction action) throws ModProcessingException {
        ArrayList<AbstractAdvancedContent> advancedContents = new ArrayList<>();
        for (AbstractBaseContent content : contents) {
            if (content instanceof AbstractAdvancedContent) {
                advancedContents.add((AbstractAdvancedContent) content);
            } else {
                DebugHelper.warn(LOGGER, "Found content that does not implement AbstractSimpleContent wile editing the game file of content " + getType() + ". The violator is " + content.name + " of type " + content.contentType.getType());
            }
        }
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
                // write file documentation
                for (String s : this.fileDocumentationContent) {
                    bw.write(s);
                    bw.write("\r\n");
                }
                // add extra empty line to delimit documentation from actual content (because this is what the original game files do)
                // only do so, if at least one documentation line was written
                if (!this.fileDocumentationContent.isEmpty()) {
                    bw.write("\r\n");
                }
                for (Map<String, String> fileContent : fileContent) {
                    printValues(fileContent, bw);
                    bw.write("\r\n");
                }
                for (AbstractAdvancedContent ac : advancedContents) {
                    Map<String, String> map = ac.getMap();
                    printValues(map, bw);
                    bw.write("\r\n");
                }
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
            }
        } else {
            ArrayList<Integer> idsToRemove = new ArrayList<>();
            for (AbstractAdvancedContent ac : advancedContents) {
                idsToRemove.add(ac.id);
            }
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
                for (String s : this.fileDocumentationContent) {
                    bw.write(s);
                    bw.write("\r\n");
                }
                // add extra empty line to delimit documentation from actual content (because this is what the original game files do)
                bw.write("\r\n");
                for (Map<String, String> fileContent : fileContent) {
                    if (!idsToRemove.contains(Integer.parseInt(fileContent.get("ID")))) {
                        printValues(fileContent, bw);
                        bw.write("\r\n");
                    }
                }
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
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
        contentNamesByIds = new HashMap<>();
        contentIdsByNames = new HashMap<>();
        try {
            fileDocumentationContent = DataStreamHelper.extractDocumentationContent(this.gameFile.toPath(), getCharset());
            fileContent = DataStreamHelper.parseDataFile(gameFile);
            int currentMaxId = 0;
            for (Map<String, String> map : fileContent) {
                if (!map.containsKey("ID") || !map.containsKey("NAME EN")) {
                    throw new IOException(I18n.INSTANCE.get("errorMessages.gameFileCorrupted"));
                }
                try {
                    int currentId = Integer.parseInt(map.get("ID"));
                    String currentName = map.get("NAME EN");
                    if (currentMaxId < currentId) {
                        currentMaxId = currentId;
                    }
                    contentNamesByIds.put(currentId, currentName);
                    contentIdsByNames.put(currentName, currentId);
                } catch (NumberFormatException e) {
                    throw new IOException(I18n.INSTANCE.get("errorMessages.gameFileCorrupted"), e);
                }
            }
            setMaxId(currentMaxId);
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze game file for mod " + getType(), e);
        }
        TextAreaHelper.appendDebug(String.format("Content of type %s in game files (contentNamesByIds):\n%s", this.getType(), Arrays.toString(contentNamesByIds.entrySet().toArray())));
        TextAreaHelper.appendDebug(String.format("Content of type %s in game files (contentIdsByNames):\n%s", this.getType(), Arrays.toString(contentIdsByNames.entrySet().toArray())));
    }

    @Override
    public String[] getDocumentationContent() {
        return fileDocumentationContent.toArray(new String[0]);
    }

    @Override
    public Map<String, Integer> getContentIdsByNames() {
        return contentIdsByNames;
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
        if (contentIdsByNames.containsKey(name)) {
            return contentIdsByNames.get(name);
        } else {
            throw new ModProcessingException(String.format("The id for sub-mod '%s' of type %s was not found. Existing ids (contentIdsByName): %s", name, getType(), Arrays.toString(contentIdsByNames.entrySet().toArray())));
        }
    }

    @Override
    public String getContentNameById(int id) throws ModProcessingException {
        if (contentNamesByIds.containsKey(id)) {
            return contentNamesByIds.get(id);
        } else {
            throw new ModProcessingException(String.format("The name of sub-mod with id %d for type %s could not be returned. The id does not exist. Existing ids (contentNamesByIds): %s", id, getType(), Arrays.toString(contentNamesByIds.entrySet().toArray())));
        }
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