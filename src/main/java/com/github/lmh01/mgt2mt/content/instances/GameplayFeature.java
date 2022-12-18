package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.SharingHelper;
import com.github.lmh01.mgt2mt.content.managed.types.GameplayFeatureType;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.*;

public class GameplayFeature extends AbstractAdvancedContent implements DependentContent {

    final String description;
    final GameplayFeatureType gameplayFeatureType;
    final String date;
    final int researchPoints;
    final int price;
    final int devCosts;
    final int gameplay;
    final int graphic;
    final int sound;
    final int tech;
    final ArrayList<Integer> badGenres;// Stores the ids of genres that fit good with this gameplay feature
    final ArrayList<Integer> goodGenres;// Stores the ids of genres that fit bad with this gameplay feature
    final ArrayList<Integer> requiredGameplayFeatures;/// Stores the ids of required gameplay features
    final boolean arcade;
    final boolean mobile;
    final boolean requiresInternet;

    public GameplayFeature(String name,
                           Integer id,
                           TranslationManager translationManager,
                           String description,
                           GameplayFeatureType gameplayFeatureType,
                           String date,
                           int researchPoints,
                           int price,
                           int devCosts,
                           int gameplay,
                           int graphic,
                           int sound,
                           int tech,
                           ArrayList<Integer> badGenres,
                           ArrayList<Integer> goodGenres,
                           ArrayList<Integer> requiredGameplayFeatures,
                           boolean arcade,
                           boolean mobile,
                           boolean requiresInternet) {
        super(GameplayFeatureManager.INSTANCE, name, id, translationManager);
        this.description = description;
        this.gameplayFeatureType = gameplayFeatureType;
        this.date = date;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devCosts = devCosts;
        this.gameplay = gameplay;
        this.graphic = graphic;
        this.sound = sound;
        this.tech = tech;
        this.badGenres = badGenres;
        this.goodGenres = goodGenres;
        this.requiredGameplayFeatures = requiredGameplayFeatures;
        this.arcade = arcade;
        this.mobile = mobile;
        this.requiresInternet = requiresInternet;
        // Check if requiredGameplayFeatures only contains one id, because more are currently not supported by the game
        if (requiredGameplayFeatures.size() > 1) {
            throw new IllegalArgumentException("Unable to create new GameplayFeature: requiredGameplayFeatures contains more than one element. This is currently not supported by the game.");
        }
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("TYP", Integer.toString(gameplayFeatureType.getId()));
        map.put("NAME EN", name);
        map.put("DESC EN", description);
        map.put("DATE", date);
        map.put("RES POINTS", Integer.toString(researchPoints));
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devCosts));
        map.put("PIC", "");
        map.put("GAMEPLAY", Integer.toString(gameplay));
        map.put("GRAPHIC", Integer.toString(graphic));
        map.put("SOUND", Integer.toString(sound));
        map.put("TECH", Integer.toString(tech));
        map.putAll(translationManager.toMap());
        if (!arcade) {
            map.put("NO_ARCADE", "");
        }
        if (!mobile) {
            map.put("NO_MOBILE", "");
        }
        if (requiresInternet) {
            map.put("INTERNET", "");
        }
        if (!badGenres.isEmpty()) {
            map.put("BAD", Utils.transformArrayListToString(badGenres));
        }
        if (!goodGenres.isEmpty()) {
            map.put("GOOD", Utils.transformArrayListToString(goodGenres));
        }
        if (requiredGameplayFeatures.size() == 1) {
            map.put("NEED_GPF", requiredGameplayFeatures.get(0).toString());
        }
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        StringBuilder badGenresOut = new StringBuilder();
        boolean firstBadFeature = true;
        for (int i : badGenres) {
            badGenresOut.append(GenreManager.INSTANCE.getContentNameById(i));
            if (!firstBadFeature) {
                badGenresOut.append(", ");
            } else {
                firstBadFeature = false;
            }
        }
        StringBuilder goodGenresOut = new StringBuilder();
        boolean firstGoodGenre = true;
        for (int i : goodGenres) {
            goodGenresOut.append(GenreManager.INSTANCE.getContentNameById(i));
            if (!firstGoodGenre) {
                goodGenresOut.append(", ");
            } else {
                firstGoodGenre = false;
            }
        }
        StringBuilder requiredGameplayFeatures = new StringBuilder();
        boolean firstGameplayFeature = true;
        for (Integer i : this.requiredGameplayFeatures) {
            if (!firstGameplayFeature) {
                requiredGameplayFeatures.append(", ");
            } else {
                firstGameplayFeature = true;
            }
            requiredGameplayFeatures.append(GameplayFeatureManager.INSTANCE.getContentNameById(i));
        }
        return I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + description + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + gameplayFeatureType.getTypeName() + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + researchPoints + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + price + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.badGenres") + "*\n\n" + badGenresOut.toString() + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.goodGenres") + "*\n\n" + goodGenresOut.toString() + "\n" +
                "\n*" + I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures") + "*\n\n" + requiredGameplayFeatures.toString() + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + gameplay + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + graphic + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + sound + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + tech + "\n" +
                I18n.INSTANCE.get("commonText.arcadeCompatibility") + ": " + Utils.getTranslatedValueFromBoolean(arcade) + "\n" +
                I18n.INSTANCE.get("commonText.mobileCompatibility") + ": " + Utils.getTranslatedValueFromBoolean(mobile) + "\n" +
                I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiresInternet") + ": " + Utils.getTranslatedValueFromBoolean(requiresInternet) + "\n";
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.addAll(SharingHelper.getExportNamesArray(GenreManager.INSTANCE, goodGenres));
        set.addAll(SharingHelper.getExportNamesArray(GenreManager.INSTANCE, badGenres));
        map.put(GenreManager.INSTANCE.getExportType(), set);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        if (map.containsKey("GOOD")) {
            map.replace("GOOD", SharingHelper.getExportNamesString(GenreManager.INSTANCE, Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("GOOD")))));
        }
        if (map.containsKey("BAD")) {
            map.replace("BAD", SharingHelper.getExportNamesString(GenreManager.INSTANCE, Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("BAD")))));
        }
    }
}
