package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.SimpleAnalyzer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.interfaces.SimpleImporter;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The simple sharer is used to export/import mods that use only text and one line per file
 */
public abstract class AbstractSimpleSharer implements BaseFunctions, BaseSharer, SimpleAnalyzer {

    /**
     * Exports the mod
     * @param name The name for the mod that should be exported
     * @param exportAsRestorePoint True when the mod should be exported as restore point. False otherwise
     * @return Returns true when the mod has been exported successfully. Returns false when the mod has already been exported.
     */
    public boolean exportMod(String name, boolean exportAsRestorePoint) {
        try {
            getAnalyzer().analyzeFile();
            String string = getAnalyzer().getLine(name);
            String exportFolder;
            if(exportAsRestorePoint){
                exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
            }else{
                exportFolder = getExportFolder();
            }
            final String EXPORTED_MOD_MAIN_FOLDER_PATH = exportFolder + name.replaceAll("[^a-zA-Z0-9]", "");
            File fileExportFolderPath = new File(EXPORTED_MOD_MAIN_FOLDER_PATH);
            File fileExportedMod = new File(EXPORTED_MOD_MAIN_FOLDER_PATH + "//" + getFileName());
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
            bw.write("[LINE]" + string + System.getProperty("line.separator"));
            bw.write("[" + getTypeCaps() + " END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer." + getMainTranslationKey() + ".exportSuccessful") + " " + name);
            return true;
        } catch (IOException e) {
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
        File fileToImport = new File(importFolderPath + "\\" + getFileName());
        Map<String, String> importMap = DataStreamHelper.parseDataFile(fileToImport).get(0);
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(importMap.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + getAnalyzer().getReplacedLine(importMap.get("LINE")));
            return getType() + " [" + getAnalyzer().getReplacedLine(importMap.get("LINE")) + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + importMap.get("MGT2MT VERSION");
        }
        for(Map.Entry<Integer, String> entry : getAnalyzer().getFileContent().entrySet()){
            if(entry.getValue().equals(importMap.get("LINE"))){
                sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + getAnalyzer().getReplacedLine(importMap.get("LINE")));
                return "false";
            }
        }
        String importLine = importMap.get("LINE");
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(importLine)) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        if(addFeature){
            getSimpleImporter().importer(importLine);
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + getAnalyzer().getReplacedLine(importMap.get("LINE")) + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + ": " + getAnalyzer().getReplacedLine(importMap.get("LINE")));
        }
        return "true";
    }

    /**
     * @return Returns the function with which the mod is imported
     */
    abstract SimpleImporter getSimpleImporter();

    /**
     * @return Returns the objects that should be displayed in the option pane
     */
    abstract String getOptionPaneMessage(String line);
}
