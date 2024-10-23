package com.ddubok.api.admin.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidConditionException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_CONDITION;
    }

    public InvalidConditionException() {
    }

    public InvalidConditionException(String message) {
        super(message);
    }

    public InvalidConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConditionException(Throwable cause) {
        super(cause);
    }

    public InvalidConditionException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
