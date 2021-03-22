package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class SharingHandler {
    //Contains functions with which exports are written to file and imports are read and imported
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingHandler.class);

    /**
     * Exports the specified genre.
     * @param genreName The genre name
     * @return Returns true when the genre has been exported successfully. Returns false when the genre has already been exported.
     * @throws IOException
     */
    public static boolean exportGenre(String genreName) throws IOException {
        int genreId = AnalyzeExistingGenres.getGenreIdByName(genreName);
        int positionInGenreList = AnalyzeExistingGenres.getPositionInGenreListByGenreId(genreId);
        final String EXPORTED_GENRE_MAIN_FOLDER_PATH = Settings.MGT2_MOD_MANAGER_PATH + "//Export//Genres//" + genreName;
        final String EXPORTED_GENRE_DATA_FOLDER_PATH = EXPORTED_GENRE_MAIN_FOLDER_PATH + "//DATA//";
        File fileDataFolder = new File(EXPORTED_GENRE_DATA_FOLDER_PATH);
        File fileExportedGenre = new File(EXPORTED_GENRE_MAIN_FOLDER_PATH + "//genre.txt");
        File fileExportedGenreIcon = new File(EXPORTED_GENRE_DATA_FOLDER_PATH + "//icon.png");
        File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + "icon" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("NAME EN").replaceAll(" ", "") + ".png");
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
        DataStreamHelper.copyDirectory(fileGenreScreenshotsToExport.toPath().toString(), EXPORTED_GENRE_DATA_FOLDER_PATH + "//screenshots//");
        fileExportedGenre.createNewFile();
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileExportedGenre), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.print("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.print("[GENRE START]" + System.getProperty("line.separator"));
        for(String translationKey : TranslationManager.TRANSLATION_KEYS){
            bw.print("[NAME " + translationKey + "]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("NAME " + translationKey)  + System.getProperty("line.separator"));
            bw.print("[DESC " + translationKey + "]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("DESC " + translationKey)  + System.getProperty("line.separator"));
        }
        bw.print("[DATE]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("DATE") + System.getProperty("line.separator"));
        bw.print("[RES POINTS]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("RES POINTS") + System.getProperty("line.separator"));
        bw.print("[PRICE]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("PRICE") + System.getProperty("line.separator"));
        bw.print("[DEV COSTS]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("DEV COSTS") + System.getProperty("line.separator"));
        bw.print("[TGROUP]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("TGROUP") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAY]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("GAMEPLAY") + System.getProperty("line.separator"));
        bw.print("[GRAPHIC]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("GRAPHIC") + System.getProperty("line.separator"));
        bw.print("[SOUND]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("SOUND") + System.getProperty("line.separator"));
        bw.print("[CONTROL]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("CONTROL") + System.getProperty("line.separator"));
        bw.print("[GENRE COMB]" + getGenreNames(genreId) + System.getProperty("line.separator"));
        bw.print("[THEME COMB]" + Utils.getCompatibleThemeIdsForGenre(positionInGenreList) + System.getProperty("line.separator"));
        bw.print("[FOCUS0]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS0") + System.getProperty("line.separator"));
        bw.print("[FOCUS1]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS1") + System.getProperty("line.separator"));
        bw.print("[FOCUS2]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS2") + System.getProperty("line.separator"));
        bw.print("[FOCUS3]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS3") + System.getProperty("line.separator"));
        bw.print("[FOCUS4]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS4") + System.getProperty("line.separator"));
        bw.print("[FOCUS5]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS5") + System.getProperty("line.separator"));
        bw.print("[FOCUS6]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS6") + System.getProperty("line.separator"));
        bw.print("[FOCUS7]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("FOCUS7") + System.getProperty("line.separator"));
        bw.print("[ALIGN0]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("ALIGN0") + System.getProperty("line.separator"));
        bw.print("[ALIGN1]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("ALIGN1") + System.getProperty("line.separator"));
        bw.print("[ALIGN2]" + AnalyzeExistingGenres.genreList.get(positionInGenreList).get("ALIGN2") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAYFEATURE GOOD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, true) + System.getProperty("line.separator"));
        bw.print("[GAMEPLAYFEATURE BAD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, false) + System.getProperty("line.separator"));
        bw.print("[GENRE END]");
        bw.close();
        ChangeLog.addLogEntry(17, AnalyzeExistingGenres.genreList.get(positionInGenreList).get("NAME EN"));
        return true;
    }

    /**
     * Imports the specified genre.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the genre has been imported successfully. Returns "false" when the genre already exists. Returns mod tool version of import genre when genre is not compatible with current mod tool version.
     */
    public static String importGenre(String importFolderPath, boolean showMessages) throws IOException, NullPointerException{
        AnalyzeExistingGenres.analyzeGenreFile();
        int newGenreId = AnalyzeExistingGenres.getFreeGenreID();
        File fileGenreToImport = new File(importFolderPath + "\\genre.txt");
        File fileScreenshotFolder = new File(Utils.getMGT2ScreenshotsPath() + "//" + newGenreId);
        File fileScreenshotsToImport = new File(importFolderPath + "//DATA//screenshots//");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileGenreToImport);
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
        for(String string : SharingManager.GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                genreCanBeImported = true;
            }
        }
        if(!genreCanBeImported && !Settings.disableSafetyFeatures){
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
            DataStreamHelper.deleteDirectory(fileScreenshotFolder);
        }
        Set<Integer> compatibleThemeIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("THEME COMB"))){
            compatibleThemeIds.add(AnalyzeExistingThemes.getPositionOfThemeInFile(string)-1);
        }
        Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
        Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE BAD"))){
            gameplayFeaturesBadIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
        }
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE GOOD"))){
            gameplayFeaturesGoodIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
        }
        ArrayList<File> genreScreenshots = DataStreamHelper.getFilesInFolderBlackList(fileScreenshotsToImport.getPath(), ".meta");
        File genreIcon = new File(importFolderPath + "//DATA//icon.png");
        GenreManager.addGenre(map, map,compatibleThemeIds, gameplayFeaturesBadIds, gameplayFeaturesGoodIds, genreScreenshots,true, genreIcon, showMessages);
        return "true";
    }

    /**
     * Exports the specified licence
     * @param licenceName The licence that should be exported
     */
    public static boolean exportLicence(String licenceName) throws IOException {
        final String EXPORTED_LICENCE_MAIN_FOLDER_PATH = Utils.getMGT2ModToolExportFolder() + "//Licence//" + licenceName;
        File fileExportMainFolder = new File(EXPORTED_LICENCE_MAIN_FOLDER_PATH);
        File fileExportedLicence = new File(EXPORTED_LICENCE_MAIN_FOLDER_PATH + "//licence.txt");
        fileExportMainFolder.mkdirs();
        fileExportedLicence.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedLicence), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION);bw.write(System.getProperty("line.separator"));
        bw.write("[LICENCE START]");bw.write(System.getProperty("line.separator"));
        bw.write("[NAME]" + licenceName);bw.write(System.getProperty("line.separator"));
        bw.write("[TYPE]" + AnalyzeExistingLicences.getTypeForLicence(licenceName));bw.write(System.getProperty("line.separator"));
        bw.write("[LICENCE END]");
        bw.close();
        ChangeLog.addLogEntry(33, licenceName);
        return true;
    }

    public static String importLicence(String importFolderPath, boolean showMessages) throws IOException {
        AnalyzeExistingLicences.analyze();
        File fileGenreToImport = new File(importFolderPath + "\\licence.txt");
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileGenreToImport);
        Map<String, String> map = list.get(0);
        boolean licenceCanBeImported = false;
        for(String string : SharingManager.LICENCE_IMPORT_COMPATIBLE_MOD_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                licenceCanBeImported = true;
            }
        }
        if(!licenceCanBeImported && !Settings.disableSafetyFeatures){
            return "Licence [" + map.get("NAME") + "] could not be imported:\nThe licence is not with the current mod tool version compatible\nLicence was exported in version: " + map.get("MGT2MT VERSION");
        }
        Map<String, String> exportMap = new HashMap<>();
        exportMap.put("NAME", map.get("NAME"));
        exportMap.put("TYPE", map.get("TYPE"));
        EditLicenceFile.addLicence(exportMap);
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, "Add this licence:\nName: " + exportMap.get("NAME") + "\nType: " + exportMap.get("TYPE"), "Add licence?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                ChangeLog.addLogEntry(22, map.get("NAME EN"));
                JOptionPane.showMessageDialog(null, "Licence " + map.get("NAME") + " has been added successfully");
                WindowMain.checkActionAvailability();
            }
        }else{
            ChangeLog.addLogEntry(34, map.get("NAME"));
            WindowMain.checkActionAvailability();
        }
        return "true";
    }
    /**
     * Exports the specified publisher.
     * @param publisherNameEN The publisher name that should be exported.
     * @return Returns true when the publisher has been exported successfully. Returns false when the publisher has already been exported.
     */
    public static boolean exportPublisher(String publisherNameEN) throws IOException {
        Map<String, String> singlePublisherMap = AnalyzeExistingPublishers.getSinglePublisherByNameMap(publisherNameEN);
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
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedPublisher), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.write("[PUBLISHER START]" + System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, singlePublisherMap);
        bw.write("[DATE]" + singlePublisherMap.get("DATE") + System.getProperty("line.separator"));
        bw.write("[DEVELOPER]" + singlePublisherMap.get("DEVELOPER") + System.getProperty("line.separator"));
        bw.write("[PUBLISHER]" + singlePublisherMap.get("PUBLISHER") + System.getProperty("line.separator"));
        bw.write("[MARKET]" + singlePublisherMap.get("MARKET") + System.getProperty("line.separator"));
        bw.write("[SHARE]" + singlePublisherMap.get("SHARE") + System.getProperty("line.separator"));
        LOGGER.info("GenreID: " + singlePublisherMap.get("GENRE"));
        LOGGER.info("GenreName: " + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(singlePublisherMap.get("GENRE"))));
        bw.write("[GENRE]" + AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(singlePublisherMap.get("GENRE"))) + System.getProperty("line.separator"));
        bw.write("[PUBLISHER END]");
        bw.close();
        ChangeLog.addLogEntry(21, singlePublisherMap.get("NAME EN"));
        return true;
    }

    /**
     * Imports the specified genre.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the publisher has been imported successfully. Returns "false" when the publisher already exists. Returns mod tool version of import publisher when publisher is not compatible with current mod tool version.
     */
    public static String importPublisher(String importFolderPath, boolean showMessages) throws IOException {
        AnalyzeExistingPublishers.analyzePublisherFile();
        int newPublisherId = AnalyzeExistingPublishers.getFreePublisherId();
        File fileGenreToImport = new File(importFolderPath + "\\publisher.txt");
        HashMap<String, String> map = new HashMap<>();
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileGenreToImport);
        map.put("ID", Integer.toString(newPublisherId));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE")){
                map.put("GENRE", Integer.toString(AnalyzeExistingGenres.getGenreIdByName(entry.getValue())));
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean publisherCanBeImported = false;
        for(String string : SharingManager.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                publisherCanBeImported = true;
            }
        }
        if(!publisherCanBeImported && !Settings.disableSafetyFeatures){
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
            if(showMessages){
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
            }else{
                EditPublishersFile.addPublisher(map, publisherImageFilePath.getPath());
                ChangeLog.addLogEntry(22, map.get("NAME EN"));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Unable to add publisher:\n\nThe special genre for for the requested publisher does not exist!", "Unable to add publisher", JOptionPane.ERROR_MESSAGE);
        }
        return "true";
    }

    /**
     * Exports the theme that stand in the map
     * @param themeNameEn The english name of the theme that should be exported
     * @return Returns true when the theme has been exported successfully. Returns false when the theme has already been exported.
     */
    public static boolean exportTheme(String themeNameEn) throws IOException {
        Map<String, String> map = AnalyzeExistingThemes.getSingleThemeByNameMap(themeNameEn);
        final String EXPORTED_PUBLISHER_MAIN_FOLDER_PATH = Utils.getMGT2ModToolExportFolder() + "//Themes//" + map.get("NAME EN");
        File fileExportFolderPath = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH);
        File fileExportedTheme = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//theme.txt");
        if(fileExportedTheme.exists()){
           return false;
        }else{
            fileExportFolderPath.mkdirs();
        }
        fileExportedTheme.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedTheme), StandardCharsets.UTF_8));
        bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.write("[THEME START]" + System.getProperty("line.separator"));
        bw.write("[VIOLENCE LEVEL]" + map.get("VIOLENCE LEVEL") + System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, map);
        bw.write("[GENRE COMB]" + getGenreNames(map.get("GENRE COMB")) + System.getProperty("line.separator"));
        bw.close();
        ChangeLog.addLogEntry(23, map.get("NAME EN"));
        return true;
    }

    /**
     * Imports the specified theme.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the theme has been imported successfully. Returns "false" when the publisher already exists. Returns mod tool version of import theme when theme is not compatible with current mod tool version.
     */
    public static String importTheme(String importFolderPath, boolean showMessages) throws IOException{
        AnalyzeExistingThemes.analyzeThemeFiles();
        File fileThemeToImport = new File(importFolderPath + "\\theme.txt");
        ArrayList<Integer> compatibleGenreIds = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int violenceRating = 0;
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileThemeToImport);
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE COMB")){
                ArrayList<String> compatibleGenreNames = Utils.getEntriesFromString(entry.getValue());
                for(String string : compatibleGenreNames){
                    compatibleGenreIds.add(AnalyzeExistingGenres.getGenreIdByName(string));
                }
            }else if(entry.getKey().equals("VIOLENCE LEVEL")){
                violenceRating = Integer.parseInt(entry.getValue());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }

        boolean themeCanBeImported = false;
        for(String string : SharingManager.THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                themeCanBeImported = true;
            }
        }
        if(!themeCanBeImported && !Settings.disableSafetyFeatures){
            return "Theme [" + map.get("NAME EN") + "] could not be imported:\nThe theme is not with the current mod tool version compatible\nTheme was exported in version: " + map.get("MGT2MT VERSION");
        }
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            if(entry.getValue().equals(map.get("NAME EN"))){
                LOGGER.info("Theme already exists - The theme name is already taken");
                return "false";
            }
        }
        try {
            if(showMessages){
                if(JOptionPane.showConfirmDialog(null, "Add this theme?\n\n" + map.get("NAME EN"), "Add theme?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    EditThemeFiles.addTheme(map, compatibleGenreIds, violenceRating);
                    ChangeLog.addLogEntry(24, map.get("NAME EN"));
                    JOptionPane.showMessageDialog(null, "Theme " + map.get("NAME EN") + " has been added successfully");
                    WindowMain.checkActionAvailability();
                }
            }else{
                EditThemeFiles.addTheme(map, compatibleGenreIds, violenceRating);
                ChangeLog.addLogEntry(24, map.get("NAME EN"));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Unable to add publisher:\n\nThe special genre for for the requested publisher does not exist!", "Unable to add publisher", JOptionPane.ERROR_MESSAGE);
        }
        return "true";
    }

    /**
     * Opens a GUI where the user can select what engine features should be exported. Exports these engine features.
     */
    public static boolean exportEngineFeature(String engineFeatureName){
        try{
            Map<String, String> map = AnalyzeExistingEngineFeatures.getSingleEngineFeatureByNameMap(engineFeatureName);
            final String EXPORTED_ENGINE_FEATURE_MAIN_FOLDER_PATH = Utils.getMGT2ModToolExportFolder() + "//Engine features//" + map.get("NAME EN");
            File fileExportFolderPath = new File(EXPORTED_ENGINE_FEATURE_MAIN_FOLDER_PATH);
            File fileExportedEngineFeature = new File(EXPORTED_ENGINE_FEATURE_MAIN_FOLDER_PATH + "//engineFeature.txt");
            if(fileExportedEngineFeature.exists()){
                return false;
            }else{
                fileExportFolderPath.mkdirs();
            }
            fileExportedEngineFeature.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedEngineFeature), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
            bw.write("[GAMEPLAY FEATURE START]" + System.getProperty("line.separator"));
            bw.write("[TYP]" + map.get("TYP") + System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, map);
            bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + map.get("RES POINTS") + System.getProperty("line.separator"));
            bw.write("[PRICE]" + map.get("PRICE") + System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + map.get("DEV COSTS") + System.getProperty("line.separator"));
            bw.write("[TECHLEVEL]" + map.get("TECHLEVEL") + System.getProperty("line.separator"));
            bw.write("[PIC]" + map.get("PIC") + System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + map.get("GAMEPLAY") + System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + map.get("GRAPHIC") + System.getProperty("line.separator"));
            bw.write("[SOUND]" + map.get("SOUND") + System.getProperty("line.separator"));
            bw.write("[TECH]" + map.get("TECH") + System.getProperty("line.separator"));
            bw.close();
            ChangeLog.addLogEntry(31, map.get("NAME EN"));
            return true;
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while exporting engine feature: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Imports an engine feature
     * @param importFolderPath The path where the engine feature files are located
     * @param showMessages True when message about adding engine feature should be shown. False if not.
     */
    public static String importEngineFeature(String importFolderPath, boolean showMessages) throws IOException {
        AnalyzeExistingEngineFeatures.analyzeEngineFeatures();
        String returnValue = SharingManager.importGeneral("engineFeature.txt",
                "Engine feature",
                importFolderPath,
                AnalyzeExistingEngineFeatures.engineFeatures,
                SharingManager.ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS,
                EditEngineFeaturesFile::addEngineFeature,
                AnalyzeExistingEngineFeatures::getFreeEngineFeatureId,
                32,
                Summaries::showEngineFeatureMessage,
                showMessages);
        return returnValue;
    }

    /**
     * Opens a GUI where the user can select what gameplay features should be exported. Exports these gameplay features.
     */
    public static boolean exportGameplayFeature(String gameplayFeatureName){
        try{
            Map<String, String> map = AnalyzeExistingGameplayFeatures.getSingleGameplayFeatureByNameMap(gameplayFeatureName);
            final String EXPORTED_GAMEPLAY_FEATURE_MAIN_FOLDER_PATH = Utils.getMGT2ModToolExportFolder() + "//Gameplay features//" + map.get("NAME EN");
            File fileExportFolderPath = new File(EXPORTED_GAMEPLAY_FEATURE_MAIN_FOLDER_PATH);
            File fileExportedGameplayFeature = new File(EXPORTED_GAMEPLAY_FEATURE_MAIN_FOLDER_PATH + "//gameplayFeature.txt");
            if(fileExportedGameplayFeature.exists()){
                return false;
            }else{
                fileExportFolderPath.mkdirs();
            }
            fileExportedGameplayFeature.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedGameplayFeature), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
            bw.write("[GAMEPLAY FEATURE START]" + System.getProperty("line.separator"));
            bw.write("[TYP]" + map.get("TYP") + System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, map);
            bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + map.get("RES POINTS") + System.getProperty("line.separator"));
            bw.write("[PRICE]" + map.get("PRICE") + System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + map.get("DEV COSTS") + System.getProperty("line.separator"));
            bw.write("[PIC]" + map.get("PIC") + System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + map.get("GAMEPLAY") + System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + map.get("GRAPHIC") + System.getProperty("line.separator"));
            bw.write("[SOUND]" + map.get("SOUND") + System.getProperty("line.separator"));
            bw.write("[TECH]" + map.get("TECH") + System.getProperty("line.separator"));
            bw.write("[BAD]" + getGenreNames(Utils.convertGenreNamesToId(map.get("BAD"))) + System.getProperty("line.separator"));
            bw.write("[GOOD]" + getGenreNames(Utils.convertGenreNamesToId(map.get("GOOD"))) + System.getProperty("line.separator"));
            bw.close();
            ChangeLog.addLogEntry(29, map.get("NAME EN"));
            return true;
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while exporting gameplay feature: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Imports an gameplay feature
     * @param importFolderPath The path where the gameplay feature files are located
     * @param showMessages True when message about adding gameplay feature should be shown. False if not.
     */
    public static String importGameplayFeature(String importFolderPath, boolean showMessages) throws IOException {
        AnalyzeExistingGameplayFeatures.analyzeGameplayFeatures();
        return SharingManager.importGeneral("gameplayFeature.txt",
                "Gameplay feature",
                importFolderPath,
                AnalyzeExistingGameplayFeatures.gameplayFeatures,
                SharingManager.GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS,
                EditGameplayFeaturesFile::addGameplayFeature,
                AnalyzeExistingGameplayFeatures::getFreeGameplayFeatureId,
                30,
                Summaries::showGameplayFeatureMessage,
                showMessages);
    }

    /**
     * @param genreId The genre id from which the genre comb names should be transformed
     * @return Returns a list of genre names
     */
    private static String getGenreNames(int genreId){
        int genrePositionInList = AnalyzeExistingGenres.getPositionInGenreListByGenreId(genreId);
        String genreNumbersRaw = AnalyzeExistingGenres.genreList.get(genrePositionInList).get("GENRE COMB");
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

    /**
     * @param genreNumbersRaw The string containing the genre ids that should be transformed
     * @return Returns a list of genre names
     */
    private static String getGenreNames(String genreNumbersRaw){
        StringBuilder genreNames = new StringBuilder();
        int charPositon = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString().replaceAll("[^0-9]", ""));
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
