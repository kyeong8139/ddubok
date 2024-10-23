package com.ddubok.api.admin.exception;

public class InvalidConditionException extends RuntimeException {

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
