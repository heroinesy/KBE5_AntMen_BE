package com.antmen.antwork.domain.reservation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManagerWorkHistoryDto {
    private Long reservationId;
    private String customerName;
    private String serviceName;
    private String workDate;
    private Short rating;
} 