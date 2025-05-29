package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private Long categoryPrice;

    @Column(nullable = false)
    private Short categoryTime;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryOption> options = new ArrayList<>();
}
