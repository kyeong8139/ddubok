package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class InvalidCardDateException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_CARD_DATE;
    }

    public InvalidCardDateException() {
    }

    public InvalidCardDateException(Throwable cause) {
        super(cause);
    }

    public InvalidCardDateException(String message) {
        super(message);
    }

    public InvalidCardDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCardDateException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
