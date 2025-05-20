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
    @Column(name = "id")
    private Integer id;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "service_area")
    private String serviceArea;

    @Column(name = "rating")
    private float rating;

    @Column(name = "is_avaliable")
    private boolean isAvailiable;

    @Column(name = "service_time_st")
    private LocalDateTime serviceTimest;

    @Column(name = "service_time_end")
    private LocalDateTime serviceTimeEnd;

    @Column(name = "is_apporved")
    private Boolean isApproved;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private LocalDateTime approvedBy;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private char gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "black_list")
    private Integer blackList; //blacklist 객체 생성

    @Column(name = "point_balance")
    private int pointBalance;

    @Column(name = "available_area")
    private String availableArea;

    @Column(name = "allow_intervention")
    private boolean allowIntervention;

    @OneToMany(mappedBy = "mate")
    private List<Reservation> reservations = new ArrayList<>();
}
