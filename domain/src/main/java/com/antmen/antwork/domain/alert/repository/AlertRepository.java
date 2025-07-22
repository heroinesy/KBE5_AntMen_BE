package com.antmen.antwork.domain.alert.repository;








@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByAlertUserIdOrderByIsRead(Long userId);

    List<Alert> findAllByAlertUserIdAndIsReadFalse(Long userId);

    Optional<Alert> findByAlertIdAndAlertUserId(Long alertId, Long userId);

    List<Alert> findAllByAlertUserIdOrderByCreatedAtDesc(Long userId);

}
