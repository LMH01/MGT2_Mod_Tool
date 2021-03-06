package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextAreaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextAreaHelper.class);

    /**
     * Will append the input text to the text area. A line break is written
     */
    public static void appendText(String text){
        writeText(text);
    }
    private static void writeText(String text){
        WindowMain.TEXT_AREA.append(text);
        WindowMain.TEXT_AREA.append(System.getProperty("line.separator"));
        LogFile.write(text);
        WindowMain.TEXT_AREA.setCaretPosition(WindowMain.TEXT_AREA.getDocument().getLength());
    }
}
