package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataStreamHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStreamHelper.class);

    /**
     * Searches the input file for the "NAME EN" key and returns its value. If not found returns -1.
     * @param file The file that should be searched for the key
     * @param charsetType Defines what charset the source file uses. Possible UTF_8BOM UTF_16LE
     */
    public static String getNameFromFile(File file, String charsetType) throws IOException {
        Map<Integer, String> map = Utils.getContentFromFile(file, charsetType);
        for(Map.Entry entry : map.entrySet()){
            if(entry.getValue().toString().contains("NAME EN")){
                return entry.getValue().toString().replace("[NAME EN]", "");
            }
        }
        return "-1";
    }

    public static void downloadZip(String URL, String destination) throws IOException {
        File destinationFile = new File(destination);
        if(destinationFile.exists()){
            destinationFile.delete();
        }
        destinationFile.getParentFile().mkdirs();
        new FileOutputStream(destination).getChannel().transferFrom(Channels.newChannel(new URL(URL).openStream()), 0, Long.MAX_VALUE);
        LOGGER.info("The zip file from " + URL + " has been successfully downloaded to " + destination);
    }

    /**
     * Unzips the zipFile to the destination directory.
     * @param zipFile The input zip file
     * @param destination The destination where the file should be unzipped to.
     */
    public static void unzip(String zipFile, File destination) throws IOException {
        LOGGER.info("Unzipping folder [" + zipFile + "] to [" + destination + "]");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destination, zipEntry);
            if(Settings.enableDebugLogging){
                LOGGER.info("Unzipped file: " + newFile.getPath());
            }
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        LOGGER.info("Unzipping complete!");
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * @param rootDirectory The directory where the file search is started
     * @param fileToSearch The file name that should be searched
     * @return Returns a array list containing all files that match the file to search
     */
    public static ArrayList<File> getFiles(File rootDirectory, String fileToSearch) throws IOException {
        ArrayList<File> arrayList = new ArrayList<>();
        Path start = Paths.get(rootDirectory.getPath());
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            List<String> collect = stream
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());

            collect.forEach((string) -> {
                if(string.contains(fileToSearch)){
                    LOGGER.info(fileToSearch + ": " + string);
                    arrayList.add(new File(string));
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info("current file: " + string);
                }
            });
        }
        return arrayList;
    }

}
