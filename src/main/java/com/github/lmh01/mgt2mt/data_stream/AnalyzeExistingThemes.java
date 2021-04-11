package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AnalyzeExistingThemes {

    public static final HashMap<Integer,String> MAP_ACTIVE_THEMES_GE = new HashMap<>();
    public static final HashMap<Integer,String> MAP_ACTIVE_THEMES_EN = new HashMap<>();
    public static final String[] DEFAULT_THEMES = {"Agents", "Agriculture", "Airplanes", "Aliens", "American Football", "Angel", "Animals", "Anime", "Art", "Assassins", "Barbarians", "Baseball", "Basketball", "Bicycle", "Birds", "Bounty Hunter", "Boxing", "Building blocks", "Bunker", "Cabs", "Candy", "Cars", "Casinos", "Castles", "Cats", "Chemistry", "Cinema", "Cities", "Colonization", "Comedy", "Companies", "Conquest", "Conspiracies", "Contract killer", "Cooking", "Cowboys", "Cricket", "Crime", "Crocodiles", "Cyberpunk", "Cyberspace", "Cyborgs", "Dancing", "Dating", "Demons", "Detectives", "Devils", "Digging", "Dinosaurs", "Diving", "Doctors", "Dogs", "Dragons", "Drawing", "Dreams", "Druids", "Dungeons", "Dwarfs", "Economy", "Education", "Elements", "Elves", "End Time", "Erotica", "Espionage", "Everyday life", "Evolution", "Extreme sports", "Fairies", "Fantasy", "Fashion", "Fire Department", "Fishes", "Fitness", "Food", "Frogs", "Fruits", "Gambling", "Game development", "Gangsters", "Ghosts", "Goats", "Gods", "Golf", "Hacking", "Helicopters", "Hell", "Historical", "Horror", "Horses", "Hospital", "Hunting", "Ice Age", "Ice Hockey", "Industrialization", "Insects", "Islands", "Karate", "Kids", "Knights", "Mafia", "Martial arts", "Mathematics", "Mecha", "Mercenary", "Middle Ages", "Military", "Monkeys", "Monster", "Motorcycles", "Motorsports", "Movies", "Mushrooms", "Music", "Ninjas", "Octopuses", "Orcs", "Paintball", "Parallel worlds", "Pets", "Physics", "Pirates", "Planets", "Plants", "Plumber", "Police", "Politics", "Portals", "Predators", "Prison", "Puzzles", "Quiz show", "Rabbits", "Radioactivity", "Religion", "Reporters", "Revolutions", "Robots", "Rockstars", "Romans", "Rugby", "Samurai", "Sandbox", "School", "Science", "Sheep", "Ships", "Singing", "Skeletons", "Soccer", "Space", "Space stations", "Spaceships", "Special Forces", "Sports", "Squirrels", "Steampunk", "Stone Age", "Stones", "Submarines", "Superheroes", "Survival", "Tanks", "Television", "Tennis", "Theme Parks", "Thieves", "Time Travel", "Toys", "Trains", "Transportation", "Treasure hunters", "Treasures", "Trolls", "Trucks", "UFOs", "Vacation", "Vampires", "Vikings", "Viruses", "Werewolves", "Wild West", "Witches", "Wizards", "World Wars", "Worms", "Wrestling", "Zombies", "Zoo", "Fishing", "Apocalypse", "Parlor games", "Swimming", "Civilizations", "Factories", "Saboteurs", "Renovate", "Noble houses", "Mining", "Healthcare", "Archaeology", "Chickens", "Expeditions", "Pinball", "Androids", "Super villains", "Riding", "Playing cards", "Skydiving", "Climbing", "Jet ski", "Skateboards", "Handball", "Table tennis", "Sailing"};
    public static final File FILE_THEMES_BY_ID_HELP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\CurrentThemesByID.txt");
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingThemes.class);

    //The theme combinations are only changed in the Themes_GE.txt file
    public static void analyzeThemeFiles() throws IOException {
        analyzeThemesFileGE();
        analyzeThemesFileEN();
        writeHelpFile();
    }

    private static void analyzeThemesFileGE() throws IOException {
        MAP_ACTIVE_THEMES_GE.clear();
        File themesGeFile = Utils.getThemesGeFile();
        LOGGER.info("Scanning for themes in file: " + themesGeFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(themesGeFile), StandardCharsets.UTF_16LE));
        String currentLine;
        boolean firstLine = true;
        int themeID = 0;//Theme id is added to make the sorting and selection of the compatible theme easier
        while((currentLine = reader.readLine()) != null){
            if(firstLine){
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(!currentLine.equals("")){
                MAP_ACTIVE_THEMES_GE.put(themeID, currentLine);
                if(Settings.enableDebugLogging){
                    LOGGER.info("Added entry to array map MAP_ACTIVE_THEMES_GE: " + "[" + themeID + "] " + currentLine);
                    LOGGER.info("Entry in map: " + MAP_ACTIVE_THEMES_GE.get(themeID));
                }
                themeID++;
            }
        }
        reader.close();
        LOGGER.info("Analyzing of themes(en) complete. Found: " + MAP_ACTIVE_THEMES_GE.size());
    }

    private static void analyzeThemesFileEN() throws IOException {
        MAP_ACTIVE_THEMES_EN.clear();
        File themesEnFile = Utils.getThemesEnFile();
        LOGGER.info("Scanning for themes in file: " + themesEnFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(themesEnFile), StandardCharsets.UTF_16LE));
        String currentLine;
        boolean firstLine = true;
        int themeID = 0;//Theme id is added to make the sorting and selection of the compatible theme easier
        while((currentLine = reader.readLine()) != null){
            if(firstLine){
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(!currentLine.equals("")){
                MAP_ACTIVE_THEMES_EN.put(themeID, currentLine);
                if(Settings.enableDebugLogging){
                    LOGGER.info("Added entry to array map MAP_ACTIVE_THEMES_EN: " + "[" + themeID + "] " + currentLine);
                    LOGGER.info("Entry in map: " + MAP_ACTIVE_THEMES_EN.get(themeID));
                }
                themeID++;
            }
        }
        reader.close();
        LOGGER.info("Analyzing of themes(en) complete. Found: " + MAP_ACTIVE_THEMES_EN.size());
    }

    /**
     * @param themeNameEn The theme name that should be searched.
     * @return Returns the position of the specified genre in the themesNamesEn file.
     */
    public static int getPositionOfThemeInFile(String themeNameEn){
        int position = 1;
        if(Settings.enableDebugLogging){
            LOGGER.info("01 - MAP_ACTIVE_THEMES_EN.size(): " + MAP_ACTIVE_THEMES_EN.size());
        }
        for(Map.Entry<Integer, String> entry: MAP_ACTIVE_THEMES_EN.entrySet()){
            if(Settings.enableDebugLogging){
                LOGGER.info("Value: " + entry.getValue());
            }
            if(entry.getValue().contains(themeNameEn)){
                return position;
            }else{
                position++;
            }
        }
        return position;
    }

    /**
     * Writes a help file with themes by id.
     */
    private static void writeHelpFile() throws IOException {
        LOGGER.info("Writing theme id help file.");
        if(FILE_THEMES_BY_ID_HELP.exists()){
            FILE_THEMES_BY_ID_HELP.delete();
        }
        FILE_THEMES_BY_ID_HELP.createNewFile();
        PrintWriter pw = new PrintWriter(FILE_THEMES_BY_ID_HELP);
        boolean firstLine = true;
        for(Map.Entry<Integer, String> entry : MAP_ACTIVE_THEMES_EN.entrySet()){
            if(firstLine){
                pw.print(entry.getKey() + " - " + entry.getValue());
                firstLine = false;
            }else{
                pw.print("\n" + entry.getKey() + " - " + entry.getValue());
            }
            if(Settings.enableDebugLogging){
                LOGGER.info("Current entryKey: " + entry.getKey());
                LOGGER.info(entry.getKey() + " - " + entry.getValue());
            }
        }
        pw.close();
        if(Settings.enableDebugLogging){
            LOGGER.info("file created.");
        }
    }

    /**
     * @return Returns a string containing all active themes sorted by alphabet.
     */
    public static String[] getThemesByAlphabet(){
        ArrayList<String> arrayListAvailableThemesSorted = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            if(Settings.enableDebugLogging){
                LOGGER.info("Adding element to list: " + entry.getValue());
            }
            arrayListAvailableThemesSorted.add(entry.getValue());
        }
        Collections.sort(arrayListAvailableThemesSorted);
        String[] string = new String[arrayListAvailableThemesSorted.size()];
        arrayListAvailableThemesSorted.toArray(string);
        return string;
    }

    public static String[] getCustomThemesByAlphabet(){
        String[] allGenresById = getThemesByAlphabet();
        ArrayList<String> arrayListCustomThemes = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(0, arrayListCustomThemes.size(), I18n.INSTANCE.get("progressBar.moddedThemes"), true);
        int currentProgressBarValue = 0;
        for (String s : allGenresById) {
            boolean defaultGenre = false;
            for (String genre : DEFAULT_THEMES) {
                if (s.equals(genre)) {
                    defaultGenre = true;
                    break;
                }
            }
            if (!defaultGenre) {
                arrayListCustomThemes.add(s);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedThemeFound") + " " + s);
            }
            currentProgressBarValue++;
            ProgressBarHelper.setValue(currentProgressBarValue);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedThemesComplete"));
        ProgressBarHelper.resetProgressBar();
        String[] string = new String[arrayListCustomThemes.size()];
        arrayListCustomThemes.toArray(string);
        return string;
    }

    public static Map<String, String> getSingleThemeByNameMap(String themeNameEn) throws IOException {
        Map<String, String> map = new HashMap<>();
        int positionOfThemeInFiles = getPositionOfThemeInFile(themeNameEn);
        for(String string : TranslationManager.TRANSLATION_KEYS){
            LOGGER.info("Current Translation Key: " + string);
            BufferedReader reader;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_8));
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_16LE));
            }else{
                break;
            }
            String currentLine;
            int currentLineNumber =1;
            boolean firstLine = true;
            while((currentLine = reader.readLine()) != null){
                if(firstLine){
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info("Reading file: " + string);
                }
                if(currentLineNumber == positionOfThemeInFiles){
                    if(string.equals("GE")){
                        String replaceViolenceLevel = currentLine.replace("<M1>", "").replace("<M2>", "").replace("<M3>", "");
                        String nameGe = replaceViolenceLevel.replaceAll("[0-9]", "").replaceAll("<", "").replaceAll(">", "");
                        nameGe = nameGe.trim();
                        map.put("NAME " + string, nameGe);
                        if(currentLine.contains("M1")){
                            map.put("VIOLENCE LEVEL", "1");
                        }else if(currentLine.contains("M2")){
                            map.put("VIOLENCE LEVEL", "2");
                        }else if(currentLine.contains("M3")){
                            map.put("VIOLENCE LEVEL", "3");
                        }else{
                            map.put("VIOLENCE LEVEL", "0");
                        }
                        map.put("GENRE COMB", replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim());
                        if(Settings.enableDebugLogging){
                            LOGGER.info("GENRE COMB: [" + replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim() + "]");
                        }
                    }else{
                        map.put("NAME " + string, currentLine);
                        if(Settings.enableDebugLogging){
                            LOGGER.info("NAME " + string + " | " + currentLine);
                        }
                    }
                }
                currentLineNumber++;
            }
            reader.close();
        }
        return map;
    }

    /**
     * @return Returns a array list containing all theme ids that have been found.
     */
    public static ArrayList<Integer> getThemeIdsInUse(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : MAP_ACTIVE_THEMES_EN.entrySet()){
            arrayList.add(entry.getKey());
        }
        return arrayList;
    }
}
