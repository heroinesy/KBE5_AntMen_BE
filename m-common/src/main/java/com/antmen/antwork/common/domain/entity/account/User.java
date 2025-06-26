package com.antmen.antwork.common.domain.entity.account;

import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.reservation.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private UserRole userRole;

        @Column(nullable = false)
        private String userLoginId;

        @Column(nullable = true)
        private String userPassword;

        @Column(nullable = false)
        private String userName;

        @Column(nullable = false)
        private String userTel;

        @Column(nullable = false)
        private String userEmail;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private UserGender userGender;

        @Column(nullable = false)
        private LocalDate userBirth;

        @Column(nullable = false)
        private String userProfile;

        @Column(nullable = true)
        private String userType;

        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime userCreatedAt;

        private LocalDateTime lastLoginAt;

        private LocalDateTime lastReservationAt;

        @Column(nullable = false)
        private Boolean isBlack;

        @OneToMany(mappedBy = "reviewCustomer", fetch = FetchType.LAZY)
        private List<Review> reviews;

        @OneToMany(mappedBy = "reviewManager", fetch = FetchType.LAZY)
        private List<Review> reviewManager;

        // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
        // private List<Reservation> customerReservations;

        // @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
        // private List<Reservation> managerReservations;
}
