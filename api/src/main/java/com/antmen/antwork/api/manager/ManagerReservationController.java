package com.antmen.antwork.api.manager;

import com.antmen.antwork.core.security.dto.AuthUserDto;
import com.antmen.antwork.domain.reservation.dto.*;
import com.antmen.antwork.domain.reservation.service.ReservationCommentService;
import com.antmen.antwork.domain.reservation.service.ReservationService;
import com.antmen.antwork.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/manager/reservations")
@RequiredArgsConstructor
public class ManagerReservationController {
    private final ReservationService reservationService;
    private final ReviewService reviewService;
    private final ReservationCommentService reservationCommentService;

    /**
     * 매니저 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        Long loginManagerId = authUserDto.getUserIdAsLong();
        List<ReservationResponseDto> reservations = reservationService.getReservationsByManager(loginManagerId);

        return ResponseEntity.ok(reservations.stream()
                .map(reservation -> {
                    boolean hasReview = reviewService.existsByReservationIdAndAuthorId(
                            reservation.getReservationId(), loginManagerId
                    );
                    reservation.setHasReview(hasReview);
                    return reservation;
                })
                .collect(Collectors.toList()));
    }

    /**
     * 예약 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        ReservationResponseDto responseDto = reservationService.getReservation(id);
        return ResponseEntity.ok(responseDto);
    }

    // 예약 상세 내역 조회 (주소 + 매칭 포함)
    @GetMapping("/{id}/history")
    public ResponseEntity<ReservationHistoryDto> getReservationDetail(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationDetail(id));
    }

    /**
     * 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeReservationStatus(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @PathVariable Long id,
            @RequestBody ReservationStatusChangeRequestDto dto) {
        Long loginManagerId = authUserDto.getUserIdAsLong();
        reservationService.changeStatusByManager(id, loginManagerId, dto.getStatus());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<Void> checkin(
            @PathVariable Long id) {
        reservationCommentService.checkIn(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/check-out")
    public ResponseEntity<Void> checkOut(
            @PathVariable Long id,
            @RequestBody CheckOutRequestDto dto
    ) {
        reservationCommentService.checkOut(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<ReservationCommentResponseDto> getComment(@PathVariable Long id) {
        ReservationCommentResponseDto dto = reservationCommentService.getComment(id);
        return ResponseEntity.ok(dto);
    }
}