package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import javax.swing.*;

public class ActiveMods {
    public static void showActiveMods() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(I18n.INSTANCE.get("window.showActiveMods.message.firstPart")).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        boolean noModsActive = true;
        for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
            String[] customContent = advancedMod.getBaseAnalyzer().getCustomContentString();
            if(customContent.length > 0){
                stringBuilder.append(advancedMod.getTypePlural()).append(": ");
                Utils.appendStringArrayToStringBuilder(stringBuilder, customContent, 10);
                noModsActive = false;
            }
        }
        for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
            String[] customContent;
            if(simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                customContent = ModManager.themeMod.getAnalyzerEn().getCustomContentString();
            }else {
                customContent = simpleMod.getBaseAnalyzer().getCustomContentString();
            }
            if(customContent.length > 0){
                stringBuilder.append(simpleMod.getTypePlural()).append(": ");
                Utils.appendStringArrayToStringBuilder(stringBuilder, customContent, 10);
                noModsActive = false;
            }
        }
        if(noModsActive){
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.importAll.noModsAvailable"));
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString());
    }
}
