package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GameplayFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GameplayFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GameplayFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;

public class GameplayFeatureMod extends AbstractAdvancedMod {
    GameplayFeatureAnalyzer gameplayFeatureAnalyzer = new GameplayFeatureAnalyzer();
    GameplayFeatureEditor gameplayFeatureEditor = new GameplayFeatureEditor();
    GameplayFeatureSharer gameplayFeatureSharer = new GameplayFeatureSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GameplayFeatureAnalyzer getAnalyzer(){
        return gameplayFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GameplayFeatureEditor getEditor(){
        return gameplayFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GameplayFeatureSharer getSharer(){
        return gameplayFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return gameplayFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return gameplayFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return gameplayFeatureSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }
}
