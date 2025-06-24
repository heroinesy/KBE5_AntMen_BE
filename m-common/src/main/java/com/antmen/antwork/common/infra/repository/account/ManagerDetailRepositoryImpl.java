package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerStatus;
import com.antmen.antwork.common.domain.entity.account.QManagerDetail;
import com.antmen.antwork.common.domain.entity.account.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerDetailRepositoryImpl implements ManagerDetailRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ManagerDetail> findByManagerStatusIsWaitingOrReapply() {
        QManagerDetail managerDetail = QManagerDetail.managerDetail;

        return jpaQueryFactory
                .selectFrom(managerDetail)
                .join(managerDetail.user, QUser.user).fetchJoin()
                .where(managerDetail.managerStatus.in(ManagerStatus.WAITING, ManagerStatus.REAPPLY))
                .orderBy(managerDetail.user.userCreatedAt.asc())
                .fetch();
    }
}
