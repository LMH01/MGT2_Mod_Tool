package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import java.util.ArrayList;

public interface DependentContentManager {

    /**
     * @return An array list that contains all the dependencies of the mod
     */
    ArrayList<BaseContentManager> getDependencies();

    /**
     * Analyses all dependencies of this mod
     */
    default void analyzeDependencies() throws ModProcessingException  {
        for (BaseContentManager mod : getDependencies()) {
            mod.analyzeFile();
        }
    }
}
