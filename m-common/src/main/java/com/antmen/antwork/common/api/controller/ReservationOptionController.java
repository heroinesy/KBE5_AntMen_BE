package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.response.reservation.ReservationOptionResponseDto;
import org.springframework.web.bind.annotation.*;
import com.antmen.antwork.common.service.serviceReservation.ReservationOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation-option")
public class ReservationOptionController {

    private final ReservationOptionService reservationOptionService;

    /**
     * 예약 옵션 저장
     */
    @PostMapping("/{reservationId}")
    public ResponseEntity<Void> saveOptions(
            @PathVariable Long reservationId,
            @RequestBody List<Long> categoryOptionIds) {
        reservationOptionService.saveReservationOptions(reservationId, categoryOptionIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 예약 ID로 옵션 ID 리스트 조회
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<List<ReservationOptionResponseDto>> getReservationOptions(
            @PathVariable Long reservationId) {
        List<ReservationOptionResponseDto> result =
                reservationOptionService.getReservationOptionDtos(reservationId);
        return ResponseEntity.ok(result);
    }
}
