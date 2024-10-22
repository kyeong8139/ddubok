package com.ddubok.common.exception.handler;

import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.common.template.response.BaseResponse;
import com.ddubok.common.template.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class SeasonExceptionHandler {

    @ExceptionHandler(SeasonNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleSeasonNotFoundException(
        SeasonNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.ofFail(ResponseCode.SEASON_NOT_FOUND));
    }
}
