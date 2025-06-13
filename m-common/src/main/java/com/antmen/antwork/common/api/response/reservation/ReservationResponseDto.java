package com.antmen.antwork.common.api.response.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReservationResponseDto {
    private Long reservationId;             // 예약번호
    private Long customerId;                // 수요자 아이디
    private LocalDateTime reservationCreatedAt; // 신청 날짜
    private LocalDate reservationDate;         // 예약 날짜
    private LocalTime reservationTime;           // 예약 시간
    private Long categoryId;                // 카테고리 번호
    private String categoryName;            // 카테코리 이름

    private short recommendDuration;        // 추천 제공 시간
    private short reservationDuration;      // 서비스 제공 시간

    private Long managerId;                 // 매니저 아이디
    private String managerName;             // 매니저 이름
    private LocalDateTime matchedAt;        // 매니저 수락 시간

    private String reservationStatus;       // 예약 상태
    private String reservationCancelReason; // 예약 취소 사유
    private String reservationMemo;         // 추가 요청 사항
    private Integer reservationAmount;      // 최종 가격

    private List<Long> optionIds;           // 선택한 옵션 아이디
    private List<String> optionNames;       // 선택한 옵션명
}
