package com.ddubok.api.card.repository.custom;

import static com.ddubok.api.card.entity.QCard.card;

import com.ddubok.api.card.entity.State;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public long updateCardStates() {
        long readyUpdatedCount = queryFactory.update(card)
            .set(card.state, State.OPEN)
            .where(isReadyState().and(isOpenedBeforeOrOnToday()))
            .execute();

        long filteredUpdatedCount = queryFactory.update(card)
            .set(card.state, State.FILTERED_OPEN)
            .where(isFilteredState().and(isOpenedBeforeOrOnToday()))
            .execute();

        return readyUpdatedCount + filteredUpdatedCount;
    }

    private BooleanExpression isReadyState() {
        return card.state.eq(State.READY);
    }

    private BooleanExpression isFilteredState() {
        return card.state.eq(State.FILTERED);
    }

    private BooleanExpression isOpenedBeforeOrOnToday() {
        return card.openedAt.loe(LocalDateTime.now());
    }

}
