package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AnalyzeExistingGenres {
    public static final ArrayList<Integer> ARRAY_LIST_GENRE_IDS_IN_USE = new ArrayList<>();
    public static final ArrayList<String> ARRAY_LIST_GENRE_NAMES_IN_USE = new ArrayList<>();
    public static final ArrayList<String> ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED = new ArrayList<>();
    public static final File FILE_GENRES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
    public static ArrayList<String> arrayListGenreNamesSorted = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGenres.class);

    /**
     * Analyzes the Genres.txt file.
     */
    public static void analyzeGenreFile() throws IOException {
        ARRAY_LIST_GENRE_IDS_IN_USE.clear();
        ARRAY_LIST_GENRE_NAMES_IN_USE.clear();
        arrayListGenreNamesSorted.clear();
        ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.clear();

        File genresFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");
        LOGGER.info("Scanning for genre id's and names in file: " + genresFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(genresFile), StandardCharsets.UTF_8));
        int currentID;
        String currentIDAsString;
        String currentLine;
        while((currentLine = reader.readLine()) != null){
            if(currentLine.contains("[ID]")){
                currentIDAsString = currentLine;
                //noinspection SpellCheckingInspection
                currentID = Integer.parseInt(currentIDAsString.replace("[ID]", "").replaceAll("\\uFEFF", ""));//replaceAll("\\uFEFF", "") is used for the correct formatting
                if(Settings.enableDebugLogging){
                    LOGGER.info("found id: " + currentID);}
                ARRAY_LIST_GENRE_IDS_IN_USE.add(currentID);
            }else if(currentLine.contains("[NAME EN]")){
                String currentName = currentLine.replace("[NAME EN]", "");
                ARRAY_LIST_GENRE_NAMES_IN_USE.add(currentName);
                if(Settings.enableDebugLogging){
                    LOGGER.info("found name: " + currentName);}
            }
        }
        reader.close();
        LOGGER.info("Analyzing of genre ids and names complete. Found: " + ARRAY_LIST_GENRE_IDS_IN_USE.size());
        if(Settings.enableDebugLogging){
            LOGGER.info("Writing to file: " + Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
        }
        writeHelpFile();
        fillGenresByIdListSorted();
        sortGenreNames();
    }

    /**
     * Writes a help file with genres by id.
     */
    private static void writeHelpFile() throws IOException {
        if(FILE_GENRES_BY_ID_HELP.exists()){
            FILE_GENRES_BY_ID_HELP.delete();
        }
        FILE_GENRES_BY_ID_HELP.createNewFile();
        PrintWriter pw = new PrintWriter(FILE_GENRES_BY_ID_HELP);
        for(int i = 0; i< ARRAY_LIST_GENRE_IDS_IN_USE.size(); i++){
            pw.print(ARRAY_LIST_GENRE_IDS_IN_USE.get(i) + " - " + ARRAY_LIST_GENRE_NAMES_IN_USE.get(i) + "\n");
        }
        pw.close();
        if(Settings.enableDebugLogging){
            LOGGER.info("file created.");
        }
    }
    private static void fillGenresByIdListSorted(){
        for(int i = 0; i< ARRAY_LIST_GENRE_IDS_IN_USE.size(); i++){
            ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.add(ARRAY_LIST_GENRE_NAMES_IN_USE.get(i) + " - " + ARRAY_LIST_GENRE_IDS_IN_USE.get(i));
        }
        Collections.sort(ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED);
    }
    public static String[] getGenresByAlphabetWithoutId(){
        ArrayList<String> arrayListAvailableGenreNamesSorted = AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE;
        Collections.sort(arrayListAvailableGenreNamesSorted);
        ArrayList<String> arrayListAvailableGenreNamesToDisplay = new ArrayList<>();
        for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size(); i++){
            arrayListAvailableGenreNamesToDisplay.add(
                    AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i)
                            .replace(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i)
                                    .replace(arrayListAvailableGenreNamesSorted.get(i), ""), ""));
        }
        String[] string = new String[arrayListAvailableGenreNamesToDisplay.size()];
        arrayListAvailableGenreNamesToDisplay.toArray(string);
        return string;
    }

    private static void sortGenreNames(){
        arrayListGenreNamesSorted = ARRAY_LIST_GENRE_NAMES_IN_USE;
        Collections.sort(arrayListGenreNamesSorted);
    }
}
