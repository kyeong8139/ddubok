package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class CardAlreadyDeletedException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.CARD_ALREADY_DELETED;
    }

    public CardAlreadyDeletedException() {
    }

    public CardAlreadyDeletedException(Throwable cause) {
        super(cause);
    }

    public CardAlreadyDeletedException(String message) {
        super(message);
    }

    public CardAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardAlreadyDeletedException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
