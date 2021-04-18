package com.github.lmh01.mgt2mt.data_stream.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;

public class AnalyzeSteamLibraries {
    private static final String STEAM_LIBRARY_FOLDERS_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\libraryfolders.vdf";
    private static final String STEAM_LIBRARY_DEFAULT_FOLDER = "C:\\Program Files (x86)\\Steam\\";
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeSteamLibraries.class);

    /**
     * @return Returns an array list containing all steam libraries.
     * @throws IOException Throws an IOException when the libraryfolders.vdf is not found.
     */
    public static ArrayList<String> getSteamLibraries() throws IOException {
        LOGGER.info("Scanning steam libraries...");
        ArrayList<String> arrayListSteamLibraries = new ArrayList<>();
        File steamLibraryFoldersVDF = new File(STEAM_LIBRARY_FOLDERS_PATH);
        BufferedReader bf = new BufferedReader(new FileReader(steamLibraryFoldersVDF));
        arrayListSteamLibraries.add(STEAM_LIBRARY_DEFAULT_FOLDER);
        String currentLine;
        int currentPath = 1;
        while((currentLine = bf.readLine()) != null){
            if(currentLine.matches(".*\\d.*") && !currentLine.contains("TimeNextStatsReport") && !currentLine.contains("ContentStatsID")){
                arrayListSteamLibraries.add(currentLine.replace("\"" + currentPath + "\"", "").replace("\"", "").replaceAll("\\d", "").replaceAll("\\s+","").replace("SteamGames", "Steam Games"));
                LOGGER.info("Added entry to Steam Libraries Array: " + arrayListSteamLibraries.get(currentPath-1));
                currentPath++;
            }
        }
        return  arrayListSteamLibraries;
    }
}
