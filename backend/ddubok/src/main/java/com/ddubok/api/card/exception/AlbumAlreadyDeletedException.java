package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class AlbumAlreadyDeletedException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.ALBUM_ALREADY_DELETED;
    }

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
