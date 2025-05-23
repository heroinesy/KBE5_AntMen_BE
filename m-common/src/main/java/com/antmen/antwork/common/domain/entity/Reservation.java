package com.antmen.antwork.common.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long reservationId; // 예약 날짜

    @Column(nullable = false)
    private Long customerId; // 수요자 아이디

    @Column(nullable = false)
    private LocalDate reservationCreatedAt; // 신청 날짜

    @Column(nullable = false)
    private String reservationDate; // 예약 날짜

    @Column(nullable = false)
    private Time reservationTime; // 예약 시간

    @Column(nullable = false)
    private Long categoryId; // 카테고리 번호

    @Column(nullable = false)
    private short reservationDuration; // 서비스 제공 시간

    private Long managerId; // 매니저 아이디 (매칭이 되기 전까지는 null)

    private LocalDateTime managerAcceptTime; // 매니저 수락 시간

    @Column(nullable = false)
    private String reservationStatus; // 예약 상태

    private String reservationCancelReason; // 예약 취소 사유

    @Column(columnDefinition = "TEXT")
    private String reservationMeno; // 예약 추가 요청 사항
}
