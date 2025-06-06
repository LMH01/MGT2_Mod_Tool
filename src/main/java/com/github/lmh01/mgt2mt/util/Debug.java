package com.github.lmh01.mgt2mt.util;

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
            ArrayList<Integer> subGenreIds = new ArrayList<>();
            subGenreIds.add(2);
            ArrayList<Integer> mainGenreIds = new ArrayList<>();
            mainGenreIds.add(0);
            GenreManager.editSubGenres(mainGenreIds, subGenreIds, true);
            //HelpSheetGenerator.generate();
        }, "Debug");
    }
}