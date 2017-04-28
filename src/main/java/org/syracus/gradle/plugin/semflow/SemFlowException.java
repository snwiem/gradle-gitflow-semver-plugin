package org.syracus.gradle.plugin.semflow;

/**
 * Created by snwiem on 4/28/2017.
 */
public class SemFlowException extends RuntimeException {

    public SemFlowException() {
    }

    public SemFlowException(String message) {
        super(message);
    }

    public SemFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemFlowException(String message, Object... args) {
        this(String.format(message, args));
    }

    public SemFlowException(String message, Throwable cause, Object... args) {
        this(String.format(message, args), cause);
    }

    public SemFlowException(Throwable cause) {
        super(cause);
    }

    public SemFlowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
