package com.ddubok.api.notification.service;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.notification.entity.NotificationToken;
import com.ddubok.api.notification.repository.NotificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final NotificationTokenRepository notificationTokenRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToken(Long memberId, String token) {
        Member member = memberRepository.findById(memberId).orElse(null);
        member.agreeNotification();
        notificationTokenRepository.save(NotificationToken.builder().member(member).token(token).build());
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
