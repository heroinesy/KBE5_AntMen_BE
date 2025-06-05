package com.antmen.antwork.common.domain.entity.account;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetail {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer customerPoint;
}
