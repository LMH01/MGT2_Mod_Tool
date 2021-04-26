package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseMod implements BaseMod, BaseFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseMod.class);
    /**
     * Initializes the mod and adds it to the mod array list
     */
    @Override
    public void initializeMod() {
        LOGGER.info("Initializing mod: " + getType());
        ModManager.mods.add(getMod());
    }

    protected abstract AbstractBaseMod getMod();
}
