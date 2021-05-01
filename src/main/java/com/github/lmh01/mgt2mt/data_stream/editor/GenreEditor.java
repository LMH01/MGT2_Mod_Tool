package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GenreEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreEditor.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre");
    }

    @Override
    public File getFileToEdit() {
        return ModManager.genreMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        if(map.containsKey("PIC")){
            EditHelper.printLine("PIC", map, bw);
        }else{
            bw.write("[PIC]icon" + map.get("NAME EN").replaceAll(" ", "") + ".png");bw.write(System.getProperty("line.separator"));
        }
        EditHelper.printLine("TGROUP", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("CONTROL", map, bw);
        EditHelper.printLine("GENRE COMB", map, bw);
        EditHelper.printLine("FOCUS0", map, bw);
        EditHelper.printLine("FOCUS1", map, bw);
        EditHelper.printLine("FOCUS2", map, bw);
        EditHelper.printLine("FOCUS3", map, bw);
        EditHelper.printLine("FOCUS4", map, bw);
        EditHelper.printLine("FOCUS5", map, bw);
        EditHelper.printLine("FOCUS6", map, bw);
        EditHelper.printLine("FOCUS7", map, bw);
        EditHelper.printLine("ALIGN0", map, bw);
        EditHelper.printLine("ALIGN1", map, bw);
        EditHelper.printLine("ALIGN2", map, bw);
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.genreMod.getAnalyzer();
    }

    /**
     * Removes the input name from the text file
     * @param name The mod name that should be removed
     */
    @Override
    public void removeMod(String name) throws IOException {
        removeGenre(name);
    }

    private void removeGenre(String genreName) throws IOException {
        super.removeMod(genreName);
        ModManager.themeMod.getEditor().editGenreAllocation(getAnalyzer().getContentIdByName(genreName), false, null);
        ModManager.gameplayFeatureMod.getEditor().removeGenreId(getAnalyzer().getContentIdByName(genreName));
        ImageFileHandler.removeImageFiles(genreName);
    }
}
