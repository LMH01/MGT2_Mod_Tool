package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage9 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage9.class);
    static final WindowAddGenrePage9 FRAME = new WindowAddGenrePage9();
    static int combinedValue;
    final JPanel contentPane = new JPanel();
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JSpinner spinnerGameplay = new JSpinner();
    final JSpinner spinnerGraphic = new JSpinner();
    final JSpinner spinnerSound = new JSpinner();
    final JSpinner spinnerControl = new JSpinner();

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setSpinners();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage9() {
        buttonNext.addActionListener(actionEvent -> {
            if (saveInputs(spinnerGameplay, spinnerGraphic, spinnerSound, spinnerControl)) {
                GenreManager.openStepWindow(10);
                FRAME.dispose();
            } else {
                JOptionPane.showMessageDialog(new Frame(), "Can't continue:\nThe combined value has to be 100.\nIt is currently at: " + combinedValue);
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerGameplay, spinnerGraphic, spinnerSound, spinnerControl);
            GenreManager.openStepWindow(8);
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
        setBounds(100, 100, 335, 185);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.9"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelGameplay = new JLabel(I18n.INSTANCE.get("commonText.gameplay") + ":");
        labelGameplay.setBounds(10, 10, 100, 23);
        contentPane.add(labelGameplay);

        JLabel labelGraphic = new JLabel(I18n.INSTANCE.get("commonText.graphic") + ":");
        labelGraphic.setBounds(10, 35, 120, 23);
        contentPane.add(labelGraphic);

        JLabel labelSound = new JLabel(I18n.INSTANCE.get("commonText.sound") + ":");
        labelSound.setBounds(10, 60, 120, 23);
        contentPane.add(labelSound);

        JLabel labelControl = new JLabel(I18n.INSTANCE.get("commonText.control") + ":");
        labelControl.setBounds(10, 85, 120, 23);
        contentPane.add(labelControl);

        buttonNext.setBounds(220, 125, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 125, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 125, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private void setSpinners() {
        spinnerGameplay.setBounds(120, 10, 100, 23);
        spinnerGraphic.setBounds(120, 35, 100, 23);
        spinnerSound.setBounds(120, 60, 100, 23);
        spinnerControl.setBounds(120, 85, 100, 23);
        spinnerGameplay.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Gameplay priority in %");
        spinnerGraphic.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Graphic priority in %");
        spinnerSound.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Sound priority in %");
        spinnerControl.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Control priority in %");
        spinnerGameplay.setModel(new SpinnerNumberModel(GenreManager.currentGenreHelper.gameplay, 5, 85, 5));
        spinnerGraphic.setModel(new SpinnerNumberModel(GenreManager.currentGenreHelper.graphic, 5, 85, 5));
        spinnerSound.setModel(new SpinnerNumberModel(GenreManager.currentGenreHelper.sound, 5, 85, 5));
        spinnerControl.setModel(new SpinnerNumberModel(GenreManager.currentGenreHelper.control, 5, 85, 5));
        if (Settings.safetyFeatures.get(SafetyFeature.UNLOCK_SPINNERS)) {
            ((JSpinner.DefaultEditor) spinnerGameplay.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor) spinnerGraphic.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor) spinnerSound.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor) spinnerControl.getEditor()).getTextField().setEditable(true);
        } else {
            ((JSpinner.DefaultEditor) spinnerGameplay.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) spinnerGraphic.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) spinnerSound.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) spinnerControl.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerGameplay);
        contentPane.add(spinnerGraphic);
        contentPane.add(spinnerSound);
        contentPane.add(spinnerControl);
    }

    private static boolean saveInputs(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl) {
        combinedValue = Integer.parseInt(spinnerGameplay.getValue().toString()) +
                Integer.parseInt(spinnerGraphic.getValue().toString()) +
                Integer.parseInt(spinnerSound.getValue().toString()) +
                Integer.parseInt(spinnerControl.getValue().toString());
        LOGGER.info("combined value: " + combinedValue);
        if (combinedValue == 100 && testIfDividableBy5(spinnerGameplay, spinnerGraphic, spinnerSound, spinnerControl)) {
            GenreManager.currentGenreHelper.gameplay = Integer.parseInt(spinnerGameplay.getValue().toString());
            GenreManager.currentGenreHelper.graphic = Integer.parseInt(spinnerGraphic.getValue().toString());
            GenreManager.currentGenreHelper.sound = Integer.parseInt(spinnerSound.getValue().toString());
            GenreManager.currentGenreHelper.control = Integer.parseInt(spinnerControl.getValue().toString());
            LOGGER.info("Gameplay = " + spinnerGameplay.getValue().toString());
            LOGGER.info("graphic = " + spinnerGraphic.getValue().toString());
            LOGGER.info("sound = " + spinnerSound.getValue().toString());
            LOGGER.info("control = " + spinnerControl.getValue().toString());
            return true;
        } else {
            return false;
        }
    }

    private static boolean testIfDividableBy5(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl) {
        boolean dividableBy5 = Integer.parseInt(spinnerGameplay.getValue().toString()) % 5 == 0;
        if (Integer.parseInt(spinnerGraphic.getValue().toString()) % 5 != 0) {
            dividableBy5 = false;
        }
        if (Integer.parseInt(spinnerSound.getValue().toString()) % 5 != 0) {
            dividableBy5 = false;
        }
        if (Integer.parseInt(spinnerControl.getValue().toString()) % 5 != 0) {
            dividableBy5 = false;
        }
        return dividableBy5;
    }
}
