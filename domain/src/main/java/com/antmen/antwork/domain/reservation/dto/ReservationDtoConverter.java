package com.antmen.antwork.domain.reservation.dto;

import com.antmen.antwork.domain.matching.dto.MatchingDto;
import com.antmen.antwork.domain.matching.entity.Matching;
import com.antmen.antwork.domain.matching.repository.MatchingRepository;
import com.antmen.antwork.domain.reservation.entity.Reservation;
import com.antmen.antwork.domain.reservation.entity.ReservationOption;
import com.antmen.antwork.domain.reservation.repository.ReservationOptionRepository;
import com.antmen.antwork.domain.review.entity.ReviewSummary;
import com.antmen.antwork.domain.review.repository.ReviewSummaryRepository;
import com.antmen.antwork.domain.user.dto.UserSummaryDto;
import com.antmen.antwork.domain.user.entity.UserRole;
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
    private final ReviewSummaryRepository reviewSummaryRepository;

    public ReservationHistoryDto toDto(Reservation reservation) {
        List<ReservationOption> options = reservationOptionRepository.findByReservation_ReservationId(reservation.getReservationId());
        List<Matching> matchings = matchingRepository.findAllByReservation_ReservationId(reservation.getReservationId());

        ReviewSummary customerSummary = reviewSummaryRepository.findByUserIdAndRole(reservation.getCustomer().getUserId(), UserRole.CUSTOMER)
                .orElse(null);
        ReviewSummary managerSummary = reservation.getManager() != null
                ? reviewSummaryRepository.findByUserIdAndRole(reservation.getManager().getUserId(), UserRole.MANAGER)
                .orElse(null) : null;
        String fullAddress = reservation.getAddress().getAddressAddr() + " " + reservation.getAddress().getAddressDetail();

        return ReservationHistoryDto.builder()
                .reservationId(reservation.getReservationId())
                .categoryName(reservation.getCategory().getCategoryName())
                .reservationStatus(reservation.getReservationStatus().name())
                .reservationDate(reservation.getReservationDate().toString())
                .reservationTime(reservation.getReservationTime().toString())
                .totalDuration(reservation.getReservationDuration())
                .totalAmount(reservation.getReservationAmount())
                .reservationMemo(reservation.getReservationMemo())
                .reservationCancelReason(reservation.getReservationCancelReason())
                .customer(UserSummaryDto.from(reservation.getCustomer(), customerSummary))
                .manager(reservation.getManager() != null ? UserSummaryDto.from(reservation.getManager(),managerSummary) : null)
                .address(fullAddress)
                .selectedOptions(options.stream()
                        .map(o -> o.getCategoryOption().getCoName())
                        .collect(Collectors.toList()))
                .matchings(matchings.stream()
                        .map(matching -> {
                            ReviewSummary summary = reviewSummaryRepository.findByUserIdAndRole(matching.getManager().getUserId(), UserRole.MANAGER).orElse(null);
                            return MatchingDto.from(matching, summary);})
                        .collect(Collectors.toList()))
                .build();
    }

    public Page<ReservationHistoryDto> convertToDtos(Page<Reservation> reservations) {
        return reservations.map(this::toDto);
    }
}