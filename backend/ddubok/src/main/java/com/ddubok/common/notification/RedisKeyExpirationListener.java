package com.ddubok.common.notification;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("card:expiration:")) {
            Long cardId = Long.parseLong(expiredKey.replace("card:expiration:", ""));
            sendNotification(cardId);
        }
    }

    private void sendNotification(Long key) {
        // TODO: Firebase를 통해 사용자에게 알림을 보내는 로직을 구현
    }
}
