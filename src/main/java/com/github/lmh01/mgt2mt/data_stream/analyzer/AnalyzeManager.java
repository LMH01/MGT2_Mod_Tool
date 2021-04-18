package com.github.lmh01.mgt2mt.data_stream.analyzer;

public class AnalyzeManager {
    public static AbstractAdvancedAnalyzer engineFeatureAnalyzer = new EngineFeatureAnalyzer();
    public static AbstractAdvancedAnalyzer gameplayFeatureAnalyzer = new GameplayFeatureAnalyzer();
    public static GenreAnalyzer genreAnalyzer = new GenreAnalyzer();
}
