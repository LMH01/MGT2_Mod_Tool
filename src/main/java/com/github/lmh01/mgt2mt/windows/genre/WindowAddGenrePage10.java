package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowAddGenrePage10 extends JFrame {
    static final WindowAddGenrePage10 FRAME = new WindowAddGenrePage10();
    final JPanel contentPane = new JPanel();
    final JButton buttonAddScreenshot = new JButton("Add screenshot(s)");
    final JButton buttonResetAddedScreenshots = new JButton("Reset");
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));

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

    public WindowAddGenrePage10() {
        buttonAddScreenshot.addActionListener(actionEvent -> {
            ArrayList<File> screenshotsSource = getScreenshotImages();
            ArrayList<Image> screenshots = new ArrayList<>();
            if (screenshotsSource.isEmpty()) {
                for (int i = 0; i<=4; i++) {
                    screenshots.add(new Image(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("0/" + i + ".png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(GenreManager.INSTANCE.getExportImageName(i + ".png", GenreManager.currentGenreHelper.name)).toFile()));
                }
            } else {
                int id = 1;
                for (File file : screenshotsSource) {
                    screenshots.add(new Image(file, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(GenreManager.INSTANCE.getExportImageName(id + ".png", GenreManager.currentGenreHelper.name)).toFile()));
                    id+=1;
                }
            }
            GenreManager.currentGenreHelper.screenshots = screenshots;
        });
        buttonResetAddedScreenshots.addActionListener(actionEvent -> {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.screenshots.button.resetScreenshots.confirm"), I18n.INSTANCE.get("frame.title.reset"), JOptionPane.YES_NO_OPTION) == 0) {
                GenreManager.currentGenreHelper.screenshots = new ArrayList<>();
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            if (GenreManager.currentGenreHelper.screenshots.isEmpty()) {
                ArrayList<Image> screenshots = new ArrayList<>();
                for (int i = 0; i<=4; i++) {
                    screenshots.add(new Image(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("0/" + i + ".png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(GenreManager.INSTANCE.getExportImageName(i + ".png", GenreManager.currentGenreHelper.name)).toFile()));
                }
                GenreManager.currentGenreHelper.screenshots = screenshots;
            }
            GenreManager.openStepWindow(11);
            FRAME.dispose();
        });
        buttonPrevious.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(9);
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
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.10"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.title"));
        labelTitle.setBounds(98, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        buttonAddScreenshot.setBounds(40, 30, 150, 23);
        buttonAddScreenshot.setToolTipText(I18n.INSTANCE.get("mod.genre.screenshots.button.toolTip"));
        contentPane.add(buttonAddScreenshot);

        JLabel labelYouCanSkipThisStep = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.text.1"));
        labelYouCanSkipThisStep.setBounds(15, 55, 300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.text.2"));
        labelAddYourOwnGenre.setBounds(15, 75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

        buttonResetAddedScreenshots.setBounds(210, 30, 80, 23);
        buttonResetAddedScreenshots.setToolTipText(I18n.INSTANCE.get("mod.genre.screenshots.button.resetScreenshots"));
        contentPane.add(buttonResetAddedScreenshots);

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

    /**
     * Opens an ui where the user can select image files.
     *
     * @return Returns the image files as ArrayList
     */
    public static ArrayList<File> getScreenshotImages() {
        ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
        ArrayList<File> arrayListScreenshotFilesSelected = new ArrayList<>();
        JTextField textFieldScreenshotFile = new JTextField();
        JLabel labelMessage = new JLabel(I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.message"));
        JButton buttonBrowse = new JButton(I18n.INSTANCE.get("commonText.browse"));
        AtomicBoolean multipleFilesSelected = new AtomicBoolean(false);
        AtomicInteger numberOfScreenshotsToAdd = new AtomicInteger();
        buttonBrowse.addActionListener(actionEventSmall -> {
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

                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFiles.fileChooser"));
                fileChooser.setMultiSelectionEnabled(true);

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    final int NUMBER_OF_SCREENSHOTS = fileChooser.getSelectedFiles().length;
                    numberOfScreenshotsToAdd.set(NUMBER_OF_SCREENSHOTS);
                    File[] screenshots = fileChooser.getSelectedFiles();
                    if (NUMBER_OF_SCREENSHOTS > 1) {
                        multipleFilesSelected.set(true);
                    }
                    boolean failed = false;
                    for (int i = 0; i < NUMBER_OF_SCREENSHOTS; i++) {
                        if (!failed) {
                            if (screenshots[i].getName().contains(".png")) {
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                                if (multipleFilesSelected.get()) {
                                    arrayListScreenshotFilesSelected.add(screenshots[i]);
                                    textFieldScreenshotFile.setText(I18n.INSTANCE.get("commonText.multipleFilesSelected"));
                                } else {
                                    textFieldScreenshotFile.setText(fileChooser.getSelectedFile().getPath());
                                }
                            } else {
                                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectOnlyPngFile"));
                                failed = true;
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                            }
                        }
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
        Object[] params = {labelMessage, textFieldScreenshotFile, buttonBrowse};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.OK_CANCEL_OPTION) == 0) {
            String textFieldPath = textFieldScreenshotFile.getText();
            if (textFieldPath.endsWith(".png")) {
                File imageFile = new File(textFieldPath);
                if (imageFile.exists()) {
                    arrayListScreenshotFiles.add(new File(textFieldPath));
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.utils.imageFileAdded"));
                } else {
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.fileNotFound"), JOptionPane.ERROR_MESSAGE);
                }
            } else if (multipleFilesSelected.get()) {
                for (int i = 0; i < numberOfScreenshotsToAdd.get(); i++) {
                    arrayListScreenshotFiles.add(arrayListScreenshotFilesSelected.get(i));
                }
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.utils.imageFilesAdded"));
            } else {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));

            }
            return arrayListScreenshotFiles;
        }
        return arrayListScreenshotFiles;
    }
}
