package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.mod.managed.ModManager;

import javax.swing.*;

public class ActiveMods {
    public static void showActiveMods() {
        String[] customEngineFeatures = ModManager.engineFeatureMod.getAnalyzer().getCustomContentString();
        String[] customGameplayFeatures = ModManager.gameplayFeatureMod.getAnalyzer().getCustomContentString();
        String[] customGenres = ModManager.genreMod.getAnalyzer().getCustomContentString();
        String[] customPublishers = ModManager.publisherMod.getAnalyzer().getCustomContentString();
        String[] customThemes = ModManager.themeMod.getAnalyzerEn().getCustomContentString();
        String[] customLicences = ModManager.licenceMod.getAnalyzer().getCustomContentString();
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
