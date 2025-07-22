package com.antmen.antwork.domain.alert.repository;

import com.antmen.antwork.domain.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByAlertUserIdOrderByIsRead(Long userId);

    List<Alert> findAllByAlertUserIdAndIsReadFalse(Long userId);

    Optional<Alert> findByAlertIdAndAlertUserId(Long alertId, Long userId);

    List<Alert> findAllByAlertUserIdOrderByCreatedAtDesc(Long userId);

}
