package com.antmen.antwork.domain.user.repository;

import com.antmen.antwork.domain.user.entity.ManagerIdFile;
import com.antmen.antwork.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerIdFileRepository extends JpaRepository<ManagerIdFile, Long> {
    List<ManagerIdFile> findAllByUser(User user);

    List<ManagerIdFile> findAllByUser_UserId(Long userId);
}
