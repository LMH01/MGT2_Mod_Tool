package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RestorePointHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestorePointHelper.class);
    /**
     * Exports all currently installed mods to this folder: "MGT2_Mod_Manager\Mod_Restore_Point\Current_Restore_Point"
     * When a restore point has already been set the old files will be moved in a storage folder.
     */
    public static void setRestorePoint(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.restorePoint.createRestorePoint"), I18n.INSTANCE.get("dialog.restorePoint.createRestorePoint.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.startingRestorePoint"));
            if(ModManagerPaths.CURRENT_RESTORE_POINT.toFile().exists()){
                LOGGER.info("Restore Point does already exist");
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.restorePoint.restorePointDoesAlreadyExist"), I18n.INSTANCE.get("dialog.restorePoint.restorePointDoesAlreadyExist.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    LOGGER.info("Moving old restore point to storage folder");
                    try {
                        DataStreamHelper.copyDirectory(ModManagerPaths.CURRENT_RESTORE_POINT.getPath(), ModManagerPaths.OLD_RESTORE_POINT.getPath().resolve(Utils.getCurrentDateTime()));
                        LOGGER.info("Old restore point has been copied into the storage folder");
                        DataStreamHelper.deleteDirectory(ModManagerPaths.CURRENT_RESTORE_POINT.getPath());
                        LOGGER.info("Old restore point has been deleted from folder: " + ModManagerPaths.CURRENT_RESTORE_POINT.getPath());
                        SharingManager.exportAll(true);
                    } catch (IOException e) {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.errorWhileMoving"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.restorePoint.moveRestorePointFailed"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }else{
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.restorePointNotCreatedAlreadyExists"));
                }
            }else{
                SharingManager.exportAll(true);
            }
        }
    }

    /**
     * This will remove all currently installed mods and replace them with the mods that have been exported previously as restore point
     */
    public static void restoreToRestorePoint(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.restorePoint.restore.mainDialog"), I18n.INSTANCE.get("dialog.restorePoint.restore.mainDialog.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.restoreToRestorePoint"));
            SharingManager.importAll(true, ModManagerPaths.CURRENT_RESTORE_POINT.getPath());
        }
    }
}
