package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class AnalyzeExistingGenres {
    public static ArrayList<Integer> genreIDsInUse = new ArrayList<>();
    public static ArrayList<String> genreNamesInUse = new ArrayList<>();
    public static ArrayList<String> genreNamesByIdSorted = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(AnalyzeExistingGenres.class);
    public static void analyzeExistingGenres(){
        genreIDsInUse.clear();
        genreNamesInUse.clear();
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
                    genreIDsInUse.add(currentID);
                    nameAdded = false;
                }else if(currentLine.contains("[NAME EN]")){
                    //TODO Remove this if clause and the nameAdded variable
                    if(!nameAdded){
                        String currentName = currentLine.replace("[NAME EN]", "");
                        genreNamesInUse.add(currentName);
                        if(Settings.enableDebugLogging){logger.info("found name: " + currentName);}
                        nameAdded = true;
                    }
                }
            }
            inputStreamReader.close();
            reader.close();
            logger.info("Analyzing of genre ids and names complete. Found: " + genreIDsInUse.size());
            if(Settings.enableDebugLogging){logger.info("Writing to file: " + Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");}
            writeHelpFile();
            fillGenresByIdListSorted();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            for(int i = 0; i<genreIDsInUse.size(); i++){
                pw.print(genreIDsInUse.get(i) + " - " + genreNamesInUse.get(i) + "\n");
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
        for(int i = 0; i<genreIDsInUse.size(); i++){
            genreNamesByIdSorted.add(genreNamesInUse.get(i) + " - " + genreIDsInUse.get(i));
        }
        Collections.sort(genreNamesByIdSorted);
    }
    public static String[] getGenresByAlphabetWithoutID(){
        ArrayList<String> arrayListAvailableGenreNamesSorted = AnalyzeExistingGenres.genreNamesInUse;
        Collections.sort(arrayListAvailableGenreNamesSorted);
        ArrayList<String> arrayListAvailableGenreNamesToDisplay = new ArrayList<>();
        for(int i = 0; i<AnalyzeExistingGenres.genreIDsInUse.size(); i++){
            arrayListAvailableGenreNamesToDisplay.add(
                    AnalyzeExistingGenres.genreNamesByIdSorted.get(i)
                            .replace(AnalyzeExistingGenres.genreNamesByIdSorted.get(i)
                                    .replace(arrayListAvailableGenreNamesSorted.get(i), ""), ""));
        }
        String[] string = new String[arrayListAvailableGenreNamesToDisplay.size()];
        arrayListAvailableGenreNamesToDisplay.toArray(string);
        return string;
    }
}
