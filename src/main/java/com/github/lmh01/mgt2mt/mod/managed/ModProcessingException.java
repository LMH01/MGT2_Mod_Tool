package com.github.lmh01.mgt2mt.mod.managed;

/**
 * Thrown to indicate that something went wrong while a mod was being processed
 */
public class ModProcessingException extends Exception {
    //TODO code überschauen, ob irgendwo die exception nicht mit übergeben wurde
    //  testen, wie die exception aussieht, wenn sie ausgelöst wird. Auch schauen, ob getCause irgendwie benutzt wird
    //  ggf. noch in AbstractBaseMod bei der startModThread() Funktion schauen, ob ich da getCause noch irgendwie einbauen kann

    private final String message;
    private final boolean internal;
    private final Exception cause;

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     * @param s the detail message.
     */
    public ModProcessingException(String s) {
        message = s;
        internal = false;
        cause = null;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     * @param s The detail message
     * @param e What caused this exception
     */
    public ModProcessingException(String s, Exception e) {
        message = s;
        internal = false;
        cause = e;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     * @param s The detail message
     * @param internalIssue If true the message will indicate that the issue is internal and not caused by faulty files
     */
    public ModProcessingException(String s, boolean internalIssue) {
        message = s;
        internal = internalIssue;
        cause = null;
    }

    /**
     * Constructs a new ModProcessingException with the specified detail message.
     * @param s The detail message
     * @param e What caused this exception
     * @param internalIssue If true the message will indicate that the issue is internal and not caused by faulty files
     */
    public ModProcessingException(String s, Exception e, boolean internalIssue) {
        message = s;
        internal = internalIssue;
        cause = e;
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

    @Override
    public Exception getCause() {
        return cause;
    }
}
