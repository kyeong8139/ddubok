package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.GetAllCardListReq;
import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.response.CardPreviewRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.entity.State;
import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import com.ddubok.api.card.repository.custom.AlbumRepositoryCustom;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    private final AlbumRepositoryCustom albumRepositoryCustom;

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
        if (!album.getIsRead() && album.getCard().getState() == State.OPEN) {
            album.read();
        }
        return GetCardDetailRes.builder()
            .id(album.getCard().getId())
            .content(album.getCard().getContent())
            .openedAt(album.getCard().getOpenedAt())
            .path(album.getCard().getPath())
            .state(album.getCard().getState())
            .writerName(album.getCard().getWriterName())
            .isRead(album.getIsRead())
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCardDetailRes> getCardListBySeason(GetCardListBySeasonReq req) {
        List<Album> albums = albumRepositoryCustom.getAllCardBySeason(req);
        return albums.stream()
            .filter(album -> !album.getIsDeleted())
            .map(album -> GetCardDetailRes.builder()
                .id(album.getCard().getId())
                .content(album.getCard().getContent())
                .openedAt(album.getCard().getOpenedAt())
                .path(album.getCard().getPath())
                .state(album.getCard().getState())
                .writerName(album.getCard().getWriterName())
                .isRead(album.getIsRead())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCardDetailRes> getAllCardList(GetAllCardListReq req) {
        List<Album> albums = albumRepositoryCustom.getAllCard(req);
        return albums.stream()
            .filter(album -> !album.getIsDeleted())
            .map(album -> GetCardDetailRes.builder()
                .id(album.getCard().getId())
                .content(album.getCard().getContent())
                .openedAt(album.getCard().getOpenedAt())
                .path(album.getCard().getPath())
                .state(album.getCard().getState())
                .writerName(album.getCard().getWriterName())
                .isRead(album.getIsRead())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CardPreviewRes getCardPreview(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());
        List<Album> albums = albumRepository.findByMemberId(memberId).orElse(null);
        String nickname = member.getNickname();
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
