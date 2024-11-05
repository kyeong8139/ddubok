package com.ddubok.common.exception.handler;

import com.ddubok.common.auth.exception.IsNotExistedResfreshTokenException;
import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RefreshTokenExceptionHandler {

    @ExceptionHandler({
        IsNotExistedResfreshTokenException.class
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<?> handleCommonException(CommonException e) {
        return BaseResponse.ofSuccess(e.getResponseCode());
    }
}
