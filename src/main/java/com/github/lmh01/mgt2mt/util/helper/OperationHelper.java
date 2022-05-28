package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.interfaces.Processor;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContentManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class OperationHelper {

    /**
     * Opens a gui where the user can select items. The selected entries are then constructed using
     * {@link AbstractBaseContentManager#constructContentFromName(String)} and put into a list.
     * This list is the given to the processor to do something.
     * The progress bar is utilized while the contents are constructed.
     *
     * @param processor                         The function that does something
     * @param stringArraySafetyFeaturesOn       An array containing the list items when the safety features are on
     * @param stringArraySafetyFeaturesDisabled An array containing the list items when the safety features are off
     * @param exportType                        The type that should be processed. Eg. genre, gameplay feature
     * @param operation                         The operation that should be written in some windows. Eg. removed, added, exported
     * @param operationNoun                     The operation that should be written in some windows. Eg. Remove, Add, Export
     * @param operationVerb                     The operation that should be written in some windows. Eg. Removing, Adding, Exporting
     * @param export                            If true a message is shown in the end where the export folder is shown when yes is clicked
     * @param manager                           The manager that is used to construct the content
     * @throws ModProcessingException When something went wrong
     */
    public static void process(Processor processor, String[] stringArraySafetyFeaturesOn, String[] stringArraySafetyFeaturesDisabled, String exportType, String operation, String operationNoun, String operationVerb, boolean export, BaseContentManager manager) throws ModProcessingException {
        try {
            boolean noOperationAvailable = true;
            JLabel labelChooseOperations = new JLabel(I18n.INSTANCE.get("processor.chooseEntries.label.firstPart") + " " + exportType + I18n.INSTANCE.get("processor.chooseEntries.label.secondPart") + " " + operation);
            String[] listContent;
            if (Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
                listContent = stringArraySafetyFeaturesDisabled;
                noOperationAvailable = false;
            } else {
                listContent = stringArraySafetyFeaturesOn;
                if (listContent.length != 0) {
                    noOperationAvailable = false;
                }
            }
            JList<String> listAvailableOperations = new JList<>(listContent);
            listAvailableOperations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableOperations.setLayoutOrientation(JList.VERTICAL);
            listAvailableOperations.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableOperations = new JScrollPane(listAvailableOperations);
            scrollPaneAvailableOperations.setPreferredSize(new Dimension(315, 140));

            Object[] params = {labelChooseOperations, scrollPaneAvailableOperations};

            if (!noOperationAvailable) {
                if (JOptionPane.showConfirmDialog(null, params, operationNoun + " " + exportType, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    if (!listAvailableOperations.isSelectionEmpty()) {
                        TimeHelper timeHelper = new TimeHelper();
                        List<AbstractBaseContent> contents = Utils.constructContents(listAvailableOperations.getSelectedValuesList(), manager);
                        try {
                            processor.process(contents);
                            TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.totalDuration"), timeHelper.getMeasuredTimeDisplay()));
                            if (export) {
                                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("processor.operationComplete.allSelected") + " " + exportType + I18n.INSTANCE.get("processor.operationComplete.firstPart") + " " + operation + " " + I18n.INSTANCE.get("commonText.successfully") + "!\n\n" + I18n.INSTANCE.get("processor.operationComplete.secondPart"), operation + " " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                                    Desktop.getDesktop().open(ModManagerPaths.EXPORT.toFile());
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.operationComplete.allSelected") + " " + exportType + I18n.INSTANCE.get("processor.operationComplete.firstPart") + " " + operation + " " + I18n.INSTANCE.get("commonText.successfully") + "!", operationNoun + " " + exportType, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ModProcessingException e) {
                            if (export) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + operationVerb + ".\n" + I18n.INSTANCE.get("processor.somethingWentWrong.secondPart") + " " + exportType + I18n.INSTANCE.get("processor.somethingWentWrong.thirdPart") + " " + operation);
                                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + operationVerb + ".\n" + I18n.INSTANCE.get("processor.somethingWentWrong.secondPart") + " " + exportType + I18n.INSTANCE.get("processor.somethingWentWrong.thirdPart") + " " + operation + "\n\n" + I18n.INSTANCE.get("processor.somethingWentWrong.fourthPart"), operation + " " + exportType, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                                    Desktop.getDesktop().open(ModManagerPaths.EXPORT.toFile());
                                }
                            } else {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + operationVerb + " " + exportType);
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.somethingWentWrong.firstPart") + " " + exportType, I18n.INSTANCE.get("frame.title.error"), JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("processor.nothingSelected.firstPart") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.nothingSelected.firstPart") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("processor.error.unableTo") + " " + operation + exportType + ":\n" + I18n.INSTANCE.get("processor.error.noCustom") + " " + exportType + " " + I18n.INSTANCE.get("processor.error.part.1") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("processor.error.unableTo") + " " + operation + exportType + ":\n" + I18n.INSTANCE.get("processor.error.noCustom") + " " + exportType + " " + I18n.INSTANCE.get("processor.error.part.1") + " " + exportType + " " + I18n.INSTANCE.get("processor.nothingSelected.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("processor.error.part.2") + " " + operation + exportType, e);
        }
    }
}
