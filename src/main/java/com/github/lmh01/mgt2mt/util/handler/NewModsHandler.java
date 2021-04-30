package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewModsHandler {
    public static void addCompanyIcon(){
        String imageFilePath = Utils.getImagePath();
        File imageFileSource = new File(imageFilePath);
        if(!imageFilePath.equals("canceled")){
            if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                File targetImage = new File(Utils.getMGT2CompanyLogosPath() + "//" + CompanyLogoAnalyzer.getLogoNumber() + ".png");
                try {
                    Files.copy(Paths.get(imageFileSource.getPath()), Paths.get(targetImage.getPath()));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.success"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.companyIcon.added"), I18n.INSTANCE.get("mod.companyIcon.added.title"), JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + e.getMessage());
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + "\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + I18n.INSTANCE.get("commonText.unknownError"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
