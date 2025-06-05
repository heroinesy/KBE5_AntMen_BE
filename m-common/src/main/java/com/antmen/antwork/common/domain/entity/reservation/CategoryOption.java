package com.antmen.antwork.common.domain.entity.reservation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String coName;

    @Column(nullable = false)
    private Integer coPrice;

    @Column(nullable = false)
    private Short coTime;

}