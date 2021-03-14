package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("JavaDoc")
public class SharingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingHandler.class);
    public static final String[] GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.0", "1.7.1"};
    public static final String[] PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.6.0", "1.7.0", "1.7.1"};

    /**
     * Exports the specified genre.
     * @param genreId The genre id
     * @param genreName The genre name
     * @return Returns true when the genre has been exported successfully. Returns false when the genre has already been exported.
     * @throws IOException
     */
    public static boolean exportGenre(int genreId, String genreName) throws IOException {
        final String EXPORTED_GENRE_MAIN_FOLDER_PATH = Settings.MGT2_MOD_MANAGER_PATH + "//Export//Genres//" + genreName;
        final String EXPORTED_GENRE_DATA_FOLDER_PATH = EXPORTED_GENRE_MAIN_FOLDER_PATH + "//DATA//";
        File fileDataFolder = new File(EXPORTED_GENRE_DATA_FOLDER_PATH);
        File fileExportedGenre = new File(EXPORTED_GENRE_MAIN_FOLDER_PATH + "//genre.txt");
        File fileExportedGenreIcon = new File(EXPORTED_GENRE_DATA_FOLDER_PATH + "//icon.png");
        File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + "icon" + AnalyzeExistingGenres.genreList.get(genreId).get("NAME EN") + ".png");
        File fileGenreScreenshotsToExport = new File(Utils.getMGT2ScreenshotsPath() + genreId);
        if(!fileExportedGenreIcon.exists()){
            fileDataFolder.mkdirs();
        }
        if(fileExportedGenreIcon.exists()){
            return false;
        }
        if(Settings.enableDebugLogging){
            LOGGER.info("Copying image files to export folder...");
        }
        Files.copy(Paths.get(fileGenreIconToExport.getPath()),Paths.get(fileExportedGenreIcon.getPath()));
        Utils.copyDirectory(fileGenreScreenshotsToExport.toPath().toString(), EXPORTED_GENRE_DATA_FOLDER_PATH + "//screenshots//");
        fileExportedGenre.createNewFile();
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileExportedGenre), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.print("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.print("[GENRE START]" + System.getProperty("line.separator"));
        for(String translationKey : TranslationManager.TRANSLATION_KEYS){
            bw.print("[NAME " + translationKey + "]" + AnalyzeExistingGenres.genreList.get(genreId).get("NAME " + translationKey)  + System.getProperty("line.separator"));
            bw.print("[DESC " + translationKey + "]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESC " + translationKey)  + System.getProperty("line.separator"));
        }
        bw.print("[DATE]" + AnalyzeExistingGenres.genreList.get(genreId).get("DATE") + System.getProperty("line.separator"));
        bw.print("[RES POINTS]" + AnalyzeExistingGenres.genreList.get(genreId).get("RES POINTS") + System.getProperty("line.separator"));
        bw.print("[PRICE]" + AnalyzeExistingGenres.genreList.get(genreId).get("PRICE") + System.getProperty("line.separator"));
        bw.print("[DEV COSTS]" + AnalyzeExistingGenres.genreList.get(genreId).get("DEV COSTS") + System.getProperty("line.separator"));
        bw.print("[TGROUP]" + AnalyzeExistingGenres.genreList.get(genreId).get("TGROUP") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAY]" + AnalyzeExistingGenres.genreList.get(genreId).get("GAMEPLAY") + System.getProperty("line.separator"));
        bw.print("[GRAPHIC]" + AnalyzeExistingGenres.genreList.get(genreId).get("GRAPHIC") + System.getProperty("line.separator"));
        bw.print("[SOUND]" + AnalyzeExistingGenres.genreList.get(genreId).get("SOUND") + System.getProperty("line.separator"));
        bw.print("[CONTROL]" + AnalyzeExistingGenres.genreList.get(genreId).get("CONTROL") + System.getProperty("line.separator"));
        bw.print("[GENRE COMB]" + getGenreNames(genreId) + System.getProperty("line.separator"));
        bw.print("[THEME COMB]" + Utils.getCompatibleThemeIdsForGenre(genreId) + System.getProperty("line.separator"));
        bw.print("[DESIGN1]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESIGN1") + System.getProperty("line.separator"));
        bw.print("[DESIGN2]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESIGN2") + System.getProperty("line.separator"));
        bw.print("[DESIGN3]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESIGN3") + System.getProperty("line.separator"));
        bw.print("[DESIGN4]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESIGN4") + System.getProperty("line.separator"));
        bw.print("[DESIGN5]" + AnalyzeExistingGenres.genreList.get(genreId).get("DESIGN5") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAYFEATURE GOOD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, true) + System.getProperty("line.separator"));
        bw.print("[GAMEPLAYFEATURE BAD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, false) + System.getProperty("line.separator"));
        bw.print("[GENRE END]");
        bw.close();
        ChangeLog.addLogEntry(17, AnalyzeExistingGenres.genreList.get(genreId).get("NAME EN"));
        return true;
    }

    /**
     * Imports the specified genre.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the genre has been imported successfully. Returns "false" when the genre already exists. Returns mod tool version of import genre when genre is not compatible with current mod tool version.
     */
    public static String importGenre(String importFolderPath) throws IOException, NullPointerException{
        AnalyzeExistingGenres.analyzeGenreFile();
        int newGenreId = AnalyzeExistingGenres.getFreeGenreID();
        File fileGenreToImport = new File(importFolderPath + "\\genre.txt");
        File fileScreenshotFolder = new File(Utils.getMGT2ScreenshotsPath() + "//" + newGenreId);
        File fileScreenshotsToImport = new File(importFolderPath + "//DATA//screenshots//");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = Utils.parseDataFile(fileGenreToImport);
        map.put("ID", Integer.toString(AnalyzeExistingGenres.getFreeGenreID()));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE COMB")){
                map.put("GENRE COMB", Utils.convertGenreNamesToId(entry.getValue()));
            }else if(entry.getKey().equals("THEME COMB")){
                ArrayList<String> arrayList = Utils.getEntriesFromString(entry.getValue().replaceAll("-", ""));
                StringBuilder themeIds = new StringBuilder();
                for(String string : arrayList){
                    int idToSearch = Integer.parseInt(string.replaceAll("[^0-9]", ""))-1;
                    if(Settings.enableDebugLogging){
                        LOGGER.info("Current id to search: " + idToSearch);
                    }
                    themeIds.append("<").append(AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.get(idToSearch)).append(">");
                }
                map.put("THEME COMB", themeIds.toString());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean genreCanBeImported = false;
        for(String string : GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                genreCanBeImported = true;
            }
        }
        if(!genreCanBeImported){
            return "Genre [" + map.get("NAME EN") + "] could not be imported:\nThe genre is not with the current mod tool version compatible\nGenre was exported in version: " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> map2 : AnalyzeExistingGenres.genreList){
            for(Map.Entry<String, String> entry : map2.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    LOGGER.info("Genre already exists - The genre name is already taken");
                    return "false";
                }
            }
        }
        if(fileScreenshotFolder.exists()){
            LOGGER.info("Genre already exists - screenshot folder already exists.");
            return "false";
        }
        Set<Integer> compatibleThemeIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("THEME COMB"))){
            compatibleThemeIds.add(AnalyzeExistingThemes.getPositionOfThemeInFile(string));
        }
        Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
        Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE BAD"))){
            gameplayFeaturesBadIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
        }
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE GOOD"))){
            gameplayFeaturesGoodIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
        }
        ArrayList<File> genreScreenshots = Utils.getFilesInFolderBlackList(fileScreenshotsToImport.getPath(), ".meta");
        File genreIcon = new File(importFolderPath + "//DATA//icon.png");
        GenreManager.addGenre(map, map,compatibleThemeIds, gameplayFeaturesBadIds, gameplayFeaturesGoodIds, genreScreenshots,true, genreIcon);
        return "true";
    }

    /**
     * Exports the specified publisher.
     * @param publisherNameEN The publisher name that should be exported.
     * @return Returns true when the publisher has been exported successfully. Returns false when the publisher has already been exported.
     */
    public static boolean exportPublisher(String publisherNameEN, Map<String, String> singlePublisherMap) throws IOException {
        final String EXPORTED_PUBLISHER_MAIN_FOLDER_PATH = Utils.getMGT2ModToolExportFolder() + "//Publishers//" + publisherNameEN;
        final String EXPORTED_PUBLISHER_DATA_FOLDER_PATH = EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//DATA//";
        File fileDataFolder = new File(EXPORTED_PUBLISHER_DATA_FOLDER_PATH);
        File fileExportedPublisher = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//publisher.txt");
        File fileExportedPublisherIcon = new File(EXPORTED_PUBLISHER_DATA_FOLDER_PATH + "//icon.png");
        File fileGenreIconToExport = new File(Utils.getMGT2CompanyLogosPath() + singlePublisherMap.get("PIC") + ".png");
        if(!fileExportedPublisherIcon.exists()){
            fileDataFolder.mkdirs();
        }
        if(fileExportedPublisherIcon.exists()){
            return false;
        }
        if(Settings.enableDebugLogging){
            LOGGER.info("Copying image files to export folder...");
        }
        Files.copy(Paths.get(fileGenreIconToExport.getPath()),Paths.get(fileExportedPublisherIcon.getPath()));
        fileExportedPublisher.createNewFile();
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileExportedPublisher), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.print("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.print("[PUBLISHER START]" + System.getProperty("line.separator"));
        bw.print("[NAME EN]" + singlePublisherMap.get("NAME EN") + System.getProperty("line.separator"));
        bw.print("[NAME GE]" + singlePublisherMap.get("NAME GE") + System.getProperty("line.separator"));
        bw.print("[NAME TU]" + singlePublisherMap.get("NAME TU") + System.getProperty("line.separator"));
        bw.print("[NAME FR]" + singlePublisherMap.get("NAME FR") + System.getProperty("line.separator"));
        bw.print("[DATE]" + singlePublisherMap.get("DATE") + System.getProperty("line.separator"));
        bw.print("[DEVELOPER]" + singlePublisherMap.get("DEVELOPER") + System.getProperty("line.separator"));
        bw.print("[PUBLISHER]" + singlePublisherMap.get("PUBLISHER") + System.getProperty("line.separator"));
        bw.print("[MARKET]" + singlePublisherMap.get("MARKET") + System.getProperty("line.separator"));
        bw.print("[SHARE]" + singlePublisherMap.get("SHARE") + System.getProperty("line.separator"));
        LOGGER.info("GenreID: " + singlePublisherMap.get("GENRE"));
        LOGGER.info("GenreName: " + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(singlePublisherMap.get("GENRE"))));
        bw.print("[GENRE]" + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(singlePublisherMap.get("GENRE"))) + System.getProperty("line.separator"));
        bw.print("[PUBLISHER END]");
        bw.close();
        ChangeLog.addLogEntry(21, singlePublisherMap.get("NAME EN"));
        return true;
    }

    /**
     * Imports the specified genre.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the publisher has been imported successfully. Returns "false" when the publisher already exists. Returns mod tool version of import publisher when publisher is not compatible with current mod tool version.
     */
    public static String importPublisher(String importFolderPath) throws IOException {
        AnalyzeExistingPublishers.analyzePublisherFile();
        int newPublisherId = AnalyzeExistingPublishers.getFreePublisherId();
        File fileGenreToImport = new File(importFolderPath + "\\publisher.txt");
        HashMap<String, String> map = new HashMap<>();
        List<Map<String, String>> list = Utils.parseDataFile(fileGenreToImport);
        map.put("ID", Integer.toString(newPublisherId));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE")){
                map.put("GENRE", Integer.toString(AnalyzeExistingGenres.getGenreIdByName(entry.getValue())));
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean publisherCanBeImported = false;
        for(String string : PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                publisherCanBeImported = true;
            }
        }
        if(!publisherCanBeImported){
            return "Publisher [" + map.get("NAME EN") + "] could not be imported:\nThe publisher is not with the current mod tool version compatible\nPublisher was exported in version: " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> map2 : AnalyzeExistingPublishers.getListMap()){
            for(Map.Entry<String, String> entry : map2.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    LOGGER.info("Publisher already exists - The genre name is already taken");
                    return "false";
                }
            }
        }
        int logoId = AnalyzeCompanyLogos.getLogoNumber();
        map.put("PIC", Integer.toString(logoId));
        File publisherImageFilePath = new File(importFolderPath + "//DATA//icon.png");
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
        try {
            if (JOptionPane.showConfirmDialog(null, "Add this publisher?\n" +
                    "\nName: " + map.get("NAME EN") +
                    "\nDate: " + map.get("DATE") +
                    "\nPic: See top left" +
                    "\nDeveloper: " + map.get("DEVELOPER") +
                    "\nPublisher: " + map.get("PUBLISHER") +
                    "\nMarketShare: " + map.get("MARKET") +
                    "\nShare: " + map.get("SHARE") +
                    "\nGenre: " + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(map.get("GENRE"))), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION) {
                EditPublishersFile.addPublisher(map, publisherImageFilePath.getPath());
                ChangeLog.addLogEntry(22, map.get("NAME EN"));
                JOptionPane.showMessageDialog(null, "Publisher " + map.get("NAME EN") + " has been added successfully");
                WindowMain.checkActionAvailability();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Unable to add publisher:\n\nThe special genre for for the requested publisher does not exist!", "Unable to add publisher", JOptionPane.ERROR_MESSAGE);
        }
        return "true";
    }

    /**
     * @param genreId The genre id from which the genre comb names should be transformed
     * @return Returns a list of genre names
     */
    private static String getGenreNames(int genreId){
        String genreNumbersRaw = AnalyzeExistingGenres.genreList.get(genreId).get("GENRE COMB");
        StringBuilder genreNames = new StringBuilder();
        int charPositon = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString());
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(AnalyzeExistingGenres.getGenreNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPositon));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentNumber);
                }
            }
            charPositon++;
        }
        String.valueOf(genreNumbersRaw.charAt(1));
        return genreNames.toString();
    }
}
