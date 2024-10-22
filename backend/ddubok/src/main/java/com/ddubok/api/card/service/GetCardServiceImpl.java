package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.card.exception.CardAlreadyDeletedException;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.AlbumRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GetCardServiceImpl implements GetCardService {

    private final AlbumRepository albumRepository;

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

    @Override
    public List<GetCardDetailRes> getCardListBySeason(GetCardListBySeasonReq req) {
        return List.of();
    }

    @Override
    public List<GetCardDetailRes> getAllCardList(Integer memberId) {
        return List.of();
    }
}
