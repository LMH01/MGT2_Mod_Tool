package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Genre extends AbstractAdvancedContent implements RequiresPictures, DependentContent {

    public final String description;
    public final String date;
    public final int researchPoints;
    public final int price;
    public final int devCosts;
    public final Image icon;
    public final ArrayList<Image> screenshots;
    public final ArrayList<TargetGroup> targetGroups;
    public final int gameplay;
    public final int graphic;
    public final int sound;
    public final int control;
    public final ArrayList<Integer> compatibleGenres;
    public final ArrayList<Integer> compatibleThemes;
    public final ArrayList<Integer> badGameplayFeatures;
    public final ArrayList<Integer> goodGameplayFeatures;
    public final int focus0;
    public final int focus1;
    public final int focus2;
    public final int focus3;
    public final int focus4;
    public final int focus5;
    public final int focus6;
    public final int focus7;
    public final int align0;
    public final int align1;
    public final int align2;

    public Genre(String name,
                 Integer id,
                 TranslationManager translationManager,
                 String description,
                 String date,
                 int researchPoints,
                 int price,
                 int devCosts,
                 Image icon,
                 ArrayList<Image> screenshots,
                 ArrayList<TargetGroup> targetGroups,
                 int gameplay,
                 int graphic,
                 int sound,
                 int control,
                 ArrayList<Integer> compatibleGenres,
                 ArrayList<Integer> compatibleThemes,
                 ArrayList<Integer> badGameplayFeatures,
                 ArrayList<Integer> goodGameplayFeatures,
                 int focus0,
                 int focus1,
                 int focus2,
                 int focus3,
                 int focus4,
                 int focus5,
                 int focus6,
                 int focus7,
                 int align0,
                 int align1,
                 int align2) {
        super(GenreManager.INSTANCE, name, id, translationManager);
        this.description = description;
        this.date = date;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devCosts = devCosts;
        this.icon = icon;
        this.screenshots = screenshots;
        this.targetGroups = targetGroups;
        this.gameplay = gameplay;
        this.graphic = graphic;
        this.sound = sound;
        this.control = control;
        this.compatibleGenres = compatibleGenres;
        this.compatibleThemes = compatibleThemes;
        this.badGameplayFeatures = badGameplayFeatures;
        this.goodGameplayFeatures = goodGameplayFeatures;
        this.focus0 = focus0;
        this.focus1 = focus1;
        this.focus2 = focus2;
        this.focus3 = focus3;
        this.focus4 = focus4;
        this.focus5 = focus5;
        this.focus6 = focus6;
        this.focus7 = focus7;
        this.align0 = align0;
        this.align1 = align1;
        this.align2 = align2;
    }

    /**
     * @return The target groups of this genre ready to be placed in the game file
     */
    private String getTargetGroupsString() {
        StringBuilder builder = new StringBuilder();
        for (TargetGroup tg : targetGroups) {
            builder.append("<").append(tg.getDataType()).append(">");
        }
        return builder.toString();
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.put("DESC EN", description);
        map.put("DATE", date);
        map.put("RES POINTS", Integer.toString(researchPoints));
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devCosts));
        map.put("PIC", icon.gameFile.getName());
        map.put("TGROUP", getTargetGroupsString());
        map.put("GAMEPLAY", Integer.toString(gameplay));
        map.put("GRAPHIC", Integer.toString(graphic));
        map.put("SOUND", Integer.toString(sound));
        map.put("CONTROL", Integer.toString(control));
        map.put("GENRE COMB", Utils.transformArrayListToString(compatibleGenres));
        map.put("FOCUS0", Integer.toString(focus0));
        map.put("FOCUS1", Integer.toString(focus1));
        map.put("FOCUS2", Integer.toString(focus2));
        map.put("FOCUS3", Integer.toString(focus3));
        map.put("FOCUS4", Integer.toString(focus4));
        map.put("FOCUS5", Integer.toString(focus5));
        map.put("FOCUS6", Integer.toString(focus6));
        map.put("FOCUS7", Integer.toString(focus7));
        map.put("ALIGN0", Integer.toString(align0));
        map.put("ALIGN1", Integer.toString(align1));
        map.put("ALIGN2", Integer.toString(align2));
        map.putAll(translationManager.toMap());
        return map;
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR GENRE CONTENT
     */
    @Deprecated
    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        throw new ModProcessingException("Call to getOptionPaneMessage(T t) is invalid. This function is not implemented for genre mod");
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put(contentType.getExportType(), new HashSet<>(SharingHelper.getExportNamesArray(contentType, compatibleGenres)));
        map.put(ThemeManager.INSTANCE.getExportType(), new HashSet<>(SharingHelper.getExportNamesArray(ThemeManager.INSTANCE, compatibleThemes)));
        Set<String> gameplayFeatures = new HashSet<>();
        gameplayFeatures.addAll(SharingHelper.getExportNamesArray(GameplayFeatureManager.INSTANCE, goodGameplayFeatures));
        gameplayFeatures.addAll(SharingHelper.getExportNamesArray(GameplayFeatureManager.INSTANCE, badGameplayFeatures));
        map.put(GameplayFeatureManager.INSTANCE.getExportType(), gameplayFeatures);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRE COMB", SharingHelper.getExportNamesString(GenreManager.INSTANCE, compatibleGenres));
        map.put("THEME COMB", ThemeManager.getCompatibleThemeIdsForGenre(id));
        map.put("GAMEPLAYFEATURE GOOD", Utils.getCompatibleGameplayFeatureIdsForGenre(id, true));
        map.put("GAMEPLAYFEATURE BAD", Utils.getCompatibleGameplayFeatureIdsForGenre(id, false));
    }

    @Override
    public void addPictures() throws IOException, NullPointerException {
        //Add icon
        Files.copy(Objects.requireNonNull(icon.extern).toPath(), icon.gameFile.toPath());
        //Add screenshots
        Path screenshotFolder = MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(id));
        if (!Files.exists(screenshotFolder)) {
            Files.createDirectories(screenshotFolder);
        }
        for (Image image : screenshots) {
            Files.copy(Objects.requireNonNull(image.extern).toPath(), screenshotFolder.resolve(image.gameFile.getName()));
        }
    }

    @Override
    public void removePictures() throws IOException {
        //Remove icon
        Files.delete(icon.gameFile.toPath());
        //Remove screenshots
        if (!screenshots.isEmpty()) {
            // Delete screenshots directory
            DataStreamHelper.deleteDirectory(screenshots.get(0).gameFile.getParentFile().toPath());
        }
    }

    @Override
    public Map<String, Image> getImageMap() {
        Map<String, Image> map = new HashMap<>();
        map.put("iconName", new Image(new File(contentType.getExportImageName("icon.png", name)), icon.gameFile));
        for (Image screenshot : screenshots) {
            String identifier = "screenshot_" + screenshot.gameFile.getName();
            map.put(identifier, new Image(new File(contentType.getExportImageName(identifier, name)), screenshot.gameFile));
        }
        return map;
    }

    @Override
    public String externalImagesAvailable() throws ModProcessingException {
        StringBuilder sb = new StringBuilder();
        if (icon.extern == null) {
            throw new ModProcessingException("icon extern is null");
        }
        if (!Files.exists(icon.extern.toPath())) {
            sb.append(icon.extern.getPath()).append("\n");
        }
        for (Image screenshot : screenshots) {
            if (screenshot.extern == null) {
                throw new ModProcessingException("screenshot extern for game file " + screenshot.gameFile.getName() + " is null");
            }
            if (!Files.exists(screenshot.extern.toPath())) {
                sb.append(screenshot.extern.getPath()).append("\n");
            }
        }
        return sb.toString();
    }
}
