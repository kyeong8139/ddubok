package com.ddubok.api.notification.controller;

import com.ddubok.api.notification.dto.request.SaveTokenReq;
import com.ddubok.api.notification.service.NotificationService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final AuthUtil authUtil;
    private final NotificationService notificationService;

    @PostMapping
    public BaseResponse<?> saveToken(@RequestBody SaveTokenReq req) {
        Long memberId = authUtil.getMemberId();
        notificationService.saveToken(memberId, req.getToken());
        return BaseResponse.ofSuccess(HttpStatus.CREATED);
    }

    @DeleteMapping
    public BaseResponse<?> deleteToken() {
        Long memberId = authUtil.getMemberId();
        notificationService.deleteToken(memberId);
        return BaseResponse.ofSuccess(HttpStatus.NO_CONTENT);
    }

}
