package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files where each mod is written in one line.
 * If the mod needs dependencies use {@link AbstractSimpleDependentMod}
 */
public abstract class AbstractSimpleMod extends AbstractBaseMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleMod.class);


    Map<Integer, String> fileContent;

    @Override
    public void analyzeFile() throws ModProcessingException {
        try {
            fileContent = DataStreamHelper.getContentFromFile(getGameFile(), getCharset());
        } catch (IOException e) {
            throw new ModProcessingException("Unable to analyze mod-files for mod " + getType(), e);
        }
    }

    @Override
    public <T> void addModToFile(T t) throws ModProcessingException {
        if (t instanceof String) {
            String string = (String) t;
            editFile(true, string);
        } else {
            throw new ModProcessingException("T is invalid: Should be String");
        }
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        editFile(false, name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        String line = getModifiedExportLine(getLine(name));
        map.put("line", line);
        map.put("mod_name", name);
        return map;
    }

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        addModToFile(getChangedImportLine((String) map.get("line")));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    /**
     * Edits the mod file
     *
     * @param addMod If true the mod will be added. If false the mod fill be removed
     * @param mod    If add mod is true: This string will be printed into the text file. If add mod is false: The string is the mod name which mod should be removed
     */
    private void editFile(boolean addMod, String mod) throws ModProcessingException {
        try {
            analyzeFile();
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
            boolean firstLine = true;
            for (int i = 1; i <= getFileContent().size(); i++) {
                if (addMod) {
                    if (!firstLine) {
                        bw.write("\r\n");
                    } else {
                        firstLine = false;
                    }
                    bw.write(getFileContent().get(i));
                } else {
                    if (!getReplacedLine(getFileContent().get(i)).equals(mod)) {
                        if (!firstLine) {
                            bw.write("\r\n");
                        } else {
                            firstLine = false;
                        }
                        bw.write(getFileContent().get(i));
                    }
                }
            }
            if (addMod) {
                bw.write("\r\n");
                bw.write(mod);
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + mod);
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to edit mod file for mod " + getType(), e);
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
            for (Map.Entry<Integer, String> entry : getFileContent().entrySet()) {
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

    /**
     * Replaces the input string to only contain the mod name. All data in the line is removed.
     */
    public abstract String getReplacedLine(String inputString);

    /**
     * This function can be used to change the import line
     */
    public String getChangedImportLine(String importLine) throws ModProcessingException {
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
    public final int getPositionInFileContentListByName(String name) throws ModProcessingException {
        for (Map.Entry<Integer, String> entry : getFileContent().entrySet()) {
            if (getReplacedLine(entry.getValue()).equals(name)) {
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
            throw new ModProcessingException("Line content could not be returned. The name " + name + "' was not found in the file content list!", e);
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
    public String getContentNameById(int id) throws ModProcessingException {
        return getReplacedLine(getFileContent().get(id));
    }

    @Override
    public String[] getDefaultContent() {
        if (defaultContent.length == 0) {
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
     * Transforms the generic to an {@literal String}.
     *
     * @throws ModProcessingException If transformation fails
     */
    @SuppressWarnings("unchecked")
    public <T> String transformGenericToString(T t) throws ModProcessingException {
        if (t instanceof String) {
            return (String) t;
        } else {
            throw new ModProcessingException("T is invalid: Should be String");
        }
    }

    /**
     * Initializes the import helper map for this mod with all currently installed mods.
     */
    @Override
    public void initializeImportHelperMap() throws ModProcessingException {
        Map<String, Integer> helperMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : getFileContent().entrySet()) {
            helperMap.put(getReplacedLine(entry.getValue()), entry.getKey());
        }
        setMaxId(helperMap.size());
        importHelperMap = helperMap;
    }
}
