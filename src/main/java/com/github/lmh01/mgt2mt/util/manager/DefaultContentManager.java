package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.Settings;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DefaultContentManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContentManager.class);

    /**
     * This is the version in which the default content files are initially saved.
     * It indicates to what game update the default files correspond that ship with this tool.
     */
    private static final String DEFAULT_CONTENT_VERSION = "BUILD 2021.08.13A";
    private static final String NEWEST_DEFAULT_CONTENT_VERSION_DOWNLOAD_URL = "https://www.dropbox.com/s/hd7f7c2b9ybr5gt/newest_default_content_version.txt?dl=1";
    public static final File DEFAULT_CONTENT_FILE = new File(Settings.MGT2_MOD_MANAGER_PATH + "//default_content.toml");

    /**
     * Analyzes the current default content file "%appdata%\LMH01\MGT2_Mod_Manager\default_content.toml" to
     * determine if they are up-to-date.
     * If the files are not up-to-date they will be updated.
     * If they do not exist yet, they will be created from the default content files located in "src\main\resources\default_content
     */
    public static void performStartTasks() {
        switch (analyzeCurrentContentVersion()) {
            case FILE_OUTDATED: {

            }

            case FILE_MISSING: {
                LOGGER.info("The default content file has not yet been generated or is corrupted. A new file will be generated.");
                if(DEFAULT_CONTENT_FILE.exists()){
                    DEFAULT_CONTENT_FILE.delete();
                }
                writeNewDefaultContentFile();
            }
        }
    }

    /**
     * Analyzes if the default content files are up-to-date.
     * @return Returns {@link DefaultContentStatus#FILE_MISSING} if the default content file has not been created yet.
     * Returns {@link DefaultContentStatus#FILE_OUTDATED} if the default content file is no longer up-to-date.
     * Returns {@link DefaultContentStatus#FILE_UP_TO_DATE} if the default content file is up-to-date.
     */
    private static DefaultContentStatus analyzeCurrentContentVersion() {
        try {
            Toml toml = new Toml().read(DEFAULT_CONTENT_FILE);
            String currentVersion = toml.getString("version");
            LOGGER.info("default content version: " + currentVersion);
            try {
                URL url = new URL(NEWEST_DEFAULT_CONTENT_VERSION_DOWNLOAD_URL);
                Scanner scanner = new Scanner(url.openStream());
                final String NEWEST_VERSION = scanner.nextLine();
                LOGGER.info("Newest default content version: " + NEWEST_VERSION);
                return isVersionNewer(currentVersion, NEWEST_VERSION);
            } catch (IOException e) {
                LOGGER.error("Unable to check for default content update.");
                return DefaultContentStatus.FILE_UP_TO_DATE;
            }
        } catch (RuntimeException e){
            return DefaultContentStatus.FILE_MISSING;
        }
    }

    /**
     *
     * @param currentVersion The version of the default content file
     * @param newestVersion The version that has been downloaded from the internet
     * @return
     * Returns {@link DefaultContentStatus#FILE_OUTDATED} if the default content file is no longer up-to-date.
     * Returns {@link DefaultContentStatus#FILE_UP_TO_DATE} if the default content file is up-to-date.
     */
    private static DefaultContentStatus isVersionNewer(String currentVersion, String newestVersion){
        //TODO Complete this function
        LOGGER.info("The current default content file is up-to-date.");
        return DefaultContentStatus.FILE_UP_TO_DATE;
    }

    /**
     * Writes a new default content file to the appdata mod tool directory.
     * The values from "src/main/resources/default_content" are used to write that file.
     */
    private static void writeNewDefaultContentFile() {
        TomlWriter tomlWriter = new TomlWriter();
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("version", DEFAULT_CONTENT_VERSION);
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource("default_content");
            String path = url.getPath();
            for (File file : Objects.requireNonNull(new File(path).listFiles())){
                map.put(file.getName().replace(".txt", ""), ReadDefaultContent.getDefaultFromSystemResource(file.getName()));
            }
            tomlWriter.write(map, DEFAULT_CONTENT_FILE);
            LOGGER.info("A new default content toml file has been created successfully!");
        } catch(IOException e) {
            LOGGER.info("A problem occurred while writing a new default content toml file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
