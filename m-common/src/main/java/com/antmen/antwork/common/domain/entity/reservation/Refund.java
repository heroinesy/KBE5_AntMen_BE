package com.antmen.antwork.common.domain.entity.reservation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "refund")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId; // 결제 번호

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pay_id")
    private Payment payment;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime refundCreatedAt; // 환불 요청일

    @Column(nullable = false)
    private String refundReason; // 환불 사유

    @Column(nullable = false)
    private double refundAmount; // 환불 가능 금액

    @Column(nullable = false)
    private RefundStatus refundStatus; // WAITING: 환불대기, APPROVED: 환불완료, REJECTED: 환불 거절

    private LocalDateTime refundProcessedAt; // 환불 최종 처리일
}
