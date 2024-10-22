package com.ddubok.api.card.service;

import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final SeasonRepository seasonRepository;

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

    @Override
    public void deleteCard(DeleteCardReq dto) {

    }
}
