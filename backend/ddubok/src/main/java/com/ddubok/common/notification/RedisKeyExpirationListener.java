package com.ddubok.common.notification;

import com.ddubok.api.card.repository.custom.CardRepositoryCustom;
import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisKeyExpirationListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CardRepositoryCustom cardRepositoryCustom;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("card:expiration:")) {
            Long cardId = Long.parseLong(expiredKey.replace("card:expiration:", ""));
            sendCardNotification(cardId);
        }
        if (expiredKey.startsWith("season:expiration:")) {
            Long seasonId = Long.parseLong(expiredKey.replace("season:expiration:", ""));
            sendSeasonNotification(seasonId);
        }
    }

    private void sendCardNotification(Long key) {
        cardRepositoryCustom.updateCardStates();
        NotificationMessageDto message = NotificationMessageDto.builder()
            .id(key)
            .title("🍀행운카드가 열렸어요!🍀")
            .body("뚜복에 접속해 행운카드 속 메세지를 확인해보세요!")
            .build();

        redisTemplate.convertAndSend("open-card", message);
    }

    private void sendSeasonNotification(Long key) {
        NotificationMessageDto message = NotificationMessageDto.builder()
            .id(key)
            .title("☘시즌이 종료되었어요!☘")
            .body("뚜복에 접속해 행운카드 속 메세지를 확인해보세요!")
            .build();

        redisTemplate.convertAndSend("end-season", message);
    }
}
