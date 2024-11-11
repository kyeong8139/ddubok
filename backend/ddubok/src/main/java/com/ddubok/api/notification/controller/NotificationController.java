package com.ddubok.api.notification.controller;

import com.ddubok.api.notification.dto.request.SaveTokenReq;
import com.ddubok.api.notification.service.NotificationService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final AuthUtil authUtil;
    private final NotificationService notificationService;

    public BaseResponse<?> saveToken(@RequestBody SaveTokenReq req) {
        return null;
    }

    public BaseResponse<?> deleteToken() {
        return null;
    }

}
