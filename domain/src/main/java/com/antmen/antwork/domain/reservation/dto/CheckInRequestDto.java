package com.antmen.antwork.domain.reservation.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInRequestDto {
    private LocalDateTime checkinAt;
}
