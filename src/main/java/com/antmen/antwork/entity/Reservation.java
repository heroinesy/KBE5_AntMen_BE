package com.antmen.antwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "address")
    private String address;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @ManyToOne
    @JoinColumn(name = "mate_id")
    private Mate mate;

    // Status Enum 정의
    public enum Status {
        // 예시 값, 실제 값에 맞게 수정
        PENDING, CONFIRMED, CANCELLED
    }

}
