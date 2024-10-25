package com.ddubok.api.member.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class UnknownStateException extends CommonException {

    @Override
    public ResponseCode getResponseCode() { return ResponseCode.UNKNOWN_STATE;}

    public UnknownStateException() {
    }

    public UnknownStateException(Throwable cause) {
        super(cause);
    }

    public UnknownStateException(String message) {
        super(message);
    }

    public UnknownStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownStateException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
