package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Utils {

    public static int genreLineNumbers = 48;//Defines how many lines a genre has inside the Genres.txt file. When the amount of lines is changed, change this number to apply the new number to all refering operations.

    //These are the files inside the mgt2 file structure that are used inside this tool.
    public static final String GITHUB_URL = "https://github.com/LMH01/MGT2_Mod_Tool";
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * @return returns the current date time in format: YYYY-MM-DD-HH-MM
     */
    public static String getCurrentDateTime(){
        return LocalDateTime.now().getYear() + "-" +
                LocalDateTime.now().getMonth() + "-"+
                LocalDateTime.now().getDayOfMonth() + "-" +
                LocalDateTime.now().getHour() + "-" +
                LocalDateTime.now().getMinute();
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

    /**
     * @param file The input file
     * @return Returns a list containing map entries for every data package in the input text file.
     * @throws IOException
     */
    public static List<Map<String,String>> parseDataFile(File file) throws IOException{
        List<Map<String, String>> fileParts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String currentLine;
        boolean firstLine = true;
        Map<String, String> mapCurrent = new HashMap<>();
        while((currentLine = reader.readLine()) != null){
            if(firstLine){
                currentLine = currentLine.replaceAll("\\uFEFF", "");
            }
            if(currentLine.isEmpty()){
                fileParts.add(mapCurrent);
                mapCurrent = new HashMap<>();
            }else{
                boolean valueComplete = false;
                StringBuilder mapKey = new StringBuilder();
                StringBuilder mapValue = new StringBuilder();
                for(int i=1; i<currentLine.length(); i++){
                    if(String.valueOf(currentLine.charAt(i)).equals("]")){
                        valueComplete = true;
                        continue;
                    }
                    if(valueComplete){
                        mapValue.append(currentLine.charAt(i));
                    }else{
                        mapKey.append(currentLine.charAt(i));
                    }
                }
                mapCurrent.put(mapKey.toString(), mapValue.toString());
            }
        }
        reader.close();
        return fileParts;
    }

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

    public static File getThemesGeFile(){return new File(getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt");}

    public static File getThemesEnFile(){return new File(getMGT2TextFolderPath() + "\\EN\\Themes_EN.txt");}

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
            case 1: JOptionPane.showMessageDialog(null, "The game files could not be analyzed.\nPlease check if your mgt2 folder is set correctly.\nTry launching the tool with administrator rights.\n\nException:\n" + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE); break;
            // 2 = When it is unsuccessful to open the github repository.
            case 2: JOptionPane.showMessageDialog(null, "Unable to open Github repository:\n\nException:\n" + e.getMessage(), "Can't open page", JOptionPane.ERROR_MESSAGE); break;
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
     * @param folder The folder that should be tested if contains the file.
     * @param content The content that should be found.
     * @return Returns true when the input file is the MGT2 folder.
     */
    public static boolean doesFolderContainFile(String folder, String content){
        File file = new File(folder);
        if(file.exists()){
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if(filesInFolder[i].getName().equals(content)){
                    return true;
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info(filesInFolder[i].getName());
                }
            }
        }else{
            LOGGER.info("File does not exist.");
        }
        return false;
    }

    /**
     * @param folder The folder that should be searched for files.
     * @return Returns an array list containing all files inside the input folder
     */
    public static ArrayList<File> getFilesInFolder(String folder){
        File file = new File(folder);
        ArrayList<File> arrayListFiles = new ArrayList<>();
        if(file.exists()){
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                arrayListFiles.add(filesInFolder[i]);
                if(Settings.enableDebugLogging){
                    LOGGER.info(filesInFolder[i].getName());
                }
            }
        }
        return arrayListFiles;
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
     * @param languageKey They key for the language to use. For more information see this functions content.
     * @return Returns the themes file for the specified language.
     */
    public static File getThemeFile(int languageKey){
        String currentLanguageKey = TranslationManager.TRANSLATION_KEYS[languageKey];
        return new File(Utils.getMGT2TextFolderPath() + "//" + currentLanguageKey + "//Themes_" + currentLanguageKey + ".txt");
    }

    /**
     * Copied from https://www.baeldung.com/java-copy-directory
     * @param sourceDirectoryLocation The source
     * @param destinationDirectoryLocation The destination
     * @throws IOException
     */
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Copied from https://www.baeldung.com/java-delete-directory
     * Deletes a complete directory with its contents
     * @return Returns true when operation was successful
     */
    public static boolean deleteDirectory(File directoryToBeDeleted ){
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
                if(Settings.enableDebugLogging){
                    LOGGER.info("Deleting file: " + file.getPath());
                }
            }
        }
        return directoryToBeDeleted.delete();
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
     * Converts the genre names to ids
     * @return Returns a list of genre ids.
     */
    public static String getGenreIds(String genreNamesRaw){
        StringBuilder genreIds = new StringBuilder();
        int charPositon = 0;
        StringBuilder currentName = new StringBuilder();
        for(int i = 0; i<genreNamesRaw.length(); i++){
            if(String.valueOf(genreNamesRaw.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNamesRaw.charAt(charPositon)).equals(">")){
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreName: " + currentName);
                }
                int genreId = AnalyzeExistingGenres.getGenreIdByName(currentName.toString());
                if(genreId != -1){
                    genreIds.append("<").append(genreId).append(">");
                }
                currentName = new StringBuilder();
            }else{
                currentName.append(genreNamesRaw.charAt(charPositon));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentName);
                }
            }
            charPositon++;
        }
        String.valueOf(genreNamesRaw.charAt(1));
        LOGGER.info("Genre ids: " + genreIds);
        return genreIds.toString();
    }

    /**
     * Converts the input string to an array list containing the elements of the string. Input string formatting: <s1><s2><s3>. The content between the <> is added to the array list.
     * @param string Input string
     * @return Returns an array list containing the elements of string
     */
    public static ArrayList<String> getEntriesFromString(String string){
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder currentEntry = new StringBuilder();
        for(Character character : string.toCharArray()){
            if(character.equals("<")){
                //Nothing happens
            }else if(character.equals(">")){
                arrayList.add(currentEntry.toString());
                currentEntry = new StringBuilder();
            }else{
                currentEntry.append(character);
            }
        }
        return arrayList;
    }
}
