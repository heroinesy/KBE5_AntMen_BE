package com.antmen.antwork.common.api.request;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationOptionRequestDto {
    private List<Long> categoryOptionIds;
}
