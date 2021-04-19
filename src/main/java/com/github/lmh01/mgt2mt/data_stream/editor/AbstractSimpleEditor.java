package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The simple editor is used to edit files that use this system:
 * ValueX [Some data]
 */
abstract class AbstractSimpleEditor implements BaseEditor{
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
    public boolean removeMod(String name) throws IOException {
        return editFile(false, name);
    }

    /**
     * Edits the mod file
     * @param addMod If true the mod will be added. If false the mod fill be removed
     * @param mod If add mod is true: This string will be printed into the text file. If add mod is false: The string is the mod name which mod should be removed
     */
    private boolean editFile(boolean addMod, String mod) throws IOException {
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
            ChangeLog.addLogEntry(35, mod);
        }else{
            ChangeLog.addLogEntry(36, mod);
        }
        bw.close();
        return true;
    }

    /**
     * @return Returns the analyzer for the mod
     */
    abstract AbstractSimpleAnalyzer getAnalyzer();

    /**
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);
}
