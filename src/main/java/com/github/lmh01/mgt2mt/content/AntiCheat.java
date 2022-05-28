package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.AntiCheatManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.HashMap;
import java.util.Map;

public class AntiCheat extends AbstractAdvancedContent {

    final String date;
    final int price;
    final int devCosts;

    public AntiCheat(String name, Integer id, TranslationManager nameTranslations, String date, int price, int devCosts) {
        super(AntiCheatManager.INSTANCE, name, id, nameTranslations);
        this.date = date;
        this.price = price;
        this.devCosts = devCosts;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.put("DATE", date);
        map.put("PRICE", Integer.toString(price));
        map.put("DEV COSTS", Integer.toString(devCosts));
        map.putAll(translationManager.toMap());
        return map;
    }

    @Override
    public String getOptionPaneMessage() {
        return "<html>" +
                I18n.INSTANCE.get("mod.antiCheat.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + devCosts + "<br>";
    }
}
