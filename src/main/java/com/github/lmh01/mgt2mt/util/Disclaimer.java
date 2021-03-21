package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Disclaimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Disclaimer.class);

    public static void showDisclaimer(){
        if(Settings.enableDisclaimerMessage){
            String string = I18n.INSTANCE.get("disclaimer.disclaimerText");
            JLabel labelMessage = new JLabel(string);
            Object[] params = {labelMessage};
            LOGGER.info("enableDisclaimerMessage: " + Settings.enableDisclaimerMessage);
            if(Settings.enableDisclaimerMessage){
                if(JOptionPane.showConfirmDialog(null, params, "Important information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    Settings.enableDisclaimerMessage = false;
                    ExportSettings.export();
                }
            }
            WindowMain.checkActionAvailability();
        }
    }
}
