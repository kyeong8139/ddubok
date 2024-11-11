package com.ddubok.api.card.service;

import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.card.dto.request.GetAllCardListReq;
import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.response.CardPreviewRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.dto.response.GetCardListRes;
import com.ddubok.api.card.dto.response.ReceiveCardPreviewRes;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.entity.State;
import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GetCardServiceImpl implements GetCardService {

    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final SeasonRepository seasonRepository;
    private final CardRepository cardRepository;

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
    public GetCardListRes getCardListBySeason(GetCardListBySeasonReq req) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(),
            Sort.by("id").descending());
        Season findSeason = seasonRepository.findById(req.getSeasonId())
            .orElseThrow(() -> new SeasonNotFoundException());
        Member findmember = memberRepository.findById(req.getMemberId())
            .orElseThrow(() -> new MemberNotFoundException());
        Page<Album> albums = albumRepository.findAllBySeason(findmember, findSeason, pageable);
        return GetCardListRes.builder().cards(getGetCardDetailRes(albums)).hasNext(!albums.isLast())
            .total(albums.getTotalElements())
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCardListRes getAllCardList(GetAllCardListReq req) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(),
            Sort.by("id").descending());
        Member findmember = memberRepository.findById(req.getMemberId())
            .orElseThrow(() -> new MemberNotFoundException());
        Page<Album> albums = albumRepository.findAll(findmember, pageable);
        return GetCardListRes.builder().cards(getGetCardDetailRes(albums)).hasNext(!albums.isLast())
            .total(albums.getTotalElements())
            .build();
    }

    private List<GetCardDetailRes> getGetCardDetailRes(Page<Album> albums) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ReceiveCardPreviewRes getCardReceivePreview(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException());
        return ReceiveCardPreviewRes.builder().id(card.getId()).content(card.getContent())
            .writerName(card.getWriterName()).state(card.getState()).openedAt(card.getOpenedAt())
            .path(card.getPath()).build();
    }
}
