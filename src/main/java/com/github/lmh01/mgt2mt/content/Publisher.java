package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.CountryType;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.PublisherManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Publisher extends AbstractAdvancedContent implements DependentContent, RequiresPictures {

    final String date;
    final Image icon;
    final boolean developer;
    final boolean publisher;
    final Integer marketShare;
    final Integer share;
    final Integer genreId;
    final boolean onlyMobile;
    final Integer speed;
    final Integer comVal;
    final boolean notForSale;
    final CountryType country;

    public Publisher(String name,
                     Integer id,
                     TranslationManager translationManager,
                     String date,
                     Image icon,
                     boolean developer,
                     boolean publisher,
                     Integer marketShare,
                     Integer share,
                     Integer genreId,
                     boolean onlyMobile,
                     Integer speed,
                     Integer comVal,
                     boolean notForSale,
                     CountryType country) {
        super(PublisherManager.INSTANCE, name, id, translationManager);
        this.date = date;
        this.icon = icon;
        this.developer = developer;
        this.publisher = publisher;
        this.marketShare = marketShare;
        this.share = share;
        this.genreId = genreId;
        this.onlyMobile = onlyMobile;
        this.speed = speed;
        this.comVal = comVal;
        this.notForSale = notForSale;
        this.country = country;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        insertIdInMap(map);
        map.putAll(translationManager.toMap());
        map.put("DATE", date);
        map.put("PIC", icon.gameFile.getName().replace(".png", ""));
        map.put("DEVELOPER", String.valueOf(developer));
        map.put("PUBLISHER", String.valueOf(developer));
        map.put("MARKET", marketShare.toString());
        map.put("SHARE", share.toString());
        map.put("GENRE", genreId.toString());
        if (onlyMobile) {
            map.put("ONLYMOBILE", "true");
        }
        map.put("SPEED", speed.toString());
        map.put("COMVAL", comVal.toString());
        if (notForSale) {
            map.put("NOTFORSALE", "true");
        }
        map.put("COUNTRY", Integer.toString(country.getId()));
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        return I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "\n" +
                I18n.INSTANCE.get("commonText.date") + ": " + date + "\n" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer") + ": " + Utils.getTranslatedValueFromBoolean(developer) + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher") + ": " + Utils.getTranslatedValueFromBoolean(publisher) + "\n" +
                I18n.INSTANCE.get("commonText.marketShare") + ": " + marketShare + "\n" +
                I18n.INSTANCE.get("commonText.share") + ": " + share + "\n" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + GenreManager.INSTANCE.getContentNameById(genreId) + "\n" +
                I18n.INSTANCE.get("commonText.onlyMobile") + ": " + Utils.getTranslatedValueFromBoolean(onlyMobile) + "\n" +
                I18n.INSTANCE.get("commonText.speed") + ": " + speed + "\n" +
                I18n.INSTANCE.get("commonText.comVal") + ": " + comVal + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.checkBox.notForSale") + ": " + Utils.getTranslatedValueFromBoolean(notForSale) + "\n" +
                I18n.INSTANCE.get("commonText.country") + ": " + country.getTypeName();
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add(GenreManager.INSTANCE.getContentNameById(genreId));
        map.put(GenreManager.INSTANCE.getExportType(), genres);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRE", GenreManager.INSTANCE.getContentNameById(genreId));
    }

    @Override
    public void addPictures() throws IOException, NullPointerException {
        if (icon.extern == null) {
            throw new NullPointerException("Icon extern is null");
        }
        if (!icon.gameFile.getName().equals("87.png")) {
            // Only copy image if it is a custom file
            Files.copy(icon.extern.toPath(), icon.gameFile.toPath());
        }
    }

    @Override
    public void removePictures() throws IOException {
        //delete image only if it is not a standard game image
        if (Integer.parseInt(icon.gameFile.getName().replaceAll("[^0-9]", "")) > 187) {
            Files.delete(icon.gameFile.toPath());
        }
    }

    @Override
    public Map<String, Image> getImageMap() {
        Map<String, Image> map = new HashMap<>();
        String identifier = "publisher_icon";
        map.put(identifier, new Image(new File(contentType.getExportImageName(identifier + ".png", name)), icon.gameFile));
        return map;
    }
}
