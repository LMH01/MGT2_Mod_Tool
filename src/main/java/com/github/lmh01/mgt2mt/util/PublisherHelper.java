package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingPublishers;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.EditPublishersFile;
import com.github.lmh01.mgt2mt.data_stream.SharingHandler;
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
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you would like to replace the\nexisting publishers with the real live equivalents?\n\nThe real publishers will have to be downloaded\nfirst so a internet connection is required.\n\nWhen continuing the publisher file is reorganized,\nloading a existing save game or\nreverting the changes might result in problems when\nloading a save game that has been affected by these changes.\n\nClick okay to continue.", "Replace publisher?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            try{
                File publisherZip = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher.zip");
                File publisherUnzipped = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher");
                boolean downloadFiles = true;
                if(publisherUnzipped.exists()){
                    if(JOptionPane.showConfirmDialog(null, "The real publisher download files already exist.\n\nDo you want to download the files again?\n", "?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                        downloadFiles = false;
                    }else{
                        Utils.deleteDirectory(publisherZip);
                        Utils.deleteDirectory(publisherUnzipped);
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
                if(AnalyzeExistingPublishers.getActivePublisherIds().contains(-1)){
                    EditPublishersFile.removePublisher("Dummy");
                }
                JOptionPane.showMessageDialog(null, "All existing publishers have been replaced\nwith the real live equivalent.");
            }catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Something went wrong:\n\nException:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
