package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public void createCard(CreateCardReqDto req) {

    }

    @Override
    public void deleteCard(DeleteCardReq req) {

    }
}
