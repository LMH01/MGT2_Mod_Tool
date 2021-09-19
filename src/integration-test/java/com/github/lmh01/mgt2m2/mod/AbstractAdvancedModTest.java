package com.github.lmh01.mgt2m2.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AbstractAdvancedModTest {

    private Path testFolder = ModManagerPaths.MAIN.getPath().resolve("test");
    private Path testGameFile = testFolder.resolve("mod_game_file.txt");

    public AbstractAdvancedMod advancedMod = new AbstractAdvancedMod() {

        @Override
        protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
            EditHelper.printLine("ID", map, bw);
            TranslationManager.printLanguages(bw, map);
            EditHelper.printLine("DATE", map, bw);
            EditHelper.printLine("PRICE", map, bw);
            EditHelper.printLine("DEV COSTS", map, bw);
        }

        @Override
        public String[] getCompatibleModToolVersions() {
            return new String[]{MadGamesTycoon2ModTool.VERSION};
        }

        @Override
        public String getMainTranslationKey() {
            return "test_mod";
        }

        @Override
        public AbstractBaseMod getMod() {
            return this;
        }

        @Override
        public String getExportType() {
            return "test_mod";
        }

        @Override
        protected String getGameFileName() {
            return "mod_game_file.txt";
        }

        @Override
        public String getDefaultContentFileName() {
            return "default_copy_protect.txt";
        }

        @Override
        protected void openAddModGui() throws ModProcessingException {

        }

        @Override
        protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
            return "Message";
        }

        @Override
        protected Charset getCharset() {
            return StandardCharsets.UTF_8;
        }

        @Override
        public File getGameFile() {
            return testGameFile.toFile();
        }
    };

    @Before
    public void main() {
        setupModFile();
    }

    @Test
    public void TestAbstractAdvancedMod() {
        try {
            advancedMod.analyzeFile();
            Assert.assertTrue(advancedMod.getDefaultContent().length != 0);
            Assert.assertEquals(0, advancedMod.getCustomContentString().length);
            Assert.assertTrue(advancedMod.getFreeId() != 0);
            Assert.assertEquals(25, advancedMod.getFreeId());
            Map<String, String> map = new HashMap<>();
            map.put("NAME EN", "test_mod");
            map.put("DATE", "AUG 1980");
            map.put("PRICE", "15000");
            map.put("DEV COSTS", "5000");
            map.put("ID", Integer.toString(advancedMod.getFreeId()));
            advancedMod.addModToFile(map);
            advancedMod.analyzeFile();
            Assert.assertEquals(1, advancedMod.getCustomContentString().length);
            Assert.assertTrue(advancedMod.getActiveIds().contains(25));
            advancedMod.removeModFromFile("test_mod");
            advancedMod.analyzeFile();
            Assert.assertEquals(0, advancedMod.getCustomContentString().length);
            Assert.assertFalse(Arrays.asList(advancedMod.getCustomContentString()).contains("test_mod"));
        } catch (ModProcessingException e) {
            Assert.fail("Failed to test abstract advanced mod. Mod processing exception got thrown: " + e.getMessage());
        }
    }

    private InputStream openFile(String filename) throws IOException {
        return AbstractAdvancedModTest.class.getClassLoader().getResourceAsStream(filename);
    }

    private void setupModFile() {
        try {
            Files.createDirectories(testFolder);
            if (Files.exists(testGameFile)) {
                Files.delete(testGameFile);
            }
            Files.createFile(testGameFile);
            Files.copy(openFile("default_test_mod.txt"), testGameFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

        }
    }
}
