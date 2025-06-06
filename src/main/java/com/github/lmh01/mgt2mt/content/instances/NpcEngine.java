package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.NpcEngineManager;
import com.github.lmh01.mgt2mt.content.manager.PlatformManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.HashMap;
import java.util.Map;

public class NpcEngine extends AbstractAdvancedContent implements DependentContent {

    final String date;
    final int genre;
    final int platform;
    final int price;
    final int share;

    public NpcEngine(String name,
                     Integer id,
                     TranslationManager translationManager,
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
    public Map<String, String> getMap() {
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
                I18n.INSTANCE.get("commonText.platform.upperCase") + ": " + PlatformManager.INSTANCE.getContentNameById(platform) + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + price + "<br>" +
                I18n.INSTANCE.get("commonText.profitShare") + ": " + share + "<br>";
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        if (genre != -1) {
            map.replace("GENRE", GenreManager.INSTANCE.getContentNameById(genre));
        } else {
            map.replace("GENRE", "none");
        }
        if (genre != -1) {
            map.replace("PLATFORM", PlatformManager.INSTANCE.getContentNameById(platform));
        } else {
            map.replace("PLATFORM", "none");
        }
    }
}
