package com.ddubok.api.admin.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidDateOrderException extends CommonException{

    @Override
    public ResponseCode getResponseCode() {return ResponseCode.INVALID_DATE_ORDER;}

    public InvalidDateOrderException() {
    }

    public InvalidDateOrderException(Throwable cause) {
        super(cause);
    }

    public InvalidDateOrderException(String message) {
        super(message);
    }

    public InvalidDateOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateOrderException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
