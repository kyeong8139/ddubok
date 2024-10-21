package com.ddubok.api.card.controller;

import com.ddubok.api.card.dto.request.CreateCardReq;
import com.ddubok.common.template.response.BaseResponse;
import com.ddubok.common.template.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    @PostMapping
    public ResponseEntity<BaseResponse<?>> createCard(CreateCardReq req) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(ResponseCode.CREATED));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<BaseResponse<?>> deleteCard(@PathVariable String cardId) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(ResponseCode.DELETED));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAllCardList() {
        return null;
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<BaseResponse<?>> getCardDetail(@PathVariable String cardId) {
        return null;
    }

    @GetMapping("/{seasonId}")
    public ResponseEntity<BaseResponse<?>> getCardListBySeason(@PathVariable String seasonId) {
        return null;
    }
}
