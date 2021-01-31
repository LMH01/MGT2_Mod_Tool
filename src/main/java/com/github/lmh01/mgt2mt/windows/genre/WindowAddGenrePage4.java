package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage4 extends JFrame{
    static WindowAddGenrePage4 frame = new WindowAddGenrePage4();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage4.class);
    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage4() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 4] Target Group");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTargetGroup = new JLabel("Select the genre target group");
        labelTargetGroup.setBounds(70,5, 200, 23);
        contentPane.add(labelTargetGroup);

        JCheckBox checkBoxTargetGroupKid = new JCheckBox("Kid");
        checkBoxTargetGroupKid.setBounds(80, 30, 100, 23);
        checkBoxTargetGroupKid.setToolTipText("Select Kid as target group.");
        checkBoxTargetGroupKid.setSelected(NewGenreManager.targetGroupKid);
        contentPane.add(checkBoxTargetGroupKid);

        JCheckBox checkBoxTargetGroupTeen = new JCheckBox("Teen");
        checkBoxTargetGroupTeen.setBounds(180, 30, 100, 23);
        checkBoxTargetGroupTeen.setToolTipText("Select Teen as target group.");
        checkBoxTargetGroupTeen.setSelected(NewGenreManager.targetGroupTeen);
        contentPane.add(checkBoxTargetGroupTeen);

        JCheckBox checkBoxTargetGroupAdult = new JCheckBox("Adult");
        checkBoxTargetGroupAdult.setBounds(80, 65, 100, 23);
        checkBoxTargetGroupAdult.setToolTipText("Select Adult as target group.");
        checkBoxTargetGroupAdult.setSelected(NewGenreManager.targetGroupAdult);
        contentPane.add(checkBoxTargetGroupAdult);

        JCheckBox checkBoxTargetGroupSenior = new JCheckBox("Senior");
        checkBoxTargetGroupSenior.setBounds(180, 65, 100, 23);
        checkBoxTargetGroupSenior.setToolTipText("Select Senior as target group.");
        checkBoxTargetGroupSenior.setSelected(NewGenreManager.targetGroupSenior);
        contentPane.add(checkBoxTargetGroupSenior);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(checkBoxTargetGroupKid, checkBoxTargetGroupTeen, checkBoxTargetGroupAdult, checkBoxTargetGroupSenior)){
                NewGenreManager.openStepWindow(5);
                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select at least one target group!");
            }
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(checkBoxTargetGroupKid, checkBoxTargetGroupTeen, checkBoxTargetGroupAdult, checkBoxTargetGroupSenior);
            NewGenreManager.openStepWindow(3);
            frame.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Cancel");
        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JCheckBox checkBoxTargetGroupKid, JCheckBox checkBoxTargetGroupTeen, JCheckBox checkBoxTargetGroupAdult, JCheckBox checkBoxTargetGroupSenior){
        if(!checkBoxTargetGroupKid.isSelected() && !checkBoxTargetGroupTeen.isSelected() && !checkBoxTargetGroupAdult.isSelected() && !checkBoxTargetGroupSenior.isSelected()){
            return false;
        }else{
            NewGenreManager.targetGroupKid = checkBoxTargetGroupKid.isSelected();
            LOGGER.info("target group kid: " + checkBoxTargetGroupKid.isSelected());
            NewGenreManager.targetGroupTeen = checkBoxTargetGroupTeen.isSelected();
            LOGGER.info("target group teen: " + checkBoxTargetGroupTeen.isSelected());
            NewGenreManager.targetGroupAdult = checkBoxTargetGroupAdult.isSelected();
            LOGGER.info("target group adult: " + checkBoxTargetGroupAdult.isSelected());
            NewGenreManager.targetGroupSenior = checkBoxTargetGroupSenior.isSelected();
            LOGGER.info("target group senior: " + checkBoxTargetGroupSenior.isSelected());
            return true;
        }
    }
}
