package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.LicenceAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.LicenceEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.LicenceSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;

public class LicenceMod extends AbstractSimpleMod {
    LicenceAnalyzer licenceAnalyzer = new LicenceAnalyzer();
    LicenceEditor licenceEditor = new LicenceEditor();
    LicenceSharer licenceSharer = new LicenceSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public LicenceAnalyzer getAnalyzer(){
        return licenceAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public LicenceEditor getEditor(){
        return licenceEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public LicenceSharer getSharer(){
        return licenceSharer;
    }

    @Override
    public AbstractSimpleAnalyzer getBaseAnalyzer() {
        return licenceAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getBaseEditor() {
        return licenceEditor;
    }

    @Override
    public AbstractSimpleSharer getBaseSharer() {
        return licenceSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "1.13.0"};
    }
}
