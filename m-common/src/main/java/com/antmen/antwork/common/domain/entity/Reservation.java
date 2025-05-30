package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer; // 수요자 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager; // 매니저 아이디 (매칭이 되기 전까지는 null)

    @Column(nullable = false)
    private LocalDate reservationCreatedAt; // 신청 날짜

    @Column(nullable = false)
    private String reservationDate; // 예약 날짜

    @Column(nullable = false)
    private Time reservationTime; // 예약 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private short reservationDuration; // 서비스 제공 시간

    private LocalDateTime managerAcceptTime; // 매니저 수락 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus; // 예약 상태

    private String reservationCancelReason; // 예약 취소 사유

    @Column(columnDefinition = "TEXT")
    private String reservationMemo; // 예약 추가 요청 사항

    @Column(nullable = false)
    private Integer reservationAmount; // 최종가격 (카테고리 시간*카테고리 가격+a)

    @OneToMany(
            mappedBy = "reservation",
            cascade = CascadeType.ALL
    )
    private List<Matching> matchings = new ArrayList<>();

    @OneToMany(
            mappedBy = "reservation",
            cascade = CascadeType.ALL
    )
    private List<Review> reviews = new ArrayList<>();


    @OneToOne(
            mappedBy = "reservation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private ReservationComment reservationComment;

}
