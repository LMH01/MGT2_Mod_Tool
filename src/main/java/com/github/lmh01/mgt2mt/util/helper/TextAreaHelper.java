package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.AdjustmentListener;
import java.util.concurrent.TimeUnit;

public class TextAreaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextAreaHelper.class);
    private static AdjustmentListener adjustmentListener = e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum());

    /**
     * Will append the input text to the text area. A line break is written
     */
    public static void appendText(String text){
        writeText(text);
    }

    public static void appendText(String text, boolean scrollDown){
        if(scrollDown){
            LOGGER.info("setScrollDown");
            setScrollDown();
        }
        writeText(text);
        if(scrollDown){
            LOGGER.info("resetAutoScroll");
            resetAutoScroll();
        }
    }

    public static void setScrollDown(){
        WindowMain.SCROLL_PANE.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
    }

    public static void resetAutoScroll(){
        WindowMain.SCROLL_PANE.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener);
    }

    private static void writeText(String text){
        WindowMain.TEXT_AREA.append(text);
        WindowMain.TEXT_AREA.append(System.getProperty("line.separator"));
        LogFile.write(text);
    }
}
