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

    /**
     * Adds a new genre to mad games tycoon 2
     */
    public static void startStepByStepGuide() throws IOException {
        AnalyzeExistingGenres.analyzeGenreFile();
        resetVariables();
        try {
            Backup.createBackup(Utils.getGenreFile());
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
        mapNewGenre.put("ID", Integer.toString(AnalyzeExistingGenres.getFreeGenreID()));
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
        String messageBody = "Your genre is ready:\n\n" +
                "Id:" + map.get("ID") + "\n" +
                "Name: " + map.get("NAME EN") + "\n" +
                "Description: " + map.get("DESC EN") + "\n" +
                "Unlock date: " + map.get("DATE") + "\n" +
                "Research point cost: " + map.get("RES POINTS") + "\n" +
                "Research cost " + map.get("PRICE") + "\n" +
                "Development cost: " + map.get("DEV COSTS") + "\n" +
                "Pic: see top left\n" +
                "Target group: " + getTargetGroups(map) + "\n" +
                "\n*Compatible genres*\n\n" + getCompatibleGenres(map) + "\n" +
                "\n*Compatible themes*\n\n" + getMapEntryToDisplay(map, "THEME COMB", 15) + "\n" +
                "\n*Good gameplay features*\n\n" + getMapEntryToDisplay(map, "GAMEPLAYFEATURE GOOD", 10) + "\n" +
                "\n*Bad gameplay features*\n\n" + getMapEntryToDisplay(map, "GAMEPLAYFEATURE BAD", 10) + "\n" +
                "\n*Design focus*\n\n" +
                "Game length: " + map.get("FOCUS0") + "\n" +
                "Game depth: " + map.get("FOCUS1") + "\n" +
                "Beginner friendliness: " + map.get("FOCUS2") + "\n" +
                "Innovation: " + map.get("FOCUS3") + "\n" +
                "Story: " + map.get("FOCUS4") + "\n" +
                "Character design: " + map.get("FOCUS5") + "\n" +
                "Level design: " + map.get("FOCUS6") + "\n" +
                "Mission design: " + map.get("FOCUS7") + "\n" +
                "\n*Design direction*\n\n" +
                "Core gamers/Casual gamers: " + map.get("ALIGN0") + "\n" +
                "Nonviolent/Extreme violence: " + map.get("ALIGN1") + "\n" +
                "Easy/Hard: " + map.get("ALIGN2") + "\n" +
                "\n*Work priority*\n\n" +
                "Gameplay: " + map.get("GAMEPLAY") + "%\n" +
                "Graphic: " + map.get("GRAPHIC") + "%\n" +
                "Sound: " + map.get("SOUND") + "%\n" +
                "Control: " + map.get("CONTROL") + "%\n";
        int returnValue;
        if(showSummaryFromImport){
            if(showMessages){
                String messageBodyButtonExplanation = "\nClick yes to add this genre.\nClick no cancel this operation.";
                returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, "Add this genre?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
            }else{
                returnValue = 0;
            }
        }else{
            String messageBodyButtonExplanation = "\nClick yes to add this genre.\nClick no to return to the step by step guide.\nClick cancel to quit this guide.";
            returnValue = JOptionPane.showConfirmDialog(null, messageBody + messageBodyButtonExplanation, "Add this genre?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon);
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
                if(JOptionPane.showConfirmDialog(null, "Error while processing image files.\nDo you wan't to add you new genre anyway?", "Continue anyway?", JOptionPane.YES_NO_OPTION) == 0){
                    //click yes
                    continueAnyway = true;
                }
            }
            if(continueAnyway | imageFileAccessedSuccess){
                try {
                    EditGenreFile.addGenre(map, genreTranslations);
                    EditThemeFiles.editGenreAllocation(Integer.parseInt(map.get("ID")), true, compatibleThemeIds);
                    EditGameplayFeaturesFile.addGenreId(gameplayFeaturesGoodIds, Integer.parseInt(map.get("ID")), true);
                    AnalyzeExistingGameplayFeatures.analyzeGameplayFeatures();
                    EditGameplayFeaturesFile.addGenreId(gameplayFeaturesBadIds, Integer.parseInt(map.get("ID")), false);
                    GenreManager.genreAdded(map, showSummaryFromImport, genreIcon, showMessages);
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
                WindowAddGenrePage11.createFrame();
            }
        }
        WindowMain.checkActionAvailability();
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
                targetGroups = targetGroups + "Kid, ";
            }else{
                targetGroups = targetGroups + "Kid";
            }
        }
        if(map.get("TGROUP").contains("true")){
            if(map.get("TGROUP").contains("ADULT") || map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + "Teen, ";
            }else{
                targetGroups = targetGroups + "Teen";
            }
        }
        if(map.get("TGROUP").contains("ADULT")){
            if(map.get("TGROUP").contains("OLD")){
                targetGroups = targetGroups + "Adult, ";
            }else{
                targetGroups = targetGroups + "Adult";
            }
        }
        if(map.get("TGROUP").contains("OLD")){
            targetGroups = targetGroups + "Senior";
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
                outputGenres.add(AnalyzeExistingGenres.getGenreNameById(Integer.parseInt(currentGenre.toString())));
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
     * @param showSummaryFromImport determines what log information to write to the log file.
     * @param genreIcon The genre icon.
     */
    public static void genreAdded(Map<String, String> map, boolean showSummaryFromImport, File genreIcon, boolean showMessages) throws IOException {
        String name = map.get("NAME EN");
        int id = AnalyzeExistingGenres.getFreeGenreID();
        if(showSummaryFromImport){
            ChangeLog.addLogEntry(1, name);
        }else{
            ChangeLog.addLogEntry(18,  name);
        }
        ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(genreIcon.getPath()));
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, "Your new genre [" + name + "] has been added successfully.\nDo you wan't to edit the NPC_Games list to include your new genre?\nNote: this can be undone with the feature [Add genre to NPC_Games].", "Genre added successfully!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == 0){
                try {
                    NPCGameListChanger.editNPCGames(id, true, 20);
                    JOptionPane.showMessageDialog(new Frame(), "Genre ID [" + id + "] has successfully\nbeen added to the NpcGames list.");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), "Error while adding genre with id [" + id + "] to NpcGames.txt.\nnPlease try again with administrator rights.\nException: " + e.getMessage(), "Unable to edit NpcGames.txt", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else{
            NPCGameListChanger.editNPCGames(id,true, 20);
        }
        WindowMain.checkActionAvailability();
    }
}
