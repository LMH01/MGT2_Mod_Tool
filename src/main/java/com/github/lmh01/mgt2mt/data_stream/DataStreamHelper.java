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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataStreamHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStreamHelper.class);

    /**
     * Downloads the specified file to the destination
     *
     * @param URL         The URL of the file to be downloaded.
     * @param destination The destination where the file should be downloaded to.
     * @throws IOException If the file cannot be downloaded or files can not be opened.
     */
    public static void downloadFile(String URL, File destination) throws IOException, URISyntaxException {
        if (Files.exists(destination.toPath())) {
            Files.delete(destination.toPath());
        }
        Files.createDirectories(destination.toPath().getParent());

        BufferedInputStream in = new BufferedInputStream(new URI(URL).toURL().openStream());
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
     *
     * @param URI         The URL of the file to be downloaded.
     * @param destination The destination where the file should be downloaded to.
     * @throws IOException If the file cannot be downloaded or files can not be opened.
     * @throws URISyntaxException URI could not be parsed as URL.
     */
    public static void downloadZip(String URI, Path destination) throws IOException, URISyntaxException {
        if (Files.exists(destination)) {
            Files.delete(destination);
        }
        Files.createDirectories(destination.getParent());
        FileOutputStream fs = new FileOutputStream(destination.toFile());
        fs.getChannel().transferFrom(Channels.newChannel(new URI(URI).toURL().openStream()), 0, Long.MAX_VALUE);
        fs.close();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.downloadZip.downloadSuccess") + " " + URI + " -> " + destination);
        LOGGER.info("The zip file from " + URI + " has been successfully downloaded to " + destination);
    }

    /**
     * @param file The input file
     * @return Returns a list containing map entries for every data package in the input text file.
     * @throws IOException If the file cannot be opened.
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
                    reader.close();
                    return new ArrayList<>();
                }
                firstLine = false;
            }
            if (currentLine.equals("////////////////////////////////////////////////////////////////////")) {
                reader.readLine();//This if/else is included so that the analyzing of the hardware.txt file works properly
            }else if (!currentLine.startsWith("//")) {
                // If line contains // at start it is commented and should be ignored (response to file description in recent game update)
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
     * If an empty line is found it will be skipped and a warning will be printed into the console.
     * @param file    The input file
     * @param charSet Defines what charset the source file uses
     * @return Returns a map. The key is the line number(=id) and the value is the content for that line number. Note: line number is reduced by 1, so line 1 becomes line 0.
     * @throws IOException When the file cannot be read.
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
            if (currentLine.isEmpty()) {
                DebugHelper.warn(DataStreamHelper.class, "Empty line found in file " + file.getPath());
                continue;
            } else if (currentLine.startsWith("//")) {
                // If line contains // at start it is commented and should be ignored (response to file description in recent game update)
                continue;
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
     * @param whiteList When the string entered here is found or equals the filename the file will be added to the arrayListFiles. All other files wont be added
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
     * @throws IOException If the zip file could not be unzipped or the destination directory could not be created or is not empty.
     */
    public static void unzip(Path zipFile, Path destination) throws IOException {
        TimeHelper timeHelper = new TimeHelper();
        LOGGER.info("Unzipping folder [" + zipFile + "] to [" + destination + "]");
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.firstPart") + " [" + zipFile + "] " + I18n.INSTANCE.get("textArea.unzip.thirdPart") + " " + "[" + destination + "]");
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.secondPart"));
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.unzip.preparing"));
        ProgressBarHelper.increaseMaxValue(getZipInputStreamSize(zipFile.toFile()));
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()));
        ZipEntry zipEntry = zis.getNextEntry();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.unzip.unzipping"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.startingUnzip.firstPart") + " " + timeHelper.getMeasuredTimeDisplay() + " " + I18n.INSTANCE.get("commonText.seconds") + " - " + I18n.INSTANCE.get("textArea.unzip.startingUnzip.secondPart"));
        while (zipEntry != null) {
            File newFile = newFile(destination.toFile(), zipEntry);
            DebugHelper.debug(LOGGER, "Unzipped file: " + newFile.getPath());
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.unzip.unzippedFile") + " " + newFile.getPath());
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    zis.close();
                    throw new IOException(I18n.INSTANCE.get("dialog.dataStreamHelper.newFile.failedToCreateDirectory") + " " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    zis.close();
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
     * Parts copied from <a href="https://www.baeldung.com/java-copy-directory">baeldung.com</a>.
     * Will use the progress bar.
     *
     * @param sourceDirectory      The source
     * @param destinationDirectory The destination
     * @throws IOException Thrown if the copy fails
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
     *
     * @param directoryToBeDeleted The directory to delete
     * @throws IOException Thrown if the directory can not be deleted
     */
    public static void deleteDirectory(Path directoryToBeDeleted) throws IOException {
        deleteDirectory(directoryToBeDeleted, true);
    }

    /**
     * Deletes a directory with its contents
     *
     * @param directoryToBeDeleted The directory to delete
     * @param useProgressBar       True when the progress bar should be used.
     * @throws IOException Thrown if the directory can not be deleted
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
        if (!Files.exists(directoryToBeDeleted)) {
            return;
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
     * @param dir The directory for which the number of files should be counted
     * @return The number of files in the directory and its subdirectories
     * @throws IOException Thrown if the directory can not be read
     */
    public static long getFileCount(Path dir) throws IOException {
        if (Files.exists(dir)) {
            return Files.walk(dir)
                    .parallel()
                    .filter(p -> !p.toFile().isDirectory())
                    .count();
        } else {
            return 0;
        }
    }

    /**
     * Searches the folder for the file with `name`, ignores case.
     * Returns the file when found, otherwise null is returned.
     * 
     * @param folder The folder that should be searched for the file
     * @param name The name of the file to search
     * @return The file if found
     */
    public static Path getImageFromFolder(Path folder, String name) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(folder);
        for (Path file : stream) {
            if (file.getFileName().toString().equalsIgnoreCase(name)) {
                stream.close();
                return file;
            }
        }
        stream.close();
        return null;
    }

    /**
     * @param gameFilePath The path to the file.
     * @return Returns all lines in the file that start with // until an empty line is found or a file is found that does not start with //.
     */
    public static List<String> extractDocumentationContent(Path gameFilePath, Charset charSet) throws IOException { 
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(gameFilePath.toFile()), charSet));
        String currentLine;
        List<String> documentationContent = new ArrayList<>();
        Boolean firstLine = true;
        while ((currentLine = br.readLine()) != null) {
            if (firstLine) {
                if (charSet == StandardCharsets.UTF_8 || charSet == StandardCharsets.UTF_16LE) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                };
                firstLine = false;
            }
            if (currentLine.startsWith("//")) {
                documentationContent.add(currentLine);
            } else {
                // all relevant lines are collected
                break;
            }
        }
        br.close();
        return documentationContent;
    }

}
