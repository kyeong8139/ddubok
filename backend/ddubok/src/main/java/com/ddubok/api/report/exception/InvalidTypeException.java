package com.ddubok.api.report.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidTypeException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_TYPE;
    }

    public InvalidTypeException() {
    }

    public InvalidTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTypeException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
