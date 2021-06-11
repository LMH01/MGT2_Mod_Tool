package com.github.lmh01.mgt2mt.data_stream.sharer.managed;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AdvancedAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The advanced sharer is used to export/import mods that use an advanced analyzer.
 * Optionally other tasks may be performed when the editing of the files is complete.
 * For that use {@link}
 */
public abstract class AbstractAdvancedSharer implements AdvancedAnalyzer, BaseFunctions, BaseSharer {

    /**
     * Exports the mod
     * @param name The name for the mod that should be exported
     * @param exportAsRestorePoint True when the mod should be exported as restore point. False otherwise
     * @return Returns true when the mod has been exported successfully. Returns false when the mod has already been exported.
     */
    public boolean exportMod(String name, boolean exportAsRestorePoint) {
        try{
            getAnalyzer().analyzeFile();
            Map<String, String> map = getAnalyzer().getSingleContentMapByName(name);
            String exportFolder;
            if(exportAsRestorePoint){
                exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
            }else{
                exportFolder = Utils.getMGT2ModToolExportFolder();
            }
            final String EXPORTED_MOD_MAIN_FOLDER_PATH = exportFolder + "//" + getExportFolder() + "//" + map.get("NAME EN").replaceAll("[^a-zA-Z0-9]", "");
            File fileExportFolderPath = new File(EXPORTED_MOD_MAIN_FOLDER_PATH);
            File fileExportedMod = new File(EXPORTED_MOD_MAIN_FOLDER_PATH + "//" + getImportExportFileName());
            if(fileExportedMod.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer." + getMainTranslationKey() + ".exportFailed.alreadyExported") + " " + name);
                return false;
            }else{
                fileExportFolderPath.mkdirs();
            }
            fileExportedMod.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedMod), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
            bw.write("[" + getTypeCaps() + " START]" + System.getProperty("line.separator"));
            printValues(map, bw);
            bw.write("[" + getTypeCaps() + " END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer." + getMainTranslationKey() + ".exportSuccessful") + " " + name);
            doOtherExportThings(name, EXPORTED_MOD_MAIN_FOLDER_PATH + "//DATA//", map);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] - " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage());
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Imports the mod.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the mod has been imported successfully. Returns "false" when the mod already exists. Returns mod tool version of import mod when mod is not compatible with current mod tool.
     */
    public String importMod(String importFolderPath, boolean showMessages) throws IOException {
        getAnalyzer().analyzeFile();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + getType());
        File fileToImport = new File(importFolderPath + "\\" + getImportExportFileName());
        Map<String, String> map = DataStreamHelper.parseDataFile(fileToImport).get(0);
        map.put("ID", Integer.toString(getAnalyzer().getFreeId()));
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + map.get("NAME EN"));
            return getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingContent : getAnalyzer().getFileContent()){
            for(Map.Entry<String, String> entry : existingContent.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + map.get("NAME EN"));
                    return "false";
                }
            }
        }
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(getChangedImportMap(map))) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        if(addFeature){
            getImporter().importer(getChangedImportMap(map));
            doOtherImportThings(importFolderPath, map.get("NAME EN"));
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("NAME EN"));
        }
        return "true";
    }

    @Override
    public final String getType() {
        //return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase");
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey());
    }

    @Override
    public final AbstractAdvancedAnalyzer getAnalyzer() {
        return getAdvancedMod().getBaseAnalyzer();
    }

    @Override
    public final String getExportFolder() {
        return "//" + getAdvancedMod().getType() + "//";
    };

    @Override
    public final String getMainTranslationKey() {
        return getAdvancedMod().getMainTranslationKey();
    }

    @Override
    public final String[] getCompatibleModToolVersions() {
        return getAdvancedMod().getCompatibleModToolVersions();
    }

    /**
     * @return Returns the map that contains the import values
     * Can be overwritten to adjust specific values
     */
    public Map<String, String> getChangedImportMap(Map<String, String> map){
        return map;
    }

    /**
     * Put things in this function that should be executed when the txt file has been imported.
     */
    public void doOtherImportThings(String importFolderPath, String name){

    }

    /**
     * Put things in this function that should be executed when the txt file has been exported.
     */
    public void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) throws IOException{

    }

    /**
     * Writes the values that are stored in the map to the file
     */
    public abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;

    /**
     * @return Returns the function with which the mod is imported
     */
    public abstract Importer getImporter();

    /**
     * @return Returns the objects that should be displayed in the option pane
     */
    public abstract String getOptionPaneMessage(Map<String, String> map);

    /**
     * @return Returns the advanced mod that is used to access its functions
     */
    public abstract AbstractAdvancedMod getAdvancedMod();
}
