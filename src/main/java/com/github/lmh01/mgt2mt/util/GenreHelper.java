package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGameplayFeatures;
import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingThemes;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GenreHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreHelper.class);

    public static void addRandomizedGenre(){
        try{
            AnalyzeExistingGenres.analyzeGenreFile();
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
            AtomicReference<ArrayList<File>> screenshotFiles = new AtomicReference<>(new ArrayList<>());
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
            AtomicReference<String> iconPath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");

            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField("ENTER GENRE NAME");
            panelName.add(labelName);
            panelName.add(textFieldName);

            JButton buttonAddNameTranslations = new JButton("Add Name Translations");
            buttonAddNameTranslations.setToolTipText("<html>Click to add name translations<br>The value entered in the main text field will be used as the english translation");
            buttonAddNameTranslations.addActionListener(actionEvent -> {
                if(!nameTranslationsAdded.get()){
                    mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                    nameTranslationsAdded.set(true);
                    buttonAddNameTranslations.setText("Name Translations Added");
                }else{
                    if(JOptionPane.showConfirmDialog(null, "Name translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                        nameTranslationsAdded.set(true);
                    }
                }
            });

            JPanel panelDescription = new JPanel();
            JLabel labelDescription = new JLabel("Description:");
            JTextField textFieldDescription = new JTextField("ENTER GENRE DESCRIPTION");
            panelDescription.add(labelDescription);
            panelDescription.add(textFieldDescription);

            JButton buttonAddDescriptionTranslations = new JButton("Add description translations");
            buttonAddDescriptionTranslations.setToolTipText("<html>Click to add description translations<br>The value entered in the main text field will be used as the english translation");
            buttonAddDescriptionTranslations.addActionListener(actionEvent -> {
                if(!descriptionTranslationsAdded.get()){
                    mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                    buttonAddDescriptionTranslations.setText("Description Translations Added");
                    descriptionTranslationsAdded.set(true);
                }else{
                    if(JOptionPane.showConfirmDialog(null, "Description translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                        descriptionTranslationsAdded.set(true);
                    }
                }
            });

            JButton buttonSetIcon = new JButton("Add Genre Icon");
            buttonSetIcon.setToolTipText("Click to select a .png file that should be the genre icon");
            buttonSetIcon.addActionListener(actionEvent -> {
                iconPath.set(getGenreImageFilePath(false, true, null));
                if(!iconPath.equals("error")){
                    buttonSetIcon.setText("Genre Icon Set");
                }else{
                    iconPath.set(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
                    buttonSetIcon.setText("Add Genre Icon");
                }
            });

            JButton buttonSetScreenshots = new JButton("Add Screenshots");
            buttonSetScreenshots.setToolTipText("Click to select .png images that should be used as game screenshots");
            buttonSetScreenshots.addActionListener(actionEvent -> {
                setGenreScreenshots(screenshotFiles, buttonSetScreenshots);
            });

            JCheckBox checkBoxShowSummary = new JCheckBox("Show Genre Summary");
            checkBoxShowSummary.setToolTipText("Check to show the genre summary");

            Object[] params = {panelName, buttonAddNameTranslations, panelDescription, buttonAddDescriptionTranslations, buttonSetIcon, buttonSetScreenshots, checkBoxShowSummary};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, "Add Random Genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    Map<String, String> map = new HashMap<>();
                    for(Map.Entry<String, String> entry : mapNameTranslations[0].entrySet()){
                        map.put("NAME " + entry.getKey(), entry.getValue());
                    }
                    for(Map.Entry<String, String> entry : mapDescriptionTranslations[0].entrySet()){
                        map.put("DESC " + entry.getKey(), entry.getValue());
                    }

                    //Randomized value allocation
                    String unlockMonth = convertMonthNumberToMonthName(Utils.getRandomNumber(1, 12));
                    int unlockYear = Utils.getRandomNumber(1976, 2010);
                    map.put("ID", Integer.toString(AnalyzeExistingGenres.getFreeGenreID()));
                    String genreName = textFieldName.getText();
                    String genreDescription = textFieldDescription.getText();
                    if(textFieldName.getText().equals("ENTER GENRE NAME") || textFieldName.getText().isEmpty()){
                        genreName = "Randomized Genre";
                    }
                    if(textFieldDescription.getText().equals("ENTER GENRE DESCRIPTION") || textFieldDescription.getText().isEmpty()){
                        genreDescription = "Genre created by the MGT2 Mod Tool using random values";
                    }
                    if(map.containsKey("NAME EN")){
                        map.replace("NAME EN", genreName);
                    }else{
                        map.put("NAME EN", genreName);
                    }
                    if(map.containsKey("DESC EN")){
                        map.replace("DESC EN", genreDescription);
                    }else{
                        map.put("DESC EN", genreDescription);
                    }
                    map.put("DATE", unlockMonth + " " + unlockYear);
                    int researchPoints = Utils.roundToNextHundred(Utils.getRandomNumber(0, 1500));
                    int price = Utils.roundToNextThousand(Utils.getRandomNumber(0, 300000));
                    int developmentCost = Utils.roundToNextThousand(Utils.getRandomNumber(0, 10000));
                    map.put("RES POINTS", Integer.toString(researchPoints));
                    map.put("PRICE", Integer.toString(price));
                    map.put("DEV COSTS", Integer.toString(developmentCost));
                    map.put("TGROUP", getRandomTargetGroup());
                    Integer[] workPriority = getRandomWorkPriorityValues();
                    map.put("GAMEPLAY", Integer.toString(workPriority[0]));
                    map.put("GRAPHIC", Integer.toString(workPriority[1]));
                    map.put("SOUND", Integer.toString(workPriority[2]));
                    map.put("CONTROL", Integer.toString(workPriority[3]));
                    map.put("GENRE COMB", getRandomGenreCombs());
                    map.put("DESIGN1", Integer.toString(Utils.getRandomNumber(1, 10)));
                    map.put("DESIGN2", Integer.toString(Utils.getRandomNumber(1, 10)));
                    map.put("DESIGN3", Integer.toString(Utils.getRandomNumber(1, 10)));
                    map.put("DESIGN4", Integer.toString(Utils.getRandomNumber(1, 10)));
                    map.put("DESIGN5", Integer.toString(Utils.getRandomNumber(1, 10)));
                    HashSet<Integer> compatibleThemeIds = getRandomThemeIds();
                    map.put("THEME COMB", getCompatibleThemes(compatibleThemeIds));
                    List<HashSet<Integer>> gameplayFeatures = getRandomGameplayFeatureIds();
                    setGameplayFeatureCompatibility(map, gameplayFeatures.get(0), gameplayFeatures.get(1));
                    File iconFile = new File(iconPath.toString());
                    GenreManager.addGenre(map, map, compatibleThemeIds, gameplayFeatures.get(0), gameplayFeatures.get(1), screenshotFiles.get(),true, iconFile,  checkBoxShowSummary.isSelected());
                    JOptionPane.showMessageDialog(null, "Genre [" + genreName + "] has been added successfully!", "Genre added", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }else{
                    break;
                }
            }
        }catch(IOException e){

        }
    }


    public static String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog, JTextField textFieldImagePath) {
        if(useTextFiledPath){
            String textFieldPath = textFieldImagePath.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    if(showDialog){
                        JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                    }
                    return textFieldPath;
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "The entered image file does not exist.\nPlease select a valid file.", "File not found", JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");
                return "error";
            }
        }else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return ".png files";
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle("Choose a genre image (.png):");

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    if(fileChooser.getSelectedFile().getName().contains(".png")){
                        JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return "error";
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                return "error";
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }

    /**
     * Sets the genreScreenshots ArrayList a new array list containing the selected files. Uses {@link GenreHelper#getGenreScreenshots()} to get the Array List.
     * @param genreScreenshots The array list that should be set
     * @param button The button of which the text should be changed
     */
    public static void setGenreScreenshots(AtomicReference<ArrayList<File>> genreScreenshots, JButton button){
        while(true){
            genreScreenshots.set(getGenreScreenshots());
            if(!genreScreenshots.get().isEmpty()){
                StringBuilder filePaths = new StringBuilder();
                for (File arrayListScreenshotFile : genreScreenshots.get()) {
                    filePaths.append("<br>").append(arrayListScreenshotFile);
                }
                if(JOptionPane.showConfirmDialog(null, "<html>The following image files have been added:<br>" + filePaths + "<br><br>Is this correct and do you want to continue?", "Is this correct?", JOptionPane.YES_NO_OPTION) == 0){
                    button.setText("Screenshots Set");
                    break;
                }
            }else{
                button.setText("Add Screenshots");
                break;
            }
        }
    }

    /**
     * Opens a ui where the user can select image files.
     * @return Returns the image files as ArrayList
     */
    private static ArrayList<File> getGenreScreenshots(){
        ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
        ArrayList<File> arrayListScreenshotFilesSelected = new ArrayList<>();
        JTextField textFieldScreenshotFile = new JTextField();
        JLabel labelMessage = new JLabel("<html>Click browse or enter enter the image path manually." +
                "<br>When the image path is set click okay." +
                "<br>This will add the screenshot to a list of screenshots that will be shown in the development progress page." +
                "<br>Note: The image file as to be a `.png` file and the aspect ratio should be 4:3");
        JButton buttonBrowse = new JButton("Browse");
        AtomicBoolean multipleFilesSelected = new AtomicBoolean(false);
        AtomicInteger numberOfScreenshotsToAdd = new AtomicInteger();
        buttonBrowse.addActionListener(actionEventSmall ->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return ".png files";
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle("Choose a genre image (.png):");
                fileChooser.setMultiSelectionEnabled(true);

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    final int NUMBER_OF_SCREENSHOTS = fileChooser.getSelectedFiles().length;
                    numberOfScreenshotsToAdd.set(NUMBER_OF_SCREENSHOTS);
                    File[] screenshots = fileChooser.getSelectedFiles();
                    if(NUMBER_OF_SCREENSHOTS > 1){
                        multipleFilesSelected.set(true);
                    }
                    boolean failed = false;
                    for(int i=0; i<NUMBER_OF_SCREENSHOTS; i++){
                        if(!failed){
                            if(screenshots[i].getName().contains(".png")){
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                                if(multipleFilesSelected.get()){
                                    arrayListScreenshotFilesSelected.add(screenshots[i]);
                                    textFieldScreenshotFile.setText("Multiple files selected");
                                }else{
                                    textFieldScreenshotFile.setText(fileChooser.getSelectedFile().getPath());
                                }
                            }else{
                                JOptionPane.showMessageDialog(new Frame(), "Please select only .png files.");
                                failed = true;
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                            }
                        }
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
        Object[] params = {labelMessage,textFieldScreenshotFile, buttonBrowse};
        if(JOptionPane.showConfirmDialog(null, params, "Add a screenshot", JOptionPane.OK_CANCEL_OPTION) == 0){
            String textFieldPath = textFieldScreenshotFile.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    arrayListScreenshotFiles.add(new File(textFieldPath));
                    JOptionPane.showMessageDialog(new Frame(), "Image file has been added.");
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "The entered image file does not exist.\nPlease select a valid file.", "File not found", JOptionPane.ERROR_MESSAGE);
                }
            }else if(multipleFilesSelected.get()){
                for(int i = 0; i< numberOfScreenshotsToAdd.get(); i++){
                    arrayListScreenshotFiles.add(arrayListScreenshotFilesSelected.get(i));
                }
                JOptionPane.showMessageDialog(new Frame(), "Image files have been added.");
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");

            }
            return arrayListScreenshotFiles;
        }
        return arrayListScreenshotFiles;
    }

    private static String convertMonthNumberToMonthName(int monthNumber){
        switch(monthNumber){
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
        }
        return "JAN";
    }

    private static String getRandomTargetGroup(){
        StringBuilder stringBuilder = new StringBuilder();
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<KID>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<TEEN>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<ADULT>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<OLD>");
        }
        return stringBuilder.toString();
    }

    private static Integer[] getRandomWorkPriorityValues(){
        int sum = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int numberOfTries = 1;
        while(sum !=100){
            a = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            b = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            c = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            d = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            sum = a+b+c+d;
            numberOfTries++;
        }
        LOGGER.info("Number of tries: " + numberOfTries);
        return new Integer[]{a, b, c, d};
    }

    private static String getRandomGenreCombs(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer integer : AnalyzeExistingGenres.getGenreIdsInUse()){
            if(Utils.getRandomNumber(1,10) > 5){
                stringBuilder.append("<").append(integer).append(">");
            }
        }
        return stringBuilder.toString();
    }

    public static HashSet<Integer> getRandomThemeIds(){
        HashSet<Integer> hashSet = new HashSet<>();
        for(Integer integer : AnalyzeExistingThemes.getThemeIdsInUse()){
            if(Utils.getRandomNumber(1,100) > 30){
                hashSet.add(integer);
            }
        }
        return hashSet;
    }

    /**
     * @Returns Returns a list containing the bad gameplay features in the first HashSet and the good gameplay features in the second HashSet
     */
    private static List<HashSet<Integer>> getRandomGameplayFeatureIds(){
        HashSet<Integer> hashSetBadGameplayFeatures = new HashSet<>();
        HashSet<Integer> hashSetGoodGameplayFeatures = new HashSet<>();
        for(String string : AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet()){
            if(Utils.getRandomNumber(1,100) > 80){
                hashSetBadGameplayFeatures.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
            }
            if(Utils.getRandomNumber(1,100) > 80){
                hashSetGoodGameplayFeatures.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(string));
            }
        }
        Collections.disjoint(hashSetBadGameplayFeatures, hashSetGoodGameplayFeatures);
        hashSetBadGameplayFeatures.removeAll(hashSetGoodGameplayFeatures);
        List<HashSet<Integer>> list = new ArrayList<>();
        list.add(hashSetBadGameplayFeatures);
        list.add(hashSetGoodGameplayFeatures);
        return list;
    }

    private static String getCompatibleThemes(HashSet<Integer> themeIds){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            if(themeIds.contains(entry.getKey())){
                stringBuilder.append("<").append(entry.getValue()).append(">");
            }
        }
        return stringBuilder.toString();
    }

    private static void setGameplayFeatureCompatibility(Map<String, String> map1, HashSet<Integer> badGameplayFeatures, HashSet<Integer> goodGameplayFeatures){
        StringBuilder gameplayFeaturesBad = new StringBuilder();
        StringBuilder gameplayFeaturesGood = new StringBuilder();
        for(Map<String, String> map : AnalyzeExistingGameplayFeatures.gameplayFeatures){
            for(Map.Entry<String, String> entry : map.entrySet()){
                for(Integer integer : badGameplayFeatures){
                    if(entry.getKey().equals("ID")){
                        if(entry.getValue().equals(Integer.toString(integer))){
                            gameplayFeaturesBad.append("<").append(AnalyzeExistingGameplayFeatures.getGameplayFeatureNameById(integer)).append(">");
                            if(Settings.enableDebugLogging){
                                LOGGER.info("Gameplay feature bad: " + entry.getKey() + " | " + entry.getValue());
                            }
                        }
                    }
                }
                for(Integer integer : goodGameplayFeatures){
                    if(entry.getKey().equals("ID")){
                        if(entry.getValue().equals(Integer.toString(integer))){
                            gameplayFeaturesGood.append("<").append(AnalyzeExistingGameplayFeatures.getGameplayFeatureNameById(integer)).append(">");
                            if(Settings.enableDebugLogging){
                                LOGGER.info("Gameplay feature good: " + entry.getKey() + " | " + entry.getValue());
                            }
                        }
                    }
                }
            }
        }
        map1.put("GAMEPLAYFEATURE BAD", gameplayFeaturesBad.toString());
        map1.put("GAMEPLAYFEATURE GOOD", gameplayFeaturesGood.toString());
    }
}
