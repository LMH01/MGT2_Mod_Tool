package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

/**
 * Used to create a new content.
 * getExportMap() has a default implementation here. Should be overwritten when a dependent content is created.
 * Advanced content also contains a translation manager that has to be set.
 */
public abstract class AbstractAdvancedContent extends AbstractBaseContent {

    protected TranslationManagerNew translationManager;

    public AbstractAdvancedContent(AbstractBaseContentManager contentType, String name, Integer id, TranslationManagerNew translationManager) {
        super(contentType, name, id);
        this.translationManager = translationManager;
    }
}
