package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.SharingHelper;
import com.github.lmh01.mgt2mt.content.managed.types.HardwareType;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.HardwareManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hardware extends AbstractAdvancedContent implements DependentContent {

    String description;
    String date;
    public HardwareType hardwareType;
    int researchPoints;
    int price;
    int devCosts;
    int techLevel;
    boolean onlyHandheld;
    boolean onlyStationary;
    ArrayList<Integer> requiredGameplayFeatures;

    public Hardware(String name,
                    Integer id,
                    TranslationManager translationManager,
                    String description,
                    String date,
                    HardwareType hardwareType,
                    int researchPoints,
                    int price,
                    int devCosts,
                    int techLevel,
                    boolean onlyHandheld,
                    boolean onlyStationary,
                    ArrayList<Integer> requiredGameplayFeatures) throws ModProcessingException {
        super(HardwareManager.INSTANCE, name, id, translationManager);
        // Check if this hardware is valid
        if (onlyHandheld && onlyStationary) {
            throw new ModProcessingException("Unable to construct hardware: Invalid arguments");
        }
        this.description = description;
        this.date = date;
        this.hardwareType = hardwareType;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devCosts = devCosts;
        this.techLevel = techLevel;
        this.onlyHandheld = onlyHandheld;
        this.onlyStationary = onlyStationary;
        this.requiredGameplayFeatures = requiredGameplayFeatures;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.put("DESC EN", description);
        map.put("TYP", Integer.toString(hardwareType.getId()));
        map.put("DATE", date);
        map.put("RES POINTS", Integer.toString(researchPoints));
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devCosts));
        map.put("TECHLEVEL", Integer.toString(techLevel));
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
                I18n.INSTANCE.get("commonText.description") + ": " + description + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + researchPoints + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + hardwareType.getTypeName() + "<br>" +
                lastPart;
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put(GameplayFeatureManager.INSTANCE.getExportType(), SharingHelper.getExportNamesArray(GameplayFeatureManager.INSTANCE, requiredGameplayFeatures));
        return map;
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
}
