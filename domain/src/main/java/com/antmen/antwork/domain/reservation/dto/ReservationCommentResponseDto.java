package com.antmen.antwork.domain.reservation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCommentResponseDto {
    private Long reservationId;
    private LocalDateTime checkinAt;
    private LocalDateTime checkoutAt;
    private String comment;
}