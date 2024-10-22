package com.ddubok.api.card.exception;

public class AlbumAlreadyDeletedException extends RuntimeException {

    public AlbumAlreadyDeletedException() {
    }

    public AlbumAlreadyDeletedException(Throwable cause) {
        super(cause);
    }

    public AlbumAlreadyDeletedException(String message) {
        super(message);
    }

    public AlbumAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumAlreadyDeletedException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
