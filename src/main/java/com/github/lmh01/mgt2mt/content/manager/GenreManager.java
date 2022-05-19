package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.Genre;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.managed.types.TagType;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.TargetGroup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.windows.genre.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenreManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GenreManager INSTANCE = new GenreManager();

    public static final String compatibleModToolVersions[] = new String[]{"4.0.0", MadGamesTycoon2ModTool.VERSION};

    private GenreManager() {
        super("genre", "genre", "default_genres.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Genres.txt").toFile(), StandardCharsets.UTF_8);
    }

    public void addContent(AbstractBaseContent content, boolean showMessages) throws ModProcessingException {
        addGenre((Genre) content, showMessages);
    }

    @Override
    public void addContent(AbstractBaseContent content) throws ModProcessingException {
        addGenre((Genre) content, false);
    }

    @Override
    public void removeContent(String name) throws ModProcessingException {
        ThemeManager.editGenreAllocation(getContentIdByName(name), false, null);
        GameplayFeatureManager.INSTANCE.removeGenreId(getContentIdByName(name));
        PublisherManager.INSTANCE.removeGenre(name);
        NpcEngineManager.INSTANCE.removeGenre(name);
        NpcGameManager.INSTANCE.editNPCGames(getContentIdByName(name), false, 0);
        super.removeContent(name);
    }

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
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        int id = Integer.parseInt(map.get("ID"));
        Image icon = new Image(new File(String.valueOf(MGT2Paths.GENRE_ICONS.getPath().resolve(map.get("PIC")))));
        ArrayList<Image> screenshots = new ArrayList<>();
        for (File file : DataStreamHelper.getFilesInFolder(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(id)))) {
            if (file.getName().endsWith(".png")) {
                screenshots.add(new Image(file));
            }
        }
        ArrayList<TargetGroup> targetGroups = new ArrayList<>();
        for (TargetGroup tg : TargetGroup.values()) {
            if (map.get("TGROUP").contains(tg.getDataType())) {
                targetGroups.add(tg);
            }
        }
        //TODO see if some calls here can be simplified
        ArrayList<Integer> compatibleGenres = Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("GENRE COMB")));
        ArrayList<Integer> compatibleThemes = Utils.getCompatibleThemeIdsForGenreNew(id);
        ArrayList<Integer> badGameplayFeatures = Utils.getContentIdsFromString(GameplayFeatureManager.INSTANCE, Utils.getCompatibleGameplayFeatureIdsForGenre(id, false));
        ArrayList<Integer> goodGameplayFeatures = Utils.getContentIdsFromString(GameplayFeatureManager.INSTANCE, Utils.getCompatibleGameplayFeatureIdsForGenre(id, true));
        return new Genre(
                map.get("NAME EN"),
                id,
                new TranslationManager(map),
                map.get("DESC EN"),
                map.get("DATE"),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                icon,
                screenshots,
                targetGroups,
                Integer.parseInt(map.get("GAMEPLAY")),
                Integer.parseInt(map.get("GRAPHIC")),
                Integer.parseInt(map.get("SOUND")),
                Integer.parseInt(map.get("CONTROL")),
                compatibleGenres,
                compatibleThemes,
                badGameplayFeatures,
                goodGameplayFeatures,
                Integer.parseInt(map.get("FOCUS0")),
                Integer.parseInt(map.get("FOCUS1")),
                Integer.parseInt(map.get("FOCUS2")),
                Integer.parseInt(map.get("FOCUS3")),
                Integer.parseInt(map.get("FOCUS4")),
                Integer.parseInt(map.get("FOCUS5")),
                Integer.parseInt(map.get("FOCUS6")),
                Integer.parseInt(map.get("FOCUS7")),
                Integer.parseInt(map.get("ALIGN0")),
                Integer.parseInt(map.get("ALIGN1")),
                Integer.parseInt(map.get("ALIGN2"))
        );
    }

    @Override
    protected Map<String, TagType> getIntegrityCheckMap() {
        Map<String, TagType> map = new HashMap<>();
        map.put("DESC EN", TagType.STRING);
        map.put("DATE", TagType.STRING);
        map.put("RES POINTS", TagType.INT);
        map.put("PRICE", TagType.INT);
        map.put("DEV COSTS", TagType.INT);
        map.put("GAMEPLAY", TagType.INT);
        map.put("GRAPHIC", TagType.INT);
        map.put("SOUND", TagType.INT);
        map.put("CONTROL", TagType.INT);
        map.put("FOCUS0", TagType.INT);
        map.put("FOCUS1", TagType.INT);
        map.put("FOCUS2", TagType.INT);
        map.put("FOCUS3", TagType.INT);
        map.put("FOCUS4", TagType.INT);
        map.put("FOCUS5", TagType.INT);
        map.put("FOCUS6", TagType.INT);
        map.put("FOCUS7", TagType.INT);
        map.put("ALIGN0", TagType.INT);
        map.put("ALIGN1", TagType.INT);
        map.put("ALIGN2", TagType.INT);
        return map;
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        String name = String.valueOf(map.get("NAME EN"));
        Image icon = new Image(assetsFolder.resolve(String.valueOf(map.get("iconName"))).toFile(),
                MGT2Paths.GENRE_ICONS.getPath().resolve("icon" + name.replaceAll(" ", "").trim() + ".png").toFile());
        ArrayList<Image> screenshots = new ArrayList<>();
        ArrayList<File> screenshotsToAdd = DataStreamHelper.getFilesInFolderWhiteList(assetsFolder, getExportImageName("screenshot_", name));
        for (File file : screenshotsToAdd) {
            if (file.getName().endsWith(".png")) {
                screenshots.add(new Image(file, new File(file.getName().replaceAll("[^0-9]", "") + ".png")));
            }
        }
        ArrayList<TargetGroup> targetGroups = new ArrayList<>();
        for (TargetGroup tg : TargetGroup.values()) {
            if (String.valueOf(map.get("TGROUP")).contains(tg.getDataType())) {
                targetGroups.add(tg);
            }
        }
        ArrayList<Integer> compatibleGenres = SharingHelper.transformContentNamesToIds(this, String.valueOf(map.get("GENRE COMB")));
        ArrayList<Integer> compatibleThemes = SharingHelper.transformContentNamesToIds(ThemeManager.INSTANCE, String.valueOf(map.get("THEME COMB")));
        ArrayList<Integer> badGameplayFeatures = SharingHelper.transformContentNamesToIds(GameplayFeatureManager.INSTANCE, String.valueOf(map.get("GAMEPLAYFEATURE BAD")));
        ArrayList<Integer> goodGameplayFeatures = SharingHelper.transformContentNamesToIds(GameplayFeatureManager.INSTANCE, String.valueOf(map.get("GAMEPLAYFEATURE GOOD")));
        return new Genre(
                String.valueOf(map.get("NAME EN")),
                getIdFromMap(map),
                new TranslationManager(map),
                String.valueOf(map.get("DESC EN")),
                String.valueOf(map.get("DATE")),
                Integer.parseInt(String.valueOf(map.get("RES POINTS"))),
                Integer.parseInt(String.valueOf(map.get("PRICE"))),
                Integer.parseInt(String.valueOf(map.get("DEV COSTS"))),
                icon,
                screenshots,
                targetGroups,
                Integer.parseInt(String.valueOf(map.get("GAMEPLAY"))),
                Integer.parseInt(String.valueOf(map.get("GRAPHIC"))),
                Integer.parseInt(String.valueOf(map.get("SOUND"))),
                Integer.parseInt(String.valueOf(map.get("CONTROL"))),
                compatibleGenres,
                compatibleThemes,
                badGameplayFeatures,
                goodGameplayFeatures,
                Integer.parseInt(String.valueOf(map.get("FOCUS0"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS1"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS2"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS3"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS4"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS5"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS6"))),
                Integer.parseInt(String.valueOf(map.get("FOCUS7"))),
                Integer.parseInt(String.valueOf(map.get("ALIGN0"))),
                Integer.parseInt(String.valueOf(map.get("ALIGN1"))),
                Integer.parseInt(String.valueOf(map.get("ALIGN2")))
        );
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        analyzeFile();
        ThemeManager.INSTANCE.analyzeFile();
        startStepByStepGuide();
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "GENRE COMB");
        replaceMapEntry(map, missingDependency, replacement, "THEME COMB");
        replaceMapEntry(map, missingDependency, replacement, "GAMEPLAYFEATURE BAD");
        replaceMapEntry(map, missingDependency, replacement, "GAMEPLAYFEATURE GOOD");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(ThemeManager.INSTANCE);
        arrayList.add(GameplayFeatureManager.INSTANCE);
        arrayList.add(GenreManager.INSTANCE);
        return arrayList;
    }

    public static GenreHelper currentGenreHelper;

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws ModProcessingException {
        GenreManager.INSTANCE.analyzeFile();
        GenreManager.INSTANCE.createBackup(false);
        currentGenreHelper = new GenreHelper();
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
     * Ads a new genre to mad games tycoon 2. Shows a summary for the genre that should be added.
     *
     * @param showMessages       True when the messages should be shown. False if not.
     * @throws ModProcessingException When something went wrong while adding the genre
     */
    public void addGenre(Genre genre, boolean showMessages) throws ModProcessingException {
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genre.icon.extern.getPath()));
        JLabel labelFirstPart = new JLabel("<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.mainBody.genreIsReady") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + genre.name + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + genre.description + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + genre.date + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + genre.researchPoints + "<br>" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + genre.price + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + genre.devCosts + "<br>" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "<br>" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + getTargetGroups(genre.targetGroups) + "<br>" + "");
        JButton buttonCompatibleGenres = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleGenres"), Utils.transformListToArray(SharingHelper.getExportNamesArray(GenreManager.INSTANCE, genre.compatibleGenres)), I18n.INSTANCE.get("commonText.compatibleGenres") + ":");
        JButton buttonCompatibleThemes = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleThemes"), Utils.transformListToArray(SharingHelper.getExportNamesArray(ThemeManager.INSTANCE, genre.compatibleThemes)), I18n.INSTANCE.get("commonText.compatibleThemes") + ":");
        JButton buttonBadGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.badGameplayFeatures"), Utils.transformListToArray(SharingHelper.getExportNamesArray(GameplayFeatureManager.INSTANCE, genre.badGameplayFeatures)), I18n.INSTANCE.get("commonText.badGameplayFeatures") + ":");
        JButton buttonGoodGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.goodGameplayFeatures"), Utils.transformListToArray(SharingHelper.getExportNamesArray(GameplayFeatureManager.INSTANCE, genre.goodGameplayFeatures)), I18n.INSTANCE.get("commonText.goodGameplayFeatures") + ":");
        JLabel labelSecondPart = new JLabel("<html>*" + I18n.INSTANCE.get("commonText.designFocus") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameLength") + ": " + genre.focus0 + "<br>" +
                I18n.INSTANCE.get("commonText.gameDepth") + ": " + genre.focus1 + "<br>" +
                I18n.INSTANCE.get("commonText.beginnerFriendliness") + ": " + genre.focus2 + "<br>" +
                I18n.INSTANCE.get("commonText.innovation") + ": " + genre.focus3 + "<br>" +
                I18n.INSTANCE.get("commonText.story") + ": " + genre.focus4 + "<br>" +
                I18n.INSTANCE.get("commonText.characterDesign") + ": " + genre.focus5 + "<br>" +
                I18n.INSTANCE.get("commonText.levelDesign") + ": " + genre.focus6 + "<br>" +
                I18n.INSTANCE.get("commonText.missionDesign") + ": " + genre.focus7 + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.designDirection") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.coreGamersCasualGamers") + ": " + genre.align0 + "<br>" +
                I18n.INSTANCE.get("commonText.nonviolentExtremeViolent") + ": " + genre.align1 + "<br>" +
                I18n.INSTANCE.get("commonText.easyHard") + ": " + genre.align2 + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.workPriority") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + genre.gameplay + "%<br>" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + genre.graphic + "%<br>" +
                I18n.INSTANCE.get("commonText.sound") + ": " + genre.sound + "%<br>" +
                I18n.INSTANCE.get("commonText.control") + ": " + genre.control + "%<br><br>");
        int returnValue;
        if (showMessages) {
            labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var1"));
            Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
            returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        } else {
            returnValue = 0;
        }
        if (returnValue == JOptionPane.YES_OPTION) {
            try {
                super.addContent(genre);
                ThemeManager.editGenreAllocation(genre.id, true, genre.compatibleThemes);
                GameplayFeatureManager.INSTANCE.addGenreId(genre.badGameplayFeatures, genre.id, false);
                GameplayFeatureManager.INSTANCE.addGenreId(genre.goodGameplayFeatures, genre.id, true);
                genreAdded(genre, genre.icon.extern, showMessages);
            } catch (ModProcessingException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) {
            //Click no or close window
            WindowAddGenrePage11.createFrame();
        }
    }

    /**
     * Shows a message to the user that the genre has been added successfully and asks if the NPC_GAMES file should be modified.
     *
     * @param genre     The genre that was added
     * @param genreIcon The genre icon.
     */
    public static void genreAdded(Genre genre, File genreIcon, boolean showMessages) throws ModProcessingException {
        String name = genre.name;
        int id = genre.id;
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        if (showMessages) {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.1") + " [" + name + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.2"), I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0) {
                try {
                    NpcGameManager.INSTANCE.editNPCGames(id, true, 20);
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("commonText.genre") + " " + I18n.INSTANCE.get("commonText.id") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.added"));
                } catch (ModProcessingException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.1") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.2") + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            NpcGameManager.INSTANCE.editNPCGames(id, true, 20);
        }
    }

    /**
     * @param targetGroups The array list where the target groups are stored.
     * @return Returns the compatible target groups to be displayed in the genre summary.
     */
    private static String getTargetGroups(ArrayList<TargetGroup> targetGroups) {
        StringBuilder out = new StringBuilder();
        boolean first = true;
        for (TargetGroup tg : targetGroups) {
            if (first) {
                first = false;
            } else {
                out.append(", ");
            }
            out.append(tg.getTypeName());
        }
        return out.toString();
    }
}
