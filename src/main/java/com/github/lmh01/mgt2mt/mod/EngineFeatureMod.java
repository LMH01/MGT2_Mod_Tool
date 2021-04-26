package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.EngineFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.EngineFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.EngineFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;

public class EngineFeatureMod extends AbstractAdvancedMod {
    String[] compatibleModToolVersions = new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    EngineFeatureAnalyzer engineFeatureAnalyzer = new EngineFeatureAnalyzer();
    EngineFeatureEditor engineFeatureEditor = new EngineFeatureEditor();
    EngineFeatureSharer engineFeatureSharer = new EngineFeatureSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public EngineFeatureAnalyzer getAnalyzer(){
        return engineFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public EngineFeatureEditor getEditor(){
        return engineFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public EngineFeatureSharer getSharer(){
        return engineFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return engineFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return engineFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
            return engineFeatureSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
