package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EditThemeFiles {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditThemeFiles.class);

    /**
     * Adds a new theme to the theme files
     * @param map The map containing the theme translations
     * @param arrayListCompatibleGenres The array list containing the compatible genres
     */
    public static void addTheme(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres) throws IOException {
        editThemeFiles(map, arrayListCompatibleGenres, true, 0);
    }

    /**
     * Removes a theme from the theme files.
     * @param themeNameEn The theme name that should be removed
     */
    public static boolean removeTheme(String themeNameEn) throws IOException {
        return editThemeFiles(null, null, false, AnalyzeExistingThemes.getPositionOfThemeInFile(themeNameEn));
    }

    /**
     * Adds/removes a theme to the theme files
     * @param map The map containing the translations
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     * @param addTheme True when the theme should be added. False when the theme should be removed.
     * @param removeThemePosition The position where the theme is positioned that should be removed.
     */
    public static boolean editThemeFiles(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, boolean addTheme, int removeThemePosition) throws IOException {
        for(String string : TranslationManager.TRANSLATION_KEYS){
            File themeFile = Utils.getThemeFile(string);
            Map<Integer, String> currentThemeFileContent;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_8BOM");
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_16LE");
            }else{
                break;
            }
            if(themeFile.exists()){
                themeFile.delete();
            }
            themeFile.createNewFile();
            BufferedWriter bw;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                bw.write("\ufeff");//Makes the file UTF8 BOM
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
            }else{
                break;
            }
            int currentLine = 1;
            boolean firstLine = true;
            for(int i = 0; i< Objects.requireNonNull(currentThemeFileContent).size(); i++){
                if(!firstLine){
                    if(!addTheme){
                        if(currentLine != removeThemePosition) {
                            bw.write(System.getProperty("line.separator"));
                        }
                    }else{
                        bw.write(System.getProperty("line.separator"));
                    }
                }else{
                    firstLine = false;
                }
                if(addTheme || currentLine != removeThemePosition) {
                    bw.write(currentThemeFileContent.get(currentLine));
                }
                currentLine++;
            }
            try{
                if(addTheme){
                    bw.write(System.getProperty("line.separator"));
                    if(string.equals("GE")){
                        StringBuilder genreIdsToPrint = new StringBuilder();
                        genreIdsToPrint.append(" ");
                        bw.write(map.get("NAME GE"));
                        for(Integer genreId : arrayListCompatibleGenres){
                            genreIdsToPrint.append("<").append(genreId).append(">");
                        }
                        bw.write(genreIdsToPrint.toString());
                    }else{
                        if(Settings.enableDebugLogging){
                            LOGGER.info("current string: " + string);
                        }
                        bw.write(map.get("NAME " + string));
                    }
                }
            }catch(NullPointerException ignored){

            }
            bw.close();
        }
        WindowMain.checkActionAvailability();
        return true;
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public static void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws IOException {
        AnalyzeExistingThemes.analyzeThemeFiles();
        File fileTopicsGe = Utils.getThemesGeFile();
        if(fileTopicsGe.exists()){
            fileTopicsGe.delete();
        }
        fileTopicsGe.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTopicsGe), StandardCharsets.UTF_16LE));
        Map<Integer, String> map = AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE;
        boolean firstLine = true;
        for(int i=0; i<map.size(); i++){
            if(addGenreID){
                if(compatibleThemeIds.contains(i)){
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(i + " - Y: " + map.get(i));
                    }
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }
                    bw.write(map.get(i) + "<" + genreID + ">");
                }else{
                    if (!firstLine) {
                        bw.write(System.getProperty("line.separator"));
                    }
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(i + " - N: " + map.get(i));
                    }
                    bw.write(map.get(i));
                }
            }else{
                if (!firstLine) {
                    bw.write(System.getProperty("line.separator"));
                }
                bw.write(map.get(i).replace("<" + genreID + ">", ""));
            }
            firstLine = false;
        }
        bw.close();
        if (addGenreID) {
            ChangeLog.addLogEntry(2, Integer.toString(genreID));
        } else {
            ChangeLog.addLogEntry(3, Integer.toString(genreID));
        }
    }
}
