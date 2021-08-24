package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.data_stream.NPCGameListChanger;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GenreMod extends AbstractAdvancedMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreMod.class);
    //New Variables
    public static Map<String, String> mapNameTranslations = new HashMap<>();
    public static Map<String, String> mapDescriptionTranslations = new HashMap<>();
    public static Map<String, String> mapNewGenre = new HashMap<>();//This is the map that contains all information on the new genre.

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        if(map.containsKey("PIC")){
            EditHelper.printLine("PIC", map, bw);
        }else{
            bw.write("[PIC]icon" + map.get("NAME EN").replaceAll(" ", "") + ".png");bw.write(System.getProperty("line.separator"));
        }
        EditHelper.printLine("TGROUP", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("CONTROL", map, bw);
        EditHelper.printLine("GENRE COMB", map, bw);
        EditHelper.printLine("FOCUS0", map, bw);
        EditHelper.printLine("FOCUS1", map, bw);
        EditHelper.printLine("FOCUS2", map, bw);
        EditHelper.printLine("FOCUS3", map, bw);
        EditHelper.printLine("FOCUS4", map, bw);
        EditHelper.printLine("FOCUS5", map, bw);
        EditHelper.printLine("FOCUS6", map, bw);
        EditHelper.printLine("FOCUS7", map, bw);
        EditHelper.printLine("ALIGN0", map, bw);
        EditHelper.printLine("ALIGN1", map, bw);
        EditHelper.printLine("ALIGN2", map, bw);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.genreMod;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "Genres.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_genres.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        analyzeFile();
        ModManager.themeMod.analyzeFile();
        startStepByStepGuide();
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR GENRE MOD
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        throw new ModProcessingException("Call to getOptionPaneMessage(T t) is invalid. This function is not implemented for genre mod", true);
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public String getTypeCaps() {
        return "GENRE";
    }

    @Override
    public String getImportExportFileName() {
        return "genre.txt";
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {
        ModManager.themeMod.editGenreAllocation(getContentIdByName(name), false, null);
        ModManager.gameplayFeatureMod.removeGenreId(getContentIdByName(name));
        ImageFileHandler.removeImageFiles(name);
        ModManager.publisherMod.removeGenre(name);
        ModManager.npcEngineMod.removeGenre(name);
        NpcGamesMod.editNPCGames(ModManager.genreMod.getContentIdByName(name), false, 0);
        super.removeMod(name);
    }

    @Override
    public boolean exportMod(String name, boolean exportAsRestorePoint) throws ModProcessingException {
        try {
            int genreId = ModManager.genreMod.getContentIdByName(name);
            int positionInGenreList = ModManager.genreMod.getPositionInFileContentListById(genreId);
            String exportFolder;
            if(exportAsRestorePoint){
                exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
            }else{
                exportFolder = Utils.getMGT2ModToolExportFolder();
            }
            final String EXPORTED_GENRE_MAIN_FOLDER_PATH = exportFolder + "//Genres//" + name.replaceAll("[^a-zA-Z0-9]", "");
            final String EXPORTED_GENRE_DATA_FOLDER_PATH = EXPORTED_GENRE_MAIN_FOLDER_PATH + "//DATA//";
            File fileDataFolder = new File(EXPORTED_GENRE_DATA_FOLDER_PATH);
            File fileExportedGenre = new File(EXPORTED_GENRE_MAIN_FOLDER_PATH + "//genre.txt");
            File fileExportedGenreIcon = new File(EXPORTED_GENRE_DATA_FOLDER_PATH + "//icon.png");
            File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + "icon" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("NAME EN").replaceAll(" ", "") + ".png");
            File fileGenreScreenshotsToExport = new File(Utils.getMGT2ScreenshotsPath() + genreId);
            if(!fileExportedGenreIcon.exists()){
                fileDataFolder.mkdirs();
            }
            if(fileExportedGenreIcon.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + getMainTranslationKey() + " - " + name + ": " + I18n.INSTANCE.get("sharer.modAlreadyExported"));
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
                if(ModManager.genreMod.getFileContent().get(positionInGenreList).get("NAME " + translationKey) != null){
                    bw.print("[NAME " + translationKey + "]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("NAME " + translationKey)  + System.getProperty("line.separator"));
                }else{
                    bw.print("[NAME " + translationKey + "]" + name + System.getProperty("line.separator"));
                }
                if(ModManager.genreMod.getFileContent().get(positionInGenreList).get("DESC " + translationKey) != null) {
                    bw.print("[DESC " + translationKey + "]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("DESC " + translationKey)  + System.getProperty("line.separator"));
                }else{
                    bw.print("[DESC " + translationKey + "]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("DESC EN") + System.getProperty("line.separator"));
                }
            }
            bw.print("[DATE]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("DATE") + System.getProperty("line.separator"));
            bw.print("[RES POINTS]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("RES POINTS") + System.getProperty("line.separator"));
            bw.print("[PRICE]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("PRICE") + System.getProperty("line.separator"));
            bw.print("[DEV COSTS]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("DEV COSTS") + System.getProperty("line.separator"));
            bw.print("[TGROUP]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("TGROUP") + System.getProperty("line.separator"));
            bw.print("[GAMEPLAY]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("GAMEPLAY") + System.getProperty("line.separator"));
            bw.print("[GRAPHIC]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("GRAPHIC") + System.getProperty("line.separator"));
            bw.print("[SOUND]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("SOUND") + System.getProperty("line.separator"));
            bw.print("[CONTROL]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("CONTROL") + System.getProperty("line.separator"));
            bw.print("[GENRE COMB]" + ModManager.genreMod.getGenreNames(genreId) + System.getProperty("line.separator"));
            bw.print("[THEME COMB]" + Utils.getCompatibleThemeIdsForGenre(positionInGenreList) + System.getProperty("line.separator"));
            bw.print("[FOCUS0]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS0") + System.getProperty("line.separator"));
            bw.print("[FOCUS1]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS1") + System.getProperty("line.separator"));
            bw.print("[FOCUS2]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS2") + System.getProperty("line.separator"));
            bw.print("[FOCUS3]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS3") + System.getProperty("line.separator"));
            bw.print("[FOCUS4]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS4") + System.getProperty("line.separator"));
            bw.print("[FOCUS5]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS5") + System.getProperty("line.separator"));
            bw.print("[FOCUS6]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS6") + System.getProperty("line.separator"));
            bw.print("[FOCUS7]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("FOCUS7") + System.getProperty("line.separator"));
            bw.print("[ALIGN0]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("ALIGN0") + System.getProperty("line.separator"));
            bw.print("[ALIGN1]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("ALIGN1") + System.getProperty("line.separator"));
            bw.print("[ALIGN2]" + ModManager.genreMod.getFileContent().get(positionInGenreList).get("ALIGN2") + System.getProperty("line.separator"));
            bw.print("[GAMEPLAYFEATURE GOOD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, true) + System.getProperty("line.separator"));
            bw.print("[GAMEPLAYFEATURE BAD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, false) + System.getProperty("line.separator"));
            bw.print("[GENRE END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exported") + " " + getMainTranslationKey() + " - " + name);
        }catch (IOException e){//
            throw new ModProcessingException(I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] - " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage());
        }
        return true;
    }

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws ModProcessingException {
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + I18n.INSTANCE.get("window.main.share.export.genre"));
        ModManager.genreMod.analyzeFile();
        ModManager.gameplayFeatureMod.analyzeFile();
        int newGenreId = ModManager.genreMod.getFreeId();
        File fileGenreToImport = new File(importFolderPath + "\\genre.txt");
        File fileScreenshotFolder = new File(Utils.getMGT2ScreenshotsPath() + "//" + newGenreId);
        File fileScreenshotsToImport = new File(importFolderPath + "//DATA//screenshots//");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list;
        try {
            list = DataStreamHelper.parseDataFile(fileGenreToImport);
        } catch (IOException e) {
            throw new ModProcessingException("File could not be parsed '" + fileGenreToImport.getName() + "': " +  e.getMessage());
        }
        map.put("ID", Integer.toString(newGenreId));
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
                    themeIds.append("<").append(ModManager.themeMod.getFileContent().get(idToSearch+1)).append(">");
                }
                map.put("THEME COMB", themeIds.toString());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean genreCanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION"))){
                genreCanBeImported = true;
            }
        }
        if(!genreCanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN") + " - " + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION")));
            return I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN") + "\n" + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION"));
        }
        for(Map<String, String> map2 : ModManager.genreMod.getFileContent()){
            for(Map.Entry<String, String> entry : map2.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.alreadyExists") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN"));
                    LOGGER.info("Genre already exists - The genre name is already taken");
                    return "false";
                }
            }
        }
        if(fileScreenshotFolder.exists()){
            DataStreamHelper.deleteDirectory(fileScreenshotFolder);
        }//Here
        Set<Integer> compatibleThemeIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("THEME COMB"))){
            compatibleThemeIds.add(ModManager.themeMod.getPositionOfThemeInFile(string));
        }
        Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
        Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE BAD"))){
            gameplayFeaturesBadIds.add(ModManager.gameplayFeatureMod.getContentIdByName(string));
        }
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE GOOD"))){
            gameplayFeaturesGoodIds.add(ModManager.gameplayFeatureMod.getContentIdByName(string));
        }
        ArrayList<File> genreScreenshots = DataStreamHelper.getFilesInFolderBlackList(fileScreenshotsToImport.getPath(), ".meta");
        File genreIcon = new File(importFolderPath + "//DATA//icon.png");
        addGenre(map,compatibleThemeIds, gameplayFeaturesBadIds, gameplayFeaturesGoodIds, genreScreenshots,true, genreIcon, showMessages);
        return "true";
    }

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws ModProcessingException {//TODO change to throw ModProcessingException
        ModManager.genreMod.analyzeFile();
        resetVariables();
        ModManager.genreMod.createBackup();
        LOGGER.info("Adding new genre");
        openStepWindow(1);
    }

    /**
     * Opens a window that is used to add a new genre
     * @param step The specific windows that should be opened
     */
    public static void openStepWindow(int step){
        switch(step){
            case 1: WindowAddGenrePage1.createFrame(); break;
            case 2: WindowAddGenrePage2.createFrame(); break;
            case 3: WindowAddGenrePage3.createFrame(); break;
            case 4: WindowAddGenrePage4.createFrame(); break;
            case 5: WindowAddGenrePage5.createFrame(); break;
            case 6: WindowAddGenrePage6.createFrame(); break;
            case 7: WindowAddGenrePage7.createFrame(); break;
            case 8: WindowAddGenrePage8.createFrame(); break;
            case 9: WindowAddGenrePage9.createFrame(); break;
            case 10: WindowAddGenrePage10.createFrame(); break;
            case 11: WindowAddGenrePage11.createFrame(); break;
        }
    }

    /**
     * Resets all variables used to add a new genre
     */
    public static void resetVariables(){
        mapNewGenre.clear();
        mapNewGenre.put("ID", Integer.toString(ModManager.genreMod.getFreeId()));
        mapNewGenre.put("UNLOCK YEAR", "1976");
        mapNewGenre.put("UNLOCK MONTH", "JAN");
        mapNewGenre.put("RES POINTS", "1000");
        mapNewGenre.put("DEV COSTS", "3000");
        mapNewGenre.put("PRICE", "150000");
        mapNewGenre.put("TGROUP", "");
        mapNewGenre.put("FOCUS0", "5");
        mapNewGenre.put("FOCUS1", "5");
        mapNewGenre.put("FOCUS2", "5");
        mapNewGenre.put("FOCUS3", "5");
        mapNewGenre.put("FOCUS4", "5");
        mapNewGenre.put("FOCUS5", "5");
        mapNewGenre.put("FOCUS6", "5");
        mapNewGenre.put("FOCUS7", "5");
        mapNewGenre.put("ALIGN0", "5");
        mapNewGenre.put("ALIGN1", "5");
        mapNewGenre.put("ALIGN2", "5");
        mapNewGenre.put("GAMEPLAY", "25");
        mapNewGenre.put("GRAPHIC", "25");
        mapNewGenre.put("SOUND", "25");
        mapNewGenre.put("CONTROL", "25");
        mapNameTranslations.clear();
        mapDescriptionTranslations.clear();
        WindowAddGenrePage1.clearTranslationArrayLists();
    }

    /**
     * Ads a new genre to mad games tycoon 2. Shows a summary for the genre that should be added.
     * @param map The map that includes the values.
     * @param compatibleThemeIds A set containing all compatible theme ids
     * @param genreScreenshots Array list containing all screenshot files
     * @param showSummaryFromImport True when called from genre import
     * @param genreIcon The genre icon file
     * @param showMessages True when the messages should be shown. False if not.
     * @return Returns true when the user clicked yes on the confirm popup
     * @throws ModProcessingException When something went wrong while adding the genre
     */
    public boolean addGenre(Map<String, String> map, Set<Integer> compatibleThemeIds, Set<Integer> gameplayFeaturesBadIds, Set<Integer> gameplayFeaturesGoodIds, ArrayList<File> genreScreenshots, boolean showSummaryFromImport, File genreIcon, boolean showMessages) throws ModProcessingException {
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        JLabel labelFirstPart = new JLabel("<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.mainBody.genreIsReady") + "<br><br>" +
                I18n.INSTANCE.get("commonText.id") + ":" + map.get("ID") + "<br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "<br>" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + getTargetGroups(map) + "<br>" + "");
        JButton buttonCompatibleGenres = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleGenres"), convertMapEntryToList(map, "GENRE COMB", true), I18n.INSTANCE.get("commonText.compatibleGenres") + ":");
        JButton buttonCompatibleThemes = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleThemes"), convertMapEntryToList(map, "THEME COMB"), I18n.INSTANCE.get("commonText.compatibleThemes") + ":");
        JButton buttonBadGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.badGameplayFeatures"), convertMapEntryToList(map, "GAMEPLAYFEATURE BAD"), I18n.INSTANCE.get("commonText.badGameplayFeatures") + ":");
        JButton buttonGoodGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.goodGameplayFeatures"), convertMapEntryToList(map, "GAMEPLAYFEATURE GOOD"), I18n.INSTANCE.get("commonText.goodGameplayFeatures") + ":");
        JLabel labelSecondPart = new JLabel("<html>*" + I18n.INSTANCE.get("commonText.designFocus") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameLength") + ": " + map.get("FOCUS0") + "<br>" +
                I18n.INSTANCE.get("commonText.gameDepth") + ": " + map.get("FOCUS1") + "<br>" +
                I18n.INSTANCE.get("commonText.beginnerFriendliness") + ": " + map.get("FOCUS2") + "<br>" +
                I18n.INSTANCE.get("commonText.innovation") + ": " + map.get("FOCUS3") + "<br>" +
                I18n.INSTANCE.get("commonText.story") + ": " + map.get("FOCUS4") + "<br>" +
                I18n.INSTANCE.get("commonText.characterDesign") + ": " + map.get("FOCUS5") + "<br>" +
                I18n.INSTANCE.get("commonText.levelDesign") + ": " + map.get("FOCUS6") + "<br>" +
                I18n.INSTANCE.get("commonText.missionDesign") + ": " + map.get("FOCUS7") + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.designDirection") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.coreGamersCasualGamers") + ": " + map.get("ALIGN0") + "<br>" +
                I18n.INSTANCE.get("commonText.nonviolentExtremeViolent") + ": " + map.get("ALIGN1") + "<br>" +
                I18n.INSTANCE.get("commonText.easyHard") + ": " + map.get("ALIGN2") + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.workPriority") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + map.get("GAMEPLAY") + "%<br>" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + map.get("GRAPHIC") + "%<br>" +
                I18n.INSTANCE.get("commonText.sound") + ": " + map.get("SOUND") + "%<br>" +
                I18n.INSTANCE.get("commonText.control") + ": " + map.get("CONTROL") + "%<br><br>");
        int returnValue;
        if(showSummaryFromImport){
            if(showMessages){
                labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var1"));
                Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
                returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
            }else{
                returnValue = 0;
            }
        }else{
            labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var2"));
            Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
            returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }
        if(returnValue == JOptionPane.YES_OPTION){
            //click yes
            boolean continueAnyway = false;
            boolean imageFileAccessedSuccess = false;
            try {
                ImageFileHandler.addGenreImageFiles(Integer.parseInt(map.get("ID")), map.get("NAME EN"), genreIcon, genreScreenshots);
                imageFileAccessedSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var1"), I18n.INSTANCE.get("frame.title.continueAnyway"), JOptionPane.YES_NO_OPTION) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway | imageFileAccessedSuccess){
                try {
                    addMod(map);
                    ModManager.themeMod.editGenreAllocation(Integer.parseInt(map.get("ID")), true, compatibleThemeIds);
                    ModManager.gameplayFeatureMod.addGenreId(gameplayFeaturesGoodIds, Integer.parseInt(map.get("ID")), true);
                    ModManager.gameplayFeatureMod.addGenreId(gameplayFeaturesBadIds, Integer.parseInt(map.get("ID")), false);
                    genreAdded(map, genreIcon, showMessages);
                    if(showSummaryFromImport){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN"));
                    }else{
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + mapNewGenre.get("NAME EN"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var3"));
            }
        }else if(returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION){
            //Click no or close window
            if(!showSummaryFromImport){
                WindowAddGenrePage11.createFrame();
            }
        }
        return returnValue == JOptionPane.YES_OPTION;
    }

    /**
     * @param map The map where the TGROUP values are stored.
     * @return Returns the compatible target groups to be displayed in the genre summary.
     */
    public static String getTargetGroups(Map<String, String> map){
        String targetGroups = "";
        if(map.get("TGROUP").contains("KID")){
            if(map.get("TGROUP").contains("TEEN") || map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid");
            }
        }
        if(map.get("TGROUP").contains("true")){
            if(map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen");
            }
        }
        if(map.get("TGROUP").contains("ADULT")){
            if(map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult");
            }
        }
        if(map.get("TGROUP").contains("OLD")){
            targetGroups = targetGroups + I18n.INSTANCE.get("commonText.senior");
        }
        return targetGroups;
    }

    /**
     * @param map The map where the map key is stored
     * @param mapKey The key where the content is written
     * @return Returns a string containing all entries that are listed as value under the map key.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey) throws ModProcessingException {
        return convertMapEntryToList(map, mapKey, false);
    }

    /**
     * @param map The map where the map key is stored
     * @param mapKey The key where the content is written
     * @param convertAsGenres If true the genre names will be written to the list
     * @return Returns a string containing all entries that are listed as value under the map key.
     * @throws ModProcessingException If {@link GenreMod#getContentNameById(int)} fails.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey, boolean convertAsGenres) throws ModProcessingException {
        String input = map.get(mapKey);
        StringBuilder currentString = new StringBuilder();
        ArrayList<String> outputArray = new ArrayList<>();

        for(int i=0; i<input.length(); i++){
            if(String.valueOf(input.charAt(i)).equals("<")){
                //Nothing happens
            }else if (String.valueOf(input.charAt(i)).equals(">")){
                if(convertAsGenres){
                    outputArray.add(ModManager.genreMod.getContentNameById(Integer.parseInt(currentString.toString())));
                }else{
                    outputArray.add(currentString.toString());
                }
                currentString = new StringBuilder();
            }else{
                currentString.append(input.charAt(i));
            }
        }
        Collections.sort(outputArray);
        String[] output = new String[outputArray.size()];
        outputArray.toArray(output);
        return output;
    }

    /**
     * @return Returns the image file name for the input genre name
     */
    public static String getImageFileName(String genreName){
        return "icon" + genreName.replaceAll("\\s+","");
    }

    /**
     * Shows a message to the user that the genre has been added successfully and asks if the NPC_GAMES file should be modified.
     * @param map The map containing the genre name.
     * @param genreIcon The genre icon.
     */
    public static void genreAdded(Map<String, String> map, File genreIcon, boolean showMessages) throws IOException {
        String name = map.get("NAME EN");
        int id = ModManager.genreMod.getFreeId();
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.1") + " [" + name + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.2"), I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0){
                try {
                    NPCGameListChanger.editNPCGames(id, true, 20);
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("commonText.genre") + " " + I18n.INSTANCE.get("commonText.id") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.added"));
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.1") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.2") + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }else{
            NPCGameListChanger.editNPCGames(id,true, 20);
        }
    }

    /**
     * @param genreId The genre id from which the genre comb names should be transformed
     * @return A list of genre names
     */
    public String getGenreNames(int genreId) throws ModProcessingException {
        int genrePositionInList = getPositionInFileContentListById(genreId);
        String genreNumbersRaw = getFileContent().get(genrePositionInList).get("GENRE COMB");
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString());
                if(Settings.enableDebugLogging){
                    sendLogMessage("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    sendLogMessage("currentNumber: " + currentNumber);
                }
            }
            charPosition++;
        }
        String.valueOf(genreNumbersRaw.charAt(1));
        return genreNames.toString();
    }

    /**
     * @param genreNumbersRaw The string containing the genre ids that should be transformed
     * @return A list of genre names
     * @throws ModProcessingException If {@link GenreMod#getContentNameById(int)} fails.
     */
    public String getGenreNames(String genreNumbersRaw) throws ModProcessingException {
        LOGGER.info("genreNumbersRaw: " + genreNumbersRaw);
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString().replaceAll("[^0-9]", ""));
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(ModManager.genreMod.getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentNumber);
                }
            }
            charPosition++;
        }
        return genreNames.toString();
    }

    /**
     * Sets the genreScreenshots ArrayList a new array list containing the selected files. Uses {@link GenreMod#getGenreScreenshots()} to get the Array List.
     * @param genreScreenshots The array list that should be set
     * @param button The button of which the text should be changed
     */
    public void setGenreScreenshots(AtomicReference<ArrayList<File>> genreScreenshots, JButton button){
        while(true){
            genreScreenshots.set(getGenreScreenshots());
            if(!genreScreenshots.get().isEmpty()){
                StringBuilder filePaths = new StringBuilder();
                for (File arrayListScreenshotFile : genreScreenshots.get()) {
                    filePaths.append("<br>").append(arrayListScreenshotFile);
                }
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.followingImageFilesHaveBeenAdded.firstPart") + "<br>" + filePaths + "<br><br>" + I18n.INSTANCE.get("commonText.isThisCorrect"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == 0){
                    button.setText(I18n.INSTANCE.get("commonText.screenshots.added"));
                    break;
                }
            }else{
                button.setText(I18n.INSTANCE.get("commonText.addScreenshots"));
                break;
            }
        }
    }

    /**
     * Opens a ui where the user can select image files.
     * @return Returns the image files as ArrayList
     */
    private ArrayList<File> getGenreScreenshots(){
        ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
        ArrayList<File> arrayListScreenshotFilesSelected = new ArrayList<>();
        JTextField textFieldScreenshotFile = new JTextField();
        JLabel labelMessage = new JLabel(I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.message"));
        JButton buttonBrowse = new JButton(I18n.INSTANCE.get("commonText.browse"));
        AtomicBoolean multipleFilesSelected = new AtomicBoolean(false);
        AtomicInteger numberOfScreenshotsToAdd = new AtomicInteger();
        buttonBrowse.addActionListener(actionEventSmall ->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFiles.fileChooser"));
                fileChooser.setMultiSelectionEnabled(true);

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    final int NUMBER_OF_SCREENSHOTS = fileChooser.getSelectedFiles().length;
                    numberOfScreenshotsToAdd.set(NUMBER_OF_SCREENSHOTS);
                    File[] screenshots = fileChooser.getSelectedFiles();
                    if(NUMBER_OF_SCREENSHOTS > 1){
                        multipleFilesSelected.set(true);
                    }
                    boolean failed = false;
                    for(int i=0; i<NUMBER_OF_SCREENSHOTS; i++){
                        if(!failed){
                            if(screenshots[i].getName().contains(".png")){
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                                if(multipleFilesSelected.get()){
                                    arrayListScreenshotFilesSelected.add(screenshots[i]);
                                    textFieldScreenshotFile.setText(I18n.INSTANCE.get("commonText.multipleFilesSelected"));
                                }else{
                                    textFieldScreenshotFile.setText(fileChooser.getSelectedFile().getPath());
                                }
                            }else{
                                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectOnlyPngFile"));
                                failed = true;
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                            }
                        }
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
        Object[] params = {labelMessage,textFieldScreenshotFile, buttonBrowse};
        if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.addScreenshot"), JOptionPane.OK_CANCEL_OPTION) == 0){
            String textFieldPath = textFieldScreenshotFile.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    arrayListScreenshotFiles.add(new File(textFieldPath));
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFileAdded"));
                }else{
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.fileNotFound"), JOptionPane.ERROR_MESSAGE);
                }
            }else if(multipleFilesSelected.get()){
                for(int i = 0; i< numberOfScreenshotsToAdd.get(); i++){
                    arrayListScreenshotFiles.add(arrayListScreenshotFilesSelected.get(i));
                }
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFilesAdded"));
            }else{
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));

            }
            return arrayListScreenshotFiles;
        }
        return arrayListScreenshotFiles;
    }

    public String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog, JTextField textFieldImagePath) {
        if(useTextFiledPath){
            String textFieldPath = textFieldImagePath.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    if(showDialog){
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return textFieldPath;
                }else{
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            }else{
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                return "error";
            }
        }else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFile.fileChooser"));

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    if(fileChooser.getSelectedFile().getName().contains(".png")){
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return "error";
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                return "error";
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }
}
