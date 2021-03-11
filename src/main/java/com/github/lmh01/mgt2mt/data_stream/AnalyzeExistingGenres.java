package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AnalyzeExistingGenres {
    public static final ArrayList<String> ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED = new ArrayList<>();
    public static final ArrayList<String> ARRAY_LIST_GENRE_NAMES_SORTED = new ArrayList<>();
    public static final File FILE_GENRES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
    public static List<Map<String, String>> genreList;
    public static int maxGenreID = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGenres.class);

    public static void analyzeGenreFile() throws IOException {
        genreList = Utils.parseDataFile(Utils.getGenreFile());
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
        LOGGER.info("MaxGenreID: " + maxGenreID);
    }

    /**
     * @return Returns a array list containing all genre ids that have been found.
     */
    public static ArrayList<Integer> getGenreIdsInUse(){
        ArrayList arrayList = new ArrayList();
        for(Map<String, String> map : genreList){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("ID")){
                    arrayList.add(entry.getValue());
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
     * Analyzes the Genres.txt file.
     */
    @Deprecated
    public static void analyzeGenreFileDeprecated() throws IOException {
        //TODO Delete unnecessary ArrayLists and use Maps instead.
        //ARRAY_LIST_GENRE_IDS_IN_USE.clear();
        //ARRAY_LIST_GENRE_NAMES_IN_USE.clear();
        ARRAY_LIST_GENRE_NAMES_SORTED.clear();

        File genresFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");
        LOGGER.info("Scanning for genre id's and names in file: " + genresFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(genresFile), StandardCharsets.UTF_8));
        int currentID;
        String currentIDAsString;
        String currentLine;
        while((currentLine = reader.readLine()) != null){
            if(currentLine.contains("[ID]")){
                currentIDAsString = currentLine;
                currentID = Integer.parseInt(currentIDAsString.replace("[ID]", "").replaceAll("\\uFEFF", ""));//replaceAll("\\uFEFF", "") is used for the correct formatting
                if(Settings.enableDebugLogging){
                    LOGGER.info("found id: " + currentID);}
                //ARRAY_LIST_GENRE_IDS_IN_USE.add(currentID);
            }else if(currentLine.contains("[NAME EN]")){
                String currentName = currentLine.replace("[NAME EN]", "");
                //ARRAY_LIST_GENRE_NAMES_IN_USE.add(currentName);
                if(Settings.enableDebugLogging){
                    LOGGER.info("found name: " + currentName);}
            }
        }
        reader.close();
        //LOGGER.info("Analyzing of genre ids and names complete. Found: " + ARRAY_LIST_GENRE_IDS_IN_USE.size());
        if(Settings.enableDebugLogging){
            LOGGER.info("Writing to file: " + Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentGenreIDsByName.txt");
        }
        writeHelpFile();
        fillGenresByIdListSorted();
    }

    /**
     * @return Returns the next free genre id.
     */
    public static int getFreeGenreID(){
        return maxGenreID+1;
    }

    /**
     * Searches the Genres.txt file for the input genre id. If found the specifications for the genre will be written in the "MAP_SINGLE_GENRE" to be used later when the genre is being exported.
     * @param genreId The genre id for the genre that should be analyzed
     * @return Returns true when the specified genre has been found. Returns false when the genre id does not exist.
     */
    public static boolean analyzeSingleGenre(int genreId) throws IOException {
        LOGGER.info("Analyzing genre that should be exported...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));
        String currentLine;
        boolean analyzingGenre = false; //Is set to true when the genre that should be exported is being analyzed. Is set to false when genre has been analyzed.
        boolean genreFound = false;
        int linesToScan = Utils.genreLineNumbers+1;
        while((currentLine = reader.readLine()) != null){
            if(currentLine.contains("[ID]")){
                if(Integer.toString(genreId).equals(currentLine.replace("[ID]", "").replaceAll("\\uFEFF", ""))){
                    LOGGER.info("The genre that should be exported has been found.");
                    GenreManager.MAP_SINGLE_GENRE.put("id", Integer.toString(genreId));
                    analyzingGenre = true;
                    genreFound = true;
                }
            }
            if(analyzingGenre && linesToScan>0){
                if(Settings.enableDebugLogging){
                    LOGGER.info("Current line: " + currentLine);
                }
                if(currentLine.contains("[NAME AR]")){//TODO Rewrite to use the TranslationsString[] and make method more compact
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME AR]", currentLine.replace("[NAME AR]", ""));
                }else if(currentLine.contains("[NAME CH]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME CH]", currentLine.replace("[NAME CH]", ""));
                }else if(currentLine.contains("[NAME CT]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME CT]", currentLine.replace("[NAME CT]", ""));
                }else if(currentLine.contains("[NAME CZ]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME CZ]", currentLine.replace("[NAME CZ]", ""));
                }else if(currentLine.contains("[NAME EN]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME EN]", currentLine.replace("[NAME EN]", ""));
                }else if(currentLine.contains("[NAME ES]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME ES]", currentLine.replace("[NAME ES]", ""));
                }else if(currentLine.contains("[NAME FR]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME FR]", currentLine.replace("[NAME FR]", ""));
                }else if(currentLine.contains("[NAME GE]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME GE]", currentLine.replace("[NAME GE]", ""));
                }else if(currentLine.contains("[NAME HU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME HU]", currentLine.replace("[NAME HU]", ""));
                }else if(currentLine.contains("[NAME IT]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME IT]", currentLine.replace("[NAME IT]", ""));
                }else if(currentLine.contains("[NAME KO]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME KO]", currentLine.replace("[NAME KO]", ""));
                }else if(currentLine.contains("[NAME PB]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME PB]", currentLine.replace("[NAME PB]", ""));
                }else if(currentLine.contains("[NAME PL]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME PL]", currentLine.replace("[NAME PL]", ""));
                }else if(currentLine.contains("[NAME RU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME RU]", currentLine.replace("[NAME RU]", ""));
                }else if(currentLine.contains("[NAME TU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[NAME TU]", currentLine.replace("[NAME TU]", ""));
                }else if(currentLine.contains("[DESC AR]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC AR]", currentLine.replace("[DESC AR]", ""));
                }else if(currentLine.contains("[DESC CH]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC CH]", currentLine.replace("[DESC CH]", ""));
                }else if(currentLine.contains("[DESC CT]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC CT]", currentLine.replace("[DESC CT]", ""));
                }else if(currentLine.contains("[DESC CZ]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC CZ]", currentLine.replace("[DESC CZ]", ""));
                }else if(currentLine.contains("[DESC EN]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC EN]", currentLine.replace("[DESC EN]", ""));
                }else if(currentLine.contains("[DESC ES]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC ES]", currentLine.replace("[DESC ES]", ""));
                }else if(currentLine.contains("[DESC FR]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC FR]", currentLine.replace("[DESC FR]", ""));
                }else if(currentLine.contains("[DESC GE]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC GE]", currentLine.replace("[DESC GE]", ""));
                }else if(currentLine.contains("[DESC HU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC HU]", currentLine.replace("[DESC HU]", ""));
                }else if(currentLine.contains("[DESC IT]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC IT]", currentLine.replace("[DESC IT]", ""));
                }else if(currentLine.contains("[DESC KO]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC KO]", currentLine.replace("[DESC KO]", ""));
                }else if(currentLine.contains("[DESC PB]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC PB]", currentLine.replace("[DESC PB]", ""));
                }else if(currentLine.contains("[DESC PL]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC PL]", currentLine.replace("[DESC PL]", ""));
                }else if(currentLine.contains("[DESC RU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC RU]", currentLine.replace("[DESC RU]", ""));
                }else if(currentLine.contains("[DESC TU]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESC TU]", currentLine.replace("[DESC TU]", ""));
                }else if(currentLine.contains("[DATE]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DATE]", currentLine.replace("[DATE]", ""));
                }else if(currentLine.contains("[RES POINTS]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[RES POINTS]", currentLine.replace("[RES POINTS]", ""));
                }else if(currentLine.contains("[PRICE]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[PRICE]", currentLine.replace("[PRICE]", ""));
                }else if(currentLine.contains("[DEV COSTS]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DEV COSTS]", currentLine.replace("[DEV COSTS]", ""));
                }else if(currentLine.contains("[PIC]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[PIC]", currentLine.replace("[PIC]", ""));
                }else if(currentLine.contains("[TGROUP]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[TGROUP]", currentLine.replace("[TGROUP]", ""));
                }else if(currentLine.contains("[GAMEPLAY]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[GAMEPLAY]", currentLine.replace("[GAMEPLAY]", ""));
                }else if(currentLine.contains("[GRAPHIC]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[GRAPHIC]", currentLine.replace("[GRAPHIC]", ""));
                }else if(currentLine.contains("[SOUND]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[SOUND]", currentLine.replace("[SOUND]", ""));
                }else if(currentLine.contains("[CONTROL]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[CONTROL]", currentLine.replace("[CONTROL]", ""));
                }else if(currentLine.contains("[GENRE COMB]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[GENRE COMB]", currentLine.replace("[GENRE COMB]", ""));
                }else if(currentLine.contains("[DESIGN1]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESIGN1]", currentLine.replace("[DESIGN1]", ""));
                }else if(currentLine.contains("[DESIGN2]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESIGN2]", currentLine.replace("[DESIGN2]", ""));
                }else if(currentLine.contains("[DESIGN3]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESIGN3]", currentLine.replace("[DESIGN3]", ""));
                }else if(currentLine.contains("[DESIGN4]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESIGN4]", currentLine.replace("[DESIGN4]", ""));
                }else if(currentLine.contains("[DESIGN5]")){
                    GenreManager.MAP_SINGLE_GENRE.put("[DESIGN5]", currentLine.replace("[DESIGN5]", ""));
                }
                linesToScan--;
            }
        }
        reader.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemesGeFile()), StandardCharsets.UTF_16LE));
        boolean firstLine = true;
        int lineNumber = 1;
        StringBuilder compatibleThemes = new StringBuilder();
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
        GenreManager.MAP_SINGLE_GENRE.put("[THEME COMB]", compatibleThemes.toString());
        return genreFound;
    }

    /**
     * Writes a help file with genres by id.
     */
    private static void writeHelpFile() throws IOException {
        //TODO Rework when genreListMap works
        /*if(FILE_GENRES_BY_ID_HELP.exists()){
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
        }*/
    }
    private static void fillGenresByIdListSorted(){
        //TODO Rewrite to public static ArrayList getGenresByIdSorted -> Should use genreListMap then.
        /*for(int i = 0; i< ARRAY_LIST_GENRE_IDS_IN_USE.size(); i++){
            ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.add(ARRAY_LIST_GENRE_NAMES_IN_USE.get(i) + " - " + ARRAY_LIST_GENRE_IDS_IN_USE.get(i));
        }
        Collections.sort(ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED);*/
    }
    public static String[] getGenresByAlphabetWithoutId(){
        //TODO Rewrite when genreMapList is implemented
        /*ArrayList<String> arrayListAvailableGenreNamesSorted = AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE;
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
        return string;*/
        return null;
    }

    public static String[] getCustomGenresByAlphabetWithoutId(){
        String[] allGenresById = getGenresByAlphabetWithoutId();
        String[] defaultGenres = {"Action", "Adventure", "Building Game", "Economic Simulation", "Fighting Game", "First-Person Shooter", "Interactive Movie", "Platformer", "Puzzle Game", "Racing", "Real-Time Strategy", "Role-Playing Game", "Simulation", "Skill Game", "Sport Game", "Strategy", "Third-Person Shooter", "Visual Novel"};
        ArrayList<String> arrayListCustomGenres = new ArrayList<>();
        for (String s : allGenresById) {
            boolean defaultGenre = false;
            for (int x = 0; x < defaultGenres.length; x++) {
                if (s.equals(defaultGenres[x])) {
                    defaultGenre = true;
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

    public static ArrayList<String> getSortedGenreNames(){
        ArrayList<String> arrayListSortedGenreNames = getSortedGenreNames();
        Collections.sort(arrayListSortedGenreNames);
        return arrayListSortedGenreNames;
    }

    /**
     * @param id The id
     * @return Returns the specified genre name by id.
     */
    public static String getGenreNameById(int id){
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
