package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.windows.WindowMain;

public class ProgressBarHelper {

    /**
     * Initializes the progress bar in the main window to use the input values.
     * Sets the current value to the min value.
     * When this function is called the input text is written to the text area
     * @param minValue The minimum value the progress bar should have
     * @param maxValue The maximum value the progress bar should have
     * @param text The text that should be displayed
     */
    public static void initializeProgressBar(int minValue, int maxValue, String text){
        WindowMain.PROGRESS_BAR.setMinimum(minValue);
        WindowMain.PROGRESS_BAR.setMaximum(maxValue);
        WindowMain.PROGRESS_BAR.setValue(minValue);
        WindowMain.PROGRESS_BAR.setString(text);
        TextAreaHelper.appendText(text + "...");
    }

    /**
     * Sets the text that should be displayed in the progress bar
     * @param text The text that should be displayed
     */
    public static void setText(String text){
        WindowMain.PROGRESS_BAR.setString(text);
    }

    /**
     * Sets the progress bar value
     */
    public static void setValue(int value){
        WindowMain.PROGRESS_BAR.setValue(value);
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
