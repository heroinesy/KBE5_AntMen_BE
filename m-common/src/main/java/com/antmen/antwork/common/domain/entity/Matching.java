package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matching")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(nullable = false)
    private Integer matchingPriority;

    private LocalDateTime matchingRequestAt;

    private Boolean matchingManagerIsAccept;

    private Boolean matchingIsFinal;    // 예약 ID 1개에 true 값이 1개여야함.

    private String matchingRefuseReason;
}
