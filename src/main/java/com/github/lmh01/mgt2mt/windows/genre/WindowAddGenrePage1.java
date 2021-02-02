package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage1 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage1.class);
    static final WindowAddGenrePage1 FRAME = new WindowAddGenrePage1();
    JPanel contentPane = new JPanel();
    JLabel labelGenreID = new JLabel("Genre id: ");
    JButton buttonExplainGenreID = new JButton("id?");
    JButton buttonNext = new JButton("Next");
    JButton buttonQuit = new JButton("Cancel");
    JTextField textFieldGenreName = new JTextField();
    JTextField textFieldGenreDescription = new JTextField();
    final JSpinner spinnerId = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage1() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(spinnerId, textFieldGenreName, textFieldGenreDescription)){
                NewGenreManager.openStepWindow(2);
                FRAME.dispose();
            }
        });

        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)) {
                WindowAvailableMods.createFrame();
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("[Page 1]");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        if(Settings.disableSafetyFeatures){
            setBounds(100, 100, 335, 160);
            spinnerId.setModel(new SpinnerNumberModel(0, 0, 999, 1));
            spinnerId.setToolTipText("<html>[Range: 0-999]<br>This is the unique id for your genre.<br>Do not change it unless you have your own genre id system.");
            spinnerId.setEnabled(true);
            spinnerId.setVisible(true);
            labelGenreID.setVisible(true);
            buttonExplainGenreID.setVisible(true);
            buttonNext.setBounds(220, 100, 100, 23);
            buttonQuit.setBounds(120, 100, 90, 23);
        }else{
            setBounds(100, 100, 335, 130);
            spinnerId.setModel(new SpinnerNumberModel(NewGenreManager.id, NewGenreManager.id, NewGenreManager.id, 1));
            spinnerId.setToolTipText("<html>[Range: Automatic]<br>This is the unique id for your genre.<br>It can only be changed when the safety features are disabled fia the settings.");
            spinnerId.setEnabled(false);
            spinnerId.setVisible(false);
            labelGenreID.setVisible(false);
            buttonExplainGenreID.setVisible(false);
            buttonNext.setBounds(220, 70, 100, 23);
            buttonQuit.setBounds(120, 70, 90, 23);
        }
        spinnerId.setBounds(120, 60, 100, 23);
        contentPane.add(spinnerId);

        JLabel labelGenreName = new JLabel("Genre name: ");
        labelGenreName.setBounds(10, 10, 100, 23);
        labelGenreName.setToolTipText("This name will be used for every translation.");
        contentPane.add(labelGenreName);


        textFieldGenreName.setBounds(120, 10, 100, 23);
        textFieldGenreName.setToolTipText("<html>This is the global genre name.<br>This name is being displayed in every translation.");
        textFieldGenreName.setText(NewGenreManager.name);
        contentPane.add(textFieldGenreName);

        JLabel labelGenreDescription = new JLabel("Genre description: ");
        labelGenreDescription.setBounds(10, 35, 120, 23);
        labelGenreDescription.setToolTipText("This description will be used for every translation.");
        contentPane.add(labelGenreDescription);

        labelGenreID.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenreID);

        textFieldGenreDescription.setBounds(120, 35, 100, 23);
        textFieldGenreDescription.setText(NewGenreManager.description);
        textFieldGenreDescription.setToolTipText("Enter the genre description that should be shown in game. Hint: use <br> in your description to make a new line.");
        contentPane.add(textFieldGenreDescription);

        buttonExplainGenreID.setBounds(230, 60, 80, 23);
        buttonExplainGenreID.setToolTipText("Click to learn what the genre id is");
        buttonExplainGenreID.addActionListener(actionEvent -> JOptionPane.showMessageDialog(null, "The genre id is the unique id under which your genre can be found.\nWhenever a game file is modified that should reference your genre this genre id is used.", "Genre id", JOptionPane.INFORMATION_MESSAGE));
        contentPane.add(buttonExplainGenreID);

        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JSpinner spinnerId, JTextField textFieldGenreName, JTextField textFieldGenreDescription){
        if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.contains(Integer.parseInt(spinnerId.getValue().toString()))){
            JOptionPane.showMessageDialog(new Frame(), "Please enter a different genre id.\nYour id is already in use!");
            return false;
        }else if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE.contains(textFieldGenreName.getText())){
            JOptionPane.showMessageDialog(new Frame(), "Please enter a different genre name.\nYour name is already in use!");
            return false;
        }else{
            NewGenreManager.id = Integer.parseInt(spinnerId.getValue().toString());
            LOGGER.info("genre id: " + Integer.parseInt(spinnerId.getValue().toString()));
            try{
                if(textFieldGenreDescription.getText().isEmpty() || textFieldGenreName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(new Frame(), "Please enter a name and description first.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(textFieldGenreDescription.getText().matches(".*\\d.*") || textFieldGenreName.getText().matches(".*\\d.*")){
                        JOptionPane.showMessageDialog(new Frame(), "Name and description may not contain numbers.\nPlease enter another name/description.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(NewGenreManager.FORBIDDEN_GENRE_NAMES.contains(textFieldGenreName.getText())){
                            JOptionPane.showMessageDialog(new Frame(), "This genre name is forbidden.\nPlease enter another name.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                        }else{
                            NewGenreManager.name = textFieldGenreName.getText();
                            LOGGER.info("genre name: " + textFieldGenreName.getText());
                            NewGenreManager.description = textFieldGenreDescription.getText();
                            LOGGER.info("genre description: " + textFieldGenreDescription.getText());
                            NewGenreManager.imageFileName = "icon" + textFieldGenreName.getText().replace(" ", "");
                            return true;
                        }
                    }
                }
            }catch (NullPointerException e){
                LOGGER.info("Something went wrong.");
                JOptionPane.showMessageDialog(new Frame(), "Please enter a name and description first.");
                return false;
            }
            return false;
        }
    }
}
