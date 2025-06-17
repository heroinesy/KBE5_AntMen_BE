package com.antmen.antwork.common.domain.entity.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewAuthorType reviewAuthor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reviewDate;

}


