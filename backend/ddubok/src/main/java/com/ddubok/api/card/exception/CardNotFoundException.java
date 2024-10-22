package com.ddubok.api.card.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

/**
 * 해당 id의 카드가 존재하지 않을때 발생되는 에러
 */
public class CardNotFoundException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.CARD_NOT_FOUND;
    }

    public CardNotFoundException() {
    }

    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardNotFoundException(Throwable cause) {
        super(cause);
    }

    public CardNotFoundException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
