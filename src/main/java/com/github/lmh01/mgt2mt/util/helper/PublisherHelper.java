package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.SharingHandler;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.data_stream.sharer.SharingManagerNew;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class PublisherHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherHelper.class);
    public static final String REAL_PUBLISHER_ZIP_URL = "https://www.dropbox.com/s/gkn7y2he1we3fgc/Publishers.zip?dl=1";

    /**
     * Asks the user if he is sure that the existing publishers should be replaced with the real publisher equivalents
     */
    public static void realPublishers(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.mainMessage"), "Replace publisher?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            try{
                ProgressBarHelper.initializeProgressBar(0,1, I18n.INSTANCE.get("progressBar.publisherHelper.replaceWithRealPublishers.initialize"), false, false);
                File publisherZip = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher.zip");
                File publisherUnzipped = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher");
                boolean downloadFiles = true;
                if(publisherUnzipped.exists()){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.zipFileAlreadyExists"), "?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                        downloadFiles = false;
                    }else{
                        DataStreamHelper.deleteDirectory(publisherZip, false);
                        DataStreamHelper.deleteDirectory(publisherUnzipped, false);
                    }
                }
                if(downloadFiles){
                    ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.downloadZip"));
                    DataStreamHelper.downloadZip(REAL_PUBLISHER_ZIP_URL, publisherZip.getPath());
                    ProgressBarHelper.resetProgressBar();
                    DataStreamHelper.unzip(publisherZip.getPath(), publisherUnzipped);
                }
                LOGGER.info("Real publisher files are ready.");
                LOGGER.info("Removing existing publishers...");
                ProgressBarHelper.initializeProgressBar(0, AnalyzeManager.publisherAnalyzer.getDefaultContent().length, I18n.INSTANCE.get("progressBar.replacePublisher.removingOriginalPublishers"));
                for(String string : AnalyzeManager.publisherAnalyzer.getDefaultContent()){
                    EditorManager.publisherEditor.removeMod(string);
                    ProgressBarHelper.increment();
                }
                LOGGER.info("Original publishers have been removed!");
                LOGGER.info("Adding new publishers...");
                ArrayList<File> filesToImport = DataStreamHelper.getFiles(publisherUnzipped, "publisher.txt");
                ProgressBarHelper.initializeProgressBar(0, filesToImport.size(), I18n.INSTANCE.get(""));
                SharingManager.importAllFiles(filesToImport, new ArrayList<>(), false, "publisher", (string) -> SharingManagerNew.publisherSharer.importMod(string, false), SharingManagerNew.publisherSharer.getCompatibleModToolVersions(), new AtomicBoolean(false));
                AnalyzeManager.publisherAnalyzer.analyzeFile();
                if(AnalyzeManager.publisherAnalyzer.getActiveIds().contains(-1)){
                    EditorManager.publisherEditor.removeMod("Dummy");
                }
                TextAreaHelper.appendText(I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.success").replace("<html>", "").replace("<br>", " "));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.success"));
            }catch (IOException e){
                e.printStackTrace();
                String errorMessageToDisplay;
                if(e.getMessage().equals("www.dropbox.com")){
                    errorMessageToDisplay = I18n.INSTANCE.get("commonText.noInternet");
                }else{
                    errorMessageToDisplay = e.getMessage();
                }
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.somethingWentWrong") + " " + errorMessageToDisplay, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
