package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.NpcIpManager;
import com.github.lmh01.mgt2mt.content.manager.PublisherManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NpcIp extends AbstractSimpleContent implements DependentContent {

    // genre, subGenre, theme, subTheme and publisher store the id of the corresponding mod
    public Integer genre;
    public Integer subGenre;
    public Integer theme;// The id offset is misaligned by one, that's why theme_id-1 has to be printed in the game npcip file - Will probably be fixed after the backend has been rewritten again
    public Integer subTheme;
    public TargetGroup targetGroup;
    public Integer publisher;
    public int releaseYear;
    public int rating;

    /**
     * Construct a new npcIp.
     * @param name The name of the npcIp
     * @param id The id of this mod, can be null. If null the id will be set when the content is added to the game
     * @param genre The id of the main genre
     * @param subGenre The id of the sub-genre
     * @param theme The id of the main theme
     * @param subTheme The id of the sub-theme
     * @param targetGroup The target group of the npcIp
     * @param publisher The publisher
     * @param releaseYear The release year
     * @param rating The rating in percent 0-100%
     */
    public NpcIp(String name, Integer id, int genre, Integer subGenre, int theme, Integer subTheme, TargetGroup targetGroup, int publisher, int releaseYear, int rating) throws IllegalArgumentException {
        super(NpcIpManager.INSTANCE, name, id);
        if (rating < 0 || rating > 100) {
            throw new IllegalArgumentException("Unable to construct new NpcIp: The rating entered is invalid!");
        }
        this.genre = genre;
        this.subGenre = subGenre;
        this.theme = theme;
        this.subTheme = subTheme;
        this.targetGroup = targetGroup;
        this.publisher = publisher;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> exportMap = new HashMap<>();
        exportMap.put("name", name);
        exportMap.put("genre", GenreManager.INSTANCE.getContentNameById(genre));
        if (subGenre != null) {
            exportMap.put("sub_genre", GenreManager.INSTANCE.getContentNameById(subGenre));
        }
        exportMap.put("theme", ThemeManager.INSTANCE.getContentNameById(theme));
        if (subTheme != null) {
            exportMap.put("sub_theme", ThemeManager.INSTANCE.getContentNameById(theme));
        }
        exportMap.put("target_group", Integer.toString(targetGroup.getId()));
        exportMap.put("publisher", PublisherManager.INSTANCE.getContentNameById(publisher));
        exportMap.put("release_year", Integer.toString(releaseYear));
        exportMap.put("rating", Integer.toString(rating));
        return exportMap;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        String subGenrePrint = "none";
        if (subGenre != null) {
            subGenrePrint = GenreManager.INSTANCE.getContentNameById(subGenre);
        }
        String subThemePrint = "none";
        if (subTheme != null) {
            subThemePrint = ThemeManager.INSTANCE.getContentNameById(subTheme);
        }
        System.out.printf("Theme id: %s\n", theme);
        return "<html>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + GenreManager.INSTANCE.getContentNameById(genre) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre") + ": " + subGenrePrint + "<br>" +
                I18n.INSTANCE.get("commonText.theme.upperCase") + ": " + ThemeManager.INSTANCE.getContentNameById(theme) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme") + ": " + subThemePrint + "<br>" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + targetGroup.getTypeName() + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher") + ": " + PublisherManager.INSTANCE.getContentNameById(publisher) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.label.releaseYear") + ": " + releaseYear + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.spinner.rating") + ": " + rating;
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add(GenreManager.INSTANCE.getContentNameById(genre));
        if (subGenre != null) {
            genres.add(GenreManager.INSTANCE.getContentNameById(subGenre));
        }
        map.put(GenreManager.INSTANCE.getExportType(), genres);
        Set<String> themes = new HashSet<>();
        themes.add(ThemeManager.INSTANCE.getContentNameById(theme));
        if (subTheme != null) {
            themes.add(ThemeManager.INSTANCE.getContentNameById(subTheme));
        }
        map.put(ThemeManager.INSTANCE.getExportType(), themes);
        Set<String> publisher = new HashSet<>();
        publisher.add(PublisherManager.INSTANCE.getContentNameById(this.publisher));
        map.put(PublisherManager.INSTANCE.getExportType(), publisher);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        // Nothing has to be done here, everything has been completed in getMap
    }

    @Override
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <G").append(genre).append("><T").append(theme)
                .append("><TG").append(targetGroup.getId()).append("><P").append(publisher)
                .append("><%").append(rating).append("><Y").append(releaseYear).append(">");
        if (subGenre != null) {
            sb.append("<SG").append(subGenre).append(">");
        }
        if (subTheme != null) {
            sb.append("<ST").append(subTheme).append(">");
        }
        return sb.toString();
    }
}
