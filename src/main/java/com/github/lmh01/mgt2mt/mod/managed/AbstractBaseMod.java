package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.util.I18n;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public abstract class AbstractBaseMod implements BaseFunctions, BaseMod{

    @Override
    public ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> addModMenuItemAction());
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(removeModItem);
        return menuItems;
    }

    public abstract ArrayList<JMenuItem> getModMenuItems();

    /**
     * This function is called when the button add mod is clicked in the main menu
     */
    public abstract void addModMenuItemAction();

    /**
     * This function is called when the button remove mod is clicked in the main menu
     */
    public abstract void removeModMenuItemAction();

    /**
     * @return Returns the file that is modified when the mod is added/removed
     */
    public abstract File getFile();
}
