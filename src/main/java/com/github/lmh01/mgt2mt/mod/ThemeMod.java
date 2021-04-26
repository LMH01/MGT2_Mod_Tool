package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileEnAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileGeAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.ThemeEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.ThemeSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThemeMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeMod.class);
    ThemeFileGeAnalyzer themeFileGeAnalyzer = new ThemeFileGeAnalyzer();
    ThemeFileEnAnalyzer themeFileEnAnalyzer = new ThemeFileEnAnalyzer();
    ThemeEditor themeEditor = new ThemeEditor();
    ThemeSharer themeSharer = new ThemeSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     * This function will analyze the theme file ge.
     */
    public ThemeFileGeAnalyzer getAnalyzerGe(){
        return themeFileGeAnalyzer;
    }

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     * This function will analyze the theme file en.
     */
    public ThemeFileEnAnalyzer getAnalyzerEn(){
        return themeFileEnAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public ThemeEditor getEditor(){
        return themeEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public ThemeSharer getSharer(){
        return themeSharer;
    }

    @Override
    public ThemeFileGeAnalyzer getBaseAnalyzer() {
        return themeFileGeAnalyzer;
    }

    @Override
    public ThemeEditor getBaseEditor() {
        return themeEditor;
    }

    @Override
    public ThemeSharer getBaseSharer() {
        return themeSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0"};
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.theme");
    }

    @Override
    protected AbstractBaseMod getMod() {
        return ModManager.themeMod;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }
}
