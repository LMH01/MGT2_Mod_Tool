package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.PlatformType;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.PlatformManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Platform extends AbstractAdvancedContent implements DependentContent, RequiresPictures {

    final String manufacturer;
    final Map<String, String> manufacturerTranslations;
    final String releaseDate;
    final String endDate;
    final Integer devKitCost;
    final Integer devCosts;
    final Integer techLevel;
    final Integer units;
    final ArrayList<PlatformImage> platformImages;// The images that belong to the platform
    final ArrayList<Integer> requiredGameplayFeatures;
    final Integer complexity;
    final boolean hasInternet;
    final PlatformType type;
    final boolean startPlatform;

    public Platform(String name,
                    Integer id,
                    TranslationManager translationManager,
                    String manufacturer,
                    Map<String, String> manufacturerTranslations,
                    String releaseDate,
                    String endDate,
                    Integer devKitCost,
                    Integer devCosts,
                    Integer techLevel,
                    Integer units,
                    ArrayList<PlatformImage> platformImages,
                    ArrayList<Integer> requiredGameplayFeatures,
                    Integer complexity,
                    boolean hasInternet,
                    PlatformType type,
                    boolean startPlatform) throws ModProcessingException {
        super(PlatformManager.INSTANCE, name, id, translationManager);
        this.manufacturer = manufacturer;
        this.manufacturerTranslations = manufacturerTranslations;
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.devKitCost = devKitCost;
        this.devCosts = devCosts;
        this.techLevel = techLevel;
        this.units = units;
        this.platformImages = platformImages;
        this.requiredGameplayFeatures = requiredGameplayFeatures;
        this.complexity = complexity;
        this.hasInternet = hasInternet;
        this.type = type;
        this.startPlatform = startPlatform;
        // Check if platform images are valid
        ArrayList<Integer> assignedIds = new ArrayList<>();
        for (PlatformImage image : platformImages) {
            if (assignedIds.contains(image.id)) {
                throw new ModProcessingException("Unable to create platform: The platform images are invalid: At least one image id is duplicate.");
            }
            assignedIds.add(image.id);
        }
        // Check if main image (image with id=1) has been set and throw exception if it does not exist
        if (!assignedIds.contains(1)) {
            throw new ModProcessingException("Unable to create platform: The platform images are invalid: The image with id 1 is missing.");
        }
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.putAll(translationManager.toMap());
        map.put("MANUFACTURER EN", manufacturer);
        for (Map.Entry<String, String> entry : manufacturerTranslations.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                map.put("MANUFACTURER " + entry.getKey(), entry.getValue());
            }
        }
        map.putAll(manufacturerTranslations);
        map.put("DATE", releaseDate);
        if (endDate != null) {
            map.put("DATE END", endDate);
        }
        map.put("PRICE", devKitCost.toString());
        map.put("DEV COSTS", devCosts.toString());
        map.put("TECHLEVEL", techLevel.toString());
        map.put("UNITS", units.toString());
        for (PlatformImage image : platformImages) {
            if (image.id == 1) {
                map.put("PIC-1", image.image.gameFile.getName());
            } else {
                map.put("PIC-" + image.id, image.image.gameFile.getName());
                map.put("PIC-" + image.id + " YEAR", image.year.toString());
            }
        }
        int needNumber = 1;
        for (Integer integer : requiredGameplayFeatures) {
            map.put("NEED-" + needNumber, integer.toString());
            needNumber+=1;
        }
        map.put("COMPLEX", complexity.toString());
        if (hasInternet) {
            map.put("INTERNET", Integer.toString(1));
        } else {
            map.put("INTERNET", Integer.toString(0));
        }
        map.put("TYP", Integer.toString(type.getId()));
        if (startPlatform) {
            map.put("STARTPLATFORM", "");
        }
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        String productionEnd = "";
        if (endDate != null) {
            productionEnd = I18n.INSTANCE.get("commonText.releaseDate") + ": " + endDate + "<br>";
        }
        StringBuilder requiredGameplayFeaturesString = new StringBuilder(I18n.INSTANCE.get("commonText.neededGameplayFeatures") + ": ");
        boolean firstGameplayFeature = true;
        for (Integer integer : requiredGameplayFeatures) {
            if (firstGameplayFeature) {
                firstGameplayFeature = false;
            } else {
                requiredGameplayFeaturesString.append(", ");
            }
            requiredGameplayFeaturesString.append(GameplayFeatureManager.INSTANCE.getContentNameById(integer));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.platform.addPlatform.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.manufacturer") + ": " + manufacturer + "<br>" +
                I18n.INSTANCE.get("commonText.releaseDate") + ": " + releaseDate + "<br>" +
                productionEnd +
                I18n.INSTANCE.get("commonText.devKitCost") + ": " + devKitCost + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "<br>" +
                I18n.INSTANCE.get("commonText.techLevel") + ": " + techLevel + "<br>" +
                I18n.INSTANCE.get("commonText.units") + ": " + units + "<br>" +
                requiredGameplayFeaturesString.toString() + "<br>" +
                I18n.INSTANCE.get("commonText.complexity") + ": " + complexity + "<br>" +
                I18n.INSTANCE.get("commonText.internet") + ": " + Utils.getTranslatedValueFromBoolean(hasInternet) + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + type.getTypeName() + "<br>" +
                I18n.INSTANCE.get("commonText.startplatform") + ": " + Utils.getTranslatedValueFromBoolean(startPlatform);
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> gameplayFeatures = new HashSet<>();
        for (Integer integer : requiredGameplayFeatures) {
            gameplayFeatures.add(GameplayFeatureManager.INSTANCE.getContentNameById(integer));
        }
        map.put(GameplayFeatureManager.INSTANCE.getExportType(), gameplayFeatures);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        Map<String, String> changedValues = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                changedValues.put(entry.getKey(), GameplayFeatureManager.INSTANCE.getContentNameById(Integer.parseInt(entry.getValue())));
            }
        }
        Utils.replaceMapEntries(map, changedValues);
    }

    @Override
    public void addPictures() throws IOException, NullPointerException {
        for (PlatformImage image : platformImages) {
            Files.copy(image.image.extern.toPath(), image.image.gameFile.toPath());
        }
    }

    @Override
    public void removePictures() throws IOException {
        for (PlatformImage image : platformImages) {
            Files.delete(image.image.gameFile.toPath());
        }
    }

    @Override
    public Map<String, Image> getImageMap() {
        Map<String, Image> map = new HashMap<>();
        for (PlatformImage image : platformImages) {
            String identifier = "platform_image_" + image.id;
            map.put(identifier, new Image(new File(contentType.getExportImageName(identifier + ".png", name)), image.image.gameFile));
        }
        return map;
    }
}
