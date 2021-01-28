package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage5 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage5 frame = new WindowAddGenrePage5();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage5.class);
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

    public WindowAddGenrePage5() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 5] Target Group");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTargetGroup = new JLabel("Select the genre target group");
        labelTargetGroup.setBounds(70,5, 200, 23);
        contentPane.add(labelTargetGroup);

        JCheckBox checkBoxTargetGroupKid = new JCheckBox("Kid");
        checkBoxTargetGroupKid.setBounds(80, 30, 100, 23);
        if(NewGenreManager.targetGroupKid){
            checkBoxTargetGroupKid.setSelected(true);
        }else{
            checkBoxTargetGroupKid.setSelected(false);
        }
        contentPane.add(checkBoxTargetGroupKid);

        JCheckBox checkBoxTargetGroupTeen = new JCheckBox("Teen");
        checkBoxTargetGroupTeen.setBounds(180, 30, 100, 23);
        if(NewGenreManager.targetGroupTeen){
            checkBoxTargetGroupTeen.setSelected(true);
        }else{
            checkBoxTargetGroupTeen.setSelected(false);
        }
        contentPane.add(checkBoxTargetGroupTeen);

        JCheckBox checkBoxTargetGroupAdult = new JCheckBox("Adult");
        checkBoxTargetGroupAdult.setBounds(80, 65, 100, 23);
        if(NewGenreManager.targetGroupAdult){
            checkBoxTargetGroupAdult.setSelected(true);
        }else{
            checkBoxTargetGroupAdult.setSelected(false);
        }
        contentPane.add(checkBoxTargetGroupAdult);

        JCheckBox checkBoxTargetGroupSenior = new JCheckBox("Senior");
        checkBoxTargetGroupSenior.setBounds(180, 65, 100, 23);
        if(NewGenreManager.targetGroupSenior){
            checkBoxTargetGroupSenior.setSelected(true);
        }else{
            checkBoxTargetGroupSenior.setSelected(false);
        }
        contentPane.add(checkBoxTargetGroupSenior);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            if(saveInputs(checkBoxTargetGroupKid, checkBoxTargetGroupTeen, checkBoxTargetGroupAdult, checkBoxTargetGroupSenior)){
                NewGenreManager.openStepWindow(6);
                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select at least one target group!");
            }
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            saveInputs(checkBoxTargetGroupKid, checkBoxTargetGroupTeen, checkBoxTargetGroupAdult, checkBoxTargetGroupSenior);
            NewGenreManager.openStepWindow(4);
            frame.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.addActionListener((ignored) -> {
            if(JOptionPane.showConfirmDialog((Component)null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", 0) == 0){
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
            logger.info("target group kid: " + checkBoxTargetGroupKid.isSelected());
            NewGenreManager.targetGroupTeen = checkBoxTargetGroupTeen.isSelected();
            logger.info("target group teen: " + checkBoxTargetGroupTeen.isSelected());
            NewGenreManager.targetGroupAdult = checkBoxTargetGroupAdult.isSelected();
            logger.info("target group adult: " + checkBoxTargetGroupAdult.isSelected());
            NewGenreManager.targetGroupSenior = checkBoxTargetGroupSenior.isSelected();
            logger.info("target group senior: " + checkBoxTargetGroupSenior.isSelected());
            return true;
        }
    }
}
