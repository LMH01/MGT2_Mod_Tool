package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingPublishers;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.EditPublishersFile;
import com.github.lmh01.mgt2mt.data_stream.SharingHandler;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.SharingManager;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PublisherHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherHelper.class);
    public static final String REAL_PUBLISHER_ZIP_URL = "https://www.dropbox.com/s/gkn7y2he1we3fgc/Publishers.zip?dl=1";

    /**
     * Asks the user if he is sure that the existing publishers should be replaced with the real publisher equivalents
     */
    public static void realPublishers(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.mainMessage"), "Replace publisher?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            try{
                ProgressBarHelper.initializeProgressBar(0,1, I18n.INSTANCE.get("progressBar.publisherHelper.replaceWithRealPublishers.initialize"));
                File publisherZip = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher.zip");
                File publisherUnzipped = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher");
                boolean downloadFiles = true;
                if(publisherUnzipped.exists()){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.zipFileAlreadyExists"), "?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                        downloadFiles = false;
                    }else{
                        DataStreamHelper.deleteDirectory(publisherZip);
                        DataStreamHelper.deleteDirectory(publisherUnzipped);
                    }
                }
                if(downloadFiles){
                    DataStreamHelper.downloadZip(REAL_PUBLISHER_ZIP_URL, publisherZip.getPath());
                    DataStreamHelper.unzip(publisherZip.getPath(), publisherUnzipped);
                }
                LOGGER.info("Real publisher files are ready.");
                LOGGER.info("Removing existing publishers...");
                for(String string : AnalyzeExistingPublishers.ORIGINAL_PUBLISHERS){
                    EditPublishersFile.removePublisher(string);
                }
                LOGGER.info("Original publishers have been removed!");
                LOGGER.info("Adding new publishers...");
                ArrayList<File> filesToImport = DataStreamHelper.getFiles(publisherUnzipped, "publisher.txt");
                SharingManager.importAllFiles(filesToImport, new ArrayList<>(), false, "publisher", (string) -> SharingHandler.importPublisher(string, false), SharingManager.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, false);
                ProgressBarHelper.increment();
                if(AnalyzeExistingPublishers.getActivePublisherIds().contains(-1)){
                    EditPublishersFile.removePublisher("Dummy");
                }
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.success"));
            }catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.somethingWentWrong") + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
        WindowMain.checkActionAvailability();
    }
}
