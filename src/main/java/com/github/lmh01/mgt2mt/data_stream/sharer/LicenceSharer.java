package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.interfaces.SimpleImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenceSharer extends AbstractSimpleSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureSharer.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.licence");
    }

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.licenceMod.getAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Licence//";
    }

    @Override
    public String getImportExportFileName() {
        return ModManager.licenceMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public String getTypeCaps() {
        return "LICENCE";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.licenceMod.getCompatibleModToolVersions();    }

    @Override
    public SimpleImporter getSimpleImporter() {
        return ModManager.licenceMod.getEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(String line) {
        String type = I18n.INSTANCE.get("settings.notFound");
        if(line.contains("[BOOK]")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book");
        }else if(line.contains("MOVIE")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie");
        }else if(line.contains("SPORT")){
            type = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport");
        }
        return I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence") + "<br>" + getAnalyzer().getReplacedLine(line) + "<br>" + I18n.INSTANCE.get("dialog.sharingHandler.type") + " " + type;
    }
}
