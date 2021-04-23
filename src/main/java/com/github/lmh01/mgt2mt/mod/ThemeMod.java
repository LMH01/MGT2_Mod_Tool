package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileEnAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileGeAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.ThemeEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.ThemeSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;

public class ThemeMod extends AbstractSimpleMod {
    AbstractSimpleAnalyzer themeFileGeAnalyzer = new ThemeFileGeAnalyzer();
    AbstractSimpleAnalyzer themeFileEnAnalyzer = new ThemeFileEnAnalyzer();
    AbstractSimpleEditor themeEditor = new ThemeEditor();
    AbstractSimpleSharer themeSharer = new ThemeSharer();

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return themeFileGeAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getEditor() {
        return themeEditor;
    }

    @Override
    public AbstractSimpleSharer getSharer() {
        return themeSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0"};
    }
}
