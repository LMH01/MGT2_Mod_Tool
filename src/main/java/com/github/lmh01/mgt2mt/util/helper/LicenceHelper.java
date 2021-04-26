package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class LicenceHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceHelper.class);

    public static void addLicence(){
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
                                EditorManager.licenceEditor.addMod(newLicence.toString());
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
    }
}
