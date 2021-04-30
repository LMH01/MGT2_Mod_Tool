package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NpcGamesEditor extends AbstractSimpleEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceEditor.class);

    @Override
    public String getReplacedLine(String inputString) {
        StringBuilder outputString = new StringBuilder();
        boolean nameComplete = false;
        for(Character character : inputString.toCharArray()){
            if(character.toString().equals("<")){
                nameComplete = true;
            }
            if(!nameComplete){
                outputString.append(character);
            }
        }
        return outputString.toString().trim();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.npcGamesMod.getType();
    }

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.npcGamesMod.getBaseAnalyzer();
    }

    @Override
    public File getFileToEdit() {
        return ModManager.npcGamesMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }
}
