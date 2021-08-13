package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NpcEngineEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcEngineEditor.class);

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        EditHelper.printLine("PLATFORM", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("SHARE", map, bw);
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.npcEngineMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.npcEngineMod.getBaseAnalyzer();
    }

    @Override
    public File getFileToEdit() {
        return ModManager.npcEngineMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    /**
     * Removes the specified genre from the npc engine file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     * @param name The name of the genre that should be removed
     */
    public void removeGenre(String name) throws IOException {
        int genreId = ModManager.genreMod.getAnalyzer().getContentIdByName(name);
        getAnalyzer().analyzeFile();
        sendLogMessage("Replacing genre id in npc engine file: " + name);
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
            if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                fileContent.remove("GENRE");
                fileContent.put("GENRE", Integer.toString(ModManager.genreMod.getAnalyzer().getActiveIds().get(Utils.getRandomNumber(0, ModManager.genreMod.getAnalyzer().getActiveIds().size()))));
            }
            printValues(fileContent, bw);
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[EOF]");
        bw.close();
    }
}
