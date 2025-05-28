package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ro_id;                 // 예약 옵션 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;    // 예약 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id", nullable = false)
    private CategoryOption categoryOption; // 카테고리 옵션 번호
}
