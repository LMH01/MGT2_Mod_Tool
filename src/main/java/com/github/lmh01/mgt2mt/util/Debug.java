package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.manager.*;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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