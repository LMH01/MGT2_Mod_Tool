package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class AnalyzeExistingGenres {
    public static ArrayList<Integer> arrayListGenreIDsInUse = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesInUse = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesByIdSorted = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesSorted = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(AnalyzeExistingGenres.class);

    /**
     *
     * @return Returns true when the Genres.txt file has been analyzed successfully. When an exception occurs it will return false.
     */
    public static boolean analyzeExistingGenres(){
        arrayListGenreIDsInUse.clear();
        arrayListGenreNamesInUse.clear();
        arrayListGenreNamesSorted.clear();
        arrayListGenreNamesByIdSorted.clear();
        try {
            File genresFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");
            logger.info("Scanning for genre id's and names in file: " + genresFile);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(genresFile), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            int currentID;
            String currentIDAsString;
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                boolean nameAdded = false;
                if(currentLine.contains("[ID]")){
                    currentIDAsString = currentLine;
                    currentID = Integer.parseInt(currentIDAsString.replace("[ID]", ""));
                    if(Settings.enableDebugLogging){logger.info("found id: " + currentID);}
                    arrayListGenreIDsInUse.add(currentID);
                    nameAdded = false;
                }else if(currentLine.contains("[NAME EN]")){
                    //TODO Remove this if clause and the nameAdded variable
                    if(!nameAdded){
                        String currentName = currentLine.replace("[NAME EN]", "");
                        arrayListGenreNamesInUse.add(currentName);
                        if(Settings.enableDebugLogging){logger.info("found name: " + currentName);}
                        nameAdded = true;
                    }
                }
            }
            inputStreamReader.close();
            reader.close();
            logger.info("Analyzing of genre ids and names complete. Found: " + arrayListGenreIDsInUse.size());
            if(Settings.enableDebugLogging){logger.info("Writing to file: " + Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");}
            writeHelpFile();
            fillGenresByIdListSorted();
            sortGenreNames();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to continue:\nError while accessing Genres.txt file.\nThe file does not exist.\nPlease check in the settings if your MGT2 path is set correctly.", "Unable to access Genres.txt", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to continue:\nError while accessing Genres.txt file.\nPlease try again with administrator rights.", "Unable to access Genres.txt", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Writes a help file with genres by id.
     */
    private static void writeHelpFile(){
        try {
            File file = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            for(int i = 0; i< arrayListGenreIDsInUse.size(); i++){
                pw.print(arrayListGenreIDsInUse.get(i) + " - " + arrayListGenreNamesInUse.get(i) + "\n");
            }
            pw.close();
            logger.info("file created.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void fillGenresByIdListSorted(){
        for(int i = 0; i< arrayListGenreIDsInUse.size(); i++){
            arrayListGenreNamesByIdSorted.add(arrayListGenreNamesInUse.get(i) + " - " + arrayListGenreIDsInUse.get(i));
        }
        Collections.sort(arrayListGenreNamesByIdSorted);
    }
    public static String[] getGenresByAlphabetWithoutID(){
        ArrayList<String> arrayListAvailableGenreNamesSorted = AnalyzeExistingGenres.arrayListGenreNamesInUse;
        Collections.sort(arrayListAvailableGenreNamesSorted);
        ArrayList<String> arrayListAvailableGenreNamesToDisplay = new ArrayList<>();
        for(int i = 0; i<AnalyzeExistingGenres.arrayListGenreIDsInUse.size(); i++){
            arrayListAvailableGenreNamesToDisplay.add(
                    AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i)
                            .replace(AnalyzeExistingGenres.arrayListGenreNamesByIdSorted.get(i)
                                    .replace(arrayListAvailableGenreNamesSorted.get(i), ""), ""));
        }
        String[] string = new String[arrayListAvailableGenreNamesToDisplay.size()];
        arrayListAvailableGenreNamesToDisplay.toArray(string);
        return string;
    }
    private static void sortGenreNames(){
        arrayListGenreNamesSorted = arrayListGenreNamesInUse;
        Collections.sort(arrayListGenreNamesSorted);
    }
}
