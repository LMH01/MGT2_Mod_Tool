package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ProgressBarHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressBarHelper.class);
    private static String currentProgressBarString = "";
    private static String currentProgressBarStringWithProgress = "";
    private static boolean progressBarRunning = false;
    private static int secondsElapsed = 0;

    /**
     * Initializes the progress bar in the main window to use the input values.
     * Sets the current value to the min value.
     * When this function is called the input text is written to the text area
     * @param minValue The minimum value the progress bar should have
     * @param maxValue The maximum value the progress bar should have
     * @param text The text that should be displayed
     */
    public static void initializeProgressBar(int minValue, int maxValue, String text){
        initializeProgressBar(minValue, maxValue, text, false);
    }

    /**
     * Initializes the progress bar in the main window to use the input values.
     * Sets the current value to the min value.
     * When this function is called the input text is written to the text area
     * @param minValue The minimum value the progress bar should have
     * @param maxValue The maximum value the progress bar should have
     * @param text The text that should be displayed
     * @param setTextArea If true the text will also be written to the text area
     */
    public static void initializeProgressBar(int minValue, int maxValue, String text, boolean setTextArea){
        initializeProgressBar(minValue, maxValue, text, setTextArea, false);
    }

    public static void initializeProgressBar(int minValue, int maxValue, String text, boolean setTextArea, boolean disableMeasuredTime){
        WindowMain.PROGRESS_BAR.setMinimum(minValue);
        WindowMain.PROGRESS_BAR.setMaximum(maxValue);
        WindowMain.PROGRESS_BAR.setValue(minValue);
        WindowMain.PROGRESS_BAR.setString(text + getProgressString());
        currentProgressBarString = text;
        if(setTextArea){
            TextAreaHelper.appendText(text);
        }
        if(!disableMeasuredTime){
            startProgressBarTimeThread();
        }
    }
    /**
     * Sets the text that should be displayed in the progress bar
     * @param text The text that should be displayed
     */
    public static void setText(String text){
        currentProgressBarString = text;
        changeProgress();
    }

    /**
     * Increments the progress bar by one
     */
    public static void increment(){
        WindowMain.PROGRESS_BAR.setValue(WindowMain.PROGRESS_BAR.getValue()+1);
        changeProgress();
    }

    /**
     * Sets the progress bar value
     */
    public static void setValue(int value){
        WindowMain.PROGRESS_BAR.setValue(value);
        changeProgress();
    }

    /**
     * Increases the maximum value of the progress bar by value
     * @param value
     */
    public static void increaseMaxValue(int value) {
        WindowMain.PROGRESS_BAR.setMaximum(WindowMain.PROGRESS_BAR.getMaximum() + value);
        changeProgress();
    }
    /**
     * Resets the progress bar in the main window
     */
    public static void resetProgressBar(){
        progressBarRunning = false;
        WindowMain.PROGRESS_BAR.setString(I18n.INSTANCE.get("progressBar.idle"));
        WindowMain.PROGRESS_BAR.setMinimum(0);
        WindowMain.PROGRESS_BAR.setMaximum(100);
        WindowMain.PROGRESS_BAR.setValue(0);
    }

    /**
     * Returns the progress string eg. (0/100)
     */
    private static String getProgressString(){
        return "(" + WindowMain.PROGRESS_BAR.getValue() + "/" + WindowMain.PROGRESS_BAR.getMaximum() + ")";
    }

    /**
     * Changes the progress value and time behind the progress bar text
     */
    private static void changeProgress(){
        WindowMain.PROGRESS_BAR.setString(currentProgressBarString + " " + getProgressString() + " " + getProgressBarTime());
    }

    private static String getProgressBarTime(){
        return "(" + I18n.INSTANCE.get("progressBar.timeElapsed") + ": " + Utils.convertSecondsToTime(secondsElapsed) + ")";
    }


    /**
     * Starts a thread that adds the time passed to the progress bar
     */
    public static void startProgressBarTimeThread(){
        Thread thread = new Thread(() -> {
            LOGGER.info("Starting to measure time");
            progressBarRunning = true;
            secondsElapsed = 0;
            while(progressBarRunning){
                try {
                    TimeUnit.SECONDS.sleep(1);
                    secondsElapsed++;
                    if(progressBarRunning){
                        changeProgress();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            LOGGER.info("Stopped to measure time");
        });
        if(!progressBarRunning){
            thread.start();
        }
    }
}
