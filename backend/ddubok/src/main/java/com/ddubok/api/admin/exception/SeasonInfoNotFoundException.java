package com.ddubok.api.admin.exception;

import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.ResponseCode;

public class SeasonInfoNotFoundException extends CommonException {

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.SEASON_INFO_NOT_FOUND;
    }
}
