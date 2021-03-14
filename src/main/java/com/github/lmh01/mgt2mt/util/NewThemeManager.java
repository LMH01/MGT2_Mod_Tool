package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingThemes;
import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.EditThemeFiles;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NewThemeManager {
    public static ArrayList<String> arrayListThemeTranslations = new ArrayList<>();
    public static ArrayList<Integer> arrayListCompatibleGenresForTheme = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(NewThemeManager.class);

    /**
     * Adds a new theme to the theme files.
     * @param themeNameEn The english theme name
     */
    public static void addNewTheme(String themeNameEn) throws IOException {
        AnalyzeExistingThemes.analyzeThemeFiles();
        if(arrayListThemeTranslations.isEmpty()){
            Map<String, String> map = new HashMap<>();
            int currentKey = 0;
            if(arrayListThemeTranslations.isEmpty()){
                for(String string : TranslationManager.TRANSLATION_KEYS){
                    map.put("NAME " + string, themeNameEn);
                }
            }
            for(String string : arrayListThemeTranslations){
                map.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentKey], string);
                currentKey++;
            }
            EditThemeFiles.addTheme(map, arrayListCompatibleGenresForTheme);
            /*for(int i=0; i<15; i++){
                EditThemeFiles.addTheme(Utils.getThemeFile(i), themeNameEn, arrayListCompatibleGenresForTheme);
            }*/
        }else{
            int currentKey = 0;
            Map<String, String> map = new HashMap<>();
            for(String string : arrayListThemeTranslations){
                map.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentKey], string);
                currentKey++;
            }
            EditThemeFiles.addTheme(map, arrayListCompatibleGenresForTheme);
            for(int i=0; i<arrayListThemeTranslations.size(); i++){
                EditThemeFiles.addTheme(Utils.getThemeFile(i), arrayListThemeTranslations.get(i), arrayListCompatibleGenresForTheme);
            }
        }
        ChangeLog.addLogEntry(15, themeNameEn);
        WindowMain.checkActionAvailability();
    }

    /**
     * Removes the theme from the theme files.
     * @param themeNameEn The english theme name.
     */
    public static void removeTheme(String themeNameEn) throws IOException {
        int removePosition = AnalyzeExistingThemes.getPositionOfThemeInFile(themeNameEn);
        LOGGER.info("removePosition: " + removePosition);
        LOGGER.info("Name: " + themeNameEn);
        EditThemeFiles.removeTheme(removePosition);
        /*AnalyzeExistingThemes.analyzeThemeFiles();
        int position = AnalyzeExistingThemes.getPositionOfThemeInFile(themeNameEn) + 1;
        for(int i=0; i<15; i++){

            EditThemeFiles.removeTheme(Utils.getThemeFile(i), themeNameEn, position);
        }
        ChangeLog.addLogEntry(16, themeNameEn);*/
    }
}
