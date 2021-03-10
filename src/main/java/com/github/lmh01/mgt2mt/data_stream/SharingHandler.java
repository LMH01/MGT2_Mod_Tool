package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class SharingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingHandler.class);

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
        File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + GenreManager.MAP_SINGLE_GENRE.get("[PIC]"));
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
        bw.print("[NAME AR]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME AR]") + System.getProperty("line.separator"));
        bw.print("[NAME CH]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME CH]") + System.getProperty("line.separator"));
        bw.print("[NAME CT]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME CT]") + System.getProperty("line.separator"));
        bw.print("[NAME CZ]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME CZ]") + System.getProperty("line.separator"));
        bw.print("[NAME EN]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]") + System.getProperty("line.separator"));
        bw.print("[NAME ES]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME ES]") + System.getProperty("line.separator"));
        bw.print("[NAME FR]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME FR]") + System.getProperty("line.separator"));
        bw.print("[NAME GE]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME GE]") + System.getProperty("line.separator"));
        bw.print("[NAME HU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME HU]") + System.getProperty("line.separator"));
        bw.print("[NAME IT]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME IT]") + System.getProperty("line.separator"));
        bw.print("[NAME KO]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME KO]") + System.getProperty("line.separator"));
        bw.print("[NAME PB]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME PB]") + System.getProperty("line.separator"));
        bw.print("[NAME PL]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME PL]") + System.getProperty("line.separator"));
        bw.print("[NAME RU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME RU]") + System.getProperty("line.separator"));
        bw.print("[NAME TU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME TU]") + System.getProperty("line.separator"));
        bw.print("[DESC AR]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC AR]") + System.getProperty("line.separator"));
        bw.print("[DESC CH]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC CH]") + System.getProperty("line.separator"));
        bw.print("[DESC CT]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC CT]") + System.getProperty("line.separator"));
        bw.print("[DESC CZ]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC CZ]") + System.getProperty("line.separator"));
        bw.print("[DESC EN]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC EN]") + System.getProperty("line.separator"));
        bw.print("[DESC ES]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC ES]") + System.getProperty("line.separator"));
        bw.print("[DESC FR]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC FR]") + System.getProperty("line.separator"));
        bw.print("[DESC GE]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC GE]") + System.getProperty("line.separator"));
        bw.print("[DESC HU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC HU]") + System.getProperty("line.separator"));
        bw.print("[DESC IT]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC IT]") + System.getProperty("line.separator"));
        bw.print("[DESC KO]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC KO]") + System.getProperty("line.separator"));
        bw.print("[DESC PB]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC PB]") + System.getProperty("line.separator"));
        bw.print("[DESC PL]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC PL]") + System.getProperty("line.separator"));
        bw.print("[DESC RU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC RU]") + System.getProperty("line.separator"));
        bw.print("[DESC TU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC TU]") + System.getProperty("line.separator"));
        bw.print("[DATE]" + GenreManager.MAP_SINGLE_GENRE.get("[DATE]") + System.getProperty("line.separator"));
        bw.print("[RES POINTS]" + GenreManager.MAP_SINGLE_GENRE.get("[RES POINTS]") + System.getProperty("line.separator"));
        bw.print("[PRICE]" + GenreManager.MAP_SINGLE_GENRE.get("[PRICE]") + System.getProperty("line.separator"));
        bw.print("[DEV COSTS]" + GenreManager.MAP_SINGLE_GENRE.get("[DEV COSTS]") + System.getProperty("line.separator"));
        bw.print("[TGROUP]" + GenreManager.MAP_SINGLE_GENRE.get("[TGROUP]") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAY]" + GenreManager.MAP_SINGLE_GENRE.get("[GAMEPLAY]") + System.getProperty("line.separator"));
        bw.print("[GRAPHIC]" + GenreManager.MAP_SINGLE_GENRE.get("[GRAPHIC]") + System.getProperty("line.separator"));
        bw.print("[SOUND]" + GenreManager.MAP_SINGLE_GENRE.get("[SOUND]") + System.getProperty("line.separator"));
        bw.print("[CONTROL]" + GenreManager.MAP_SINGLE_GENRE.get("[CONTROL]") + System.getProperty("line.separator"));
        bw.print("[GENRE COMB]" + getGenreNames() + System.getProperty("line.separator"));
        bw.print("[THEME COMB]" + GenreManager.MAP_SINGLE_GENRE.get("[THEME COMB]") + System.getProperty("line.separator"));
        bw.print("[DESIGN1]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN1]") + System.getProperty("line.separator"));
        bw.print("[DESIGN2]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN2]") + System.getProperty("line.separator"));
        bw.print("[DESIGN3]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN3]") + System.getProperty("line.separator"));
        bw.print("[DESIGN4]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN4]") + System.getProperty("line.separator"));
        bw.print("[DESIGN5]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN5]") + System.getProperty("line.separator"));
        bw.print("[GENRE END]");
        bw.close();
        ChangeLog.addLogEntry(17, GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]"));
        return true;
    }

    /**
     * Imports the specified genre.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns true when the genre has been imported successfully. Returns false when the genre already exists.
     */
    public static boolean importGenre(String importFolderPath) throws IOException {
        AnalyzeExistingGenres.analyzeGenreFile();
        GenreManager.MAP_SINGLE_GENRE.clear();
        GenreManager.MAP_SINGLE_GENRE.put("[ID]", Integer.toString(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size() + 1));
        int newGenreId = AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size();
        File fileGenreToImport = new File(importFolderPath + "\\genre.txt");
        File fileScreenshotFolder = new File(Utils.getMGT2ScreenshotsPath() + "//" + newGenreId + "//");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileGenreToImport), StandardCharsets.UTF_8));
        String currentLine;
        int line = 1;
        while((currentLine = reader.readLine()) != null){
            switch(line){
                case 1: GenreManager.MAP_SINGLE_GENRE.put("[MGT2MT VERSION]", currentLine.replaceAll("\\uFEFF", "").replace("[MGT2MT VERSION]", "")); break;
                case 3: GenreManager.MAP_SINGLE_GENRE.put("[NAME AR]", currentLine.replace("[NAME AR]", "")); break;
                case 4: GenreManager.MAP_SINGLE_GENRE.put("[NAME CH]", currentLine.replace("[NAME CH]", "")); break;
                case 5: GenreManager.MAP_SINGLE_GENRE.put("[NAME CT]", currentLine.replace("[NAME CT]", "")); break;
                case 6: GenreManager.MAP_SINGLE_GENRE.put("[NAME CZ]", currentLine.replace("[NAME CZ]", "")); break;
                case 7: GenreManager.MAP_SINGLE_GENRE.put("[NAME EN]", currentLine.replace("[NAME EN]", "")); break;
                case 8: GenreManager.MAP_SINGLE_GENRE.put("[NAME ES]", currentLine.replace("[NAME ES]", "")); break;
                case 9: GenreManager.MAP_SINGLE_GENRE.put("[NAME FR]", currentLine.replace("[NAME FR]", "")); break;
                case 10: GenreManager.MAP_SINGLE_GENRE.put("[NAME GE]", currentLine.replace("[NAME GE]", "")); break;
                case 11: GenreManager.MAP_SINGLE_GENRE.put("[NAME HU]", currentLine.replace("[NAME HU]", "")); break;
                case 12: GenreManager.MAP_SINGLE_GENRE.put("[NAME IT]", currentLine.replace("[NAME IT]", ""));break;
                case 13: GenreManager.MAP_SINGLE_GENRE.put("[NAME KO]", currentLine.replace("[NAME KO]", "")); break;
                case 14: GenreManager.MAP_SINGLE_GENRE.put("[NAME PB]", currentLine.replace("[NAME PB]", "")); break;
                case 15: GenreManager.MAP_SINGLE_GENRE.put("[NAME PL]", currentLine.replace("[NAME PL]", "")); break;
                case 16: GenreManager.MAP_SINGLE_GENRE.put("[NAME RU]", currentLine.replace("[NAME RU]", "")); break;
                case 17: GenreManager.MAP_SINGLE_GENRE.put("[NAME TU]", currentLine.replace("[NAME TU]", "")); break;
                case 18: GenreManager.MAP_SINGLE_GENRE.put("[DESC AR]", currentLine.replace("[DESC AR]", "")); break;
                case 19: GenreManager.MAP_SINGLE_GENRE.put("[DESC CH]", currentLine.replace("[DESC CH]", "")); break;
                case 20: GenreManager.MAP_SINGLE_GENRE.put("[DESC CT]", currentLine.replace("[DESC CT]", "")); break;
                case 21: GenreManager.MAP_SINGLE_GENRE.put("[DESC CZ]", currentLine.replace("[DESC CZ]", "")); break;
                case 22: GenreManager.MAP_SINGLE_GENRE.put("[DESC EN]", currentLine.replace("[DESC EN]", "")); break;
                case 23: GenreManager.MAP_SINGLE_GENRE.put("[DESC ES]", currentLine.replace("[DESC ES]", "")); break;
                case 24: GenreManager.MAP_SINGLE_GENRE.put("[DESC FR]", currentLine.replace("[DESC FR]", "")); break;
                case 25: GenreManager.MAP_SINGLE_GENRE.put("[DESC GE]", currentLine.replace("[DESC GE]", "")); break;
                case 26: GenreManager.MAP_SINGLE_GENRE.put("[DESC HU]", currentLine.replace("[DESC HU]", "")); break;
                case 27: GenreManager.MAP_SINGLE_GENRE.put("[DESC IT]", currentLine.replace("[DESC IT]", "")); break;
                case 28: GenreManager.MAP_SINGLE_GENRE.put("[DESC KO]", currentLine.replace("[DESC KO]", "")); break;
                case 29: GenreManager.MAP_SINGLE_GENRE.put("[DESC PB]", currentLine.replace("[DESC PB]", "")); break;
                case 30: GenreManager.MAP_SINGLE_GENRE.put("[DESC PL]", currentLine.replace("[DESC PL]", "")); break;
                case 31: GenreManager.MAP_SINGLE_GENRE.put("[DESC RU]", currentLine.replace("[DESC RU]", "")); break;
                case 32: GenreManager.MAP_SINGLE_GENRE.put("[DESC TU]", currentLine.replace("[DESC TU]", "")); break;
                case 33: GenreManager.MAP_SINGLE_GENRE.put("[DATE]", currentLine.replace("[DATE]", "")); break;
                case 34: GenreManager.MAP_SINGLE_GENRE.put("[RES POINTS]", currentLine.replace("[RES POINTS]", "")); break;
                case 35: GenreManager.MAP_SINGLE_GENRE.put("[PRICE]", currentLine.replace("[PRICE]", "")); break;
                case 36: GenreManager.MAP_SINGLE_GENRE.put("[DEV COSTS]", currentLine.replace("[DEV COSTS]", "")); break;
                case 37: GenreManager.MAP_SINGLE_GENRE.put("[TGROUP]", currentLine.replace("[TGROUP]", "")); break;
                case 38: GenreManager.MAP_SINGLE_GENRE.put("[GAMEPLAY]", currentLine.replace("[GAMEPLAY]", "")); break;
                case 39: GenreManager.MAP_SINGLE_GENRE.put("[GRAPHIC]", currentLine.replace("[GRAPHIC]", "")); break;
                case 40: GenreManager.MAP_SINGLE_GENRE.put("[SOUND]", currentLine.replace("[SOUND]", "")); break;
                case 41: GenreManager.MAP_SINGLE_GENRE.put("[CONTROL]", currentLine.replace("[CONTROL]", "")); break;
                case 42: GenreManager.MAP_SINGLE_GENRE.put("[GENRE COMB]", getGenreIds(currentLine.replace("[GENRE COMB]", ""))); break;
                case 43: GenreManager.MAP_SINGLE_GENRE.put("[THEME COMB]", currentLine.replace("[THEME COMB]", ""));break;
                case 44: GenreManager.MAP_SINGLE_GENRE.put("[DESIGN1]", currentLine.replace("[DESIGN1]", "")); break;
                case 45: GenreManager.MAP_SINGLE_GENRE.put("[DESIGN2]", currentLine.replace("[DESIGN2]", "")); break;
                case 46: GenreManager.MAP_SINGLE_GENRE.put("[DESIGN3]", currentLine.replace("[DESIGN3]", "")); break;
                case 47: GenreManager.MAP_SINGLE_GENRE.put("[DESIGN4]", currentLine.replace("[DESIGN4]", "")); break;
                case 48: GenreManager.MAP_SINGLE_GENRE.put("[DESIGN5]", currentLine.replace("[DESIGN5]", "")); break;
            }
            line++;
        }
        reader.close();
        for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE.size(); i++){//Checks if the genre already exists that should be imported.
            if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE.contains(GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]"))){
                return false;
            }
        }
        if(fileScreenshotFolder.exists()){
            return false;
        }
        fillNewGenreVariables(importFolderPath);
        GenreManager.showSummary(true);
        return true;
    }
    //TODO Rewrite into a more simple approach -> rewrite addGenre so that it uses a Map and not multiple ArrayLists

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
     * @return Returns true when the publisher has been imported successfully. Returns false when the publisher already exists.
     */
    public static boolean importPublisher(String importFolderPath) throws IOException {
        AnalyzeExistingPublishers.analyzePublisherFile();
        HashMap<String, String> mapNewPublisher = new HashMap<>();
        int newPublisherId = AnalyzeExistingPublishers.maxThemeID+1;
        File fileGenreToImport = new File(importFolderPath + "\\publisher.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileGenreToImport), StandardCharsets.UTF_8));
        String currentLine;
        int line = 1;
        while((currentLine = reader.readLine()) != null){
            switch(line){
                case 3: mapNewPublisher.put("NAME EN", currentLine.replace("[NAME EN]", "")); break;
                case 4: mapNewPublisher.put("NAME GE", currentLine.replace("[NAME GE]", "")); break;
                case 5: mapNewPublisher.put("NAME TU", currentLine.replace("[NAME TU]", "")); break;
                case 6: mapNewPublisher.put("NAME FR", currentLine.replace("[NAME FR]", "")); break;
                case 7: mapNewPublisher.put("DATE", currentLine.replace("[DATE]", "")); break;
                case 8: mapNewPublisher.put("DEVELOPER", currentLine.replace("[DEVELOPER]", "")); break;
                case 9: mapNewPublisher.put("PUBLISHER", currentLine.replace("[PUBLISHER]", "")); break;
                case 10: mapNewPublisher.put("MARKET", currentLine.replace("[MARKET]", "")); break;
                case 11: mapNewPublisher.put("SHARE", currentLine.replace("[SHARE]", "")); break;
                case 12: mapNewPublisher.put("GENRE", Integer.toString(AnalyzeExistingGenres.getGenreIdByName(currentLine.replace("[GENRE]", "")))); break;
            }
            line++;
        }
        reader.close();
        mapNewPublisher.put("ID", Integer.toString(newPublisherId));
        int logoId = AnalyzeCompanyLogos.getLogoNumber();
        mapNewPublisher.put("PIC", Integer.toString(logoId));
        List<Map<String, String>> list = AnalyzeExistingPublishers.getListMap();
        for(int i=0; i<list.size(); i++){
            Map<String, String> map = list.get(i);
            if(map.get("NAME EN").equals(mapNewPublisher.get("NAME EN"))){
                return false;
            }
        }
        File publisherImageFilePath = new File(importFolderPath + "//DATA//icon.png");
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
        if(JOptionPane.showConfirmDialog(null, "Add this publisher?:\n" +
                "\nName: " + mapNewPublisher.get("NAME EN") +
                "\nDate: " + mapNewPublisher.get("DATE") +
                "\nPic: See top left" +
                "\nDeveloper: " + mapNewPublisher.get("DEVELOPER") +
                "\nPublisher: " + mapNewPublisher.get("PUBLISHER") +
                "\nMarketShare: " + mapNewPublisher.get("MARKET") +
                "\nShare: " + mapNewPublisher.get("SHARE") +
                "\nGenre: " + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(mapNewPublisher.get("GENRE"))), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION){
            EditPublishersFile.addPublisher(mapNewPublisher, publisherImageFilePath.getPath());
            ChangeLog.addLogEntry(22, mapNewPublisher.get("NAME EN"));
            JOptionPane.showMessageDialog(null, "Publisher " + mapNewPublisher.get("NAME EN") + " has been added successfully");
            WindowMain.checkActionAvailability();
        }
        return true;
    }
    /**
     * Fills the new genre variables that are used to add a new genre. The variables that are being filled are the ones in the {@link GenreManager}.
     */
    private static void fillNewGenreVariables(String importFolderPath){
        GenreManager.resetVariablesToDefault();
        GenreManager.id = AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size();
        GenreManager.name = GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]");
        GenreManager.description = GenreManager.MAP_SINGLE_GENRE.get("[DESC EN]");
        GenreManager.arrayListNameTranslations.clear();
        GenreManager.arrayListDescriptionTranslations.clear();
        fillTranslationsArrays();
        GenreManager.nameTranslationsAdded = true;
        GenreManager.descriptionTranslationsAdded = true;
        setGenreUnlockDate();
        GenreManager.researchPoints = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[RES POINTS]"));
        GenreManager.price = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[PRICE]"));
        GenreManager.devCost = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DEV COSTS]"));
        setGenreTargetGroup();
        setGenreCompatibleGenres();
        setGenreCompatibleThemes();
        GenreManager.gameplay = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[GAMEPLAY]"));
        GenreManager.graphic = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[GRAPHIC]"));
        GenreManager.sound = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[SOUND]"));
        GenreManager.control = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[CONTROL]"));
        GenreManager.design1 = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DESIGN1]"));
        GenreManager.design2 = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DESIGN2]"));
        GenreManager.design3 = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DESIGN3]"));
        GenreManager.design4 = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DESIGN4]"));
        GenreManager.design5 = Integer.parseInt(GenreManager.MAP_SINGLE_GENRE.get("[DESIGN5]"));
        GenreManager.imageFile = new File(importFolderPath + "\\DATA\\icon.png");
        GenreManager.imageFileName = "icon" + GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]");
        GenreManager.useDefaultImageFile = false;
        setGenreScreenshotFiles(importFolderPath);
    }

    /**
     * Fills the translations arrays with the translations.
     */
    private static void fillTranslationsArrays(){
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME AR]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME CH]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME CT]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME CZ]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME ES]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME FR]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME GE]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME HU]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME IT]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME KO]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME PB]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME PL]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME RU]"));
        GenreManager.arrayListNameTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[NAME TU]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC AR]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC CH]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC CT]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC CZ]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC EN]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC ES]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC FR]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC GE]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC HU]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC IT]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC KO]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC PB]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC PL]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC RU]"));
        GenreManager.arrayListDescriptionTranslations.add(GenreManager.MAP_SINGLE_GENRE.get("[DESC TU]"));
    }

    /**
     * Splits the single string [DATE] into its year and month part to be written in the variables
     */
    private static void setGenreUnlockDate(){
        String string = GenreManager.MAP_SINGLE_GENRE.get("[DATE]");
        GenreManager.unlockMonth = string.replaceAll("[0-9]", "").replace(" ", "");
        GenreManager.unlockYear = Integer.parseInt(string.replaceAll("[^0-9]", ""));
    }

    /**
     * Sets the genre target group.
     */
    private static void setGenreTargetGroup(){
        String string = GenreManager.MAP_SINGLE_GENRE.get("[TGROUP]");
        if(string.contains("KID")){
            GenreManager.targetGroupKid = true;
        }
        if(string.contains("TEEN")){
            GenreManager.targetGroupTeen = true;
        }
        if(string.contains("ADULT")){
            GenreManager.targetGroupAdult = true;
        }
        if(string.contains("OLD")){
            GenreManager.targetGroupSenior = true;
        }
    }

    /**
     * Fills this array list with correct compatible genres {@link GenreManager#ARRAY_LIST_COMPATIBLE_GENRES}
     */
    private static void setGenreCompatibleGenres(){
        String string = GenreManager.MAP_SINGLE_GENRE.get("[GENRE COMB]");
        int charPosition = 0;
        StringBuilder currentGenre = new StringBuilder();
        for(int i = 0; i<string.length(); i++){
            if(String.valueOf(string.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(string.charAt(charPosition)).equals(">")){
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreName: " + currentGenre);
                }
                GenreManager.ARRAY_LIST_COMPATIBLE_GENRES.add(AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(currentGenre.toString())));
                currentGenre = new StringBuilder();
            }else{
                currentGenre.append(string.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentGenre);
                }
            }
            charPosition++;
        }
    }

    /**
     * Fills this map with correct compatible themes {@link GenreManager#MAP_COMPATIBLE_THEMES}
     */
    private static void setGenreCompatibleThemes(){
        String string = GenreManager.MAP_SINGLE_GENRE.get("[THEME COMB]");
        int charPosition = 0;
        boolean scanningNumber = false;
        StringBuilder themeNumber = new StringBuilder();
        StringBuilder currentTheme = new StringBuilder();
        for(int i = 0; i<string.length(); i++){
            if(String.valueOf(string.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(string.charAt(charPosition)).equals("-")){
                scanningNumber = true;
            }else if(String.valueOf(string.charAt(charPosition)).equals(">")){
                if(Settings.enableDebugLogging){
                    LOGGER.info("themeName: " + currentTheme);
                }
                GenreManager.MAP_COMPATIBLE_THEMES.put(Integer.parseInt(themeNumber.toString().replaceAll("[^0-9]", "")), currentTheme.toString());
                currentTheme = new StringBuilder();
                themeNumber = new StringBuilder();
                scanningNumber = false;
            }else{
                if(scanningNumber){
                    themeNumber.append(string.charAt(charPosition));
                }else{
                    if(String.valueOf(string.charAt(charPosition)).equals("_")){
                        currentTheme.append(" ");
                    }else{
                        currentTheme.append(string.charAt(charPosition));
                        if(Settings.enableDebugLogging){
                            LOGGER.info("currentNumber: " + currentTheme);
                        }
                    }
                }
            }
            charPosition++;
        }
    }

    /**
     * Fills this array list with genre screenshot files {@link GenreManager#arrayListScreenshotFiles}
     */
    private static void setGenreScreenshotFiles(String importFolderPath){
        ArrayList<File> arrayListFilesInScreenshotFolder = Utils.getFilesInFolder(importFolderPath + "\\DATA\\screenshots");
        for (File file : arrayListFilesInScreenshotFolder) {
            if (file.getName().endsWith(".png")) {
                GenreManager.arrayListScreenshotFiles.add(file);
            }
        }
    }

    /**
     * Converts the genre names to ids
     * @return Returns a list of genre ids.
     */
    private static String getGenreIds(String genreNamesRaw){
        StringBuilder genreIds = new StringBuilder();
        int charPositon = 0;
        StringBuilder currentName = new StringBuilder();
        for(int i = 0; i<genreNamesRaw.length(); i++){
            if(String.valueOf(genreNamesRaw.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNamesRaw.charAt(charPositon)).equals(">")){
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreName: " + currentName);
                }
                int genreId = AnalyzeExistingGenres.getGenreIdByName(currentName.toString());
                if(genreId != -1){
                    genreIds.append("<").append(genreId).append(">");
                }
                currentName = new StringBuilder();
            }else{
                currentName.append(genreNamesRaw.charAt(charPositon));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentName);
                }
            }
            charPositon++;
        }
        String.valueOf(genreNamesRaw.charAt(1));
        LOGGER.info("Genre ids: " + genreIds);
        return genreIds.toString();
    }

    private static String getGenreNames(){
        String genreNumbersRaw = GenreManager.MAP_SINGLE_GENRE.get("[GENRE COMB]");
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
