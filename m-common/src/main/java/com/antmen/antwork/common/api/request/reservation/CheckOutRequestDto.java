package com.antmen.antwork.common.api.request.reservation;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckOutRequestDto {
    private LocalDateTime checkoutAt;
    private String comment;
}
