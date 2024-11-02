package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class AlbumAlreadyExistException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.ALBUM_ALREADY_EXIST;
    }

    public AlbumAlreadyExistException() {
    }

    public AlbumAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public AlbumAlreadyExistException(String message) {
        super(message);
    }

    public AlbumAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
