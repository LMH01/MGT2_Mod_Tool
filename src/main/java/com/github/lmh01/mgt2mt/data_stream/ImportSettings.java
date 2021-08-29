package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportSettings{//TODO Rewrite to use .toml file
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSettings.class);

    /**
     * @param path Path to the settings file
     * @return true if import was successful
     */
    public static boolean Import(Path path) {
        LOGGER.info("Starting settings import process...");
        try {
            LOGGER.info("Scanning for File '" + path + "'...");
            File file = path.toFile();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            LOGGER.info("Beginning to import settings from file: " + file);
            int setting = 1;
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                switch(setting) {
                    case 1:
                        Settings.mgt2Path = Paths.get(currentLine); break;
                    case 2:
                        if(currentLine.equals("true")){
                            Settings.enableDisclaimerMessage = true;
                        }else if(currentLine.equals("false")){
                            Settings.enableDisclaimerMessage = false;
                        } break;
                    case 3:
                        if(currentLine.equals("true")){
                            Settings.enableDebugLogging = true;
                        }else if(currentLine.equals("false")){
                            Settings.enableDebugLogging = false;
                        } break;
                    case 4:
                        if(currentLine.equals("true")){
                            Settings.disableSafetyFeatures = true;
                        }else if(currentLine.equals("false")){
                            Settings.disableSafetyFeatures = false;
                        } break;
                    case 5:
                        Settings.steamLibraryFolder = Paths.get(currentLine); break;
                    case 6:
                        if(currentLine.equals("true")){
                            Settings.enableCustomFolder = true;
                        }else if(currentLine.equals("false")){
                            Settings.enableCustomFolder = false;
                        } break;
                    case 7:
                        if(currentLine.equals("true")){
                            Settings.enableGenreNameTranslationInfo = true;
                        }else if(currentLine.equals("false")){
                            Settings.enableGenreNameTranslationInfo = false;
                        } break;
                    case 8:
                        if(currentLine.equals("true")){
                            Settings.enableGenreDescriptionTranslationInfo = true;
                        }else if(currentLine.equals("false")){
                            Settings.enableGenreDescriptionTranslationInfo = false;
                        } break;
                    case 9:
                        Settings.language = currentLine;
                        break;
                    case 10:
                        Settings.updateBranch = currentLine;
                        break;
                    case 11:
                        if(currentLine.equals("true")){
                            Settings.saveLogs = true;
                        }else if(currentLine.equals("false")){
                            Settings.saveLogs = false;
                        }
                        break;
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info("Imported Setting (" + setting + "): " + currentLine);
                }
                ++setting;
            }
            if(Settings.enableDebugLogging){
                LOGGER.info("Import Complete!");
            }
            reader.close();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException var6) {
            var6.printStackTrace();
            LOGGER.info("Unable to import settings: File not found! Using default settings!");
            LogFile.write("Something went wrong wile importing the settings: ");
            LogFile.printStacktrace(var6);
            Settings.resetSettings();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}