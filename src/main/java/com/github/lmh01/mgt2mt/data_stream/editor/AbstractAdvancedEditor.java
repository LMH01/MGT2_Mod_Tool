package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The advanced editor is used to edit files that use this system:
 * [KeyX]ValueX
 */
public abstract class AbstractAdvancedEditor implements BaseEditor{

    /**
     * Adds a new mod to the file
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     */
    public void addMod(Map<String, String> map) throws IOException {
        getAnalyzer().analyzeFile();
        sendLogMessage("Adding new " + getEditorType() + ": " + map.get("NAME EN"));
        Charset charset = getCharset();
        File fileToEdit = getFileToEdit();
        if(fileToEdit.exists()){
            fileToEdit.delete();
        }
        fileToEdit.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
        if(charset.equals(StandardCharsets.UTF_8)){
            bw.write("\ufeff");
        }
        for(Map<String, String> fileContent : getAnalyzer().getFileContent()){
            printValues(fileContent, bw);
            bw.write(System.getProperty("line.separator"));
        }
        printValues(map, bw);
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * Removes the input name from the text file
     * @param name The mod name that should be removed
     */
    public boolean removeMod(String name) throws IOException {
        getAnalyzer().analyzeFile();
        int modId = getAnalyzer().getContentIdByName(name);
        sendLogMessage("Removing " + getEditorType() + ": " + name);
        Charset charset = getCharset();
        File fileToEdit = getFileToEdit();
        if(fileToEdit.exists()){
            fileToEdit.delete();
        }
        fileToEdit.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
        if(charset.equals(StandardCharsets.UTF_8)){
            bw.write("\ufeff");
        }
        for(Map<String, String> fileContent : getAnalyzer().getFileContent()){
            if (Integer.parseInt(fileContent.get("ID")) != modId) {
                printValues(fileContent, bw);
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getEditorType() + " - " + name);
        return true;
    }

    /**
     * Writes the values that are stored in the map to the file
     */
    abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;

    /**
     * @return Returns the analyzer for the mod
     */
    abstract AbstractAdvancedAnalyzer getAnalyzer();
}
