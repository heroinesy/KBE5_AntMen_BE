package com.antmen.antwork.common.api.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ManagerWorkHistoryDto {
    private Long reservationId;
    private String customerName;
    private String serviceName;
    private String workDate;
    private Short rating;
} 