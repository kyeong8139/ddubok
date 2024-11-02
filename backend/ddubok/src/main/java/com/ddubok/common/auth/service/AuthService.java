package com.ddubok.common.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    /**
     * RefreshToken을 사용하여 AccessToken을 갱신한다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    void reissueToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * RefreshToken의 유무를 판단한다.4
     *
     * @param request HTTP 요청 객체
     */
    void checkRefreshToken(HttpServletRequest request);
}
