package com.ddubok.common.auth.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class UnsupportedOAuth2ProviderException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.PROVIDER_UNSUPPORTED;
    }

    public UnsupportedOAuth2ProviderException() {
    }

    public UnsupportedOAuth2ProviderException(Throwable cause) {
        super(cause);
    }

    public UnsupportedOAuth2ProviderException(String message) {
        super(message);
    }

    public UnsupportedOAuth2ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOAuth2ProviderException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
