package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.LicenceAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.LicenceEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.LicenceSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a"};
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
    public void menuActionAddMod() {
        JLabel labelAddLicence = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        JTextField textFieldName = new JTextField();

        JPanel panelType = new JPanel();
        JLabel labelType = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        JComboBox comboBoxType = new JComboBox();
        comboBoxType.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie"), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book"), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport")}));
        panelType.add(labelType);
        panelType.add(comboBoxType);

        Object[] params = {labelAddLicence, textFieldName, panelType};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                if(!textFieldName.getText().isEmpty()){
                    StringBuilder newLicence = new StringBuilder();
                    newLicence.append(textFieldName.getText()).append(" ");
                    String selectedType = Objects.requireNonNull(comboBoxType.getSelectedItem()).toString();
                    if(selectedType.equals(I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie"))){
                        newLicence.append("[MOVIE]");
                    }else if(selectedType.equals(I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book"))){
                        newLicence.append("[BOOK]");
                    }else if(selectedType.equals(I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport"))){
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
                        stringBuilder.append(I18n.INSTANCE.get("mod.licence.addMod.confirm")).append(":").append(System.getProperty("line.separator"))
                                .append(textFieldName.getText()).append(System.getProperty("line.separator"))
                                .append("Type: ").append(comboBoxType.getSelectedItem());
                        if(JOptionPane.showConfirmDialog(null, stringBuilder.toString(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            try {
                                Backup.createBackup(getFile());
                                ModManager.licenceMod.getEditor().addMod(newLicence.toString());
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.licence.upperCase") + " - " + textFieldName.getText());
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.licence.upperCase") + ": [" + textFieldName.getText() + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                break;
            }
        }
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
    public File getFile() {
        return new File(getMGT2DataPath() + "//Licence.txt");
    }
}
