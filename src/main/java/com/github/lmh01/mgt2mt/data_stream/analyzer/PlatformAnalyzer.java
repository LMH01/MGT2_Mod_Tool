package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class PlatformAnalyzer extends AbstractAdvancedAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformAnalyzer.class);

    @Override
    public File getFileToAnalyze() {
        return ModManager.platformModOld.getFile();
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_platforms.txt";
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.platformModOld.getType();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.platformModOld.getMainTranslationKey();
    }
}
