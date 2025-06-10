package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByUserIdOrderByIsRead(Long userId);
}
