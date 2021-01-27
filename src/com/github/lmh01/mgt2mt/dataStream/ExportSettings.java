package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.helpers.DebugHelper;
import com.github.lmh01.mgt2mt.util.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ExportSettings {
    public static void export() {
        try {

            String directoryName = System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//";
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryName + "/" + "settings.txt");
            DebugHelper.sendInfo("Creating settings.txt");
            DebugHelper.sendInfo(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
            file.createNewFile();
            DebugHelper.sendInfo("Successfully created the file settings.txt");
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            DebugHelper.sendInfo("Writing to file...");
            pw.print(Settings.mgt2FilePath + "\n" +
                    Settings.languageToAdd);
            pw.close();
            DebugHelper.sendInfo(Settings.mgt2FilePath + "\n" + Settings.languageToAdd);
            DebugHelper.sendInfo("Writing to file successfull!");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
