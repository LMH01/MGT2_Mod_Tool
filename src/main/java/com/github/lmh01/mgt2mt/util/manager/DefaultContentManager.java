package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DefaultContentManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContentManager.class);

    /**
     * This is the version in which the default content files are initially saved.
     * It indicates to what game update the default files correspond that ship with this tool.
     */
    private static final String DEFAULT_CONTENT_VERSION = "BUILD 2021.10.11B";
    private static final String NEWEST_DEFAULT_CONTENT_VERSION_DOWNLOAD_URL = "https://www.dropbox.com/s/hd7f7c2b9ybr5gt/newest_default_content_version.txt?dl=1";
    private static final String NEWEST_DEFAULT_CONTENT_DOWNLOAD_URL = "https://www.dropbox.com/s/7l89pg9x4venqje/newest_default_content.toml?dl=1";
    public static final File DEFAULT_CONTENT_FILE = ModManagerPaths.MAIN.getPath().resolve("default_content.toml").toFile();

    /**
     * Analyzes the current default content file "%appdata%\LMH01\MGT2_Mod_Manager\default_content.toml" to
     * determine if they are up-to-date.
     * If the files are not up-to-date they will be updated.
     * If they do not exist yet, they will be created from the default content files located in "src\main\resources\default_content
     */
    public static void performStartTasks() {
        switch (analyzeCurrentContentVersion()) {
            case FILE_UP_TO_DATE: {
                LogFile.write("The current default content file is up-to-date.");
            }
            break;
            case FILE_OUTDATED: {
                LogFile.write("The current default content file is outdated.");
                updateToml();
            }
            break;
            case FILE_MISSING: {
                LogFile.write("The default content file has not yet been generated or is corrupted. A new file will be generated.");
                if (DEFAULT_CONTENT_FILE.exists()) {
                    DEFAULT_CONTENT_FILE.delete();
                }
                writeNewDefaultContentFile();
                performStartTasks();
            }
            break;
            case UPDATE_CHECK_FAILED: {
                LogFile.write("Unable to check for default content update.");
            }
            break;
        }
    }

    /**
     * Analyzes if the default content files are up-to-date.
     *
     * @return Returns {@link DefaultContentStatus#FILE_MISSING} if the default content file has not been created yet.
     * Returns {@link DefaultContentStatus#FILE_OUTDATED} if the default content file is no longer up-to-date.
     * Returns {@link DefaultContentStatus#FILE_UP_TO_DATE} if the default content file is up-to-date.
     * Returns {@link DefaultContentStatus#UPDATE_CHECK_FAILED} if the default content file exists but the update check has failed.
     */
    private static DefaultContentStatus analyzeCurrentContentVersion() {
        try {
            if (DEFAULT_CONTENT_FILE.exists()) {
                Toml toml = new Toml().read(DefaultContentManager.DEFAULT_CONTENT_FILE);
                String currentVersion = toml.getString("version");
                DebugHelper.debug(LOGGER, "default content version: " + currentVersion);
                try {
                    URL url = new URL(NEWEST_DEFAULT_CONTENT_VERSION_DOWNLOAD_URL);
                    Scanner scanner = new Scanner(url.openStream());
                    final String NEWEST_VERSION = scanner.nextLine();
                    DebugHelper.debug(LOGGER, "Newest default content version: " + NEWEST_VERSION);
                    return isVersionNewer(currentVersion, NEWEST_VERSION);
                } catch (IOException e) {
                    return DefaultContentStatus.UPDATE_CHECK_FAILED;
                }
            } else {
                return DefaultContentStatus.FILE_MISSING;
            }
        } catch (RuntimeException | ExceptionInInitializerError e) {
            e.printStackTrace();
            return DefaultContentStatus.FILE_MISSING;
        }
    }

    /**
     * @param currentVersion The version of the default content file
     * @param newestVersion  The version that has been downloaded from the internet
     * @return Returns {@link DefaultContentStatus#FILE_OUTDATED} if the default content file is no longer up-to-date.
     * Returns {@link DefaultContentStatus#FILE_UP_TO_DATE} if the default content file is up-to-date.
     */
    private static DefaultContentStatus isVersionNewer(String currentVersion, String newestVersion) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate currentVersionDate = LocalDate.parse(currentVersion.replace("BUILD ", "").replaceAll("[a-zA-Z]", ""), formatter);
        LocalDate newestVersionDate = LocalDate.parse(newestVersion.replace("BUILD ", "").replaceAll("[a-zA-Z]", ""), formatter);
        if (newestVersionDate.isAfter(currentVersionDate)) {
            return DefaultContentStatus.FILE_OUTDATED;
        } else if (newestVersionDate.equals(currentVersionDate)) {
            Character a = currentVersion.charAt(currentVersion.length() - 1);
            Character b = newestVersion.charAt(newestVersion.length() - 1);
            if (a.compareTo(b) == 0 || a.compareTo(b) > 0) {
                return DefaultContentStatus.FILE_UP_TO_DATE;
            } else {
                return DefaultContentStatus.FILE_OUTDATED;
            }
        } else {
            return DefaultContentStatus.FILE_UP_TO_DATE;
        }
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
            for (String string : getDefaultContentNames()) {
                map.put(string.replace(".txt", ""), getDefaultFromSystemResource(string));
            }
            tomlWriter.write(map, DEFAULT_CONTENT_FILE);
            LOGGER.info("A new default content toml file has been created successfully!");
        } catch (IOException e) {
            LOGGER.info("A problem occurred while writing a new default content toml file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return Returns a string containing the contents of the file. The file will be searched in "src/main/resources/default_content".
     * Use {@link ReadDefaultContent#getDefault(String)} instead, if you would like to get the custom content that is saved in appdata.
     */
    private static String[] getDefaultFromSystemResource(String fileName) throws IOException {
        DebugHelper.debug(LOGGER, "file name: " + fileName);
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("default_content/" + fileName)), StandardCharsets.UTF_8));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            arrayList.add(currentLine);
        }
        reader.close();
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }

    /**
     * Downloads the newest toml file to this location "%appdata%\LMH01\MGT2_Mod_Manager\default_content.toml".
     * The existing file will be replaced.
     */
    private static void updateToml() {
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.initializingTool") + ": " + I18n.INSTANCE.get("progressBar.downloadingDefaultContentUpdate"));
        LOGGER.info("Updating default content toml file...");
        try {
            File tomlDownload = ModManagerPaths.MAIN.getPath().resolve("default_content_update.toml").toFile();
            DataStreamHelper.downloadFile(NEWEST_DEFAULT_CONTENT_DOWNLOAD_URL, tomlDownload);
            DEFAULT_CONTENT_FILE.delete();
            Files.copy(Paths.get(tomlDownload.getPath()), Paths.get(DEFAULT_CONTENT_FILE.getPath()));
            tomlDownload.delete();
            LogFile.write("The default_content.toml file has been updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns an array list containing the filename of the default content files that should be read
     */
    private static ArrayList<String> getDefaultContentNames() {
        ArrayList<String> strings = new ArrayList<>();
        for (AbstractBaseMod mod : ModManager.mods) {
            strings.add(mod.getDefaultContentFileName());
        }
        return strings;
    }
}
