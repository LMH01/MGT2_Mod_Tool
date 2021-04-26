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
    GenreAnalyzer genreAnalyzer = new GenreAnalyzer();
    GenreEditor genreEditor = new GenreEditor();
    GenreSharer genreSharer = new GenreSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GenreAnalyzer getAnalyzer(){
        return genreAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GenreEditor getEditor(){
        return genreEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GenreSharer getSharer(){
        return genreSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return genreAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return genreEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return genreSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {//TODO getCompatibleModToolVersions aus sharer raus nehmen
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }
}
