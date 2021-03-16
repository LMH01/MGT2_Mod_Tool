package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingEngineFeatures;
import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.SharingHandler;
import com.github.lmh01.mgt2mt.util.interfaces.FreeId;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import com.github.lmh01.mgt2mt.util.interfaces.ReturnValue;
import com.github.lmh01.mgt2mt.util.interfaces.Summary;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);
    public static final String[] GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.0", "1.7.1", "1.8.0"};
    public static final String[] PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.6.0", "1.7.0", "1.7.1", "1.8.0"};
    public static final String[] THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen
    public static final String[] ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen
    public static final String[] GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen

    /**
     *
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     */
    public static void importThings(String fileName, String importName, ReturnValue importFunction, String[] compatibleModToolVersions){
        try {
            ArrayList<String> importFolders = getImportFolderPath(fileName);
            try{
                for(String importFolder : importFolders){
                    analyzeReturnValue(importName, importFunction.getReturnValue(importFolder), compatibleModToolVersions);
                }
            }catch(NullPointerException ignored){

            }
        }catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to import " + importName + ":\nThe file is corrupted or not compatible with the current Mod Manager Version", "Action unavailable", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads the contents of import file into a map and imports feature with this map
     * @param importFile This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. Engine feature, Gameplay feature
     * @param importFolderPath The folder where the importFile is located.
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFunction The function that edits the file
     * @param freeId The function that returns the free id
     * @param changelogId The id that should be used when the changelog file is being edited
     * @param summary The summary function that should be used
     * @return
     */
    public static String importGeneral(String importFile, String importName, String importFolderPath, String[] compatibleModToolVersions, Importer importFunction, FreeId freeId, int changelogId, Summary summary) throws IOException{
        File fileToImport = new File(importFolderPath + "\\" + importFile);
        Map<String, String> map = Utils.parseDataFile(fileToImport).get(0);
        map.put("ID", Integer.toString(freeId.getFreeId()));
        boolean CanBeImported = false;
        for(String string : compatibleModToolVersions){
            if(string.equals(map.get("MGT2MT VERSION"))){
                CanBeImported = true;
            }
        }
        if(!CanBeImported){
            return importName + " [" + map.get("NAME EN") + "] could not be imported:\n" + importName + " is not with the current mod tool version compatible\n" + importName + " was exported in version: " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingEngineFeatures : AnalyzeExistingEngineFeatures.engineFeatures){
            for(Map.Entry<String, String> entry : existingEngineFeatures.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    LOGGER.info(importName + " already exists - " + importName + " name is already taken");
                    return "false";
                }
            }
        }
        boolean addFeature = summary.showSummary(map);
        if(addFeature){
            importFunction.importer(map);
            JOptionPane.showMessageDialog(null, importName + " [" + map.get("NAME EN") + "] has been added successfully");
            ChangeLog.addLogEntry(changelogId, map.get("NAME EN"));
            WindowMain.checkActionAvailability();
        }
        return "true";
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @return Returns the selected folder, ready for import
     */
    private static ArrayList<String> getImportFolderPath(String fileName){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setDialogTitle("Choose the folder(s) where the " + fileName + " file is located.");
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();
                ArrayList<String> importFolders = new ArrayList<>();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    String importFolder = files[i].getPath();
                    if(Utils.doesFolderContainFile(importFolder, fileName)){
                        File fileGenreToImport = new File(importFolder + "//" + fileName);
                        BufferedReader br = new BufferedReader(new FileReader(fileGenreToImport));
                        String currentLine = br.readLine();
                        br.close();
                        if(currentLine.contains("[MGT2MT VERSION]")){
                            LOGGER.info("File seams to be valid.");
                            importFolders.add(importFolder);
                        }else{
                            JOptionPane.showMessageDialog(null, "The selected folder does not contain a valid " + fileName + " file.\nPlease select the correct folder.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "The selected folder does not contain the " + fileName + " file.\nPlease select the correct folder.");
                    }
                }
                return importFolders;
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Use this function to evaluate the return value from the respective import function.
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param returnValue The return value from the import function
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     */
    private static void analyzeReturnValue(String importName, String returnValue, String[] compatibleModToolVersions){
        try{
            if(returnValue.equals("false")){
                JOptionPane.showMessageDialog(null, "The selected " + importName + " already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }else{
                if(!returnValue.equals("true")){
                    StringBuilder supportedModToolVersions = new StringBuilder();
                    for(String string : compatibleModToolVersions){
                        supportedModToolVersions.append("[").append(string).append("]");
                    }
                    JOptionPane.showMessageDialog(null, returnValue + "\nSupported versions: " + supportedModToolVersions.toString(), "Action unavailable", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to import " + importName + ":\nThe file is corrupted or not compatible with the current Mod Manager Version", "Action unavailable", JOptionPane.ERROR_MESSAGE);
        }
    }
}
