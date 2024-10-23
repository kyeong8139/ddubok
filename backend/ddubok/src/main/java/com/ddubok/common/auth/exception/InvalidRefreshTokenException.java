package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidRefreshTokenException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_REFRESH_TOKEN;
    }

    public InvalidRefreshTokenException() {
    }

    public InvalidRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRefreshTokenException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
