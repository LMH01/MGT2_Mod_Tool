package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Uninstaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Uninstaller.class);

    /**
     * Opens a gui where the user can select what should be removed. Selected items are then removed and the tool closes.
     */
    public static void uninstall(){//TODO Make Image icons that have been added be removed as well
        JLabel labelDescription = new JLabel("<html>Select what should be removed<br>After uninstalling the program is exited");
        JCheckBox checkboxDeleteBackups = new JCheckBox("Delete Backups");
        checkboxDeleteBackups.setSelected(true);
        checkboxDeleteBackups.setToolTipText("Check to delete all backups upon uninstalling");
        JCheckBox checkboxRevertAllMods = new JCheckBox("Revert all mods");
        checkboxRevertAllMods.setSelected(true);
        checkboxRevertAllMods.setToolTipText("Check to revert all mods upon uninstalling");
        JCheckBox checkboxDeleteConfigFiles = new JCheckBox("Delete config files");
        checkboxDeleteConfigFiles.setSelected(true);
        checkboxDeleteConfigFiles.setToolTipText("Check to delete the config file.");
        JCheckBox checkboxDeleteExports = new JCheckBox("Delete Exports");
        checkboxDeleteExports.setSelected(true);
        checkboxDeleteExports.setToolTipText("Check to delete all exports");
        Object[] params = {labelDescription, checkboxDeleteBackups, checkboxRevertAllMods, checkboxDeleteConfigFiles, checkboxDeleteExports};
        if(JOptionPane.showConfirmDialog(null, params, "Uninstall", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            boolean uninstallFailed = false;
            StringBuilder stringActions = new StringBuilder();
            if(checkboxDeleteBackups.isSelected()){
                stringActions.append("Delete Backups").append(System.getProperty("line.separator"));
            }
            if(checkboxRevertAllMods.isSelected()){
                stringActions.append("Revert All Mods").append(System.getProperty("line.separator"));
            }
            if(checkboxDeleteConfigFiles.isSelected()){
                stringActions.append("Delete config files").append(System.getProperty("line.separator"));
            }
            if(checkboxDeleteExports.isSelected()){
                stringActions.append("Delete exports").append(System.getProperty("line.separator"));
            }
            if(JOptionPane.showConfirmDialog(null, "Warning!\nAre you sure that you wan't to do the following:\n\n" + stringActions + "\nThis can't be reverted!", "Confirm uninstall", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                LOGGER.info("Uninstalling...");
                StringBuilder uninstallFailedExplanation = new StringBuilder();
                if(checkboxRevertAllMods.isSelected()){
                    String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                    for (String customGenre : customGenres) {
                        try {
                            EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(customGenre));
                            ImageFileHandler.removeImageFiles(customGenre, AnalyzeExistingGenres.getGenreIdByName(customGenre));
                            LOGGER.info("Game files have been restored to original.");
                        } catch (IOException e) {
                            LOGGER.info("Genre could not be removed: " + e.getMessage());
                            uninstallFailedExplanation.append("Genre could not be removed: ").append(e.getMessage()).append(System.getProperty("line.separator"));
                            e.printStackTrace();
                            uninstallFailed = true;
                        }
                    }
                    String[] customPublishers = AnalyzeExistingPublishers.getCustomPublisherString();
                    for (String customPublisher : customPublishers) {
                        try {
                            EditPublishersFile.removePublisher(customPublisher);
                            LOGGER.info("Publisher files have been restored to original.");
                        } catch (IOException e) {
                            LOGGER.info("Publisher could not be removed: " + e.getMessage());
                            uninstallFailedExplanation.append("Publisher could not be removed: ").append(e.getMessage()).append(System.getProperty("line.separator"));
                            e.printStackTrace();
                            uninstallFailed = true;
                        }
                    }
                    Backup.restoreBackup(true, false);//This is used to restore the Themes files to its original condition
                }
                if(checkboxDeleteBackups.isSelected() && checkboxDeleteConfigFiles.isSelected() && checkboxDeleteExports.isSelected()){
                    File modManagerPath = new File(Settings.MGT2_MOD_MANAGER_PATH);
                    Utils.deleteDirectory(modManagerPath);
                }
                if(checkboxDeleteBackups.isSelected()){
                    File backupFolder = new File(Backup.BACKUP_FOLDER_PATH);
                    Utils.deleteDirectory(backupFolder);
                    LOGGER.info("Backups have been deleted.");
                }
                if(checkboxDeleteConfigFiles.isSelected()){
                    File configFile = new File(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
                    configFile.deleteOnExit();
                    LOGGER.info("Settings file has been deleted.");
                }
                if(checkboxDeleteExports.isSelected()){
                    File exportFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
                    Utils.deleteDirectory(exportFolder);
                    LOGGER.info("Exports have been deleted.");
                }
                if(uninstallFailed){
                    JOptionPane.showMessageDialog(null, "There was a problem while uninstalling:\n\n" + uninstallFailedExplanation, "Uninstall incomplete", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Your selected files have been uninstalled successfully!", "Uninstall successful", JOptionPane.INFORMATION_MESSAGE);
                }
                System.exit(0);
            }
        }
    }
}
