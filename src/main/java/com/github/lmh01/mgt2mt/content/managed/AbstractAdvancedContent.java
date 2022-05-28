package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

/**
 * Used to create a new content.
 * getExportMap() has a default implementation here. Should be overwritten when a dependent content is created.
 * Advanced content also contains a translation manager that has to be set.
 */
public abstract class AbstractAdvancedContent extends AbstractBaseContent {

    protected final TranslationManager translationManager;

    public AbstractAdvancedContent(AbstractBaseContentManager contentType, String name, Integer id, TranslationManager translationManager) {
        super(contentType, name, id);
        this.translationManager = translationManager;
    }
}
