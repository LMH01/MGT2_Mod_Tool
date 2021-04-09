package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class RestorePointHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestorePointHelper.class);
    /**
     * Exports all currently installed mods to this folder: "MGT2_Mod_Manager\Mod_Restore_Point\Current_Restore_Point"
     * When a restore point has already been set the old files will be moved in a storage folder.
     */
    public static void setRestorePoint(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.restorePoint.createRestorePoint"), I18n.INSTANCE.get("dialog.restorePoint.createRestorePoint.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            if(new File(Utils.getMGT2ModToolModRestorePointFolder()).exists()){
                LOGGER.info("Restore Point does already exist");
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.restorePoint.restorePointDoesAlreadyExist"), I18n.INSTANCE.get("dialog.restorePoint.restorePointDoesAlreadyExist.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    LOGGER.info("Moving old restore point to storage folder");
                    File restorePointFolder = new File(Utils.getMGT2ModToolModRestorePointFolder());
                    File restorePointStorageFolder = new File(Utils.getMGT2ModToolModRestorePointStorageFolder() + "//" + Utils.getCurrentDateTime());
                    File restorePointStorageFolderMainFolder = new File(Utils.getMGT2ModToolModRestorePointStorageFolder());
                    try {
                        if(!restorePointStorageFolderMainFolder.exists()){
                            restorePointStorageFolderMainFolder.mkdirs();
                        }
                        DataStreamHelper.copyDirectory(restorePointFolder.getPath(), restorePointStorageFolder.getPath());
                        LOGGER.info("Old restore point has been copied into the storage folder");
                        DataStreamHelper.deleteDirectory(restorePointFolder);
                        LOGGER.info("Old restore point has been deleted from folder: " + Utils.getMGT2ModToolModRestorePointFolder());
                        SharingManager.exportAll(true);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.restorePoint.moveRestorePointFailed"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }else{
                SharingManager.exportAll(true);
            }
        }
    }
}
