package com.antmen.antwork.common.domain.entity.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "calculation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calculationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Float amount;

    private boolean status;

}
