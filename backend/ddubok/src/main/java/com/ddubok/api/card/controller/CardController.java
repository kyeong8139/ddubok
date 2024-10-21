package com.ddubok.api.card.controller;

import com.ddubok.api.card.dto.request.CreateCardReq;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.service.CardService;
import com.ddubok.common.s3.S3ImageService;
import com.ddubok.common.s3.dto.FileMetaInfo;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final S3ImageService s3ImageService;
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> createCard(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart CreateCardReq req) {
        cardService.createCard(CreateCardReqDto.builder().receiveMemberId(req.getReceiveMemberId())
            .content(req.getContent()).seasonId(req.getSeasonId())
            .path(uploadCustomPlanetImg(image, req.getSeasonId())).isCustom(true)
            .writerName(req.getWriterName()).build());
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

    /**
     * 카드 이미지를 업로드합니다.
     *
     * @param customPlanetImg 업로드할 카드 이미지 파일입니다.
     * @param seasonId        시즌 id입니다.
     * @return 업로드된 이미지의 URL을 반환합니다.
     */
    private String uploadCustomPlanetImg(MultipartFile customPlanetImg, Long seasonId) {
        FileMetaInfo fileMetaInfo = s3ImageService.uploadCardImg(customPlanetImg, seasonId);
        return fileMetaInfo.getUrl();
    }
}
