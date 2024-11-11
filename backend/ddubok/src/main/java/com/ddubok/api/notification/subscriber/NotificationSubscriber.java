package com.ddubok.api.notification.subscriber;

import com.ddubok.api.notification.service.FCMService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSubscriber {

    private final FCMService fcmService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void subscribe() {

    }
}
