package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
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

    //TODO Rewrite how genres are added and shared to use maps and make steps easier

    public static void addGenre(){
        JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this warning again");
        JLabel labelMessage = new JLabel("<html>Warning:<br>Loading a save-file with this new added genre will tie it to the file.<br>Removing the genre later won't remove it from save-files that have been accessed with said genre.<br><br>Note:<br>In case you don't know what an input field does hover over it with your mouse.<br>If you need additional help visit the Github repository and read the README.md file.<br>(You can open the Github repo by clicking \"Utilities -> Open Github Page\".<br><br>Add new genre?");
        Object[] params = {labelMessage,checkBoxDontShowAgain};
        LOGGER.info("enableAddGenreWarning: " + Settings.enableAddGenreWarning);
        boolean cancelAddGenre = false;
        if(Settings.enableAddGenreWarning){
            if(JOptionPane.showConfirmDialog(null, params, "Add genre?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0){
                cancelAddGenre = true;
            }
        }
        if(!cancelAddGenre){
            if(checkBoxDontShowAgain.isSelected()){
                Settings.enableAddGenreWarning = false;
                ExportSettings.export();
            }
            try {
                Backup.createBackup(Utils.getGenreFile());
                resetVariablesToDefault();
                LOGGER.info("Adding new genre");
                openStepWindow(1);
            } catch (IOException e) {
                if(Utils.showConfirmDialog(1, e)){
                    resetVariablesToDefault();
                    LOGGER.info("Adding new genre");
                    openStepWindow(1);
                }
                e.printStackTrace();
            }
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
        }
    }

    /**
     * Shows a summary for the genre that should be added
     * @param showSummaryFromImport True when called from genre import
     */
    public static void showSummary(boolean showSummaryFromImport){

        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(mapNewGenre.get("IMAGE FILE")));
        String messageBody = "Your genre is ready:\n\n" +
                "Id:" + mapNewGenre.get("ID") + "\n" +
                "Name: " + mapNewGenre.get("NAME EN") + "\n" +
                "Description: " + mapNewGenre.get("DESC EN") + "\n" +
                "Unlock date: " + mapNewGenre.get("UNLOCK MONTH") + " " + mapNewGenre.get("UNLOCK YEAR") + "\n" +
                "Research point cost: " + mapNewGenre.get("RES POINTS") + "\n" +
                "Research cost " + mapNewGenre.get("PRICE") + "\n" +
                "Development cost: " + mapNewGenre.get("DEV COSTS") + "\n" +
                "Pic: see top left\n" +
                "Target group: " + getTargetGroups() + "\n" +
                "\n*Compatible genres*\n\n" + getCompatibleGenres() + "\n" +
                "\n*Compatible themes*\n\n" + getCompatibleThemes() + "\n" +
                "\n*Design priority*\n\n" +
                "Gameplay/Visuals: " + mapNewGenre.get("DESIGN1") + "\n" +
                "Story/Game length: " + mapNewGenre.get("DESIGN2") + "\n" +
                "Atmosphere/Content: " + mapNewGenre.get("DESIGN3") + "\n" +
                "Game depth/Beginner-friendly: " + mapNewGenre.get("DESIGN4") + "\n" +
                "Core Gamers/Casual Gamer: " + mapNewGenre.get("DESIGN5") + "\n" +
                "\n*Work priority*\n\n" +
                "Gameplay: " + mapNewGenre.get("GAMEPLAY") + "%\n" +
                "Graphic: " + mapNewGenre.get("GRAPHIC") + "%\n" +
                "Sound: " + mapNewGenre.get("SOUND") + "%\n" +
                "Control: " + mapNewGenre.get("CONTROL") + "%\n";
        int returnValue;
        if(showSummaryFromImport){
            String messageBodyButtonExplanation = "\nClick yes to add this genre.\nClick no cancel this operation.";
            returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, "Add this genre?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }else{
            String messageBodyButtonExplanation = "\nClick yes to add this genre.\nClick no to return to the step by step guide.\nClick cancel to quit this guide.";
            returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, "Add this genre?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        }
        if(returnValue == 0){
            //click yes
            boolean continueAnyway = false;
            boolean imageFileAccessedSuccess = false;
            try {
                ImageFileHandler.addGenreImageFiles(Integer.parseInt(mapNewGenre.get("ID")), mapNewGenre.get("NAME EN"), new File(mapNewGenre.get("IMAGE FILE")));
                imageFileAccessedSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                if(JOptionPane.showConfirmDialog(null, "Error while processing image files.\nDo you wan't to add you new genre anyway?", "Continue anyway?", JOptionPane.YES_NO_OPTION) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway | imageFileAccessedSuccess){
                try {
                    EditGenreFile.addGenre();
                    EditThemeFiles.editGenreAllocation(Integer.parseInt(mapNewGenre.get("ID")), true);
                    GenreManager.genreAdded(showSummaryFromImport);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "The genre was not added:\nError while editing Genres.txt\nPlease try again with administrator rights.", "Unable to edit Genres.txt", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Your genre was not added.");
            }
        }else if(returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION){
            //Click no or close window
            if(!showSummaryFromImport){
                WindowAddGenrePage10.createFrame();
            }
        }
        WindowMain.checkActionAvailability();
    }
    /**
     * @return Returns the compatible target groups in the correct formatting to be put into the Genres.txt file.
     */
    public static String getTargetGroups(){
        String targetGroups = "";
        if(mapNewGenre.get("KID").equals("true")){
            if(mapNewGenre.get("TEEN").equals("true") || mapNewGenre.get("ADULT").equals("true") || mapNewGenre.get("SENIOR").equals("true")){
                targetGroups = targetGroups + "Kid, ";
            }else{
                targetGroups = targetGroups + "Kid";
            }
        }
        if(mapNewGenre.get("TEEN").equals("true")){
            if(mapNewGenre.get("ADULT").equals("true") || mapNewGenre.get("SENIOR").equals("true")){
                targetGroups = targetGroups + "Teen, ";
            }else{
                targetGroups = targetGroups + "Teen";
            }
        }
        if(mapNewGenre.get("ADULT").equals("true")){
            if(mapNewGenre.get("SENIOR").equals("true")){
                targetGroups = targetGroups + "Adult, ";
            }else{
                targetGroups = targetGroups + "Adult";
            }
        }
        if(mapNewGenre.get("SENIOR").equals("true")){
            targetGroups = targetGroups + "Senior";
        }
        return targetGroups;
    }
    /**
     * @return Returns a string containing all genres that are compatible with the new genre.
     */
    private static String getCompatibleGenres(){
        //StringBuilder compatibleGenres = new StringBuilder();
        int n = 0;
        int charPositon = 0;
        StringBuilder currentGenre = new StringBuilder();
        String compatibleGenres = mapNewGenre.get("COMPATIBLE GENRES");
        for(int i=0; i<compatibleGenres.length(); i++){
            if(String.valueOf(compatibleGenres.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(compatibleGenres.charAt(charPositon)).equals(">")){
                currentGenre.append("<").append(genreId).append(">");
                currentGenre = new StringBuilder();
            }else{
                currentGenre.append(compatibleGenres.charAt(charPositon));
            }
            charPositon++;
        }
        for(int i = 0; i< ARRAY_LIST_COMPATIBLE_GENRES.size(); i++){
            if(i== ARRAY_LIST_COMPATIBLE_GENRES.size()-1){
                compatibleGenres.append(ARRAY_LIST_COMPATIBLE_GENRES.get(i));
            }else{
                compatibleGenres.append(ARRAY_LIST_COMPATIBLE_GENRES.get(i)).append(", ");
                if(n == 5){
                    compatibleGenres.append(System.getProperty("line.separator"));
                    n = 0;
                }
            }
            n++;
        }
        return compatibleGenres.toString();
    }
    /**
     * @return Returns a string containing all themes that are compatible with the new genre.
     */
    private static String getCompatibleThemes(){
        ArrayList<String> sortedCompatibleThemes = new ArrayList();
        StringBuilder compatibleThemes = new StringBuilder();
        for(Map.Entry<Integer, String> entry : GenreManager.MAP_COMPATIBLE_THEMES.entrySet()){
            sortedCompatibleThemes.add(entry.getValue());
        }
        int n = 1;
        Collections.sort(sortedCompatibleThemes);
        for(int i = 0; i<sortedCompatibleThemes.size(); i++){
            if(i == sortedCompatibleThemes.size()-1){
                compatibleThemes.append(sortedCompatibleThemes.get(i));
            }else{
                compatibleThemes.append(sortedCompatibleThemes.get(i)).append(", ");
                if(n == 10){
                    compatibleThemes.append(System.getProperty("line.separator"));
                    n = 0;
                }
            }
            n++;
        }
        return compatibleThemes.toString();
    }

    public static void genreAdded(boolean showSummaryFromImport){
        if(showSummaryFromImport){
            ChangeLog.addLogEntry(1, name);
        }else{
            ChangeLog.addLogEntry(18,  name);
        }
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(GenreManager.imageFile.getPath()));
        if(JOptionPane.showConfirmDialog(null, "Your new genre [" + name + "] has been added successfully.\nDo you wan't to edit the NPC_Games list to include your new genre?\nNote: this can be undone with the feature [Add genre to NPC_Games].", "Genre added successfully!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0){
            try {
                NPCGameListChanger.editNPCGames(id, true, 20);
                JOptionPane.showMessageDialog(new Frame(), "Genre ID [" + id + "] has successfully\nbeen added to the NpcGames list.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Error while adding genre with id [" + id + "] to NpcGames.txt.\nnPlease try again with administrator rights.\nException: " + e.getMessage(), "Unable to edit NpcGames.txt", JOptionPane.ERROR_MESSAGE);
            }
        }
        WindowMain.checkActionAvailability();
    }

    /**
     * @return Returns a string containing the compatible genre ids. The ids are already in this format <id><id><id>...
     */
    public static String getCompatibleGenresIds(){
        ArrayList<Integer> arrayListGenreIDs = new ArrayList<>();
        StringBuilder compatibleGenresIds = new StringBuilder();
        if(Settings.enableDebugLogging){
            LOGGER.info("AnalyzeExistingGenres.genreList.size(): " + AnalyzeExistingGenres.genreList.size());
        }
        for (Map<String, String> map : AnalyzeExistingGenres.genreList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    int currentId = Integer.parseInt(entry.getValue());
                    if(ARRAY_LIST_COMPATIBLE_GENRES.contains(currentId)){
                        arrayListGenreIDs.add(currentId);
                    }
                }
            }
        }
        Collections.sort(arrayListGenreIDs);
        for (Integer arrayListGenreID : arrayListGenreIDs) {
            compatibleGenresIds.append("<").append(arrayListGenreID).append(">");
        }
        return compatibleGenresIds.toString();
    }

    /**
     * Call to add name translations to the genre.
     */
    public static void addNameTranslations(){
        boolean continueWithTranslations = true;
        if(Settings.enableGenreNameTranslationInfo){
            JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this information again");
            JLabel labelMessage = new JLabel("<html>Note:<br>The translation that you have entered in the \"main\"text field<br>will be used as the english translation.<br><br>Continue?");
            Object[] params = {labelMessage,checkBoxDontShowAgain};
            LOGGER.info("enableGenreNameTranslationInfo: " + Settings.enableGenreNameTranslationInfo);
            if(JOptionPane.showConfirmDialog(null, params, "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0){
                continueWithTranslations = false;
            }
            Settings.enableGenreNameTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export();
        }
        if(continueWithTranslations){
            mapNameTranslations = TranslationManager.getTranslationsMap();
        }
    }

    /**
     * Call to add descriptionTranslations to the genre.
     */
    public static void addDescriptionTranslations(){
        boolean continueWithTranslations = true;
        if(Settings.enableGenreDescriptionTranslationInfo){
            JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this information again");
            JLabel labelMessage = new JLabel("<html>Note:<br>The translation that you have entered in the \"main\"text field<br>will be used as the english translation.<br><br>Continue?");
            Object[] params = {labelMessage,checkBoxDontShowAgain};
            LOGGER.info("enableGenreDescriptionTranslationInfo: " + Settings.enableGenreDescriptionTranslationInfo);
            if(JOptionPane.showConfirmDialog(null, params, "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0){
                continueWithTranslations = false;
            }
            Settings.enableGenreDescriptionTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export();
        }
        if(continueWithTranslations){
            mapDescriptionTranslations = TranslationManager.getTranslationsMap();
        }
    }
}
