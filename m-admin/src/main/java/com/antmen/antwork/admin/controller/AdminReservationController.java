package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.request.reservation.ReservationStatusChangeRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeReservationStatusByAdmin(
            @PathVariable Long id,
            @RequestBody ReservationStatusChangeRequestDto dto
    ) {
        reservationService.changeStatusByAdmin(id, dto.getStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * 전체 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}