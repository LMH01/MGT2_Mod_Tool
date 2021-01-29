package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.*;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import com.github.lmh01.mgt2mt.windows.genre.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

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
    private static Logger logger = LoggerFactory.getLogger(NewGenreManager.class);


    public static void addGenre(){
        if(JOptionPane.showConfirmDialog((Component)null, "Warning:\nLoading a save-file with this new added genre will tie it to the file.\nRemoving the genre later won't remove it from save-files that have been accessed with said genre.\nUsing this feature will unfortunately remove all other translations.\nAdd new genre?", "Add genre", 0) == 0){
            Backup.createBackup(false);
            resetVariablesToDefault();
            logger.info("Adding new genre");
            openStepWindow(1);
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
            case 9: WindowAddGenrePage9.createFrame(); break;
        }
    }
    public static void showSummary(){
        //TODO Write summary
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
        String compatibleGenres = "";
        for(int i=0; i<arrayListCompatibleGenres.size(); i++){
            if(i==arrayListCompatibleGenres.size()-1){
                compatibleGenres = compatibleGenres + arrayListCompatibleGenres.get(i);
            }else{
                compatibleGenres = compatibleGenres + arrayListCompatibleGenres.get(i) + ", ";
            }
        }
        ImageIcon iconGenre = new ImageIcon(imageFile.getPath());
        if(JOptionPane.showConfirmDialog((Component)null, "Your genre is ready:\n" +
                "Id:" + id + "\n" +
                "Name: " + name + "\n" +
                "Description:" + description + "\n" +
                "Unlock date: " + unlockMonth + " " + unlockYear + "\n" +
                "Research point cost: " + researchPoints + "\n" +
                "Research cost " + price + "\n" +
                "Development cost: " + devCost + "\n" +
                "Pic: see top left\n" +
                "Target group: " + targetGroups + "\n" +
                "\nDesign Priority:" +
                "Gameplay/Visuals: " + design1 + "\n" +
                "Story/Game length: " + design2 + "\n" +
                "Atmosphere/Content: " + design3 + "\n" +
                "Game depth/Beginner-friendly: " + design4 + "\n" +
                "Core Gamers/Casual Gamer: " + design5 + "\n" +
                "\nCompatible genres: " + compatibleGenres + "\n" +
                "\nWorkPriority:" +
                "Gameplay: " + gameplay + "%\n" +
                "Graphic: " + graphic + "%\n" +
                "Sound: " + sound + "%\n" +
                "Control: " + control + "%\n" +
                "\nClick yes to add this genre.\nClick no if you wan't to make changes\nand return to the step by step guide.", "Add this genre?", 0, JOptionPane.QUESTION_MESSAGE, iconGenre) == 0)
        {
            ImageFileHandler.moveImage(imageFile);
            EditGenreFile.addGenre();
        }else{
            WindowAddGenrePage9.createFrame();
        }
    }
    public static void resetVariablesToDefault(){
        logger.info("resetting genre variables...");
        language = "English";
        id = AnalyzeExistingGenres.genreIDsInUse.size();
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
    }
    public static void genreAdded(){
        ChangeLog.addLogEntry(1, name);
        ImageIcon iconGenre = new ImageIcon(NewGenreManager.imageFile.getPath());
        if(JOptionPane.showConfirmDialog((Component)null, "Your new genre [" + name + "] has been added successfully.\nDo you wan't to edit the NPC_Games list to include your new genre?\nNote: this can be undone with the feature [Add genre to NPC_Games].", "Genre added successfully!", 0, JOptionPane.QUESTION_MESSAGE, iconGenre) == 0){
            NPCGameListChanger.editNPCGameList(id, "add", 20);
        }
    }
    public static String getLanguageAbbreviation(){
        if(language.equals("English")){
            return "EN";
        }else if(language.equals("Deutsch")){
            return "GE";
        }
        return "";
    }
    public static String getCompatibleGenresByID(){
        ArrayList<Integer> arrayListGenreIDs = new ArrayList<>();
        String compatibleGenresByID = "";
        for(int i = 0; i<AnalyzeExistingGenres.genreNamesByIdSorted.size(); i++){
            for(int n = 0; n<arrayListCompatibleGenres.size(); n++){
                if(AnalyzeExistingGenres.genreNamesByIdSorted.get(i).contains(arrayListCompatibleGenres.get(n))){
                    logger.info("genreNamesByIdSorted: " + AnalyzeExistingGenres.genreNamesByIdSorted.get(i));
                    logger.info("arrayListCompatibleGenres: " + arrayListCompatibleGenres.get(n));
                    logger.info("This should only be an id: " + AnalyzeExistingGenres.genreNamesByIdSorted.get(i).replaceAll("[^0-9]",""));
                    arrayListGenreIDs.add(Integer.parseInt(AnalyzeExistingGenres.genreNamesByIdSorted.get(i).replaceAll("[^0-9]","")));
                    logger.info("compatible genre ids: " + Integer.parseInt(AnalyzeExistingGenres.genreNamesByIdSorted.get(i).replaceAll("[^0-9]","")));
                }
            }
        }
        for(int n = 0; n<arrayListGenreIDs.size(); n++){
            compatibleGenresByID = compatibleGenresByID + "<" + arrayListGenreIDs.get(n) + ">";
        }
        return compatibleGenresByID;
    }
}
