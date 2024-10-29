package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.response.CardPreviewRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GetCardServiceImpl implements GetCardService {

    private final AlbumRepository albumRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCardDetailRes getCardDetail(GetCardDetailReq req) {
        Album album = albumRepository.findByCardIdAndMemberId(req.getCardId(), req.getMemberId())
            .orElseThrow(() -> new CardNotFoundException());
        if (album.getIsDeleted()) {
            throw new CardAlreadyDeletedException();
        }
        return GetCardDetailRes.builder()
            .id(album.getCard().getId())
            .content(album.getCard().getContent())
            .openedAt(album.getCard().getOpenedAt())
            .path(album.getCard().getPath())
            .state(album.getCard().getState())
            .writerName(album.getCard().getWriterName())
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCardDetailRes> getCardListBySeason(GetCardListBySeasonReq req) {
        List<Album> albums = albumRepository.findByMemberId(req.getMemberId()).orElse(List.of());
        return albums.stream()
            .filter(album -> album.getCard().getSeason().getId().equals(req.getSeasonId()))
            .map(album -> GetCardDetailRes.builder()
                .id(album.getCard().getId())
                .content(album.getCard().getContent())
                .openedAt(album.getCard().getOpenedAt())
                .path(album.getCard().getPath())
                .state(album.getCard().getState())
                .writerName(album.getCard().getWriterName())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCardDetailRes> getAllCardList(Long memberId) {
        List<Album> albums = albumRepository.findByMemberId(memberId).orElse(List.of());
        return albums.stream()
            .filter(album -> !album.getIsDeleted())
            .map(album -> GetCardDetailRes.builder()
                .id(album.getCard().getId())
                .content(album.getCard().getContent())
                .openedAt(album.getCard().getOpenedAt())
                .path(album.getCard().getPath())
                .state(album.getCard().getState())
                .writerName(album.getCard().getWriterName())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CardPreviewRes getCardPreview(Long memberId) {
        //TODO: member id 유효성 검증
        List<Album> albums = albumRepository.findByMemberId(memberId).orElse(List.of());
        //TODO: 앨범이 비어있을 경우 체크
        String nickname = albums.get(0).getMember().getNickname();
        List<String> cardUrl = albums.stream()
            .filter(album -> !album.getIsDeleted())
            .map(album -> album.getCard().getPath())
            .collect(Collectors.toList());
        return CardPreviewRes.builder()
            .nickname(nickname)
            .cardUrl(cardUrl)
            .build();
    }
}
