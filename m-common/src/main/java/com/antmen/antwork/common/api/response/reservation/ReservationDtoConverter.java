package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationOption;
import com.antmen.antwork.common.infra.repository.reservation.MatchingRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationDtoConverter {
    private final ReservationOptionRepository reservationOptionRepository;
    private final MatchingRepository matchingRepository;

    public ReservationHistoryDto toDto(Reservation reservation) {
        List<ReservationOption> options = reservationOptionRepository.findByReservation_ReservationId(reservation.getReservationId());
        List<Matching> matchings = matchingRepository.findAllByReservation_ReservationId(reservation.getReservationId());

        String fullAddress = reservation.getAddress().getAddressAddr() + " " + reservation.getAddress().getAddressDetail();

        return ReservationHistoryDto.builder()
                .reservationId(reservation.getReservationId())
                .categoryName(reservation.getCategory().getCategoryName())
                .reservationStatus(reservation.getReservationStatus().name())
                .reservationDate(reservation.getReservationDate().toString())
                .totalDuration(reservation.getReservationDuration())
                .totalAmount(reservation.getReservationAmount())
                .customer(UserSummaryDto.from(reservation.getCustomer()))
                .manager(reservation.getManager() != null ? UserSummaryDto.from(reservation.getManager()) : null)
                .address(fullAddress)
                .selectedOptions(options.stream()
                        .map(o -> o.getCategoryOption().getCoName())
                        .collect(Collectors.toList()))
                .matchings(matchings.stream()
                        .map(MatchingDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public Page<ReservationHistoryDto> convertToDtos(Page<Reservation> reservations) {
        return reservations.map(this::toDto);
    }
}