package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataStreamHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStreamHelper.class);

    /**
     * Downloads the specified file to the destination
     *
     * @throws IOException when download failed
     */
    public static void downloadFile(String URL, File destination) throws IOException {
        if (destination.exists()) {
            destination.delete();
        }
        destination.getParentFile().mkdirs();

        BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        LOGGER.info("The file from " + URL + " has been successfully downloaded to " + destination);
    }

    /**
     * Downloads the specified zip file to the destination.
     * Prints message to text area.
     */
    public static void downloadZip(String URL, Path destination) throws IOException {
        File destinationFile = destination.toFile();
        if (destinationFile.exists()) {
            destinationFile.delete();
        }
        destinationFile.getParentFile().mkdirs();
        new FileOutputStream(destinationFile).getChannel().transferFrom(Channels.newChannel(new URL(URL).openStream()), 0, Long.MAX_VALUE);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.downloadZip.downloadSuccess") + " " + URL + " -> " + destination);
        LOGGER.info("The zip file from " + URL + " has been successfully downloaded to " + destination);
    }

    /**
     * @param file The input file
     * @return Returns a list containing map entries for every data package in the input text file.
     */
    public static List<Map<String, String>> parseDataFile(File file) throws IOException {
        List<Map<String, String>> fileParts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String currentLine;
        boolean firstLine = true;
        boolean firstList = true;
        Map<String, String> mapCurrent = new HashMap<>();
        while ((currentLine = reader.readLine()) != null) {
            if (firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                if (currentLine.contains("EOF")) {
                    //This is being put into the list when the file is empty except for the [EOF]
                    //A dummy id and name are inserted
                    mapCurrent.put("ID", "-1");
                    mapCurrent.put("NAME EN", "Dummy");
                    mapCurrent.put("PIC", "0");
                    fileParts.add(mapCurrent);
                    reader.close();
                    return fileParts;
                }
                firstLine = false;
            }
            if (currentLine.equals("////////////////////////////////////////////////////////////////////")) {
                reader.readLine();//This if/else is included so that the analyzing of the hardware.txt file works properly
            } else {
                if (currentLine.isEmpty()) {
                    if (!mapCurrent.isEmpty()) {
                        // Add new map only when the current map is not empty and if a new line is detected
                        fileParts.add(mapCurrent);
                        mapCurrent = new HashMap<>();
                        firstList = false;
                    }
                } else {
                    boolean keyComplete = false;
                    StringBuilder mapKey = new StringBuilder();
                    StringBuilder mapValue = new StringBuilder();
                    for (int i = 1; i < currentLine.length(); i++) {
                        if (!keyComplete) {
                            if (String.valueOf(currentLine.charAt(i)).equals("]")) {
                                keyComplete = true;
                                continue;
                            }
                        }
                        if (keyComplete) {
                            mapValue.append(currentLine.charAt(i));
                        } else {
                            mapKey.append(currentLine.charAt(i));
                        }
                    }
                    mapCurrent.put(mapKey.toString(), mapValue.toString().trim());
                }
            }
        }
        if (firstList) {
            fileParts.add(mapCurrent);
        }
        reader.close();
        return fileParts;
    }

    /**
     * @param path    The folder that should be tested if contains the file.
     * @param content The content that should be found.
     * @return Returns true when the input file is the MGT2 folder.
     */
    public static boolean doesFolderContainFile(Path path, String content) {
        File file = path.toFile();
        if (file.exists()) {
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if (filesInFolder[i].getName().equals(content)) {
                    return true;
                }
            }
        } else {
            LOGGER.info("File \"" + content + "\"does not exist in folder \"" + path + "\"");
        }
        return false;
    }

    /**
     * @param file    The input file
     * @param charSet Defines what charset the source file uses
     * @return Returns a map. The key is the line number(=id) and the value is the content for that line number. Note: line number is reduced by 1, so line 1 becomes line 0.
     */
    public static Map<Integer, String> getContentFromFile(File file, Charset charSet) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
        String currentLine;
        boolean firstLine = true;
        Map<Integer, String> mapCurrent = new HashMap<>();
        int currentLineNumber = 0;
        while ((currentLine = br.readLine()) != null) {
            if (firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            mapCurrent.put(currentLineNumber, currentLine);
            currentLineNumber++;
        }
        br.close();
        return mapCurrent;
    }

    /**
     * @param path The folder that should be searched for files.
     * @return Returns an array list containing all files inside the input folder
     */
    public static ArrayList<File> getFilesInFolder(Path path) {
        return getFilesInFolderBlackList(path, "EMPTY");
    }

    /**
     * @param path      The folder that should be searched for files.
     * @param blackList When the string entered here is found in the filename the file wont be added to the arrayListFiles.
     * @return Returns an array list containing all files inside the input folder
     */
    public static ArrayList<File> getFilesInFolderBlackList(Path path, String blackList) {
        File file = path.toFile();
        ArrayList<File> arrayListFiles = new ArrayList<>();
        if (file.exists()) {
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if (!filesInFolder[i].getName().contains(blackList) || blackList.equals("EMPTY")) {
                    arrayListFiles.add(filesInFolder[i]);
                }
            }
        }
        return arrayListFiles;
    }

    /**
     * @param path      The folder that should be searched for files.
     * @param whiteList When the string entered here is found/equals the filename the file will be added to the arrayListFiles. All other files wont be added
     * @return Returns an array list containing all files inside the input folder
     */
    public static ArrayList<File> getFilesInFolderWhiteList(Path path, String whiteList) {
        File file = path.toFile();
        ArrayList<File> arrayListFiles = new ArrayList<>();
        if (file.exists()) {
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if (filesInFolder[i].getName().contains(whiteList)) {
                    arrayListFiles.add(filesInFolder[i]);
                }
            }
        }
        return arrayListFiles;
    }

    /**
     * Unzips the zipFile to the destination directory.
     *
     * @param zipFile     The input zip file
     * @param destination The destination where the file should be unzipped to.
     */
    public static void unzip(Path zipFile, Path destination) throws IOException, IllegalArgumentException {
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS, true);
        LOGGER.info("Unzipping folder [" + zipFile + "] to [" + destination + "]");
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.firstPart") + " [" + zipFile + "] " + I18n.INSTANCE.get("textArea.unzip.thirdPart") + " " + "[" + destination + "]");
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.secondPart"));
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.unzip.preparing"));
        ProgressBarHelper.increaseMaxValue(getZipInputStreamSize(zipFile.toFile()));
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()));
        ZipEntry zipEntry = zis.getNextEntry();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.unzip.unzipping"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.startingUnzip.firstPart") + " " + (int) timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " ms - " + I18n.INSTANCE.get("textArea.unzip.startingUnzip.secondPart"));
        while (zipEntry != null) {
            File newFile = newFile(destination.toFile(), zipEntry);
            DebugHelper.debug(LOGGER, "Unzipped file: " + newFile.getPath());
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.unzippedFile") + " " + newFile.getPath());
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException(I18n.INSTANCE.get("dialog.dataStreamHelper.newFile.failedToCreateDirectory") + " " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException(I18n.INSTANCE.get("dialog.dataStreamHelper.newFile.failedToCreateDirectory") + " " + parent);
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
            ProgressBarHelper.increment();
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
            throw new IOException(I18n.INSTANCE.get("dialog.dataStreamHelper.newFile.entryOutOfTarget") + " " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * Returns the amount of files in the input stream
     */
    private static int getZipInputStreamSize(File zipFile) throws IOException, IllegalArgumentException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        int zipEntryAmount = 0;
        while (zipEntry != null) {
            zipEntry = zis.getNextEntry();
            zipEntryAmount++;
        }
        zis.closeEntry();
        zis.close();
        return zipEntryAmount;
    }

    /**
     * @param rootDirectory The directory where the file search is started
     * @param fileName      The file name that should be searched
     * @return Returns an array list containing all files that match the file to search
     */
    public static ArrayList<File> getFiles(File rootDirectory, String fileName) throws IOException {
        ArrayList<File> arrayList = new ArrayList<>();
        Path start = Paths.get(rootDirectory.getPath());
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            List<String> collect = stream
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());

            collect.forEach((string) -> {
                if (string.contains(fileName)) {
                    LOGGER.info(fileName + ": " + string);
                    arrayList.add(new File(string));
                }
            });
        }
        return arrayList;
    }

    /**
     * Copied from https://www.baeldung.com/java-copy-directory.
     * Will use the progress bar.
     *
     * @param sourceDirectory      The source
     * @param destinationDirectory The destination
     */
    public static void copyDirectory(Path sourceDirectory, Path destinationDirectory)
            throws IOException {
        ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.copyDirectory.title"));
        Files.createDirectories(destinationDirectory);
        TextAreaHelper.appendText(I18n.INSTANCE.get("progressBar.copyDirectory.title") + ": " + sourceDirectory + " -> " + destinationDirectory);
        ProgressBarHelper.increaseMaxValue((int) getFileCount(sourceDirectory));
        Files.walk(sourceDirectory)
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectory.toString(), source.toString()
                            .substring(sourceDirectory.toString().length()));
                    try {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ProgressBarHelper.increment();
                });
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Deletes a complete directory with its contents.
     * Will use the progress bar.
     */
    public static void deleteDirectory(Path directoryToBeDeleted) throws IOException {
        deleteDirectory(directoryToBeDeleted, true);
    }

    /**
     * Deletes a directory with its contents
     *
     * @param useProgressBar True when the progress bar should be used.
     */
    public static void deleteDirectory(Path directoryToBeDeleted, boolean useProgressBar) throws IOException {
        if (useProgressBar) {
            try {
                ProgressBarHelper.initializeProgressBar(0, Objects.requireNonNull(directoryToBeDeleted.toFile().listFiles()).length, I18n.INSTANCE.get("progressBar.delete") + " " + directoryToBeDeleted.toFile().getName());
            } catch (NullPointerException ignored) {
                ProgressBarHelper.initializeProgressBar(0, 0, I18n.INSTANCE.get("progressBar.delete") + " " + directoryToBeDeleted);
            }
            ProgressBarHelper.increaseMaxValue((int) getFileCount(directoryToBeDeleted));
        }
        Files.walkFileTree(directoryToBeDeleted, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                DebugHelper.debug(LOGGER, "Deleting file: " + file);
                Files.delete(file);
                if (useProgressBar) {
                    ProgressBarHelper.increment();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        if (useProgressBar) {
            ProgressBarHelper.resetProgressBar();
        }
    }

    /**
     * @return The number of files in the directory and its subdirectories
     */
    public static long getFileCount(Path dir) throws IOException {
        return Files.walk(dir)
                .parallel()
                .filter(p -> !p.toFile().isDirectory())
                .count();
    }
}
