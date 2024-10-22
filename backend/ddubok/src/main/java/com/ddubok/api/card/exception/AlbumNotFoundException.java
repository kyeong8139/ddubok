package com.ddubok.api.card.exception;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException() {
    }

    public AlbumNotFoundException(Throwable cause) {
        super(cause);
    }

    public AlbumNotFoundException(String message) {
        super(message);
    }

    public AlbumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumNotFoundException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
