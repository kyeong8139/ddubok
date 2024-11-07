package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class IsNotExistedResfreshTokenException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.REFRESH_TOKEN_NOT_FOUND;
    }

    public IsNotExistedResfreshTokenException() {
    }

    public IsNotExistedResfreshTokenException(Throwable cause) {
        super(cause);
    }

    public IsNotExistedResfreshTokenException(String message) {
        super(message);
    }

    public IsNotExistedResfreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IsNotExistedResfreshTokenException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
