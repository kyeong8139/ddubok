package com.ddubok.api.notification.service;

import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    /**
     * 파이어베이스에 알림을 전송한다.
     *
     * @param message 메세지
     */
    public void sendNotification(NotificationMessageDto message) {

    }

}
