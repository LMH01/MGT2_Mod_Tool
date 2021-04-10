package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);

    private static Runnable runnableExportLicence = () -> OperationHelper.process((string) -> SharingHandler.exportLicence(string, false), AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), "licence", "exported", "Export", true);
    private static Runnable runnableExportEngineFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportEngineFeature(string, false), AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), "engine feature", "exported", "Export", true);
    private static Runnable runnableExportGameplayFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportGameplayFeature(string, false), AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), "gameplay feature", "exported", "Export", true);
    private static Runnable runnableExportThemes = () -> OperationHelper.process((string) -> SharingHandler.exportTheme(string, false), AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), "themes", "exported", "Export", true);
    private static Runnable runnableExportPublisher = () -> OperationHelper.process((string) -> SharingHandler.exportPublisher(string, false), AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), "publisher", "exported", "Export", true);
    private static Runnable runnableExportGenre = () -> OperationHelper.process((string) -> SharingHandler.exportGenre(string, false), AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), "genre", "exported", "Export", true);
    private static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    private static Runnable runnableUninstall = () -> Uninstaller.uninstall();
    private static Runnable runnableImportAll = () -> SharingManager.importAll();
    private static Runnable runnableShowActiveMods = () -> ActiveMods.showActiveMods();
    public static Runnable runnableDoOnShutdown = () -> {
        LOGGER.info("Performing exit tasks...");
        LogFile.stopLogging();
        LOGGER.info("Exit tasks complete. Good night");
    };

    /**
     * Starts the given thread.
     * Also sets the scroll pane to be locked.
     */
    public static void startThread(String threadName){
        TextAreaHelper.setScrollDown();
        Thread thread;
        switch(threadName){
            case "runnableExportEngineFeatures": thread  = new Thread(ThreadHandler.runnableExportEngineFeatures);break;
            case "runnableExportGameplayFeatures": thread  = new Thread(ThreadHandler.runnableExportGameplayFeatures);break;
            case "runnableExportThemes": thread  = new Thread(ThreadHandler.runnableExportThemes);break;
            case "runnableExportPublisher": thread  = new Thread(ThreadHandler.runnableExportPublisher);break;
            case "runnableExportLicence": thread  = new Thread(ThreadHandler.runnableExportLicence);break;
            case "runnableExportGenre": thread  = new Thread(ThreadHandler.runnableExportGenre);break;
            case "runnableExportAll": thread  = new Thread(ThreadHandler.runnableExportAll);break;
            case "runnableUninstall": thread  = new Thread(ThreadHandler.runnableUninstall);break;
            case "runnableImportAll": thread  = new Thread(ThreadHandler.runnableImportAll);break;
            case "runnableShowActiveMods": thread  = new Thread(ThreadHandler.runnableShowActiveMods);break;
            default:
                throw new IllegalStateException("This thread name is not accepted: " + threadName);
        }
        thread.start();
    }
}