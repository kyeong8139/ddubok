package com.ddubok.api.card.service;

import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.dto.request.ReceiveCardReq;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.exception.AlbumAlreadyDeletedException;
import com.ddubok.api.card.exception.AlbumAlreadyExistException;
import com.ddubok.api.card.exception.AlbumNotFoundException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import com.ddubok.common.openai.dto.OpenAiReq;
import com.ddubok.common.openai.dto.OpenAiRes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Transactional
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate openAiTemplate;

    private final RedisTemplate<String, Object> redisTemplate;
    private final CardRepository cardRepository;
    private final SeasonRepository seasonRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createCard(CreateCardReqDto dto) {
        Season season = seasonRepository.findById(dto.getSeasonId()).orElseThrow(
            () -> new SeasonNotFoundException("season not found: " + dto.getSeasonId()));
        Card card = cardRepository.save(Card.builderForSeasonCard()
            .content(dto.getContent())
            .writerName(dto.getWriterName())
            .season(season)
            .path(dto.getPath())
            .build());
        if (dto.getMemberId() != null) {
            NotificationMessageDto message = NotificationMessageDto.builder()
                .id(dto.getMemberId())
                .title("새로운 행운카드가 배송되었어요!")
                .body("뚜복에 접속해 행운카드를 확인해보세요!")
                .data(Map.of())
                .timestamp(LocalDateTime.now())
                .build();

            redisTemplate.convertAndSend("create-card", message);
            saveAlbum(card.getId(), dto.getMemberId());
        }
        if (filteringCheck(dto.getContent())) {
            card.filtering();
        }
        return card.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCard(DeleteCardReq dto) {
        Album album = albumRepository.findByCardIdAndMemberId(dto.getCardId(), dto.getMemberId())
            .orElseThrow(() -> new AlbumNotFoundException(
                "album not found: " + dto.getCardId() + " : " + dto.getMemberId()));
        if (album.getIsDeleted()) {
            throw new AlbumAlreadyDeletedException("album already deleted: " + dto.getCardId());
        }
        if (!album.getMember().getState().equals(UserState.ACTIVATED)) {
            throw new MemberNotFoundException("Invalid member state: " + dto.getMemberId());
        }
        album.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveCard(ReceiveCardReq dto) {
        saveAlbum(dto.getCardId(), dto.getMemberId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createNormalCard(CreateCardReqDto dto) {
        Card card = cardRepository.save(Card.builderForNormalCard()
            .content(dto.getContent())
            .writerName(dto.getWriterName())
            .openedAt(LocalDateTime.now().plusHours(24))
            .path(dto.getPath())
            .build());
        if (dto.getMemberId() != null) {
            saveAlbum(card.getId(), dto.getMemberId());
        }
        if (filteringCheck(dto.getContent())) {
            card.filtering();
        }
        setExpirationForNotification(card);
        return card.getId();
    }

    /**
     * 카드의 내용이 적절한지 부적절한지 판별하는 메서드
     *
     * @param content 카드에 들어가는 편지 내용
     * @return 필터링 결과를 반환
     */
    private Boolean filteringCheck(String content) {
        OpenAiReq request = new OpenAiReq(model, content);
        OpenAiRes openAiRes = openAiTemplate.postForObject(apiURL, request, OpenAiRes.class);
        return openAiRes.getChoices().get(0).getMessage().getContent().equals("DENIED");
    }

    /**
     * 앨범에 카드를 저장하는 메서드
     *
     * @param cardId   카드의 고유 id
     * @param memberId 멤버의 고유 id
     */
    private void saveAlbum(Long cardId, Long memberId) {
        Album album = albumRepository.findByCardIdAndMemberId(cardId, memberId).orElse(null);
        if (album != null) {
            throw new AlbumAlreadyExistException();
        }
        albumRepository.save(Album.builder()
            .card(cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException()))
            .member(memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException())).build());
    }

    /**
     * 24시간 후 만료되는 키를 레디스에 저장하는 메서드
     *
     * @param card 키로 사용할 생성된 카드
     */
    private void setExpirationForNotification(Card card) {
        String redisKey = "card:expiration:" + card.getId();
        redisTemplate.opsForValue().set(redisKey, card.getWriterName(), Duration.ofHours(24));
    }
}
