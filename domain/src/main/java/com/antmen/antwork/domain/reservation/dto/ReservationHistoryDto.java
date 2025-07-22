package com.antmen.antwork.domain.reservation.dto;









@Getter
@Builder
@AllArgsConstructor
public class ReservationHistoryDto {
    private Long reservationId;
    private String categoryName;
    private String reservationStatus;
    private String reservationDate;
    private String reservationTime;

    private short totalDuration;
    private int totalAmount;

    private String reservationMemo;
    private String reservationCancelReason;
    private UserSummaryDto customer;
    private UserSummaryDto manager;
    private String address;

    private List<String> selectedOptions;
    private List<MatchingDto> matchings;
}