package com.ddubok.api.card.repository.custom;

import static com.ddubok.api.card.entity.QAlbum.album;
import static com.ddubok.api.card.entity.QCard.card;

import com.ddubok.api.card.dto.request.GetAllCardListReq;
import com.ddubok.api.card.dto.request.GetCardListBySeasonReq;
import com.ddubok.api.card.entity.Album;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AlbumRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Album> getAllCard(GetAllCardListReq req) {
        return queryFactory.selectFrom(album)
            .where(
                ltCardId(req.getLastCardId()),
                eqMemberId(req.getMemberId()))
            .orderBy(card.id.desc())
            .limit(req.getSize() + 1)
            .fetch();
    }

    public List<Album> getAllCardBySeason(GetCardListBySeasonReq req) {
        return queryFactory.selectFrom(album)
            .where(
                ltCardId(req.getLastCardId()),
                eqMemberId(req.getMemberId()),
                eqSeasonId(req.getSeasonId()))
            .orderBy(card.id.desc())
            .limit(req.getSize() + 1)
            .fetch();
    }

    private BooleanExpression ltCardId(Long lastCardId) {
        return lastCardId != null ? album.card.id.lt(lastCardId) : null;
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return album.member.id.eq(memberId);
    }

    private BooleanExpression eqSeasonId(Long seasonId) {
        return album.card.season.id.eq(seasonId);
    }
}
