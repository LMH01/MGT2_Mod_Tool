package com.github.lmh01.mgt2mt.util.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimeHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeHelper.class);

    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private static int timeHelpersActive = 0;

    private int timePassed = 0;
    private boolean measureTime = false;
    private final int id;
    private int lastMeasurement = 0;

    /**
     * Construct a new time helper and start the measurement
     */
    public TimeHelper() {
        this(true);
    }

    public TimeHelper(boolean start) {
        id = timeHelpersActive;
        timeHelpersActive++;
        if (start) {
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
     * Stops to measure time and returns the time in milliseconds
     */
    public long getMeasuredTime() {
        if (timePassed != 0) {
            measureTime = false;
            lastMeasurement = timePassed;
            timePassed = 0;
        }
        return timeUnit.convert(lastMeasurement, timeUnit);
    }

    /**
     * Stops the measurement and returns the time passed in seconds.
     */
    public String getMeasuredTimeDisplay() {
        double divisor;
        switch (timeUnit) {
            case DAYS:
                divisor = 0.0000115741;
                break;
            case HOURS:
                divisor = 0.00027778;
                break;
            case MINUTES:
                divisor = 0.016667;
                break;
            case SECONDS:
                divisor = 1.0;
                break;
            case MILLISECONDS:
                divisor = 1000.0;
                break;
            case MICROSECONDS:
                divisor = 1000000.0;
                break;
            case NANOSECONDS:
                divisor = 1000000000.0;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + timeUnit);
        }
        return String.format("%.2f", getMeasuredTime() / divisor);
    }
}
