package com.antmen.antwork.domain.user.repository;








@Repository
public interface ManagerIdFileRepository extends JpaRepository<ManagerIdFile, Long> {
    List<ManagerIdFile> findAllByUser(User user);

    List<ManagerIdFile> findAllByUser_UserId(Long userId);
}
