package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.PublisherAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.PublisherEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.PublisherSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublisherMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherMod.class);
    PublisherAnalyzer publisherAnalyzer = new PublisherAnalyzer();
    PublisherEditor publisherEditor = new PublisherEditor();
    PublisherSharer publisherSharer = new PublisherSharer();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public PublisherAnalyzer getAnalyzer(){
        return publisherAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public PublisherEditor getEditor(){
        return publisherEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public PublisherSharer getSharer(){
        return publisherSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return publisherAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return publisherEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return publisherSharer;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.6.0", "1.7.0", "1.7.1", "1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.publisher");
    }

    @Override
    protected AbstractBaseMod getMod() {
        return ModManager.publisherMod;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }
}
