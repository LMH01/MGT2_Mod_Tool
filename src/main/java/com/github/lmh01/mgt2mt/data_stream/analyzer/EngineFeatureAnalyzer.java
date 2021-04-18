package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EngineFeatureAnalyzer extends AbstractAdvancedAnalyzer {
    List<Map<String, String>> fileContent;
    int maxId = 0;

    @Override
    public File getFileToAnalyze() {
        return Utils.getEngineFeaturesFile();
    }

    @Override
    public String getAnalyzerType() {
        return I18n.INSTANCE.get("commonText.engineFeature.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public String getDefaultContentFile() {
        return "default_engine_features.txt";
    }

    @Override
    public List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = getAnalyzedFile();
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public void setMaxId(int id) {
        maxId = id;
    }
}
