package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class AbstractAdvancedEditor implements BaseEditor{

    /**
     * Adds a new mod to the file
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     */
    public void addMod(Map<String, String> map) throws IOException {
        analyzeFile();
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
        for(Map<String, String> fileContent : getFileContent()){
            printValues(fileContent, bw);
            bw.write(System.getProperty("line.separator"));
        }
        printValues(map, bw);
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * Removes the input engine feature id from the EngineFeatures.txt file
     * @param name The gameplay feature id for which the gameplay feature should be removed
     */
    public boolean removeMod(String name) throws IOException {
        analyzeFile();
        int modId = getContentIdByName(name);
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
        for(Map<String, String> fileContent : getFileContent()){
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
}
