package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AnalyzeExistingGenres {
    public static final File FILE_GENRES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
    public static List<Map<String, String>> genreList;
    public static int maxGenreID = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGenres.class);

    public static void analyzeGenreFile() throws IOException {
        genreList = DataStreamHelper.parseDataFile(Utils.getGenreFile());
        int currentMaxGenreId = 0;
        for (Map<String, String> map : genreList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    int currentId = Integer.parseInt(entry.getValue());
                    if (currentMaxGenreId < currentId) {
                        currentMaxGenreId = currentId;
                    }
                }
            }
        }
        maxGenreID = currentMaxGenreId;
        writeHelpFile();
        LOGGER.info("MaxGenreID: " + maxGenreID);
    }

    /**
     * @return Returns a array list containing all genre ids that have been found.
     */
    public static ArrayList<Integer> getGenreIdsInUse(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(Map<String, String> map : genreList){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("ID")){
                    arrayList.add(Integer.parseInt(entry.getValue()));
                }
            }
        }
        return arrayList;
    }

    /**
     * @return Returns an ArrayList containing all genre names that are in use.
     */
    public static ArrayList<String> getGenreNamesInUse(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(Map<String, String> map : genreList){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayList.add(entry.getValue());
                }
            }
        }
        return arrayList;
    }

    /**
     * @return Returns the next free genre id.
     */
    public static int getFreeGenreID(){
        return maxGenreID+1;
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
        for(Map<String, String> map : genreList){
            int currentId = 0;
            String currentName = "";
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("ID")){
                    currentId = Integer.parseInt(entry.getValue());
                }
                if(entry.getKey().equals("NAME EN")){
                    currentName = entry.getValue();
                }
            }
            pw.print(currentId + " - " + currentName);
            pw.print(System.getProperty("line.separator"));
        }
        pw.close();

        if(Settings.enableDebugLogging){
            LOGGER.info("file created.");
        }
    }

    /**
     * @return Returns a string containing all genre names sorted by alphabet
     */
    public static String[] getGenresByAlphabetWithoutId(){
        ArrayList<String> genresByAlphabet = new ArrayList<>();
        for(Map<String, String> map : AnalyzeExistingGenres.genreList){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    genresByAlphabet.add(entry.getValue());
                }
            }
        }
        Collections.sort(genresByAlphabet);
        String[] string = new String[genresByAlphabet.size()];
        genresByAlphabet.toArray(string);
        return string;
    }

    public static String[] getCustomGenresByAlphabetWithoutId(){
        String[] allGenresById = getGenresByAlphabetWithoutId();
        String[] defaultGenres = {"Action", "Adventure", "Building Game", "Economic Simulation", "Fighting Game", "First-Person Shooter", "Interactive Movie", "Platformer", "Puzzle Game", "Racing", "Real-Time Strategy", "Role-Playing Game", "Simulation", "Skill Game", "Sport Game", "Strategy", "Third-Person Shooter", "Visual Novel"};
        ArrayList<String> arrayListCustomGenres = new ArrayList<>();
        for (String s : allGenresById) {
            boolean defaultGenre = false;
            for (String genre : defaultGenres) {
                if (s.equals(genre)) {
                    defaultGenre = true;
                    break;
                }
            }
            if (!defaultGenre) {
                arrayListCustomGenres.add(s);
            }
        }
        String[] string = new String[arrayListCustomGenres.size()];
        arrayListCustomGenres.toArray(string);
        return string;
    }

    /**
     * @param id The id
     * @return Returns the specified genre name by id.
     * @throws ArrayIndexOutOfBoundsException Is thrown when the requested genre id does not exist in the map.
     */
    public static String getGenreNameById(int id) throws ArrayIndexOutOfBoundsException{
        return getGenreNamesInUse().get(id);
    }

    /**
     * Returns -1 when genre name does not exist.
     * @param genreName The genre name
     * @return Returns the genre id for the specified name.
     */
    public static int getGenreIdByName(String genreName){
        int genreId = -1;
        for(Map<String, String> map : genreList){
            if(map.get("NAME EN").equals(genreName)){
                genreId = Integer.parseInt(map.get("ID"));
            }
        }
        if(genreId == -1){
            LOGGER.info("Genre [" + genreName + "] does not exist");
        }else{
            LOGGER.info("Genre [" + genreName + "] has been found. Id: " + genreId);
        }
        return genreId;
    }
}
