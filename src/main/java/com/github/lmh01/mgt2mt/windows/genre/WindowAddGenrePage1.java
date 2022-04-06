package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.GenreHelper;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WindowAddGenrePage1 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage1.class);
    static final WindowAddGenrePage1 FRAME = new WindowAddGenrePage1();
    private static Map<String, String> mapNameTranslations = new HashMap<>();
    private static Map<String, String> mapDescriptionTranslations = new HashMap<>();
    public static boolean nameTranslationsAdded = false;
    public static boolean descriptionTranslationsAdded = false;
    JPanel contentPane = new JPanel();
    JLabel labelGenreID = new JLabel("Genre id: ");
    JButton buttonExplainGenreID = new JButton("id?");
    JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    JButton buttonAddNameTranslations = new JButton("TRANSL");
    JButton buttonAddDescriptionTranslations = new JButton("TRANSL");
    JButton buttonClearTranslations = new JButton(I18n.INSTANCE.get("mod.genre.clearTranslations"));
    JTextField textFieldGenreName = new JTextField();
    JTextField textFieldGenreDescription = new JTextField();

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage1() {
        buttonNext.addActionListener(actionEvent -> {
            ThreadHandler.startModThread(() -> {
                if (saveInputs(textFieldGenreName, textFieldGenreDescription)) {
                    GenreManager.openStepWindow(2);
                    FRAME.dispose();
                }
            }, "WindowAddGenrePage1ButtonNext");
        });

        buttonQuit.addActionListener(actionEvent -> {
            if (Utils.showConfirmDialog(1)) {
                FRAME.dispose();
            }
        });

        buttonAddNameTranslations.addActionListener(actionEvent -> {
            if (!nameTranslationsAdded) {
                addNameTranslations();
                nameTranslationsAdded = true;
            } else {
                if (JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION) {
                    addNameTranslations();
                    nameTranslationsAdded = true;
                }
            }
        });

        buttonAddDescriptionTranslations.addActionListener(actionEvent -> {
            if (!descriptionTranslationsAdded) {
                addDescriptionTranslations();
                descriptionTranslationsAdded = true;
            } else {
                if (JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION) {
                    addDescriptionTranslations();
                    descriptionTranslationsAdded = true;
                }
            }
        });
        buttonClearTranslations.addActionListener(actionEvent -> {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.clearTranslations.question"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == 0) {
                GenreManager.currentGenreHelper.nameTranslations = null;
                GenreManager.currentGenreHelper.descriptionTranslations = null;
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.genre.clearTranslations.success"));
            }
        });
    }

    private void setGuiComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.1"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        if (Settings.disableSafetyFeatures) {
            setBounds(100, 100, 335, 190);
            labelGenreID.setVisible(true);
            buttonExplainGenreID.setVisible(true);
            buttonNext.setBounds(220, 130, 100, 23);
            buttonQuit.setBounds(120, 130, 90, 23);
            buttonExplainGenreID.setBounds(230, 95, 80, 23);
            buttonExplainGenreID.setToolTipText("Click to learn what the genre id is");
            buttonExplainGenreID.addActionListener(actionEvent -> JOptionPane.showMessageDialog(null, "The genre id is the unique id under which your genre can be found.\nWhenever a game file is modified that should reference your genre this genre id is used.", "Genre id", JOptionPane.INFORMATION_MESSAGE));
            contentPane.add(buttonExplainGenreID);
        } else {
            setBounds(100, 100, 335, 160);
            labelGenreID.setVisible(false);
            buttonExplainGenreID.setVisible(false);
            buttonNext.setBounds(220, 100, 100, 23);
            buttonQuit.setBounds(120, 100, 90, 23);
        }


        JLabel labelGenreName = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        labelGenreName.setBounds(10, 10, 100, 23);
        contentPane.add(labelGenreName);


        textFieldGenreName.setBounds(120, 10, 100, 23);
        textFieldGenreName.setText(GenreManager.currentGenreHelper.name);
        contentPane.add(textFieldGenreName);

        buttonAddNameTranslations.setBounds(230, 10, 90, 23);
        buttonAddNameTranslations.setToolTipText(I18n.INSTANCE.get("commonText.addNameTranslations.toolTip"));
        contentPane.add(buttonAddNameTranslations);

        JLabel labelGenreDescription = new JLabel(I18n.INSTANCE.get("commonText.description") + ":");
        labelGenreDescription.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreDescription);

        labelGenreID.setBounds(10, 95, 120, 23);
        contentPane.add(labelGenreID);

        textFieldGenreDescription.setBounds(120, 35, 100, 23);
        textFieldGenreDescription.setText(GenreManager.currentGenreHelper.description);
        textFieldGenreDescription.setToolTipText(I18n.INSTANCE.get("mod.genre.description.hint"));
        contentPane.add(textFieldGenreDescription);

        buttonAddDescriptionTranslations.setBounds(230, 35, 90, 23);
        buttonAddDescriptionTranslations.setToolTipText(I18n.INSTANCE.get("commonText.addDescriptionTranslations.toolTip"));
        contentPane.add(buttonAddDescriptionTranslations);

        buttonClearTranslations.setBounds(120, 65, 200, 23);
        buttonClearTranslations.setToolTipText(I18n.INSTANCE.get("mod.genre.clearTranslations.button"));
        contentPane.add(buttonClearTranslations);

        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private static boolean saveInputs(JTextField textFieldGenreName, JTextField textFieldGenreDescription) throws ModProcessingException {
        for (String string : GenreManager.INSTANCE.getContentByAlphabet()) {
            if (string.equals(textFieldGenreName.getText())) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"));
                return false;
            }
        }
        if (textFieldGenreName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("mod.genre.enterNameFirst"));
            return false;
        } else if (textFieldGenreDescription.getText().isEmpty()) {
            JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("mod.genre.enterDescriptionFirst"));
        } else {
            GenreManager.currentGenreHelper.name = textFieldGenreName.getText();
            GenreManager.currentGenreHelper.description = textFieldGenreDescription.getText();
            GenreManager.currentGenreHelper.nameTranslations = mapNameTranslations;
            GenreManager.currentGenreHelper.descriptionTranslations = mapDescriptionTranslations;
            return true;
        }
        return false;
    }


    /**
     * Call to add nameTranslations to the genre.
     */
    public static void addNameTranslations() {
        boolean continueWithTranslations = true;
        if (Settings.enableGenreNameTranslationInfo) {
            JCheckBox checkBoxDontShowAgain = new JCheckBox(I18n.INSTANCE.get("mod.genre.translation.donNotShowInfoAgain"));
            JLabel labelMessage = new JLabel(I18n.INSTANCE.get("mod.genre.translation.information"));
            Object[] params = {labelMessage, checkBoxDontShowAgain};
            LOGGER.info("enableGenreNameTranslationInfo: " + Settings.enableGenreDescriptionTranslationInfo);
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.information"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0) {
                continueWithTranslations = false;
            }
            Settings.enableGenreNameTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export(ModManagerPaths.MAIN.getPath().resolve("settings.toml").toFile());
        }
        if (continueWithTranslations) {
            mapNameTranslations = TranslationManager.getTranslationsMap();
            nameTranslationsAdded = true;
        }
    }

    /**
     * Call to add descriptionTranslations to the genre.
     */
    public static void addDescriptionTranslations() {
        boolean continueWithTranslations = true;
        if (Settings.enableGenreDescriptionTranslationInfo) {
            JCheckBox checkBoxDontShowAgain = new JCheckBox(I18n.INSTANCE.get("mod.genre.translation.donNotShowInfoAgain"));
            JLabel labelMessage = new JLabel(I18n.INSTANCE.get("mod.genre.translation.information"));
            Object[] params = {labelMessage, checkBoxDontShowAgain};
            LOGGER.info("enableGenreDescriptionTranslationInfo: " + Settings.enableGenreDescriptionTranslationInfo);
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.information"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0) {
                continueWithTranslations = false;
            }
            Settings.enableGenreDescriptionTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export(ModManagerPaths.MAIN.getPath().resolve("settings.toml").toFile());
        }
        if (continueWithTranslations) {
            mapDescriptionTranslations = TranslationManager.getTranslationsMap();
        }
    }

    /**
     * Clears the translation maps
     */
    public static void clearTranslationArrayLists() {
        mapNameTranslations.clear();
        mapDescriptionTranslations.clear();
    }
}
