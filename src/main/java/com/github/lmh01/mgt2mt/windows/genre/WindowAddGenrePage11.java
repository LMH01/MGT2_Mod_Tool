package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.Genre;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.windows.WindowMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage11 extends JFrame {
    static final WindowAddGenrePage11 FRAME = new WindowAddGenrePage11();
    final JPanel contentPane = new JPanel();
    final JButton buttonBrowse = new JButton("Browse");
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JTextField textFieldImagePath = new JTextField();
    File genreIcon = GenreManager.defaultGenreIcon.toFile();

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

    public WindowAddGenrePage11() {
        buttonBrowse.addActionListener(actionEvent -> {
            String imageFilePath = getGenreImageFilePath(false, true, textFieldImagePath);
            if (!imageFilePath.equals("error") && !imageFilePath.isEmpty()) {
                genreIcon = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            } else {
                textFieldImagePath.setText(GenreManager.defaultGenreIcon.toString());
            }
        });
        buttonNext.addActionListener(actionEvent -> ThreadHandler.startModThread(() -> {
            if (textFieldImagePath.getText().isEmpty()) {
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.picture.noPictureSelected"), "Reset image?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    genreIcon = GenreManager.defaultGenreIcon.toFile();
                    GenreManager.currentGenreHelper.icon = new Image(genreIcon, MGT2Paths.GENRE_ICONS.getPath().resolve("icon" + GenreManager.currentGenreHelper.name.replaceAll(" ", "_") + ".png").toFile());
                }
            } else {
                String imageFilePath = getGenreImageFilePath(true, false, textFieldImagePath);
                if (!imageFilePath.equals("error")) {
                    genreIcon = new File(imageFilePath);
                    GenreManager.currentGenreHelper.icon = new Image(genreIcon, MGT2Paths.GENRE_ICONS.getPath().resolve("icon" + GenreManager.currentGenreHelper.name.replaceAll(" ", "_") + ".png").toFile());
                } else if (textFieldImagePath.getText().isEmpty()) {

                }
            }
            Genre genre = new Genre(
                    GenreManager.currentGenreHelper.name,
                    null,
                    new TranslationManager(GenreManager.currentGenreHelper.nameTranslations, GenreManager.currentGenreHelper.descriptionTranslations),
                    GenreManager.currentGenreHelper.description,
                    GenreManager.currentGenreHelper.date,
                    GenreManager.currentGenreHelper.researchPoints,
                    GenreManager.currentGenreHelper.price,
                    GenreManager.currentGenreHelper.devCosts,
                    GenreManager.currentGenreHelper.icon,
                    GenreManager.currentGenreHelper.screenshots,
                    GenreManager.currentGenreHelper.targetGroups,
                    GenreManager.currentGenreHelper.gameplay,
                    GenreManager.currentGenreHelper.graphic,
                    GenreManager.currentGenreHelper.sound,
                    GenreManager.currentGenreHelper.control,
                    GenreManager.currentGenreHelper.compatibleGenres,
                    GenreManager.currentGenreHelper.compatibleThemes,
                    GenreManager.currentGenreHelper.badGameplayFeatures,
                    GenreManager.currentGenreHelper.goodGameplayFeatures,
                    GenreManager.currentGenreHelper.focus0,
                    GenreManager.currentGenreHelper.focus1,
                    GenreManager.currentGenreHelper.focus2,
                    GenreManager.currentGenreHelper.focus3,
                    GenreManager.currentGenreHelper.focus4,
                    GenreManager.currentGenreHelper.focus5,
                    GenreManager.currentGenreHelper.focus6,
                    GenreManager.currentGenreHelper.focus7,
                    GenreManager.currentGenreHelper.align0,
                    GenreManager.currentGenreHelper.align1,
                    GenreManager.currentGenreHelper.align2
                    );
            ThreadHandler.startModThread(() -> GenreManager.INSTANCE.addGenre(genre, true), "AddGenre");
            FRAME.dispose();
            WindowMain.checkActionAvailability();
        }, "WindowAddGenrePage11ButtonNext"));
        buttonPrevious.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(10);
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
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.11"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel(I18n.INSTANCE.get("mod.genre.picture.selectImage"));
        labelTitle.setBounds(100, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        textFieldImagePath.setBounds(20, 30, 210, 23);
        textFieldImagePath.setToolTipText(I18n.INSTANCE.get("mod.genre.picture.imagePath"));
        textFieldImagePath.setText(genreIcon.getPath());
        contentPane.add(textFieldImagePath);

        buttonBrowse.setBounds(240, 30, 80, 23);
        buttonBrowse.setToolTipText(I18n.INSTANCE.get("mod.genre.picture.button.toolTip"));
        contentPane.add(buttonBrowse);

        JLabel labelYouCanSkipThisStep = new JLabel(I18n.INSTANCE.get("mod.genre.picture.hint.1"));
        labelYouCanSkipThisStep.setBounds(15, 55, 300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel(I18n.INSTANCE.get("mod.genre.picture.hint.2"));
        labelAddYourOwnGenre.setBounds(15, 75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

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

    public String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog, JTextField textFieldImagePath) {
        if (useTextFiledPath) {
            String textFieldPath = textFieldImagePath.getText();
            if (textFieldPath.endsWith(".png")) {
                File imageFile = new File(textFieldPath);
                if (imageFile.exists()) {
                    if (showDialog) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return textFieldPath;
                } else {
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            } else {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                return "error";
            }
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if (f.getName().contains(".png")) {
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFile.fileChooser"));

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    if (fileChooser.getSelectedFile().getName().contains(".png")) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    } else {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return "error";
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                return "error";
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }
}
