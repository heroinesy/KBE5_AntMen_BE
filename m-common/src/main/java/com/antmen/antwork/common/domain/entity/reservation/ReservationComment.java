package com.antmen.antwork.common.domain.entity.reservation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationComment {

    @Id
    private Long reservationId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private LocalDateTime checkinAt;

    private LocalDateTime checkoutAt;

    private String comment;
}
