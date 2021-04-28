package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.LicenceAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.LicenceEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.LicenceSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class LicenceMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceMod.class);
    LicenceAnalyzer licenceAnalyzer = new LicenceAnalyzer();
    LicenceEditor licenceEditor = new LicenceEditor();
    LicenceSharer licenceSharer = new LicenceSharer();
    public ArrayList<JMenuItem> licenceModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public LicenceAnalyzer getAnalyzer(){
        return licenceAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public LicenceEditor getEditor(){
        return licenceEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public LicenceSharer getSharer(){
        return licenceSharer;
    }

    @Override
    public AbstractSimpleAnalyzer getBaseAnalyzer() {
        return licenceAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getBaseEditor() {
        return licenceEditor;
    }

    @Override
    public AbstractSimpleSharer getBaseSharer() {
        return licenceSharer;
    }

    @Override
    public AbstractSimpleMod getSimpleMod() {
        return ModManager.licenceMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action4");
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.licence.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.licence.upperCase.plural");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return licenceModMenuItems;
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "licence.txt";
    }

    @Override
    public void addModMenuItemAction() {
        Thread thread = new Thread(() -> {
            JLabel labelAddLicence = new JLabel("Name: ");
            JTextField textFieldName = new JTextField();

            JPanel panelType = new JPanel();
            JLabel labelType = new JLabel("Type: ");
            JComboBox comboBoxType = new JComboBox();
            comboBoxType.setModel(new DefaultComboBoxModel<>(new String[]{"Movie", "Book", "Sport"}));
            panelType.add(labelType);
            panelType.add(comboBoxType);

            Object[] params = {labelAddLicence, textFieldName, panelType};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, "Add Licence", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!textFieldName.getText().isEmpty()){
                        StringBuilder newLicence = new StringBuilder();
                        newLicence.append(textFieldName.getText()).append(" ");
                        String selectedType = comboBoxType.getSelectedItem().toString();
                        if(selectedType.equals("Movie")){
                            newLicence.append("[MOVIE]");
                        }else if(selectedType.equals("Book")){
                            newLicence.append("[BOOK]");
                        }else if(selectedType.equals("Sport")){
                            newLicence.append("[SPORT]");
                        }
                        boolean licenceAlreadyExists = false;
                        for(Map.Entry<Integer, String> entry : ModManager.licenceMod.getAnalyzer().getFileContent().entrySet()){
                            if(entry.getValue().equals(newLicence.toString())){
                                LOGGER.info("Licence already exists");
                                licenceAlreadyExists = true;
                            }
                        }
                        if(!licenceAlreadyExists){
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Add this licence:").append(System.getProperty("line.separator"))
                                    .append(textFieldName.getText()).append(System.getProperty("line.separator"))
                                    .append("Type: ").append(comboBoxType.getSelectedItem());
                            if(JOptionPane.showConfirmDialog(null, stringBuilder.toString(), "Add this licence?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                try {
                                    ModManager.licenceMod.getEditor().addMod(newLicence.toString());
                                    JOptionPane.showMessageDialog(null, "Licence [" + textFieldName.getText() + "] has been added successfully!");
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.licence") + " - " + textFieldName.getText());
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "<html>Unable to add licence:<br>Exception: " + e.getMessage(), "Unable to add licence", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Unable to add licence: The licence does already exist!", I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please enter a licence name first!", "Unable to continue", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    break;
                }
            }
        });
        ThreadHandler.startThread(thread, "runnableAddNewLicence");
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//Licence.txt");
    }
}
