package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.reservation.ReservationCancelRequestDto;
import com.antmen.antwork.common.api.request.reservation.ReservationRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationHistoryDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import com.antmen.antwork.common.service.serviceReservation.ReviewService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer/reservations")
public class CustomerReservationController {
    private final ReservationService reservationService;
    private final ReviewService reviewService;

    /**
     * 예약 생성
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 예약 단건 조회
     * 예약 폼 -> 예약 확인 페이지용
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(
            @PathVariable Long id) {
        ReservationResponseDto responseDto = reservationService.getReservation(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 예약 상세 내역 조회 (주소 + 매칭 포함)
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<ReservationHistoryDto> getReservationDetail(
            @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationDetail(id));
    }

    /**
     * 내 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long loginUserId = authUserDto.getUserIdAsLong();
        List<ReservationResponseDto> reservations = reservationService.getReservationsByCustomer(loginUserId);
        return ResponseEntity.ok(reservations.stream()
                .map(reservation -> {
                    boolean hasReview = reviewService.existsByReservationIdAndAuthorId(
                            reservation.getReservationId(), loginUserId
                    );
                    reservation.setHasReview(hasReview);
                    return reservation;
                })
                .collect(Collectors.toList()));
    }

    /**
     * 예약 취소
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @RequestBody ReservationCancelRequestDto dto) {
        reservationService.cancelReservation(id, dto.getCancelReason());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manager/{id}")
    public ResponseEntity showManagerDetail(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationService.getManagerDetail(id));
    }


}
