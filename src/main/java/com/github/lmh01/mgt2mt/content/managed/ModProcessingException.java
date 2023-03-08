package com.github.lmh01.mgt2mt.content.managed;

/**
 * Thrown to indicate that something went wrong while a mod was being processed
 */
public class ModProcessingException extends Exception {
    private final String message;
    private final Exception cause;
    private final String name;

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     *
     * @param s the detail message.
     */
    public ModProcessingException(String s) {
        message = s;
        cause = null;
        name = null;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     *
     * @param s The detail message
     * @param e What caused this exception
     */
    public ModProcessingException(String s, Exception e) {
        message = s;
        cause = e;
        name = null;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     *
     * @param s The detail message
     * @param e What caused this exception
     * @param name The name of the content that caused this exception
     */
    public ModProcessingException(String s, Exception e, String name) {
        message = s;
        cause = e;
        this.name = name;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     *
     * @param s The detail message
     * @param name The name of the content that caused this exception
     */
    public ModProcessingException(String s, String name) {
        message = s;
        cause = null;
        this.name = name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Exception getCause() {
        return cause;
    }

    /**
     * @return If set: the name of the content that caused this exception.
     */
    public String getName() {
        return name;
    }
}