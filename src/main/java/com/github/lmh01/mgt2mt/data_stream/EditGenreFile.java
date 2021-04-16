package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EditGenreFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditGenreFile.class);

    /**
     * Adds a new genre to the Genres.txt file
     * @param map The values that stand in this map are used to print the file
     * @param genreTranslations The map that includes the genre name translations
     */
    public static void addGenre(Map<String, String> map, Map<String, String> genreTranslations) throws IOException {
        LOGGER.info("Adding new genre...");
        File genreFile = Utils.getGenreFile();
        if(genreFile.exists()){
            genreFile.delete();
        }
        genreFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> mapExistingGenres : AnalyzeExistingGenres.genreList){
            EditHelper.printLine("ID", mapExistingGenres, bw);
            TranslationManager.printLanguages(bw, mapExistingGenres);
            EditHelper.printLine("DATE", mapExistingGenres, bw);
            EditHelper.printLine("RES POINTS", mapExistingGenres, bw);
            EditHelper.printLine("PRICE", mapExistingGenres, bw);
            EditHelper.printLine("DEV COSTS", mapExistingGenres, bw);
            EditHelper.printLine("PIC", mapExistingGenres, bw);
            EditHelper.printLine("TGROUP", mapExistingGenres, bw);
            EditHelper.printLine("GAMEPLAY", mapExistingGenres, bw);
            EditHelper.printLine("GRAPHIC", mapExistingGenres, bw);
            EditHelper.printLine("SOUND", mapExistingGenres, bw);
            EditHelper.printLine("CONTROL", mapExistingGenres, bw);
            EditHelper.printLine("GENRE COMB", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS0", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS1", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS2", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS3", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS4", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS5", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS6", mapExistingGenres, bw);
            EditHelper.printLine("FOCUS7", mapExistingGenres, bw);
            EditHelper.printLine("ALIGN0", mapExistingGenres, bw);
            EditHelper.printLine("ALIGN1", mapExistingGenres, bw);
            EditHelper.printLine("ALIGN2", mapExistingGenres, bw);
            bw.write(System.getProperty("line.separator"));
        }
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        bw.write("[PIC]icon" + map.get("NAME EN").replaceAll(" ", "") + ".png");bw.write(System.getProperty("line.separator"));
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
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    public static boolean removeGenre(String genreName) throws IOException {
        removeGenre(AnalyzeExistingGenres.getGenreIdByName(genreName));
        EditThemeFiles.editGenreAllocation(AnalyzeExistingGenres.getGenreIdByName(genreName), false, null);
        EditGameplayFeaturesFile.removeGenreId(AnalyzeExistingGenres.getGenreIdByName(genreName));
        ImageFileHandler.removeImageFiles(genreName);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + genreName);
        return true;
    }

    /**
     * @param genreId The genre id that should be removed.
     */
    private static void removeGenre(int genreId) throws IOException {
        AnalyzeExistingGenres.analyzeGenreFile();
        LOGGER.info("Removing genre...");
        File genreFile = Utils.getGenreFile();
        if(genreFile.exists()){
            genreFile.delete();
        }
        genreFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> mapExistingGenres : AnalyzeExistingGenres.genreList){
            if(!mapExistingGenres.get("ID").equals(Integer.toString(genreId))){
                EditHelper.printLine("ID", mapExistingGenres, bw);
                TranslationManager.printLanguages(bw, mapExistingGenres);
                EditHelper.printLine("DATE", mapExistingGenres, bw);
                EditHelper.printLine("RES POINTS", mapExistingGenres, bw);
                EditHelper.printLine("PRICE", mapExistingGenres, bw);
                EditHelper.printLine("DEV COSTS", mapExistingGenres, bw);
                EditHelper.printLine("PIC", mapExistingGenres, bw);
                EditHelper.printLine("TGROUP", mapExistingGenres, bw);
                EditHelper.printLine("GAMEPLAY", mapExistingGenres, bw);
                EditHelper.printLine("GRAPHIC", mapExistingGenres, bw);
                EditHelper.printLine("SOUND", mapExistingGenres, bw);
                EditHelper.printLine("CONTROL", mapExistingGenres, bw);
                EditHelper.printLine("GENRE COMB", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS0", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS1", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS2", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS3", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS4", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS5", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS6", mapExistingGenres, bw);
                EditHelper.printLine("FOCUS7", mapExistingGenres, bw);
                EditHelper.printLine("ALIGN0", mapExistingGenres, bw);
                EditHelper.printLine("ALIGN1", mapExistingGenres, bw);
                EditHelper.printLine("ALIGN2", mapExistingGenres, bw);
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
    }
}
