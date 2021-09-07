package com.github.lmh01.mgt2mt.util.helper;

import java.util.concurrent.TimeUnit;

/**
 * @deprecated It is unsafe to use this method please use {@link TimeHelper} instead
 */
@Deprecated
public class TimeHelperOld {//TODO Remove class
    private static int timePassed = 0;
    private static boolean measureTime = false;
    public static void startMeasureTimeThread(){
        Thread thread = new Thread(() -> {
            measureTime = true;
            while(measureTime){
                try {
                    TimeUnit.SECONDS.sleep(1);
                    timePassed++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        if(!measureTime){
            thread.start();
        }
    }

    /**
     * Stops to measure time and returns time
     */
    public static int getMeasuredTime(){
        measureTime = false;
        int returnTime = timePassed;
        timePassed = 0;
        return returnTime;
    }
}
