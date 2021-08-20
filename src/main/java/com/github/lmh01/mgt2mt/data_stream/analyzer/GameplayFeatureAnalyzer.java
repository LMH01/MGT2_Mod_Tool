package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class GameplayFeatureAnalyzer extends AbstractAdvancedAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureAnalyzer.class);

    @Override
    public File getFileToAnalyze() {
        return ModManager.gameplayFeatureModOld.getFile();
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_gameplay_features.txt";
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }
}
