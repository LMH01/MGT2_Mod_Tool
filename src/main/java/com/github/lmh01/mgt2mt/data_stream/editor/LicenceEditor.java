package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LicenceEditor extends AbstractSimpleEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceEditor.class);

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.licenceModOld.getAnalyzer();
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
        return ModManager.licenceModOld.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
