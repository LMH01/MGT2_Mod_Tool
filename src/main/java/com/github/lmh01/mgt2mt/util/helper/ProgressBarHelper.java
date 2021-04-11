package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.ThreadHandler;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressBarHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressBarHelper.class);

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
        WindowMain.PROGRESS_BAR.setMinimum(minValue);
        WindowMain.PROGRESS_BAR.setMaximum(maxValue);
        WindowMain.PROGRESS_BAR.setValue(minValue);
        WindowMain.PROGRESS_BAR.setString(text);
        if(setTextArea){
            TextAreaHelper.appendText(text);
        }
    }
    /**
     * Sets the text that should be displayed in the progress bar
     * @param text The text that should be displayed
     */
    public static void setText(String text){
        WindowMain.PROGRESS_BAR.setString(text);
    }

    /**
     * Increments the progress bar by one
     */
    public static void increment(){
        WindowMain.PROGRESS_BAR.setValue(WindowMain.PROGRESS_BAR.getValue()+1);
    }

    /**
     * Sets the progress bar value
     */
    public static void setValue(int value){
        WindowMain.PROGRESS_BAR.setValue(value);
    }

    /**
     * Increases the maximum value of the progress bar by value
     * @param value
     */
    public static void increaseMaxValue(int value) {
        WindowMain.PROGRESS_BAR.setMaximum(WindowMain.PROGRESS_BAR.getMaximum() + value);
    }
    /**
     * Resets the progress bar in the main window
     */
    public static void resetProgressBar(){
        WindowMain.PROGRESS_BAR.setString(I18n.INSTANCE.get("progressBar.idle"));
        WindowMain.PROGRESS_BAR.setMinimum(0);
        WindowMain.PROGRESS_BAR.setMaximum(100);
        WindowMain.PROGRESS_BAR.setValue(0);
    }
}
