package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractSimpleContentManager extends AbstractBaseContentManager {
    final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleContentManager.class);

    public Map<Integer, String> fileContent;
    // Contains the documentation on what the contents of the game file mean
    // thas is written in the games files before the actual content starts.
    private List<String> fileDocumentationContent;
    private Map<String, Integer> contentIdsByNames;

    public AbstractSimpleContentManager(String mainTranslationKey, String id, File gameFile, Charset gameFileCharset) {
        super(mainTranslationKey, id, gameFile, gameFileCharset);
    }

    /**
     * Replaces the input string to only contain the mod name. All data in the line is removed.
     */
    protected abstract String getReplacedLine(String line);

    /**
     * Returns the line for the name
     */
    protected final String getLineByName(String name) throws ModProcessingException {
        return fileContent.get(getContentIdByName(name));
    }

    /**
     * Returns the data of the name
     */
    protected final String getDataForName(String name) throws ModProcessingException {
        return getLineByName(name).replace(name, "").trim();
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
        ArrayList<String> contentNames = new ArrayList<>();
        for (AbstractBaseContent sc : contents) {
            contentNames.add(sc.name);
        }
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

            for (String s : this.fileDocumentationContent) {
                bw.write(s);
                bw.write("\r\n");
            }

            boolean firstLine = true;
            for (int i = 0; i < fileContent.size(); i++) {
                if (action.equals(ContentAction.ADD_MOD)) {
                    if (!firstLine) {
                        bw.write("\r\n");
                    } else {
                        firstLine = false;
                    }
                    bw.write(fileContent.get(i));
                } else {
                    if (!contentNames.contains(getReplacedLine(fileContent.get(i)))) {
                        if (!firstLine) {
                            bw.write("\r\n");
                        } else {
                            firstLine = false;
                        }
                        bw.write(fileContent.get(i));
                    }
                }
            }
            if (action.equals(ContentAction.ADD_MOD)) {
                for (AbstractBaseContent sc : contents) {
                    bw.write("\r\n");
                    if (sc instanceof AbstractSimpleContent) {
                        bw.write(((AbstractSimpleContent) sc).getLine());
                    } else {
                        DebugHelper.warn(LOGGER, "Found content that does not implement AbstractSimpleContent wile editing the game file of content " + getType() + ". The violator is " + sc.name + " of type " + sc.contentType.getType());
                    }
                }
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to edit mod file for mod " + getType(), e);
        }
    }

    @Override
    public String verifyContentIntegrity() {
        StringBuilder integrityViolations = new StringBuilder();
        for (Map.Entry<Integer, String> map : fileContent.entrySet()) {
            String result = isLineValid(map.getValue());
            if (!result.isEmpty()) {
                integrityViolations.append(result).append("\n");
            }
        }
        return integrityViolations.toString();
    }

    /**
     * Checks if the input line is valid
     *
     * @return An empty string if the line is valid, otherwise a string containing the error
     */
    protected abstract String isLineValid(String line);

    @Override
    public void analyzeFile() throws ModProcessingException {
        try {
            fileDocumentationContent = DataStreamHelper.extractDocumentationContent(this.gameFile.toPath(), getCharset());
            fileContent = DataStreamHelper.getContentFromFile(gameFile, getCharset());
            setMaxId(fileContent.size() - 1);
            Map<String, Integer> contentIdsByName = new HashMap<>();
            for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
                contentIdsByName.put(getReplacedLine(entry.getValue()), entry.getKey());
            }
            this.contentIdsByNames = contentIdsByName;
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze game file for mod " + getType(), e);
        }
    }

    @Override
    public String[] getDocumentationContent() {
        return fileDocumentationContent.toArray(new String[0]);
    }

    @Override
    public String[] getContentByAlphabet() throws ModProcessingException {
        try {
            ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
                arrayListAvailableThingsSorted.add(getReplacedLine(entry.getValue()));
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
            throw new ModProcessingException("Unable to find id for name " + name + ": This name does not exist in the map!", name);
        }
    }

    @Override
    public String getContentNameById(int id) throws ModProcessingException {
        if (fileContent.containsKey(id)) {
            return getReplacedLine(fileContent.get(id));
        }
        throw new ModProcessingException("Unable to find name for id " + id + ": This id does not exist in the map!");
    }

    @Override
    public ArrayList<Integer> getActiveIds() {
        ArrayList<Integer> activeIds = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
            activeIds.add(entry.getKey());
        }
        return activeIds;
    }
}
