package com.antmen.antwork.domain.review.entity;








@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User reviewCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User reviewManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Short reviewRating;

    private String reviewComment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewAuthorType reviewAuthor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reviewDate;

}


