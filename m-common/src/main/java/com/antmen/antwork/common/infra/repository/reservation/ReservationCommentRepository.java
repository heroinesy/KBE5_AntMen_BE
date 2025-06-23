package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.ReservationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationCommentRepository extends JpaRepository<ReservationComment, Long> {
}