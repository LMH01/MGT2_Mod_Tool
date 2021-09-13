package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class LicenceMod extends AbstractSimpleMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceMod.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.3.0"};
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
    public String getExportType() {
        return "licence";
    }

    @Override
    public String getGameFileName() {
        return "Licence.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_licences.txt";
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
                        stringBuilder.append(I18n.INSTANCE.get("mod.licence.addMod.confirm")).append(":").append("\r\n")
                                .append(textFieldName.getText()).append("\r\n")
                                .append("Type: ").append(comboBoxType.getSelectedItem());
                        if(JOptionPane.showConfirmDialog(null, stringBuilder.toString(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            createBackup();
                            addModToFile(newLicence.toString());
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
    public ArrayList<AbstractBaseMod> getDependencies() {
        return new ArrayList<>();
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public String getModFileCharset() {
        return "UTF_8BOM";
    }

    @Override
    public String[] getCustomContentString(boolean disableTextAreaMessage) throws ModProcessingException {
        String[] allLicenceNamesByAlphabet = getContentByAlphabet();

        ArrayList<String> arrayListCustomLicences = new ArrayList<>();

        ProgressBarHelper.initializeProgressBar(0, allLicenceNamesByAlphabet.length, I18n.INSTANCE.get("progressBar.moddedLicences"), !disableTextAreaMessage);
        int currentProgressBarValue = 0;
        for (String s : allLicenceNamesByAlphabet) {
            boolean defaultGenre = false;
            for (String licenceName : getDefaultContent()) {
                if (s.equals(licenceName)) {
                    defaultGenre = true;
                    break;
                }
                if(s.equals("Chronicles of Nornio [5]")){
                    defaultGenre = true;
                }
            }
            if (!defaultGenre) {
                arrayListCustomLicences.add(s);
                if(!disableTextAreaMessage){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicenceFound") + " " + s);
                }
            }
            currentProgressBarValue++;
            ProgressBarHelper.setValue(currentProgressBarValue);
        }
        if(!disableTextAreaMessage){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicencesComplete"));
        }
        ProgressBarHelper.resetProgressBar();
        arrayListCustomLicences.remove("Chronicles of Nornio [5]");
        String[] string = new String[arrayListCustomLicences.size()];
        arrayListCustomLicences.toArray(string);
        setFinishedCustomContentString(string);
        return string;
    }
}
