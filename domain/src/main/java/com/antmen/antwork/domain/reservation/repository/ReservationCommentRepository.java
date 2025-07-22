package com.antmen.antwork.domain.reservation.repository;

import com.antmen.antwork.domain.reservation.entity.ReservationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationCommentRepository extends JpaRepository<ReservationComment, Long> {
}