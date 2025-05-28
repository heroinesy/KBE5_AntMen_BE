package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

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

        @Column(nullable = false)
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

        @OneToMany(mappedBy = "boardUser", fetch = FetchType.LAZY)
        private List<Board> boards;

        @OneToMany(mappedBy = "commentUser", fetch = FetchType.LAZY)
        private List<Comment> comments;

        @OneToMany(mappedBy = "alertUser", fetch = FetchType.LAZY)
        private List<Alert> alerts;

        @OneToMany(mappedBy = "reviewCustomer", fetch = FetchType.LAZY)
        private List<Review> reviews;

    @OneToMany(
            mappedBy = "reviewManager",
            fetch = FetchType.LAZY
    )
    private List<Review> reviewsManager;

        @OneToMany(mappedBy = "reviewManager", fetch = FetchType.LAZY)
        private List<Review> reviewManager;

        @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
        private List<Reservation> customerReservations;

        @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
        private List<Reservation> managerReservations;
}
