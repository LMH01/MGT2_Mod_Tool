package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GenreSharer extends AbstractAdvancedSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreSharer.class);

    @Override
    void doOtherImportThings(String importFolderPath) {

    }

    @Override
    void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) {

    }

    @Override
    public boolean exportMod(String name, boolean exportAsRestorePoint) {
        try {
            int genreId = AnalyzeManager.genreAnalyzer.getContentIdByName(name);
            int positionInGenreList = AnalyzeManager.genreAnalyzer.getPositionInFileContentListById(genreId);
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
            File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + "icon" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("NAME EN").replaceAll(" ", "") + ".png");
            File fileGenreScreenshotsToExport = new File(Utils.getMGT2ScreenshotsPath() + genreId);
            if(!fileExportedGenreIcon.exists()){
                fileDataFolder.mkdirs();
            }
            if(fileExportedGenreIcon.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.genreExportFailed.alreadyExported") + " " + name);
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
                bw.print("[NAME " + translationKey + "]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("NAME " + translationKey)  + System.getProperty("line.separator"));
                bw.print("[DESC " + translationKey + "]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("DESC " + translationKey)  + System.getProperty("line.separator"));
            }
            bw.print("[DATE]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("DATE") + System.getProperty("line.separator"));
            bw.print("[RES POINTS]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("RES POINTS") + System.getProperty("line.separator"));
            bw.print("[PRICE]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("PRICE") + System.getProperty("line.separator"));
            bw.print("[DEV COSTS]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("DEV COSTS") + System.getProperty("line.separator"));
            bw.print("[TGROUP]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("TGROUP") + System.getProperty("line.separator"));
            bw.print("[GAMEPLAY]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("GAMEPLAY") + System.getProperty("line.separator"));
            bw.print("[GRAPHIC]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("GRAPHIC") + System.getProperty("line.separator"));
            bw.print("[SOUND]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("SOUND") + System.getProperty("line.separator"));
            bw.print("[CONTROL]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("CONTROL") + System.getProperty("line.separator"));
            bw.print("[GENRE COMB]" + AnalyzeManager.genreAnalyzer.getGenreNames(genreId) + System.getProperty("line.separator"));
            bw.print("[THEME COMB]" + Utils.getCompatibleThemeIdsForGenre(positionInGenreList) + System.getProperty("line.separator"));
            bw.print("[FOCUS0]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS0") + System.getProperty("line.separator"));
            bw.print("[FOCUS1]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS1") + System.getProperty("line.separator"));
            bw.print("[FOCUS2]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS2") + System.getProperty("line.separator"));
            bw.print("[FOCUS3]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS3") + System.getProperty("line.separator"));
            bw.print("[FOCUS4]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS4") + System.getProperty("line.separator"));
            bw.print("[FOCUS5]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS5") + System.getProperty("line.separator"));
            bw.print("[FOCUS6]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS6") + System.getProperty("line.separator"));
            bw.print("[FOCUS7]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("FOCUS7") + System.getProperty("line.separator"));
            bw.print("[ALIGN0]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("ALIGN0") + System.getProperty("line.separator"));
            bw.print("[ALIGN1]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("ALIGN1") + System.getProperty("line.separator"));
            bw.print("[ALIGN2]" + AnalyzeManager.genreAnalyzer.getFileContent().get(positionInGenreList).get("ALIGN2") + System.getProperty("line.separator"));
            bw.print("[GAMEPLAYFEATURE GOOD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, true) + System.getProperty("line.separator"));
            bw.print("[GAMEPLAYFEATURE BAD]" + Utils.getCompatibleGameplayFeatureIdsForGenre(genreId, false) + System.getProperty("line.separator"));
            bw.print("[GENRE END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.genreExportSuccessful") + " " + name);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] - " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage());
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws IOException {
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + I18n.INSTANCE.get("window.main.share.export.genre"));
        AnalyzeManager.genreAnalyzer.analyzeFile();
        AnalyzeManager.gameplayFeatureAnalyzer.analyzeFile();
        int newGenreId = AnalyzeManager.genreAnalyzer.getFreeId();
        File fileGenreToImport = new File(importFolderPath + "\\genre.txt");
        File fileScreenshotFolder = new File(Utils.getMGT2ScreenshotsPath() + "//" + newGenreId);
        File fileScreenshotsToImport = new File(importFolderPath + "//DATA//screenshots//");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileGenreToImport);
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
                    themeIds.append("<").append(AnalyzeManager.themeFileEnAnalyzer.getFileContent().get(idToSearch+1)).append(">");
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
        for(Map<String, String> map2 : AnalyzeManager.genreAnalyzer.getFileContent()){
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
            compatibleThemeIds.add(ThemeFileAnalyzer.getPositionOfThemeInFile(string));
        }
        Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
        Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE BAD"))){
            gameplayFeaturesBadIds.add(AnalyzeManager.gameplayFeatureAnalyzer.getContentIdByName(string));
        }
        for(String string : Utils.getEntriesFromString(map.get("GAMEPLAYFEATURE GOOD"))){
            gameplayFeaturesGoodIds.add(AnalyzeManager.gameplayFeatureAnalyzer.getContentIdByName(string));
        }
        ArrayList<File> genreScreenshots = DataStreamHelper.getFilesInFolderBlackList(fileScreenshotsToImport.getPath(), ".meta");
        File genreIcon = new File(importFolderPath + "//DATA//icon.png");
        GenreManager.addGenre(map, map,compatibleThemeIds, gameplayFeaturesBadIds, gameplayFeaturesGoodIds, genreScreenshots,true, genreIcon, showMessages);
        return "true";
    }

    @Override
    void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {

    }

    @Override
    Importer getImporter() {
        return EditorManager.genreEditor::addMod;
    }

    @Override
    String getOptionPaneMessage(Map<String, String> map) {
        return null;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre");
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return AnalyzeManager.genreAnalyzer;
    }

    @Override
    public String getExportFolder() {
        return "//Genre//";
    }

    @Override
    public String getFileName() {
        return "genre.txt";
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public String getTypeCaps() {
        return "GENRE";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }
}
