package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    public static String newestVersion = "";
    public static String newestVersionKeyFeatures = "";
    private static final String UPDATE_URL = "https://www.dropbox.com/s/7dut2h3tqdc92xz/mgt2mtNewestVerion.txt?dl=1";
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    public static void checkForUpdates(boolean showNoUpdateAvailableDialog){
        new Thread("UpdateChecker"){
            public void run(){
                try {
                    LOGGER.info("Checking for updates...");
                    java.net.URL url = new URL(UPDATE_URL);
                    Scanner scanner = new Scanner(url.openStream());
                    String currentLine = scanner.nextLine();
                    newestVersion = currentLine;
                    if(!newestVersion.equals(MadGamesTycoon2ModTool.VERSION)){
                        LOGGER.info("New version found: " + newestVersion);
                        LOGGER.info("Key features:");
                        StringBuilder stringBuilder = new StringBuilder();
                        while(scanner.hasNextLine()){
                            currentLine = scanner.nextLine();
                            LOGGER.info(currentLine);
                            stringBuilder.append(currentLine).append(System.getProperty("line.separator"));
                        }
                        newestVersionKeyFeatures = stringBuilder.toString();
                        if(JOptionPane.showConfirmDialog(null, "A new version is available: " + newestVersion + "\nKey features:\n" + newestVersionKeyFeatures + "\nIt is recommended to always use the newest version to keep this tool compatible with MGT2.\n\nDo you wan't to open the github repository to download the newest version?", "New version available", JOptionPane.YES_NO_OPTION) == 0){
                            try {
                                Utils.openGithubPage();
                            } catch (Exception e) {
                                Utils.showErrorMessage(2, e);
                                e.printStackTrace();
                            }
                        }
                    }else{
                        LOGGER.info("You are using the newest version");
                        if(showNoUpdateAvailableDialog){
                            JOptionPane.showMessageDialog(null, "You are using the newest version.\nNo update available.", "No update available", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
