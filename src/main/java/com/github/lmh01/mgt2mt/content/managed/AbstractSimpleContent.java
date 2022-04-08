package com.github.lmh01.mgt2mt.content.managed;

public abstract class AbstractSimpleContent extends AbstractBaseContent {

    public AbstractSimpleContent(AbstractBaseContentManager contentType, String name, Integer id) {
        super(contentType, name, id);
    }

    /**
     * @return The line representation of this content
     */
    public abstract String getLine();
}
