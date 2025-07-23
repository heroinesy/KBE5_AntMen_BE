package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.QMatching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class MatchingRepositoryCustomImpl implements MatchingRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Reservation> findMatchingReservationByManagerId(Long userId, Pageable pageable) {
        QMatching qMatching = QMatching.matching;

        BooleanExpression condition = qMatching.manager.userId.eq(userId)
                .and(qMatching.matchingIsRequest.isTrue())
                .and(qMatching.matchingManagerIsAccept.isNull())
                .and(qMatching.reservation.reservationStatus.eq(ReservationStatus.WAITING));

        List<Reservation> reservations = jpaQueryFactory
                .select(qMatching.reservation)
                .from(qMatching)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(qMatching.count())
                .from(qMatching)
                .where(condition)
                .fetchOne();

        return new PageImpl<Reservation>(reservations, pageable, total);
    }
}
