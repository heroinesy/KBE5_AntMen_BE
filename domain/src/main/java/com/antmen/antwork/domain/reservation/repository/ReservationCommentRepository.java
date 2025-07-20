package com.antmen.antwork.domain.reservation.repository;

import com.antmen.antwork.common.domain.entity.reservation.ReservationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationCommentRepository extends JpaRepository<ReservationComment, Long> {
}