package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Uninstaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Uninstaller.class);

    /**
     * Opens a gui where the user can select what should be removed. Selected items are then removed and the tool closes.
     */
    public static void uninstall(){
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
                            uninstallFailed = uninstallAllMods(uninstallFailedExplanation);
                        }
                        if(checkboxDeleteBackups.isSelected() && checkboxDeleteConfigFiles.isSelected() && checkboxDeleteExports.isSelected()){
                            File modManagerPath = new File(Settings.MGT2_MOD_MANAGER_PATH);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deleteModManagerFiles"));
                            LogFile.stopLogging();
                            DataStreamHelper.deleteDirectory(modManagerPath);
                            exitProgram = true;
                        }else{
                            if(checkboxDeleteBackups.isSelected()){
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingBackups"));
                                File backupFolder = new File(Backup.BACKUP_FOLDER_PATH);
                                if(Settings.disableSafetyFeatures){
                                    DataStreamHelper.deleteDirectory(backupFolder);
                                }else{
                                    ArrayList<File> filesInBackupFolder = DataStreamHelper.getFilesInFolder(backupFolder.getPath());
                                    for(File file : filesInBackupFolder){
                                        if(!file.getPath().endsWith(".initialBackup")){
                                            DataStreamHelper.deleteDirectory(file);
                                        }
                                    }
                                }
                                LOGGER.info("Backups have been deleted.");
                            }
                            if(checkboxDeleteConfigFiles.isSelected()){
                                File configFile = new File(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingSettings"));
                                configFile.deleteOnExit();
                                LOGGER.info("Settings file has been deleted.");
                                exitProgram = true;
                            }
                            if(checkboxDeleteExports.isSelected()){
                                File exportFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.deletingExports"));
                                DataStreamHelper.deleteDirectory(exportFolder);
                                LOGGER.info("Exports have been deleted.");
                            }
                        }
                        if(uninstallFailed){
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete") + "\n\n" + uninstallFailedExplanation.toString(), I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.title"), JOptionPane.WARNING_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallSuccessful"), I18n.INSTANCE.get("window.uninstall.uninstallSuccessful.title"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        if(exitProgram){
                            System.exit(0);
                        }
                    }
                }
            }
            break;
        }
    }

    /**
     * Removes all currently installed mods
     * @param uninstallFailedExplanation This string builder contains reasons in case the removal of some mods fails
     * @return Returns false when the removal of mods was successful
     */
    public static boolean uninstallAllMods(StringBuilder uninstallFailedExplanation){
        boolean uninstallFailed = false;
        String[] customGenres = AnalyzeManager.genreAnalyzer.getCustomContentString();
        String[] customPublishers = AnalyzeManager.publisherAnalyzer.getCustomContentString();
        String[] customGameplayFeatures = AnalyzeManager.gameplayFeatureAnalyzer.getCustomContentString();
        String[] customEngineFeatures = AnalyzeManager.engineFeatureAnalyzer.getCustomContentString();
        String[] customThemes = AnalyzeManager.themeFileEnAnalyzer.getCustomContentString();
        String[] customLicences = AnalyzeManager.licenceAnalyzer.getCustomContentString();
        if(customGenres.length + customPublishers.length + customGameplayFeatures.length + customEngineFeatures.length + customThemes.length + customLicences.length!= 0){
            ProgressBarHelper.initializeProgressBar(0, customGenres.length + customPublishers.length, I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods"), true);
            for (String customGenre : customGenres) {
                try {
                    EditGenreFile.removeGenre(customGenre);
                } catch (IOException e) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.genre.failed") + " " + customGenre + "; " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
                    LOGGER.info("Genre could not be removed: " + e.getMessage());
                    uninstallFailedExplanation.append(I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.genreCouldNotBeRemoved")).append(" ").append(e.getMessage()).append(System.getProperty("line.separator"));
                    e.printStackTrace();
                    uninstallFailed = true;
                }
                ProgressBarHelper.increment();
            }
            for (String customPublisher : customPublishers) {
                try {
                    EditPublishersFile.removePublisher(customPublisher);
                    LOGGER.info("Publisher files have been restored to original.");
                } catch (IOException e) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.uninstalling.uninstallingAllMods.publisher.failed") + " " + customPublisher + "; " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
                    LOGGER.info("Publisher could not be removed: " + e.getMessage());
                    uninstallFailedExplanation.append(I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.publisherCouldNotBeRemoved")).append(" ").append(e.getMessage()).append(System.getProperty("line.separator"));
                    e.printStackTrace();
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
    public static void deleteAllExports(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete all exports?", "Delete exports?", JOptionPane.YES_NO_OPTION) == 0){
            DataStreamHelper.deleteDirectory(new File(Utils.getMGT2ModToolExportFolder()));
            JOptionPane.showMessageDialog(null, "All exports have been deleted.");
        }
    }
}
