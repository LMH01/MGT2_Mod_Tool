package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;

import javax.swing.*;

public class About {
    public static void showAboutPopup(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MGT2-Mod-Tool").append(System.getProperty("line.separator"));
        stringBuilder.append("Version " + MadGamesTycoon2ModTool.VERSION).append(System.getProperty("line.separator"));
        if(UpdateChecker.updateAvailable){
            stringBuilder.append("Newest version: ").append(UpdateChecker.newestVersion).append(System.getProperty("line.separator"));
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
