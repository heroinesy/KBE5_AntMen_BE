package com.antmen.antwork.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationMatchingDetailDto {
    private int customerAge;
    private String customerGender;
    private String customerPhone;
    private String customerEmail;

    private String reservationStatus;
    private String reservationAddress;
    private short reservationDuration;
    List<String> selectedOptions;
    private String reservationMemo;
    private String reservationCancelReason;

    List<MatchingDto> matchingDtoList;

    private String managerName;
    private Integer managerAge;
    private String managerGender;
    private String managerPhone;
    private String managerEmail;
    private String managerProfile;

    List<ReviewResponseDto> reviewResponseDtoList;
}
