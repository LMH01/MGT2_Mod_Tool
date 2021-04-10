package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import javax.swing.*;

public class ActiveMods {
    public static void showActiveMods() {
        String[] customEngineFeatures = AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString();
        String[] customGameplayFeatures = AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString();
        String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
        String[] customPublishers = AnalyzeExistingPublishers.getCustomPublisherString();
        String[] customThemes = AnalyzeExistingThemes.getCustomThemesByAlphabet();
        String[] customLicences = AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(I18n.INSTANCE.get("window.showActiveMods.message.firstPart")).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        if(customEngineFeatures.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName1")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customEngineFeatures, 10);
        }
        if(customGameplayFeatures.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName2")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customGameplayFeatures, 10);
        }
        if(customGenres.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName3")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customGenres, 10);
        }
        if(customPublishers.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName4")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customPublishers, 10);
        }
        if(customThemes.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName5")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customThemes, 10);
        }
        if(customLicences.length > 0){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.importName6")).append(": ");
            Utils.appendStringArrayToStringBuilder(stringBuilder, customLicences, 10);
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString());
    }
}
