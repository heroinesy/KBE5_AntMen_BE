package com.antmen.antwork.common.api.response;

import com.antmen.antwork.common.domain.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.antmen.antwork.common.domain.entity.Category;

@Getter
@Setter
@Builder
public class ReservationResponseDto {
    private Long reservationId;                     // 예약번호
    private Long customerId;                        // 수요자 아이디
    private LocalDate reservationCreatedAt;         // 신청 날짜
    private String reservationDate;                 // 예약 날짜
    private Time reservationTime;                   // 예약 시간
    private Long categoryId;                        // 카테고리 번호
    private String categoryName;                    // 카테고리 이름
    private short reservationDuration;              // 서비스 제공 시간
    private Long managerId;                         // 매니저 아이디
    private LocalDateTime managerAcceptTime;        // 매니저 수락 시간
    private ReservationStatus reservationStatus;    // 예약 상태
    private String reservationCancelReason;         // 예약 취소 사유
    private String reservationMeno;                 // 추가 요청 사항
    private Integer reservationAmount;              // 최종 가격
}
