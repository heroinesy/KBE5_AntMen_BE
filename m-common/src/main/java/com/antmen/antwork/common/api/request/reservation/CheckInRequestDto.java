package com.antmen.antwork.common.api.request.reservation;

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
