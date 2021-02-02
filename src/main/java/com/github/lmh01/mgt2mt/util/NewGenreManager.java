package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.windows.MainWindow;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NewGenreManager {
    public static int id = 18;
    public static String name = "";
    public static String description = "";
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
    private static final Logger LOGGER = LoggerFactory.getLogger(NewGenreManager.class);
    public static final String FORBIDDEN_GENRE_NAMES = "[ID], [NAME EN], [NAME GE], [NAME CH], [NAME TU], [NAME FR], [DESC EN], [DESC GE], [DESC CH], [DESC TU], [DESC FR], [NAME PB], [DESC PB], [NAME HU], [DESC HU], [NAME CT], [DESC CT], [NAME ES], [DESC ES], [NAME PL], [DESC PL], [DATE], [RES POINTS], [PRICE], [DEV COSTS], [PIC], [TGROUP], [GAMEPLAY], [GRAPHIC], [SOUND], [CONTROL], [GENRE COMB], [DESIGN1], [DESIGN2], [DESIGN3], [DESIGN4], [DESIGN5]";


    public static void addGenre(){
        //TODO The user should have the option to select a checkbox "Don't show me again" to not show this dialog again. If dialog has been opened will be saved to the settings file
        if(JOptionPane.showConfirmDialog(null, "Warning:\n\nLoading a save-file with this new added genre will tie it to the file.\nRemoving the genre later won't remove it from save-files that have been accessed with said genre.\n\nAdd new genre?", "Add genre?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0){
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
        }
    }
    public static void showSummary(){
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
                "\n*Design Priority*\n\n" +
                "Gameplay/Visuals: " + design1 + "\n" +
                "Story/Game length: " + design2 + "\n" +
                "Atmosphere/Content: " + design3 + "\n" +
                "Game depth/Beginner-friendly: " + design4 + "\n" +
                "Core Gamers/Casual Gamer: " + design5 + "\n" +
                "\n*Compatible genres*\n\n" + compatibleGenres + "\n" +
                "\n*WorkPriority*\n\n" +
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
                ImageFileHandler.moveImage(imageFile);
                imageFileAccessedSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                if(JOptionPane.showConfirmDialog(null, "Error while processing image files.\nDo you wan't to add you new genre anyway?", "Continue anyway?", JOptionPane.YES_NO_OPTION) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway || imageFileAccessedSuccess){
                try {
                    EditGenreFile.addGenre();
                    NewGenreManager.genreAdded();
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "The genre was not added:\nError while editing Genres.txt\nPlease try again with administrator rights.", "Unable to edit Genres.txt", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION){
            //Click no or close window
            WindowAddGenrePage8.createFrame();
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

    public static void resetVariablesToDefault(){
        LOGGER.info("resetting genre variables...");
        id = AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size();
        name = "";
        description = "";
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
        LOGGER.info("arrayListGenreNamesByIdSorted.size: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.size());
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
                    LOGGER.info("compatible genre id: " + Integer.parseInt(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", "")));
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
