package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AnalyzeSteamLibraries {
    private static final Path STEAM_LIBRARY_FOLDERS_PATH;
    public static final Path STEAM_LIBRARY_DEFAULT_FOLDER;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeSteamLibraries.class);

    static {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        if (MadGamesTycoon2ModTool.isWindows()) {
            STEAM_LIBRARY_FOLDERS_PATH = Paths.get("C:", "Program Files (x86)", "Steam", "steamapps", "libraryfolders.vdf");
            STEAM_LIBRARY_DEFAULT_FOLDER = Paths.get("C:", "Program Files (x86)", "Steam");
        } else {
            STEAM_LIBRARY_FOLDERS_PATH = Paths.get(System.getProperty("user.home"), ".local", "share", "Steam", "steamapps", "libraryfolders.vdf");
            STEAM_LIBRARY_DEFAULT_FOLDER = Paths.get(System.getProperty("user.home"), ".local", "share", "Steam");
        }
    }

    /**
     * @return Returns an array list containing all steam libraries.
     * @throws IOException Throws an IOException when the libraryfolders.vdf is not found.
     */
    public static ArrayList<String> getSteamLibraries() throws IOException {
        LOGGER.info("Scanning steam libraries...");
        ArrayList<String> arrayListSteamLibraries = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader(STEAM_LIBRARY_FOLDERS_PATH.toFile()));
        arrayListSteamLibraries.add(STEAM_LIBRARY_DEFAULT_FOLDER.toString());
        String currentLine;
        while ((currentLine = bf.readLine()) != null) {
            if (currentLine.contains("\"path\"")) {
                final String trim = currentLine.replace("\"path\"", "").replaceAll("\"+", "").trim();
                arrayListSteamLibraries.add(trim);
                LOGGER.info("Added entry to Steam Libraries Array: " + trim);
            }
        }
        bf.close();
        return arrayListSteamLibraries;
    }
}
