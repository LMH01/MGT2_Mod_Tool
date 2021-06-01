package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import javax.swing.*;

public class GameUpdateHandler {
    public static void performUpdateTasks(){
        SharingManager.exportAll(true, false);
        Backup.restoreBackup(true, false);
        Backup.createInitialBackup(true);
        SharingManager.importAll(true, Utils.getMGT2ModToolModRestorePointFolder());
    }

    public static void showDialog(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.gameUpdateHandler.mainMessage"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            performUpdateTasks();
        }
    }
}
