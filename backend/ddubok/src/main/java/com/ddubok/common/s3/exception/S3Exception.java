package com.ddubok.common.s3.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class S3Exception extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.FAIL_TO_UPLOAD_FILE;
    }

    public S3Exception() {
    }

    public S3Exception(String message) {
        super(message);
    }

    public S3Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public S3Exception(Throwable cause) {
        super(cause);
    }

    public S3Exception(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
