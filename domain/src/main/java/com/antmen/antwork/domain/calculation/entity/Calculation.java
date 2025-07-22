package com.antmen.antwork.domain.calculation.entity;









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
    private User manager;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer amount;

    private String reservationIds;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CalculationStatus status;

    @CreationTimestamp
    private LocalDateTime requestedAt;
}
