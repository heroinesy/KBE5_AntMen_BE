package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_comment")
public class ReservationComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id;

    private LocalDateTime checkinAt;

    private LocalDateTime checkoutAt;

    private String comment;

}
