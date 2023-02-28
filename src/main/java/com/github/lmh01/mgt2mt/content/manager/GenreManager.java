package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.Genre;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.windows.genre.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GenreManager INSTANCE = new GenreManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", MadGamesTycoon2ModTool.VERSION};

    public static final Path defaultGenreIcon = MGT2Paths.GENRE_ICONS.getPath().resolve("iconSkill.png");

    private GenreManager() {
        super("genre", "genre", MGT2Paths.TEXT_DATA.getPath().resolve("Genres.txt").toFile(), StandardCharsets.UTF_8);
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
        PublisherManager.removeGenre(name);
        NpcEngineManager.INSTANCE.removeGenre(name);
        NpcGameManager.INSTANCE.editNPCGames(getContentIdByName(name), false, 0);
        super.removeContent(name);
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
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("RES POINTS", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("DEV COSTS", true, DataType.INT));
        list.add(new DataLine("PIC", true, DataType.UNCHECKED));
        list.add(new DataLine("TGROUP", true, DataType.UNCHECKED));
        list.add(new DataLine("GAMEPLAY", true, DataType.INT));
        list.add(new DataLine("GRAPHIC", true, DataType.INT));
        list.add(new DataLine("SOUND", true, DataType.INT));
        list.add(new DataLine("CONTROL", true, DataType.INT));
        list.add(new DataLine("GENRE COMB", true, DataType.INT_LIST));
        list.add(new DataLine("FOCUS0", true, DataType.INT));
        list.add(new DataLine("FOCUS1", true, DataType.INT));
        list.add(new DataLine("FOCUS2", true, DataType.INT));
        list.add(new DataLine("FOCUS3", true, DataType.INT));
        list.add(new DataLine("FOCUS4", true, DataType.INT));
        list.add(new DataLine("FOCUS5", true, DataType.INT));
        list.add(new DataLine("FOCUS6", true, DataType.INT));
        list.add(new DataLine("FOCUS7", true, DataType.INT));
        list.add(new DataLine("ALIGN0", true, DataType.INT));
        list.add(new DataLine("ALIGN1", true, DataType.INT));
        list.add(new DataLine("ALIGN2", true, DataType.INT));
        return list;
    }

    @Override
    protected String analyzeSpecialCases(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        // Check if platform picture is valid
        if (map.containsKey("PIC")) {
            File file = MGT2Paths.GENRE_ICONS.getPath().resolve(map.get("PIC")).toFile();
            if (!file.exists()) {
                sb.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.pictureNotFound"), gameFile.getName(), getType(), map.get("NAME EN"), file.getName(), MGT2Paths.GENRE_ICONS.getPath())).append("\n");
            }
        }
        // Check if TGROUP is valid
        if (map.containsKey("TGROUP")) {
            String tgroup = map.get("TGROUP");
            if (tgroup.isEmpty()) {
                sb.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.genreInvalid.targetGroupInvalid.variation1"), gameFile.getName(), getType(), map.get("NAME EN"))).append("\n");
            } else {
                ArrayList<String> tgroups = Utils.getEntriesFromString(tgroup);
                for (String tg : tgroups) {
                    if (!tg.equals("KID") && !tg.equals("TEEN") && !tg.equals("ADULT") && !tg.equals("OLD") && !tg.equals("ALL")) {
                        sb.append(String.format(I18n.INSTANCE.get("verifyContentIntegrity.genreInvalid.targetGroupInvalid.variation2"), gameFile.getName(), getType(), map.get("NAME EN"), tgroup)).append("\n");
                    }
                }
            }
        }
        return sb.toString();
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
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
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

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        map.put(GenreManager.INSTANCE.getId(), new HashSet<>(Utils.getEntriesFromString((String)importMap.get("GENRE COMB"))));
        map.put(ThemeManager.INSTANCE.getId(), new HashSet<>(Utils.getEntriesFromString((String)importMap.get("THEME COMB"))));
        Set<String> gameplayFeatures = new HashSet<>();
        gameplayFeatures.addAll(Utils.getEntriesFromString((String)importMap.get("GAMEPLAYFEATURE GOOD")));
        gameplayFeatures.addAll(Utils.getEntriesFromString((String)importMap.get("GAMEPLAYFEATURE BAD")));
        map.put(GameplayFeatureManager.INSTANCE.getId(), gameplayFeatures);
        return map;
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
     * @param showMessages True when the messages should be shown. False if not.
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

    /**
     * @param mainGenre The main genre
     * @param subGenre The sub genre
     * @return Returns the optimal slider settings between mainGenre and subGenre (focus and align values).
     */
    public static List<Integer> getComboSliderSettings(Genre mainGenre, Genre subGenre) {
        // The correct algorithm was figgured out by the user Ali
        int[] focus = new int[8];
        ArrayList<Integer> mainGenreSettings = mainGenre.getSliderSettings();
        ArrayList<Integer> subGenreSettings = subGenre.getSliderSettings();
        // Fill base settings (focus values)
        for (int i = 0; i < focus.length; i++) {
            focus[i] += mainGenreSettings.get(i);
            focus[i] += subGenreSettings.get(i) / 2;
            float num = (float)focus[i];
            num /= 1.5f;
            focus[i] = Math.round(num);
        }
        
        int combinedValue = 0;
        for (int i = 0; i < focus.length; i++) {
            combinedValue += focus[i];
        }

        // Check if total is 40
        combinedValue = 40 - combinedValue;
        if (combinedValue > 0) {
            // Fix total value to little
            while (combinedValue > 0) {
                for (int i = 0; i < focus.length; i++) {
                    if (combinedValue > 0 && focus[i] < 10) {
                        focus[i] += 1;
                        combinedValue -= 1;
                    }
                }
            }
        }

        if (combinedValue < 0) {
            // Fix total value to much
            while (combinedValue < 0) {
                for (int i = 0; i < focus.length; i++) {
                    if (combinedValue < 0 && focus[i] > 0) {
                        focus[i] -= 1;
                        combinedValue += 1;
                    }
                }
            }
        }

        // Fill align values
        int[] align = new int[3];
        int offset = 8;
        for (int i = 0; i < align.length; i++) {
            align[i] += mainGenreSettings.get(i + offset);
            align[i] += subGenreSettings.get(i + offset) / 2;
            float num = (float)align[i];
            num /= 1.5f;
            align[i] = Math.round(num);
        }

        int[] settings = new int[focus.length + align.length];
        System.arraycopy(focus, 0, settings, 0, focus.length);
        System.arraycopy(align, 0, settings, focus.length, align.length);

        return Arrays.stream(settings).boxed().collect(Collectors.toList());
    }
}
