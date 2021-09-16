package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    public static String newestVersion = "";
    public static String newestVersionKeyFeatures = "";
    public static boolean latestGameVersionCompatible = true;
    public static boolean updateAvailable = false;
    private static final String RELEASE_UPDATE_URL = "https://www.dropbox.com/s/7dut2h3tqdc92xz/mgt2mtNewestVerion.txt?dl=1";
    private static final String ALPHA_UPDATE_URL = "https://www.dropbox.com/s/16lk6kyc1wdpw43/mgt2mtNewestAlphaVersion.txt?dl=1";
    private static final String COMPATIBILITY_CHECK_URL = "https://www.dropbox.com/s/8bbob53h13dvspj/mgt2mtCompatibleWithLatestVersion.txt?dl=1";
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);

    public static void checkForUpdates(boolean showNoUpdateAvailableDialog) {
        checkForUpdates(showNoUpdateAvailableDialog, true);
    }

    public static void checkForUpdates(boolean showNoUpdateAvailableDialog, boolean useProgressBar) {
        checkForVersionCompatibility();
        Thread updateChecker = new Thread(() -> {
            try {
                if (useProgressBar) {
                    WindowMain.lockMenuItems(true);
                    ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.checkForUpdates"));
                }
                LOGGER.info("Checking for updates...");
                updateAvailable = false;
                java.net.URL url;
                String versionType;
                if (Settings.updateBranch.equals("Release")) {
                    url = new URL(RELEASE_UPDATE_URL);
                    versionType = I18n.INSTANCE.get("dialog.updateChecker.updateAvailable.versionType.ver1") + " ";
                } else {
                    url = new URL(ALPHA_UPDATE_URL);
                    versionType = I18n.INSTANCE.get("dialog.updateChecker.updateAvailable.versionType.ver2") + " ";
                }
                Scanner scanner = new Scanner(url.openStream());
                String currentLine = scanner.nextLine();
                newestVersion = currentLine;
                if (useProgressBar) {
                    ProgressBarHelper.increment();
                }

                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    currentLine = scanner.nextLine();
                    LOGGER.info(currentLine);
                    stringBuilder.append(currentLine).append(System.getProperty("line.separator"));
                }
                newestVersionKeyFeatures = stringBuilder.toString();
                if (!newestVersion.equals(MadGamesTycoon2ModTool.VERSION)) {
                    if (!newestVersion.equals(MadGamesTycoon2ModTool.CURRENT_RELEASE_VERSION)) {
                        updateAvailable = true;
                        LOGGER.info("New version found: " + newestVersion);
                        LOGGER.info("Key features:");
                        if (JOptionPane.showConfirmDialog(null, versionType + newestVersion + "\n" + I18n.INSTANCE.get("dialog.updateChecker.keyFeatures") + "\n" + newestVersionKeyFeatures + "\n" + I18n.INSTANCE.get("dialog.updateChecker.updateAvailable"), I18n.INSTANCE.get("dialog.updateChecker.updateAvailable.title"), JOptionPane.YES_NO_OPTION) == 0) {
                            try {
                                Utils.openGithubPage();
                            } catch (Exception e) {
                                Utils.showErrorMessage(2, e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (!updateAvailable) {
                    LOGGER.info("You are using the newest version");
                    if (showNoUpdateAvailableDialog) {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.updateChecker.noUpdateAvailable"), I18n.INSTANCE.get("dialog.updateChecker.noUpdateAvailable.title"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (showNoUpdateAvailableDialog) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.updateChecker.updateFailed"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
            if (useProgressBar) {
                ProgressBarHelper.resetProgressBar();
            }
        });
        updateChecker.setName("UpdateChecker");
        updateChecker.start();
        if (useProgressBar) {
            ThreadHandler.startControlThread(updateChecker);
        }
    }

    /**
     * Checks if this tool is compatible with the newest MGT2 version
     */
    public static void checkForVersionCompatibility() {
        Thread compatibilityCheck = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(new URL(COMPATIBILITY_CHECK_URL).openStream());
                String currentLine = scanner.nextLine();
                if (currentLine.equals("true")) {
                    latestGameVersionCompatible = true;
                    LOGGER.info("Latest mod tool version is compatible with mgt2");
                } else if (currentLine.equals("false")) {
                    latestGameVersionCompatible = false;
                    LOGGER.info("Latest mod tool version is not compatible with mgt2");
                }
                if (!latestGameVersionCompatible && !Utils.isAlpha()) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.updateChecker.latestGameVersionNotCompatible"), I18n.INSTANCE.get("frame.title.warning"), JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compatibilityCheck.setName("CompatibilityChecker");
        compatibilityCheck.start();
    }
}
