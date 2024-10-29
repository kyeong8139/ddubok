package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class RefreshTokenExpiredException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.RT_EXPIRED;
    }

    public RefreshTokenExpiredException() {
    }

    public RefreshTokenExpiredException(Throwable cause) {
        super(cause);
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenExpiredException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
