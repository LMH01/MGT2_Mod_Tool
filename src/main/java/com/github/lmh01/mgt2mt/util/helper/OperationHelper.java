package com.github.lmh01.mgt2mt.util.helper;

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
     * @param export If true a message is shown in the end where the export folder is shown when yes is clicked
     */
    public static void process(Exporter processor, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, String exportType, String operation, String operationNoun, boolean export){
        try {
            boolean noOperationAvailable = true;
            JLabel labelChooseOperations = new JLabel("Select the " + exportType + "(s) that should be " + operation);
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
                        for(int i=0; i<listAvailableOperations.getSelectedValuesList().size(); i++){
                            String currentExport = listAvailableOperations.getSelectedValuesList().get(i);
                            if(!processor.export(currentExport)){
                                if(!multipleExports){
                                    JOptionPane.showMessageDialog(null, "The selected " + exportType + " has already been " + operation, "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }
                                failedOperations.append(currentExport).append(" - The selected ").append(exportType).append(" has already been ").append(operation).append(System.getProperty("line.separator"));
                                operationFailed = true;
                            }
                            numberOfOperations--;
                        }
                        if(numberOfOperations == 0){
                            if(operationFailed){
                                if(export){
                                    if(JOptionPane.showConfirmDialog(null, "Something went wrong wile exporting " + exportType + ".\nThe following " + exportType + "s where not exported:\n" + failedOperations + "\n\nDo you want to open the folder where it has been saved?", "Exported " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "Something went wrong while " + exportType, "Something went wrong", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }else{
                                if(export){
                                    if(JOptionPane.showConfirmDialog(null, "All selected " + exportType + "s have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Exported " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "All selected " + exportType + "s have been " + operation + " successfully!", operationNoun + " " + exportType, JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a " + exportType + " first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to export " + exportType + ":\nThere is no custom " + exportType + " that could be exported.\nPlease add a " + exportType + " first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while exporting " + exportType + ": An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        TextAreaHelper.resetAutoScroll();
        WindowMain.checkActionAvailability();
    }
}
