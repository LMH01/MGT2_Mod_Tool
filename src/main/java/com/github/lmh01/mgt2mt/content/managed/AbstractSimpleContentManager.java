package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractSimpleContentManager extends AbstractBaseContentManager {
    public Map<Integer, String> fileContent;

    public AbstractSimpleContentManager(String mainTranslationKey, String exportType, String defaultContentFileName, File gameFile, Charset gameFileCharset, String[] compatibleModToolVersions) {
        super(mainTranslationKey, exportType, defaultContentFileName, gameFile, gameFileCharset, compatibleModToolVersions);
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

    @Override
    public void analyzeFile() throws ModProcessingException {
        try {
            fileContent = DataStreamHelper.getContentFromFileNew(gameFile, getCharset());
            setMaxId(fileContent.size()-1);
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze game file for mod " + getType(), e);
        }
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
        for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
            if (getReplacedLine(entry.getValue()).equals(name)) {
                return entry.getKey();
            }
        }
        throw new ModProcessingException("Unable to find id for name " + name + ": This name does not exist in the map!");
    }

    @Override
    public String getContentNameById(int id) throws ModProcessingException {
        if (fileContent.containsKey(id)) {
            return fileContent.get(id);
        }
        throw new ModProcessingException("Unable to find name for id " + id + ": This id does not exist in the map!");
    }
}
