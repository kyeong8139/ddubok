package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class UnsupportedOAuth2UserTypeException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.USER_TYPE_UNSUPPORTED;
    }

    public UnsupportedOAuth2UserTypeException() {
    }

    public UnsupportedOAuth2UserTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedOAuth2UserTypeException(String message) {
        super(message);
    }

    public UnsupportedOAuth2UserTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOAuth2UserTypeException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
