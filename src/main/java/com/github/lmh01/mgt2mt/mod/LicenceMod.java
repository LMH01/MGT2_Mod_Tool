package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class LicenceMod extends AbstractSimpleMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceMod.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.licenceMod;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "Licence.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_licences";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
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
                    for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
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
                            createBackup();
                            addMod(newLicence.toString());
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.licence.upperCase") + " - " + textFieldName.getText());
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.licence.upperCase") + ": [" + textFieldName.getText() + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
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
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        String line = transformGenericToString(t);
        String type = I18n.INSTANCE.get("settings.notFound");
        if(line.contains("[BOOK]")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book");
        }else if(line.contains("MOVIE")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie");
        }else if(line.contains("SPORT")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport");
        }
        return I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence") + "<br>" + getReplacedLine(line) + "<br>" + I18n.INSTANCE.get("dialog.sharingHandler.type") + " " + type;
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public String getTypeCaps() {
        return "LICENCE";
    }

    @Override
    public String getImportExportFileName() {
        return "licence.txt";
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public String getModFileCharset() {
        return "UTF_8BOM";
    }
}
