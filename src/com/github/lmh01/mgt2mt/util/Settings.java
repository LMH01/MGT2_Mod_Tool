package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ExportSettings;
import com.github.lmh01.mgt2mt.dataStream.ImportSettings;

import java.awt.*;

public class Settings {
    public static String mgt2FilePath = "";
    public static String languageToAdd = "";

    public static void resetSettings(){
        mgt2FilePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";
        languageToAdd = "English";
    }
    public static void importCustomSettings(String filePath){
        ImportSettings.Import(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//settings.txt", true);
    }
    public static void importSettings(){
        ImportSettings.Import(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//settings.txt", false);
    }
    public static  void exportSettings(){
        ExportSettings.export();
    }
    public static void setMgt2FilePath(){
        new FileDialog((java.awt.Frame) null).setVisible(true);
    }
}
