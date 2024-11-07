package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.GetAllCardListReq;
import com.ddubok.api.card.dto.request.GetCardDetailReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.dto.response.CardPreviewRes;
import com.ddubok.api.card.dto.response.GetCardDetailRes;
import com.ddubok.api.card.dto.response.GetCardListRes;
import com.ddubok.api.card.dto.response.ReceiveCardPreviewRes;

public interface GetCardService {

    /**
     * 카드의 상세정보를 조회한다.
     *
     * @param req 카드의 상세정보를 조회하기 위한 정보
     * @return 카드의 상세정보를 반환한다.
     */
    GetCardDetailRes getCardDetail(GetCardDetailReq req);

    /**
     * 시즌별 카드 상세 정보를 조회한다.
     *
     * @param req 시즌별 카드를 조회하기 위한 정보
     * @return 시즌별로 조회된 카드의 정보를 반환한다.
     */
    GetCardListRes getCardListBySeason(GetCardListBySeasonReq req);

    /**
     * <p>
     * 보유한 모든 카드 정보를 조회한다.
     * </p>
     * 삭제된 카드는 반환하지 않는다.
     *
     * @param req 보유한 카드를 조회하기 위한 정보
     * @return 보유한 모든 카드들을 반환한다.
     */
    GetCardListRes getAllCardList(GetAllCardListReq req);

    /**
     * 사용자가 보유한 카드 정보와 사용자 정보를 미리보기 형태로 조회한다.
     *
     * @param memberId 조회할 멤버의 고유 id
     * @return 보유한 카드들의 정보를 일부(이미지 링크) 반환한다.
     */
    CardPreviewRes getCardPreview(Long memberId);

    /**
     * 사용자가 카드 받기 링크에 접속 했을때 보여질 정보를 조회한다.
     *
     * @param cardId 조회할 카드의 고유 id
     * @return id에 해당하는 카드의 정보를 일부 반환한다.
     */
    ReceiveCardPreviewRes getCardReceivePreview(Long cardId);
}
