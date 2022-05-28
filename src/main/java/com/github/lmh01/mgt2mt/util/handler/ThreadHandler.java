package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.DefaultContentManager;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);
    private static int threadsRunning = 0;
    private static final String[] controlThreadBlacklist = {"runnableCheckForUpdates"};
    private static Map<String, Integer> threadsRan = new HashMap<>();
    public static Runnable runnableDoOnShutdown = () -> {
        LOGGER.info("Performing exit tasks...");
        LogFile.stopLogging();
        LOGGER.info("Exit tasks complete. Good night");
    };

    public static Runnable runnableDeleteTempFolder = () -> {
        WindowMain.lockMenuItems(true);
        deleteTempFolder();
        WindowMain.lockMenuItems(false);
    };

    public static Thread threadPerformStartTasks() {
        Thread thread = new Thread(() -> {
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.initializingTool"), false, true, false);
            if (Utils.isAlpha()) {
                Settings.updateBranch = UpdateBranch.ALPHA;
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.runningAlphaVersion.1"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.runningAlphaVersion.2"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.runningAlphaVersion.3") + ": " + ModManagerPaths.MAIN.getPath());
            }
            if (Settings.mgt2FolderIsCorrect) {
                WindowMain.lockMenuItems(true);
            }
            UpdateChecker.checkForUpdates(false, false);
            DefaultContentManager.performStartTasks();
            deleteTempFolder();
            ContentAdministrator.initializeDefaultContents();
            try {
                WindowMain.checkActionAvailability();
                if (Settings.mgt2FolderIsCorrect) {
                    WindowMain.lockMenuItems(false);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                if (Settings.mgt2FolderIsCorrect) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("errorMessages.gameFileCorrupted.textArea.firstPart"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("errorMessages.gameFileCorrupted.textArea.secondPart"));
                }
            }
            try {
                if (Settings.enableInitialBackupCheck) {
                    if (!InitialBackupChecker.checkIfUpToDate()) {
                        LOGGER.warn("Initial backup is not up-to-date");
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.startTasks.initialBackupOutdated"), I18n.INSTANCE.get("frame.title.warning"), JOptionPane.WARNING_MESSAGE);
                    } else {
                        DebugHelper.debug(LOGGER, "Initial backup is up-to-date");
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Unable to check if initial backup is up-to-date");
                e.printStackTrace();
            }
            ProgressBarHelper.resetProgressBar();

            if (Settings.disableSafetyFeatures) {
                DebugHelper.warn(LOGGER, "check for game file integrity is disabled because the safety features are disabled");
            } else {
                ContentAdministrator.analyzeGameFileIntegrity(false);
            }
        });
        thread.setName("PerformStartTasks");
        return thread;
    }

    /**
     * Starts the given thread.
     * Also sets the scroll pane to be locked.
     * When the thread starts, a control thread is started.
     *
     * @param runnable   The runnable that should be used for the thread
     * @param threadName The name for the thread
     */
    public static void startThread(Runnable runnable, String threadName) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        thread.start();
        threadsRunning++;
        DebugHelper.debug(LOGGER, "Thread started: " + threadName);
        DebugHelper.debug(LOGGER, "Threads running: " + threadsRunning);
        WindowMain.lockMenuItems(true);
        boolean startControlThread = true;
        for (String string : controlThreadBlacklist) {
            if (string.equals(thread.getName())) {
                startControlThread = false;
                break;
            }
        }
        if (startControlThread) {
            startControlThread(thread);
        }
    }

    /**
     * Starts a thread that can catch an exception.
     * Primary use case should be to catch an {@link ModProcessingException}.
     * If that exception is caught an error message displayed and printed into the text area. The thread will terminate.
     * The thread name will get a unique id for each run of the same thread.
     */
    public static void startModThread(ModAction action, String threadName) {
        Thread thread = new Thread(() -> {
            try {
                action.run();
            } catch (Exception e) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.firstPart") + " " + threadName);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.secondPart"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.thirdPart"));
                TextAreaHelper.printStackTrace(e);
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.modProcessingException.firstPart") + " " + threadName + "\n" + I18n.INSTANCE.get("commonText.reason") + " " + e.getMessage().replace(" - ", "\n - "), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                LOGGER.info("Error in thread: " + threadName + "; Reason: " + e.getMessage());
            }
        });
        if (threadsRan.containsKey(threadName)) {
            threadsRan.replace(threadName, threadsRan.get(threadName) + 1);
        } else {
            threadsRan.put(threadName, 1);
        }
        startThread(thread, threadName + threadsRan.get(threadName));
    }

    /**
     * Deletes the temp folder and initializes the progress bar for that action
     */
    private static void deleteTempFolder() {
        if (ModManagerPaths.TEMP.getPath().toFile().exists()) {
            try {
                LOGGER.info("Deleted temp folder.");
                DataStreamHelper.deleteDirectory(ModManagerPaths.TEMP.getPath(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts a thread that waits until the started thread dies and performs tasks after that.
     */
    public static void startControlThread(Thread threadToWaitFor) {
        Thread thread = new Thread(() -> {
            DebugHelper.debug(LOGGER, "Started control thread for thread: " + threadToWaitFor.getName());
            while (threadToWaitFor.isAlive()) {
            }
            if (threadsRunning < 2) {
                DebugHelper.debug(LOGGER, "Thread died : " + threadToWaitFor.getName());
                WindowMain.checkActionAvailability();
            } else {
                DebugHelper.debug(LOGGER, "Thread died : " + threadToWaitFor.getName() + ". Another threat is still running, exit tasks are not executed");
            }
            threadsRunning--;
        });
        thread.setName("Controller" + "For" + threadToWaitFor.getName().replace("runnable", "Runnable"));
        thread.start();
    }

    /**
     * Returns the amount of active threads
     */
    public static int getThreadsRunning() {
        return threadsRunning;
    }

    public static Thread getShutdownHookThread() {
        Thread thread = new Thread(runnableDoOnShutdown);
        thread.setName("Shutdown");
        return thread;
    }
}