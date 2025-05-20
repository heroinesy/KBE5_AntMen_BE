package com.antmen.antwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ReviewUser")
@Getter
@Setter
@NoArgsConstructor
public class ReviewUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

//    @ManyToOne
    @Column(name = "user_id")
    private User user;

//    @ManyToOne
    @Column(name = "mate_id")
    private Mate mate;

    //    @ManyToOne
    @Column(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "rating")
    private Byte rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;


}
