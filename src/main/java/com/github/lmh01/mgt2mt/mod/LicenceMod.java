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
    AbstractSimpleAnalyzer licenceAnalyzer = new LicenceAnalyzer();
    AbstractSimpleEditor licenceEditor = new LicenceEditor();
    AbstractSimpleSharer licenceSharer = new LicenceSharer();

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return licenceAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getEditor() {
        return licenceEditor;
    }

    @Override
    public AbstractSimpleSharer getSharer() {
        return licenceSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "1.13.0"};
    }
}
