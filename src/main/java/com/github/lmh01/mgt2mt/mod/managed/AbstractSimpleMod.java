package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
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
    public final void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.getContentFromFile(getGameFile(), getModFileCharset());
    }

    @Override
    public <T> void addMod(T t) throws ModProcessingException {
        if (t instanceof String) {
            String string = (String) t;
        } else {
            throw new ModProcessingException("T is invalid: Should be String", true);
        }
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {

    }


    /**
     * returns that analyzed file
     */
    public final Map<Integer, String> getFileContent() {
        return fileContent;
    }

    /**
     * @return A string containing all active things sorted by alphabet.
     */
    @Override
    public final String[] getContentByAlphabet(){
        ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            arrayListAvailableThingsSorted.add(getReplacedLine(entry.getValue()));
        }
        Collections.sort(arrayListAvailableThingsSorted);
        String[] string = new String[arrayListAvailableThingsSorted.size()];
        arrayListAvailableThingsSorted.toArray(string);
        return string;
    }

    /**
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);

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
    public abstract String getModFileCharset();
}
