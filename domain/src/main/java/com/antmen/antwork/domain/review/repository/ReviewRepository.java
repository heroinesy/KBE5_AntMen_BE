package com.antmen.antwork.domain.review.repository;











@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType reviewAuthor, Long reviewCustomerUserId);

    List<Review> findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType reviewAuthorType, Long loginId);

    boolean existsByReservation_ReservationIdAndReviewAuthorAndReviewCustomer_UserId(Long reservationId,ReviewAuthorType reviewAuthortype, Long loginId);
    boolean existsByReservation_ReservationIdAndReviewAuthorAndReviewManager_UserId(Long reservationId, ReviewAuthorType reviewAuthorType, Long loginId);


    List<Review> findAllByReservation(Reservation reservation);

    /**
     * 매니저의 리뷰 수 계산
     */
    @Query("""
        SELECT COUNT(r)
        FROM Review r
        WHERE r.reviewManager.userId = :managerId
            AND r.reviewAuthor = 'CUSTOMER'
    """)
    Long countReviewsByManager(@Param("managerId") Long managerId);

    /**
     * 여러 매니저의 리뷰 수를 한 번에 계산 (최적화용)
     */
    @Query("""
        SELECT r.reviewManager.userId, COUNT(r)
        FROM Review r
        WHERE r.reviewManager.userId IN :managerIds
            AND r.reviewAuthor = 'CUSTOMER'
        GROUP BY r.reviewManager.userId
    """)
    List<Object[]> countReviewsByManagers(@Param("managerIds") List<Long> managerIds);

    Review findByReservation_ReservationIdAndReviewAuthor(Long reservationId, ReviewAuthorType reviewAuthorType);
}
