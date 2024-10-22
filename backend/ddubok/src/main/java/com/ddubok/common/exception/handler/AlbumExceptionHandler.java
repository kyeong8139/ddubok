package com.ddubok.common.exception.handler;

import com.ddubok.api.card.exception.AlbumAlreadyDeletedException;
import com.ddubok.api.card.exception.AlbumNotFoundException;
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
public class AlbumExceptionHandler {

    @ExceptionHandler(AlbumAlreadyDeletedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAlbumAlreadyDeletedException(
        AlbumAlreadyDeletedException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.ofFail(ResponseCode.ALBUM_ALREADY_DELETED));
    }

    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleAlbumNotFoundException(
        AlbumNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.ofFail(ResponseCode.ALBUM_NOT_FOUND));
    }
}
