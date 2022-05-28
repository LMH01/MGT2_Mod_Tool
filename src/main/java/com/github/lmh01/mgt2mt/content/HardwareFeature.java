package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.HardwareFeatureManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.HashMap;
import java.util.Map;

public class HardwareFeature extends AbstractAdvancedContent {

    final String description;
    final String date;
    final int researchPoints;
    final int price;
    final int devCosts;
    final int quality;
    final boolean onlyStationary;
    final boolean needsInternet;

    public HardwareFeature(String name,
                           Integer id,
                           TranslationManager translationManager,
                           String description,
                           String date,
                           int researchPoints,
                           int price,
                           int devCosts,
                           int quality,
                           boolean onlyStationary,
                           boolean needsInternet) {
        super(HardwareFeatureManager.INSTANCE, name, id, translationManager);
        this.description = description;
        this.date = date;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devCosts = devCosts;
        this.quality = quality;
        this.onlyStationary = onlyStationary;
        this.needsInternet = needsInternet;
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
        map.put("QUALITY", Integer.toString(quality));
        map.putAll(translationManager.toMap());
        if (onlyStationary) {
            map.put("ONLY_STATIONARY", "");
        }
        if (needsInternet) {
            map.put("NEEDINTERNET", "");
        }
        return map;
    }

    @Override
    public String getOptionPaneMessage() {
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.needInternet")).append(": ");
        if (needsInternet) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.stationary")).append(": ");
        if (onlyStationary) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardwareFeature.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + description + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + researchPoints + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "<br>" +
                I18n.INSTANCE.get("commonText.quality") + ": " + quality + "<br>" +
                lastPart;
    }
}
