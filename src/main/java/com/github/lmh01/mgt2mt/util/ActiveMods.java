package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import javax.swing.*;

public class ActiveMods {
    public static void showActiveMods() {
        String[] customEngineFeatures = AnalyzeManager.engineFeatureAnalyzer.getCustomContentString();
        String[] customGameplayFeatures = AnalyzeManager.gameplayFeatureAnalyzer.getCustomContentString();
        String[] customGenres = AnalyzeManager.genreAnalyzer.getCustomContentString();
        String[] customPublishers = AnalyzeManager.genreAnalyzer.getCustomContentString();
        String[] customThemes = AnalyzeManager.themeFileEnAnalyzer.getCustomContentString();
        String[] customLicences = AnalyzeManager.licenceAnalyzer.getCustomContentString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(I18n.INSTANCE.get("window.showActiveMods.message.firstPart")).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        boolean noModsActive = true;
        if(customEngineFeatures.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName1")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customEngineFeatures, 10);
            noModsActive = false;
        }
        if(customGameplayFeatures.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName2")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customGameplayFeatures, 10);
            noModsActive = false;
        }
        if(customGenres.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName3")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customGenres, 10);
            noModsActive = false;
        }
        if(customPublishers.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName4")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customPublishers, 10);
            noModsActive = false;
        }
        if(customThemes.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName5")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customThemes, 10);
            noModsActive = false;
        }
        if(customLicences.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName6")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customLicences, 10);
            noModsActive = false;
        }
        if(noModsActive){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.noModsAvailable"));
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString());
    }
}
