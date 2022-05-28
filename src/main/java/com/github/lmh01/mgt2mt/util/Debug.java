package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This file contains functions that are used for debug purposes only
 */
public class Debug {//TODO Calls zu debug aus richtigem code rausnehmen (wenn bereit für release)
    private static final Logger LOGGER = LoggerFactory.getLogger(Debug.class);

    public static void test() {
        ThreadHandler.startModThread(() -> {

        }, "Debug");
    }
}