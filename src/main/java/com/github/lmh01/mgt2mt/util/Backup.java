package com.github.lmh01.mgt2mt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Backup {
    private static ArrayList<String> listOfFilesToBackup = new ArrayList<>();
    private static ArrayList<String> listOfFileNamesToBackup = new ArrayList<>();
    private static ArrayList<String> contentsOfFileToBackup = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(Backup.class);
    private static String currentTimeAndDay;
    private static boolean backupSuccessful = false;
    private static String backupFailedException = "";

    public static void createBackup(boolean showSuccessDialog){
            backupSuccessful = false;
            currentTimeAndDay = Utils.getCurrentDateTime();
            createListOfFilesToBackup();
            for(int n = 0; n < listOfFilesToBackup.size(); n++){
                logger.info("Backing up file: " + listOfFilesToBackup.get(n));
                File backupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + currentTimeAndDay + "//" + listOfFileNamesToBackup.get(n) + ".backup");
                File backupFileFolder = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + currentTimeAndDay + "//");
                readContentToBackup(new File(listOfFilesToBackup.get(n)));
                logger.info("Creating backup...");
                writeBackup(backupFile, backupFileFolder);
                backupSuccessful = true;
            }
            if(backupSuccessful){
                if(showSuccessDialog){
                    JOptionPane.showMessageDialog(new Frame(), "The backup has been successfully created.");
                }
            }else{
                JOptionPane.showMessageDialog(new Frame(), "The backup could not be created:\n" + backupFailedException);
            }

    }

    private static void createListOfFilesToBackup(){
        logger.info("creating list of files to backup...");
        listOfFilesToBackup.clear();
        listOfFileNamesToBackup.clear();
        addEntryToArray("Genres.txt");
        addEntryToArray("NpcGames.txt");
        logger.info("list created.");
    }
    private static void addEntryToArray(String fileName){
        listOfFilesToBackup.add(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\" + fileName);
        listOfFileNamesToBackup.add(fileName);
    }
    private static void readContentToBackup(File fileToBackup){
        try {
            logger.info("reading contents of file: " + fileToBackup);
            contentsOfFileToBackup.clear();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileToBackup), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                contentsOfFileToBackup.add(currentLine);
                if(Settings.enableDebugLogging){logger.info("contents of file: " + currentLine);}
            }
            reader.close();
            logger.info("content has been read.");
        } catch (FileNotFoundException e) {
            backupFailedException = "The file to backup could not be found:\n" + fileToBackup;
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void writeBackup(File backupFile, File backupFileFolder){
        try {
            if(!backupFileFolder.exists()){
                backupFileFolder.mkdirs();

            }
            if(!backupFile.exists()){
                backupFile.createNewFile();
            }
            PrintWriter pw = new PrintWriter(backupFile);
            for (String s : contentsOfFileToBackup) {
                pw.print(s + "\n");
            }
            pw.close();
            logger.info("Backup file created.");

        } catch (FileNotFoundException e) {
            backupFailedException = "The file to write the backup to could not be found:\n";
            e.printStackTrace();
        } catch (IOException e) {
            backupFailedException = "Unknown.";
            e.printStackTrace();
        }
    }
}
