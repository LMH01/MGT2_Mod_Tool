package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadHandler.class);

    public static Runnable runnableExportLicence = () -> OperationHelper.process((string) -> SharingHandler.exportLicence(string, false), AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), "licence", "exported", "Export", true);
    public static Runnable runnableExportEngineFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportEngineFeature(string, false), AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), "engine feature", "exported", "Export", true);
    public static Runnable runnableExportGameplayFeatures = () -> OperationHelper.process((string) -> SharingHandler.exportGameplayFeature(string, false), AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), "gameplay feature", "exported", "Export", true);
    public static Runnable runnableExportThemes = () -> OperationHelper.process((string) -> SharingHandler.exportTheme(string, false), AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), "themes", "exported", "Export", true);
    public static Runnable runnableExportPublisher = () -> OperationHelper.process((string) -> SharingHandler.exportPublisher(string, false), AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), "publisher", "exported", "Export", true);
    public static Runnable runnableExportGenre = () -> OperationHelper.process((string) -> SharingHandler.exportGenre(string, false), AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), "genre", "exported", "Export", true);
    public static Runnable runnableExportAll = () -> SharingManager.exportAll(false);
    public static Runnable runnableUninstall = () -> Uninstaller.uninstall();

    public static Runnable runnableDoOnShutdown = () -> {
        LOGGER.info("Performing exit tasks...");
        LogFile.stopLogging();
        LOGGER.info("Exit tasks complete. Good night");
    };
}