package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Uninstaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Uninstaller.class);

    /**
     * Opens a gui where the user can select what should be removed. Selected items are then removed and the tool closes.
     */
    public static void uninstall() throws ModProcessingException {
        JLabel labelDescription = new JLabel(I18n.INSTANCE.get("window.uninstall.labelDescription"));
        JCheckBox checkboxDeleteBackups = new JCheckBox(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteBackups"));
        checkboxDeleteBackups.setSelected(true);
        checkboxDeleteBackups.setToolTipText(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteBackups.toolTip"));
        JCheckBox checkboxRevertAllMods = new JCheckBox(I18n.INSTANCE.get("window.uninstall.checkBoxRevertAllMods"));
        checkboxRevertAllMods.setSelected(true);
        checkboxRevertAllMods.setToolTipText(I18n.INSTANCE.get("window.uninstall.checkBoxRevertAllMods.toolTip"));
        JCheckBox checkboxDeleteConfigFiles = new JCheckBox(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteConfigFiles"));
        checkboxDeleteConfigFiles.setSelected(true);
        checkboxDeleteConfigFiles.setToolTipText(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteConfigFiles.toolTip"));
        JCheckBox checkboxDeleteExports = new JCheckBox(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteExports"));
        checkboxDeleteExports.setSelected(true);
        checkboxDeleteExports.setToolTipText(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteExports.toolTip"));
        Object[] params = {labelDescription, checkboxDeleteBackups, checkboxRevertAllMods, checkboxDeleteConfigFiles, checkboxDeleteExports};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("window.uninstall.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                boolean uninstallFailed = false;
                StringBuilder stringActions = new StringBuilder();
                if(checkboxDeleteBackups.isSelected()){
                    stringActions.append(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteBackups")).append(System.getProperty("line.separator"));
                }
                if(checkboxRevertAllMods.isSelected()){
                    stringActions.append(I18n.INSTANCE.get("window.uninstall.checkBoxRevertAllMods")).append(System.getProperty("line.separator"));
                }
                if(checkboxDeleteConfigFiles.isSelected()){
                    stringActions.append(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteConfigFiles")).append(System.getProperty("line.separator"));
                }
                if(checkboxDeleteExports.isSelected()){
                    stringActions.append(I18n.INSTANCE.get("window.uninstall.checkBoxDeleteExports")).append(System.getProperty("line.separator"));
                }
                if(!checkboxDeleteBackups.isSelected() && !checkboxRevertAllMods.isSelected() && !checkboxDeleteConfigFiles.isSelected() && !checkboxDeleteExports.isSelected()){
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.nothingSelected"), I18n.INSTANCE.get("window.uninstall.nothingSelected.title"), JOptionPane.ERROR_MESSAGE);
                }else{
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.uninstall.confirmMessage.firstPart") + "\n\n" + stringActions + "\n" + I18n.INSTANCE.get("window.uninstall.confirmMessage.secondPart"), I18n.INSTANCE.get("window.uninstall.confirmMessage.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        LOGGER.info("Uninstalling...");
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling"));
                        ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("textArea.uninstalling"));
                        StringBuilder uninstallFailedExplanation = new StringBuilder();
                        boolean exitProgram = false;
                        if(checkboxRevertAllMods.isSelected()){
                            try {
                                uninstallFailed = uninstallAllMods(uninstallFailedExplanation);
                            } catch (ModProcessingException e) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.error"));
                                TextAreaHelper.printStackTrace(e);
                            }
                        }
                        if(checkboxDeleteBackups.isSelected() && checkboxDeleteConfigFiles.isSelected() && checkboxDeleteExports.isSelected()){
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deleteModManagerFiles"));
                            LogFile.stopLogging();
                            try {
                                DataStreamHelper.deleteDirectory(ModManagerPaths.MAIN.getPath());
                            } catch (IOException e) {
                                throw new ModProcessingException("Unable to delete mod manager directory", e);
                            }
                            exitProgram = true;
                        }else{
                            if(checkboxDeleteBackups.isSelected()){
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingBackups"));
                                if(Settings.disableSafetyFeatures){
                                    try {
                                        DataStreamHelper.deleteDirectory(ModManagerPaths.BACKUP.getPath());
                                    } catch (IOException e) {
                                        throw new ModProcessingException("Unable to delete mod backup folder", e);
                                    }
                                }else{
                                    ArrayList<File> filesInBackupFolder = DataStreamHelper.getFilesInFolder(ModManagerPaths.BACKUP.getPath());
                                    for(File file : filesInBackupFolder){
                                        if(!file.getPath().endsWith(".initialBackup")){
                                            try {
                                                Files.delete(file.toPath());
                                            } catch (IOException e) {
                                                throw new ModProcessingException("Unable to delete backup", e);
                                            }
                                        }
                                    }
                                }
                                LOGGER.info("Backups have been deleted.");
                            }
                            if(checkboxDeleteConfigFiles.isSelected()){
                                File configFile = ModManagerPaths.MAIN.getPath().resolve("settings.txt").toFile();
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingSettings"));
                                configFile.deleteOnExit();
                                LOGGER.info("Settings file has been deleted.");
                                exitProgram = true;
                            }
                            if(checkboxDeleteExports.isSelected()){
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingExports"));
                                try {
                                    DataStreamHelper.deleteDirectory(ModManagerPaths.EXPORT.getPath());
                                } catch (IOException e) {
                                    throw new ModProcessingException("Unable to delete export folder", e);
                                }
                                LOGGER.info("Exports have been deleted.");
                            }
                        }
                        if(uninstallFailed){
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete") + "\n\n" + uninstallFailedExplanation, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.title"), JOptionPane.WARNING_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallSuccessful"), I18n.INSTANCE.get("window.uninstall.uninstallSuccessful.title"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        if(exitProgram){
                            System.exit(0);
                        }
                    }
                    break;
                }
            }else{
                break;
            }
        }
    }

    /**
     * Removes all currently installed mods.
     * Will write a message to the text area in case the removal of some mods fails.
     * Uses {@link Uninstaller#uninstallAllMods(StringBuilder)} to remove the mods.
     */
    public static void uninstallAllMods() {
        StringBuilder failedUninstalls = new StringBuilder();
        boolean modRemovalFailed;
        try {
            modRemovalFailed = Uninstaller.uninstallAllMods(failedUninstalls);
        } catch (ModProcessingException e) {
            TextAreaHelper.printStackTrace(e);
            modRemovalFailed = true;
        }
        if(modRemovalFailed){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.notAllModsRemoved") + " " + failedUninstalls);
            LOGGER.info("Something went wrong while uninstalling mods, however this should not necessarily impact the uninstall process: " + failedUninstalls);
        }else{
            LOGGER.info("All mods have been removed");
        }
    }

    /**
     * Removes all currently installed mods
     * @param uninstallFailedExplanation This string builder contains reasons in case the removal of some mods fails
     * @return Returns false when the removal of mods was successful
     */
    public static boolean uninstallAllMods(StringBuilder uninstallFailedExplanation) throws ModProcessingException {
        boolean uninstallFailed = false;
        ArrayList<String> customContentArrayList = new ArrayList<>();
        for (AbstractBaseMod mod : ModManager.mods) {
            customContentArrayList.addAll(Arrays.asList(mod.getCustomContentString()));
        }
        if(customContentArrayList.size() != 0){
            ProgressBarHelper.initializeProgressBar(0, customContentArrayList.size(), I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods"), true);
            for (AbstractBaseMod mod : ModManager.mods) {
                String currentMod = "";
                try {
                    for (String string : mod.getCustomContentString()) {
                        currentMod = string;
                        mod.removeMod(string);
                    }
                } catch (ModProcessingException e) {
                    TextAreaHelper.printStackTrace(e);
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.mod.failed") + " " + currentMod + " - " + customContentArrayList + "; " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
                    LOGGER.info("Mod of type " + mod.getType() + " could not be removed: " + e.getMessage());
                    uninstallFailedExplanation.append(e.getMessage()).append(System.getProperty("line.separator"));
                    uninstallFailed = true;
                }
                ProgressBarHelper.increment();
            }
            Backup.restoreBackup(true, false);//This is used to restore the Themes files to its original condition
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.success"));
            LOGGER.info("Game files have been restored to original.");
        }else{
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.noModsFound"));
        }
        return uninstallFailed;
    }

    public static void deleteAllExports() throws ModProcessingException{
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.uninstall.exports.message"), I18n.INSTANCE.get("frame.title.areYouSure"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                DataStreamHelper.deleteDirectory(ModManagerPaths.EXPORT.getPath());
            } catch (IOException e) {
                throw new ModProcessingException("Unable to delete export folder", e);
            }
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.exports.success"));
        }
    }
}
