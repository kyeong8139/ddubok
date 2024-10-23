package com.ddubok.common.exception.handler;

import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.common.auth.exception.InvalidRefreshTokenException;
import com.ddubok.common.auth.exception.RefreshTokenExpiredException;
import com.ddubok.common.auth.exception.UnsupportedOAuth2ProviderException;
import com.ddubok.common.auth.exception.UnsupportedOAuth2UserTypeException;
import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({
        CardNotFoundException.class,
        CardAlreadyDeletedException.class,
        RefreshTokenExpiredException.class,
        InvalidRefreshTokenException.class,
        UnsupportedOAuth2ProviderException.class,
        UnsupportedOAuth2UserTypeException.class,
        MemberNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleCommonException(CommonException e) {
        log.info(e.getMessage());
        return BaseResponse.ofFail(e.getResponseCode());
    }
}
