package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private UserRole userRole;

    private String userLoginId;

    private String userPassword;

    private String userName;

    private String userTel;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private UserGender userGender;

    private LocalDate userBirth;

    private String userProfile;

    private String userType;

    @CreatedDate
    private LocalDateTime userCreatedAt;

}
