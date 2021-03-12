package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
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
    private static final File FILE_TEMP_GENRE_FILE = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt.temp");

    /**
     * Adds a new genre to the Genres.txt file
     * @param map The values that stand is this map are used to print the file
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
            printLanguages(bw, mapExistingGenres);
            bw.write("[DATE]" + mapExistingGenres.get("DATE"));bw.write(System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + mapExistingGenres.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PRICE]" + mapExistingGenres.get("PRICE"));bw.write(System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + mapExistingGenres.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PIC]icon" + mapExistingGenres.get("NAME EN").replaceAll(" ", "") + ".png");bw.write(System.getProperty("line.separator"));
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
        printLanguages(bw, genreTranslations);
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
        LOGGER.info("Removing genre...");//TODO Image file has to be removed aswell (search where this removeGenre function is called and delete image file there)
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
                printLanguages(bw, mapExistingGenres);
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

    /**
     * Adds a new genre to the Genres.txt file with the current values that stand in {@link GenreManager}
     * @deprecated Use {@link EditGenreFile#addGenre(Map, Map)} instead.
     */
    @Deprecated
    public static void addGenre() throws IOException {
        //TODO Rewrite to use a map
        /*LOGGER.info("Adding new genre...");
        createTempFile();
        if(Settings.enableDebugLogging){
            LOGGER.info("Deleting old Genres.txt file and writing new file.");
        }
        Utils.getGenreFile().delete();
        Utils.getGenreFile().createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));

        int currentid = 0;
        String currentLine;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(Settings.enableDebugLogging) {
                LOGGER.info("currentLine: " + currentLine);
            }
            if(currentLine.contains("[ID]")){
                currentid = Integer.parseInt(currentLine.replace("[ID]", ""));
                bw.write(currentLine + System.getProperty("line.separator"));
            }else if(currentLine.contains("[GENRE COMB]")){
                if(GenreManager.getCompatibleGenresIds().contains(Integer.toString(currentid))){
                    bw.write(currentLine + "<" + GenreManager.id + ">" + System.getProperty("line.separator"));
                    LOGGER.info("Found compatible genre id in file to write: " + currentid);
                }else{
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }else{
                bw.write(currentLine + System.getProperty("line.separator"));
            }
        }
        br.close();
        FILE_TEMP_GENRE_FILE.delete();
        LOGGER.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
        //Print new genre:
        bw.write("[ID]" + GenreManager.id + System.getProperty("line.separator"));
        printLanguages(bw, false, null);
        bw.write("[DATE]" + GenreManager.unlockMonth + " " + GenreManager.unlockYear + System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + GenreManager.researchPoints + System.getProperty("line.separator"));
        bw.write("[PRICE]" + GenreManager.price + System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + GenreManager.devCost + System.getProperty("line.separator"));
        if(GenreManager.useDefaultImageFile){
            bw.write("[PIC]iconSkill.png" + System.getProperty("line.separator"));
        }else{
            bw.write("[PIC]" + GenreManager.imageFileName + ".png" + System.getProperty("line.separator"));
        }
        //noinspection SpellCheckingInspection
        bw.write("[TGROUP]" + getTargetGroup() + System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + GenreManager.gameplay + System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + GenreManager.graphic + System.getProperty("line.separator"));
        bw.write("[SOUND]" + GenreManager.sound + System.getProperty("line.separator"));
        bw.write("[CONTROL]" + GenreManager.control + System.getProperty("line.separator"));
        bw.write("[GENRE COMB]" + GenreManager.getCompatibleGenresIds() + System.getProperty("line.separator"));
        bw.write("[DESIGN1]" + GenreManager.design1 + System.getProperty("line.separator"));
        bw.write("[DESIGN2]" + GenreManager.design2 + System.getProperty("line.separator"));
        bw.write("[DESIGN3]" + GenreManager.design3 + System.getProperty("line.separator"));
        bw.write("[DESIGN4]" + GenreManager.design4 + System.getProperty("line.separator"));
        bw.write("[DESIGN5]" + GenreManager.design5 + System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator") + "[EOF]");
        bw.close();
        LOGGER.info("Temp file has been filled. Renaming...");*/
    }


    /**
     * @param genreId The genre id that should be removed.
     */
    /*public static void removeGenre(int genreId) throws IOException {
        //TODO Rewrite to use a map
        LOGGER.info("Removing genre with id [" + genreId + "] from Genres.txt");
        createTempFile();
        LOGGER.info("Deleting old Genres.txt file and writing new file.");
        Utils.getGenreFile().delete();
        Utils.getGenreFile().createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));

        String currentLine;
        boolean firstLine = true;
        int linesToSkip = 0;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            boolean skipNormalWrite = false;
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(currentLine.equals("[ID]" + genreId)){
                linesToSkip = Utils.genreLineNumbers;//This is how many line the genre has that should be removed
            }else if(currentLine.contains("[GENRE COMB]")){
                if(currentLine.contains("<" + genreId + ">")){
                    bw.write(currentLine.replace("<" + genreId + ">", "" + System.getProperty("line.separator")));
                    skipNormalWrite = true;
                }
            }
            if(linesToSkip>0){
                while(linesToSkip>0){
                    currentLine = br.readLine();
                    if(currentLine.contains("[NAME EN]")){
                        LOGGER.info("Found [NAME EN] for genre to remove. Trying to remove image files.");
                        String genreName = currentLine.replace("[NAME EN]", "");
                        ImageFileHandler.removeImageFiles(genreName, genreId);
                    }
                    if(Settings.enableDebugLogging){
                        LOGGER.info("CurrentLines for genre that should be removed: " + currentLine);
                    }
                    linesToSkip--;
                }
            }else{
                if(!skipNormalWrite){
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }
        }
        bw.write("[EOF]");
        br.close();
        bw.close();
        FILE_TEMP_GENRE_FILE.delete();
        LOGGER.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
    }*/
    /**
     * Creates the temp file Genres.txt.temp which will be read later to add a new genre
     */
    private static void createTempFile() throws IOException {
        if(FILE_TEMP_GENRE_FILE.exists()){
            FILE_TEMP_GENRE_FILE.delete();
        }
        FILE_TEMP_GENRE_FILE.createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        LOGGER.info("Writing current content to temp file.");
        String currentLine;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(Settings.enableDebugLogging) {
                LOGGER.info("currentLine: " + currentLine);
            }
            if(!currentLine.equals("[EOF]")){
                bw.write(currentLine + System.getProperty("line.separator"));
            }
        }
        br.close();
        bw.close();
        LOGGER.info("Temp file has been created.");
    }

    private static void printLanguages(BufferedWriter bw, Map<String, String> map) throws IOException {
        for(int i=0; i<TranslationManager.TRANSLATION_KEYS.length; i++){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME " + TranslationManager.TRANSLATION_KEYS[i])){
                    bw.write("[NAME " + TranslationManager.TRANSLATION_KEYS[i] + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
                if(entry.getKey().equals("DESC " + TranslationManager.TRANSLATION_KEYS[i])){
                    bw.write("[DESC " + TranslationManager.TRANSLATION_KEYS[i] + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
            }
        }
    }
    private static String getTargetGroup(){//TODO Remove function
        /*String targetGroups = "";
        if(GenreManager.targetGroupKid){
            targetGroups = targetGroups + "<KID>";
        }
        if(GenreManager.targetGroupTeen){
            targetGroups = targetGroups + "<TEEN>";
        }
        if(GenreManager.targetGroupAdult){
            targetGroups = targetGroups + "<ADULT>";
        }
        if(GenreManager.targetGroupSenior){
            targetGroups = targetGroups + "<OLD>";
        }
        return targetGroups;*/
        return null;
    }
}
