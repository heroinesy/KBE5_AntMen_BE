package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerStatus;
import com.antmen.antwork.common.domain.entity.account.QManagerDetail;
import com.antmen.antwork.common.domain.entity.account.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerDetailRepositoryImpl implements ManagerDetailRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ManagerDetail> findByManagerStatusIsWaitingOrReapplyWithName(String name, Pageable pageable) {
        QManagerDetail managerDetail = QManagerDetail.managerDetail;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(managerDetail.managerStatus.in(ManagerStatus.WAITING, ManagerStatus.REAPPLY));

        if (name != null && !name.trim().isEmpty()) {
            whereCondition.and(managerDetail.user.userName.containsIgnoreCase(name.trim()));
        }

        List<ManagerDetail> contents =  jpaQueryFactory
                                .selectFrom(managerDetail)
                                .join(managerDetail.user, QUser.user).fetchJoin()
                                .where(whereCondition)
                                .orderBy(managerDetail.user.userCreatedAt.asc())
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch();

        long total = jpaQueryFactory
                .select(managerDetail.count())
                .from(managerDetail)
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<ManagerDetail> searchApprovedManagersWithPaging(String name, String sortBy, Pageable pageable) {
        QManagerDetail managerDetail = QManagerDetail.managerDetail;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(managerDetail.managerStatus.eq(ManagerStatus.APPROVED));

        if (name != null && !name.trim().isEmpty()) {
            whereCondition.and(managerDetail.user.userName.containsIgnoreCase(name.trim()));
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if ("userCreatedDate".equals(sortBy)) {
            orderSpecifiers.add(managerDetail.user.userCreatedAt.desc());
        } else if ("lastReservationDate".equals(sortBy)) {
            orderSpecifiers.add(managerDetail.user.lastReservationAt.desc());
        }
        orderSpecifiers.add(managerDetail.user.userId.asc());

        List<ManagerDetail> content = jpaQueryFactory
                .selectFrom(managerDetail)
                .join(managerDetail.user, QUser.user).fetchJoin()
                .where(whereCondition)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(managerDetail.count())
                .from(managerDetail)
                .join(managerDetail.user, QUser.user)
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
