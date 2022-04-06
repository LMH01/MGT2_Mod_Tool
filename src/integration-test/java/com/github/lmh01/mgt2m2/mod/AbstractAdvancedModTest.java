package com.github.lmh01.mgt2m2.mod;

import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AbstractAdvancedModTest {

    private Path testFolder = ModManagerPaths.MAIN.getPath().resolve("test");
    private Path testGameFile = testFolder.resolve("mod_game_file.txt");

    @Before
    public void main() {
        setupModFile();
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
