package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class NewGenreManager {
    public static int id = 18;
    public static String name = "";
    public static String description = "";
    public static ArrayList<String> arrayListNameTranslations = new ArrayList<>();
    public static ArrayList<String> arrayListDescriptionTranslations = new ArrayList<>();
    public static boolean nameTranslationsAdded = false;
    public static boolean descriptionTranslationsAdded = false;
    public static int unlockYear = 1976;
    public static String unlockMonth = "JAN";
    public static int researchPoints = 0;
    public static int price = 0;
    public static int devCost = 0;
    public static boolean targetGroupKid = false;
    public static boolean targetGroupTeen = false;
    public static boolean targetGroupAdult = false;
    public static boolean targetGroupSenior = false;
    public static final ArrayList<String> ARRAY_LIST_COMPATIBLE_GENRES = new ArrayList<>();
    public static final HashSet<Integer> MAP_COMPATIBLE_THEME_IDS = new HashSet<>();
    public static final HashMap<Integer, String> MAP_COMPATIBLE_THEMES = new HashMap<>();
    public static int gameplay;
    public static int graphic;
    public static int sound;
    public static int control;
    public static int design1;
    public static int design2;
    public static int design3;
    public static int design4;
    public static int design5;
    public static File imageFile;
    public static String imageFileName;
    public static boolean useDefaultImageFile;
    public static ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(NewGenreManager.class);
    public static final String FORBIDDEN_GENRE_NAMES = "[ID], [NAME EN], [NAME GE], [NAME CH], [NAME TU], [NAME FR], [DESC EN], [DESC GE], [DESC CH], [DESC TU], [DESC FR], [NAME PB], [DESC PB], [NAME HU], [DESC HU], [NAME CT], [DESC CT], [NAME ES], [DESC ES], [NAME PL], [DESC PL], [DATE], [RES POINTS], [PRICE], [DEV COSTS], [PIC], [TGROUP], [GAMEPLAY], [GRAPHIC], [SOUND], [CONTROL], [GENRE COMB], [DESIGN1], [DESIGN2], [DESIGN3], [DESIGN4], [DESIGN5]";


    public static void addGenre(){
        JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this warning again");
        JLabel labelMessage = new JLabel("<html>Warning:<br>Loading a save-file with this new added genre will tie it to the file.<br>Removing the genre later won't remove it from save-files that have been accessed with said genre.<br><br>Note:<br>In case you don't know what an input field does hover over it with your mouse.<br>If you need additional help visit the Github repository and read the README.md file.<br>(You can open the Github repo by clicking the \"Github\" button in the main menu.<br><br>Add new genre?");
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
                }else{
                    WindowAvailableMods.createFrame();
                }
                e.printStackTrace();
            }

        }else{
            WindowAvailableMods.createFrame();
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
    public static void showSummary(){

        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(imageFile.getPath()));
        int returnValue = JOptionPane.showConfirmDialog(null, "Your genre is ready:\n\n" +
                "Id:" + id + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Unlock date: " + unlockMonth + " " + unlockYear + "\n" +
                "Research point cost: " + researchPoints + "\n" +
                "Research cost " + price + "\n" +
                "Development cost: " + devCost + "\n" +
                "Pic: see top left\n" +
                "Target group: " + getTargetGroups() + "\n" +
                "\n*Compatible genres*\n\n" + getCompatibleGenres() + "\n" +
                "\n*Compatible themes*\n\n" + getCompatibleThemes() + "\n" +
                "\n*Design priority*\n\n" +
                "Gameplay/Visuals: " + design1 + "\n" +
                "Story/Game length: " + design2 + "\n" +
                "Atmosphere/Content: " + design3 + "\n" +
                "Game depth/Beginner-friendly: " + design4 + "\n" +
                "Core Gamers/Casual Gamer: " + design5 + "\n" +
                "\n*Work priority*\n\n" +
                "Gameplay: " + gameplay + "%\n" +
                "Graphic: " + graphic + "%\n" +
                "Sound: " + sound + "%\n" +
                "Control: " + control + "%\n" +
                "\nClick yes to add this genre.\nClick no to return to the step by step guide.\nClick cancel to quit this guide.", "Add this genre?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
        if(returnValue == 0){
            //click yes
            boolean continueAnyway = false;
            boolean imageFileAccessedSuccess = false;
            try {
                ImageFileHandler.addGenreImageFiles(id, name, imageFile);
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
                    EditThemes.editGenreAllocation(NewGenreManager.id, true);
                    NewGenreManager.genreAdded();
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "The genre was not added:\nError while editing Genres.txt\nPlease try again with administrator rights.", "Unable to edit Genres.txt", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Your genre was not added.");
                WindowAvailableMods.createFrame();
            }
        }else if(returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION){
            //Click no or close window
            WindowAddGenrePage10.createFrame();
        }else if (returnValue == JOptionPane.CANCEL_OPTION){
            //click cancel
            WindowAvailableMods.createFrame();
        }

    }

    /**
     * @return Returns the compatible target groups in the correct formatting to be put into the Genres.txt file.
     */
    public static String getTargetGroups(){
        String targetGroups = "";
        if(targetGroupKid){
            if(targetGroupTeen || targetGroupAdult || targetGroupSenior){
                targetGroups = targetGroups + "Kid, ";
            }else{
                targetGroups = targetGroups + "Kid";
            }
        }
        if(targetGroupTeen){
            if(targetGroupAdult || targetGroupSenior){
                targetGroups = targetGroups + "Teen, ";
            }else{
                targetGroups = targetGroups + "Teen";
            }
        }
        if(targetGroupAdult){
            if(targetGroupSenior){
                targetGroups = targetGroups + "Adult, ";
            }else{
                targetGroups = targetGroups + "Adult";
            }
        }
        if(targetGroupSenior){
            targetGroups = targetGroups + "Senior";
        }
        return targetGroups;
    }

    /**
     * @return Returns a string containing all genres that are compatible with the new genre.
     */
    private static String getCompatibleGenres(){
        StringBuilder compatibleGenres = new StringBuilder();
        int n = 0;
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
        for(Map.Entry<Integer, String> entry : NewGenreManager.MAP_COMPATIBLE_THEMES.entrySet()){
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

    public static void resetVariablesToDefault(){
        LOGGER.info("resetting genre variables...");
        id = AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size();
        name = "";
        description = "";
        arrayListNameTranslations.clear();
        arrayListDescriptionTranslations.clear();
        nameTranslationsAdded = false;
        descriptionTranslationsAdded = false;
        unlockYear = 1976;
        unlockMonth = "JAN";
        researchPoints = 1000;
        price = 150000;
        devCost = 3000;
        targetGroupKid = false;
        targetGroupTeen = false;
        targetGroupAdult = false;
        targetGroupSenior = false;
        ARRAY_LIST_COMPATIBLE_GENRES.clear();
        MAP_COMPATIBLE_THEMES.clear();
        gameplay = 25;
        graphic = 25;
        sound = 25;
        control = 25;
        design1 = 5;
        design2 = 5;
        design3 = 5;
        design4 = 5;
        design5 = 5;
        imageFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
        imageFileName = "iconSkill";
        useDefaultImageFile = true;
        arrayListScreenshotFiles.clear();
    }
    public static void genreAdded(){
        ChangeLog.addLogEntry(1, name);
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(NewGenreManager.imageFile.getPath()));
        if(JOptionPane.showConfirmDialog(null, "Your new genre [" + name + "] has been added successfully.\nDo you wan't to edit the NPC_Games list to include your new genre?\nNote: this can be undone with the feature [Add genre to NPC_Games].", "Genre added successfully!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0){
            try {
                NPCGameListChanger.editNPCGames(id, true, 20);
                JOptionPane.showMessageDialog(new Frame(), "Genre ID [" + id + "] has successfully\nbeen added to the NpcGames list.");
                WindowAvailableMods.createFrame();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Error while adding genre with id [" + id + "] to NpcGames.txt.\nnPlease try again with administrator rights.\nException: " + e.getMessage(), "Unable to edit NpcGames.txt", JOptionPane.ERROR_MESSAGE);
                WindowAvailableMods.createFrame();
            }
        }else{
            WindowAvailableMods.createFrame();
        }
    }
    public static String getCompatibleGenresByID(){
        ArrayList<Integer> arrayListGenreIDs = new ArrayList<>();
        StringBuilder compatibleGenresByID = new StringBuilder();
        if(Settings.enableDebugLogging){
            LOGGER.info("arrayListGenreNamesByIdSorted.size: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.size());
        }
        for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.size(); i++){
            if(Settings.enableDebugLogging){
                LOGGER.info("current i: " + i);
            }
            for (String arrayListCompatibleGenre : ARRAY_LIST_COMPATIBLE_GENRES) {
                String number = AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", "");
                if (Settings.enableDebugLogging) {
                    LOGGER.info("current n: " + i);
                    LOGGER.info("current number: " + number);
                    LOGGER.info("arrayListGenreNamesByIdSorted [i]: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replace(" - " + number, ""));
                    LOGGER.info("Does it equal that: " + arrayListCompatibleGenre);
                }
                if (AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replace(" - " + number, "").equals(arrayListCompatibleGenre)) {
                    if (Settings.enableDebugLogging) {
                        LOGGER.info("genreNamesByIdSorted: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i));
                        LOGGER.info("arrayListCompatibleGenres: " + arrayListCompatibleGenre);
                        LOGGER.info("This should only be an id: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", ""));
                    }
                    arrayListGenreIDs.add(Integer.parseInt(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", "")));
                    if(Settings.enableDebugLogging){
                        LOGGER.info("compatible genre id: " + Integer.parseInt(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", "")));
                    }
                }
            }
        }
        Collections.sort(arrayListGenreIDs);
        for (Integer arrayListGenreID : arrayListGenreIDs) {
            compatibleGenresByID.append("<").append(arrayListGenreID).append(">");
        }
        return compatibleGenresByID.toString();
    }
}
