package com.ddubok.api.notification.service;

import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import com.ddubok.api.notification.entity.NotificationToken;
import com.ddubok.api.notification.repository.NotificationTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final NotificationTokenRepository notificationTokenRepository;

    /**
     * 파이어베이스에 알림을 전송한다.
     *
     * @param messageDto 메세지
     */
    public void sendNotification(NotificationMessageDto messageDto, String path) {
        List<String> tokens = getcreateCardTokens(messageDto.getId());
        if (tokens.isEmpty()) {
            log.warn("No FCM tokens found for member: {}", messageDto.getId());
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(WebpushNotification.builder()
                    .setTitle(messageDto.getTitle())
                    .setBody(messageDto.getBody())
                    .build())
                .setFcmOptions(WebpushFcmOptions.builder()
                    .setLink(path)
                    .build())
                .build())
            .build();

        try {
            firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send web push notification to member: {}",
                messageDto.getId(), e);
        }
    }

    /**
     * 카드 오픈 여부를 파이어베이스에 알림을 전송한다.
     *
     * @param messageDto 메세지
     */
    public void sendCardOpenedNotification(NotificationMessageDto messageDto, String path) {
        List<String> tokens = sendCardOpenedNotification(messageDto.getId());
        if (tokens.isEmpty()) {
            log.warn("No FCM tokens found for card: {}", messageDto.getId());
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(WebpushNotification.builder()
                    .setTitle(messageDto.getTitle())
                    .setBody(messageDto.getBody())
                    .build())
                .setFcmOptions(WebpushFcmOptions.builder()
                    .setLink(path)
                    .build())
                .build())
            .build();

        try {
            firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send web push notification to member: {}",
                messageDto.getId(), e);
        }
    }

    /**
     * 시즌 종료 사실을 파이어베이스에 알림을 전송한다.
     *
     * @param messageDto 메세지
     */
    public void sendSeasonEndedNotification(NotificationMessageDto messageDto, String path) {
        List<String> tokens = sendSeasonEndedNotification(messageDto.getId());
        if (tokens.isEmpty()) {
            log.warn("No FCM tokens found for season: {}", messageDto.getId());
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(WebpushNotification.builder()
                    .setTitle(messageDto.getTitle())
                    .setBody(messageDto.getBody())
                    .build())
                .setFcmOptions(WebpushFcmOptions.builder()
                    .setLink(path)
                    .build())
                .build())
            .build();

        try {
            firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send web push notification to member: {}",
                messageDto.getId(), e);
        }
    }

    /**
     * 시즌 종료 사실을 파이어베이스에 알림을 전송한다.
     *
     * @param messageDto 메세지
     */
    public void sendAttendanceNotification(NotificationMessageDto messageDto, String path) {
        List<String> tokens = getTokensByAttendance();
        if (tokens.isEmpty()) {
            log.warn("No FCM tokens");
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(WebpushNotification.builder()
                    .setTitle(messageDto.getTitle())
                    .setBody(messageDto.getBody())
                    .build())
                .setFcmOptions(WebpushFcmOptions.builder()
                    .setLink(path)
                    .build())
                .build())
            .build();

        try {
            firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send web push notification to member: {}",
                messageDto.getId(), e);
        }
    }

    private List<String> getcreateCardTokens(Long memberId) {
        return notificationTokenRepository.findAllByMemberId(memberId).stream()
            .map(NotificationToken::getToken)
            .toList();
    }

    private List<String> sendCardOpenedNotification(Long cardId) {
        return notificationTokenRepository.findNotificationTokensByCardId(cardId).stream()
            .map(NotificationToken::getToken)
            .toList();
    }

    private List<String> sendSeasonEndedNotification(Long seasonId) {
        return notificationTokenRepository.findNotificationTokensBySeasonId(seasonId).stream()
            .map(NotificationToken::getToken)
            .toList();
    }

    private List<String> getTokensByAttendance() {
        return notificationTokenRepository.findTokensByEnabledNotification();
    }


}
