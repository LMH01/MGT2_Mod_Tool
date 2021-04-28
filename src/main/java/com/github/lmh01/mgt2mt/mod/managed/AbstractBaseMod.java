package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public abstract class AbstractBaseMod implements BaseFunctions, BaseMod{

    @Override
    public ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> this.doAddModMenuItemAction());
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(removeModItem);
        return menuItems;
    }

    /**
     * @return Returns a new JMenuItem that is used to be added to the export menu.
     */
    public JMenuItem getInitialExportMenuItem(){
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> exportMenuItemAction());
        return menuItem;
    }

    public void doAddModMenuItemAction(){
        Thread thread = new Thread(() -> {
            addModMenuItemAction();
        });
        ThreadHandler.startThread(thread, "runnableAddNew" + getType());
    }

    /**
     * @return Returns an array list that contains all JMenuItems that have been created for the corresponding mod menu
     */
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
     * This function is called when the button export mod is clicked in the export menu
     */
    public abstract void exportMenuItemAction();

    /**
     * @return Returns the file that is modified when the mod is added/removed
     */
    public abstract File getFile();
}
