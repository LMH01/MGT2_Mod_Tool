package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);
    private static int threadsRunning = 0;
    private static String[] controlThreadBlacklist = {"runnableCheckForUpdates"};
    public static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    public static Runnable runnableUninstall = Uninstaller::uninstall;
    public static Runnable runnableImportAll = SharingManager::importAll;
    public static Runnable runnableShowActiveMods = ActiveMods::showActiveMods;
    public static Runnable runnableDeleteExports = Uninstaller::deleteAllExports;
    public static Runnable runnableImportFromURL = ImportFromURLHelper::importFromURL;
    public static Runnable runnableAddCompanyIcon = NewModsHandler::addCompanyIcon;
    public static Runnable runnableNPCGamesList = NPCGameListHandler::modifyNPCGameList;
    public static Runnable runnableCreateRestorePoint = RestorePointHelper::setRestorePoint;
    public static Runnable runnableRestoreToRestorePoint = RestorePointHelper::restoreToRestorePoint;
    public static Runnable runnableCreateFullBackup = () -> Backup.createBackup("full");
    public static Runnable runnableCreateSaveGameBackup = () -> Backup.createBackup("save_game");
    public static Runnable runnableRestoreInitialBackup = WindowMain::restoreInitialBackup;
    public static Runnable runnableRestoreLatestBackup = WindowMain::restoreLatestBackup;
    public static Runnable runnableRestoreSaveGameBackup = Backup::restoreSaveGameBackup;
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

    public static Thread threadPerformStartTasks(){
        Thread thread = new Thread(() -> {
            if(Settings.mgt2FolderIsCorrect){
                WindowMain.lockMenuItems(true);
            }
            UpdateChecker.checkForUpdates(false, false);
            deleteTempFolder();
            try{
                WindowMain.checkActionAvailability();
                if(Settings.mgt2FolderIsCorrect){
                    WindowMain.lockMenuItems(false);
                }
            }catch(NullPointerException e){
                if(Settings.mgt2FolderIsCorrect){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("errorMessages.gameFileCorrupted.textArea.firstPart"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("errorMessages.gameFileCorrupted.textArea.secondPart"));
                }
            }
        });
        thread.setName("PerformStartTasks");
        return thread;
    }

    /**
     * Starts the given thread.
     * Also sets the scroll pane to be locked.
     * When the thread starts, a control thread is started.
     * @param runnable The runnable that should be used for the thread
     * @param threadName The name for the thread
     */
    public static void startThread(Runnable runnable, String threadName){
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        thread.start();
        threadsRunning++;
        LOGGER.info("Thread started: " + threadName);
        LOGGER.info("Threads running: " + threadsRunning);
        WindowMain.lockMenuItems(true);
        boolean startControlThread = true;
        for(String string : controlThreadBlacklist){
            if(string.equals(thread.getName())){
                startControlThread = false;
            }
        }
        if(startControlThread){
            startControlThread(thread);
        }
    }
    /**
     * Deletes the temp folder and initializes a progress bar.
     */
    private static void deleteTempFolder(){
        File tempFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//");
        if(tempFolder.exists()){
            DataStreamHelper.deleteDirectory(tempFolder);
            LOGGER.info("Deleted temp folder.");
        }
    }

    /**
     * Starts a thread that waits until the started thread dies and performs tasks after that.
     */
    public static void startControlThread(Thread threadToWaitFor){
        Thread thread = new Thread(() -> {
            LOGGER.info("Started control thread for thread: " + threadToWaitFor.getName());
            while(threadToWaitFor.isAlive()){
            }
            if(threadsRunning<2){
                LOGGER.info("Thread died: " + threadToWaitFor.getName());
                WindowMain.checkActionAvailability();
            }else{
                LOGGER.info("Thread died but another thread is still running. Exit tasks are not executed. Thread that died: " + threadToWaitFor.getName());
            }
            threadsRunning--;
        });
        thread.setName("ThreadController" + "For" + threadToWaitFor.getName().replace("runnable", "Runnable"));
        thread.start();
    }

    /**
     * Returns the amount of active threads
     */
    public static int getThreadsRunning(){
        return threadsRunning;
    }

    public static Thread getShutdownHookThread(){
        Thread thread = new Thread(runnableDoOnShutdown);
        thread.setName("Shutdown");
        return thread;
    }
}