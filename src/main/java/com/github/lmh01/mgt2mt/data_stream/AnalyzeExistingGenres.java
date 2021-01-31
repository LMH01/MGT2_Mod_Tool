package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class AnalyzeExistingGenres {
    public static ArrayList<Integer> arrayListGenreIDsInUse = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesInUse = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesByIdSorted = new ArrayList<>();
    public static ArrayList<String> arrayListGenreNamesSorted = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(AnalyzeExistingGenres.class);

    /**
     * Analyzes the Genres.txt file.
     * @return Returns true when the Genres.txt file has been analyzed successfully. When an exception occurs it will return false.
     */
    public static void analyzeGenreFile() throws IOException {
        arrayListGenreIDsInUse.clear();
        arrayListGenreNamesInUse.clear();
        arrayListGenreNamesSorted.clear();
        arrayListGenreNamesByIdSorted.clear();

        File genresFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");
        logger.info("Scanning for genre id's and names in file: " + genresFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(genresFile), StandardCharsets.UTF_8));
        int currentID;
        String currentIDAsString;
        String currentLine;
        while((currentLine = reader.readLine()) != null){
            if(currentLine.contains("[ID]")){
                currentIDAsString = currentLine;
                currentID = Integer.parseInt(currentIDAsString.replace("[ID]", "").replaceAll("\\uFEFF", ""));//replaceAll("\\uFEFF", "") is used for the correct formatting
                if(Settings.enableDebugLogging){logger.info("found id: " + currentID);}
                arrayListGenreIDsInUse.add(currentID);
            }else if(currentLine.contains("[NAME EN]")){
                String currentName = currentLine.replace("[NAME EN]", "");
                arrayListGenreNamesInUse.add(currentName);
                if(Settings.enableDebugLogging){logger.info("found name: " + currentName);}
            }
        }
        reader.close();
        logger.info("Analyzing of genre ids and names complete. Found: " + arrayListGenreIDsInUse.size());
        if(Settings.enableDebugLogging){logger.info("Writing to file: " + Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");}
        writeHelpFile();
        fillGenresByIdListSorted();
        sortGenreNames();;
    }

    /**
     * Writes a help file with genres by id.
     */
    private static void writeHelpFile() throws IOException {
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
    }
    private static void fillGenresByIdListSorted(){
        for(int i = 0; i< arrayListGenreIDsInUse.size(); i++){
            arrayListGenreNamesByIdSorted.add(arrayListGenreNamesInUse.get(i) + " - " + arrayListGenreIDsInUse.get(i));
        }
        Collections.sort(arrayListGenreNamesByIdSorted);
    }
    public static String[] getGenresByAlphabetWithoutId(){
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
