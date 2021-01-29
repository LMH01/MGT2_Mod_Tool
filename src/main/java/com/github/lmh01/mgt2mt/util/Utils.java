package com.github.lmh01.mgt2mt.util;

import java.time.LocalDateTime;

public class Utils {
    /**
     * @return returns the current date time in format: YYYY-MM-DD-HH-MM
     */
    public static String getCurrentDateTime(){
        String currentDateTime = LocalDateTime.now().getYear() + "-" +
                LocalDateTime.now().getMonth() + "-"+
                LocalDateTime.now().getDayOfMonth() + "-" +
                LocalDateTime.now().getHour() + "-" +
                LocalDateTime.now().getMinute();
        return currentDateTime;
    }
}
