package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);

    private static Runnable runnableExportLicence = () -> OperationHelper.process((string) -> SharingHandler.exportLicence(string, false), AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportEngineFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportEngineFeature(string, false), AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportGameplayFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportGameplayFeature(string, false), AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportThemes = () -> OperationHelper.process((string) -> SharingHandler.exportTheme(string, false), AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportPublisher = () -> OperationHelper.process((string) -> SharingHandler.exportPublisher(string, false), AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportGenre = () -> OperationHelper.process((string) -> SharingHandler.exportGenre(string, false), AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
    private static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    private static Runnable runnableUninstall = Uninstaller::uninstall;
    private static Runnable runnableImportAll = SharingManager::importAll;
    private static Runnable runnableShowActiveMods = ActiveMods::showActiveMods;
    private static Runnable runnableDeleteExports = Uninstaller::deleteAllExports;
    private static Runnable runnableReplacePublisherWithRealPublishers = PublisherHelper::realPublishers;
    private static Runnable runnableImportFromURL = ImportFromURLHelper::importFromURL;
    private static Runnable runnableCheckForUpdates = () -> UpdateChecker.checkForUpdates(true);
    private static Runnable runnableRemoveGenre = () -> OperationHelper.process(EditGenreFile::removeGenre, AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), I18n.INSTANCE.get("commonText.genre"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    private static Runnable runnableRemoveTheme = () -> OperationHelper.process(EditThemeFiles::removeTheme, AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.removeed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    private static Runnable runnableRemovePublisher = () -> OperationHelper.process(EditPublishersFile::removePublisher, AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), I18n.INSTANCE.get("commonText.publisher"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    private static Runnable runnableRemoveGameplayFeature = () -> OperationHelper.process(EditGameplayFeaturesFile::removeGameplayFeature, AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), I18n.INSTANCE.get("commonText.gameplayFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    private static Runnable runnableRemoveEngineFeature = () -> OperationHelper.process(EditEngineFeaturesFile::removeEngineFeature, AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), I18n.INSTANCE.get("commonText.engineFeature"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    private static Runnable runnableRemoveLicence = () -> OperationHelper.process(EditLicenceFile::removeLicence, AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), I18n.INSTANCE.get("commonText.licence"), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
    public static Runnable runnableDoOnShutdown = () -> {
        LOGGER.info("Performing exit tasks...");
        LogFile.stopLogging();
        LOGGER.info("Exit tasks complete. Good night");
    };

    public static Thread threadDeleteTempFolder = new Thread(() -> {
        WindowMain.lockMenuItems(true);
        File tempFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//");
        if(tempFolder.exists()){
            DataStreamHelper.deleteDirectory(tempFolder);
            LOGGER.info("Deleted temp folder.");
        }
        WindowMain.lockMenuItems(false);
    });

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
            case "runnableDeleteExports": thread = new Thread(runnableDeleteExports);break;
            case "runnableReplacePublisherWithRealPublishers": thread = new Thread(runnableReplacePublisherWithRealPublishers);break;
            case "runnableImportFromURL": thread = new Thread(runnableImportFromURL);break;
            case "runnableCheckForUpdates": thread = new Thread(runnableCheckForUpdates);break;
            case "runnableRemoveGenre": thread = new Thread(runnableRemoveGenre);break;
            case "runnableRemoveTheme": thread = new Thread(runnableRemoveTheme);break;
            case "runnableRemovePublisher": thread = new Thread(runnableRemovePublisher);break;
            case "runnableRemoveGameplayFeature": thread = new Thread(runnableRemoveGameplayFeature);break;
            case "runnableRemoveEngineFeature": thread = new Thread(runnableRemoveEngineFeature);break;
            case "runnableRemoveLicence": thread = new Thread(runnableRemoveLicence);break;
            default:
                throw new IllegalStateException("This thread name is not accepted: " + threadName);
        }
        thread.start();
        WindowMain.lockMenuItems(true);
    }
}