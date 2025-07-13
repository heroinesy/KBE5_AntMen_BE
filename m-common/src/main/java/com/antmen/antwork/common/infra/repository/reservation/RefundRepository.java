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

    @Query("SELECT SUM(r.payment.payAmount) FROM Refund r WHERE r.refundStatus = :refundStatus")
    Long TotalRefundAmountByStatus(@Param("refundStatus") RefundStatus refundStatus);

    Long countByRefundStatus(RefundStatus status);

    interface RefundReasonStatisticsDto {
        String getReason();
        Long getCount();
    }
    @Query("SELECT r.refundReason AS reason, COUNT(r) AS count " +
            "FROM Refund r " +
            "GROUP BY r.refundReason " +
            "ORDER BY count DESC")
    List<RefundReasonStatisticsDto> CountByRefundReason();

    interface CustomerRefundProjection {
        Long getCustomerId();
        String getCustomerName();
        Long getRefundCount();
        Long getTotalRefundAmount();
    }
    @Query("SELECT res.customer.userId AS customerId, " +
            "res.customer.userName AS customerName, " +
            "COUNT(r) AS refundCount, SUM(p.payAmount) AS totalRefundAmount " +
            "FROM Refund r " +
            "JOIN r.payment p " +
            "JOIN p.reservation res " +
            "WHERE r.refundStatus = :status " +
            "GROUP BY res.customer.userId, res.customer.userName " +
            "ORDER BY totalRefundAmount DESC")
    List<CustomerRefundProjection> getTopApprovedRefundCustomers(@Param("status") RefundStatus status);

    interface ManagerRefundProjection {
        Long getManagerId();
        String getManagerName();
        Long getRefundCount();
        Long getTotalRefundAmount();
    }
    @Query("SELECT res.manager.userId AS managerId, " +
            "res.manager.userName AS managerName, " +
            "COUNT(r) AS refundCount, SUM(p.payAmount) AS totalRefundAmount " +
            "FROM Refund r " +
            "JOIN r.payment p " +
            "JOIN p.reservation res " +
            "WHERE r.refundStatus = :status " +
            "AND res.manager IS NOT NULL " +
            "GROUP BY res.manager.userId, res.manager.userName " +
            "ORDER BY totalRefundAmount DESC")
    List<ManagerRefundProjection> getTopApprovedRefundManagers(@Param("status") RefundStatus status);
}