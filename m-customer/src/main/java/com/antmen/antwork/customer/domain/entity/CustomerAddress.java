package com.antmen.antwork.customer.domain.entity;

import com.antmen.antwork.common.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String addressName;

    @Column(nullable = false)
    private String addressAddr;

    @Column(nullable = true)
    private String addressDetail;

    @Column(nullable = false)
    private Integer addressArea;

}
