package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.util.Utils;

import java.io.File;

public class TestSimpleMod extends AbstractSimpleMod {

    @Override
    public String getMainTranslationKey() {
        return "testSimpleMod";
    }

    @Override
    public AbstractBaseMod getMod() {
        return this;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "NpcGames.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_npcGames.txt";
    }

    @Override
    public String getReplacedLine(String inputString) {
        StringBuilder outputString = new StringBuilder();
        boolean nameComplete = false;
        for(Character character : inputString.toCharArray()){
            if(character.toString().equals("<")){
                nameComplete = true;
            }
            if(!nameComplete){
                outputString.append(character);
            }
        }
        return outputString.toString().trim();
    }
}
