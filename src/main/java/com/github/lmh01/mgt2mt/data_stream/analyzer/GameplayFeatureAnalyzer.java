package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GameplayFeatureAnalyzer extends AbstractAdvancedAnalyzer{
    List<Map<String, String>> fileContent;

    @Override
    public File getFileToAnalyze() {
        return Utils.getGameplayFeaturesFile();
    }

    @Override
    public String getAnalyzerType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }

    @Override
    public String getDefaultContentFile() {
        return "default_gameplay_features.txt";
    }

    @Override
    public List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = getAnalyzedFile();
    }
}
