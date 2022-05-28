package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.TimeHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
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
    public static String getCurrentDateTime() {
        return LocalDateTime.now().getYear() + "-" +
                LocalDateTime.now().getMonth() + "-" +
                LocalDateTime.now().getDayOfMonth() + "-" +
                LocalDateTime.now().getHour() + "-" +
                LocalDateTime.now().getMinute() + "-" +
                LocalDateTime.now().getSecond();
    }

    /**
     * @param saveGameNumber The save game number
     * @return Returns the save game file for the input save game number
     */
    public static File getSaveGameFile(int saveGameNumber) {
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
     *
     * @param errorMessageKey the error message key. see this functions for meanings
     * @param e               The exception
     */
    public static void showErrorMessage(int errorMessageKey, Exception e) {
        switch (errorMessageKey) {
            // 1 = Used when AnalyzeExistingGenres.analyzeGenreFile() or AnalyzeThemes.analyzeThemesFile() an exception.
            case 1:
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("errorMessages.gameFilesNotAnalyzed") + "\n\nException:\n" + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
                break;
            // 2 = When it is unsuccessful to open the github repository.
            case 2:
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("errorMessages.unableToOpenWebPage") + "\n\nException:\n" + e.getMessage(), "Can't open page", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    /**
     * Opens a message dialog with a specified error message.
     *
     * @param confirmMessageKey The confirm message key. See this functions for meanings.
     * @param e                 The exception
     * @return Returns true when the user clicks yes. Returns false when the user clicks no.
     */
    public static boolean showConfirmDialog(int confirmMessageKey, Exception e) {
        if (confirmMessageKey == 1) {
            return JOptionPane.showConfirmDialog(null, "The backup could not be created.\n\nException:\n" + e.getMessage() + "\nDo you want to continue anyway?", "Unable to backup file", JOptionPane.YES_NO_OPTION) == 0;
        }
        return true;
    }

    /**
     * Opens a confirm dialog with a specified message.
     *
     * @param confirmMessageKey The confirm message key. See this function for meanings.
     * @return Returns true when the user clicks yes. Returns false when the user clicks no.
     */
    public static boolean showConfirmDialog(int confirmMessageKey) {
        if (confirmMessageKey == 1) {
            return JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.quit.confirmMessage"), I18n.INSTANCE.get("mod.genre.quit.confirmMessage.title"), JOptionPane.YES_NO_OPTION) == 0;
        }
        return true;
    }

    /**
     * Opens the GitHub page for MGT2MT in the default browser.
     *
     * @throws IOException If the page could not be opened
     */
    public static void openGithubPage() throws Exception {
        Desktop.getDesktop().browse(new URL(GITHUB_URL).toURI());
    }

    /**
     * Opens the GitHub page for MGT2MT in the default browser.
     *
     * @throws IOException If the page could not be opened
     */
    public static void openMoreModsPage() throws Exception {
        Desktop.getDesktop().browse(new URL(MORE_MODS_URL).toURI());
    }

    /**
     * @param imageFile The image file that should be resized
     * @return Returns the resized image file
     */
    public static ImageIcon getSmallerImageIcon(ImageIcon imageFile) {
        Image image = imageFile.getImage(); // transform it
        Image resizedImage = image.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(resizedImage);  // transform it back
    }

    /**
     * @param languageKey They key number for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(int languageKey) {
        String currentLanguageKey = TranslationManager.TRANSLATION_KEYS[languageKey];
        return MGT2Paths.TEXT.getPath().resolve(Paths.get(currentLanguageKey, "Themes_" + currentLanguageKey + ".txt")).toFile();
    }

    /**
     * @param languageKey They key for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(String languageKey) {
        return MGT2Paths.TEXT.getPath().resolve(languageKey + "/Themes_" + languageKey + ".txt").toFile();
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     *
     * @return The selected image file path
     * @throws ModProcessingException If the user did not select a file
     */
    public static Path getImagePath() throws ModProcessingException {
        return getImagePath(false);
    }

    /**
     * Opens a file chooser where a single image file can be selected.
     *
     * @param showConfirmMessage Set true to display a message that the image file has been set.
     * @return Returns the selected image file path
     * @throws ModProcessingException If the user did not select a file
     */
    public static Path getImagePath(boolean showConfirmMessage) throws ModProcessingException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

            javax.swing.filechooser.FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                @Override
                public boolean accept(File f) {
                    if (f.getName().contains(".png")) {
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
            if (return_value == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().getName().contains(".png")) {
                    if (showConfirmMessage) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return fileChooser.getSelectedFile().toPath();
                } else {
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                    throw new ModProcessingException("Image file could not be selected: Please select a .png file");
                }
            } else {
                throw new ModProcessingException("Image selection has been canceled");
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            throw new ModProcessingException("Image file could not be selected", e);
        }
    }

    /**
     * Converts the input string to an array list containing the elements of the string. Input string formatting: {@literal <s1><s2><s3>}. The content between the {@literal <>} is added to the array list.
     *
     * @param string Input string
     * @return Returns an array list containing the elements of string
     */
    public static ArrayList<String> getEntriesFromString(String string) throws NullPointerException {
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder currentEntry = new StringBuilder();
        boolean workingOnEntry = false;
        for (Character character : string.toCharArray()) {
            if (character.toString().equals("<")) {
                workingOnEntry = true;
            } else if (character.toString().equals(">")) {
                arrayList.add(currentEntry.toString());
                currentEntry = new StringBuilder();
                workingOnEntry = false;
            } else {
                if (workingOnEntry) {
                    currentEntry.append(character);
                }
            }
        }
        return arrayList;
    }

    /**
     * Transforms a string array to a integer array.
     *
     * @param strings String array to transform
     * @return The transformed integer array
     * @throws NumberFormatException When the strings can not be parsed to integer.
     */
    public static ArrayList<Integer> transformStringArrayToIntegerArray(ArrayList<String> strings) throws NumberFormatException {
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : strings) {
            integers.add(Integer.parseInt(s));
        }
        return integers;
    }

    /**
     * Returns the part before the first {@literal <}.
     * Trims the string to remove whitespaces.
     *
     * @param string The string to split the first part from
     * @return The first part of the string
     * @see Utils#getEntriesFromString(String) for more information
     */
    public static String getFirstPart(String string) throws NullPointerException {
        StringBuilder output = new StringBuilder();
        for (Character character : string.toCharArray()) {
            if (!character.toString().equals("<")) {
                output.append(character);
            } else {
                break;
            }
        }
        return output.toString().trim();
    }

    /**
     * Converts the names in the string to the corresponding id.
     * Example: Input: {@literal <hallo><tree>} Output: {@literal [0,1]}
     *
     * @param contentManager The content manager to get the names from
     * @param string         The string to convert
     * @return An array list containing the ids of the names
     * @throws ModProcessingException When {@link Utils#getEntriesFromString(String)} fails
     */
    public static ArrayList<Integer> getContentIdsFromString(BaseContentManager contentManager, String string) throws ModProcessingException {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> content = Utils.getEntriesFromString(string);
        for (String s : content) {
            ids.add(contentManager.getContentIdByName(s));
        }
        return ids;
    }

    public static ArrayList<Integer> getCompatibleThemeIdsForGenreNew(int genreId) throws ModProcessingException {
        ArrayList<Integer> themeIds = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ThemeManager.INSTANCE.getGameFile()), StandardCharsets.UTF_16LE));
            boolean firstLine = true;
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                ArrayList<String> entries = Utils.getEntriesFromString(currentLine);
                for (String s : entries) {
                    try {
                        if (Integer.parseInt(s) == genreId) {
                            themeIds.add(Integer.parseInt(s));
                        }
                    } catch (NumberFormatException ignored) {

                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to retrieve theme ids for genre", e);
        }
        return themeIds;
    }

    /**
     * @param genreId     The genre id for which the file should be searched
     * @param goodFeature True when the file should be searched for good features. False when it should be searched for bad features.
     * @return Returns a String containing gameplay feature names
     */
    public static String getCompatibleGameplayFeatureIdsForGenre(int genreId, boolean goodFeature) {
        StringBuilder gameplayFeaturesIds = new StringBuilder();
        if (goodFeature) {
            for (Map<String, String> map : GameplayFeatureManager.INSTANCE.fileContent) {
                if (map.get("GOOD") != null) {
                    if (map.get("GOOD").contains("<" + genreId + ">")) {
                        gameplayFeaturesIds.append("<").append(map.get("NAME EN")).append(">");
                    }
                }
            }
        } else {
            for (Map<String, String> map : GameplayFeatureManager.INSTANCE.fileContent) {
                if (map.get("BAD") != null) {
                    if (map.get("BAD").contains("<" + genreId + ">")) {
                        gameplayFeaturesIds.append("<").append(map.get("NAME EN")).append(">");
                    }
                }
            }
        }
        return gameplayFeaturesIds.toString();
    }

    /**
     * Opens a window where the user can select entries from a list.
     *
     * @param labelText                         The text that should be displayed at the top of the window
     * @param windowTile                        The window title that the window should get
     * @param stringArraySafetyFeaturesOn       An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @return Returns the selected entry names. If cancel is pressed null is returned.
     */
    public static List<String> getSelectedEntries(String labelText, String windowTile, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled) {
        List<String> returnValues = new ArrayList<>();
        JLabel labelChooseEntry = new JLabel(labelText);
        String[] existingListContent;
        if (Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
            existingListContent = stringArraySafetyFeaturesDisabled;
        } else {
            existingListContent = stringArraySafetyFeaturesOn;
        }
        JList<String> listAvailableEntries = new JList<>(existingListContent);
        listAvailableEntries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableEntries.setLayoutOrientation(JList.VERTICAL);
        listAvailableEntries.setVisibleRowCount(-1);
        JScrollPane scrollPaneAvailableEntries = new JScrollPane(listAvailableEntries);
        scrollPaneAvailableEntries.setPreferredSize(new Dimension(315, 140));

        Object[] params = {labelChooseEntry, scrollPaneAvailableEntries};

        int returnValue = JOptionPane.showConfirmDialog(null, params, windowTile, JOptionPane.OK_CANCEL_OPTION);
        if (returnValue == JOptionPane.OK_OPTION) {
            if (!listAvailableEntries.isSelectionEmpty()) {
                returnValues.addAll(listAvailableEntries.getSelectedValuesList());
            }
        } else if (returnValue == JOptionPane.CANCEL_OPTION) {
            return null;
        }
        return returnValues;
    }

    /**
     * Opens a window where the user can select entries from a list.
     *
     * @param labelText                         The text that should be displayed at the top of the window
     * @param windowTile                        The window title that the window should get
     * @param stringArraySafetyFeaturesOn       An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @param showNoSelectionMessage            If true the message that something should be selected, when selection is empty is not shown.
     * @return Returns the selected entries as array list.
     */
    public static ArrayList<Integer> getSelectedEntriesOld(String labelText, String windowTile, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, boolean showNoSelectionMessage) {
        ArrayList<Integer> returnValues = new ArrayList<>();
        JLabel labelChooseEntry = new JLabel(labelText);
        String[] existingListContent;
        if (Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
            existingListContent = stringArraySafetyFeaturesDisabled;
        } else {
            existingListContent = stringArraySafetyFeaturesOn;
        }
        JList<String> listAvailableEntries = new JList<>(existingListContent);
        listAvailableEntries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableEntries.setLayoutOrientation(JList.VERTICAL);
        listAvailableEntries.setVisibleRowCount(-1);
        JScrollPane scrollPaneAvailableEntries = new JScrollPane(listAvailableEntries);
        scrollPaneAvailableEntries.setPreferredSize(new Dimension(315, 140));

        Object[] params = {labelChooseEntry, scrollPaneAvailableEntries};

        if (JOptionPane.showConfirmDialog(null, params, windowTile, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (!listAvailableEntries.isSelectionEmpty()) {
                for (String string : listAvailableEntries.getSelectedValuesList()) {
                    returnValues.add(getPositionInList(string, existingListContent));
                }
            } else {
                if (showNoSelectionMessage) {
                    JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
        return returnValues;
    }

    /**
     * @param arrayList The array list containing the values
     * @return Returns the values in the following formatting {@literal <VALUE>}.
     */
    public static String transformArrayListToString(ArrayList<?> arrayList) {
        StringBuilder returnString = new StringBuilder();
        for (Object object : arrayList) {
            returnString.append("<").append(object.toString()).append(">");
        }
        return returnString.toString();
    }

    /**
     * Checks the array lists if they have mutual entries. Returns true if the do. Returns false if the don't
     *
     * @param arrayList1 The first array list
     * @param arrayList2 The second array list
     * @return Returns true if the lists have mutual entries, false if they don't
     */
    public static boolean checkForMutualEntries(ArrayList<?> arrayList1, ArrayList<?> arrayList2) {
        for (Object object1 : arrayList1) {
            for (Object object2 : arrayList2) {
                if (object1 == object2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Opens the given folder/file
     *
     * @param path The path that should be opened
     */
    public static void open(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Desktop.getDesktop().open(path.toFile());
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
     * @param itemInList  The item from which the position should be returned
     * @param listContent A list containing the selected entries
     * @return Returns the position of the item in the list
     */
    public static int getPositionInList(String itemInList, String[] listContent) {
        int currentNumber = 0;
        for (String string : listContent) {
            if (string.equals(itemInList)) {
                return currentNumber;
            }
            currentNumber++;
        }
        return -1;
    }

    /**
     * @param min The smallest number
     * @param max The largest number
     * @return Returns a random number between and including origin and range.
     */
    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Converts the input seconds to minute and seconds
     *
     * @param second The number of seconds to convert
     * @return Returns the converted time
     */
    public static String convertSecondsToTime(int second) {
        int minutes = (second % 3600) / 60;
        int seconds = second % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * @param value The value that should be translated
     * @return Returns the value of the boolean translated
     */
    public static String getTranslatedValueFromBoolean(boolean value) {
        if (value) {
            return I18n.INSTANCE.get("commonText.yes");
        } else {
            return I18n.INSTANCE.get("commonText.no");
        }
    }

    /**
     * Converts the input integer to string in the following way: 1000000 {@literal ->} 1.000.000
     *
     * @param inputInt The input number
     * @return Returns the converted number as string
     */
    public static String convertIntToString(int inputInt) {
        DecimalFormat formatter = new DecimalFormat("###,###.###");
        return formatter.format(inputInt);
    }

    /**
     * Turns the input string to lowercase and replaces all whitespaces and symbols with space
     *
     * @param string The input string
     * @return Returns the converted string
     */
    public static String convertName(String string) {
        return string.toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", "_");
    }

    /**
     * Transforms the input map of format {@literal Map<String, Object> to Map<String, String>}.
     * If an object can not be cast to string it will not be placed in the map that is returned.
     *
     * @param map The map that should be converted
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

    /**
     * Checks if the current running mod tool version is an alpha version.
     *
     * @return True if the version is alpha. False otherwise.
     */
    public static boolean isAlpha() {
        if (MadGamesTycoon2ModTool.VERSION.contains("alpha")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sorts the panels that are listed in the array list and returns the array list sorted.
     * The panels are sorted by retrieving their names.
     *
     * @param panels The array list that contains the JPanels
     * @return An objects array that contains the panels in a sorted way.
     * @throws IllegalArgumentException If the panels that are stored in the array list do not contain names
     */
    public static Object[] getSortedModPanels(ArrayList<JPanel> panels) {
        JPanel[] modPanels = panels.toArray(new JPanel[0]);
        try {
            return Arrays.stream(modPanels).sorted(Comparator.comparing(Component::getName)).toArray();
        } catch (NullPointerException e) {
            DebugHelper.warn(LOGGER, "The panels array list is invalid: panel.getName returns null! The list will not be returned sorted!");
            return panels.toArray();
        }
    }

    /**
     * Replaces all keys in the origin map with the values in the replacement map.
     * For that the replacement map has to contain the same keys as the origin map.
     *
     * @param origin       In this map the values will be replaced.
     * @param replacements The map that contains the values that should be replaced.
     * @throws IllegalArgumentException When the replacement map contains a key that does not exist in the origin map.
     */
    public static void replaceMapEntries(Map<String, String> origin, Map<String, String> replacements) throws IllegalArgumentException {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (origin.containsKey(entry.getKey())) {
                origin.replace(entry.getKey(), entry.getValue());
            } else {
                throw new IllegalArgumentException("Replacements map contains a key that the origin map does not contain");
            }
        }
    }


    /**
     * @param list The list that should be converted to a string array
     * @return The array that contains the strings of the list
     */
    public static String[] transformListToArray(List<String> list) {
        String[] out = new String[list.size()];
        list.toArray(out);
        return out;
    }

    /**
     * Constructs a content for each name in the names list.
     * Uses the progressbar.
     *
     * @param names   The names for which the content should be constructed.
     * @param manager The manager that is used to construct the mods.
     * @return The list with the constructed content.
     * @throws ModProcessingException When the content could not be constructed.
     */
    public static List<AbstractBaseContent> constructContents(List<String> names, BaseContentManager manager) throws ModProcessingException {
        TimeHelper th = new TimeHelper();
        ArrayList<AbstractBaseContent> contents = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(0, names.size(), String.format(I18n.INSTANCE.get("progressBar.constructingContent"), manager.getType()));
        TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("progressBar.constructingContent"), manager.getType()));
        for (String name : names) {
            try {
                contents.add(manager.constructContentFromName(name));
                ProgressBarHelper.increment();
            } catch (ModProcessingException | NumberFormatException e) {
                throw new ModProcessingException("Unable to construct content named " + name, e);
            }
        }
        ProgressBarHelper.resetProgressBar();
        TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.constructingContents.duration"), th.getMeasuredTime() / 1000.0));
        return contents;
    }
}
