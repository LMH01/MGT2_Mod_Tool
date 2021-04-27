package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LicenceEditor extends AbstractSimpleEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceEditor.class);

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.licenceMod.getAnalyzer();
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.licence");
    }

    @Override
    public File getFileToEdit() {
        return ModManager.licenceMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
