package com.antmen.antwork.domain.reservation.dto;




@Getter
@Builder
public class ManagerWorkHistoryDto {
    private Long reservationId;
    private String customerName;
    private String serviceName;
    private String workDate;
    private Short rating;
} 