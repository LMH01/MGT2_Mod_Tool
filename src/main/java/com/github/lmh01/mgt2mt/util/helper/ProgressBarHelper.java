package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ProgressBarHelper {//TODO Rewrite to extend JProgressBar to make it more save. (Not sure on that)
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressBarHelper.class);
    private static String currentProgressBarString = "";
    private static boolean progressBarRunning = false;
    private static int secondsElapsed = 0;
    private static boolean timeEnabled = false;
    private static boolean progressStringEnabled = true;

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
        initializeProgressBar(minValue, maxValue, text, setTextArea, true);
    }

    /**
     * Initializes the progress bar in the main window to use the input values.
     * Sets the current value to the min value.
     * When this function is called the input text is written to the text area
     * @param minValue The minimum value the progress bar should have
     * @param maxValue The maximum value the progress bar should have
     * @param text The text that should be displayed
     * @param setTextArea If true the text will also be written to the text area
     * @param disableMeasuredTime If false the time will be measured
     */
    public static void initializeProgressBar(int minValue, int maxValue, String text, boolean setTextArea, boolean disableMeasuredTime){
        initializeProgressBar(minValue, maxValue, text, setTextArea, disableMeasuredTime, true);
    }

    /**
     * Initializes the progress bar in the main window to use the input values.
     * Sets the current value to the min value.
     * When this function is called the input text is written to the text area
     * @param minValue The minimum value the progress bar should have
     * @param maxValue The maximum value the progress bar should have
     * @param text The text that should be displayed
     * @param setTextArea If true the text will also be written to the text area
     * @param disableMeasuredTime If false the time will be measured
     * @param showProgressInText If true the progress will be displayed in text
     */
    public static void initializeProgressBar(int minValue, int maxValue, String text, boolean setTextArea, boolean disableMeasuredTime, boolean showProgressInText){
        secondsElapsed = 0;
        if(!disableMeasuredTime){
            timeEnabled = true;
            startProgressBarTimeThread();
        }
        progressStringEnabled = showProgressInText;
        WindowMain.PROGRESS_BAR.setMinimum(minValue);
        WindowMain.PROGRESS_BAR.setMaximum(maxValue);
        WindowMain.PROGRESS_BAR.setValue(minValue);
        currentProgressBarString = text;
        changeProgress();
        if(setTextArea){
            TextAreaHelper.appendText(text);
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
        timeEnabled = false;
        WindowMain.PROGRESS_BAR.setString(I18n.INSTANCE.get("progressBar.idle"));
        WindowMain.PROGRESS_BAR.setMinimum(0);
        WindowMain.PROGRESS_BAR.setMaximum(1);
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentProgressBarString).append(" ");
        if(progressStringEnabled) {
            stringBuilder.append(getProgressString());
        }
        stringBuilder.append(getProgressBarTime());
        WindowMain.PROGRESS_BAR.setString(stringBuilder.toString());
    }

    private static String getProgressBarTime(){
        if(timeEnabled){
            return " (" + I18n.INSTANCE.get("progressBar.timeElapsed") + ": " + Utils.convertSecondsToTime(secondsElapsed) + ")";
        }else{
            return "";
        }
    }

    /**
     * Returns how many second the progress bar is running
     */
    public static int getProgressBarTimer(){
        return secondsElapsed;
    }
    /**
     * Starts a thread that adds the time passed to the progress bar
     */
    public static void startProgressBarTimeThread(){
        Thread thread = new Thread(() -> {
            LOGGER.info("Starting to measure time");
            timeEnabled = true;
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
            thread.setName("ProgressBarTimeMeasurer");
            thread.start();
        }
    }
}
