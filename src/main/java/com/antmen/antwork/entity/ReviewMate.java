package com.antmen.antwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ReviewMate")
@Getter
@Setter
@NoArgsConstructor
public class ReviewMate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //@ManyToOne 관계성 수정 필요
    @Column(name = "user_id")
    private User user;

    //@ManyToOne
    @Column(name = "mate_id")
    private Mate mate;

    //@ManyToOne
    @Column(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "rating")
    private Byte rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "reg_date", updatable = false)
    private LocalDateTime reg_date;

    @Column(name = "mod_date")
    private LocalDateTime mod_date;
}
