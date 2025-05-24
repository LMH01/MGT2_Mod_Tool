package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.Version;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;

import javax.swing.*;

public class About {
    public static void showAboutPopup() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MGT2-Mod-Tool").append(System.getProperty("line.separator"));
        stringBuilder.append("Version " + Version.getVersion()).append(System.getProperty("line.separator"));
        if (UpdateChecker.updateAvailable) {
            stringBuilder.append("Newest version: ").append(UpdateChecker.newestVersion).append(System.getProperty("line.separator"));
        }
        stringBuilder.append("Build on: " + Version.getBuildDate()).append(System.getProperty("line.separator"));
        stringBuilder.append("Git rev: " + Version.getBuildGitHash());
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), I18n.INSTANCE.get("frame.title.about"), JOptionPane.INFORMATION_MESSAGE);
    }
}
