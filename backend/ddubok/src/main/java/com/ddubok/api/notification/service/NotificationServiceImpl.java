package com.ddubok.api.notification.service;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import com.ddubok.api.notification.entity.NotificationToken;
import com.ddubok.api.notification.repository.NotificationTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FCMService fcmService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final String CARDBOOK_URL = "https://ddubok.com/book";
    private final String ATTENDANCE_URL = "https://ddubok.com/fortune";
    private final RedisMessageListenerContainer redisMessageListener;
    private final NotificationTokenRepository notificationTokenRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToken(Long memberId, String token) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());
        member.agreeNotification();
        notificationTokenRepository.save(NotificationToken.builder().member(member).token(token).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteToken(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());
        member.disagreeNotification();
        notificationTokenRepository.deleteByMemberId(memberId);
    }

    @PostConstruct
    public void subscribe() {
        redisMessageListener.addMessageListener(
            (message, pattern) -> {
                try {
                    NotificationMessageDto notification = objectMapper.readValue(
                        message.getBody(),
                        NotificationMessageDto.class
                    );
                    fcmService.sendNotification(notification, CARDBOOK_URL);
                } catch (Exception e) {
                    log.error("create card notification error", e);
                }
            },
            new ChannelTopic("create-card")
        );

        redisMessageListener.addMessageListener(
            (message, pattern) -> {
                try {
                    NotificationMessageDto notification = objectMapper.readValue(
                        message.getBody(),
                        NotificationMessageDto.class
                    );
                    fcmService.sendCardOpenedNotification(notification, CARDBOOK_URL);
                } catch (Exception e) {
                    log.error("open card notification error", e);
                }
            },
            new ChannelTopic("open-card")
        );

        redisMessageListener.addMessageListener(
            (message, pattern) -> {
                try {
                    NotificationMessageDto notification = objectMapper.readValue(
                        message.getBody(),
                        NotificationMessageDto.class
                    );
                    fcmService.sendSeasonEndedNotification(notification, CARDBOOK_URL);
                } catch (Exception e) {
                    log.error("open card notification error", e);
                }
            },
            new ChannelTopic("end-season")
        );

        redisMessageListener.addMessageListener(
            (message, pattern) -> {
                try {
                    NotificationMessageDto notification = objectMapper.readValue(
                        message.getBody(),
                        NotificationMessageDto.class
                    );
                    fcmService.sendAttendanceNotification(notification, ATTENDANCE_URL);
                } catch (Exception e) {
                    log.error("attendance check notification error", e);
                }
            },
            new ChannelTopic("attendance-check")
        );
    }
}
