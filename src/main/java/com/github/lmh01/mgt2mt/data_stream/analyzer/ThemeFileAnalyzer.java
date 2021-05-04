package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ThemeFileAnalyzer {
    public static final File FILE_THEMES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentThemesByID.txt");
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeFileAnalyzer.class);

    public static void analyzeThemeFiles(){
        try{
            ModManager.themeMod.getAnalyzerEn().analyzeFile();
            ModManager.themeMod.getAnalyzerGe().analyzeFile();
            writeHelpFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Writes a help file with themes by id.
     */
    private static void writeHelpFile() throws IOException {
        ModManager.themeMod.getAnalyzerEn().analyzeFile();
        LOGGER.info("Writing theme id help file.");
        if(FILE_THEMES_BY_ID_HELP.exists()){
            FILE_THEMES_BY_ID_HELP.delete();
        }
        FILE_THEMES_BY_ID_HELP.createNewFile();
        PrintWriter pw = new PrintWriter(FILE_THEMES_BY_ID_HELP);
        boolean firstLine = true;
        for(Map.Entry<Integer, String> entry : ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
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
     * @param themeNameEn The theme name that should be searched.
     * @return Returns the position of the specified genre in the themesNamesEn file.
     */
    public static int getPositionOfThemeInFile(String themeNameEn){
        int position = 1;
        if(Settings.enableDebugLogging){
            LOGGER.info("01 - MAP_ACTIVE_THEMES_EN.size(): " + ModManager.themeMod.getAnalyzerEn().getFileContent().size());
        }
        for(Map.Entry<Integer, String> entry: ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
            if(Settings.enableDebugLogging){
                LOGGER.info("Value: " + entry.getValue());
            }
            if(entry.getValue().contains(themeNameEn)){
                return position;
            }else{
                position++;
            }
        }
        return position;
    }

    public static Map<String, String> getSingleThemeByNameMap(String themeNameEn) throws IOException {
        Map<String, String> map = new HashMap<>();
        int positionOfThemeInFiles = getPositionOfThemeInFile(themeNameEn);
        for(String string : TranslationManager.TRANSLATION_KEYS){
            LOGGER.info("Current Translation Key: " + string);
            BufferedReader reader;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_8));
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_16LE));
            }else{
                break;
            }
            String currentLine;
            int currentLineNumber =1;
            boolean firstLine = true;
            while((currentLine = reader.readLine()) != null){
                if(firstLine){
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info("Reading file: " + string);
                }
                if(currentLineNumber == positionOfThemeInFiles){
                    if(string.equals("GE")){
                        String replaceViolenceLevel = currentLine.replace("<M1>", "").replace("<M2>", "").replace("<M3>", "").replace("<M4>", "").replace("<M5>", "");
                        String nameGe = replaceViolenceLevel.replaceAll("[0-9]", "").replaceAll("<", "").replaceAll(">", "");
                        nameGe = nameGe.trim();
                        map.put("NAME " + string, nameGe);
                        if(currentLine.contains("M1")){
                            map.put("VIOLENCE LEVEL", "1");
                        }else if(currentLine.contains("M2")){
                            map.put("VIOLENCE LEVEL", "2");
                        }else if(currentLine.contains("M3")){
                            map.put("VIOLENCE LEVEL", "3");
                        }else if(currentLine.contains("M4")){
                            map.put("VIOLENCE LEVEL", "4");
                        }else if(currentLine.contains("M5")){
                            map.put("VIOLENCE LEVEL", "5");
                        }else{
                            map.put("VIOLENCE LEVEL", "0");
                        }
                        map.put("GENRE COMB", replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim());
                        if(Settings.enableDebugLogging){
                            LOGGER.info("GENRE COMB: [" + replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim() + "]");
                        }
                    }else{
                        map.put("NAME " + string, currentLine);
                        if(Settings.enableDebugLogging){
                            LOGGER.info("NAME " + string + " | " + currentLine);
                        }
                    }
                }
                currentLineNumber++;
            }
            reader.close();
        }
        return map;
    }

    /**
     * @return Returns a array list containing all theme ids that have been found.
     */
    public static ArrayList<Integer> getThemeIdsInUse(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
            arrayList.add(entry.getKey());
        }
        return arrayList;
    }
}
