package com.antmen.antwork.common.api.response.calculation;

import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationListWithTotalDto {
    private List<CalculationResponseDto> list;
    private Integer totalAmount;
}
