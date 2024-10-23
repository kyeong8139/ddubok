package com.ddubok.api.admin.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class SeasonNotFoundException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.SEASON_NOT_FOUND;
    }

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
