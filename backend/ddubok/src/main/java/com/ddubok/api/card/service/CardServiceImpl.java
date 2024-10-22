package com.ddubok.api.card.service;

import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.dto.request.ReceiveCardReq;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.exception.AlbumAlreadyDeletedException;
import com.ddubok.api.card.exception.AlbumNotFoundException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final SeasonRepository seasonRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createCard(CreateCardReqDto dto) {
        Card card = cardRepository.save(Card.builder()
            .content(dto.getContent())
            .writerName(dto.getWriterName())
            .season(seasonRepository.findById(dto.getSeasonId()).orElseThrow(
                () -> new SeasonNotFoundException("season not found: " + dto.getSeasonId())))
            .path(dto.getPath())
            .build());
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
        if (!album.getMember().getState().equals(UserState.ACTIVATED.getValue())) {
            throw new MemberNotFoundException("Invalid member state: " + dto.getMemberId());
        }
        album.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveCard(ReceiveCardReq dto) {
        albumRepository.save(Album.builder()
            .card(cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new CardNotFoundException()))
            .member(memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException())).build());
    }
}
