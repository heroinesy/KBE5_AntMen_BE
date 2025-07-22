package com.antmen.antwork.domain.matching.repository;













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
