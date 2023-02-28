package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.Hardware;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.content.managed.types.HardwareType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HardwareManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final HardwareManager INSTANCE = new HardwareManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", MadGamesTycoon2ModTool.VERSION};

    private HardwareManager() {
        super("hardware", "hardware", MGT2Paths.TEXT_DATA.getPath().resolve("Hardware.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        if (!(content instanceof Hardware)) {
            throw new ModProcessingException("Unable to edit text files for content of type " + getType() + ": The input content is not instance of Hardware!");
        }
        Hardware hardware = (Hardware) content;
        if (action.equals(ContentAction.ADD_MOD)) {
            try {
                Charset charset = getCharset();
                Path gameFilePath = gameFile.toPath();
                if (Files.exists(gameFilePath)) {
                    Files.delete(gameFilePath);
                }
                Files.createFile(gameFilePath);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                if (charset.equals(StandardCharsets.UTF_8)) {
                    bw.write("\ufeff");
                }
                int currentType = 0;
                boolean firstMap = true;
                for (Map<String, String> fileContent : fileContent) {
                    if (firstMap) {
                        currentType = Integer.parseInt(fileContent.get("TYP"));
                        firstMap = false;
                    }
                    if (currentType != Integer.parseInt(fileContent.get("TYP"))) {
                        currentType = Integer.parseInt(fileContent.get("TYP"));
                        bw.write("////////////////////////////////////////////////////////////////////");
                        bw.write("\r\n");
                        bw.write("\r\n");
                    }
                    printValues(fileContent, bw);
                    bw.write("\r\n");
                }
                if (currentType != hardware.hardwareType.getId()) {
                    bw.write("////////////////////////////////////////////////////////////////////");
                    bw.write("\r\n");
                    bw.write("\r\n");
                }
                printValues(content.getMap(), bw);
                bw.write("\r\n");
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
            }
        } else {
            try {
                int contentId = content.id;
                Charset charset = getCharset();
                Path gameFilePath = gameFile.toPath();
                if (Files.exists(gameFilePath)) {
                    Files.delete(gameFilePath);
                }
                Files.createFile(gameFilePath);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                if (charset.equals(StandardCharsets.UTF_8)) {
                    bw.write("\ufeff");
                }
                int currentType = 0;
                boolean firstMap = true;
                for (Map<String, String> fileContent : fileContent) {
                    if (Integer.parseInt(fileContent.get("ID")) != contentId) {
                        if (firstMap) {
                            currentType = Integer.parseInt(fileContent.get("TYP"));
                            firstMap = false;
                        }
                        if (currentType != Integer.parseInt(fileContent.get("TYP"))) {
                            currentType = Integer.parseInt(fileContent.get("TYP"));
                            bw.write("////////////////////////////////////////////////////////////////////");
                            bw.write("\r\n");
                            bw.write("\r\n");
                        }
                        printValues(fileContent, bw);
                        bw.write("\r\n");
                    }
                }
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
            }
        }
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        super.printValues(map, bw);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                bw.write(entry.getKey()  + entry.getValue() + "\r\n");
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        ArrayList<Integer> requiredGenres = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                requiredGenres.add(Integer.parseInt(entry.getValue()));
            }
        }
        Image icon = null;
        if (map.containsKey("PIC")) {
            icon = new Image(MGT2Paths.HARDWARE_ICONS.getPath().resolve(map.get("PIC")).toFile());
        }
        return new Hardware(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DATE"),
                HardwareType.getFromId(Integer.parseInt(map.get("TYP"))),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("TECHLEVEL")),
                map.containsKey("ONLY_HANDHELD"),
                map.containsKey("ONLY_STATIONARY"),
                requiredGenres,
                icon
        );
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("TYP", true, DataType.INT));
        list.add(new DataLine("RES POINTS", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("DEV COSTS", true, DataType.INT));
        list.add(new DataLine("TECHLEVEL", true, DataType.INT));
        list.add(new DataLine("PIC", false, DataType.UNCHECKED));
        list.add(new DataLine("ONLY_HANDHELD", false, DataType.EMPTY));
        list.add(new DataLine("ONLY_STATIONARY", false, DataType.EMPTY));
        return list;
    }

    @Override
    protected String analyzeSpecialCases(Map<String, String> map) {
        try {
            if (map.containsKey("TYP")) {
                try {
                    HardwareType.getFromId(Integer.parseInt(map.get("TYP")));
                } catch (NumberFormatException ignored) {

                }
            }
        } catch (IllegalArgumentException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.typeInvalid") + "\n", getGameFile().getName(), getType(), map.get("NAME EN"), e.getMessage());
        }
        if (map.containsKey("PIC")) {
            if (!MGT2Paths.HARDWARE_ICONS.getPath().resolve(map.get("PIC")).toFile().exists()) {
                return String.format(I18n.INSTANCE.get("verifyContentIntegrity.pictureNotFound") + "\n", getGameFile().getName(), getType(), map.get("NAME EN"), map.get("PIC"), MGT2Paths.HARDWARE_ICONS.getPath());
            }
        }
        return "";
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        ArrayList<Integer> requiredGenres = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                requiredGenres.add(SharingHelper.getContentIdByNameFromImport(GameplayFeatureManager.INSTANCE, (String) entry.getValue()));
            }
        }
        Image icon = null;
        if (map.containsKey("platform_icon")) {
            icon = new Image(assetsFolder.resolve((String) map.get("platform_icon")).toFile(), MGT2Paths.HARDWARE_ICONS.getPath().resolve("icon_" + Utils.convertName((String) map.get("NAME EN")) + ".png").toFile());
        }
        return new Hardware(
                (String) map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                (String) map.get("DATE"),
                HardwareType.getFromId(Integer.parseInt((String) map.get("TYP"))),
                Integer.parseInt((String) map.get("RES POINTS")),
                Integer.parseInt((String) map.get("PRICE")),
                Integer.parseInt((String) map.get("DEV COSTS")),
                Integer.parseInt((String) map.get("TECHLEVEL")),
                map.containsKey("ONLY_HANDHELD"),
                map.containsKey("ONLY_STATIONARY"),
                requiredGenres,
                icon
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JComboBox<String> comboBoxType = WindowHelper.getComboBox(HardwareType.class, "mod.hardware.addMod.components.comboBox.type.toolTip", HardwareType.CPU.getTypeName());

        AtomicReference<JSpinner> spinnerResearchPoints = new AtomicReference<>(WindowHelper.getResearchPointSpinner());
        AtomicReference<JSpinner> spinnerTechLevel = new AtomicReference<>(WindowHelper.getTechLevelSpinner());
        JLabel labelComponentRating = new JLabel(String.format("%s: ", I18n.INSTANCE.get("commonText.componentRating")));
        AtomicReference<JTextField> tfComponentRating = new AtomicReference<>(new JTextField());
        tfComponentRating.get().setEditable(false);
        tfComponentRating.get().setText(String.format("%03d", calculateComponentRating((Integer) spinnerTechLevel.get().getValue(), (Integer) spinnerResearchPoints.get().getValue())));
        tfComponentRating.get().setToolTipText("<html>" + I18n.INSTANCE.get("mod.hardware.addMod.components.textBox.rating"));
        JPanel panelComponentRating = new JPanel();
        panelComponentRating.add(labelComponentRating);
        panelComponentRating.add(tfComponentRating.get());

        spinnerResearchPoints.get().addChangeListener(c -> {
            tfComponentRating.get().setText(String.format("%03d", calculateComponentRating((Integer) spinnerTechLevel.get().getValue(), (Integer) spinnerResearchPoints.get().getValue())));
        });

        spinnerTechLevel.get().addChangeListener(c -> {
            tfComponentRating.get().setText(String.format("%03d", calculateComponentRating((Integer) spinnerTechLevel.get().getValue(), (Integer) spinnerResearchPoints.get().getValue())));
        });

        JSpinner spinnerCost = WindowHelper.getBaseSpinner("commonText.cost.spinner.toolTip", 500000, 0, 30000000, 100000);
        JSpinner spinnerDevelopmentCost = WindowHelper.getBaseSpinner("commonText.developmentCost.spinner", 1000000, 0, 50000000, 10000);

        JComboBox<String> comboBoxExclusivity = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"), I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value2")}));
        comboBoxExclusivity.setSelectedItem(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"));
        comboBoxExclusivity.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.toolTip"));
        comboBoxExclusivity.setEnabled(false);

        AtomicBoolean lastValue = new AtomicBoolean(false);
        JCheckBox checkBoxEnableExclusivity = new JCheckBox(I18n.INSTANCE.get("mod.hardware.addMod.components.checkBox.exclusiveConsole"));
        checkBoxEnableExclusivity.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.checkBox.exclusiveConsole.toolTip"));
        checkBoxEnableExclusivity.addChangeListener(changeListener -> {
            if (checkBoxEnableExclusivity.isSelected() != lastValue.get()) {
                comboBoxExclusivity.setEnabled(checkBoxEnableExclusivity.isSelected());
                lastValue.set(checkBoxEnableExclusivity.isSelected());
            }
        });

        AtomicReference<Path> customIconPath = new AtomicReference<>(null);
        JButton buttonCustomIcon = new JButton(I18n.INSTANCE.get("mod.hardware.addMod.components.button.customIcon"));
        buttonCustomIcon.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.button.customIcon.toolTip"));
        buttonCustomIcon.addActionListener(actionEvent -> {
            try {
                Path imageFilePath = Utils.getImagePath();
                if (imageFilePath.toFile().exists()) {
                    customIconPath.set(imageFilePath);
                }
                buttonCustomIcon.setText(I18n.INSTANCE.get("mod.hardware.addMod.components.button.customIcon.selected"));
            } catch (ModProcessingException e) {
                customIconPath.set(null);
                buttonCustomIcon.setText(I18n.INSTANCE.get("mod.hardware.addMod.components.button.customIcon"));
                e.printStackTrace();
            }
        });

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getTypePanel(comboBoxType), WindowHelper.getSpinnerPanel(spinnerResearchPoints.get(), SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerTechLevel.get(), SpinnerType.TECH_LEVEL), panelComponentRating, checkBoxEnableExclusivity, comboBoxExclusivity, buttonCustomIcon};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue"))) {
                    boolean modAlreadyExists = false;
                    for (String string : getContentByAlphabet()) {
                        if (textFieldName.getText().equals(string)) {
                            modAlreadyExists = true;
                        }
                    }
                    if (!modAlreadyExists) {
                        HardwareType hT = null;
                        for (HardwareType hardwareType : HardwareType.values()) {
                            if (Objects.requireNonNull(comboBoxType.getSelectedItem()).toString().equals(hardwareType.getTypeName())) {
                                hT = hardwareType;
                            }
                        }
                        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
                        if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("commonText.controller"))) {
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) < 1992) {
                                requiredGameplayFeatures.add(45);
                            } else {
                                requiredGameplayFeatures.add(51);
                            }
                        }
                        if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("commonText.screen"))) {
                            requiredGameplayFeatures.add(45);
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) > 2001) {
                                requiredGameplayFeatures.add(65);
                            }
                        }
                        boolean onlyStationary = false;
                        boolean onlyHandheld = false;
                        if (checkBoxEnableExclusivity.isSelected()) {
                            if (Objects.requireNonNull(comboBoxExclusivity.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"))) {
                                onlyStationary = true;
                            } else {
                                onlyHandheld = true;
                            }
                        }
                        Image icon = null;
                        if (customIconPath.get() != null) {
                            icon = new Image(customIconPath.get().toFile(), MGT2Paths.HARDWARE_ICONS.getPath().resolve(Utils.convertName(textFieldName.getText()) + "platform_icon" + ".png").toFile());
                        }
                        AbstractBaseContent hardware = new Hardware(
                                textFieldName.getText(),
                                null,
                                new TranslationManager(mapNameTranslations[0]),
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                hT,
                                Integer.parseInt(spinnerResearchPoints.get().getValue().toString()),
                                Integer.parseInt(spinnerCost.getValue().toString()),
                                Integer.parseInt(spinnerDevelopmentCost.getValue().toString()),
                                Integer.parseInt(spinnerTechLevel.get().getValue().toString()),
                                onlyHandheld,
                                onlyStationary,
                                requiredGameplayFeatures,
                                icon
                        );
                        if (JOptionPane.showConfirmDialog(null, hardware.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(hardware);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardware.upperCase") + ": [" + hardware.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameDescriptionFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                replaceMapEntry(map, missingDependency, replacement, entry.getKey());
            }
        }
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(GameplayFeatureManager.INSTANCE);
        return arrayList;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        Set<String> gameplayFeatures = new HashSet<>();
        for (Map.Entry<String, Object> entry : importMap.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                gameplayFeatures.add((String)entry.getValue());
            }
        }
        map.put(GameplayFeatureManager.INSTANCE.getId(), gameplayFeatures);
        return map;
    }

    /**
     * Calculates the component rating that the component will receive
     */
    private int calculateComponentRating(int techLevel, int researchPoints) {
        return techLevel * (researchPoints + 500)/100;
    }
}
