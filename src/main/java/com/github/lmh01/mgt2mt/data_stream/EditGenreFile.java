package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
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
            bw.write("[DESIGN1]" + mapExistingGenres.get("DESIGN1"));bw.write(System.getProperty("line.separator"));
            bw.write("[DESIGN2]" + mapExistingGenres.get("DESIGN2"));bw.write(System.getProperty("line.separator"));
            bw.write("[DESIGN3]" + mapExistingGenres.get("DESIGN3"));bw.write(System.getProperty("line.separator"));
            bw.write("[DESIGN4]" + mapExistingGenres.get("DESIGN4"));bw.write(System.getProperty("line.separator"));
            bw.write("[DESIGN5]" + mapExistingGenres.get("DESIGN5"));bw.write(System.getProperty("line.separator"));
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
        bw.write("[DESIGN1]" + map.get("DESIGN1"));bw.write(System.getProperty("line.separator"));
        bw.write("[DESIGN2]" + map.get("DESIGN2"));bw.write(System.getProperty("line.separator"));
        bw.write("[DESIGN3]" + map.get("DESIGN3"));bw.write(System.getProperty("line.separator"));
        bw.write("[DESIGN4]" + map.get("DESIGN4"));bw.write(System.getProperty("line.separator"));
        bw.write("[DESIGN5]" + map.get("DESIGN5"));bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * @param genreId The genre id that should be removed.
     */
    public static void removeGenre(int genreId) throws IOException {
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
                bw.write("[DESIGN1]" + mapExistingGenres.get("DESIGN1"));bw.write(System.getProperty("line.separator"));
                bw.write("[DESIGN2]" + mapExistingGenres.get("DESIGN2"));bw.write(System.getProperty("line.separator"));
                bw.write("[DESIGN3]" + mapExistingGenres.get("DESIGN3"));bw.write(System.getProperty("line.separator"));
                bw.write("[DESIGN4]" + mapExistingGenres.get("DESIGN4"));bw.write(System.getProperty("line.separator"));
                bw.write("[DESIGN5]" + mapExistingGenres.get("DESIGN5"));bw.write(System.getProperty("line.separator"));
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
    }
}
