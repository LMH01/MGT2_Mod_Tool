package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.HardwareType;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.HardwareManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hardware extends AbstractAdvancedContent implements DependentContent, RequiresPictures {

    String date;
    public HardwareType hardwareType;
    int researchPoints;
    int price;
    int devCosts;
    int techLevel;
    boolean onlyHandheld;
    boolean onlyStationary;
    ArrayList<Integer> requiredGameplayFeatures;
    Image icon;//The image is optional and can currently not be added with the guide

    public Hardware(String name,
                    Integer id,
                    TranslationManager translationManager,
                    String date,
                    HardwareType hardwareType,
                    int researchPoints,
                    int price,
                    int devCosts,
                    int techLevel,
                    boolean onlyHandheld,
                    boolean onlyStationary,
                    ArrayList<Integer> requiredGameplayFeatures,
                    Image icon) throws ModProcessingException {
        super(HardwareManager.INSTANCE, name, id, translationManager);
        // Check if this hardware is valid
        if (onlyHandheld && onlyStationary) {
            throw new ModProcessingException("Unable to construct hardware: Invalid arguments");
        }
        this.date = date;
        this.hardwareType = hardwareType;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devCosts = devCosts;
        this.techLevel = techLevel;
        this.onlyHandheld = onlyHandheld;
        this.onlyStationary = onlyStationary;
        this.requiredGameplayFeatures = requiredGameplayFeatures;
        this.icon = icon;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.put("TYP", Integer.toString(hardwareType.getId()));
        map.put("DATE", date);
        map.put("RES POINTS", Integer.toString(researchPoints));
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devCosts));
        map.put("TECHLEVEL", Integer.toString(techLevel));
        if (icon != null && icon.gameFile != null) {
            map.put("PIC", icon.gameFile.getName());
        }
        if (onlyStationary) {
            map.put("ONLY_STATIONARY", "");
        }
        if (onlyHandheld) {
            map.put("ONLY_HANDHELD", "");
        }
        map.putAll(translationManager.toMap());
        int needNumber = 1;
        for (Integer integer : requiredGameplayFeatures) {
            map.put("[NEED-" + needNumber + "]", Integer.toString(integer));
        }
        return map;
    }

    @Override
    public String getOptionPaneMessage() {
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.stationaryConsole")).append(": ");
        if (onlyStationary) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.portableConsole")).append(": ");
        if (onlyHandheld) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardware.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + researchPoints + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + hardwareType.getTypeName() + "<br>" +
                lastPart;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        Map<String, String> modifications = new HashMap<>(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                modifications.put(entry.getKey(), GameplayFeatureManager.INSTANCE.getContentNameById(Integer.parseInt(entry.getValue())));
            }
        }
        for (Map.Entry<String, String> entry : modifications.entrySet()) {
            map.replace(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addPictures() throws IOException, NullPointerException {
        if (icon != null) {
            if (icon.extern == null) {
                throw new NullPointerException("Icon extern is null");
            }
            Files.copy(icon.extern.toPath(), icon.gameFile.toPath());
        }
    }

    @Override
    public void removePictures() throws IOException {
        if (icon != null) {
            Files.delete(icon.gameFile.toPath());
        }
    }

    @Override
    public Map<String, Image> getImageMap() {
        Map<String, Image> map = new HashMap<>();
        if (icon != null) {
            String identifier = "platform_icon";
            map.put(identifier, new Image(new File(contentType.getExportImageName(identifier + ".png", name)), icon.gameFile));
        }
        return map;
    }

    @Override
    public String externalImagesAvailable() throws ModProcessingException {
        if (icon != null) {
            if (icon.extern == null) {
                throw new ModProcessingException("Icon extern is null");
            }
            if (!Files.exists(icon.extern.toPath())) {
                return icon.extern.getPath() + "\n";
            }
        }
        return "";
    }
}
