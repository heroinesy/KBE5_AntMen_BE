package com.antmen.antwork.manager.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCalculationListWithTotalDto {
    private List<ManagerCalculationResponseDto> list;
    private Integer totalAmount;
}
