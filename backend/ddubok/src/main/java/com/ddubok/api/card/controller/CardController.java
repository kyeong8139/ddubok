package com.ddubok.api.card.controller;

import com.ddubok.api.card.dto.request.CreateCardReq;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.dto.request.GetAllCardListReq;
import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.request.ReceiveCardReq;
import com.ddubok.api.card.dto.response.CardIdRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.dto.response.GetCardListRes;
import com.ddubok.api.card.service.CardService;
import com.ddubok.api.card.service.GetCardService;
import com.ddubok.common.auth.util.AuthUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final AuthUtil authUtil;

    private final S3ImageService s3ImageService;
    private final CardService cardService;
    private final GetCardService getCardService;

    @PostMapping
    public BaseResponse<?> createCard(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart(name = "req") CreateCardReq req) {
        Long cardId = cardService.createCard(
            CreateCardReqDto.builder().content(req.getContent()).seasonId(req.getSeasonId())
                .path(uploadCardImg(image, req.getSeasonId())).isCustom(true)
                .writerName(req.getWriterName()).memberId(req.getMemberId()).build());
        return BaseResponse.ofSuccess(CardIdRes.builder().cardId(cardId).build());
    }

    @DeleteMapping("/{cardId}")
    public BaseResponse<?> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(
            DeleteCardReq.builder().memberId(authUtil.getMemberId()).cardId(cardId).build());
        return BaseResponse.ofSuccess(ResponseCode.DELETED);
    }

    @PostMapping("/{cardId}")
    public BaseResponse<?> receiveCard(@PathVariable Long cardId) {
        cardService.receiveCard(
            ReceiveCardReq.builder().cardId(cardId).memberId(authUtil.getMemberId()).build());
        return BaseResponse.ofSuccess(ResponseCode.OK);
    }

    @GetMapping
    public BaseResponse<?> getAllCardList(
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size
    ) {
        GetCardListRes res = getCardService.getAllCardList(
            GetAllCardListReq.builder().memberId(authUtil.getMemberId())
                .page(page).size(size)
                .build());
        return BaseResponse.ofSuccess(res);
    }

    @GetMapping("/albums/{cardId}/detail")
    public BaseResponse<?> getCardDetail(@PathVariable Long cardId) {
        return BaseResponse.ofSuccess(getCardService.getCardDetail(
            GetCardDetailReq.builder().cardId(cardId).memberId(authUtil.getMemberId()).build()));
    }

    @GetMapping("/albums/{seasonId}")
    public BaseResponse<?> getCardListBySeason(@PathVariable Long seasonId,
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size) {
        GetCardListRes res = getCardService.getCardListBySeason(
            GetCardListBySeasonReq.builder().seasonId(seasonId).memberId(authUtil.getMemberId())
                .page(page).size(size)
                .build());
        return BaseResponse.ofSuccess(res);
    }

    @GetMapping("/{memberId}/preview")
    public BaseResponse<?> getCardPreview(@PathVariable Long memberId) {
        return BaseResponse.ofSuccess(getCardService.getCardPreview(memberId));
    }

    @GetMapping("/receive/{cardId}")
    public BaseResponse<?> getCardReceivePreview(@PathVariable Long cardId) {
        return BaseResponse.ofSuccess(getCardService.getCardReceivePreview(cardId));
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
