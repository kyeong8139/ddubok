package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidDeleteMemberException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_REFRESH_TOKEN;
    }

    public InvalidDeleteMemberException() {
    }

    public InvalidDeleteMemberException(Throwable cause) {
        super(cause);
    }

    public InvalidDeleteMemberException(String message) {
        super(message);
    }

    public InvalidDeleteMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDeleteMemberException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
