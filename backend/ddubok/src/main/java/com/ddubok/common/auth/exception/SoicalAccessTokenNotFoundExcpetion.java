package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class SoicalAccessTokenNotFoundExcpetion extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.SOCIAL_ACCESS_TOKEN_NOT_FOUND;
    }

    public SoicalAccessTokenNotFoundExcpetion() {
    }

    public SoicalAccessTokenNotFoundExcpetion(Throwable cause) {
        super(cause);
    }

    public SoicalAccessTokenNotFoundExcpetion(String message) {
        super(message);
    }

    public SoicalAccessTokenNotFoundExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public SoicalAccessTokenNotFoundExcpetion(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
