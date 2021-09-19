package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.mod.GenreMod;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class WindowAddGenrePage2 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage2.class);
    static final WindowAddGenrePage2 FRAME = new WindowAddGenrePage2();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    JSpinner spinnerUnlockYear = new JSpinner();
    JComboBox<String> comboBoxGenreUnlockMonth = WindowHelper.getUnlockMonthComboBox();

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

    public WindowAddGenrePage2() {
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            GenreMod.openStepWindow(3);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if (Utils.showConfirmDialog(1)) {
                FRAME.dispose();
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            GenreMod.openStepWindow(1);
            FRAME.dispose();
        });
    }

    private void setGuiComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.2"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        if (Settings.disableSafetyFeatures) {
            spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinnerUnlockYear.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("UNLOCK YEAR")), 1976, 2050, 1));
            ((JSpinner.DefaultEditor) spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
        } else {
            spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinnerUnlockYear.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("UNLOCK YEAR")), 1976, 2050, 1));
            ((JSpinner.DefaultEditor) spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
        }

        JLabel labelGenreUnlockDate = new JLabel(I18n.INSTANCE.get("commonText.unlockDate") + ":");
        labelGenreUnlockDate.setBounds(100, 5, 130, 23);
        contentPane.add(labelGenreUnlockDate);

        JLabel labelGenreUnlockMonth = new JLabel(I18n.INSTANCE.get("commonText.unlockMonth") + ":");
        labelGenreUnlockMonth.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreUnlockMonth);

        comboBoxGenreUnlockMonth.setBounds(120, 35, 100, 23);
        contentPane.add(comboBoxGenreUnlockMonth);

        JLabel labelGenreID = new JLabel(I18n.INSTANCE.get("commonText.unlockYear") + ":");
        labelGenreID.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenreID);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);

        spinnerUnlockYear.setBounds(120, 60, 100, 23);
        contentPane.add(spinnerUnlockYear);
    }

    private static void saveInputs(JSpinner spinnerUnlockYear, JComboBox<String> comboBoxGenreUnlockMonth) {
        GenreMod.mapNewGenre.put("DATE", Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxGenreUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString());
        GenreMod.mapNewGenre.put("UNLOCK MONTH", Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxGenreUnlockMonth.getSelectedItem()).toString()));
        GenreMod.mapNewGenre.put("UNLOCK YEAR", spinnerUnlockYear.getValue().toString());
        LOGGER.info("genre unlock year: " + Integer.parseInt(spinnerUnlockYear.getValue().toString()));
        LOGGER.info("Genre unlock month: " + Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxGenreUnlockMonth.getSelectedItem()).toString()));
    }
}
