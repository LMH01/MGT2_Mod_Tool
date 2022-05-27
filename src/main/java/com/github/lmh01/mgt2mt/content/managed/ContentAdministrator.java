package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.content.manager.*;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Manages all different contents
 */
public class ContentAdministrator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentAdministrator.class);
    public static final ArrayList<BaseContentManager> contentManagers = new ArrayList<>();

    public static void initializeContentManagers() {
        contentManagers.add(AntiCheatManager.INSTANCE);
        contentManagers.add(CopyProtectionManager.INSTANCE);
        contentManagers.add(EngineFeatureManager.INSTANCE);
        contentManagers.add(GameplayFeatureManager.INSTANCE);
        contentManagers.add(NpcIpManager.INSTANCE);
        contentManagers.add(GenreManager.INSTANCE);
        contentManagers.add(HardwareFeatureManager.INSTANCE);
        contentManagers.add(HardwareManager.INSTANCE);
        contentManagers.add(LicenceManager.INSTANCE);
        contentManagers.add(NpcEngineManager.INSTANCE);
        contentManagers.add(NpcGameManager.INSTANCE);
        contentManagers.add(PlatformManager.INSTANCE);
        contentManagers.add(PublisherManager.INSTANCE);
        contentManagers.add(ThemeManager.INSTANCE);
        DebugHelper.debug(LOGGER, "Total content managers active: " + contentManagers.size());
    }

    /**
     * Analyses all the content
     */
    public static void analyzeContents() throws ModProcessingException {
        for (BaseContentManager manager : contentManagers) {
            manager.analyzeFile();
        }
    }

    /**
     * Shows the following message to the user:
     *
     * @param e The ModProcessingException for wich the message should be displayed
     */
    public static void showException(ModProcessingException e) {
        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("frame.title.error") + ": " + e.getMessage().replace(" - ", "\n - "), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message that the mod has been added successfully.
     * Also writes a message to the text area.
     */
    public static void modAdded(String modName, String modType) {
        JOptionPane.showMessageDialog(null, modType + " \"" + modName + "\" " + I18n.INSTANCE.get("commonText.hasSuccessfullyBeenAddedToTheGame"), "frame.title.success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Calls the function {@link AbstractBaseContentManager#initializeDefaultContent()} for each content manager.
     */
    public static void initializeDefaultContents() {
        for (BaseContentManager manager : contentManagers) {
            if (manager instanceof AbstractBaseContentManager) {
                ((AbstractBaseContentManager)manager).initializeDefaultContent();
            }
        }
    }

    /**
     * Analyzes the integrity of the games files and displays a warning if the integrity is not correct.
     */
    public static void analyzeGameFileIntegrity(boolean showInTextArea) {
        StringBuilder integrityViolations = new StringBuilder();

        LOGGER.info("Checking game file integrity...");
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            if (manager instanceof AbstractAdvancedContentManager) {
                integrityViolations.append(((AbstractAdvancedContentManager) manager).verifyContentIntegrity());
            }
        }
        if (integrityViolations.toString().isEmpty()) {
            if (showInTextArea) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.gameFileIntegrityCheckSuccessful"));
            }
            LOGGER.info("Integrity check successful!");
        } else {
            LOGGER.error("Error: Game file integrity is violated: \n" + integrityViolations.toString());
            TextAreaHelper.appendText(integrityViolations.toString());
            TextAreaHelper.appendText(I18n.INSTANCE.get("warnMessage.integrityCheckFailed.textArea.1"));
            TextAreaHelper.appendText(I18n.INSTANCE.get("warnMessage.integrityCheckFailed.textArea.2"));
        }
    }
}
