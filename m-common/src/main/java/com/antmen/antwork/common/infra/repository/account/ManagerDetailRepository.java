package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long>, ManagerDetailRepositoryCustom {
    Optional<ManagerDetail> findByUser(User user);

    ManagerDetail findByUserId(Long userId);
}
