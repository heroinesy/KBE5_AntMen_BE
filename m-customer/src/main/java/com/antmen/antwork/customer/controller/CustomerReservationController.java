package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.reservation.ReservationCancelRequestDto;
import com.antmen.antwork.common.api.request.reservation.ReservationRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationOptionResponseDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer/reservations")
public class CustomerReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 생성
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 예약 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(
            @PathVariable Long id) {
        ReservationResponseDto responseDto = reservationService.getReservation(id);
        return ResponseEntity.ok(responseDto);
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
        return ResponseEntity.ok(reservations);
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
}
