package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.TargetGroup;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class WindowAddGenrePage4 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage4.class);
    static final WindowAddGenrePage4 FRAME = new WindowAddGenrePage4();
    final JPanel contentPane = new JPanel();
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JList<String> LIST_TARGET_GROUPS = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_GENRES = new JScrollPane(LIST_TARGET_GROUPS);

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage4() {
        buttonNext.addActionListener(actionEvent -> {
            if (saveInputs(LIST_TARGET_GROUPS)) {
                GenreManager.openStepWindow(5);
                FRAME.dispose();
            } else {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("mod.genre.selectAtLeastOneTargetGroup"));
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(LIST_TARGET_GROUPS);
            GenreManager.openStepWindow(3);
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
        setBounds(100, 100, 335, 175);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.4"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTargetGroup = new JLabel(I18n.INSTANCE.get("commonText.targetGroup") + ":");
        labelTargetGroup.setBounds(70, 5, 200, 23);
        contentPane.add(labelTargetGroup);

        JLabel labelTipPart1 = new JLabel(I18n.INSTANCE.get("mod.genre.tipPart1"));
        labelTipPart1.setBounds(15, 40, 130, 23);
        contentPane.add(labelTipPart1);

        JLabel labelTipPart2 = new JLabel(I18n.INSTANCE.get("mod.genre.tipPart2"));
        labelTipPart2.setBounds(15, 60, 130, 23);
        contentPane.add(labelTipPart2);

        buttonNext.setBounds(220, 115, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 115, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 115, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private void setList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> arrayListInt = new ArrayList<>();
        for (TargetGroup tg : GenreManager.currentGenreHelper.targetGroups) {
            arrayListInt.add(tg.getId());
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[arrayListInt.size()];
        int index = 0;
        for (final Integer value : arrayListInt) {
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
        SCROLL_PANE_AVAILABLE_GENRES.setBounds(150, 30, 100, 75);
        contentPane.add(SCROLL_PANE_AVAILABLE_GENRES);
    }

    private static boolean saveInputs(JList<String> listTargetGroups) {
        if (listTargetGroups.getSelectedValuesList().size() != 0) {
            ArrayList<TargetGroup> targetGroups = new ArrayList<>();
            for (int i = 0; i < listTargetGroups.getSelectedValuesList().size(); i++) {
                LOGGER.info("Current target group " + listTargetGroups.getSelectedValuesList().get(i));
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Kid")) {
                    targetGroups.add(TargetGroup.KID);
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Teen")) {
                    targetGroups.add(TargetGroup.TEEN);
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Adult")) {
                    targetGroups.add(TargetGroup.ADULT);
                }
                if (listTargetGroups.getSelectedValuesList().get(i).contains("Senior")) {
                    targetGroups.add(TargetGroup.OLD);
                }
            }
            GenreManager.currentGenreHelper.targetGroups = targetGroups;
            return true;
        } else {
            return false;
        }
    }
}
