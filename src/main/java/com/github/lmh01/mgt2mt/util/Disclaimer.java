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
            JLabel labelMessage = new JLabel("<html>Important information:" +
                    "<br>" +
                    "<br>Backups will get created automatically but please created your own safety" +
                    "<br>copy in case something went wrong with the backups that this tool creates." +
                    "<br>Your save game files can be found by clicking" +
                    "<br>\"Open Save Game Location\" located in the utilities menu." +
                    "<br>Mods that have been applied may only take effect when you start a new game." +
                    "<br>Removing mods later won't necessarily remove them from your save games." +
                    "<br>" +
                    "<br>Note:" +
                    "<br>Most spinners are locked to prevent the input of values that might break the game balance." +
                    "<br>This can be circumvented by disabling the safety features in the settings." +
                    "<br>In case you don't know what an input field does hover over it with your mouse." +
                    "<br>If you need additional help visit the Github repository and read the README.md file." +
                    "<br>The Github repository can be opened by clicking Utilities -> Open Github Page." +
                    "<br>If you encounter a bug please report it so i can fix it." +
                    "<br>" +
                    "<br>By clicking ok you accept that you have read this information.");
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
