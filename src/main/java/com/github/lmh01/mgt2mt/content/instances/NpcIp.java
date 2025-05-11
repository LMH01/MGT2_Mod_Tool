package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.TargetGroup;
import com.github.lmh01.mgt2mt.content.managed.types.SequelNumeration;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.NpcIpManager;
import com.github.lmh01.mgt2mt.content.manager.PublisherManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.Map;

public class NpcIp extends AbstractSimpleContent implements DependentContent {

    // genre, subGenre, theme, subTheme and publisher store the id of the corresponding mod
    public Integer genre;
    public Integer subGenre;
    public Integer theme;
    public Integer subTheme;
    public TargetGroup targetGroup;
    public Integer publisher;
    public int releaseYear;
    public int rating;
    public SequelNumeration sn;
    // if null the platform id is not set
    // if this is set, the developer must me a publisher
    public Integer platformId;
    // sequels have the same platform
    public Boolean staticPlatform;
    public Boolean exclusiveGame;
    public Boolean mmo;
    public Boolean f2p;
    public Boolean nospin;

    /**
     * Construct a new npcIp.
     *
     * @param name        The name of the npcIp
     * @param id          The id of this mod, can be null. If null the id will be set when the content is added to the game
     * @param genre       The id of the main genre
     * @param subGenre    The id of the sub-genre
     * @param theme       The id of the main theme
     * @param subTheme    The id of the sub-theme
     * @param targetGroup The target group of the npcIp
     * @param publisher   The publisher
     * @param releaseYear The release year
     * @param rating      The rating in percent 0-100%
     */
    public NpcIp(String name, Integer id, int genre, Integer subGenre, int theme, Integer subTheme, TargetGroup targetGroup, int publisher, int releaseYear, int rating, SequelNumeration sn, Integer platformId, Boolean staticPlatform, Boolean exclusiveGame, Boolean mmo, Boolean f2p, Boolean nospin) throws IllegalArgumentException {
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
        this.sn = sn;
        this.platformId = platformId;
        this.staticPlatform = staticPlatform;
        this.exclusiveGame = exclusiveGame;
        this.mmo = mmo;
        this.f2p = f2p;
        this.nospin = nospin;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> exportMap = new HashMap<>();
        exportMap.put("NAME EN", name);
        exportMap.put("genre", GenreManager.INSTANCE.getContentNameById(genre));
        if (subGenre != null) {
            exportMap.put("sub_genre", GenreManager.INSTANCE.getContentNameById(subGenre));
        }
        exportMap.put("theme", ThemeManager.INSTANCE.getContentNameById(theme));
        if (subTheme != null) {
            exportMap.put("sub_theme", ThemeManager.INSTANCE.getContentNameById(theme));
        }
        exportMap.put("target_group", targetGroup.getDataType());
        exportMap.put("publisher", PublisherManager.INSTANCE.getContentNameById(publisher));
        exportMap.put("release_year", Integer.toString(releaseYear));
        exportMap.put("rating", Integer.toString(rating));
        if (sn != null && sn != SequelNumeration.NONE) {
            exportMap.put("SEQUEL_NUMERATION", sn.name());
        }
        if (this.platformId != null) {
            exportMap.put("platform_id", this.platformId.toString());
        }
        if (this.staticPlatform != null) {
            exportMap.put("static_platform", this.staticPlatform.toString());
        }
        if (this.exclusiveGame != null) {
            exportMap.put("exclusive_game", this.exclusiveGame.toString());
        } 
        if (this.mmo != null) {
            exportMap.put("mmo", this.mmo.toString());
        }
        if (this.f2p != null) {
            exportMap.put("f2p", this.f2p.toString());
        }
        if (this.nospin != null) {
            exportMap.put("nospin", this.nospin.toString());
        }
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
    public void changeExportMap(Map<String, String> map) {
        // Nothing has to be done here, everything has been completed in getMap
    }

    @Override
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <P").append(publisher).append("><G").append(genre).append(">");
        if (subGenre != null) {
            sb.append("<SG").append(subGenre).append(">");
        }
        sb.append("<T").append(theme).append(">");
        if (subTheme != null) {
            sb.append("<ST").append(subTheme).append(">");
        }
        sb.append("<%").append(rating)
                        .append("><Y").append(releaseYear).append("><TG").append(targetGroup.getId()).append(">");
        if (sn != null && sn != SequelNumeration.NONE) {
            sb.append("<").append(sn.name()).append(">");
        }
        if (platformId != null) {
            sb.append("<PL(").append(platformId).append(")>"); 
        }
        if (staticPlatform != null && staticPlatform == true) {
            sb.append("<PLSTATIC>");
        }
        if (exclusiveGame != null && exclusiveGame == true) {
            sb.append("<EX>");
        }
        if (mmo != null && mmo == true) {
            sb.append("<MMO>");
        }
        if (f2p != null && f2p == true) {
            sb.append("<F2P>");
        }
        if (nospin != null && nospin == true) {
            sb.append("<NOSPIN>");
        }
        return sb.toString();
    }
}
