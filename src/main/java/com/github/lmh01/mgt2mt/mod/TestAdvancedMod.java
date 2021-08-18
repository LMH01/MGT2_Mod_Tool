package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.util.Utils;

import java.io.File;

public class TestAdvancedMod extends AbstractAdvancedMod {

    @Override
    public String getMainTranslationKey() {
        return "testAdvancedMod";
    }

    @Override
    public AbstractBaseMod getMod() {
        return this;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "Genres.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_genres.txt";
    }
}
