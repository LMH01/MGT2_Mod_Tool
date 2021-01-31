package com.github.lmh01.mgt2mt.util;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;

public class Utils {

    //These are the files inside the mgt2 file structure that are used inside this tool.
    public static File fileGenres = new File(Utils.getMGT2DataPath() + "\\Genres.txt");
    public static File fileNpcGames = new File(Utils.getMGT2DataPath() + "\\NpcGames.txt");

    /**
     * @return returns the current date time in format: YYYY-MM-DD-HH-MM
     */
    public static String getCurrentDateTime(){
        String currentDateTime = LocalDateTime.now().getYear() + "-" +
                LocalDateTime.now().getMonth() + "-"+
                LocalDateTime.now().getDayOfMonth() + "-" +
                LocalDateTime.now().getHour() + "-" +
                LocalDateTime.now().getMinute();
        return currentDateTime;
    }
    /**
     * @return Returns the path to \Mad Games Tycoon 2_Data\Extern\Text\DATA\
     */
    public static String getMGT2DataPath(){
        return Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\";
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
            // 1 = Used when AnalyzeExistingGenres.analyzeGenreFile() throws an exception.
            case 1: JOptionPane.showMessageDialog(null, "The Genres.txt file could not be analyzed.\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Opens a message dialog with a specified error message.
     * @param confirmMessageKey the confirm message key. see this functions for meanings
     * @param e The exception
     * @return Returns true when the user clicks yes. Returns false when the user clicks no.
     */
    public static boolean showConfirmDialog(int confirmMessageKey, Exception e){
        if(confirmMessageKey == 1){
            if(JOptionPane.showConfirmDialog(null, "The backup could not be created.\n\nException:\n" + e.getMessage() + "\nDo you want to continue anyway?", "Unable to backup file", JOptionPane.YES_NO_OPTION) == 0){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }
}
