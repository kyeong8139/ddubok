package com.ddubok.api.admin.exception;

public class SeasonNotFoundException extends RuntimeException {

    public SeasonNotFoundException() {
    }

    public SeasonNotFoundException(Throwable cause) {
        super(cause);
    }

    public SeasonNotFoundException(String message) {
        super(message);
    }

    public SeasonNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeasonNotFoundException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
