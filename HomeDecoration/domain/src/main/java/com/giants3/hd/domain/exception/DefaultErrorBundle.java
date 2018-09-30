package com.giants3.hd.domain.exception;

/**
 * Wrapper around Exceptions used to manage default errors.
 */
public class DefaultErrorBundle implements ErrorBundle {


    public static final  String DEFAULT_ERROR_MESSAGE="unknown";
    private final Exception exception;

    public DefaultErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public String getErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }
}
