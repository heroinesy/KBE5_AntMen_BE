package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerIdFileRepository extends JpaRepository<ManagerIdFile, Long> {
    List<ManagerIdFile> findAllByUser(User user);
}
