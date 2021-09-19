package com.github.lmh01.mgt2mt.mod.managed;

/**
 * Thrown to indicate that something went wrong while a mod was being processed
 */
public class ModProcessingException extends Exception {
    private final String message;
    private final Exception cause;

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     *
     * @param s the detail message.
     */
    public ModProcessingException(String s) {
        message = s;
        cause = null;
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
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Exception getCause() {
        return cause;
    }
}