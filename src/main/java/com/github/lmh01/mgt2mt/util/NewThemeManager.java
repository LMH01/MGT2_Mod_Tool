package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingThemes;
import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.EditThemeFiles;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class NewThemeManager {
    public static ArrayList<String> arrayListThemeTranslations = new ArrayList<>();
    public static ArrayList<Integer> arrayListCompatibleGenresForTheme = new ArrayList<>();
    // --Commented out by Inspection (08.03.2021 13:25):private static final Logger LOGGER = LoggerFactory.getLogger(NewThemeManager.class);

    /**
     * Adds a new theme to the theme files.
     * @param themeNameEn The english theme name
     */
    public static void addNewTheme(String themeNameEn) throws IOException {
        AnalyzeExistingThemes.analyzeThemeFiles();
        if(arrayListThemeTranslations.isEmpty()){
            for(int i=0; i<15; i++){
                EditThemeFiles.addTheme(Utils.getThemeFile(i), themeNameEn, arrayListCompatibleGenresForTheme);
            }
        }else{
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
        AnalyzeExistingThemes.analyzeThemeFiles();
        int position = AnalyzeExistingThemes.getPositionOfThemeInFile(themeNameEn) + 1;
        for(int i=0; i<15; i++){
            EditThemeFiles.removeTheme(Utils.getThemeFile(i), themeNameEn, position);
        }
        ChangeLog.addLogEntry(16, themeNameEn);
    }
}
