package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    List<Matching> findAllByMatchingManagerIsAcceptIsNullAndMatchingRequestAtBefore(LocalDateTime time);
}
