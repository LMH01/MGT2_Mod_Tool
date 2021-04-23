package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.EngineFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.EngineFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.EngineFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;

public class EngineFeatureMod extends AbstractAdvancedMod {
    String[] compatibleModToolVersions = new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    AbstractAdvancedAnalyzer engineFeatureAnalyzer = new EngineFeatureAnalyzer();
    AbstractAdvancedEditor engineFeatureEditor = new EngineFeatureEditor();
    AbstractAdvancedSharer engineFeatureSharer = new EngineFeatureSharer() {
        @Override
        public Importer getImporter() {
            return engineFeatureEditor::addMod;
        }

        @Override
        public String[] getCompatibleModToolVersions() {
            return compatibleModToolVersions;
        }

        @Override
        public AbstractAdvancedAnalyzer getAnalyzer() {
            return engineFeatureAnalyzer;
        }
    };

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return engineFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getEditor() {
        return engineFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getSharer() {
            return engineFeatureSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
