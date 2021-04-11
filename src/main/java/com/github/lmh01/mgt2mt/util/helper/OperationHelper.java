package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.interfaces.Exporter;
import com.github.lmh01.mgt2mt.windows.WindowMain;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OperationHelper {

    /**
     * Opens a gui where the user can select items. The selected entries are then processed by processor.
     * @param processor The function that does something
     * @param stringArraySafetyFeaturesOn An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @param exportType The type that should be processed. Eg. genre, gameplay feature
     * @param operation The operation that should be written in some windows. Eg. removed, added, exported
     * @param operationNoun The operation that should be written in some windows. Eg. Remove, Add, Export
     * @param operationVerb The operation that should be written in some windows. Eg. Removing, Adding, Exporting
     * @param export If true a message is shown in the end where the export folder is shown when yes is clicked
     */
    public static void process(Exporter processor, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, String exportType, String operation, String operationNoun, String operationVerb, boolean export){
        try {
            ProgressBarHelper.initializeProgressBar(0, 1, operationVerb + " " + exportType);
            boolean noOperationAvailable = true;
            JLabel labelChooseOperations = new JLabel(I18n.INSTANCE.get("processor.chooseEntries.label.firstPart") + " " + exportType + I18n.INSTANCE.get("processor.chooseEntries.label.secondPart") + " " + operation);
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = stringArraySafetyFeaturesDisabled;
                noOperationAvailable = false;
            }else{
                string = stringArraySafetyFeaturesOn;
                if(string.length != 0){
                    noOperationAvailable = false;
                }
            }
            JList<String> listAvailableOperations = new JList<>(string);
            listAvailableOperations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableOperations.setLayoutOrientation(JList.VERTICAL);
            listAvailableOperations.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableOperations = new JScrollPane(listAvailableOperations);
            scrollPaneAvailableOperations.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseOperations, scrollPaneAvailableOperations};

            if(!noOperationAvailable){
                if(JOptionPane.showConfirmDialog(null, params, operationNoun + " " + exportType, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableOperations.isSelectionEmpty()){
                        boolean operationFailed = false;
                        boolean multipleExports = false;
                        if(listAvailableOperations.getSelectedValuesList().size() > 0){
                            multipleExports = true;
                        }
                        int numberOfOperations = listAvailableOperations.getSelectedValuesList().size();
                        StringBuilder failedOperations = new StringBuilder();
                        ProgressBarHelper.increaseMaxValue(numberOfOperations);
                        ProgressBarHelper.increment();
                        for(int i=0; i<listAvailableOperations.getSelectedValuesList().size(); i++){
                            String currentExport = listAvailableOperations.getSelectedValuesList().get(i);
                            if(!processor.export(currentExport)){
                                if(!multipleExports){
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.alreadyProcessed.firstPart") + " " + exportType + " " + I18n.INSTANCE.get("processor.alreadyProcessed.secondPart") + " " + operation, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                }
                                failedOperations.append(currentExport).append(" - ").append(I18n.INSTANCE.get("processor.alreadyProcessed.firstPart")).append(" ").append(exportType).append(" ").append(I18n.INSTANCE.get("processor.alreadyProcessed.secondPart")).append(" ").append(operation).append(System.getProperty("line.separator"));
                                operationFailed = true;
                            }
                            numberOfOperations--;
                            ProgressBarHelper.increment();
                        }
                        if(numberOfOperations == 0){
                            if(operationFailed){
                                if(export){
                                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + operationVerb + ".\n" + I18n.INSTANCE.get("processor.somethingWentWrong.secondPart") + " " + exportType + I18n.INSTANCE.get("processor.somethingWentWrong.thirdPart") + " " + operation + ": " + "\n" + failedOperations + "\n\n" + I18n.INSTANCE.get("processor.somethingWentWrong.fourthPart"), operation + " " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + exportType, I18n.INSTANCE.get("frame.title.error"), JOptionPane.INFORMATION_MESSAGE);
                                }
                            }else{
                                if(export){
                                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("processor.operationComplete.allSelected") + " " + exportType + I18n.INSTANCE.get("processor.operationComplete.firstPart") + " " + operation + " " + I18n.INSTANCE.get("commonText.successfully") + "!\n\n" + I18n.INSTANCE.get("processor.operationComplete.secondPart"), operation + " " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.operationComplete.allSelected") + " " + exportType + I18n.INSTANCE.get("processor.operationComplete.firstPart") + " " + operation + " " + I18n.INSTANCE.get("commonText.successfully") + "!", operationNoun + " " + exportType, JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.nothingSelected.firstPart") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.error.unableTo") + " " + operation + exportType + ":\n" + I18n.INSTANCE.get("processor.error.noCustom") + " " + exportType + " " + I18n.INSTANCE.get("processor.error.part.1") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.error.part.2") + " " + operation + exportType + ": " + I18n.INSTANCE.get("processor.error.part.3") + ": " + "\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        TextAreaHelper.resetAutoScroll();
        WindowMain.checkActionAvailability();
    }
}
