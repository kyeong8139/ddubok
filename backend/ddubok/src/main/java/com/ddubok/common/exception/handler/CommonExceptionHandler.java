package com.ddubok.common.exception.handler;

import com.ddubok.api.admin.exception.InvalidConditionException;
import com.ddubok.api.admin.exception.InvalidDateOrderException;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.card.exception.AlbumAlreadyDeletedException;
import com.ddubok.api.card.exception.AlbumAlreadyExistException;
import com.ddubok.api.card.exception.AlbumNotFoundException;
import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.exception.UnknownRoleException;
import com.ddubok.api.member.exception.UnknownStateException;
import com.ddubok.api.report.exception.InvalidTypeException;
import com.ddubok.api.report.exception.ReportNotFoundException;
import com.ddubok.common.auth.exception.InvalidDeleteMemberException;
import com.ddubok.common.auth.exception.SoicalAccessTokenNotFoundExcpetion;
import com.ddubok.common.auth.exception.UnsupportedOAuth2ProviderException;
import com.ddubok.common.auth.exception.UnsupportedOAuth2UserTypeException;
import com.ddubok.common.exception.CommonException;
import com.ddubok.common.s3.exception.S3Exception;
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
        UnsupportedOAuth2ProviderException.class,
        UnsupportedOAuth2UserTypeException.class,
        MemberNotFoundException.class,
        S3Exception.class,
        AlbumAlreadyDeletedException.class,
        AlbumNotFoundException.class,
        SeasonNotFoundException.class,
        InvalidConditionException.class,
        InvalidTypeException.class,
        ReportNotFoundException.class,
        SoicalAccessTokenNotFoundExcpetion.class,
        InvalidDeleteMemberException.class,
        UnknownRoleException.class,
        UnknownStateException.class,
        InvalidDateOrderException.class,
        AlbumAlreadyExistException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleCommonException(CommonException e) {
        log.info(e.getMessage());
        return BaseResponse.ofFail(e.getResponseCode());
    }
}
