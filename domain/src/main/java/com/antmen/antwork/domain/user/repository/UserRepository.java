package com.antmen.antwork.domain.user.repository;













@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByUserLoginId(String userLoginId);

    @Query("SELECT u FROM User u " +
            "WHERE (:name IS NULL OR u.userName LIKE %:name%) " +
            "AND (:userId IS NULL OR u.userId = :userId) " +
            "AND (:role IS NULL OR u.userRole = :role)")
    List<User> searchUsers(
            @Param("name") String name,
            @Param("userId") Long userId,
            @Param("role") UserRole role
    );

    List<User> findByUserRole(UserRole userRole);

    // 추후에 조건 추가 예정
    @Query("SELECT u.userId FROM User u WHERE u.userRole = 'MANAGER' " +
    "And ((:reservationId IS NULL) OR " +
    "    (u.userId NOT IN ( " +
            "SELECT m.manager.userId FROM Matching m " +
            "WHERE m.reservation.reservationId = :reservationId))) ")
    List<Long> findTop3AvailableManagers(@Param("reservationId") Long reservationId, Pageable pageable);

    User findByUserId(Long id);

    boolean existsByUserLoginId(String loginId);

    // 예약 가능한 매니저
    List<User> findByUserRoleAndUserIdNotIn(UserRole role, List<Long> userIds);

    /**
     * 블랙리스트 회원 조회 (페이징 지원)
     */
    @Query("SELECT u FROM User u " +
            "WHERE u.isBlack = true " +
            "AND (:name IS NULL OR u.userName LIKE %:name%) " +
            "AND (:userRole IS NULL OR u.userRole = :userRole) " +
            "ORDER BY u.userCreatedAt DESC")
    Page<User> findBlacklistUsers(
            @Param("name") String name,
            @Param("userRole") UserRole userRole,
            Pageable pageable
    );
}