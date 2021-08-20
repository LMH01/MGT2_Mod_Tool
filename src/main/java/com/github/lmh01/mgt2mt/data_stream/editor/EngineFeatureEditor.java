package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EngineFeatureEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureEditor.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.engineFeature");
    }

    @Override
    public File getFileToEdit() {
        return ModManager.engineFeatureModOld.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.engineFeatureModOld.getAnalyzer();
    }
}
