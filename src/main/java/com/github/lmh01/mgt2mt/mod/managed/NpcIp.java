package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.Utils;

import java.util.*;

/**
 * Symbolizes a single npc ip
 */
public class NpcIp {
    //TODO This class will sometime extend a new base mod class.
    // Function getDependencyMap will be implemented here

    // genre, subGenre, theme, subTheme and platform store the id of the corresponding mod
    public String name;
    public Integer genre;
    public Integer subGenre;
    public Integer theme;
    public Integer subTheme;
    public TargetGroup targetGroup;
    public Integer platform;
    public int releaseYear;
    public int rating;

    /**
     * Construct a new npcIp. The required values will be taken from the line.
     * @param line The line that stands in the game files.
     * @param constructionType The way the mod should be constructed.
     *                         If construction type is {@link ModConstructionType#IMPORT} the mod names in the line will be replaced by the mod ids
     */
    public NpcIp(String line, ModConstructionType constructionType) throws ModProcessingException {
        this.name = Utils.getFirstPart(line);
        ArrayList<String> data = Utils.getEntriesFromString(line);
        String placeholder = getPlaceholder(constructionType);
        for (String d : data) {
            if (d.startsWith("P" + placeholder)) {
                this.platform = getModId(ModManager.platformMod, d, "P" + placeholder, constructionType);
            } else if (d.startsWith("G" + placeholder)) {
                this.genre = getModId(ModManager.genreMod, d, "G" + placeholder, constructionType);
            } else if (d.startsWith("SG" + placeholder)) {
                this.subGenre = getModId(ModManager.genreMod, d, "SG" + placeholder, constructionType);
            } else if (d.startsWith("TG" + placeholder)) {
                int tgId = Integer.parseInt(d.replaceAll("[^0-9]", ""));
                for (TargetGroup tg : TargetGroup.values()) {
                    if (tg.getId() == tgId) {
                        this.targetGroup = tg;
                    }
                }
            }  else if (d.startsWith("T" + placeholder)) {
                this.theme = getModId(ModManager.themeMod, d, "T" + placeholder, constructionType);
            } else if (d.startsWith("ST" + placeholder)) {
                this.subTheme = getModId(ModManager.themeMod, d, "ST" + placeholder, constructionType);
            }else if (d.startsWith("Y" + placeholder)) {
                this.releaseYear = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("%" + placeholder)) {
                this.rating = Integer.parseInt(d.replaceAll("%" + placeholder, ""));
            }
        }
    }

    /**
     * Construct a new npcIp.
     * @param name The name of the npcIp
     * @param genre The id of the main genre
     * @param subGenre The id of the sub-genre
     * @param theme The id of the main theme
     * @param subTheme The id of the sub-theme
     * @param targetGroup The target group of the npcIp
     * @param platform The main platform
     * @param releaseYear The release year
     * @param rating The rating in percent 0-100%
     */
    public NpcIp(String name, int genre, Integer subGenre, int theme, Integer subTheme, TargetGroup targetGroup, int platform, int releaseYear, int rating) throws IllegalArgumentException {
        if (rating < 0 || rating > 100) {
            throw new IllegalArgumentException("Unable to construct new NpcIp: The rating entered is invalid!");
        }
        this.name = name;
        this.genre = genre;
        this.subGenre = subGenre;
        this.theme = theme;
        this.subTheme = subTheme;
        this.targetGroup = targetGroup;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    /**
     * Returns the placeholder string that is required to construct a new mod.
     * The placeholder is either "" or "-", dependent on the mod construction type.
     */
    private String getPlaceholder(ModConstructionType constructionType) {
        if (constructionType == ModConstructionType.GAME_FILES) {
            return "";
        } else {
            return "-";
        }
    }

    /**
     * Returns the mod id for the given mod.
     * @param mod The mod for which the mod id should be determined
     * @param data The string that contains the data
     * @param identifier The string that should be replaced
     * @param constructionType Determines how the mod id should be determined
     */
    private int getModId(AbstractBaseMod mod, String data, String identifier, ModConstructionType constructionType) throws ModProcessingException {
        if (constructionType == ModConstructionType.GAME_FILES) {
            return Integer.parseInt(data.replaceAll(identifier, ""));
        } else {
            return mod.getModIdByNameFromImportHelperMap(data.replaceFirst(identifier, ""));
        }
    }

    /**
     * The new version of {@link DependentMod#getDependencyMap(Object)}.
     * Uses the member variables of the mod type to generate the dependency map.
     */
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add(ModManager.genreMod.getContentNameById(genre));
        if (subGenre != null) {
            genres.add(ModManager.genreMod.getContentNameById(subGenre));
        }
        map.put(ModManager.genreMod.getExportType(), genres);
        Set<String> themes = new HashSet<>();
        themes.add(ModManager.themeMod.getContentNameById(theme));
        if (subTheme != null) {
            themes.add(ModManager.themeMod.getContentNameById(subTheme));
        }
        map.put(ModManager.themeMod.getExportType(), themes);
        Set<String> platforms = new HashSet<>();
        platforms.add(ModManager.platformMod.getContentNameById(platform));
        map.put(ModManager.platformMod.getExportType(), platforms);
        return map;
    }

    /**
     * Returns the mod represented as line
     */
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <G").append(genre).append("><T").append(theme)
                .append("><TG").append(targetGroup.getId()).append("><P").append(platform)
                .append("><%").append(rating).append("><Y").append(releaseYear).append(">");
        if (subGenre != null) {
            sb.append("<SG").append(subGenre).append(">");
        }
        if (subTheme != null) {
            sb.append("<ST").append(subTheme).append(">");
        }
        return sb.toString();
    }

    /**
     * The new version of {@link AbstractSimpleMod#getModifiedExportLine(String)}.
     * Uses the member variables to construct the export line.
     */
    public String getExportLine() throws ModProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <G-").append(ModManager.genreMod.getContentNameById(genre)).append("><T-")
                .append(ModManager.themeMod.getContentNameById(theme)).append("><TG-")
                .append(targetGroup.getId()).append("><P-")
                .append(ModManager.platformMod.getContentNameById(platform))
                .append("><%-").append(rating).append("><Y-").append(releaseYear).append(">");
        if (subGenre != null) {
            sb.append("<SG-").append(ModManager.genreMod.getContentNameById(subGenre)).append(">");
        }
        if (subTheme != null) {
            sb.append("<ST-").append(ModManager.themeMod.getContentNameById(subTheme)).append(">");
        }
        return sb.toString();
    }
}
