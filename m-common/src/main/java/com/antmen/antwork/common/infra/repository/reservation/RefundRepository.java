package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Refund;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByRefundStatus(RefundStatus refundStatus);
    boolean existsByPayment_PayId(Long payId);

    @Query("SELECT SUM(r.payment.payAmount) FROM Refund r")
    Long TotalRefundAmount();

    @Query("SELECT SUM(r.payment.payAmount) FROM Refund r WHERE r.refundStatus = :refundStatus")
    Long TotalRefundAmountByStatus(@Param("refundStatus") RefundStatus refundStatus);

    Long countByRefundStatus(RefundStatus status);

    @Query("SELECT r.refundReason AS reason, COUNT(r) AS count " +
            "FROM Refund r " +
            "GROUP BY r.refundReason " +
            "ORDER BY count DESC")
    List<RefundReasonStatisticsDto> CountByRefundReason();
    interface RefundReasonStatisticsDto {
        String getReason();
        Long getCount();
    }
}