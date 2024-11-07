package com.ddubok.common.exception.handler;

import com.ddubok.common.auth.exception.InvalidRefreshTokenException;
import com.ddubok.common.exception.CommonException;
import com.ddubok.common.template.response.BaseResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidRefreshTokenExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<?> handleInvalidRefreshToken(InvalidRefreshTokenException e, HttpServletResponse response) {
        String refreshTokenCookieName = "refresh";

        // 여기서 쿠키 설정
        Cookie cookie = new Cookie(refreshTokenCookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return BaseResponse.ofFail(e.getResponseCode());
    }

}
