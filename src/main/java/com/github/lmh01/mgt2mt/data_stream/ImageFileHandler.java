package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.MGT2Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ImageFileHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileHandler.class);
    public static final Path defaultGenreIcon = MGT2Paths.GENRE_ICONS.getPath().resolve("iconSkill.png");
    public static final Path defaultPublisherIcon = MGT2Paths.COMPANY_ICONS.getPath().resolve("87.png");

    /**
     * Removes all custom publisher icons
     * TODO Move to Backup class
     */
    public static void removePublisherIcons() {
        ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(MGT2Paths.GENRE_SCREENSHOTS.getPath(), ".png");
        for (File file : files) {
            try {
                if (Integer.parseInt(file.getName().replace(".png", "")) > 187) {
                    file.delete();
                }
            } catch (NumberFormatException e) {

            }
        }
    }
}
