package com.antmen.antwork.common.api.request.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class MatchingRequestDto {
    private Long addressId;                         // 기본 주소 ID (프론트에서 선택)
    private LocalDate reservationDate;              // 예약 날짜
    private LocalTime reservationTime;              // 예약 시간
    private short reservationDuration;              // 서비스 최종 제공 시간
}
