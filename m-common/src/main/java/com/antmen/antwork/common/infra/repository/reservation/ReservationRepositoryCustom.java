package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.api.response.reservation.ReservationMatchingListDto;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate reservatedAt);
}
