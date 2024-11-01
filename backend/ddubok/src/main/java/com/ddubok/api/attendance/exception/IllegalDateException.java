package com.ddubok.api.attendance.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class IllegalDateException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return null;
    }

    public IllegalDateException() {
    }

    public IllegalDateException(Throwable cause) {
        super(cause);
    }

    public IllegalDateException(String message) {
        super(message);
    }

    public IllegalDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDateException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
