package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GenreAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GenreEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GenreSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;

public class GenreMod extends AbstractAdvancedMod {
    AbstractAdvancedAnalyzer genreAnalyzer = new GenreAnalyzer();
    AbstractAdvancedEditor genreEditor = new GenreEditor();
    AbstractAdvancedSharer genreSharer = new GenreSharer();

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return genreAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getEditor() {
        return genreEditor;
    }

    @Override
    public AbstractAdvancedSharer getSharer() {
        return genreSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }
}
