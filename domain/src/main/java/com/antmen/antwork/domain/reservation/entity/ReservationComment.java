package com.antmen.antwork.domain.reservation.entity;






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
