package com.antmen.antwork.domain.calculation.dto;








@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCalculationListWithTotalDto {
    private List<ManagerCalculationResponseDto> list;
    private Integer totalAmount;
}
