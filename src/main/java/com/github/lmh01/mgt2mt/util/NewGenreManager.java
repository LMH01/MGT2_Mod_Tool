package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
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
    public static String language = "English";
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
    public static ArrayList<String> arrayListCompatibleGenres = new ArrayList<>();
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
    private static Logger logger = LoggerFactory.getLogger(NewGenreManager.class);


    public static void addGenre(){
        if(JOptionPane.showConfirmDialog((Component)null, "Warning:\n\nLoading a save-file with this new added genre will tie it to the file.\nRemoving the genre later won't remove it from save-files that have been accessed with said genre.\n\nAdd new genre?", "Add genre?", 0, JOptionPane.QUESTION_MESSAGE) == 0){
            try {
                Backup.createBackup(Utils.fileGenres);
                resetVariablesToDefault();
                logger.info("Adding new genre");
                openStepWindow(1);
            } catch (IOException e) {
                if(Utils.showConfirmDialog(1, e)){
                    resetVariablesToDefault();
                    logger.info("Adding new genre");
                    openStepWindow(1);
                }else{
                    WindowAddNewGenre.createFrame();
                }
                e.printStackTrace();
            }

        }else{
            WindowAddNewGenre.createFrame();
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
        String targetGroups = "";
        if(targetGroupKid){//TODO Move this to own little function
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
        String compatibleGenres = "";
        for(int i=0; i<arrayListCompatibleGenres.size(); i++){
            if(i==arrayListCompatibleGenres.size()-1){
                compatibleGenres = compatibleGenres + arrayListCompatibleGenres.get(i);
            }else{
                compatibleGenres = compatibleGenres + arrayListCompatibleGenres.get(i) + ", ";
            }
        }
        ImageIcon iconGenre = new ImageIcon(imageFile.getPath());
        if(JOptionPane.showConfirmDialog(null, "Your genre is ready:\n" +
                "Id:" + id + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Unlock date: " + unlockMonth + " " + unlockYear + "\n" +
                "Research point cost: " + researchPoints + "\n" +
                "Research cost " + price + "\n" +
                "Development cost: " + devCost + "\n" +
                "Pic: see top left\n" +
                "Target group: " + targetGroups + "\n" +
                "\nDesign Priority:\n" +
                "Gameplay/Visuals: " + design1 + "\n" +
                "Story/Game length: " + design2 + "\n" +
                "Atmosphere/Content: " + design3 + "\n" +
                "Game depth/Beginner-friendly: " + design4 + "\n" +
                "Core Gamers/Casual Gamer: " + design5 + "\n" +
                "\nCompatible genres: " + compatibleGenres + "\n" +
                "\nWorkPriority:\n" +
                "Gameplay: " + gameplay + "%\n" +
                "Graphic: " + graphic + "%\n" +
                "Sound: " + sound + "%\n" +
                "Control: " + control + "%\n" +
                "\nClick yes to add this genre.\nClick no if you wan't to make changes\nand return to the step by step guide.", "Add this genre?", 0, JOptionPane.QUESTION_MESSAGE, iconGenre) == 0)
        {
            //click yes
            boolean continueAnyway = false;
            boolean imageFileAccessedSuccess = false;
            try {
                ImageFileHandler.moveImage(imageFile);
                imageFileAccessedSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                if(JOptionPane.showConfirmDialog(null, "Error while processing image files.\nDo you wan't to add you new genre anyway?", "Continue anyway?", JOptionPane.ERROR_MESSAGE) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway || imageFileAccessedSuccess){
                try {
                    EditGenreFile.addGenre();
                    NewGenreManager.genreAdded();
                    WindowAddNewGenre.createFrame();
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "The genre was not added:\nError while editing Genres.txt\nPlease try again with administrator rights.", "Unable to edit Genres.txt", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else{
            //click no
            WindowAddGenrePage8.createFrame();
        }
    }
    public static void resetVariablesToDefault(){
        logger.info("resetting genre variables...");
        language = "English";
        id = AnalyzeExistingGenres.arrayListGenreIDsInUse.size();
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
        arrayListCompatibleGenres.clear();
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
        ImageIcon iconGenre = new ImageIcon(NewGenreManager.imageFile.getPath());
        if(JOptionPane.showConfirmDialog((Component)null, "Your new genre [" + name + "] has been added successfully.\nDo you wan't to edit the NPC_Games list to include your new genre?\nNote: this can be undone with the feature [Add genre to NPC_Games].", "Genre added successfully!", 0, JOptionPane.QUESTION_MESSAGE, iconGenre) == 0){
            try {
                NPCGameListChanger.editNPCGames(id, true, 20);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new Frame(), "Error while adding genre with id [" + id + "] to NpcGames.txt.\nnPlease try again with administrator rights.\nException: " + e.getMessage(), "Unable to edit NpcGames.txt", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    public static String getCompatibleGenresByID(){
        ArrayList<Integer> arrayListGenreIDs = new ArrayList<>();
        String compatibleGenresByID = "";
        logger.info("arrayListGenreNamesByIdSorted.size: " + AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.size());
        for(int i = 0; i<AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.size(); i++){
            if(Settings.enableDebugLogging){
                logger.info("current i: " + i);
            }
            for(int n = 0; n<arrayListCompatibleGenres.size(); n++){
                String number = AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replaceAll("[^0-9]","");
                if(Settings.enableDebugLogging){
                    logger.info("current n: " + i);
                    logger.info("current number: " + number);
                    logger.info("arrayListGenreNamesByIdSorted [i]: " + AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replace(" - " + number,""));
                    logger.info("Does it equal that: " + arrayListCompatibleGenres.get(n));
                }
                if(AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replace(" - " + number,"").equals(arrayListCompatibleGenres.get(n))){
                    if(Settings.enableDebugLogging){
                        logger.info("genreNamesByIdSorted: " + AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i));
                        logger.info("arrayListCompatibleGenres: " + arrayListCompatibleGenres.get(n));
                        logger.info("This should only be an id: " + AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replaceAll("[^0-9]",""));
                    }
                    arrayListGenreIDs.add(Integer.parseInt(AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replaceAll("[^0-9]","")));
                    logger.info("compatible genre id: " + Integer.parseInt(AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i).replaceAll("[^0-9]","")));
                }
            }
        }
        Collections.sort(arrayListGenreIDs);
        for(int n = 0; n<arrayListGenreIDs.size(); n++){
            compatibleGenresByID = compatibleGenresByID + "<" + arrayListGenreIDs.get(n) + ">";
        }
        return compatibleGenresByID;
    }
}
