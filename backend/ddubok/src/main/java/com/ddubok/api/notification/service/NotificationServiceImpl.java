package com.ddubok.api.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToken(Long memberId, String token) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteToken(Long memberId) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCardOpenedPushNotification(String memberId, String title, String body) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCardWrittenPushNotification(String memberId, String title, String body) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCheckAttendancePushNotification(String memberId, String title, String body) {

    }
}
