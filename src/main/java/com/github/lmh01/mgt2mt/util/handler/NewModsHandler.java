package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewModsHandler {
    public static void addCompanyIcon() {
        try {
            Path imageFilePath = Utils.getImagePath();
            File imageFileSource = imageFilePath.toFile();
            if (imageFileSource.exists()) {
                File targetImage = MGT2Paths.COMPANY_ICONS.getPath().resolve(CompanyLogoAnalyzer.getLogoNumber() + ".png").toFile();
                try {
                    Files.copy(Paths.get(imageFileSource.getPath()), Paths.get(targetImage.getPath()));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.success"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.companyIcon.added"), I18n.INSTANCE.get("mod.companyIcon.added.title"), JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + e.getMessage());
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + "\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } catch (ModProcessingException e) {
            if (!e.getMessage().contains("canceled")) {
                TextAreaHelper.printStackTrace(e);
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + I18n.INSTANCE.get("commonText.unknownError"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
