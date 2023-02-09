package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class DefaultContentManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContentManager.class);

    /**
     * This is the version in which the default content files are initially saved.
     * It indicates to what game update the default files correspond that ship with this tool.
     */
    public static final File DEFAULT_CONTENT_FILE = ModManagerPaths.MAIN.getPath().resolve("default_content.toml").toFile();

    /**
     * Writes a new default content file to the appdata mod tool directory.
     * The content that exists in the game files is set as default content.
     * If the default content file already exists it is replaced.
     */
    public static void writeNewDefaultContentFile() {
        TomlWriter tomlWriter = new TomlWriter();
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("generation_date", Utils.getCurrentDateTime());
            for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                map.put(manager.getId(), manager.getContentByAlphabet());
            }
            if (Files.exists(DEFAULT_CONTENT_FILE.toPath())) {
                Files.delete(DEFAULT_CONTENT_FILE.toPath());
            }
            tomlWriter.write(map, DEFAULT_CONTENT_FILE);
            LOGGER.info("A new default content file has been created successfully!");
        } catch (IOException | ModProcessingException e) {
            LOGGER.info("A problem occurred while writing a new default content toml file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
