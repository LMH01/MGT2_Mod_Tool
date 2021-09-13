package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
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
     * @param saveGameNumber The save game number
     * @return Returns the save game file for the input save game number
     */
    public static File getSaveGameFile(int saveGameNumber){
        return new File(Backup.FILE_SAVE_GAME_FOLDER + "/" + "savegame" + saveGameNumber + ".txt");
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
            return JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.quit.confirmMessage"), I18n.INSTANCE.get("mod.genre.quit.confirmMessage.title"), JOptionPane.YES_NO_OPTION) == 0;
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
        return MGT2Paths.TEXT.getPath().resolve(Paths.get(currentLanguageKey, "Themes_" + currentLanguageKey + ".txt")).toFile();
    }

    /**
     * @param languageKey They key for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(String languageKey){
        return MGT2Paths.TEXT.getPath().resolve(languageKey + "/Themes_" + languageKey + ".txt").toFile();
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     * @return The selected image file path
     */
    public static Path getImagePath() throws ModProcessingException{
        return getImagePath(false);
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     * @param showConfirmMessage Set true to display a message that the image file has been set.
     * @return Returns the selected image file path
     */
    public static Path getImagePath(boolean showConfirmMessage) throws ModProcessingException {
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
                    return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                }
            };

            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFile.fileChooser"));

            int return_value = fileChooser.showOpenDialog(null);
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            if(return_value == JFileChooser.APPROVE_OPTION){
                if(fileChooser.getSelectedFile().getName().contains(".png")){
                    if(showConfirmMessage){
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return fileChooser.getSelectedFile().toPath();
                }else{
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                    throw new ModProcessingException("Image file could not be selected: Please select a .png file");
                }
            }else{
                throw new ModProcessingException("Image selection has been canceled");
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            throw new ModProcessingException("Image file could not be selected: " + e.getMessage(), e);
        }
    }

    /**
     * Converts the input string to an array list containing the elements of the string. Input string formatting: <s1><s2><s3>. The content between the <> is added to the array list.
     * @param string Input string
     * @return Returns an array list containing the elements of string
     */
    public static ArrayList<String> getEntriesFromString(String string) throws NullPointerException{
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder currentEntry = new StringBuilder();
        boolean workingOnEntry = false;
        for(Character character : string.toCharArray()){
            if(character.toString().equals("<")){
                workingOnEntry = true;
            }else if(character.toString().equals(">")){
                arrayList.add(currentEntry.toString());
                currentEntry = new StringBuilder();
                workingOnEntry = false;
            }else{
                if(workingOnEntry){
                    currentEntry.append(character);
                }
            }
        }
        return arrayList;
    }

    /**
     * Returns the part before the first <.
     * Trims the string to remove whitespaces.
     * See {@link Utils#getEntriesFromString(String)} for more information
     */
    public static String getFirstPart(String string) throws NullPointerException{
        StringBuilder output = new StringBuilder();
        for(Character character : string.toCharArray()){
            if(!character.toString().equals("<")){
                output.append(character);
            }else{
                break;
            }
        }
        return output.toString().trim();
    }

    /**
     * @param genreId The genre id for which the file should be searched
     * @return Returns a String containing theme ids
     */
    public static String getCompatibleThemeIdsForGenre(int genreId) throws ModProcessingException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ModManager.themeMod.getGameFile()), StandardCharsets.UTF_16LE));
            boolean firstLine = true;
            StringBuilder compatibleThemes = new StringBuilder();
            String currentLine;
            while((currentLine = br.readLine()) != null){
                if(firstLine){
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(currentLine.contains(Integer.toString(genreId))){
                    compatibleThemes.append("<");
                    compatibleThemes.append(ModManager.themeMod.getReplacedLine(currentLine));
                    compatibleThemes.append(">");
                }
            }
            br.close();
            return compatibleThemes.toString();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to retrieve theme ids for genre", e);
        }
    }

    /**
     * @param genreId The genre id for which the file should be searched
     * @param goodFeature True when the file should be searched for good features. False when it should be searched for bad features.
     * @return Returns a String containing gameplay feature names
     */
    public static String getCompatibleGameplayFeatureIdsForGenre(int genreId, boolean goodFeature) {
        StringBuilder gameplayFeaturesIds = new StringBuilder();
        if(goodFeature){
            for(Map<String, String> map : ModManager.gameplayFeatureMod.getFileContent()){
                if(map.get("GOOD") != null){
                    if(map.get("GOOD").contains("<" + genreId + ">")){
                        gameplayFeaturesIds.append("<").append(map.get("NAME EN")).append(">");
                    }
                }
            }
        }else{
            for(Map<String, String> map : ModManager.gameplayFeatureMod.getFileContent()){
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
                    returnValues.add(getPositionInList(string, existingListContent));
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
    public static String transformArrayListToString(ArrayList<?> arrayList) {
        StringBuilder returnString = new StringBuilder();
        for(Object object : arrayList){
            returnString.append("<").append(object.toString()).append(">");
        }
        return returnString.toString();
    }

    /**
     * Checks the array lists if they have mutual entries. Returns true if the do. Returns false if the don't
     */
    public static boolean checkForMutualEntries(ArrayList<?> arrayList1, ArrayList<?> arrayList2) {
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
     */
    public static void open(Path path){
        try {
            File file = path.toFile();
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
    public static String[] convertArrayListToArray(ArrayList<String> arrayList) {
        String[] strings = new String[arrayList.size()];
        strings = arrayList.toArray(strings);
        return strings;
    }

    /**
     * @param itemInList The item from which the position should be returned
     * @param listContent A list containing the selected entries
     * @return Returns the position of the item in the list
     */
    public static int getPositionInList(String itemInList, String[] listContent) {
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
    public static int getRandomNumber(int origin, int range) {
        return ThreadLocalRandom.current().nextInt(origin, range);
    }

    /**
     * Rounds the input number up/down to the next five
     * @param inputNumber The input number
     * @return Returns the rounded number
     */
    public static int roundToNextFive(int inputNumber) {
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
    public static int roundToNextHundred(int inputNumber) {
        try {
            return (inputNumber + 50) / 100 * 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Rounds the input number up/down to the next thousand
     * @param inputNumber The input number
     * @return Returns the rounded number
     */
    public static int roundToNextThousand(int inputNumber) {
        try {
            return (inputNumber + 500) / 1000 * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

// --Commented out by Inspection START (13.08.2021 22:57):
//    /**
//     * Appends the contents of the input array to the string builder
//     * @param entriesPerLine Defines how many objects should be written in one line.
//     */
//    public static void appendStringArrayToStringBuilder(StringBuilder stringBuilder, String[] inputArray, int entriesPerLine){
//        int currentLine = 1;
//        boolean firstEntry = true;
//        for(String string : inputArray){
//            if(!firstEntry){
//                stringBuilder.append(", ");
//            }
//            if(currentLine == entriesPerLine+1){
//                stringBuilder.append(System.getProperty("line.separator")).append("          ");
//                currentLine = 1;
//            }
//            stringBuilder.append(string);
//            currentLine++;
//            firstEntry = false;
//        }
//        stringBuilder.append(System.getProperty("line.separator"));
//    }
// --Commented out by Inspection STOP (13.08.2021 22:57)

    /**
     * Converts the input seconds to minutes and seconds
     */
    public static String convertSecondsToTime(int second){
        int minutes = (second % 3600) / 60;
        int seconds = second % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * @return Returns the value of the boolean translated
     */
    public static String getTranslatedValueFromBoolean(boolean value){
        if(value){
            return I18n.INSTANCE.get("commonText.yes");
        }else{
            return I18n.INSTANCE.get("commonText.no");
        }
    }

    /**
     * Converts the input genre id to a number
     * JAN = 1
     * ...
     * DEC = 12
     * @param string The input string that should be converted - Converts the whole string and just searches for the month keyword
     */
    public static int getNumberForMonth(String string) throws IllegalArgumentException { //TODO Replace with enum / throw ModProcessingException
        if(string.contains("JAN")){
            return 1;
        }else if(string.contains("FEB")){
            return 2;
        }else if(string.contains("MAR")){
            return 3;
        }else if(string.contains("APR")){
            return 4;
        }else if(string.contains("MAY")){
            return 5;
        }else if(string.contains("JUN")){
            return 6;
        }else if(string.contains("JUL")){
            return 7;
        }else if(string.contains("AUG")){
            return 8;
        }else if(string.contains("SEP")){
            return 9;
        }else if(string.contains("OCT")){
            return 10;
        }else if(string.contains("NOV")){
            return 11;
        }else if(string.contains("DEC")){
            return 12;
        }
        throw new IllegalArgumentException("The input string is invalid: " + string);
    }

    /**
     * Converts the input integer to string in the following way: 1000000 -> 1.000.000
     * @return Returns the converted number as string
     */
    public static String convertIntToString(int inputInt){
        DecimalFormat formatter = new DecimalFormat("###,###.###");
        return formatter.format(inputInt);
    }

    /**
     * Turns the input string to lowercase and replaces all whitespaces and symbols with space
     */
    public static String convertName(String string) {
        return string.toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", "_");
    }

    /**
     * Transforms the input map of format {@literal Map<String, Object> to Map<String, String>}.
     * If an object can not be cast to string it will not be placed in the map that is returned.
     * @return The transformed map
     */
    public static Map<String, String> transformObjectMapToStringMap(Map<String, Object> map) {
        Map<String, String> returnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                returnMap.put(entry.getKey(), (String) entry.getValue());
            } catch (ClassCastException ignored) {

            }
        }
        return returnMap;
    }
}
