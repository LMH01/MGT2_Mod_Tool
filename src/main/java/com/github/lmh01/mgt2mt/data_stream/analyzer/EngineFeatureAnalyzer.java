package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.util.Utils;
import java.io.File;

public class EngineFeatureAnalyzer extends AbstractAnalyzer{
    @Override
    public File getFileToAnalyze() {
        return Utils.getEngineFeaturesFile();
    }

    @Override
    public String getAnalyzerType() {
        return "Engine feature";
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public String getDefaultContentFile() {
        return "default_engine_features.txt";
    }
}
