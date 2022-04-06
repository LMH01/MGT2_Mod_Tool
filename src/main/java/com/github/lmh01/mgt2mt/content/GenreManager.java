package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.TargetGroup;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;
import com.github.lmh01.mgt2mt.windows.genre.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class GenreManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GenreManager INSTANCE = new GenreManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private GenreManager() {
        super("genre", "genre", "default_genres.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Genres.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
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
                new TranslationManagerNew(map),
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
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        String name = String.valueOf(map.get("NAME EN"));
        System.out.println("Icon key name in map: " + getExportImageName("icon.png", name));
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
        ArrayList<Integer> compatibleThemes = SharingHelper.transformContentNamesToIds(this, String.valueOf(map.get("THEME COMB")));
        ArrayList<Integer> badGameplayFeatures = SharingHelper.transformContentNamesToIds(GameplayFeatureManager.INSTANCE, String.valueOf(map.get("GAMEPLAYFEATURE BAD")));
        ArrayList<Integer> goodGameplayFeatures = SharingHelper.transformContentNamesToIds(GameplayFeatureManager.INSTANCE, String.valueOf(map.get("GAMEPLAYFEATURE GOOD")));
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.printf("%s|%s\n", entry.getKey(), entry.getValue());
        }
        return new Genre(
                String.valueOf(map.get("NAME EN")),
                getIdFromMap(map),
                new TranslationManagerNew(map),
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
    public void openAddModGui() throws ModProcessingException {
        analyzeFile();
        ModManager.themeMod.analyzeFile();
        startStepByStepGuide();
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(ThemeManager.INSTANCE);
        arrayList.add(GameplayFeatureManager.INSTANCE);
        arrayList.add(GenreManager.INSTANCE);
        return arrayList;
    }

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws ModProcessingException {
        ModManager.genreMod.analyzeFile();
        ModManager.genreMod.createBackup();
        throw new ModProcessingException("The adding of genres with the step by step guide is not yet implemented.");
        //openStepWindow(1);
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
     * @param genreId The genre id for which the file should be searched
     * @return Returns a String containing theme ids
     */
    public static String getCompatibleThemeIdsForGenre(int genreId) throws ModProcessingException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ModManager.themeMod.getGameFile()), StandardCharsets.UTF_16LE));
            boolean firstLine = true;
            StringBuilder compatibleThemes = new StringBuilder();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if (currentLine.contains(Integer.toString(genreId))) {
                    compatibleThemes.append("<");
                    compatibleThemes.append(ModManager.themeMod.getReplacedLine(currentLine));
                    compatibleThemes.append(">");
                }
            }
            br.close();
            return compatibleThemes.toString();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to retrieve theme ids for genre", e);
        }
    }
}
