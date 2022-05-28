package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.types.EngineFeatureType;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.EngineFeatureManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.HashMap;
import java.util.Map;

public class EngineFeature extends AbstractAdvancedContent {

    final String description;
    final EngineFeatureType engineFeatureType;
    final String date;
    final int researchPoints;
    final int price;
    final int devConsts;
    final int techLevel;
    final int gameplay;
    final int graphic;
    final int sound;
    final int tech;

    public EngineFeature(String name,
                         TranslationManager translations,
                         Integer id,
                         String description,
                         EngineFeatureType engineFeatureType,
                         String date,
                         int researchPoints,
                         int price,
                         int devConsts,
                         int techLevel,
                         int gameplay,
                         int graphic,
                         int sound,
                         int tech) {
        super(EngineFeatureManager.INSTANCE, name, id, translations);
        this.description = description;
        this.engineFeatureType = engineFeatureType;
        this.date = date;
        this.researchPoints = researchPoints;
        this.price = price;
        this.devConsts = devConsts;
        this.techLevel = techLevel;
        this.gameplay = gameplay;
        this.graphic = graphic;
        this.sound = sound;
        this.tech = tech;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("TYP", Integer.toString(engineFeatureType.getId()));
        map.put("NAME EN", name);
        map.put("DESC EN", description);
        map.put("DATE", date);
        map.put("RES POINTS", Integer.toString(researchPoints));
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devConsts));
        map.put("TECHLEVEL", Integer.toString(techLevel));
        map.put("PIC", "");
        map.put("GAMEPLAY", Integer.toString(gameplay));
        map.put("GRAPHIC", Integer.toString(graphic));
        map.put("SOUND", Integer.toString(sound));
        map.put("TECH", Integer.toString(tech));
        map.putAll(translationManager.toMap());
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        return I18n.INSTANCE.get("sharer.engineFeature.optionPaneMessage.main") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + description + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + engineFeatureType.getTypeName() + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + researchPoints + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + price + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devConsts + "\n" +
                I18n.INSTANCE.get("commonText.techLevel") + ": " + techLevel + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + gameplay + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + graphic + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + sound + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + tech + "\n";
    }
}
