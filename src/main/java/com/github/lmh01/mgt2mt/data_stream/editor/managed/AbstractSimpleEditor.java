package com.github.lmh01.mgt2mt.data_stream.editor.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.SimpleAnalyzer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The simple editor is used to edit files that use this system:
 * ValueX [Some data]
 */
public abstract class AbstractSimpleEditor implements BaseEditor, BaseFunctions {
    /**
     * Adds a mod to the file
     * @param lineToWrite The line that should be written
     */
    public void addMod(String lineToWrite) throws IOException {
        editFile(true, lineToWrite);
    }

    /**
     * Removes the input name from the text file
     * @param name The mod name that should be removed
     */
    public void removeMod(String name) throws IOException {
        editFile(false, name);
    }

    /**
     * Edits the mod file
     * @param addMod If true the mod will be added. If false the mod fill be removed
     * @param mod If add mod is true: This string will be printed into the text file. If add mod is false: The string is the mod name which mod should be removed
     */
    private void editFile(boolean addMod, String mod) throws IOException {
        getAnalyzer().analyzeFile();
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
        boolean firstLine = true;
        for(int i=1; i<=getAnalyzer().getFileContent().size(); i++){
            if(addMod){
                if(!firstLine){
                    bw.write(System.getProperty("line.separator"));
                }else{
                    firstLine = false;
                }
                bw.write(getAnalyzer().getFileContent().get(i));
            }else{
                if(!getReplacedLine(getAnalyzer().getFileContent().get(i)).equals(mod)){
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }else{
                        firstLine = false;
                    }
                    bw.write(getAnalyzer().getFileContent().get(i));
                }
            }
        }
        if(addMod){
            bw.write(System.getProperty("line.separator"));
            bw.write(mod);
        }else{
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + mod);
        }
        bw.close();
    }

    /**
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);

    /**
     * @return Returns an simple analyzer
     */
    public abstract AbstractSimpleAnalyzer getAnalyzer();
}
