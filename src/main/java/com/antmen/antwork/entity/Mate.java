package com.antmen.antwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mate")
@Getter
@Setter
@NoArgsConstructor
public class Mate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(name = "bio")
    private String bio;

    @Column(name = "service_area")
    private String serviceArea;

    @Column(name = "rating")
    private float rating;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "service_time_st")
    private LocalDateTime serviceTimest;

    @Column(name = "service_time_end")
    private LocalDateTime serviceTimeEnd;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "password")
    private String password; // 해시된 값만 저장되도록 주의

    @Column(name = "email")
    private String email;

    @Column(name = "black_list")
    private Integer blackList; // 추후 boolean 또는 별도 엔티티로 리팩토링 가능

    @Column(name = "point_balance")
    private int pointBalance;

    @Column(name = "available_area")
    private String availableArea;

    @Column(name = "allow_intervention")
    private boolean allowIntervention;

    @OneToMany(mappedBy = "mate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
