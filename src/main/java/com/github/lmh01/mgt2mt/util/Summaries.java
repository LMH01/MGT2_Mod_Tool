package com.github.lmh01.mgt2mt.util;

import javax.swing.*;

public class Summaries {
    //Contains functions that show messages to the user, asking to confirm the import/addition of feature

    public static boolean showSummary(String summaryMainPart, String title){
        return JOptionPane.showConfirmDialog(null, summaryMainPart, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
