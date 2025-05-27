package com.antmen.antwork.common.api;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.request.ReservationStatusChangeRequestDto;
import com.antmen.antwork.common.api.request.ReservationCancelRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto requestDto) {
        try {
            ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 예외 처리 (간단 예시)
            return ResponseEntity.badRequest().build();
        }
    }

    // 예약 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable Long id) {
        try {
            ReservationResponseDto responseDto = reservationService.getReservation(id);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 예외 처리 (간단 예시)
            return ResponseEntity.notFound().build();
        }
    }

    // 예약 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestBody ReservationStatusChangeRequestDto dto) {
        try {
            reservationService.changeStatus(id, dto.getStatus());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 예약 취소
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id, @RequestBody ReservationCancelRequestDto dto) {
        try {
            reservationService.cancelReservation(id, dto.getCancelReason());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}