package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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
     * @param removeThemePosition The position where the theme stands in the theme files
     */
    public static void removeTheme(int removeThemePosition) throws IOException {
        editThemeFiles(null, null, false, removeThemePosition);
    }

    /**
     * Adds/removes a theme to the theme files
     * @param map The map containing the translations
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     * @param addTheme True when the theme should be added. False when the theme should be removed.
     * @param removeThemePosition The position where the theme is positioned that should be removed.
     */
    public static void editThemeFiles(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, boolean addTheme, int removeThemePosition) throws IOException {
        for(String string : TranslationManager.TRANSLATION_KEYS){
            File themeFile = Utils.getThemeFile(string);
            Map<Integer, String> currentThemeFileContent;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                currentThemeFileContent = Utils.getContentFromFile(themeFile, "UTF_8BOM");
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                currentThemeFileContent = Utils.getContentFromFile(themeFile, "UTF_16LE");
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
            for(int i=0; i<currentThemeFileContent.size(); i++){
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
                    LOGGER.info("current string: " + string);
                    bw.write(map.get("NAME " + string));
                }
            }
            bw.close();
        }
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public static void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws IOException {
        File fileTopicsGeTemp = new File(Utils.getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt.temp");
        fileTopicsGeTemp.createNewFile();
        Backup.createBackup(Utils.getThemesGeFile());
        LOGGER.info("Themes_GE.txt.temp has been created");//TODO Rewrite to use AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemesGeFile()), StandardCharsets.UTF_16LE));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTopicsGeTemp), StandardCharsets.UTF_16LE));
        String currentLine;
        int currentLineNumber = 0;
        boolean firstLine = true;
        while ((currentLine = br.readLine()) != null) {
            if (firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
            }
            if (addGenreID) {
                if (compatibleThemeIds.contains(currentLineNumber)) {
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(currentLineNumber + " - Y: " + currentLine);
                    }
                    if (!firstLine) {
                        bw.write(System.getProperty("line.separator"));
                    }
                    bw.write(currentLine + "<" + genreID + ">");
                } else {
                    if (!firstLine) {
                        bw.write(System.getProperty("line.separator"));
                    }
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(currentLineNumber + " - N: " + currentLine);
                    }
                    bw.write(currentLine);
                }
            } else {
                if (!firstLine) {
                    bw.write(System.getProperty("line.separator"));
                }
                bw.write(currentLine.replace("<" + genreID + ">", ""));
            }
            firstLine = false;
            currentLineNumber++;
        }
        br.close();
        bw.close();
        Utils.getThemesGeFile().delete();
        fileTopicsGeTemp.renameTo(Utils.getThemesGeFile());
        if (addGenreID) {
            ChangeLog.addLogEntry(2, Integer.toString(genreID));
        } else {
            ChangeLog.addLogEntry(3, Integer.toString(genreID));
        }
    }
}
