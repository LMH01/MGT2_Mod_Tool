package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AntiCheatEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AntiCheatEditor.class);

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.antiCheatMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.antiCheatMod.getBaseAnalyzer();
    }

    @Override
    public File getFileToEdit() {
        return ModManager.antiCheatMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
