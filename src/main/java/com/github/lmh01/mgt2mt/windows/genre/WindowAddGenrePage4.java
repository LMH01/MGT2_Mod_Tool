package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class WindowAddGenrePage4 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage4.class);
    static final WindowAddGenrePage4 FRAME = new WindowAddGenrePage4();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    final JList<String> LIST_TARGET_GROUPS = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_GENRES = new JScrollPane(LIST_TARGET_GROUPS);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage4() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(LIST_TARGET_GROUPS)){
                GenreManager.openStepWindow(5);
                FRAME.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select at least one target group!");
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(LIST_TARGET_GROUPS);
            GenreManager.openStepWindow(3);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAvailableMods.createFrame();
                FRAME.dispose();
            }
        });
    }
    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 175);
        setResizable(false);
        setTitle("[Page 4] Target Group");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTargetGroup = new JLabel("Select the genre target group(s)");
        labelTargetGroup.setBounds(70,5, 200, 23);
        contentPane.add(labelTargetGroup);

        JLabel labelTipPart1 = new JLabel("Tip: Hold STRG and");
        labelTipPart1.setBounds(15, 40, 130, 23);
        contentPane.add(labelTipPart1);

        JLabel labelTipPart2 = new JLabel("click with your mouse");
        labelTipPart2.setBounds(15, 60, 130, 23);
        contentPane.add(labelTipPart2);

        buttonNext.setBounds(220, 115, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 115, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 115, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }
    private void setList(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> arrayListInt = new ArrayList<>();
        if(GenreManager.targetGroupKid){
            arrayListInt.add(0);
        }
        if(GenreManager.targetGroupTeen){
            arrayListInt.add(1);
        }
        if(GenreManager.targetGroupAdult){
            arrayListInt.add(2);
        }
        if(GenreManager.targetGroupSenior){
            arrayListInt.add(3);
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[arrayListInt.size()];
        int index = 0;
        for (final Integer value: arrayListInt) {
            selectedIndices[index++] = value;
        }

        listModel.clear();
        listModel.addElement("Kid");
        listModel.addElement("Teen");
        listModel.addElement("Adult");
        listModel.addElement("Senior");
        LIST_TARGET_GROUPS.setModel(listModel);
        LIST_TARGET_GROUPS.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_TARGET_GROUPS.setLayoutOrientation(JList.VERTICAL);
        LIST_TARGET_GROUPS.setVisibleRowCount(-1);
        LIST_TARGET_GROUPS.setSelectedIndices(selectedIndices);
        SCROLL_PANE_AVAILABLE_GENRES.setBounds(150,30, 100,75);
        contentPane.add(SCROLL_PANE_AVAILABLE_GENRES);
    }
    private static boolean saveInputs(JList<String> listTargetGroups){
        GenreManager.targetGroupKid = false;
        GenreManager.targetGroupTeen = false;
        GenreManager.targetGroupAdult = false;
        GenreManager.targetGroupSenior = false;
        if(listTargetGroups.getSelectedValuesList().size() != 0){
            for(int i=0; i<listTargetGroups.getSelectedValuesList().size(); i++) {
                LOGGER.info("Current target group " + listTargetGroups.getSelectedValuesList().get(i));
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Kid")) {
                    GenreManager.targetGroupKid = true;
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Teen")) {
                    GenreManager.targetGroupTeen = true;
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Adult")) {
                    GenreManager.targetGroupAdult = true;
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Senior")) {
                    GenreManager.targetGroupSenior = true;
                }
            }
            return true;
        }else{
            return false;
        }
    }
}
