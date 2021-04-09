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
                        StringBuilder uninstallFailedExplanation = new StringBuilder();
                        if(checkboxRevertAllMods.isSelected()){
                            uninstallFailed = uninstallAllMods(uninstallFailedExplanation);
                        }
                        if(checkboxDeleteBackups.isSelected() && checkboxDeleteConfigFiles.isSelected() && checkboxDeleteExports.isSelected()){
                            File modManagerPath = new File(Settings.MGT2_MOD_MANAGER_PATH);
                            DataStreamHelper.deleteDirectory(modManagerPath);
                        }
                        if(checkboxDeleteBackups.isSelected()){
                            File backupFolder = new File(Backup.BACKUP_FOLDER_PATH);
                            DataStreamHelper.deleteDirectory(backupFolder);
                            LOGGER.info("Backups have been deleted.");
                        }
                        if(checkboxDeleteConfigFiles.isSelected()){
                            File configFile = new File(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
                            configFile.deleteOnExit();
                            LOGGER.info("Settings file has been deleted.");
                        }
                        if(checkboxDeleteExports.isSelected()){
                            File exportFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
                            DataStreamHelper.deleteDirectory(exportFolder);
                            LOGGER.info("Exports have been deleted.");
                        }
                        if(uninstallFailed){
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete") + "\n\n" + uninstallFailedExplanation, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.title"), JOptionPane.WARNING_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallSuccessful"), I18n.INSTANCE.get("window.uninstall.uninstallSuccessful.title"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        System.exit(0);
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
        String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
        for (String customGenre : customGenres) {
            try {
                EditGenreFile.removeGenre(customGenre);
                LOGGER.info("Game files have been restored to original.");
            } catch (IOException e) {
                LOGGER.info("Genre could not be removed: " + e.getMessage());
                uninstallFailedExplanation.append(I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.genreCouldNotBeRemoved")).append(" ").append(e.getMessage()).append(System.getProperty("line.separator"));
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
                uninstallFailedExplanation.append(I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.publisherCouldNotBeRemoved")).append(" ").append(e.getMessage()).append(System.getProperty("line.separator"));
                e.printStackTrace();
                uninstallFailed = true;
            }
        }
        Backup.restoreBackup(true, false);//This is used to restore the Themes files to its original condition
        return uninstallFailed;
    }
}
