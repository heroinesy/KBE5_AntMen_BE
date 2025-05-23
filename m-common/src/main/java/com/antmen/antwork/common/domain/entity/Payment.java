package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long payId; // 결제 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Long reservationId; // 예약 번호

    @Column(nullable = false)
    private String payMethod; // 결제 수단

    @Column(nullable = false)
    private double payAmount; // 결제 금액

    @Column(nullable = false)
    private String payStatus; // 결제 상태

    @Column(nullable = false)
    private LocalDateTime payRequestTime; // 결제를 요청 시간

    @CreatedDate
    private LocalDateTime pay_createdTime; // 결제가 된 시간

    private String payLastTransactionKey; // 마지막 거래 키 값 (환불할 때, 키 값 필요 ex 카드사에게 정보 요청)

//    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private Refund refund;

}
