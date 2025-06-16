package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReservationHistoryDto {
    private Long reservationId;
    private String categoryName;
    private String reservationStatus;
    private String reservationDate;
    private short totalDuration;
    private int totalAmount;

    private String reservationMemo;
    private UserSummaryDto customer;
    private UserSummaryDto manager;
    private String address;

    private List<String> selectedOptions;
    private List<MatchingDto> matchings;
}