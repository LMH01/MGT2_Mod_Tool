package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
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
     * @param genreTranslations The map that includes the genre name translations
     * @param compatibleThemeIds A set containing all compatible theme ids
     * @param genreScreenshots Array list containing all screenshot files
     * @param showSummaryFromImport True when called from genre import
     * @param genreIcon The genre icon file
     * @param showMessages True when the messages should be shown. False if not.
     * @return Returns true when the user clicked yes on the confirm popup
     */
    public static boolean addGenre(Map<String, String> map, Map<String, String> genreTranslations,Set<Integer> compatibleThemeIds, Set<Integer> gameplayFeaturesBadIds, Set<Integer> gameplayFeaturesGoodIds, ArrayList<File> genreScreenshots, boolean showSummaryFromImport, File genreIcon, boolean showMessages){

        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        String messageBody = I18n.INSTANCE.get("dialog.genreManager.addGenre.mainBody.genreIsReady") + "\n\n" +
                I18n.INSTANCE.get("commonText.id") + ":" + map.get("ID") + "\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + map.get("PRICE") + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "\n" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "\n" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + getTargetGroups(map) + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.compatibleGenres") + "*\n\n" + getCompatibleGenres(map) + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.compatibleThemes") + "*\n\n" + getMapEntryToDisplay(map, "THEME COMB", 15) + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.goodGameplayFeatures") + "*\n\n" + getMapEntryToDisplay(map, "GAMEPLAYFEATURE GOOD", 10) + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.badGameplayFeatures") + "*\n\n" + getMapEntryToDisplay(map, "GAMEPLAYFEATURE BAD", 10) + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.designFocus") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameLength") + ": " + map.get("FOCUS0") + "\n" +
                I18n.INSTANCE.get("commonText.gameDepth") + ": " + map.get("FOCUS1") + "\n" +
                I18n.INSTANCE.get("commonText.beginnerFriendliness") + ": " + map.get("FOCUS2") + "\n" +
                I18n.INSTANCE.get("commonText.innovation") + ": " + map.get("FOCUS3") + "\n" +
                I18n.INSTANCE.get("commonText.story") + ": " + map.get("FOCUS4") + "\n" +
                I18n.INSTANCE.get("commonText.characterDesign") + ": " + map.get("FOCUS5") + "\n" +
                I18n.INSTANCE.get("commonText.levelDesign") + ": " + map.get("FOCUS6") + "\n" +
                I18n.INSTANCE.get("commonText.missionDesign") + ": " + map.get("FOCUS7") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.designDirection") + "*\n\n" +
                I18n.INSTANCE.get("commonText.coreGamersCasualGamers") + ": " + map.get("ALIGN0") + "\n" +
                I18n.INSTANCE.get("commonText.nonviolentExtremeViolent") + ": " + map.get("ALIGN1") + "\n" +
                I18n.INSTANCE.get("commonText.easyHard") + ": " + map.get("ALIGN2") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.workPriority") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + map.get("GAMEPLAY") + "%\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + map.get("GRAPHIC") + "%\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + map.get("SOUND") + "%\n" +
                I18n.INSTANCE.get("commonText.control") + ": " + map.get("CONTROL") + "%\n";
        int returnValue;
        if(showSummaryFromImport){
            if(showMessages){
                String messageBodyButtonExplanation = "\n" + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var1");
                returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
            }else{
                returnValue = 0;
            }
        }else{
            String messageBodyButtonExplanation = "\n" + I18n.INSTANCE.get("dialog.genreManager.addGenre.bodyButtonExplanation.var2");
            returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, I18n.INSTANCE.get("dialog.genreManager.addGenre.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }
        if(returnValue == 0){
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
                    ModManager.gameplayFeatureMod.getEditor().addGenreId(gameplayFeaturesGoodIds, Integer.parseInt(map.get("ID")), false);
                    GenreManager.genreAdded(map, genreIcon, showMessages);
                    if(showSummaryFromImport){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + map.get("NAME EN"));
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
        if(returnValue == JOptionPane.YES_OPTION){
            return true;
        }else{
            return false;
        }
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
     * @param objectsPerLine Set how many objects should be dispalyed per line
     * @return Returns a string containing all entries under the map key ready to display
     */
    private static String getMapEntryToDisplay(Map<String, String> map, String mapKey, int objectsPerLine){
        String input = map.get(mapKey);
        StringBuilder currentString = new StringBuilder();
        ArrayList<String> outputArray = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        for(int i=0; i<input.length(); i++){
            if(String.valueOf(input.charAt(i)).equals("<")){
                //Nothing happens
            }else if (String.valueOf(input.charAt(i)).equals(">")){
                outputArray.add(currentString.toString());
                currentString = new StringBuilder();
            }else{
                currentString.append(input.charAt(i));
            }
        }

        int n = 0;
        for(int i=0; i<outputArray.size(); i++){
            if(i == outputArray.size()-1){
                output.append(outputArray.get(i));
            }else{
                output.append(outputArray.get(i)).append(", ");
                if(n == objectsPerLine){
                    output.append(System.getProperty("line.separator"));
                    n = 0;
                }
            }
            n++;
        }
        return output.toString();
    }

    /**
     * @param map The map where the GENRE COMB values are stored.
     * @return Returns a string containing all genres that are compatible with the new genre. This is called when the compatible genres are displayed in the summary.
     */
    private static String getCompatibleGenres(Map<String, String> map){
        String inputGenres = map.get("GENRE COMB");
        StringBuilder currentGenre = new StringBuilder();
        ArrayList<String> outputGenres = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        for(int i=0; i<inputGenres.length(); i++){
            if(String.valueOf(inputGenres.charAt(i)).equals("<")){
                //Nothing happens
            }else if (String.valueOf(inputGenres.charAt(i)).equals(">")){
                outputGenres.add(ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(currentGenre.toString()), true));
                currentGenre = new StringBuilder();
            }else{
                currentGenre.append(inputGenres.charAt(i));
            }
        }

        int n = 0;
        for(int i=0; i<outputGenres.size(); i++){
            if(i == outputGenres.size()-1){
                output.append(outputGenres.get(i));
            }else{
                output.append(outputGenres.get(i)).append(", ");
                if(n == 5){
                    output.append(System.getProperty("line.separator"));
                    n = 0;
                }
            }
            n++;
        }
        return output.toString();
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
