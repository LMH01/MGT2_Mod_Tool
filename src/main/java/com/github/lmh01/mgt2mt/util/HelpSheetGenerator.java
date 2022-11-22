package com.github.lmh01.mgt2mt.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lmh01.mgt2mt.content.instances.Genre;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.TargetGroup;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;

public class HelpSheetGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpSheetGenerator.class);
   
    private static final int THEMES_PER_LINE = 20;
    private static final int GENRES_PER_LINE = 7;

    /**
     * Generates a markdown file with the perfect slider settings per genre for the current game files.
     */
    public static void generate() throws ModProcessingException {

        StringBuilder sb = new StringBuilder();
        sb.append("# Help sheet\n\n");
        sb.append("Generated on " + Utils.getCurrentDateTime() + "\n\n");
        sb.append("## Genres\n\n");
        // Construct all genres
        ArrayList<Genre> genres = new ArrayList<>();
        for (String genreName : GenreManager.INSTANCE.getContentByAlphabet()) {
            genres.add((Genre)GenreManager.INSTANCE.constructContentFromName(genreName));
        }

        for (Genre genre : genres) {
            sb.append("### " + genre.name + "\n\n");
            sb.append("| | Value|\n");
            sb.append("|-|------|\n");
            // Target group
            boolean first_tg = true;
            for (TargetGroup tg : genre.targetGroups) {
                if (first_tg) {
                    sb.append("| Traget audience | " + tg.getTypeName() + " |\n");
                    first_tg = false;
                } else {
                    sb.append("| | " + tg.getTypeName() + " |\n");
                }
            }
            // Genre combinations
            boolean first_genre_comb = true;
            for (Integer genre_id : genre.compatibleGenres) {
                if (first_genre_comb) {
                    sb.append("| Genre combinations | " + GenreManager.INSTANCE.getContentNameById(genre_id) + " |\n");
                    first_genre_comb = false;
                } else {
                    sb.append("| | " + GenreManager.INSTANCE.getContentNameById(genre_id) + " |\n");
                }
            }
            // Design slider
            sb.append("| Design slider | " + I18n.INSTANCE.get("commonText.gameLength") + ": " + genre.focus0 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.gameDepth") + ": " + genre.focus1 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.beginnerFriendliness") + ": " + genre.focus2 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.innovation") + ": " + genre.focus3 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.story") + ": " + genre.focus4 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.characterDesign") + ": " + genre.focus5 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.levelDesign") + ": " + genre.focus6 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.missionDesign") + ": " + genre.focus7 + " |\n");

            // Alignment
            sb.append("| Alignment | " + I18n.INSTANCE.get("commonText.coreGamersCasualGamers") + ": " + genre.align0 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.nonviolentExtremeViolent") + ": " + genre.align1 + " |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.easyHard") + ": " + genre.align2 + " |\n");
           
            // Priority
            sb.append("| Priority | " + I18n.INSTANCE.get("commonText.gameplay") + ": " + genre.gameplay + "% |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.graphic") + ": " + genre.graphic + "% |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.sound") + ": " + genre.sound + "% |\n");
            sb.append("| | " + I18n.INSTANCE.get("commonText.tech") + ": " + genre.control + "% |\n\n");

            // Themes
            sb.append("#### Themes\n\n");
            ArrayList<String> themeNames = new ArrayList<>();
            int counter = 0;
            boolean firstTheme = true;
            for (Integer themeId : genre.compatibleThemes) {
                themeNames.add(ThemeManager.INSTANCE.getContentNameById(themeId));
            }
            themeNames.sort(Comparator.naturalOrder());
            for (String theme : themeNames) {
                if (firstTheme) {
                    firstTheme = false;
                } else {
                    sb.append(", ");
                }
                if (counter == THEMES_PER_LINE) {
                    sb.append("\n");
                    counter = 0;
                }
                sb.append(theme);
                counter += 1;
            }
            sb.append("\n\n");

            // Genre comb
            ArrayList<List<Integer>> combs = new ArrayList<>();
            ArrayList<String> combNames = new ArrayList<>();
            for (Genre comb : genres) {
                if (comb != genre) {
                    combs.add(GenreManager.getComboSliderSettings(genre, comb));
                    combNames.add(comb.name);
                }
            }
            sb.append("#### " + genre.name +  " combinations\n\n");
            int remainingGenres = combs.size();
            int innerOffset = 0;
            while (remainingGenres > 0) {
                int collums = GENRES_PER_LINE;
                if (remainingGenres < GENRES_PER_LINE) {
                    collums = remainingGenres;
                }
                sb.append("| |");
                for (int i = 0; i < collums; i++) {
                    sb.append(" " + combNames.get(i + innerOffset) + " |");
                }
                sb.append("\n|-|");
                for (int i = 0; i < collums; i++) {
                    sb.append("-|");
                }
                sb.append("\n");
                appendCombLine(sb, combs, collums, "commonText.gameLength", 0, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.gameDepth", 1, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.beginnerFriendliness", 2, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.innovation", 3, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.story", 4, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.characterDesign", 5, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.levelDesign", 6, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.missionDesign", 7, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.coreGamersCasualGamers", 8, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.nonviolentExtremeViolent", 9, innerOffset);
                appendCombLine(sb, combs, collums, "commonText.easyHard", 10, innerOffset);
                sb.append("\n");
                innerOffset += collums;
                remainingGenres -= collums;
            }
        }
        
        try {
            Path path = ModManagerPaths.MAIN.getPath().resolve("help_sheet.md");
            if (Files.exists(path)) {
                Files.delete(path);
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()));
            bw.write(sb.toString());
            bw.close();
            LOGGER.info("Cheat sheet has been generated successfully and is available here: " + path.toString());
        } catch (IOException e) {
            throw new ModProcessingException("Unable to generate cheat sheet", e);
        }
   } 

   /**
    * Appends a genre comb line to the string builder.
    * @param translationKey Translation for the value name
    * @param index The index in the inner combs list
    * @param listOffset Offset of the inner comb list
    */
   private static void appendCombLine(StringBuilder sb, ArrayList<List<Integer>> combs, int collums, String translationKey, int index, int listOffset) {
        sb.append(I18n.INSTANCE.get(translationKey) + " |");
        for (int i = 0; i < collums; i++) {
            sb.append(" " + combs.get(i+listOffset).get(index) + " |");
        }
        sb.append("\n");
   }
}
