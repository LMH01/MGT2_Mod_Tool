package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
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
    public static Runnable runnableExportLicence = () -> OperationHelper.process((string) -> SharingManager.licenceSharer.exportMod(string, false), ModManager.licenceMod.getAnalyzer().getCustomContentString(), ModManager.licenceMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportEngineFeatures = () -> OperationHelper.process((string) -> SharingManager.engineFeatureSharer.exportMod(string, false), ModManager.engineFeatureMod.getAnalyzer().getCustomContentString(), ModManager.engineFeatureMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportGameplayFeatures = () -> OperationHelper.process((string) -> SharingManager.gameplayFeatureSharer.exportMod(string, false), ModManager.gameplayFeatureMod.getAnalyzer().getCustomContentString(), ModManager.gameplayFeatureMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportThemes = () -> OperationHelper.process((string) -> SharingManager.themeSharer.exportMod(string, false), ModManager.themeMod.getAnalyzerEn().getCustomContentString(), ModManager.themeMod.getAnalyzerEn().getContentByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportPublisher = () -> OperationHelper.process((string) -> SharingManager.publisherSharer.exportMod(string, false), ModManager.publisherMod.getAnalyzer().getCustomContentString(), ModManager.publisherMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportGenre = () -> OperationHelper.process((string) -> SharingManager.genreSharer.exportMod(string, false), ModManager.genreMod.getAnalyzer().getCustomContentString(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    public static Runnable runnableUninstall = Uninstaller::uninstall;
    public static Runnable runnableImportAll = SharingManager::importAll;
    public static Runnable runnableShowActiveMods = ActiveMods::showActiveMods;
    public static Runnable runnableDeleteExports = Uninstaller::deleteAllExports;
    public static Runnable runnableReplacePublisherWithRealPublishers = PublisherHelper::realPublishers;
    public static Runnable runnableImportFromURL = ImportFromURLHelper::importFromURL;
    public static Runnable runnableRemoveGenre = () -> OperationHelper.process(EditorManager.genreEditor::removeMod, ModManager.genreMod.getAnalyzer().getCustomContentString(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveTheme = () -> OperationHelper.process(EditorManager.themeEditor::removeMod, ModManager.themeMod.getAnalyzerEn().getCustomContentString(), ModManager.themeMod.getAnalyzerEn().getContentByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemovePublisher = () -> OperationHelper.process(EditorManager.publisherEditor::removeMod, ModManager.publisherMod.getAnalyzer().getCustomContentString(), ModManager.publisherMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveGameplayFeature = () -> OperationHelper.process(EditorManager.gameplayFeatureEditor::removeMod, ModManager.gameplayFeatureMod.getAnalyzer().getCustomContentString(), ModManager.gameplayFeatureMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveEngineFeature = () -> OperationHelper.process(EditorManager.engineFeatureEditor::removeMod, ModManager.engineFeatureMod.getAnalyzer().getCustomContentString(), ModManager.engineFeatureMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveLicence = () -> OperationHelper.process(EditorManager.licenceEditor::removeMod, ModManager.licenceMod.getAnalyzer().getCustomContentString(), ModManager.licenceMod.getAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableAddNewGenre = NewModsHandler::addGenre;
    public static Runnable runnableAddRandomizedGenre = GenreHelper::addRandomizedGenre;
    public static Runnable runnableAddNewTheme = NewModsHandler::addTheme;
    public static Runnable runnableAddNewPublisher = NewModsHandler::addPublisher;
    public static Runnable runnableAddNewLicence = LicenceHelper::addLicence;
    public static Runnable runnableAddNewEngineFeature = EngineFeatureHelper::addEngineFeature;
    public static Runnable runnableAddNewGameplayFeature = GameplayFeatureHelper::addGameplayFeature;
    public static Runnable runnableAddCompanyIcon = NewModsHandler::addCompanyIcon;
    public static Runnable runnableNPCGamesList = NPCGameListHandler::modifyNPCGameList;
    public static Runnable runnableCreateRestorePoint = RestorePointHelper::setRestorePoint;
    public static Runnable runnableRestoreToRestorePoint = RestorePointHelper::restoreToRestorePoint;
    public static Runnable runnableCreateFullBackup = () -> Backup.createBackup("full");
    public static Runnable runnableCreateSaveGameBackup = () -> Backup.createBackup("save_game");
    public static Runnable runnableRestoreInitialBackup = WindowMain::restoreInitialBackup;
    public static Runnable runnableRestoreLatestBackup = WindowMain::restoreLatestBackup;
    public static Runnable runnableRestoreSaveGameBackup = () -> Backup.restoreSaveGameBackup();
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
            WindowMain.checkActionAvailability();
            if(Settings.mgt2FolderIsCorrect){
                WindowMain.lockMenuItems(false);
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