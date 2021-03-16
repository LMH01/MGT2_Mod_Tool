package com.github.lmh01.mgt2mt.util;

import javax.swing.*;
import java.util.Map;

public class Summaries {
    //Contains functions that show messages to the user, asking to confirm the import/addition of feature

    /**
     * Opens a message to the user asking to confirm the addition of engine feature
     * @param map The map containing the values
     * @return Returns true if user clicked ok. Returns false if user clicked something else
     */
    public static boolean showEngineFeatureMessage(Map<String, String> map){
        String messageBody = "Your engine feature is ready:\n\n" +
                "Name: " + map.get("NAME EN") + "\n" +
                "Description: " + map.get("DESC EN") + "\n" +
                "Unlock date: " + map.get("DATE") + "\n" +
                "Type: " + EngineFeatureHelper.getEngineFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                "Research point cost: " + map.get("RES POINTS") + "\n" +
                "Research cost " + map.get("PRICE") + "\n" +
                "Development cost: " + map.get("DEV COSTS") + "\n" +
                "Tech level: " + map.get("TECHLEVEL") + "\n" +
                "\n*Points*\n\n" +
                "Gameplay: " + map.get("GAMEPLAY") + "\n" +
                "Graphic: " + map.get("GRAPHIC") + "\n" +
                "Sound: " + map.get("SOUND") + "\n" +
                "Tech: " + map.get("TECH") + "\n";
        if(JOptionPane.showConfirmDialog(null, messageBody, "Add gameplay feature?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            return true;
        }else{
            return false;
        }
    }
}
