package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage8 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage8.class);
    static final WindowAddGenrePage8 FRAME = new WindowAddGenrePage8();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    JSpinner spinnerDesign1 = new JSpinner();
    JSpinner spinnerDesign2 = new JSpinner();
    JSpinner spinnerDesign3 = new JSpinner();
    JSpinner spinnerDesign4 = new JSpinner();
    JSpinner spinnerDesign5 = new JSpinner();
    JSpinner spinnerDesign6 = new JSpinner();
    JSpinner spinnerDesign7 = new JSpinner();
    JSpinner spinnerDesign8 = new JSpinner();
    JSpinner spinnerDesign9 = new JSpinner();
    JSpinner spinnerDesign10 = new JSpinner();
    JSpinner spinnerDesign11 = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setSpinners();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage8() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(false, spinnerDesign1, spinnerDesign2, spinnerDesign3, spinnerDesign4, spinnerDesign5, spinnerDesign6, spinnerDesign7, spinnerDesign8, spinnerDesign9, spinnerDesign10, spinnerDesign11)){
                GenreManager.openStepWindow(9);
                FRAME.dispose();
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(true, spinnerDesign1, spinnerDesign2, spinnerDesign3, spinnerDesign4, spinnerDesign5, spinnerDesign6, spinnerDesign7, spinnerDesign8, spinnerDesign9, spinnerDesign10, spinnerDesign11);
            GenreManager.openStepWindow(7);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 335, 410);
        setResizable(false);
        setTitle("[Page 8] Design");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelDesignFocus = new JLabel("Design focus");
        labelDesignFocus.setBounds(125, 10, 180, 23);
        contentPane.add(labelDesignFocus);

        JLabel labelDesign1 = new JLabel("Game length: ");
        labelDesign1.setBounds(10, 35, 180, 23);
        contentPane.add(labelDesign1);

        JLabel labelDesign2 = new JLabel("Game depth: ");
        labelDesign2.setBounds(10, 60, 180, 23);
        contentPane.add(labelDesign2);

        JLabel labelDesign3 = new JLabel("Beginner friendliness: ");
        labelDesign3.setBounds(10, 85, 180, 23);
        contentPane.add(labelDesign3);

        JLabel labelDesign4 = new JLabel("Innovation: ");
        labelDesign4.setBounds(10, 110, 180, 23);
        contentPane.add(labelDesign4);

        JLabel labelDesign5 = new JLabel("Story: ");
        labelDesign5.setBounds(10, 135, 180, 23);
        contentPane.add(labelDesign5);

        JLabel labelDesign6 = new JLabel("Character design: ");
        labelDesign6.setBounds(10, 160, 180, 23);
        contentPane.add(labelDesign6);

        JLabel labelDesign7 = new JLabel("Level design: ");
        labelDesign7.setBounds(10, 185, 180, 23);
        contentPane.add(labelDesign7);

        JLabel labelDesign8 = new JLabel("Mission design: ");
        labelDesign8.setBounds(10, 210, 180, 23);
        contentPane.add(labelDesign8);

        JLabel labelDesignDirection = new JLabel("Design Direction");
        labelDesignDirection.setBounds(120, 235, 180, 23);
        contentPane.add(labelDesignDirection);

        JLabel labelDesign9 = new JLabel("Core Gamers/Casual Gamer: ");
        labelDesign9.setBounds(10, 260, 180, 23);
        contentPane.add(labelDesign9);

        JLabel labelDesign10 = new JLabel("Nonviolent/Explicit Content");
        labelDesign10.setBounds(10, 285, 180, 23);
        contentPane.add(labelDesign10);

        JLabel labelDesign11 = new JLabel("Easy/Hard");
        labelDesign11.setBounds(10, 310, 180, 23);
        contentPane.add(labelDesign11);

        buttonNext.setBounds(220, 350, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 350, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 350, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }

    private void setSpinners(){
        spinnerDesign1.setBounds(200, 35, 100, 23);
        spinnerDesign2.setBounds(200, 60, 100, 23);
        spinnerDesign3.setBounds(200, 85, 100, 23);
        spinnerDesign4.setBounds(200, 110, 100, 23);
        spinnerDesign5.setBounds(200, 135, 100, 23);
        spinnerDesign6.setBounds(200, 160, 100, 23);
        spinnerDesign7.setBounds(200, 185, 100, 23);
        spinnerDesign8.setBounds(200, 210, 100, 23);
        spinnerDesign9.setBounds(200, 260, 100, 23);
        spinnerDesign10.setBounds(200, 285, 100, 23);
        spinnerDesign11.setBounds(200, 310, 100, 23);
        spinnerDesign1.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If game length is more important for your genre type value bigger 5.<br>If game length is less important type value smaller 5.");
        spinnerDesign2.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If game depth is more important for your genre type value bigger 5.<br>If game depth is less important type value smaller 5.");
        spinnerDesign3.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If beginner friendliness is more important for your genre type value bigger 5.<br>If friendliness is less important type value smaller 5.");
        spinnerDesign4.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If innovation is more important for your genre type value bigger 5.<br>If innovation is less important type value smaller 5.");
        spinnerDesign5.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If story is more important for your genre type value bigger 5.<br>If story is less important type value smaller 5.");
        spinnerDesign6.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If character design is more important for your genre type value bigger 5.<br>If character design is less important type value smaller 5.");
        spinnerDesign7.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If level design is more important for your genre type value bigger 5.<br>If level design is less important type value smaller 5.");
        spinnerDesign8.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Focus:<br>If mission design is more important for your genre type value bigger 5.<br>If mission design is less important type value smaller 5.");

        spinnerDesign9.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Direction:<br>If Core Gamers are favoured type value smaller 5.<br>If Casual Gamers are favoured type value bigger 5.");
        spinnerDesign10.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Direction:<br>If Nonviolence is favoured type value smaller 5.<br>If Explicit Content is favoured type value bigger 5.");
        spinnerDesign11.setToolTipText("<html>[Range: 0 - 10; Default 5]<br>Design Direction:<br>If Easy is favoured type value smaller 5.<br>If Hard is favoured type value bigger 5.");
        spinnerDesign1.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS0")), 0, 10, 1));
        spinnerDesign2.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS1")), 0, 10, 1));
        spinnerDesign3.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS2")), 0, 10, 1));
        spinnerDesign4.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS3")), 0, 10, 1));
        spinnerDesign5.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS4")), 0, 10, 1));
        spinnerDesign6.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS5")), 0, 10, 1));
        spinnerDesign7.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS6")), 0, 10, 1));
        spinnerDesign8.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("FOCUS7")), 0, 10, 1));
        spinnerDesign9.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("ALIGN0")), 0, 10, 1));
        spinnerDesign10.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("ALIGN1")), 0, 10, 1));
        spinnerDesign11.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("ALIGN2")), 0, 10, 1));
        if(Settings.disableSafetyFeatures){
            ((JSpinner.DefaultEditor)spinnerDesign1.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign2.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign3.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign4.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign5.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign6.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign7.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign8.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign9.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign10.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDesign11.getEditor()).getTextField().setEditable(true);
        }else{
            ((JSpinner.DefaultEditor)spinnerDesign1.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign2.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign3.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign4.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign5.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign6.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign7.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign8.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign9.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign10.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDesign11.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerDesign1);
        contentPane.add(spinnerDesign2);
        contentPane.add(spinnerDesign3);
        contentPane.add(spinnerDesign4);
        contentPane.add(spinnerDesign5);
        contentPane.add(spinnerDesign6);
        contentPane.add(spinnerDesign7);
        contentPane.add(spinnerDesign8);
        contentPane.add(spinnerDesign9);
        contentPane.add(spinnerDesign10);
        contentPane.add(spinnerDesign11);
    }

    /**
     * Safes the input to the genre map
     * @return Returns false when the values from spinner 1-8 are combined bigger than 40
     */
    private static boolean saveInputs(boolean calledFromButtonPrevious, JSpinner spinnerDesign1, JSpinner spinnerDesign2, JSpinner spinnerDesign3, JSpinner spinnerDesign4, JSpinner spinnerDesign5, JSpinner spinnerDesign6, JSpinner spinnerDesign7, JSpinner spinnerDesign8, JSpinner spinnerDesign9, JSpinner spinnerDesign10, JSpinner spinnerDesign11) {
        int combinedValue = Integer.parseInt(spinnerDesign1.getValue().toString())
                + Integer.parseInt(spinnerDesign2.getValue().toString())
                + Integer.parseInt(spinnerDesign3.getValue().toString())
                + Integer.parseInt(spinnerDesign4.getValue().toString())
                + Integer.parseInt(spinnerDesign5.getValue().toString())
                + Integer.parseInt(spinnerDesign6.getValue().toString())
                + Integer.parseInt(spinnerDesign7.getValue().toString())
                + Integer.parseInt(spinnerDesign8.getValue().toString());
        boolean saveValues = false;
        if(Settings.disableSafetyFeatures){
            saveValues = true;
        }else if(combinedValue == 40){
            saveValues = true;
        }else{
            if(!calledFromButtonPrevious){
                JOptionPane.showMessageDialog(null, "Unable to continue:\n\nThe combined value of the design focus spinners has to be 40!\nIt is currently: " + combinedValue, "Unable to continue", JOptionPane.ERROR_MESSAGE);
            }else{
                saveValues = true;
            }
        }
        if(saveValues){
            GenreManager.mapNewGenre.remove("FOCUS0");
            GenreManager.mapNewGenre.remove("FOCUS1");
            GenreManager.mapNewGenre.remove("FOCUS2");
            GenreManager.mapNewGenre.remove("FOCUS3");
            GenreManager.mapNewGenre.remove("FOCUS4");
            GenreManager.mapNewGenre.remove("FOCUS5");
            GenreManager.mapNewGenre.remove("FOCUS6");
            GenreManager.mapNewGenre.remove("FOCUS7");
            GenreManager.mapNewGenre.remove("ALIGN0");
            GenreManager.mapNewGenre.remove("ALIGN1");
            GenreManager.mapNewGenre.remove("ALIGN2");
            GenreManager.mapNewGenre.put("FOCUS0", spinnerDesign1.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS1", spinnerDesign2.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS2", spinnerDesign3.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS3", spinnerDesign4.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS4", spinnerDesign5.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS5", spinnerDesign6.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS6", spinnerDesign7.getValue().toString());
            GenreManager.mapNewGenre.put("FOCUS7", spinnerDesign8.getValue().toString());
            GenreManager.mapNewGenre.put("ALIGN0", spinnerDesign9.getValue().toString());
            GenreManager.mapNewGenre.put("ALIGN1", spinnerDesign10.getValue().toString());
            GenreManager.mapNewGenre.put("ALIGN2", spinnerDesign11.getValue().toString());
            LOGGER.info("Design focus 1 = " + spinnerDesign1.getValue().toString());
            LOGGER.info("Design focus 2 = " + spinnerDesign2.getValue().toString());
            LOGGER.info("Design focus 3 = " + spinnerDesign3.getValue().toString());
            LOGGER.info("Design focus 4 = " + spinnerDesign4.getValue().toString());
            LOGGER.info("Design focus 5 = " + spinnerDesign5.getValue().toString());
            LOGGER.info("Design focus 6 = " + spinnerDesign6.getValue().toString());
            LOGGER.info("Design focus 7 = " + spinnerDesign7.getValue().toString());
            LOGGER.info("Design focus 8 = " + spinnerDesign8.getValue().toString());
            LOGGER.info("Design direction 1 = " + spinnerDesign9.getValue().toString());
            LOGGER.info("Design direction 2 = " + spinnerDesign10.getValue().toString());
            LOGGER.info("Design direction 3 = " + spinnerDesign11.getValue().toString());
            return true;
        }else{
            return false;
        }
    }
}
