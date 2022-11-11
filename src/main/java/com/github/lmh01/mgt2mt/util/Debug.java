package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.instances.Genre;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This file contains functions that are used for debug purposes only
 */
public class Debug {
    private static final Logger LOGGER = LoggerFactory.getLogger(Debug.class);

    public static void test() {
        ThreadHandler.startModThread(() -> {
            Genre mainGenre = (Genre)GenreManager.INSTANCE.constructContentFromName("Adventure");
            Genre subGenre = (Genre)GenreManager.INSTANCE.constructContentFromName("Sports Game");
            ArrayList<Integer> mainGenreSettings = mainGenre.getSliderSettings();
            ArrayList<Integer> subGenreSettings = subGenre.getSliderSettings();
            ArrayList<Integer> combinedSettings = GenreManager.getComboSliderSettings(mainGenre, subGenre);
            for (int i = 0; i< mainGenreSettings.size(); i++) {
                System.out.printf("%2d - %2d -> %2d\n", mainGenreSettings.get(i), subGenreSettings.get(i), combinedSettings.get(i));
            }
        }, "Debug");
    }
}