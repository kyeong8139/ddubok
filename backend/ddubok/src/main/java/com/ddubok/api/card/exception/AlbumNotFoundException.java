package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class AlbumNotFoundException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.ALBUM_NOT_FOUND;
    }

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
