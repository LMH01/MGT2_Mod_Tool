package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.mod.GenreMod;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage3 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage3.class);
    static final WindowAddGenrePage3 FRAME = new WindowAddGenrePage3();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    JSpinner spinnerResearchPoints = new JSpinner();
    JSpinner spinnerDevelopmentCost = new JSpinner();
    JSpinner spinnerGenrePrice = new JSpinner();

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

    public WindowAddGenrePage3() {
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            GenreMod.openStepWindow(4);
            FRAME.dispose();
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            GenreMod.openStepWindow(2);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if (Utils.showConfirmDialog(1)) {
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.3"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        spinnerResearchPoints.setBounds(180, 10, 100, 23);
        spinnerDevelopmentCost.setBounds(180, 35, 100, 23);
        spinnerGenrePrice.setBounds(180, 60, 100, 23);
        spinnerResearchPoints.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 10.000; " + I18n.INSTANCE.get("commonText.default") + ": 1.000]" + "<br>" + I18n.INSTANCE.get("commonText.researchPointCost.spinner.toolTip"));
        spinnerDevelopmentCost.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 100.000; " + I18n.INSTANCE.get("commonText.default") + ": 3.000]" + "<br>" + I18n.INSTANCE.get("commonText.developmentCost.spinner.toolTip"));
        spinnerGenrePrice.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 1.000.000; " + I18n.INSTANCE.get("commonText.default") + ": 150.000]" + "<br>" + I18n.INSTANCE.get("commonText.researchCost.spinner.toolTip"));
        if (Settings.disableSafetyFeatures) {
            spinnerResearchPoints.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("RES POINTS")), 0, Integer.MAX_VALUE, 1));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("DEV COSTS")), 0, Integer.MAX_VALUE, 1));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("PRICE")), 0, Integer.MAX_VALUE, 1));
            ((JSpinner.DefaultEditor) spinnerResearchPoints.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor) spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor) spinnerGenrePrice.getEditor()).getTextField().setEditable(true);
        } else {
            spinnerResearchPoints.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("RES POINTS")), 0, 10000, 100));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("DEV COSTS")), 0, 100000, 1000));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(Integer.parseInt(GenreMod.mapNewGenre.get("PRICE")), 0, 1000000, 1000));
            ((JSpinner.DefaultEditor) spinnerResearchPoints.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) spinnerDevelopmentCost.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) spinnerGenrePrice.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerResearchPoints);
        contentPane.add(spinnerDevelopmentCost);
        contentPane.add(spinnerGenrePrice);

        JLabel labelResearchPoints = new JLabel(I18n.INSTANCE.get("commonText.researchPointCost") + ":");
        labelResearchPoints.setBounds(10, 10, 150, 23);
        contentPane.add(labelResearchPoints);

        JLabel labelGenreDevelopmentCost = new JLabel(I18n.INSTANCE.get("commonText.developmentCost") + ":");
        labelGenreDevelopmentCost.setBounds(10, 35, 150, 23);
        contentPane.add(labelGenreDevelopmentCost);

        JLabel labelGenrePrice = new JLabel(I18n.INSTANCE.get("commonText.researchCost") + ":");
        labelGenrePrice.setBounds(10, 60, 150, 23);
        contentPane.add(labelGenrePrice);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private static void saveInputs(JSpinner spinnerResearchPoints, JSpinner spinnerDevelopmentCost, JSpinner spinnerGenrePrice) {
        GenreMod.mapNewGenre.remove("RES POINTS");
        GenreMod.mapNewGenre.remove("DEV COSTS");
        GenreMod.mapNewGenre.remove("PRICE");
        GenreMod.mapNewGenre.put("RES POINTS", spinnerResearchPoints.getValue().toString());
        GenreMod.mapNewGenre.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
        GenreMod.mapNewGenre.put("PRICE", spinnerGenrePrice.getValue().toString());
        LOGGER.info("genre research points: " + Integer.parseInt(spinnerResearchPoints.getValue().toString()));
        LOGGER.info("genre development cost: " + Integer.parseInt(spinnerDevelopmentCost.getValue().toString()));
        LOGGER.info("genre price: " + Integer.parseInt(spinnerGenrePrice.getValue().toString()));
    }
}
