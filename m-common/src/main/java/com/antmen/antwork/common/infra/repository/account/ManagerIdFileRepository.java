package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerIdFileRepository extends JpaRepository<ManagerIdFile, Long> {
    List<ManagerIdFile> findAllByUser(User user);

    List<ManagerIdFile> findAllByUser_UserId(Long userId);
}
