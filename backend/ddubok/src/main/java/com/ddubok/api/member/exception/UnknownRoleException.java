package com.ddubok.api.member.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class UnknownRoleException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {return ResponseCode.UNKNOWN_ROLE;}

    public UnknownRoleException() {
    }

    public UnknownRoleException(Throwable cause) {
        super(cause);
    }

    public UnknownRoleException(String message) {
        super(message);
    }

    public UnknownRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownRoleException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
