package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractComplexMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GenreMod extends AbstractComplexMod {

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
        if (map.containsKey("PIC")) {
            EditHelper.printLine("PIC", map, bw);
        } else {
            bw.write("[PIC]icon" + map.get("NAME EN").replaceAll(" ", "") + ".png");
            bw.write("\r\n");
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
        return new String[]{"3.0.0-alpha-1", MadGamesTycoon2ModTool.VERSION};
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
    public String getExportType() {
        return "genre";
    }

    @Override
    public String getGameFileName() {
        return "Genres.txt";
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
        throw new ModProcessingException("Call to getOptionPaneMessage(T t) is invalid. This function is not implemented for genre mod");
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "GENRE COMB");
        replaceMapEntry(map, missingDependency, replacement, "THEME COMB");
        replaceMapEntry(map, missingDependency, replacement, "GAMEPLAYFEATURE BAD");
        replaceMapEntry(map, missingDependency, replacement, "GAMEPLAYFEATURE GOOD");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.themeMod);
        arrayList.add(ModManager.gameplayFeatureMod);
        arrayList.add(ModManager.genreMod);
        return arrayList;
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        ModManager.themeMod.editGenreAllocation(getContentIdByName(name), false, null);
        ModManager.gameplayFeatureMod.removeGenreId(getContentIdByName(name));
        ModManager.publisherMod.removeGenre(name);
        ModManager.npcEngineMod.removeGenre(name);
        NpcGamesMod.editNPCGames(ModManager.genreMod.getContentIdByName(name), false, 0);
        super.removeModFromFile(name);
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        map.put("GENRE COMB", convertGenreNamesToId(map.get("GENRE COMB"), true));
        return map;
    }

    @Override
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        int id = getContentIdByName(name);
        map.replace("GENRE COMB", getGenreNames(id));
        map.put("THEME COMB", Utils.getCompatibleThemeIdsForGenre(ModManager.genreMod.getPositionInFileContentListById(id)));
        map.put("GAMEPLAYFEATURE GOOD", Utils.getCompatibleGameplayFeatureIdsForGenre(id, true));
        map.put("GAMEPLAYFEATURE BAD", Utils.getCompatibleGameplayFeatureIdsForGenre(id, false));
        return map;
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        Map<String, String> modMap = transformGenericToMap(t);
        Map<String, Object> map = new HashMap<>();
        map.put(getExportType(), new HashSet<>(Utils.getEntriesFromString(modMap.get("GENRE COMB"))));
        map.put(ModManager.themeMod.getExportType(), new HashSet<>(Utils.getEntriesFromString(modMap.get("THEME COMB"))));
        Set<String> gameplayFeatures = new HashSet<>();
        gameplayFeatures.addAll(Utils.getEntriesFromString(modMap.get("GAMEPLAYFEATURE GOOD")));
        gameplayFeatures.addAll(Utils.getEntriesFromString(modMap.get("GAMEPLAYFEATURE BAD")));
        map.put(ModManager.gameplayFeatureMod.getExportType(), gameplayFeatures);
        return map;
    }

    @Override
    public Map<String, String> exportImages(String name, Path assetsFolder) throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        int id = getContentIdByName(name);
        String iconName = getType().toLowerCase().replaceAll(" ", "_") + "_" + name.toLowerCase().replaceAll(" ", "_") + "_icon.png";
        File fileExportedGenreIcon = assetsFolder.resolve(iconName).toFile();
        try {
            if (!fileExportedGenreIcon.exists()) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.exportingImage") + ": " + getType() + " - " + iconName);
                File fileGenreIconToExport = MGT2Paths.GENRE_ICONS.getPath().resolve(getSingleContentMapByName(name).get("PIC")).toFile();
                Files.copy(fileGenreIconToExport.toPath(), fileExportedGenreIcon.toPath());
                map.put("iconName", iconName);
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.imageNotExported") + ": " + MGT2Paths.GENRE_ICONS.getPath().resolve(getSingleContentMapByName(name).get("PIC")).toFile());
            }
            for (File file : DataStreamHelper.getFilesInFolder(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(id)))) {
                if (file.getName().endsWith(".png")) {
                    String imageName = Utils.convertName(getType()) + "_" + Utils.convertName(name) + "_screenshot_" + file.getName();
                    File exportedImage = assetsFolder.resolve(imageName).toFile();
                    map.put("screenshot" + file.getName().replace(".png", "") + "Name", imageName);
                    if (!exportedImage.exists()) {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.exportingImage") + ": " + getType() + " - " + imageName);
                        Files.copy(file.toPath(), exportedImage.toPath());
                    } else {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.imageNotExported") + ": " + file);
                    }
                }
            }
        } catch (IOException e) {
            throw new ModProcessingException("Genre image files could not be copied", e);
        }
        return map;
    }

    @Override
    protected Map<String, String> importImages(Map<String, String> map) throws ModProcessingException {
        return new HashMap<>();
    }

    @Override
    public void removeImageFiles(String name) throws ModProcessingException {
        ImageFileHandler.removeImageFiles(name);
    }

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeFile();
        analyzeDependencies();
        Map<String, String> importMap = getChangedImportMap(Utils.transformObjectMapToStringMap(map));
        importMap.put("ID", Integer.toString(getModIdByNameFromImportHelperMap(map.get("mod_name").toString())));
        int newGenreId = ModManager.genreMod.getFreeId();
        Path screenshotFolder = MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(newGenreId));
        for (Map.Entry<String, String> entry : importMap.entrySet()) {
            if (entry.getKey().equals("THEME COMB")) {
                ArrayList<String> arrayList = Utils.getEntriesFromString(entry.getValue());
                StringBuilder themeIds = new StringBuilder();
                ArrayList<String> themes = new ArrayList<>(Arrays.asList(ModManager.themeMod.getContentByAlphabet()));
                for (String string : arrayList) {
                    if (themes.contains(string)) {
                        themeIds.append("<").append(string).append(">");
                    }
                }
                map.put("THEME COMB", themeIds.toString());
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        if (screenshotFolder.toFile().exists()) {
            try {
                DataStreamHelper.deleteDirectory(screenshotFolder);
            } catch (IOException e) {
                throw new ModProcessingException("Unable to delete screenshot folder: " + screenshotFolder, e);
            }
        }
        Set<Integer> compatibleThemeIds = new HashSet<>();
        for (String string : Utils.getEntriesFromString(importMap.get("THEME COMB"))) {
            compatibleThemeIds.add(ModManager.themeMod.getPositionOfThemeInFile(string));
        }
        Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
        Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
        for (String string : Utils.getEntriesFromString(importMap.get("GAMEPLAYFEATURE BAD"))) {
            gameplayFeaturesBadIds.add(ModManager.gameplayFeatureMod.getModIdByNameFromImportHelperMap(string));
        }
        for (String string : Utils.getEntriesFromString(importMap.get("GAMEPLAYFEATURE GOOD"))) {
            gameplayFeaturesGoodIds.add(ModManager.gameplayFeatureMod.getModIdByNameFromImportHelperMap(string));
        }
        ArrayList<File> genreScreenshots = DataStreamHelper.getFilesInFolderWhiteList(Paths.get("assets_folder"), "genre_" + map.get("mod_name").toString().toLowerCase() + "_screenshot");
        File genreIcon = Paths.get(importMap.get("assets_folder")).resolve(importMap.get("iconName")).toFile();
        addGenre(importMap, compatibleThemeIds, gameplayFeaturesBadIds, gameplayFeaturesGoodIds, genreScreenshots, true, genreIcon, false);
    }

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws ModProcessingException {
        ModManager.genreMod.analyzeFile();
        resetVariables();
        ModManager.genreMod.createBackup();
        LOGGER.info("Adding new genre");
        openStepWindow(1);
    }

    /**
     * Opens a window that is used to add a new genre
     *
     * @param step The specific windows that should be opened
     */
    public static void openStepWindow(int step) {
        switch (step) {
            case 1:
                WindowAddGenrePage1.createFrame();
                break;
            case 2:
                WindowAddGenrePage2.createFrame();
                break;
            case 3:
                WindowAddGenrePage3.createFrame();
                break;
            case 4:
                WindowAddGenrePage4.createFrame();
                break;
            case 5:
                WindowAddGenrePage5.createFrame();
                break;
            case 6:
                WindowAddGenrePage6.createFrame();
                break;
            case 7:
                WindowAddGenrePage7.createFrame();
                break;
            case 8:
                WindowAddGenrePage8.createFrame();
                break;
            case 9:
                WindowAddGenrePage9.createFrame();
                break;
            case 10:
                WindowAddGenrePage10.createFrame();
                break;
            case 11:
                WindowAddGenrePage11.createFrame();
                break;
        }
    }

    /**
     * Resets all variables used to add a new genre
     */
    public static void resetVariables() {
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
     *
     * @param map                The map that includes the values.
     * @param compatibleThemeIds A set containing all compatible theme ids
     * @param genreScreenshots   Array list containing all screenshot files
     * @param calledFromImport   True when called from genre import
     * @param genreIcon          The genre icon file
     * @param showMessages       True when the messages should be shown. False if not.
     * @return Returns true when the user clicked yes on the confirmation popup
     * @throws ModProcessingException When something went wrong while adding the genre
     */
    public boolean addGenre(Map<String, String> map, Set<Integer> compatibleThemeIds, Set<Integer> gameplayFeaturesBadIds, Set<Integer> gameplayFeaturesGoodIds, ArrayList<File> genreScreenshots, boolean calledFromImport, File genreIcon, boolean showMessages) throws ModProcessingException {
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
        JButton buttonCompatibleGenres = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleGenres"), convertMapEntryToList(map, "GENRE COMB", true, calledFromImport), I18n.INSTANCE.get("commonText.compatibleGenres") + ":");
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
        if (calledFromImport) {
            if (showMessages) {
                labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var1"));
                Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
                returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
            } else {
                returnValue = 0;
            }
        } else {
            labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var2"));
            Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
            returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }
        if (returnValue == JOptionPane.YES_OPTION) {
            //click yes
            try {
                ImageFileHandler.addGenreImageFiles(Integer.parseInt(map.get("ID")), map.get("NAME EN"), genreIcon, genreScreenshots);
            } catch (IOException e) {
                throw new ModProcessingException("Unable to process genre image files", e);
            }
            try {
                addModToFile(map);
                ModManager.themeMod.editGenreAllocation(Integer.parseInt(map.get("ID")), true, compatibleThemeIds);
                ModManager.gameplayFeatureMod.addGenreId(gameplayFeaturesGoodIds, Integer.parseInt(map.get("ID")), true);
                ModManager.gameplayFeatureMod.addGenreId(gameplayFeaturesBadIds, Integer.parseInt(map.get("ID")), false);
                genreAdded(map, genreIcon, showMessages);
                if (calledFromImport) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
                } else {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + mapNewGenre.get("NAME EN"));
                }
            } catch (ModProcessingException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) {
            //Click no or close window
            if (!calledFromImport) {
                WindowAddGenrePage11.createFrame();
            }
        }
        return returnValue == JOptionPane.YES_OPTION;
    }

    /**
     * @param map The map where the TGROUP values are stored.
     * @return Returns the compatible target groups to be displayed in the genre summary.
     */
    public static String getTargetGroups(Map<String, String> map) {
        String targetGroups = "";
        if (map.get("TGROUP").contains("KID")) {
            if (map.get("TGROUP").contains("TEEN") || map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")) {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid") + ", ";
            } else {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid");
            }
        }
        if (map.get("TGROUP").contains("true")) {
            if (map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")) {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen") + ", ";
            } else {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen");
            }
        }
        if (map.get("TGROUP").contains("ADULT")) {
            if (map.get("TGROUP").contains("OLD")) {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult") + ", ";
            } else {
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult");
            }
        }
        if (map.get("TGROUP").contains("OLD")) {
            targetGroups = targetGroups + I18n.INSTANCE.get("commonText.senior");
        }
        return targetGroups;
    }

    /**
     * @param map    The map where the map key is stored
     * @param mapKey The key where the content is written
     * @return Returns a string containing all entries that are listed as value under the map key.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey) throws ModProcessingException {
        return convertMapEntryToList(map, mapKey, false, false);
    }

    /**
     * @param map             The map where the map key is stored
     * @param mapKey          The key where the content is written
     * @param convertAsGenres If true the genre names will be written to the list
     * @return Returns a string containing all entries that are listed as value under the map key.
     * @throws ModProcessingException If {@link GenreMod#getContentNameById(int)} fails.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey, boolean convertAsGenres, boolean importMod) throws ModProcessingException {
        String input = map.get(mapKey);
        StringBuilder currentString = new StringBuilder();
        ArrayList<String> outputArray = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            if (String.valueOf(input.charAt(i)).equals("<")) {
                //Nothing happens
            } else if (String.valueOf(input.charAt(i)).equals(">")) {
                if (convertAsGenres) {
                    if (importMod) {
                        outputArray.add(ModManager.genreMod.getModNameByIdFromImportHelperMap(Integer.parseInt(currentString.toString())));
                    } else {
                        outputArray.add(ModManager.genreMod.getContentNameById(Integer.parseInt(currentString.toString())));
                    }
                } else {
                    outputArray.add(currentString.toString());
                }
                currentString = new StringBuilder();
            } else {
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
    public static String getImageFileName(String genreName) {
        return "icon" + genreName.replaceAll("\\s+", "");
    }

    /**
     * Shows a message to the user that the genre has been added successfully and asks if the NPC_GAMES file should be modified.
     *
     * @param map       The map containing the genre name.
     * @param genreIcon The genre icon.
     */
    public static void genreAdded(Map<String, String> map, File genreIcon, boolean showMessages) throws ModProcessingException {
        String name = map.get("NAME EN");
        int id = ModManager.genreMod.getFreeId();
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        if (showMessages) {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.1") + " [" + name + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.2"), I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0) {
                try {
                    NpcGamesMod.editNPCGames(id, true, 20);
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("commonText.genre") + " " + I18n.INSTANCE.get("commonText.id") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.added"));
                } catch (ModProcessingException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.1") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.2") + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            NpcGamesMod.editNPCGames(id, true, 20);
        }
    }

    /**
     * @param genreId The genre id from which the genre comb names should be transformed
     * @return A list of genre names
     */
    private String getGenreNames(int genreId) throws ModProcessingException {
        int genrePositionInList = getPositionInFileContentListById(genreId);
        String genreNumbersRaw = getFileContent().get(genrePositionInList).get("GENRE COMB");
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < genreNumbersRaw.length(); i++) {
            if (String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")) {
                //Nothing happens
            } else if (String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")) {
                int genreNumber = Integer.parseInt(currentNumber.toString());
                genreNames.append("<").append(getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            } else {
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
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
        DebugHelper.debug(LOGGER, "genreNumbersRaw: " + genreNumbersRaw);
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < genreNumbersRaw.length(); i++) {
            if (String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")) {
                //Nothing happens
            } else if (String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")) {
                int genreNumber = Integer.parseInt(currentNumber.toString().replaceAll("[^0-9]", ""));
                genreNames.append("<").append(ModManager.genreMod.getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            } else {
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
            }
            charPosition++;
        }
        return genreNames.toString();
    }

    /**
     * Sets the genreScreenshots ArrayList a new array list containing the selected files. Uses {@link GenreMod#getGenreScreenshots()} to get the Array List.
     *
     * @param genreScreenshots The array list that should be set
     * @param button           The button of which the text should be changed
     */
    public void setGenreScreenshots(AtomicReference<ArrayList<File>> genreScreenshots, JButton button) {
        while (true) {
            genreScreenshots.set(getGenreScreenshots());
            if (!genreScreenshots.get().isEmpty()) {
                StringBuilder filePaths = new StringBuilder();
                for (File arrayListScreenshotFile : genreScreenshots.get()) {
                    filePaths.append("<br>").append(arrayListScreenshotFile);
                }
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.followingImageFilesHaveBeenAdded.firstPart") + "<br>" + filePaths + "<br><br>" + I18n.INSTANCE.get("commonText.isThisCorrect"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == 0) {
                    button.setText(I18n.INSTANCE.get("commonText.screenshots.added"));
                    break;
                }
            } else {
                button.setText(I18n.INSTANCE.get("commonText.addScreenshots"));
                break;
            }
        }
    }

    /**
     * Opens a ui where the user can select image files.
     *
     * @return Returns the image files as ArrayList
     */
    private ArrayList<File> getGenreScreenshots() {
        ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
        ArrayList<File> arrayListScreenshotFilesSelected = new ArrayList<>();
        JTextField textFieldScreenshotFile = new JTextField();
        JLabel labelMessage = new JLabel(I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.message"));
        JButton buttonBrowse = new JButton(I18n.INSTANCE.get("commonText.browse"));
        AtomicBoolean multipleFilesSelected = new AtomicBoolean(false);
        AtomicInteger numberOfScreenshotsToAdd = new AtomicInteger();
        buttonBrowse.addActionListener(actionEventSmall -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if (f.getName().contains(".png")) {
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
                    if (NUMBER_OF_SCREENSHOTS > 1) {
                        multipleFilesSelected.set(true);
                    }
                    boolean failed = false;
                    for (int i = 0; i < NUMBER_OF_SCREENSHOTS; i++) {
                        if (!failed) {
                            if (screenshots[i].getName().contains(".png")) {
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                                if (multipleFilesSelected.get()) {
                                    arrayListScreenshotFilesSelected.add(screenshots[i]);
                                    textFieldScreenshotFile.setText(I18n.INSTANCE.get("commonText.multipleFilesSelected"));
                                } else {
                                    textFieldScreenshotFile.setText(fileChooser.getSelectedFile().getPath());
                                }
                            } else {
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
        Object[] params = {labelMessage, textFieldScreenshotFile, buttonBrowse};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.addScreenshot"), JOptionPane.OK_CANCEL_OPTION) == 0) {
            String textFieldPath = textFieldScreenshotFile.getText();
            if (textFieldPath.endsWith(".png")) {
                File imageFile = new File(textFieldPath);
                if (imageFile.exists()) {
                    arrayListScreenshotFiles.add(new File(textFieldPath));
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFileAdded"));
                } else {
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.fileNotFound"), JOptionPane.ERROR_MESSAGE);
                }
            } else if (multipleFilesSelected.get()) {
                for (int i = 0; i < numberOfScreenshotsToAdd.get(); i++) {
                    arrayListScreenshotFiles.add(arrayListScreenshotFilesSelected.get(i));
                }
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFilesAdded"));
            } else {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));

            }
            return arrayListScreenshotFiles;
        }
        return arrayListScreenshotFiles;
    }

    public String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog, JTextField textFieldImagePath) {
        if (useTextFiledPath) {
            String textFieldPath = textFieldImagePath.getText();
            if (textFieldPath.endsWith(".png")) {
                File imageFile = new File(textFieldPath);
                if (imageFile.exists()) {
                    if (showDialog) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return textFieldPath;
                } else {
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            } else {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                return "error";
            }
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if (f.getName().contains(".png")) {
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
                    if (fileChooser.getSelectedFile().getName().contains(".png")) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    } else {
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

    /**
     * Takes the input string and replaces the genreNames with the corresponding genre id.
     *
     * @param useImportHelperMap If true {@link AbstractBaseMod#getModIdByNameFromImportHelperMap(String)} will be used to retrieve the mod id.
     *                           If false {@link AbstractBaseMod#getContentIdByName(String)} will be used to retrieve the mod id.
     * @return Returns a list of genre ids.
     * @throws ModProcessingException If {@link AbstractBaseMod#getContentIdByName(String)} or {@link AbstractBaseMod#getModIdByNameFromImportHelperMap(String)} fails.
     */
    public static String convertGenreNamesToId(String genreNamesRaw, boolean useImportHelperMap) throws ModProcessingException {
        if (genreNamesRaw.length() > 0) {
            StringBuilder genreIds = new StringBuilder();
            int charPosition = 0;
            StringBuilder currentName = new StringBuilder();
            for (int i = 0; i < genreNamesRaw.length(); i++) {
                if (String.valueOf(genreNamesRaw.charAt(charPosition)).equals("<")) {
                    //Nothing happens
                } else if (String.valueOf(genreNamesRaw.charAt(charPosition)).equals(">")) {
                    int genreId;
                    if (useImportHelperMap) {
                        genreId = ModManager.genreMod.getModIdByNameFromImportHelperMap(currentName.toString());
                    } else {
                        genreId = ModManager.genreMod.getContentIdByName(currentName.toString());
                    }
                    if (genreId != -1) {
                        genreIds.append("<").append(genreId).append(">");
                    }
                    currentName = new StringBuilder();
                } else {
                    currentName.append(genreNamesRaw.charAt(charPosition));
                }
                charPosition++;
            }
            String.valueOf(genreNamesRaw.charAt(1));
            return genreIds.toString();
        } else {
            return "";
        }
    }
}
