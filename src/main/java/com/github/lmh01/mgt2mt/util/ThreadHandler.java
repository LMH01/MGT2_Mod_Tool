package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);
    private static int threadsRunning = 0;
    private static String[] controlThreadBlacklist = {"runnableCheckForUpdates"};
    public static Runnable runnableExportLicence = () -> OperationHelper.process((string) -> SharingHandler.exportLicence(string, false), AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportEngineFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportEngineFeature(string, false), AnalyzeManager.engineFeatureAnalyzer.getCustomContentString(), AnalyzeManager.engineFeatureAnalyzer.getContentByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportGameplayFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportGameplayFeature(string, false), AnalyzeManager.gameplayFeatureAnalyzer.getCustomContentString(), AnalyzeManager.gameplayFeatureAnalyzer.getContentByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportThemes = () -> OperationHelper.process((string) -> SharingHandler.exportTheme(string, false), AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportPublisher = () -> OperationHelper.process((string) -> SharingHandler.exportPublisher(string, false), AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportGenre = () -> OperationHelper.process((string) -> SharingHandler.exportGenre(string, false), AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    public static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    public static Runnable runnableUninstall = Uninstaller::uninstall;
    public static Runnable runnableImportAll = SharingManager::importAll;
    public static Runnable runnableShowActiveMods = ActiveMods::showActiveMods;
    public static Runnable runnableDeleteExports = Uninstaller::deleteAllExports;
    public static Runnable runnableReplacePublisherWithRealPublishers = PublisherHelper::realPublishers;
    public static Runnable runnableImportFromURL = ImportFromURLHelper::importFromURL;
    public static Runnable runnableRemoveGenre = () -> OperationHelper.process(EditGenreFile::removeGenre, AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveTheme = () -> OperationHelper.process(EditThemeFiles::removeTheme, AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemovePublisher = () -> OperationHelper.process(EditPublishersFile::removePublisher, AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveGameplayFeature = () -> OperationHelper.process(EditGameplayFeaturesFile::removeGameplayFeature, AnalyzeManager.gameplayFeatureAnalyzer.getCustomContentString(), AnalyzeManager.gameplayFeatureAnalyzer.getContentByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveEngineFeature = () -> OperationHelper.process(EditEngineFeaturesFile::removeEngineFeature, AnalyzeManager.engineFeatureAnalyzer.getCustomContentString(), AnalyzeManager.engineFeatureAnalyzer.getContentByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableRemoveLicence = () -> OperationHelper.process(EditLicenceFile::removeLicence, AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
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