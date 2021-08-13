package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GenreManager {

    //New Variables
    public static Map<String, String> mapNameTranslations = new HashMap<>();
    public static Map<String, String> mapDescriptionTranslations = new HashMap<>();
    public static Map<String, String> mapNewGenre = new HashMap<>();//This is the map that contains all information on the new genre.
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreManager.class);

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws IOException {
        ModManager.genreMod.getAnalyzer().analyzeFile();
        resetVariables();
        try {
            Backup.createBackup(ModManager.genreMod.getFile());
            LOGGER.info("Adding new genre");
            openStepWindow(1);
        } catch (IOException e) {
            if(Utils.showConfirmDialog(1, e)){
                LOGGER.info("Adding new genre");
                openStepWindow(1);
            }
            e.printStackTrace();
        }
    }
    public static void openStepWindow(int step){
        switch(step){
            case 1: WindowAddGenrePage1.createFrame(); break;
            case 2: WindowAddGenrePage2.createFrame(); break;
            case 3: WindowAddGenrePage3.createFrame(); break;
            case 4: WindowAddGenrePage4.createFrame(); break;
            case 5: WindowAddGenrePage5.createFrame(); break;
            case 6: WindowAddGenrePage6.createFrame(); break;
            case 7: WindowAddGenrePage7.createFrame(); break;
            case 8: WindowAddGenrePage8.createFrame(); break;
            case 9: WindowAddGenrePage9.createFrame(); break;
            case 10: WindowAddGenrePage10.createFrame(); break;
            case 11: WindowAddGenrePage11.createFrame(); break;
        }
    }

    /**
     * Resets all variables used to add a new genre
     */
    public static void resetVariables(){
        mapNewGenre.clear();
        mapNewGenre.put("ID", Integer.toString(ModManager.genreMod.getAnalyzer().getFreeId()));
        mapNewGenre.put("UNLOCK YEAR", "1976");
        mapNewGenre.put("UNLOCK MONTH", "JAN");
        mapNewGenre.put("RES POINTS", "1000");
        mapNewGenre.put("DEV COSTS", "3000");
        mapNewGenre.put("PRICE", "150000");
        mapNewGenre.put("TGROUP", "");
        mapNewGenre.put("FOCUS0", "5");
        mapNewGenre.put("FOCUS1", "5");
        mapNewGenre.put("FOCUS2", "5");
        mapNewGenre.put("FOCUS3", "5");
        mapNewGenre.put("FOCUS4", "5");
        mapNewGenre.put("FOCUS5", "5");
        mapNewGenre.put("FOCUS6", "5");
        mapNewGenre.put("FOCUS7", "5");
        mapNewGenre.put("ALIGN0", "5");
        mapNewGenre.put("ALIGN1", "5");
        mapNewGenre.put("ALIGN2", "5");
        mapNewGenre.put("GAMEPLAY", "25");
        mapNewGenre.put("GRAPHIC", "25");
        mapNewGenre.put("SOUND", "25");
        mapNewGenre.put("CONTROL", "25");
        mapNameTranslations.clear();
        mapDescriptionTranslations.clear();
        WindowAddGenrePage1.clearTranslationArrayLists();
    }

    /**
     * Ads a new genre to mad games tycoon 2. Shows a summary for the genre that should be added.
     * @param map The map that includes the values.
     * @param compatibleThemeIds A set containing all compatible theme ids
     * @param genreScreenshots Array list containing all screenshot files
     * @param showSummaryFromImport True when called from genre import
     * @param genreIcon The genre icon file
     * @param showMessages True when the messages should be shown. False if not.
     * @return Returns true when the user clicked yes on the confirm popup
     */
    public static boolean addGenre(Map<String, String> map, Set<Integer> compatibleThemeIds, Set<Integer> gameplayFeaturesBadIds, Set<Integer> gameplayFeaturesGoodIds, ArrayList<File> genreScreenshots, boolean showSummaryFromImport, File genreIcon, boolean showMessages){

        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        JLabel labelFirstPart = new JLabel("<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.mainBody.genreIsReady") + "<br><br>" +
                I18n.INSTANCE.get("commonText.id") + ":" + map.get("ID") + "<br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "<br>" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + getTargetGroups(map) + "<br>" + "");
        JButton buttonCompatibleGenres = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleGenres"), convertMapEntryToList(map, "GENRE COMB", true), I18n.INSTANCE.get("commonText.compatibleGenres") + ":");
        JButton buttonCompatibleThemes = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.compatibleThemes"), convertMapEntryToList(map, "THEME COMB"), I18n.INSTANCE.get("commonText.compatibleThemes") + ":");
        JButton buttonBadGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.badGameplayFeatures"), convertMapEntryToList(map, "GAMEPLAYFEATURE BAD"), I18n.INSTANCE.get("commonText.badGameplayFeatures") + ":");
        JButton buttonGoodGameplayFeatures = WindowHelper.getListDisplayButton(I18n.INSTANCE.get("commonText.goodGameplayFeatures"), convertMapEntryToList(map, "GAMEPLAYFEATURE GOOD"), I18n.INSTANCE.get("commonText.goodGameplayFeatures") + ":");
        JLabel labelSecondPart = new JLabel("<html>*" + I18n.INSTANCE.get("commonText.designFocus") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameLength") + ": " + map.get("FOCUS0") + "<br>" +
                I18n.INSTANCE.get("commonText.gameDepth") + ": " + map.get("FOCUS1") + "<br>" +
                I18n.INSTANCE.get("commonText.beginnerFriendliness") + ": " + map.get("FOCUS2") + "<br>" +
                I18n.INSTANCE.get("commonText.innovation") + ": " + map.get("FOCUS3") + "<br>" +
                I18n.INSTANCE.get("commonText.story") + ": " + map.get("FOCUS4") + "<br>" +
                I18n.INSTANCE.get("commonText.characterDesign") + ": " + map.get("FOCUS5") + "<br>" +
                I18n.INSTANCE.get("commonText.levelDesign") + ": " + map.get("FOCUS6") + "<br>" +
                I18n.INSTANCE.get("commonText.missionDesign") + ": " + map.get("FOCUS7") + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.designDirection") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.coreGamersCasualGamers") + ": " + map.get("ALIGN0") + "<br>" +
                I18n.INSTANCE.get("commonText.nonviolentExtremeViolent") + ": " + map.get("ALIGN1") + "<br>" +
                I18n.INSTANCE.get("commonText.easyHard") + ": " + map.get("ALIGN2") + "<br>" +
                "<br>*" + I18n.INSTANCE.get("commonText.workPriority") + "*<br><br>" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + map.get("GAMEPLAY") + "%<br>" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + map.get("GRAPHIC") + "%<br>" +
                I18n.INSTANCE.get("commonText.sound") + ": " + map.get("SOUND") + "%<br>" +
                I18n.INSTANCE.get("commonText.control") + ": " + map.get("CONTROL") + "%<br><br>");
        int returnValue;
        if(showSummaryFromImport){
            if(showMessages){
                labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var1"));
                Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
                returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
            }else{
                returnValue = 0;
            }
        }else{
            labelSecondPart.setText(labelSecondPart.getText() + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var2"));
            Object[] params = {labelFirstPart, buttonCompatibleGenres, buttonCompatibleThemes, buttonBadGameplayFeatures, buttonGoodGameplayFeatures, labelSecondPart};
            returnValue = JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }
        if(returnValue == JOptionPane.YES_OPTION){
            //click yes
            boolean continueAnyway = false;
            boolean imageFileAccessedSuccess = false;
            try {
                ImageFileHandler.addGenreImageFiles(Integer.parseInt(map.get("ID")), map.get("NAME EN"), genreIcon, genreScreenshots);
                imageFileAccessedSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var1"), I18n.INSTANCE.get("frame.title.continueAnyway"), JOptionPane.YES_NO_OPTION) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway | imageFileAccessedSuccess){
                try {
                    ModManager.genreMod.getEditor().addMod(map);
                    ModManager.themeMod.getEditor().editGenreAllocation(Integer.parseInt(map.get("ID")), true, compatibleThemeIds);
                    ModManager.gameplayFeatureMod.getEditor().addGenreId(gameplayFeaturesGoodIds, Integer.parseInt(map.get("ID")), true);
                    ModManager.gameplayFeatureMod.getAnalyzer().analyzeFile();
                    ModManager.gameplayFeatureMod.getEditor().addGenreId(gameplayFeaturesBadIds, Integer.parseInt(map.get("ID")), false);
                    GenreManager.genreAdded(map, genreIcon, showMessages);
                    if(showSummaryFromImport){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN"));
                    }else{
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + GenreManager.mapNewGenre.get("NAME EN"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.error.var3"));
            }
        }else if(returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION){
            //Click no or close window
            if(!showSummaryFromImport){
                WindowAddGenrePage11.createFrame();
            }
        }
        return returnValue == JOptionPane.YES_OPTION;
    }

    /**
     * @param map The map where the TGROUP values are stored.
     * @return Returns the compatible target groups to be displayed in the genre summary.
     */
    public static String getTargetGroups(Map<String, String> map){
        String targetGroups = "";
        if(map.get("TGROUP").contains("KID")){
            if(map.get("TGROUP").contains("TEEN") || map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.kid");
            }
        }
        if(map.get("TGROUP").contains("true")){
            if(map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.teen");
            }
        }
        if(map.get("TGROUP").contains("ADULT")){
            if(map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult") + ", ";
            }else{
                targetGroups = targetGroups + I18n.INSTANCE.get("commonText.adult");
            }
        }
        if(map.get("TGROUP").contains("OLD")){
            targetGroups = targetGroups + I18n.INSTANCE.get("commonText.senior");
        }
        return targetGroups;
    }

    /**
     * @param map The map where the map key is stored
     * @param mapKey The key where the content is written
     * @return Returns a string containing all entries that are listed as value under the map key.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey){
        return convertMapEntryToList(map, mapKey, false);
    }

    /**
     * @param map The map where the map key is stored
     * @param mapKey The key where the content is written
     * @param convertAsGenres If true the genre names will be written to the list
     * @return Returns a string containing all entries that are listed as value under the map key.
     */
    private static String[] convertMapEntryToList(Map<String, String> map, String mapKey, boolean convertAsGenres){
        String input = map.get(mapKey);
        StringBuilder currentString = new StringBuilder();
        ArrayList<String> outputArray = new ArrayList<>();

        for(int i=0; i<input.length(); i++){
            if(String.valueOf(input.charAt(i)).equals("<")){
                //Nothing happens
            }else if (String.valueOf(input.charAt(i)).equals(">")){
                if(convertAsGenres){
                    outputArray.add(ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(currentString.toString())));
                }else{
                    outputArray.add(currentString.toString());
                }
                currentString = new StringBuilder();
            }else{
                currentString.append(input.charAt(i));
            }
        }
        Collections.sort(outputArray);
        String[] output = new String[outputArray.size()];
        outputArray.toArray(output);
        return output;
    }

    /**
     * @return Returns the image file name for the input genre name
     */
    public static String getImageFileName(String genreName){
        return "icon" + genreName.replaceAll(" ", "");
    }

    /**
     * Shows a message to the user that the genre has been added successfully and asks if the NPC_GAMES file should be modified.
     * @param map The map containing the genre name.
     * @param genreIcon The genre icon.
     */
    public static void genreAdded(Map<String, String> map, File genreIcon, boolean showMessages) throws IOException {
        String name = map.get("NAME EN");
        int id = ModManager.genreMod.getAnalyzer().getFreeId();
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.1") + " [" + name + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.2"), I18n.INSTANCE.get("dialog.genreManager.addGenre.genreAdded.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0){
                try {
                    NPCGameListChanger.editNPCGames(id, true, 20);
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("commonText.genre") + " " + I18n.INSTANCE.get("commonText.id") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.added"));
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "<html>" + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.1") + " [" + id + "] " + I18n.INSTANCE.get("dialog.genreManager.addGenre.npcGamesList.error.2") + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }else{
            NPCGameListChanger.editNPCGames(id,true, 20);
        }
    }
}
