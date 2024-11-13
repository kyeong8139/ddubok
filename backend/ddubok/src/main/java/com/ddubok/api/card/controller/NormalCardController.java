package com.ddubok.api.card.controller;

import com.ddubok.api.card.dto.request.CreateCardReq;
import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.response.CardIdRes;
import com.ddubok.api.card.service.CardService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.s3.S3ImageService;
import com.ddubok.common.s3.dto.FileMetaInfo;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/cards")
public class NormalCardController {

    private final AuthUtil authUtil;

    private final S3ImageService s3ImageService;
    private final CardService cardService;

    @PostMapping
    public BaseResponse<?> createNormalCard(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart(name = "req") CreateCardReq req) {
        Long cardId = cardService.createNormalCard(
            CreateCardReqDto.builder().content(req.getContent())
                .path(uploadCardImg(image, req.getWriterName())).isCustom(true)
                .writerName(req.getWriterName()).memberId(req.getMemberId()).build());
        return BaseResponse.ofSuccess(CardIdRes.builder().cardId(cardId).build());
    }

    /**
     * 카드 이미지를 업로드합니다.
     *
     * @param cardImg  업로드할 카드 이미지 파일입니다.
     * @return 업로드된 이미지의 URL을 반환합니다.
     */
    private String uploadCardImg(MultipartFile cardImg, String writerName) {
        FileMetaInfo fileMetaInfo = s3ImageService.uploadNormalCardImg(cardImg, writerName);
        return fileMetaInfo.getUrl();
    }
}
