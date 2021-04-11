package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.windows.WindowMain;
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
            bw.write("[ID]" + mapExistingGenres.get("ID"));bw.write(System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, mapExistingGenres);
            bw.write("[DATE]" + mapExistingGenres.get("DATE"));bw.write(System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + mapExistingGenres.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PRICE]" + mapExistingGenres.get("PRICE"));bw.write(System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + mapExistingGenres.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PIC]" + mapExistingGenres.get("PIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[TGROUP]" + mapExistingGenres.get("TGROUP"));bw.write(System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + mapExistingGenres.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + mapExistingGenres.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[SOUND]" + mapExistingGenres.get("SOUND"));bw.write(System.getProperty("line.separator"));
            bw.write("[CONTROL]" + mapExistingGenres.get("CONTROL"));bw.write(System.getProperty("line.separator"));
            bw.write("[GENRE COMB]" + mapExistingGenres.get("GENRE COMB"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS0]" + mapExistingGenres.get("FOCUS0"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS1]" + mapExistingGenres.get("FOCUS1"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS2]" + mapExistingGenres.get("FOCUS2"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS3]" + mapExistingGenres.get("FOCUS3"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS4]" + mapExistingGenres.get("FOCUS4"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS5]" + mapExistingGenres.get("FOCUS5"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS6]" + mapExistingGenres.get("FOCUS6"));bw.write(System.getProperty("line.separator"));
            bw.write("[FOCUS7]" + mapExistingGenres.get("FOCUS7"));bw.write(System.getProperty("line.separator"));
            bw.write("[ALIGN0]" + mapExistingGenres.get("ALIGN0"));bw.write(System.getProperty("line.separator"));
            bw.write("[ALIGN1]" + mapExistingGenres.get("ALIGN1"));bw.write(System.getProperty("line.separator"));
            bw.write("[ALIGN2]" + mapExistingGenres.get("ALIGN2"));bw.write(System.getProperty("line.separator"));
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[ID]" + map.get("ID"));bw.write(System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, genreTranslations);
        bw.write("[DATE]" + map.get("DATE"));bw.write(System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + map.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
        bw.write("[PRICE]" + map.get("PRICE"));bw.write(System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + map.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
        bw.write("[PIC]icon" + map.get("NAME EN").replaceAll(" ", "") + ".png");bw.write(System.getProperty("line.separator"));
        bw.write("[TGROUP]" + map.get("TGROUP"));bw.write(System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + map.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + map.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
        bw.write("[SOUND]" + map.get("SOUND"));bw.write(System.getProperty("line.separator"));
        bw.write("[CONTROL]" + map.get("CONTROL"));bw.write(System.getProperty("line.separator"));
        bw.write("[GENRE COMB]" + map.get("GENRE COMB"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS0]" + map.get("FOCUS0"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS1]" + map.get("FOCUS1"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS2]" + map.get("FOCUS2"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS3]" + map.get("FOCUS3"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS4]" + map.get("FOCUS4"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS5]" + map.get("FOCUS5"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS6]" + map.get("FOCUS6"));bw.write(System.getProperty("line.separator"));
        bw.write("[FOCUS7]" + map.get("FOCUS7"));bw.write(System.getProperty("line.separator"));
        bw.write("[ALIGN0]" + map.get("ALIGN0"));bw.write(System.getProperty("line.separator"));
        bw.write("[ALIGN1]" + map.get("ALIGN1"));bw.write(System.getProperty("line.separator"));
        bw.write("[ALIGN2]" + map.get("ALIGN2"));bw.write(System.getProperty("line.separator"));
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
                bw.write("[ID]" + mapExistingGenres.get("ID"));bw.write(System.getProperty("line.separator"));
                TranslationManager.printLanguages(bw, mapExistingGenres);
                bw.write("[DATE]" + mapExistingGenres.get("DATE"));bw.write(System.getProperty("line.separator"));
                bw.write("[RES POINTS]" + mapExistingGenres.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
                bw.write("[PRICE]" + mapExistingGenres.get("PRICE"));bw.write(System.getProperty("line.separator"));
                bw.write("[DEV COSTS]" + mapExistingGenres.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
                bw.write("[PIC]" + mapExistingGenres.get("PIC"));bw.write(System.getProperty("line.separator"));
                bw.write("[TGROUP]" + mapExistingGenres.get("TGROUP"));bw.write(System.getProperty("line.separator"));
                bw.write("[GAMEPLAY]" + mapExistingGenres.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
                bw.write("[GRAPHIC]" + mapExistingGenres.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
                bw.write("[SOUND]" + mapExistingGenres.get("SOUND"));bw.write(System.getProperty("line.separator"));
                bw.write("[CONTROL]" + mapExistingGenres.get("CONTROL"));bw.write(System.getProperty("line.separator"));
                bw.write("[GENRE COMB]" + mapExistingGenres.get("GENRE COMB"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS0]" + mapExistingGenres.get("FOCUS0"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS1]" + mapExistingGenres.get("FOCUS1"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS2]" + mapExistingGenres.get("FOCUS2"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS3]" + mapExistingGenres.get("FOCUS3"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS4]" + mapExistingGenres.get("FOCUS4"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS5]" + mapExistingGenres.get("FOCUS5"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS6]" + mapExistingGenres.get("FOCUS6"));bw.write(System.getProperty("line.separator"));
                bw.write("[FOCUS7]" + mapExistingGenres.get("FOCUS7"));bw.write(System.getProperty("line.separator"));
                bw.write("[ALIGN0]" + mapExistingGenres.get("ALIGN0"));bw.write(System.getProperty("line.separator"));
                bw.write("[ALIGN1]" + mapExistingGenres.get("ALIGN1"));bw.write(System.getProperty("line.separator"));
                bw.write("[ALIGN2]" + mapExistingGenres.get("ALIGN2"));bw.write(System.getProperty("line.separator"));
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
    }
}
