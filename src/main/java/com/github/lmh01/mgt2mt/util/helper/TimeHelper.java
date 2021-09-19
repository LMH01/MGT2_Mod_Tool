package com.github.lmh01.mgt2mt.util.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimeHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeHelper.class);

    private static int timeHelpersActive = 0;

    private int timePassed = 0;
    private boolean measureTime = false;
    private TimeUnit timeUnit;
    private int id;
    private int lastMeasurement = 0;

    /**
     * Create a new time helper
     *
     * @param startTimer If true the timer will be started
     * @param unit       Indicates how accurate the measured time should be
     */
    public TimeHelper(TimeUnit unit, boolean startTimer) {
        timeUnit = unit;
        id = timeHelpersActive;
        timeHelpersActive++;
        if (startTimer) {
            measureTime();
        }
    }

    /**
     * Starts a new thread that measures the time
     */
    public void measureTime() {
        Thread thread = new Thread(() -> {
            DebugHelper.debug(LOGGER, "Starting to measure time in " + timeUnit);
            measureTime = true;
            while (measureTime) {
                try {
                    timeUnit.sleep(1);
                    timePassed++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        thread.setName("TimeMeasurer" + id);
        if (!measureTime) {
            thread.start();
        }
    }

    /**
     * Stops to measure time and returns the time in the indicated unit
     *
     * @param unit The time unit in which the time should be returned
     */
    public long getMeasuredTime(TimeUnit unit) {
        if (timePassed == 0) {
            return timeUnit.convert(lastMeasurement, unit);
        } else {
            measureTime = false;
            lastMeasurement = timePassed;
            timePassed = 0;
            return timeUnit.convert(lastMeasurement, unit);
        }
    }
}
