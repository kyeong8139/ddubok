package com.ddubok.api.card.service;

import com.ddubok.api.card.dto.request.CreateCardReqDto;
import com.ddubok.api.card.dto.request.DeleteCardReq;
import com.ddubok.api.card.dto.request.ReceiveCardReq;

public interface CardService {

    /**
     * 시즌 카드를 생성한다.
     *
     * @param dto 카드를 생성하는데 필요한 정보
     * @return 생성된 카드의 고유 id
     */
    Long createCard(CreateCardReqDto dto);

    /**
     * 카드를 삭제한다.
     *
     * @param dto 카드를 삭제하는데 필요한 정보
     */
    void deleteCard(DeleteCardReq dto);

    /**
     * 카드를 내 앨범에 전달 받는다.
     *
     * @param dto 전달 받을 카드 id
     */
    void receiveCard(ReceiveCardReq dto);

    /**
     * 일반 카드를 생성한다.
     *
     * @param dto 카드를 생성하는데 필요한 정보
     * @return 생성된 카드의 고유 id
     */
    Long createNormalCard(CreateCardReqDto dto);
}
