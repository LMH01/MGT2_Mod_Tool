package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

import java.util.HashMap;
import java.util.Map;

public class NpcEngine extends AbstractAdvancedContent implements DependentContent {

    String date;
    int genre;
    int platform;
    int price;
    int share;

    public NpcEngine(String name,
                     Integer id,
                     TranslationManagerNew translationManager,
                     String date,
                     int genre,
                     int platform,
                     int price,
                     int share) {
        super(NpcEngineManager.INSTANCE, name, id, translationManager);
        this.date = date;
        this.genre = genre;
        this.platform = platform;
        this.price = price;
        this.share = share;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        insertIdInMap(map);
        map.put("NAME EN", name);
        map.put("DATE", date);
        map.put("GENRE", Integer.toString(genre));
        map.put("PLATFORM", Integer.toString(platform));
        map.put("PRICE", Integer.toString(price));
        map.put("SHARE", Integer.toString(share));
        map.putAll(translationManager.toMap());
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        return "<html>" +
                I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + date + "<br>" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + GenreManager.INSTANCE.getContentNameById(genre) + "<br>" +
                I18n.INSTANCE.get("commonText.platform.upperCase") + ": " + ModManager.platformMod.getContentNameById(platform) + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.profitShare") + ": " + share + "<br>";
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put(GenreManager.INSTANCE.getExportType(), GenreManager.INSTANCE.getContentNameById(genre));
        map.put(PlatformManager.INSTANCE.getExportType(), PlatformManager.INSTANCE.getContentNameById(platform));
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRE", GenreManager.INSTANCE.getContentNameById(genre));
        map.replace("PLATFORM", PlatformManager.INSTANCE.getContentNameById(platform));
    }
}
