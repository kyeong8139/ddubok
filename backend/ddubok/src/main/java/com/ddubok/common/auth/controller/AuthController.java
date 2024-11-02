package com.ddubok.common.auth.controller;

import com.ddubok.common.auth.service.AuthService;
import com.ddubok.common.template.response.BaseResponse;
import com.ddubok.common.template.response.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public BaseResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueToken(request, response);
        return BaseResponse.ofSuccess(ResponseCode.CREATED);
    }
}
