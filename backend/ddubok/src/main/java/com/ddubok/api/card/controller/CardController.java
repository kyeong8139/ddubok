package com.ddubok.api.card.controller;

import com.ddubok.api.card.dto.request.CreateCardReq;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.request.ReceiveCardReq;
import com.ddubok.api.card.dto.response.CardIdRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.service.CardService;
import com.ddubok.api.card.service.GetCardService;
import com.ddubok.common.s3.S3ImageService;
import com.ddubok.common.s3.dto.FileMetaInfo;
import com.ddubok.common.template.response.BaseResponse;
import com.ddubok.common.template.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final GetCardService getCardService;

    @PostMapping
    public BaseResponse<?> createCard(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart CreateCardReq req) {
        Long cardId = cardService.createCard(
            CreateCardReqDto.builder().content(req.getContent()).seasonId(req.getSeasonId())
                .path(uploadCardImg(image, req.getSeasonId())).isCustom(true)
                .writerName(req.getWriterName()).build());
        return BaseResponse.ofSuccess(CardIdRes.builder().cardId(cardId).build());
    }

    @DeleteMapping("/{cardId}")
    public BaseResponse<?> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(DeleteCardReq.builder().memberId(1l).cardId(cardId).build());
        return BaseResponse.ofSuccess(ResponseCode.DELETED);
    }

    @PostMapping("/{cardId}")
    public BaseResponse<?> receiveCard(@PathVariable Long cardId) {
        cardService.receiveCard(ReceiveCardReq.builder().cardId(cardId).memberId(1l).build());
        return BaseResponse.ofSuccess(ResponseCode.OK);
    }

    @GetMapping
    public BaseResponse<?> getAllCardList() {
        List<GetCardDetailRes> res = getCardService.getAllCardList(1l);
        if (res.size() == 0) {
            return BaseResponse.ofSuccess(ResponseCode.NO_ALBUM);
        }
        return BaseResponse.ofSuccess(res);
    }

    @GetMapping("/{cardId}")
    public BaseResponse<?> getCardDetail(@PathVariable Long cardId) {
        return BaseResponse.ofSuccess(getCardService.getCardDetail(
            GetCardDetailReq.builder().cardId(cardId).memberId(1l).build()));
    }

    @GetMapping("/albums/{seasonId}")
    public BaseResponse<?> getCardListBySeason(@PathVariable Long seasonId) {
        return BaseResponse.ofSuccess(getCardService.getCardListBySeason(
            GetCardListBySeasonReq.builder().seasonId(seasonId).memberId(1l).build()));
    }

    /**
     * 카드 이미지를 업로드합니다.
     *
     * @param cardImg  업로드할 카드 이미지 파일입니다.
     * @param seasonId 시즌 id입니다.
     * @return 업로드된 이미지의 URL을 반환합니다.
     */
    private String uploadCardImg(MultipartFile cardImg, Long seasonId) {
        FileMetaInfo fileMetaInfo = s3ImageService.uploadCardImg(cardImg, seasonId);
        return fileMetaInfo.getUrl();
    }
}
