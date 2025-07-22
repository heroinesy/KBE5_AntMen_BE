package com.antmen.antwork.domain.board.repository;












@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    Optional<Board> findByBoardId(Long boardId);
//    List<Board> findAllByBoardTypeAndIsPinnedIsTrue(String boardType);

    interface DailyBoardProjection {
        LocalDate getCreatedDate();
        Long getDailyCustomerInquiries();
        Long getDailyManagerInquiries();
    }
    
    @Query("""
    SELECT
        FUNCTION('DATE', b.boardCreatedAt) AS createdDate,
        SUM(CASE WHEN b.boardType = 'customer' THEN 1 ELSE 0 END) AS dailyCustomerInquiries,
        SUM(CASE WHEN b.boardType = 'manager' THEN 1 ELSE 0 END) AS dailyManagerInquiries
    FROM Board b
    WHERE b.boardCreatedAt >= :startDate
        AND b.boardType IN ('customer', 'manager')
    GROUP BY FUNCTION('DATE', b.boardCreatedAt)
    ORDER BY FUNCTION('DATE', b.boardCreatedAt) ASC
    """)
    List<DailyBoardProjection> getDailyBoardStatistics(@Param("startDate") LocalDateTime startDate);
}