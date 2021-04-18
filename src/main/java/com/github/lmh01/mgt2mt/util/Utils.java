package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    //These are the files inside the mgt2 file structure that are used inside this tool.
    public static final String GITHUB_URL = "https://github.com/LMH01/MGT2_Mod_Tool";
    public static final String MORE_MODS_URL = "https://github.com/LMH01/MGT2_Mod_Tool/discussions/34";
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * @return returns the current date time in format: YYYY-MM-DD-HH-MM-SS
     */
    public static String getCurrentDateTime(){
        return LocalDateTime.now().getYear() + "-" +
                LocalDateTime.now().getMonth() + "-"+
                LocalDateTime.now().getDayOfMonth() + "-" +
                LocalDateTime.now().getHour() + "-" +
                LocalDateTime.now().getMinute() + "-" +
                LocalDateTime.now().getSecond();
    }
    /**
     * @return Returns the path to \Mad Games Tycoon 2_Data\Extern\Text\DATA\
     */
    public static String getMGT2DataPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\";
    }

    public static String getMGT2TextFolderPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\";
    }

    public static String getMGT2ScreenshotsPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Screenshots\\";
    }

    public static String getMGT2GenreIconsPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\";
    }

    public static String getMGT2CompanyLogosPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\";
    }

    public static String getMGT2ModToolExportFolder(){
        return Settings.MGT2_MOD_MANAGER_PATH + "//Export//";
    }

    public static String getMGT2ModToolModRestorePointFolder() {return Settings.MGT2_MOD_MANAGER_PATH + "//Mod_Restore_Point//Current_Restore_Point//";}

    public static String getMGT2ModToolModRestorePointStorageFolder() {return Settings.MGT2_MOD_MANAGER_PATH + "//Mod_Restore_Point//Old_Restore_Points//";}

    /**
     * @return Returns the genre file inside the mgt2 folder.
     */
    public static File getGenreFile(){
        return new File(getMGT2DataPath() + "\\Genres.txt");
    }

    /**
     * @return Returns the publisher.txt file.
     */
    public static File getPublisherFile(){
        return new File(getMGT2DataPath() + "\\Publisher.txt");
    }

    /**
     * @return Returns the genre file inside the mgt2 folder.
     */
    public static File getNpcGamesFile(){
        return new File(getMGT2DataPath() + "\\NpcGames.txt");
    }

    /**
     * @return Returns the Themes_GE.txt file.
     */
    public static File getThemesGeFile(){return new File(getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt");}

    /**
     * @return Returns the Themes_EN.txt file.
     */
    public static File getThemesEnFile(){return new File(getMGT2TextFolderPath() + "\\EN\\Themes_EN.txt");}

    public static File getGameplayFeaturesFile(){return new File(getMGT2DataPath() + "//GameplayFeatures.txt");}

    public static File getPlatformsFile(){return new File(getMGT2DataPath() + "Platforms.txt");}

    /**
     * @return Returns the EngineFeatures.txt file.
     */
    public static File getEngineFeaturesFile(){return new File(getMGT2DataPath() + "//EngineFeatures.txt");}
    /**
     * @param saveGameNumber The save game number
     * @return Returns the save game file for the input save game number
     */
    public static File getSaveGameFile(int saveGameNumber){
        return new File(Backup.FILE_SAVE_GAME_FOLDER + "//" + "savegame" + saveGameNumber + ".txt");
    }

    /**
     * @return Returns the path to this folder \Mad Games Tycoon 2_Data\Extern\CompanyLogos\.
     */
    public static String getCompanyLogosPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\";
    }

    /**
     * @param s The input String
     * @return Returns the input String without UTF8BOM
     */
    public static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        return s;
    }

    /**
     * Opens a message dialog with a specified error message.
     * @param errorMessageKey the error message key. see this functions for meanings
     * @param e The exception
     */
    public static void showErrorMessage(int errorMessageKey, Exception e){
        switch(errorMessageKey){
            // 1 = Used when AnalyzeExistingGenres.analyzeGenreFile() or AnalyzeThemes.analyzeThemesFile() an exception.
            case 1: JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("errorMessages.gameFilesNotAnalyzed") + "\n\nException:\n" + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE); break;
            // 2 = When it is unsuccessful to open the github repository.
            case 2: JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("errorMessages.unableToOpenWebPage") + "\n\nException:\n" + e.getMessage(), "Can't open page", JOptionPane.ERROR_MESSAGE); break;
        }
    }

    /**
     * Opens a message dialog with a specified error message.
     * @param confirmMessageKey The confirm message key. See this functions for meanings.
     * @param e The exception
     * @return Returns true when the user clicks yes. Returns false when the user clicks no.
     */
    public static boolean showConfirmDialog(int confirmMessageKey, Exception e){
        if(confirmMessageKey == 1){
            return JOptionPane.showConfirmDialog(null, "The backup could not be created.\n\nException:\n" + e.getMessage() + "\nDo you want to continue anyway?", "Unable to backup file", JOptionPane.YES_NO_OPTION) == 0;
        }
        return true;
    }

    /**
     * Opens a confirm dialog with a specified message.
     * @param confirmMessageKey The confirm message key. See this function for meanings.
     * @return Returns true when the user clicks yes. Returns false when the user clicks no.
     */
    public static boolean showConfirmDialog(int confirmMessageKey){
        if(confirmMessageKey == 1){
            return JOptionPane.showConfirmDialog(null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", JOptionPane.YES_NO_OPTION) == 0;
        }
        return true;
    }
    /**
     * Opens the Github page for MGT2MT in the default browser.
     */
    public static void openGithubPage() throws Exception {
        Desktop.getDesktop().browse(new URL(GITHUB_URL).toURI());
    }

    /**
     * Opens the Github page for MGT2MT in the default browser.
     */
    public static void openMoreModsPage() throws Exception {
        Desktop.getDesktop().browse(new URL(MORE_MODS_URL).toURI());
    }

    /**
     * @param imageFile The image file that should be resized
     * @return Returns the resized image file
     */
    public static ImageIcon getSmallerImageIcon(ImageIcon imageFile){
        Image image = imageFile.getImage(); // transform it
        Image resizedImage = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(resizedImage);  // transform it back
    }

    /**
     * @param languageKey They key number for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(int languageKey){
        String currentLanguageKey = TranslationManager.TRANSLATION_KEYS[languageKey];
        return new File(Utils.getMGT2TextFolderPath() + "//" + currentLanguageKey + "//Themes_" + currentLanguageKey + ".txt");
    }

    /**
     * @param languageKey They key for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(String languageKey){
        return new File(Utils.getMGT2TextFolderPath() + "//" + languageKey + "//Themes_" + languageKey + ".txt");
    }

    /**
     * @return Returns the Licence.txt file
     */
    public static File getLicenceFile(){
        return new File(Utils.getMGT2DataPath() + "//Licence.txt");
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     * @return Returns the selected image file path
     */
    public static String getImagePath(){
        return getImagePath(false);
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     * @param showConfirmMessage Set true to display a message that the image file has been set.
     * @return Returns the selected image file path
     */
    public static String getImagePath(boolean showConfirmMessage){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

            javax.swing.filechooser.FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
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
                    if(showConfirmMessage){
                        JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                    }
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

    /**
     * Takes the input string and replaces the genreNames with the corresponding genre id.
     * @return Returns a list of genre ids.
     */
    public static String convertGenreNamesToId(String genreNamesRaw){
        StringBuilder genreIds = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentName = new StringBuilder();
        for(int i = 0; i<genreNamesRaw.length(); i++){
            if(String.valueOf(genreNamesRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNamesRaw.charAt(charPosition)).equals(">")){
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreName: " + currentName);
                }
                int genreId = AnalyzeManager.genreAnalyzer.getContentIdByName(currentName.toString());
                if(genreId != -1){
                    genreIds.append("<").append(genreId).append(">");
                }
                currentName = new StringBuilder();
            }else{
                currentName.append(genreNamesRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentName);
                }
            }
            charPosition++;
        }
        String.valueOf(genreNamesRaw.charAt(1));
        if(Settings.enableDebugLogging){
            LOGGER.info("Genre ids: " + genreIds);
        }
        return genreIds.toString();
    }

    /**
     * Converts the input string to an array list containing the elements of the string. Input string formatting: <s1><s2><s3>. The content between the <> is added to the array list.
     * @param string Input string
     * @return Returns an array list containing the elements of string
     */
    public static ArrayList<String> getEntriesFromString(String string) throws NullPointerException{
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder currentEntry = new StringBuilder();
        for(Character character : string.toCharArray()){
            if(character.toString().equals("<")){
                //Nothing happens
            }else if(character.toString().equals(">")){
                arrayList.add(currentEntry.toString());
                currentEntry = new StringBuilder();
            }else{
                currentEntry.append(character);
            }
        }
        return arrayList;
    }

    /**
     * @param genreId The genre id for which the file should be searched
     * @return Returns a String containing theme ids
     */
    public static String getCompatibleThemeIdsForGenre(int genreId) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemesGeFile()), StandardCharsets.UTF_16LE));
        boolean firstLine = true;
        int lineNumber = 1;
        StringBuilder compatibleThemes = new StringBuilder();
        String currentLine;
        while((currentLine = br.readLine()) != null){
            if(firstLine){
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(currentLine.contains(Integer.toString(genreId))){
                compatibleThemes.append("<");
                compatibleThemes.append(currentLine.replace(" ", "_").replace("<", "").replace(">", "").replaceAll("[0-9]", ""));
                compatibleThemes.append("-");
                compatibleThemes.append(lineNumber);
                compatibleThemes.append(">");
            }
            lineNumber++;
        }
        br.close();
        return compatibleThemes.toString();
    }

    /**
     * @param genreId The genre id for which the file should be searched
     * @param goodFeature True when the file should be searched for good features. False when it should be searched for bad features.
     * @return Returns a String containing gameplay feature names
     */
    public static String getCompatibleGameplayFeatureIdsForGenre(int genreId, boolean goodFeature) throws IOException {
        StringBuilder gameplayFeaturesIds = new StringBuilder();
        if(goodFeature){
            for(Map<String, String> map : AnalyzeManager.gameplayFeatureAnalyzer.getFileContent()){
                if(map.get("GOOD") != null){
                    if(map.get("GOOD").contains("<" + genreId + ">")){
                        gameplayFeaturesIds.append("<").append(map.get("NAME EN")).append(">");
                    }
                }
            }
        }else{
            for(Map<String, String> map : AnalyzeManager.gameplayFeatureAnalyzer.getFileContent()){
                if(map.get("BAD") != null){
                    if(map.get("BAD").contains("<" + genreId + ">")){
                        gameplayFeaturesIds.append("<").append(map.get("NAME EN")).append(">");
                    }
                }
            }
        }
        return gameplayFeaturesIds.toString();
    }

    /**
     * Opens a window where the user can select entries from a list.
     * @param labelText The text that should be displayed at the top of the window
     * @param windowTile The window title that the window should get
     * @param stringArraySafetyFeaturesOn An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @param showNoSelectionMessage If true the message that something should be selected, when selection is empty is not shown.
     * @return Returns the selected entries as array list.
     */
    public static ArrayList<Integer> getSelectedEntries(String labelText, String windowTile, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, boolean showNoSelectionMessage){
        return getSelectedEntries(labelText, windowTile, stringArraySafetyFeaturesOn, stringArraySafetyFeaturesDisabled, showNoSelectionMessage, false);
    }

    /**
     * Opens a window where the user can select entries from a list.
     * @param labelText The text that should be displayed at the top of the window
     * @param windowTile The window title that the window should get
     * @param stringArraySafetyFeaturesOn An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @param returnGenreIds If true the return value is a array list containing genre ids. If false the position of the selected entries is returned.
     * @param showNoSelectionMessage If true the message that something should be selected, when selection is empty is not shown.
     * @return Returns the selected entries as array list.
     */
    public static ArrayList<Integer> getSelectedEntries(String labelText, String windowTile, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, boolean showNoSelectionMessage, boolean returnGenreIds){
        ArrayList<Integer> returnValues = new ArrayList<>();
        JLabel labelChooseEntry = new JLabel(labelText);
        String[] existingListContent;
        if(Settings.disableSafetyFeatures){
            existingListContent = stringArraySafetyFeaturesDisabled;
        }else {
            existingListContent = stringArraySafetyFeaturesOn;
        }
        JList<String> listAvailableEntries = new JList<>(existingListContent);
        listAvailableEntries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableEntries.setLayoutOrientation(JList.VERTICAL);
        listAvailableEntries.setVisibleRowCount(-1);
        JScrollPane scrollPaneAvailableEntries = new JScrollPane(listAvailableEntries);
        scrollPaneAvailableEntries.setPreferredSize(new Dimension(315,140));

        Object[] params = {labelChooseEntry, scrollPaneAvailableEntries};

        if(JOptionPane.showConfirmDialog(null, params, windowTile, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            if(!listAvailableEntries.isSelectionEmpty()){
                for(String string : listAvailableEntries.getSelectedValuesList()){
                    if(returnGenreIds){
                        returnValues.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
                    }else{
                        returnValues.add(getPositionInList(string, existingListContent));
                    }
                }
            }else{
                if(showNoSelectionMessage){
                    JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
        return returnValues;
    }

    /**
     * @param arrayList The array list containing the values
     * @return Returns the values in the respective formatting
     */
    public static String transformArrayListToString(ArrayList arrayList){
        StringBuilder returnString = new StringBuilder();
        for(Object object : arrayList){
            returnString.append("<").append(object.toString()).append(">");
        }
        return returnString.toString();
    }

    /**
     * Checks the array lists if they have mutual entries. Returns true if the do. Returns false if the don't
     */
    public static boolean checkForMutualEntries(ArrayList arrayList1, ArrayList arrayList2){
        for(Object object1 : arrayList1){
            for(Object object2 : arrayList2){
                if(object1 == object2){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Opens the given folder
     * @param path
     */
    public static void open(String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to open folder.\n\nException:\n" + e.getMessage(), "Unable to open folder", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * @param arrayList The array list that should be converted
     * @return Returns a string array
     */
    public static String[] convertArrayListToArray(ArrayList<String> arrayList){
        String[] strings = new String[arrayList.size()];
        strings = arrayList.toArray(strings);
        return strings;
    }

    /**
     * @param itemInList The item from which the position should be returned
     * @param listContent A list containing the selected entries
     * @return Returns the position of the item in the list
     */
    public static int getPositionInList(String itemInList, String[] listContent){
        int currentNumber = 0;
        for(String string : listContent){
            if(string.equals(itemInList)){
                return currentNumber;
            }
            currentNumber++;
        }
        return -1;
    }

    /**
     * @return Returns a random number between and including origin and range.
     */
    public static int getRandomNumber(int origin, int range){
        return ThreadLocalRandom.current().nextInt(origin, range);
    }

    /**
     * Rounds the input number up/down to the next five
     * @param inputNumber The input number
     * @return Returns the rounded number
     */
    public static int roundToNextFive(int inputNumber){
        double num = inputNumber;
        if (num % 5 == 0)
            return (int) num;
        else if (num % 5 < 2.5)
            num = num - num % 5;
        else
            num = num + (5 - num % 5);
        return (int) num;
    }

    /**
     * Rounds the input number up/down to the next hundred
     * @param inputNumber The input number
     * @return Returns the rounded number
     */
    public static int roundToNextHundred(int inputNumber){
        try{
            return (inputNumber + 50) / 100 * 100;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Rounds the input number up/down to the next thousand
     * @param inputNumber The input number
     * @return Returns the rounded number
     */
    public static int roundToNextThousand(int inputNumber){
        try{
            return (inputNumber + 500) / 1000 * 1000;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Appends the contents of the input array to the string builder
     * @param entriesPerLine Defines how many objects should be written in one line.
     */
    public static void appendStringArrayToStringBuilder(StringBuilder stringBuilder, String[] inputArray, int entriesPerLine){
        int currentLine = 1;
        boolean firstEntry = true;
        for(String string : inputArray){
            if(!firstEntry){
                stringBuilder.append(", ");
            }
            if(currentLine == entriesPerLine+1){
                stringBuilder.append(System.getProperty("line.separator"));
                currentLine = 1;
            }
            stringBuilder.append(string);
            currentLine++;
            firstEntry = false;
        }
        stringBuilder.append(System.getProperty("line.separator"));
    }

    /**
     * Converts the input seconds to minutes and seconds
     */
    public static String convertSecondsToTime(int second){
        int minutes = (second % 3600) / 60;
        int seconds = second % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
