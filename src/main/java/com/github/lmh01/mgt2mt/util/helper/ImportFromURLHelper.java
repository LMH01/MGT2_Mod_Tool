package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.github.lmh01.mgt2mt.util.Utils;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ImportFromURLHelper {
    /**
     * Opens a window where the user can enter a url. The content behind the url is then downloaded, extracted and searched for mods.
     * A summary is then shown what mods can be imported.
     */
    public static void importFromURL(){
        JLabel labelText = new JLabel(I18n.INSTANCE.get("window.importFromURL.labelText"));
        JTextField textFieldUrl = new JTextField();
        textFieldUrl.setToolTipText(I18n.INSTANCE.get("window.importFromURL.textFieldURL.toolTip"));

        Object[] params = {labelText, textFieldUrl};
        if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("window.importFromURL.dialog.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION){
            try{
                ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.downloadZip"), false, false);
                String currentDateTime = Utils.getCurrentDateTime();
                File downloadZip = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//" + currentDateTime + "download.zip");
                File downloadUnzipped = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//" + currentDateTime + "download");
                DataStreamHelper.downloadZip(textFieldUrl.getText(), downloadZip.getPath());
                DataStreamHelper.unzip(downloadZip.getPath(), downloadUnzipped);
                SharingManager.importAll(false, downloadUnzipped.getPath());
            }catch(IOException e){
                e.printStackTrace();
                String errorMessageToDisplay;
                if(e.getMessage().equals("www.dropbox.com")){
                    errorMessageToDisplay = I18n.INSTANCE.get("commonText.noInternet");
                }else{
                    errorMessageToDisplay = e.getMessage();
                }
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.importFromURL.error.body") + "\n" + I18n.INSTANCE.get("commonBodies.exception") + " " + errorMessageToDisplay, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
