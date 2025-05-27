package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cgId;

    @Column(nullable = false)
    private Long reservationId;

    @Column(nullable = false)
    private Long categoryId;
} 