package com.ddubok.common.exception;

import com.ddubok.common.template.response.ResponseCode;

public abstract class CommonException extends RuntimeException {

    /**
     * Exception에 해당하는 ResponseCode를 반환하도록 상속받는 클래스에서 반드시 구현하여야 함.
     *
     * @return ResponseCode
     */
    public abstract ResponseCode getResponseCode();

    public CommonException() {
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
