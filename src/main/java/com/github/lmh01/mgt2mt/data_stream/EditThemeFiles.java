package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public class EditThemeFiles {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditThemeFiles.class);
    /**
     * Add the specified theme to the specified file.
     * @param themeFile The file where the theme should be added.
     * @param themeName The name of the new theme.
     * @param addTheme True if the theme should be added. False if it should be removed.
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     * @param position The position where the theme file is positioned that should be removed.
     */
    private static void addOrRemoveTheme(File themeFile, String themeName, boolean addTheme, ArrayList<Integer> arrayListCompatibleGenres, int position) throws IOException {
        //TODO Rewrite to use maps
        File fileThemeFileTemp = new File(themeFile.getPath() + ".temp");
        if(themeFile.exists()){
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(themeFile), StandardCharsets.UTF_16LE));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileThemeFileTemp), StandardCharsets.UTF_16LE));
            boolean firstLine = true;
            int lineNumber = 1;
            String currentLine;
            while((currentLine = br.readLine()) != null){
                if(firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                }
                if(!firstLine){
                    if(addTheme){
                        bw.write(System.getProperty("line.separator"));
                    }else{
                        if(lineNumber != position){
                            bw.write(System.getProperty("line.separator"));
                        }
                    }
                }
                firstLine = false;
                if(addTheme){
                    bw.write(currentLine);
                }else{
                    if(lineNumber != position){
                        bw.write(currentLine);
                    }
                }
                lineNumber++;
            }
            if(addTheme){
                if(themeFile.getName().equals("Themes_GE.txt")){//Writes information for compatible genres only to the german theme file.
                    StringBuilder stringCompatibleGenres = new StringBuilder();
                    for (Integer arrayListCompatibleGenre : arrayListCompatibleGenres) {
                        stringCompatibleGenres.append("<").append(arrayListCompatibleGenre).append(">");
                    }
                    bw.write(System.getProperty("line.separator") + themeName + " " + stringCompatibleGenres);
                }else{
                    bw.write(System.getProperty("line.separator") + themeName);
                }
            }
            br.close();
            bw.close();
            themeFile.delete();
            fileThemeFileTemp.renameTo(themeFile);
        }
    }

    /**
     * Removes the specified theme from the specified file.
     * @param themeFile The file where the theme should be added.
     * @param themeName The name of the new theme.
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     */
    public static void addTheme(File themeFile, String themeName, ArrayList arrayListCompatibleGenres) throws IOException {
        addOrRemoveTheme(themeFile, themeName, true, arrayListCompatibleGenres, 0);
    }

    /**
     * Adds the specified theme to the specified file.
     * @param themeFile The file where the theme should be added.
     * @param themeName The name of the new theme.
     * @param position The position where the theme file is positioned that should be removed.
     */
    public static void removeTheme(File themeFile, String themeName, int position) throws IOException {
        addOrRemoveTheme(themeFile, themeName, false, new ArrayList<>(), position);
    }

    /**
     * Adds all themes that are currently in this map: MAP_COMPATIBLE_THEMES (NewGenreManager)
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public static void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws IOException {
        File fileTopicsGeTemp = new File(Utils.getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt.temp");
        fileTopicsGeTemp.createNewFile();
        Backup.createBackup(Utils.getThemesGeFile());
        LOGGER.info("Themes_GE.txt.temp has been created");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemesGeFile()), StandardCharsets.UTF_16LE));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTopicsGeTemp), StandardCharsets.UTF_16LE));
        String currentLine;
        int currentLineNumber = 0;
        boolean firstLine = true;
        bw.write("\ufeff");
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
