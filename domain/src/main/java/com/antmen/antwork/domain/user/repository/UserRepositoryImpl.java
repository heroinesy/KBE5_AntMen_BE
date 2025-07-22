package com.antmen.antwork.domain.user.repository;

import com.antmen.antwork.domain.user.entity.User;
import com.antmen.antwork.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<User> searchCustomersWithPaging(String name, String sortBy, Pageable pageable) {
        QUser user = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(user.userRole.eq(UserRole.CUSTOMER));
        whereCondition.and(user.isBlack.eq(false)); // 블랙리스트가 아닌 회원만 조회

        if (name != null && !name.trim().isEmpty()) {
            whereCondition.and(user.userName.containsIgnoreCase(name.trim()));
        }

        // 정렬 조건 설정
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if ("userCreatedDate".equals(sortBy)) {
            orderSpecifiers.add(user.userCreatedAt.desc());
        } else if ("lastReservationDate".equals(sortBy)) {
            orderSpecifiers.add(user.lastReservationAt.desc());
        }
        // 기본 정렬: userId 오름차순
        orderSpecifiers.add(user.userId.asc());

        List<User> content = jpaQueryFactory
                .selectFrom(user)
                .where(whereCondition)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}