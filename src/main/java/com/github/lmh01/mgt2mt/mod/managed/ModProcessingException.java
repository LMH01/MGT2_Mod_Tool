package com.github.lmh01.mgt2mt.mod.managed;

/**
 * Thrown to indicate that something went wrong while a mod was being processed
 */
public class ModProcessingException extends Exception {

    private final String message;
    private final boolean internal;

    /**
     * Constructs an ModProcessingException with the specified detail message.
     * @param s the detail message.
     */
    public ModProcessingException(String s) {
        message = s;
        internal = false;
    }

    /**
     * Constructs an ModProcessingException with the specified detail message.
     * @param s the detail message.
     * @param internalIssue If true the message will indicate that the issue is internal and not caused by faulty files
     */
    public ModProcessingException(String s, boolean internalIssue) {
        message = s;
        internal = internalIssue;
    }

    @Override
    public String getMessage() {
        StringBuilder s = new StringBuilder();
        s.append(message);
        if (internal) {
            s.append(" - This error is caused by faulty code! Please contact the developer so this problem can be fixed!");
        } else {
            s.append(" - This error is likely caused by corrupted files!");
        }
        return s.toString();
    }
}
