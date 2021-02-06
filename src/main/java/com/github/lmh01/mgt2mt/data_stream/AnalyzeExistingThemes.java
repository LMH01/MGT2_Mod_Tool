package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeExistingThemes {

    public static final HashMap<Integer,String> MAP_ACTIVE_THEMES_GE = new HashMap<>();
    public static final HashMap<Integer,String> MAP_ACTIVE_THEMES_EN = new HashMap<>();
    public static final File FILE_THEMES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentThemesByID.txt");
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingThemes.class);

    //The theme combinations are only changed in the Themes_GE.txt file
    public static void analyzeThemeFiles() throws IOException {
        analyzeThemesFileGE();
        analyzeThemesFileEN();
        writeHelpFile();
    }

    private static void analyzeThemesFileGE() throws IOException {
        MAP_ACTIVE_THEMES_GE.clear();
        File themesGeFile = Utils.getThemesGeFile();
        LOGGER.info("Scanning for themes in file: " + themesGeFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(themesGeFile), StandardCharsets.UTF_16LE));
        String currentLine;
        boolean firstLine = true;
        int themeID = 0;//Theme id is added to make the sorting and selection of the compatible theme easier
        while((currentLine = reader.readLine()) != null){
            if(firstLine){
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(!currentLine.equals("")){
                MAP_ACTIVE_THEMES_GE.put(themeID, currentLine);
                if(Settings.enableDebugLogging){
                    LOGGER.info("Added entry to array map MAP_ACTIVE_THEMES_GE: " + "[" + themeID + "] " + currentLine);
                    LOGGER.info("Entry in map: " + MAP_ACTIVE_THEMES_GE.get(themeID));
                }
                themeID++;
            }
        }
        reader.close();
        LOGGER.info("Analyzing of themes(en) complete. Found: " + MAP_ACTIVE_THEMES_GE.size());
    }

    private static void analyzeThemesFileEN() throws IOException {
        MAP_ACTIVE_THEMES_EN.clear();
        File themesEnFile = Utils.getThemesEnFile();
        LOGGER.info("Scanning for themes in file: " + themesEnFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(themesEnFile), StandardCharsets.UTF_16LE));
        String currentLine;
        boolean firstLine = true;
        int themeID = 0;//Theme id is added to make the sorting and selection of the compatible theme easier
        while((currentLine = reader.readLine()) != null){
            if(firstLine){
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(!currentLine.equals("")){
                MAP_ACTIVE_THEMES_EN.put(themeID, currentLine);
                if(Settings.enableDebugLogging){
                    LOGGER.info("Added entry to array map MAP_ACTIVE_THEMES_GE: " + "[" + themeID + "] " + currentLine);
                    LOGGER.info("Entry in map: " + MAP_ACTIVE_THEMES_EN.get(themeID));
                }
                themeID++;
            }
        }
        reader.close();
        LOGGER.info("Analyzing of themes(en) complete. Found: " + MAP_ACTIVE_THEMES_EN.size());
    }

    /**
     * Writes a help file with themes by id.
     */
    private static void writeHelpFile() throws IOException {
        LOGGER.info("Writing theme id help file.");
        if(FILE_THEMES_BY_ID_HELP.exists()){
            FILE_THEMES_BY_ID_HELP.delete();
        }
        FILE_THEMES_BY_ID_HELP.createNewFile();
        PrintWriter pw = new PrintWriter(FILE_THEMES_BY_ID_HELP);
        LOGGER.info("MAP_ACTIVE_THEMES_EN.size() = " + MAP_ACTIVE_THEMES_EN.size());
        boolean firstLine = true;
        for(Map.Entry<Integer, String> entry : MAP_ACTIVE_THEMES_EN.entrySet()){
            if(firstLine){
                pw.print(entry.getKey() + " - " + entry.getValue());
                firstLine = false;
            }else{
                pw.print("\n" + entry.getKey() + " - " + entry.getValue());
            }
            if(Settings.enableDebugLogging){
                LOGGER.info("Current entryKey: " + entry.getKey());
                LOGGER.info(entry.getKey() + " - " + entry.getValue());
            }
        }
        pw.close();
        if(Settings.enableDebugLogging){
            LOGGER.info("file created.");
        }
    }

    /**
     * @return Returns a string containing all active themes sorted by alphabet.
     */
    public static String[] getThemesByAlphabet(){
        ArrayList<String> arrayListAvailableThemesSorted = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            if(Settings.enableDebugLogging){
                LOGGER.info("Adding element to list: " + entry.getValue());
            }
            arrayListAvailableThemesSorted.add(entry.getValue());
        }
        Collections.sort(arrayListAvailableThemesSorted);
        String[] string = new String[arrayListAvailableThemesSorted.size()];
        arrayListAvailableThemesSorted.toArray(string);
        return string;
    }
}
