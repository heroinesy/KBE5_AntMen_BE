package com.antmen.antwork.domain.alert.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;

    // user와의 연결 제거
    private Long alertUserId;

    @Column(nullable = false)
    private String alertContent;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isRead;

    @Column(nullable = false)
    private String alertTrigger;

    private String redirectUrl;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}