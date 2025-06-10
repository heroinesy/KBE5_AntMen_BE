package com.antmen.antwork.common.domain.entity.account;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manager_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerDetail {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String managerAddress;

    @Column(nullable = false)
    private String managerArea;  // Json

    @Column(nullable = false)
    private String managerTime; // Json

    @Column(nullable = false)
    private ManagerStatus managerStatus = ManagerStatus.WAITING;

    private String rejectReason;
}
