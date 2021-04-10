package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.windows.WindowMain;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class TextAreaHelper {
    private static AdjustmentListener adjustmentListener = e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum());

    /**
     * Will append the input text to the text area. A line break is written
     */
    public static void appendText(String text){
        WindowMain.TEXT_AREA.append(text);
        WindowMain.TEXT_AREA.append(System.getProperty("line.separator"));
    }

    public static void setScrollDown(){
        WindowMain.SCROLL_PANE.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
    }

    public static void resetAutoScroll(){
        WindowMain.SCROLL_PANE.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener);
    }
}
