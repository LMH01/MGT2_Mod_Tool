package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.PlatformAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.PlatformEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.PlatformSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class PlatformMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreMod.class);
    PlatformAnalyzer platformAnalyzer = new PlatformAnalyzer();
    PlatformEditor platformEditor = new PlatformEditor();
    PlatformSharer platformSharer = new PlatformSharer();
    public ArrayList<JMenuItem> genreModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    public PlatformEditor getEditor() {
        return platformEditor;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return platformAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return platformEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return platformSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.platformMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return genreModMenuItems;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            getBaseAnalyzer().analyzeFile();
            ModManager.genreMod.getAnalyzer().analyzeFile();
            ModManager.gameplayFeatureMod.getAnalyzer().analyzeFile();
            final Map<String, String>[] mapManufacturerTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean manufacturerTranslationsAdded = new AtomicBoolean(false);
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JTextField textFieldManufacturer = new JTextField(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldManufacturer.initialValue"));
            JButton buttonAddManufacturerTranslation = WindowHelper.getAddTranslationsButton(mapManufacturerTranslations, manufacturerTranslationsAdded, 2);
            JComboBox<String> comboBoxFeatureType = WindowHelper.getTypeComboBox(2);

            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();

            JSpinner spinnerEndYear = new JSpinner();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            spinnerUnlockYear.addChangeListener(e -> setEndYearSpinner(spinnerUnlockYear, spinnerEndYear));

            JComboBox comboBoxEndDateMonth = WindowHelper.getUnlockMonthComboBox();
            comboBoxEndDateMonth.setToolTipText(null);
            comboBoxEndDateMonth.setEnabled(false);

            JPanel panelEndDate = new JPanel();

            JLabel labelEndDate = new JLabel(I18n.INSTANCE.get("mod.platform.addPlatform.components.label.endDate") + ":");
            spinnerEndYear.setEnabled(false);
            setEndYearSpinner(spinnerUnlockYear, spinnerEndYear);
            panelEndDate.add(labelEndDate);
            panelEndDate.add(comboBoxEndDateMonth);
            panelEndDate.add(spinnerEndYear);
            JCheckBox checkBoxEnableEndDate = new JCheckBox(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.enableEndDate"));
            checkBoxEnableEndDate.setSelected(false);
            checkBoxEnableEndDate.addChangeListener(changeListener -> {
                if(checkBoxEnableEndDate.isSelected()){
                    comboBoxEndDateMonth.setEnabled(true);
                    spinnerEndYear.setEnabled(true);
                }else{
                    comboBoxEndDateMonth.setEnabled(false);
                    spinnerEndYear.setEnabled(false);
                }
            });

            JSpinner spinnerTechLevel = WindowHelper.getTechLevelSpinner();
            JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();
            JSpinner spinnerDevKitCost = WindowHelper.getDevKitCostSpinner();

            JCheckBox checkBoxInternet = new JCheckBox(I18n.INSTANCE.get("commonText.internet"));
            checkBoxInternet.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.internet.toolTip"));

            JSpinner spinnerComplexity = WindowHelper.getComplexitySpinner();
            JSpinner spinnerUnits = WindowHelper.getUnitsSpinner();

            JLabel labelGameplayFeatureList = new JLabel("<html>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.selectGameplayFeatures") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
            JList<String> listAvailableGameplayFeatures = WindowHelper.getList(ModManager.gameplayFeatureMod.getAnalyzer().getContentByAlphabet(), true);
            listAvailableGameplayFeatures.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        listAvailableGameplayFeatures.clearSelection();
                    }
                }
            });
            JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGameplayFeatures);

            AtomicBoolean picturesAdded = new AtomicBoolean(false);
            Map<Integer, File> pictureMap = new HashMap<>();//This map contains all the pictures that should be added for the platform
            JButton buttonAddPictures = new JButton(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"));
            buttonAddPictures.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.toolTip"));
            buttonAddPictures.addActionListener(actionEvent -> {
                boolean continueWithMessage = false;
                if(picturesAdded.get()){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.pictureAlreadyAdded"), I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.pictureAlreadyAdded.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        continueWithMessage = true;
                        picturesAdded.set(false);
                        pictureMap.clear();
                    }
                }else{
                    continueWithMessage = true;
                }
                if(continueWithMessage){
                    JTextArea textAreaAddedImages = new JTextArea();
                    textAreaAddedImages.setPreferredSize(new Dimension(315, 140));
                    JLabel labelTextAreaExplanation = new JLabel(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.label.textAreaExplanation"));
                    JScrollPane scrollPane = new JScrollPane(textAreaAddedImages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    JButton buttonAddPicture = new JButton(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture"));
                    AtomicInteger lastYear = new AtomicInteger(1975);
                    final boolean[] firstImage = {true};
                    buttonAddPicture.addActionListener(actionEvent2 -> {
                        boolean continueWithPictures = false;
                        if(pictureMap.size() < 2 || Settings.disableSafetyFeatures){
                            continueWithPictures = true;
                        }else{
                            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("frame.title.unableToContinue") + ":<br><br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.maxPicturesSelectedMessage"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                        }
                        if(continueWithPictures){
                            JButton buttonSelectImage = new JButton(I18n.INSTANCE.get("commonText.selectImage"));
                            AtomicReference<File> imageFile = new AtomicReference<>();
                            JPanel panelChangeDate = new JPanel();
                            JLabel labelChangeDate = new JLabel(I18n.INSTANCE.get(""));
                            JComboBox comboBoxChangeMonth = new JComboBox();
                            comboBoxChangeMonth.setEnabled(false);
                            comboBoxChangeMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
                            comboBoxChangeMonth.setSelectedItem("JAN");
                            JSpinner spinnerChangeYear = new JSpinner();
                            spinnerChangeYear.setEnabled(false);
                            if(Settings.disableSafetyFeatures){
                                spinnerChangeYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture.actionListener.spinnerChangeYear.toolTip"));
                                spinnerChangeYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                                ((JSpinner.DefaultEditor)spinnerEndYear.getEditor()).getTextField().setEditable(true);
                            }else{
                                spinnerChangeYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": " + lastYear.get() +1 + " - 2050]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture.actionListener.spinnerChangeYear.toolTip"));
                                spinnerChangeYear.setModel(new SpinnerNumberModel(lastYear.get() +1, lastYear.get() +1, 2050, 1));
                                ((JSpinner.DefaultEditor)spinnerEndYear.getEditor()).getTextField().setEditable(false);
                            }
                            panelChangeDate.add(labelChangeDate);
                            panelChangeDate.add(comboBoxChangeMonth);
                            panelChangeDate.add(spinnerChangeYear);
                            buttonSelectImage.addActionListener(actionEvent3 -> {
                                File newImageFile = new File(Utils.getImagePath());
                                imageFile.set(newImageFile);
                                if(newImageFile.exists()){
                                    buttonSelectImage.setText(I18n.INSTANCE.get("commonText.imageSelected"));
                                }else{
                                    buttonSelectImage.setText(I18n.INSTANCE.get("commonText.selectImage"));
                                }
                            });
                            if(!firstImage[0]){
                                comboBoxChangeMonth.setEnabled(true);
                                spinnerChangeYear.setEnabled(true);
                            }
                            Object[] params = {buttonSelectImage, panelChangeDate};
                            while(true){
                                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                                    if(imageFile.get() != null){
                                        if(firstImage[0]){
                                            firstImage[0] = false;
                                        }else{
                                            comboBoxChangeMonth.setEnabled(true);
                                            spinnerChangeYear.setEnabled(true);
                                            textAreaAddedImages.append(System.getProperty("line.separator"));
                                        }
                                        textAreaAddedImages.append(Objects.requireNonNull(comboBoxChangeMonth.getSelectedItem()).toString() + " " + spinnerChangeYear.getValue().toString() + " - " + imageFile.get().getPath());
                                        pictureMap.put(Integer.parseInt(spinnerChangeYear.getValue().toString()), imageFile.get());
                                        lastYear.set(Integer.parseInt(spinnerChangeYear.getValue().toString()));
                                        break;
                                    }else{
                                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.noPictureSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                    }
                                }else{
                                    break;
                                }
                            }
                        }
                    });
                    Object[] params = {labelTextAreaExplanation, scrollPane, buttonAddPicture};
                    if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION && pictureMap.size() > 0){
                        picturesAdded.set(true);
                        buttonAddPictures.setText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.picturesSelected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.picturesSet"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        picturesAdded.set(false);
                        buttonAddPictures.setText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"));
                        pictureMap.clear();
                    }
                }
            });

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getManufacturerPanel(textFieldManufacturer), buttonAddManufacturerTranslation, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), checkBoxEnableEndDate, panelEndDate, WindowHelper.getSpinnerPanel(spinnerTechLevel, 4), WindowHelper.getSpinnerPanel(spinnerComplexity, 10), WindowHelper.getSpinnerPanel(spinnerUnits, 11), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerDevKitCost, 8), checkBoxInternet, labelGameplayFeatureList, scrollPaneAvailableGenres, buttonAddPictures};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldName.initialValue"))){
                        if(!textFieldManufacturer.getText().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldManufacturer.initialValue"))){
                            boolean modAlreadyExists = false;
                            for(String string : getBaseAnalyzer().getContentByAlphabet()){
                                if(textFieldName.getText().equals(string)){
                                    modAlreadyExists = true;
                                }
                            }
                            if(!modAlreadyExists) {
                                Map<String, String> platformMap = new HashMap<>();
                                Map<Integer, File> finalPictureMap = new HashMap<>();
                                platformMap.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                                if(!nameTranslationsAdded.get() && !manufacturerTranslationsAdded.get()){
                                    platformMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                    platformMap.putAll(TranslationManager.getDefaultManufacturerTranslations(textFieldManufacturer.getText()));
                                }else if(!nameTranslationsAdded.get() && manufacturerTranslationsAdded.get()){
                                    platformMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapManufacturerTranslations[0], "MANUFACTURER"));
                                }else if(nameTranslationsAdded.get() && !manufacturerTranslationsAdded.get()){
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                    platformMap.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldManufacturer.getText()));
                                }else{
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapManufacturerTranslations[0], "MANUFACTURER"));
                                    platformMap.put("NAME EN", textFieldName.getText());
                                    platformMap.put("MANUFACTURER EN", textFieldManufacturer.getText());
                                }
                                platformMap.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString());
                                if(checkBoxEnableEndDate.isSelected()){
                                    platformMap.put("DATE END", Objects.requireNonNull(comboBoxEndDateMonth.getSelectedItem()).toString() + " " + spinnerEndYear.getValue().toString());
                                }
                                platformMap.put("PRICE", spinnerDevKitCost.getValue().toString());
                                platformMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                                platformMap.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                                platformMap.put("UNITS", spinnerUnits.getValue().toString());
                                if(pictureMap.isEmpty()){
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"))){
                                        platformMap.put("PIC-1", "AmstradCPC.png");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"))){
                                        platformMap.put("PIC-1", "N64.png");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"))){
                                        platformMap.put("PIC-1", "Nintendo3DS.png");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"))){
                                        platformMap.put("PIC-1", "iPhone4.png");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard"))){
                                        platformMap.put("PIC-1", "ASB6.png");
                                    }
                                }else{
                                    ArrayList<Integer> pictureYears = new ArrayList<>();
                                    for(Map.Entry<Integer, File> entry : pictureMap.entrySet()){
                                        pictureYears.add(entry.getKey());
                                    }
                                    int pictureNumber = 1;
                                    for(Integer integer : pictureYears){
                                        finalPictureMap.put(pictureNumber, pictureMap.get(integer));
                                        if(pictureNumber>1){
                                            platformMap.put("PIC-" + pictureNumber + " YEAR", Integer.toString(integer));
                                        }
                                        pictureNumber++;
                                    }
                                }
                                if(listAvailableGameplayFeatures.getSelectedValuesList().isEmpty()){
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"))){
                                        platformMap.put("NEED-1", "44");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"))){
                                        platformMap.put("NEED-1", "45");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"))){
                                        platformMap.put("NEED-1", "45");
                                        platformMap.put("NEED-2", "56");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"))){
                                        platformMap.put("NEED-1", "56");
                                    }
                                    if(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard"))){
                                        platformMap.put("NEED-1", "59");
                                    }
                                }else{
                                    int currentNeededGameplayFeatureNumber = 1;
                                    for(String string : listAvailableGameplayFeatures.getSelectedValuesList()){
                                        platformMap.put("NEED-" + currentNeededGameplayFeatureNumber, Integer.toString(ModManager.gameplayFeatureMod.getAnalyzer().getContentIdByName(string)));
                                        currentNeededGameplayFeatureNumber++;
                                    }
                                }
                                platformMap.put("COMPLEX", spinnerComplexity.getValue().toString());
                                if(checkBoxInternet.isSelected()){
                                    platformMap.put("INTERNET", "1");
                                }else{
                                    platformMap.put("INTERNET", "0");
                                }
                                platformMap.put("TYP", Integer.toString(getPlatformTypeIdByString(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString())));
                                if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(platformMap), I18n.INSTANCE.get(""), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                    Backup.createBackup(getFile());
                                    getEditor().addMod(platformMap, finalPictureMap);
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.platform.upperCase") + " - " + platformMap.get("NAME EN"));
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.platform.upperCase") + ": [" + platformMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.platform.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                    break;
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.manufacturer.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "platform";
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "Platforms.txt";
    }

    @Override
    public void removeMod(String name) throws IOException {
        getEditor().removePlatform(name);
    }

    @Override
    public void removeModMenuItemAction() {
        Thread thread = new Thread(() -> OperationHelper.process(getEditor()::removePlatform, getBaseAnalyzer().getCustomContentString(), getBaseAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false));
        ThreadHandler.startThread(thread, "runnableRemove" + getType());
    }

    /**
     * @return Returns the platform type for the input id. Returns -1 if the string is not correct
     */
    public int getPlatformTypeIdByString(String type){
        if(type.equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"))){
            return 0;
        }
        if(type.equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"))){
            return 1;
        }
        if(type.equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"))){
            return 2;
        }
        if(type.equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"))){
            return 3;
        }
        if(type.equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard"))){
            return 4;
        }
        return -1;
    }

    /**
     * @return Returns the type string for the input id.
     */
    public String getPlatformTypeStringById(int id){
        switch(id){
            case 0: return I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer");
            case 1: return I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console");
            case 2: return I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld");
            case 3: return I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone");
            case 4: return I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard");
            default: throw new IllegalArgumentException("The input string is invalid!");
        }
    }

    private void setEndYearSpinner(JSpinner spinnerUnlockYear, JSpinner spinnerEndYear){
        if(Settings.disableSafetyFeatures){
            spinnerEndYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.spinner.endYear.toolTip"));
            spinnerEndYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
            ((JSpinner.DefaultEditor)spinnerEndYear.getEditor()).getTextField().setEditable(true);
        }else{
            spinnerEndYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": " + Integer.parseInt(spinnerUnlockYear.getValue().toString())+1 + " - 2050]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.spinner.endYear.toolTip"));
            spinnerEndYear.setModel(new SpinnerNumberModel(Integer.parseInt(spinnerUnlockYear.getValue().toString())+1, Integer.parseInt(spinnerUnlockYear.getValue().toString())+1, 2050, 1));
            ((JSpinner.DefaultEditor)spinnerEndYear.getEditor()).getTextField().setEditable(false);
        }
    }
}
