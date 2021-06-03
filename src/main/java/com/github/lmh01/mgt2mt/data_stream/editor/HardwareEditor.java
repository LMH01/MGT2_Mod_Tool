package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HardwareEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareEditor.class);

    @Override
    public void addMod(Map<String, String> map) throws IOException {
        getAnalyzer().analyzeFile();
        sendLogMessage("Adding new " + getType() + ": " + map.get("NAME EN"));
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
        int currentType = 0;
        boolean firstMap = true;
        for(Map<String, String> fileContent : getAnalyzer().getFileContent()){
            if(firstMap){
                currentType = Integer.parseInt(fileContent.get("TYP"));
                firstMap = false;
            }
            if(currentType != Integer.parseInt(fileContent.get("TYP"))){
                currentType = Integer.parseInt(fileContent.get("TYP"));
                bw.write("////////////////////////////////////////////////////////////////////");
                bw.write(System.getProperty("line.separator"));
                bw.write(System.getProperty("line.separator"));
            }
            printValues(fileContent, bw);
            bw.write(System.getProperty("line.separator"));
        }
        if(currentType != Integer.parseInt(map.get("TYP"))){
            currentType = Integer.parseInt(map.get("TYP"));
            bw.write("////////////////////////////////////////////////////////////////////");
            bw.write(System.getProperty("line.separator"));
            bw.write(System.getProperty("line.separator"));
        }
        printValues(map, bw);
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    @Override
    public void removeMod(String name) throws IOException {
        getAnalyzer().analyzeFile();
        int modId = getAnalyzer().getContentIdByName(name);
        sendLogMessage("Removing " + getType() + ": " + name);
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
        int currentType = 0;
        boolean firstMap = true;
        for(Map<String, String> fileContent : getAnalyzer().getFileContent()){
            if (Integer.parseInt(fileContent.get("ID")) != modId) {
                if(firstMap){
                    currentType = Integer.parseInt(fileContent.get("TYP"));
                    firstMap = false;
                }
                if(currentType != Integer.parseInt(fileContent.get("TYP"))){
                    currentType = Integer.parseInt(fileContent.get("TYP"));
                    bw.write("////////////////////////////////////////////////////////////////////");
                    bw.write(System.getProperty("line.separator"));
                    bw.write(System.getProperty("line.separator"));
                }
                printValues(fileContent, bw);
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + name);
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        EditHelper.printLine("TYP",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        if(map.containsKey("ONLY_HANDHELD")){
            EditHelper.printLine("ONLY_HANDHELD", map, bw);
        }
        if(map.containsKey("ONLY_STATIONARY")){
            EditHelper.printLine("ONLY_STATIONARY", map, bw);
        }
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.hardwareMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.hardwareMod.getBaseAnalyzer();
    }

    @Override
    public File getFileToEdit() {
        return ModManager.hardwareMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
